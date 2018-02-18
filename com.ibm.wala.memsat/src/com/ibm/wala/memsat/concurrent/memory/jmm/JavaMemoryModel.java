/******************************************************************************
 * Copyright (c) 2009 - 2015 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *****************************************************************************/
/**
 * 
 */
package com.ibm.wala.memsat.concurrent.memory.jmm;

import static com.ibm.wala.memsat.frontEnd.InlinedInstruction.Action.END;
import static com.ibm.wala.memsat.frontEnd.InlinedInstruction.Action.LOCK;
import static com.ibm.wala.memsat.frontEnd.InlinedInstruction.Action.NORMAL_READ;
import static com.ibm.wala.memsat.frontEnd.InlinedInstruction.Action.NORMAL_WRITE;
import static com.ibm.wala.memsat.frontEnd.InlinedInstruction.Action.START;
import static com.ibm.wala.memsat.frontEnd.InlinedInstruction.Action.UNLOCK;
import static com.ibm.wala.memsat.frontEnd.InlinedInstruction.Action.VOLATILE_READ;
import static com.ibm.wala.memsat.frontEnd.InlinedInstruction.Action.VOLATILE_WRITE;
import static com.ibm.wala.memsat.util.Graphs.reflexive;
import static com.ibm.wala.memsat.util.Graphs.restrict;
import static com.ibm.wala.memsat.util.Programs.executionOrder;
import static com.ibm.wala.memsat.util.Programs.filter;
import static com.ibm.wala.memsat.util.Programs.instructions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.ibm.wala.classLoader.NewSiteReference;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.memsat.concurrent.Execution;
import com.ibm.wala.memsat.concurrent.Justification;
import com.ibm.wala.memsat.concurrent.MemoryModel;
import com.ibm.wala.memsat.concurrent.Program;
import com.ibm.wala.memsat.concurrent.Program.BoundsBuilder;
import com.ibm.wala.memsat.concurrent.memory.AbstractExecution;
import com.ibm.wala.memsat.frontEnd.InlinedInstruction;
import com.ibm.wala.memsat.frontEnd.WalaConcurrentInformation;
import com.ibm.wala.memsat.frontEnd.WalaInformation;
import com.ibm.wala.memsat.frontEnd.InlinedInstruction.Action;
import com.ibm.wala.memsat.util.Graphs;
import com.ibm.wala.memsat.util.Programs;
import com.ibm.wala.ssa.SSAFieldAccessInstruction;
import com.ibm.wala.types.FieldReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.util.collections.Pair;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.util.graph.impl.SlowSparseNumberedGraph;

import edu.emory.mathcs.backport.java.util.Arrays;
import kodkod.ast.Expression;
import kodkod.ast.Formula;
import kodkod.ast.IntConstant;
import kodkod.ast.Relation;
import kodkod.ast.Variable;
import kodkod.instance.Bounds;
/**
 * A base implementation of the Java Memory Model.
 * Subclasses can override implementations of 
 * individual rules.
 * 
 * @author etorlak
 */
public abstract class JavaMemoryModel implements MemoryModel {
	
	private final Set<MethodReference> externs;
	
	final int maxSpeculations;
	
	/**
	 * Constructs an instance of the JavaMemoryModel that will use 
	 * at most the given number of speculations to justify a program
	 * and that will consider calls to the specified methods as 
	 * external instructions.
	 * @effects this.memInstructions' = memInsts
	 * @requires maxSpeculations > 0
	 */
	protected JavaMemoryModel(int maxSpeculations, Set<MethodReference> memInsts) { 
		assert maxSpeculations > 0;
		this.maxSpeculations = maxSpeculations;
		if (memInsts.isEmpty())
			this.externs = Collections.emptySet();
		else
			this.externs = Collections.unmodifiableSet(new LinkedHashSet<MethodReference>(memInsts));
	}
	
