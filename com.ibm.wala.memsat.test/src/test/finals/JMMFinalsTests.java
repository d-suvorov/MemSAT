package test.finals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.ibm.wala.memsat.concurrent.MemoryModel;
import com.ibm.wala.memsat.concurrent.memory.jmm.JMMAlt;
import com.ibm.wala.memsat.concurrent.memory.jmm.JMMOriginal;
import com.ibm.wala.types.MethodReference;

public class JMMFinalsTests extends FinalsTests {
	public JMMFinalsTests() {
		super(new HashSet<String>(Arrays.asList("02", "03", "04", "05", "06", "08", "09", "12")));
	}

	@Override
	protected MemoryModel memoryModel(int maxSpeculations,Set<MethodReference> special) {
		return new JMMAlt(maxSpeculations, special);
	}
}
