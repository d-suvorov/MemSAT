package test.finals;

import static com.ibm.wala.memsat.util.Graphs.graph;
import static test.TestUtil.threadMethods;

import java.io.File;
import java.util.Set;

import org.junit.Test;

import com.ibm.wala.memsat.Miniatur;

import data.finals.SimplePublication01;
import data.finals.SimplePublication02;
import data.finals.LeakedReferences01;
import data.finals.LeakedReferences02;
import data.finals.LeakedReferences03;
import data.finals.LeakedReferences04;
import data.finals.LeakedReferences05;
import data.finals.LeakedReferences06;
import data.finals.NestedCtors01;
import data.finals.NestedCtors02;
import data.finals.SimplePublicationObject0;
import data.finals.SimplePublicationObject1;
import data.finals.Reachability00;
import data.finals.Reachability01;
import test.ConcurrentTests;

public abstract class FinalsTests extends ConcurrentTests {
	private static final File FINALS_TESTS = new File("source/data/finals");

	private final Set<String> sat;

	protected FinalsTests(Set<String> sat) { this.sat = sat; }

	final void test(Miniatur miniatur, Class<?> testCase, boolean sat) {
		test(miniatur, FINALS_TESTS, graph(threadMethods("p", testCase)), sat);
	}

	private final boolean sat(String test) { return sat.contains(test); }

	@Test
	public final void simplePublication01() {
		test(miniatur(4), SimplePublication01.class, sat("01"));
	}
	
	@Test
	public final void simplePublication02() {
		test(miniatur(4), SimplePublication02.class, sat("02"));
	}
	
	@Test
	public final void leakedReferences01() {
		test(miniatur(4), LeakedReferences01.class, sat("03"));
	}
	
	@Test
	public final void leakedReferences02() {
		test(miniatur(4), LeakedReferences02.class, sat("04"));
	}
	
	@Test
	public final void leakedReferences03() {
		test(miniatur(4), LeakedReferences03.class, sat("05"));
	}
	
	@Test
	public final void leakedReferences04() {
		test(miniatur(4), LeakedReferences04.class, sat("06"));
	}
	
	@Test
	public final void leakedReferences05() {
		test(miniatur(4), LeakedReferences05.class, sat("07"));
	}
	
	@Test
	public final void leakedReferences06() {
		test(miniatur(4), LeakedReferences06.class, sat("08"));
	}
	
	@Test
	public final void nestedCtors01() {
		test(miniatur(4), NestedCtors01.class, sat("09"));
	}
	
	@Test
	public final void nestedCtors02() {
		test(miniatur(4), NestedCtors02.class, sat("10"));
	}
	
	@Test
	public final void simplePublicationObject0() {
		test(miniatur(4), SimplePublicationObject0.class, sat("11"));
	}
	
	@Test
	public final void simplePublicationObject1() {
		test(miniatur(4), SimplePublicationObject1.class, sat("12"));
	}
	
	@Test
	public final void reachability00() {
		test(miniatur(5), Reachability00.class, false);
	}
	
	@Test
	public final void reachability01() {
		test(miniatur(5), Reachability01.class, true);
	}
}