	/**
	 * Returns the bounds for the justification of the given (main) execution of the given program,
	 * using the provided speculations and commits.
	 * @return bounds for the justification of the given (main) execution of the given program,
	 * using the provided speculations and commits.
	 */
	@SuppressWarnings("unchecked")
	private final Bounds bounds(Program prog, JMMExecution main, List<JMMExecution> speculations, List<Relation> commits) { 
		final BoundsBuilder builder = prog.builder();
		
		final WalaInformation info = prog.info();
		final Set<InlinedInstruction> all = instructions(info);
		final Graph<InlinedInstruction> so = restrict(reflexive(executionOrder(info)), filter(START,END,VOLATILE_READ,VOLATILE_WRITE,LOCK,UNLOCK));
		
//		assert Graphs.equal(so, Programs.syncOrder(info, EnumSet.of(START,END,VOLATILE_READ,VOLATILE_WRITE,LOCK,UNLOCK)));
		
		builder.boundExecution(main);
		builder.boundOrdering(main.so(), so);
		
		for(JMMExecution exec : speculations) {
			builder.boundExecution(exec);
			builder.boundOrdering(exec.so(), so);
		}
		
		builder.boundActions(commits.get(0), Collections.EMPTY_SET);
		for(int i = 1; i < maxSpeculations; i++) { 
			builder.boundActions(commits.get(i), all);
		}
		
		Graph<InlinedInstruction> dc = dereferenceUpperBound(info);
		builder.boundReflexiveOrdering(main.dc(), dc);
		for (JMMExecution exec : speculations) {
		  builder.boundOrdering(exec.dc(), SlowSparseNumberedGraph.make());
		}
		
		List<Graph<InlinedInstruction>> mcl = new ArrayList<>();
		mcl.add(memoryChainUpperBound(info));
		mcl.add(dc);
		mcl.add(revert(Programs.visibleWrites(info)));
		Graph<InlinedInstruction> mc = Graphs.union(mcl);
		builder.boundReflexiveOrdering(main.mc(), mc);
    for (JMMExecution exec : speculations) {
      builder.boundOrdering(exec.mc(), SlowSparseNumberedGraph.make());
    }
		
		return builder.build();
	}
	
	// TODO Move somewhere
	private <T> Graph<T> revert(Graph<T> g) {
	  Graph<T> res = SlowSparseNumberedGraph.make();
	  for (T n : g) {
	    res.addNode(n);
	  }
	  for (T n : g) {
	    for(Iterator<? extends T> itr = g.getSuccNodes(n); itr.hasNext(); ) { 
        T succ = itr.next();
        res.addEdge(succ, n);
      }
	  }
	  return res;
	}
	
	// TODO initialized by other thread
	private Graph<InlinedInstruction> dereferenceUpperBound(WalaInformation info) {
	  Graph<InlinedInstruction> res = SlowSparseNumberedGraph.make();
    for (CGNode t : info.threads()) {
      WalaConcurrentInformation cInfo = info.concurrentInformation(t);
      Set<InlinedInstruction> actions = cInfo.actions();
      for (InlinedInstruction inst : actions) {
        if (!(inst.instruction() instanceof SSAFieldAccessInstruction))
          continue;
        Set<InstanceKey> instances = Programs.referencedInstance(info, inst);
        for (InlinedInstruction read : actions) {
          if (!read.equals(inst) && isRead(read)) {
            Set<InstanceKey> values = Programs.valueFor(info, read);
            /*values.retainAll(instances);
            boolean sameInstance = !values.isEmpty()*/
            boolean sameInstance = false;
            if (instances.size() == 1 && values.size() == 1) {
              InstanceKey instanceKey = instances.iterator().next();
              sameInstance = instanceKey.equals(values.iterator().next());
            }
            if (sameInstance) {
              res.addNode(read);
              res.addNode(inst);
              res.addEdge(read, inst);
            }
          }
        }
      }
    }
    return res;
	}
	
	private Graph<InlinedInstruction> memoryChainUpperBound(WalaInformation info) {
	  Graph<InlinedInstruction> res = SlowSparseNumberedGraph.make();
	  for (CGNode t : info.threads()) {
      WalaConcurrentInformation cInfo = info.concurrentInformation(t);
      Set<InlinedInstruction> actions = cInfo.actions();
      for (InlinedInstruction inst : actions) {
        if (!isWrite(inst))
          continue;
        Set<InstanceKey> instances = Programs.valueFor(info, inst);
        for (InlinedInstruction read : actions) {
          if (!read.equals(inst) && isRead(read)) {
            Set<InstanceKey> values = Programs.valueFor(info, read);
            /*values.retainAll(instances);
            boolean sameInstance = !values.isEmpty()*/
            boolean sameInstance = false;
            if (instances.size() == 1 && values.size() == 1) {
              InstanceKey instanceKey = instances.iterator().next();
              sameInstance = instanceKey.equals(values.iterator().next());
            }
            if (sameInstance) {
              res.addNode(read);
              res.addNode(inst);
              res.addEdge(read, inst);
            }
          }
        }
      }
    }
	  return res;
	}
	
