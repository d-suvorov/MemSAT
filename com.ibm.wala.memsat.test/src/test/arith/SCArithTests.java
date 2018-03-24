package test.arith;

import java.util.Set;

import com.ibm.wala.memsat.concurrent.MemoryModel;
import com.ibm.wala.memsat.concurrent.memory.simple.SequentialConsistency;
import com.ibm.wala.types.MethodReference;

public class SCArithTests extends ArithTests {
	@Override
	protected MemoryModel memoryModel(int maxSpeculations, Set<MethodReference> special) {
		return new SequentialConsistency();
	}
}
