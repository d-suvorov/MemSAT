package test.finals;

import static com.ibm.wala.memsat.util.Graphs.graph;
import static test.TestUtil.threadMethods;

import java.io.File;
import java.util.Set;

import org.junit.Test;

import com.ibm.wala.memsat.Miniatur;

import data.finals.Test01;
import data.finals.Test02;
import data.finals.Test03;
import data.finals.Test04;
import data.finals.Test05;
import data.finals.Test06;
import data.finals.Test07;
import data.finals.Test08;
import data.finals.Test09;
import data.finals.Test10;
import data.finals.Test11;
import data.finals.Test12;
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
	public final void test01() {
		test(miniatur(4), Test01.class, sat("01"));
	}
	
	@Test
	public final void test02() {
		test(miniatur(4), Test02.class, sat("02"));
	}
	
	@Test
	public final void test03() {
		test(miniatur(4), Test03.class, sat("03"));
	}
	
	@Test
	public final void test04() {
		test(miniatur(4), Test04.class, sat("04"));
	}
	
	@Test
	public final void test05() {
		test(miniatur(4), Test05.class, sat("05"));
	}
	
	@Test
	public final void test06() {
		test(miniatur(4), Test06.class, sat("06"));
	}
	
	@Test
	public final void test07() {
		test(miniatur(4), Test07.class, sat("07"));
	}
	
	@Test
	public final void test08() {
		test(miniatur(4), Test08.class, sat("08"));
	}
	
	@Test
	public final void test09() {
		test(miniatur(4), Test09.class, sat("09"));
	}
	
	@Test
	public final void test10() {
		test(miniatur(4), Test10.class, sat("10"));
	}
	
	@Test
	public final void test11() {
		test(miniatur(4), Test11.class, sat("11"));
	}
	
	@Test
	public final void test12() {
		test(miniatur(4), Test12.class, sat("12"));
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