	private boolean isRead(InlinedInstruction inst) {
	  return inst.action() == NORMAL_READ || inst.action() == VOLATILE_READ;
	}
	
	private boolean isWrite(InlinedInstruction inst) {
    return inst.action() == NORMAL_WRITE || inst.action() == VOLATILE_WRITE;
  }
	
	/**
	 * Returns a sequence of freshly created commit sets.
	 * @return sequence of freshly created commit sets.
	 */
	private final List<Relation> commits() {
		final List<Relation> ret = new ArrayList<Relation>(maxSpeculations);
		for(int i = 0; i < maxSpeculations; i++) { 
			ret.add(Relation.unary("C"+i));
		}
		return ret;
	}
	
	/**
	 * Returns the legality formula for the given program, main execution, speculations and commits.
	 * @requires prog = (main + speculations[int]).prog
	 * @requires commits.size() = speculations.size()
	 * @return the legality formula for the given program, main execution, speculations and commits.
	 */
	private final Formula legal(Program prog, JMMExecution main, List<JMMExecution> speculations, List<Relation> commits) { 
		final Collection<Formula> ret = new ArrayList<Formula>(36);
		
		// all executions are well formed
		ret.add( main.wellFormed() );
		for(int i = 1; i < maxSpeculations; i++) { 
			ret.add( speculations.get(i).wellFormed() );
		}
				
		// C_0 is empty 
		ret.add( commits.get(0).no() );
		// C_i in C_{i+1}
		for(int i = 1; i < maxSpeculations; i++) { 
			ret.add( commits.get(i-1).in(commits.get(i)) );
		}
		// A = union(C_0, ..., C_n)
		ret.add( main.actions().eq(Expression.union(commits)) );
		
		ret.add( locationConsistency(prog, main, speculations, commits) );
		ret.add( monitorConsistency(prog, main, speculations, commits) );
		ret.add( rule1(prog, main, speculations, commits) );
		ret.add( rule2(prog, main, speculations, commits) );
		ret.add( rule3(prog, main, speculations, commits) );
		ret.add( rule4(prog, main, speculations, commits) );
		ret.add( rule5(prog, main, speculations, commits) );
		ret.add( rule6(prog, main, speculations, commits) );
		ret.add( rule7(prog, main, speculations, commits) );
		ret.add( rule8(prog, main, speculations, commits) );
		ret.add( rule9(prog, main, speculations, commits) );
		
		ret.add( partialOrderOver(main.dc(), main.actions()) );
		ret.add( partialOrderOver(main.mc(), main.actions()) );
		ret.add( dc1(prog, main) );
		ret.add( mc1(prog, main) );
		ret.add( mc2(prog, main) );
		ret.add( mc3(prog, main) );
		
		return Formula.and(ret);
	}
	
	/**
	 * Returns the formula C_i <: location_i = C_i <: location for all i.
	 * @requires prog = (main + speculations[int]).prog
	 * @requires commits.size() = speculations.size() 
	 * @return a formula that specifies the location consistency rule.
	 */
	private Formula locationConsistency(Program prog, JMMExecution main, List<JMMExecution> speculations, List<? extends Expression> commits) { 
		final Collection<Formula> ret = new ArrayList<Formula>(maxSpeculations);
		final Expression L = main.location();
		for(int i = 1; i < maxSpeculations; i++) { 
			final Expression C_i = commits.get(i);
			final Expression L_i = speculations.get(i).location();
			final Expression Ci2All = C_i.product(Expression.UNIV);
			ret.add( Ci2All.intersection(L_i).eq(Ci2All.intersection(L)) );
		}
		return Formula.and(ret);
	}
	
