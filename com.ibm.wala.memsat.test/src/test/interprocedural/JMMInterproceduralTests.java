package test.interprocedural;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.ibm.wala.memsat.concurrent.MemoryModel;
import com.ibm.wala.memsat.concurrent.memory.jmm.JMMAlt;
import com.ibm.wala.types.MethodReference;

public class JMMInterproceduralTests extends InterproceduralTests  {
	public JMMInterproceduralTests() {
		super(new HashSet<String>(Arrays.asList("01", "02", "03", "04", "05", "06")));
	}

	@Override
	protected MemoryModel memoryModel(int maxSpeculations,Set<MethodReference> special) {
		return new JMMAlt(maxSpeculations, special);
	}
}
