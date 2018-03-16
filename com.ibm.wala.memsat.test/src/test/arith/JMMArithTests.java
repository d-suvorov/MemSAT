package test.arith;

import java.util.Set;

import com.ibm.wala.memsat.concurrent.MemoryModel;
import com.ibm.wala.memsat.concurrent.memory.jmm.JMMAlt;
import com.ibm.wala.types.MethodReference;

public class JMMArithTests extends ArithTests {
	public JMMArithTests() {}

	@Override
	protected MemoryModel memoryModel(int maxSpeculations, Set<MethodReference> special) {
		return new JMMAlt(maxSpeculations, special);
	}

}
