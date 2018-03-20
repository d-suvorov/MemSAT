package test.interesting;

import static com.ibm.wala.memsat.util.Graphs.graph;
import static test.TestUtil.threadMethods;

import java.io.File;

import org.junit.Test;

import com.ibm.wala.memsat.Miniatur;

import data.interesting.FinalWrapperFactory;
import data.interesting.FinalWrapperFactoryBug;
import data.interesting.MultipleObjects;
import test.ConcurrentTests;

public abstract class InterestingTests extends ConcurrentTests {
	private static final File INTERESTING_TESTS = new File("source/data/interesting");

	final void test(Miniatur miniatur, Class<?> testCase, boolean sat) {
		test(miniatur, INTERESTING_TESTS, graph(threadMethods("p", testCase)), sat);
	}
	
	private Miniatur getMiniatur()  {
		Miniatur mini = miniatur(10);
		mini.options().kodkodOptions().setBitwidth(8);
		return mini;
	}
	
	@Test
	public final void testFinalWrapperFactory() {
		test(getMiniatur(), FinalWrapperFactory.class, true);
	}
	
	@Test
	public final void testFinalWrapperFactoryBug() {
		test(getMiniatur(), FinalWrapperFactoryBug.class, false);
	}
	
	@Test
	public final void testMultipleObjects() {
		test(getMiniatur(), MultipleObjects.class, true);
	}
}
