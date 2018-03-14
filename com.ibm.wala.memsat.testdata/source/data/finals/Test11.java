package data.finals;

public class Test11 {
	public static class A {
		final int f;
		
		A() {
			f = 1;
		}
	}
	
	public static A[] a = new A[2];
	
	static {
		a[0] = null;
		a[1] = null;
	}
	
	public static final void p1() {
		a[1] = new A();
	}
	
	public static final void p2() {
		A ta = a[1];
		assert ta != null;
		if (ta != null) {
			// Prohibited by the finals semantics
			assert ta.f == 0;
		}
	}
}