	/**
	 * Returns the formula C_i <: monitor_i = C_i <: monitor for all i.
	 * @requires prog = (main + speculations[int]).prog
	 * @requires commits.size() = speculations.size() 
	 * @return a formula that specifies the monitor consistency rule.
	 */
	private Formula monitorConsistency(Program prog, JMMExecution main, List<JMMExecution> speculations, List<? extends Expression> commits) { 
		final Collection<Formula> ret = new ArrayList<Formula>(maxSpeculations);
		final Expression M = main.monitor();
		for(int i = 1; i < maxSpeculations; i++) { 
			final Expression C_i = commits.get(i);
			final Expression M_i = speculations.get(i).monitor();
			final Expression Ci2All = C_i.product(Expression.UNIV);
			ret.add( Ci2All.intersection(M_i).eq(Ci2All.intersection(M)) );
		}
		return Formula.and(ret);
	}
	
	/**
	 * Returns a formula that specifies the first rule of the JMM:  C_i in A_i for all i.
	 * @requires prog = (main + speculations[int]).prog
	 * @requires commits.size() = speculations.size() 
	 * @return a formula that specifies the first rule of the JMM:  C_i in A_i for all i.
	 */
	protected Formula rule1(Program prog, JMMExecution main, List<JMMExecution> speculations, List<? extends Expression> commits) { 
		final Collection<Formula> ret = new ArrayList<Formula>(maxSpeculations);
		for(int i = 1; i < maxSpeculations; i++) { 
			final Expression C_i = commits.get(i);
			final Expression A_i = speculations.get(i).actions();
			ret.add( C_i.in(A_i) );
		}
		return Formula.and(ret);
	}
	
	/**
	 * Returns a formula that specifies the second rule of the JMM, which defines the relationship
	 * between  hb and each hb_i.
	 * @requires prog = (main + speculations[int]).prog
	 * @requires commits.size() = speculations.size()
	 * @return a formula that specifies the second rule of the JMM, which defines the relationship
	 * between  hb and each hb_i.
	 */
	protected abstract Formula rule2(Program prog, JMMExecution main, List<JMMExecution> speculations, List<? extends Expression>  commits);
	
	/**
	 * Returns a formula that specifies the third rule of the JMM, which defines the relationship
	 * between so and each so_i.
	 * @requires prog = (main + speculations[int]).prog
	 * @requires commits.size() = speculations.size()
	 * @return a formula that specifies the second rule of the JMM, which defines the relationship
	 * between so and each so_i.
	 */
	protected abstract Formula rule3(Program prog, JMMExecution main, List<JMMExecution> speculations, List<? extends Expression>  commits);

	/**
	 * Returns a formula that specifies the fourth rule of the JMM:  C_i <: V_i = C_i <: V for all i.
	 * @requires prog = (main + speculations[int]).prog
	 * @requires commits.size() = speculations.size()
	 * @return a formula that specifies the fourth rule of the JMM:  C_i <: V_i = C_i <: V for all i.
	 */
	protected Formula rule4(Program prog, JMMExecution main, List<JMMExecution> speculations, List<? extends Expression>  commits) { 
		final Collection<Formula> ret = new ArrayList<Formula>(maxSpeculations);
		final Expression V = main.v();
		for(int i = 1; i < maxSpeculations; i++) { 
			final Expression C_i = commits.get(i);
			final Expression V_i = speculations.get(i).v();
			final Expression Ci2All = C_i.product(Expression.UNIV);
			ret.add( Ci2All.intersection(V_i).eq(Ci2All.intersection(V)) );
		}
		return Formula.and(ret);
	}
	
	/**
	 * Returns a formula that specifies the fifth rule of the JMM:  C_{i-1} <: W_i = C_{i-1} <: W for all i.
	 * @requires prog = (main + speculations[int]).prog
	 * @requires commits.size() = speculations.size()
	 * @return a formula that specifies the fifth rule of the JMM:  C_{i-1} <: W_i = C_{i-1} <: W for all i. 
	 */
	protected Formula rule5(Program prog, JMMExecution main, List<JMMExecution> speculations, List<? extends Expression>  commits) { 
		final Collection<Formula> ret = new ArrayList<Formula>(maxSpeculations);
		final Expression W = main.w();
		for(int i = 1; i < maxSpeculations; i++) { 
			final Expression C_j = commits.get(i-1);
			final Expression W_i = speculations.get(i).w();
			final Expression Cj2All = C_j.product(Expression.UNIV);
			ret.add( Cj2All.intersection(W_i).eq(Cj2All.intersection(W)) );
		}
		return Formula.and(ret);
	}
	
