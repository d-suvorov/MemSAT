package test.interesting;

import static com.ibm.wala.memsat.util.Graphs.graph;
import static test.TestUtil.threadMethods;

import java.io.File;

import org.junit.Test;

import com.ibm.wala.memsat.Miniatur;

import data.interesting.BlockingFactoryReadInitialized;
import data.interesting.BlockingFactoryReadUninitialized;
import data.interesting.FinalWrapperFactory;
import data.interesting.FinalWrapperFactoryBug;
import data.interesting.MultipleObjects;
import test.ConcurrentTests;

public abstract class InterestingTests extends ConcurrentTests {
	private static final File INTERESTING_TESTS = new File("source/data/interesting");

	final void test(Miniatur miniatur, Class<?> testCase, boolean sat) {
		test(miniatur, INTERESTING_TESTS, graph(threadMethods("p", testCase)), sat);
	}
	
	private Miniatur getMiniatur(int speculations) {
		Miniatur mini = miniatur(speculations);
		mini.options().kodkodOptions().setBitwidth(8);
		return mini;
	}
	
	@Test
	public final void testBlockingFactoryReadInitialized() {
		test(getMiniatur(3), BlockingFactoryReadInitialized.class, true);
	}
	
	@Test
	public final void testBlockingFactoryReadUninitialized() {
		test(getMiniatur(3), BlockingFactoryReadUninitialized.class, false);
	}
	
	@Test
	public final void testFinalWrapperFactory() {
		test(getMiniatur(3), FinalWrapperFactory.class, true);
	}
	
	@Test
	public final void testFinalWrapperFactoryBug() {
		test(getMiniatur(3), FinalWrapperFactoryBug.class, false);
	}
	
	@Test
	public final void testMultipleObjects() {
		test(getMiniatur(3), MultipleObjects.class, true);
	}
}
