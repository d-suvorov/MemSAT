package data.finals;

public class Test02 {
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
		assert ta != null;
		if (ta != null) {
			assert ta.f == 1;
		}
	}
}
