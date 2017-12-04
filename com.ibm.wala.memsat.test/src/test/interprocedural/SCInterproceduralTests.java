package test.interprocedural;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.ibm.wala.memsat.concurrent.MemoryModel;
import com.ibm.wala.memsat.concurrent.memory.simple.SequentialConsistency;
import com.ibm.wala.types.MethodReference;

public class SCInterproceduralTests extends InterproceduralTests {
	public SCInterproceduralTests() {
		super(new HashSet<String>(Arrays.asList("01", "02", "03", "04", "05", "06")));
	}

	@Override
	protected MemoryModel memoryModel(int maxSpeculations, Set<MethodReference> special) {
		return new SequentialConsistency();
	}
}
