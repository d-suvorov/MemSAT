package test.arith;

import static com.ibm.wala.memsat.util.Graphs.graph;
import static test.TestUtil.threadMethods;

import java.io.File;

import org.junit.Test;

import com.ibm.wala.memsat.Miniatur;

import data.arith.Arithmetic;
import data.arith.Relational;
import data.arith.Logical;
import data.arith.Bitwise;
import test.ConcurrentTests;

public abstract class ArithTests extends ConcurrentTests {
	private static final File ARITH_TESTS = new File("source/data/arith");

	final void test(Miniatur miniatur, Class<?> testCase, boolean sat) {
		test(miniatur, ARITH_TESTS, graph(threadMethods("p", testCase)), sat);
	}
	
	private Miniatur getMiniatur()  {
		Miniatur mini = miniatur(4);
		mini.options().kodkodOptions().setBitwidth(8);
		return mini;
	}
	
	@Test
	public final void testArithmetic() {
		test(getMiniatur(), Arithmetic.class, true);
	}
	
	@Test
	public final void testRelational() {
		test(getMiniatur(), Relational.class, true);
	}
	
	@Test
	public final void testLogical() {
		test(getMiniatur(), Logical.class, true);
	}
	
	@Test
	public final void testBitwise() {
		test(getMiniatur(), Bitwise.class, true);
	}
}
