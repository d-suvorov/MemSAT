package test.interesting;

import static com.ibm.wala.memsat.util.Graphs.graph;
import static test.TestUtil.threadMethods;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import com.ibm.wala.memsat.Miniatur;

import data.interesting.BlockingFactoryReadInitialized;
import data.interesting.BlockingFactoryReadUninitialized;
import data.interesting.FinalWrapperFactoryInitialized;
import data.interesting.FinalWrapperFactoryInitializedI;
import data.interesting.FinalWrapperFactoryUninitialized;
import data.interesting.FinalWrapperDebug0;
import data.interesting.FinalWrapperDebug1;
import data.interesting.FinalWrapperDebug2;
import data.interesting.FinalWrapperDebug3;
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
		// mini.options().setRecursionLimit(5);
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
	public final void testFinalWrapperFactoryInitialized() {
		test(getMiniatur(3), FinalWrapperFactoryInitialized.class, true);
	}
	
	@Test
	public final void testFinalWrapperFactoryInitializedI() {
		test(getMiniatur(5), FinalWrapperFactoryInitializedI.class, true);
	}
	
	@Test
	public final void testFinalWrapperDebug0() {
		test(getMiniatur(5), FinalWrapperDebug0.class, true);
	}
	
	@Ignore
	@Test
	public final void testFinalWrapperDebug1() {
		test(getMiniatur(5), FinalWrapperDebug1.class, true);
	}
	
	@Ignore
	@Test
	public final void testFinalWrapperDebug2() {
		test(getMiniatur(5), FinalWrapperDebug2.class, true);
	}
	
	@Test
	public final void testFinalWrapperDebug3() {
		test(getMiniatur(5), FinalWrapperDebug3.class, true);
	}
	
	@Test
	public final void testFinalWrapperFactoryUninitialized() {
		test(getMiniatur(5), FinalWrapperFactoryUninitialized.class, false);
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