	/**
	 * Returns a formula that specifies the sixth rule of the JMM:  
	 * for all reads r in A_i - C_{i-1}, W_i(r) <=_{hb_i} r.
	 * @requires prog = (main + speculations[int]).prog
	 * @requires commits.size() = speculations.size()
	 * @return a formula that specifies the sixth rule of the JMM:  for all reads r in A_i - C_{i-1}, W_i(r) <=_{hb_i} r.
	 */
	protected Formula rule6(Program prog, JMMExecution main, List<JMMExecution> speculations, List<? extends Expression>  commits) { 
		final Collection<Formula> ret = new ArrayList<Formula>(maxSpeculations);
		final Expression reads = prog.allOf(NORMAL_READ, VOLATILE_READ);
		for(int i = 1; i < maxSpeculations; i++) { 
			final JMMExecution E_i = speculations.get(i);
			final Expression C_j = commits.get(i-1);
			final Expression A_i = E_i.actions();
			final Expression hb_i = E_i.hb();
			final Expression relevantReads = A_i.difference(C_j).intersection(reads);
			final Variable r = Variable.unary("r"+i);
			ret.add( E_i.w(r).product(r).in(hb_i).forAll(r.oneOf(relevantReads)) );
		}
		return Formula.and(ret);
	}
	
	/**
	 * Returns a formula that specifies the seventh rule of the JMM, which constrains 
	 * the committing of reads and writes.
	 * @requires prog = (main + speculations[int]).prog
	 * @requires commits.size() = speculations.size()
	 * @return a formula that specifies the seventh rule of the JMM, which constrains
	 * on the committing of reads and writes.
	 */
	protected abstract Formula rule7(Program prog, JMMExecution main, List<JMMExecution> speculations, List<? extends Expression>  commits);

	/**
	 * Returns a formula that specifies the eighth rule of the JMM, which constrains the 
	 * committing in the presence of sufficient synchronizes-with edges.
	 * @requires prog = (main + speculations[int]).prog
	 * @requires commits.size() = speculations.size()
	 * @return a formula that specifies the eighth rule of the JMM, which constrains the 
	 * committing in the presence of sufficient synchronizes-with edges.
	 */
	protected abstract Formula rule8(Program prog, JMMExecution main, List<JMMExecution> speculations, List<? extends Expression>  commits);

	/**
	 * Returns a formula that specifies the ninth rule of the JMM, which constrains the 
	 * committing of external actions.
	 * @requires prog = (main + speculations[int]).prog
	 * @requires commits.size() = speculations.size()
	 * @return a formula that specifies the ninth rule of the JMM, which constrains the 
	 * committing of external actions.
	 */
	protected abstract Formula rule9(Program prog, JMMExecution main, List<JMMExecution> speculations, List<? extends Expression>  commits);
	
	protected Formula dc1(Program prog, JMMExecution main) {
	  Variable a = Variable.unary("a"), r = Variable.unary("r");
	  
	  Formula fieldAccess = main.locationOf(a).count().eq(IntConstant.constant(2));
	  Formula initializedByOtherThread = main.locationOf(a).join(prog.constructs())
	    .eq(prog.threadOf(a)).not();
	  Expression valueRead = main.v(main.w(r));
	  Formula seesObject = prog.instances().some().and(valueRead.in(prog.instances()));
	  Formula sameObject = valueRead.in(main.locationOf(a));
	  Formula sameThread = prog.threadOf(a).eq(prog.threadOf(r));
	  
	  Formula existRead = seesObject
	    .and(sameObject).and(sameThread)
	    .and(r.product(a).in(main.dc()))
	      .forSome(r.oneOf(reads(prog)));
	  
	  return fieldAccess.and(initializedByOtherThread).implies(existRead)
	    .forAll(a.oneOf(prog.allOf(
	      NORMAL_READ, NORMAL_WRITE, VOLATILE_READ, VOLATILE_WRITE)));
	}
	
