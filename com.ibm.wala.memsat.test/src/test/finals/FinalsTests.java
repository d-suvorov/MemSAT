package test.finals;

import static com.ibm.wala.memsat.util.Graphs.graph;
import static test.TestUtil.threadMethods;

import java.io.File;
import java.util.Set;

import org.junit.Test;

import com.ibm.wala.memsat.Miniatur;

import data.finals.Test01;
import data.finals.Test02;
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
	public final void test01() {
		test(miniatur(4), Test01.class, sat("01"));
	}
	
	@Test
	public final void test02() {
		test(miniatur(4), Test02.class, sat("02"));
	}
}
