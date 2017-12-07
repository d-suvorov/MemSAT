package test.interprocedural;

import static com.ibm.wala.memsat.util.Graphs.graph;
import static test.TestUtil.threadMethods;

import java.io.File;
import java.util.Set;

import org.junit.Test;

import com.ibm.wala.memsat.Miniatur;
import data.interprocedural.Test01;
import data.interprocedural.Test02;
import data.interprocedural.Test03;
import data.interprocedural.Test04;
import data.interprocedural.Test05;
import data.interprocedural.Test06;
import test.ConcurrentTests;

public abstract class InterproceduralTests extends ConcurrentTests {
	private static final File INTERPROCEDURAL_TESTS = new File("source/data/interprocedural");

	private final Set<String> sat;

	protected InterproceduralTests(Set<String> sat) { this.sat = sat; }

	final void test(Miniatur miniatur, Class<?> testCase, boolean sat) {
		test(miniatur, INTERPROCEDURAL_TESTS, graph(threadMethods("p", testCase)), sat);
	}

	private final boolean sat(String test) { return sat.contains(test); }

	@Test
	public final void test01() {
		test(miniatur(3), Test01.class, sat("01"));
	}
	
	@Test
	public final void test02() {
		test(miniatur(3), Test02.class, sat("02"));
	}
	
	@Test
	public final void test03() {
		test(miniatur(3), Test03.class, sat("03"));
	}
	
	@Test
	public final void test04() {
		test(miniatur(3), Test04.class, sat("04"));
	}
	
	@Test
	public final void test05() {
		test(miniatur(3), Test05.class, sat("05"));
	}
	
	@Test
	public final void test06() {
		test(miniatur(3), Test06.class, sat("06"));
	}
}
