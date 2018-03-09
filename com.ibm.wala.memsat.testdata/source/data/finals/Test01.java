package data.finals;

public final class Test01 {
	public static class A {
		final int f;
		
		A() {
			f = 1;
		}
	}
	
	public static A a = null;
	
	public static final void p1() {
		a = new A();
	}
	
	public static final void p2() {
		A ta = a;
		if (ta != null) {
			// Prohibited by the finals semantics
			assert ta.f == 0;
		}
	}
}