	private Formula partialOrderOver(Relation ord, Expression universe) {
	  Variable a = Variable.unary("a");
	  Variable b = Variable.unary("b");
	  
	  Formula reflexive = a.product(a).in(ord).forAll(a.oneOf(universe));
	  
	  Formula antisymmetric = (a.product(b).in(ord).and(b.product(a).in(ord)))
	    .implies(a.eq(b))
	      .forAll(a.oneOf(universe).and(b.oneOf(universe)));
	  
	  final Expression proj = ord.intersection(universe.product(universe));
	  Formula transitive = proj.join(proj).in(proj);
	  
	  return Formula.and(reflexive, antisymmetric, transitive);
	}
	
	protected Formula mc1(Program prog, JMMExecution main) {
	  Variable w = Variable.unary("w"), r = Variable.unary("r");
	  Formula lhs = r.product(w).in(main.w());
	  Formula rhs = w.product(r).in(main.mc());
	  return lhs.implies(rhs).forAll(
	    w.oneOf(writes(prog)).and(
	      r.oneOf(reads(prog))));
  }
	
	protected Formula mc2(Program prog, JMMExecution main) {
	  Variable r = Variable.unary("r"), a = Variable.unary("a");
	  Formula lhs = r.product(a).in(main.dc());
	  Formula rhs = r.product(a).in(main.mc());
	  return lhs.implies(rhs).forAll(
	    r.oneOf(prog.allOf(Action.values())).and(
	      a.oneOf(prog.allOf(Action.values()))));
	}
	
	protected Formula mc3(Program prog, JMMExecution main) {
	  Variable w = Variable.unary("w"), r = Variable.unary("r");
	  
	  Expression writtenValue = main.v(w);
    Formula writesObject = prog.instances().some().and(writtenValue.in(prog.instances()));
    Formula initializedByOtherThread = writtenValue.join(prog.constructs())
      .eq(prog.threadOf(w)).not();
    Formula sameValue = writtenValue.eq(valuesRead(main, r));
    Formula sameThread = prog.threadOf(w).eq(prog.threadOf(r));
    
    Formula existRead = sameValue.and(sameThread)
      .and(r.product(w).in(main.mc()))
        .forSome(r.oneOf(reads(prog)));
    
    return writesObject.and(initializedByOtherThread).implies(existRead)
      .forAll(w.oneOf(writes(prog)));
	}
	
	private Expression reads(Program prog) {
	  return prog.allOf(NORMAL_READ, VOLATILE_READ);
	}
	
	private Expression writes(Program prog) {
	  return prog.allOf(NORMAL_WRITE, VOLATILE_WRITE);
	}
	
	private Expression valuesRead(AbstractExecution exec, Expression r) {
    return exec.v(exec.w(r));
  }
	
	/**
	 * {@inheritDoc}
	 * @see com.ibm.wala.memsat.concurrent.MemoryModel#justify(com.ibm.wala.memsat.concurrent.Program)
	 */
	public final Justification justify(final Program prog) {
		final List<Relation> commits = commits();
		final List<JMMExecution> speculations = JMMExecution.speculative(prog, maxSpeculations);
		final JMMExecution main = JMMExecution.main(prog);
		
		final Formula formula = legal(prog, main, speculations, commits);
		final Bounds bounds = bounds(prog, main, speculations, commits);
		
		return new Justification() {
			public Program program() { return prog; }
			public Execution execution() { return main; }	
			public List<? extends Expression> commits() { return commits; }
			public List<? extends Execution> speculations() { return speculations; }
			public Formula formula() { return formula; }
			public Bounds bounds() { return bounds; }
		};
	}

	/**
	 * {@inheritDoc}
	 * @see com.ibm.wala.memsat.concurrent.MemoryModel#memoryInstructions()
	 */
	public final Set<MethodReference> memoryInstructions() { return externs; }

	/**
	 * {@inheritDoc}
	 * @see com.ibm.wala.memsat.concurrent.MemoryModel#usesSpeculation()
	 */
	public final boolean usesSpeculation() { return true; }
	
	/**
	 * Returns the maximum number of speculations used by this instance of the
	 * JavaMemoryModel to justify a program.
	 * @return  maximum number of speculations used by this instance of the
	 * JavaMemoryModel to justify a program.
	 */
	public final int maxSpeculations() { return maxSpeculations; }

}
