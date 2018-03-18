package test.interesting;

import java.util.Set;

import com.ibm.wala.memsat.concurrent.MemoryModel;
import com.ibm.wala.memsat.concurrent.memory.jmm.JMMAlt;
import com.ibm.wala.types.MethodReference;

public class JMMInsterestingTests extends InterestingTests {
	public JMMInsterestingTests() {}

	@Override
	protected MemoryModel memoryModel(int maxSpeculations, Set<MethodReference> special) {
		return new JMMAlt(maxSpeculations, special);
	}

}
