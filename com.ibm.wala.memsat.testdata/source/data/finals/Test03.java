package data.finals;

public class Test03 {
	public static class A {
		final int f;
		
		A() {
			f = 1;
			p = this;
		}
	}
	
	public static A p = null;
	public static A q = null;
	
	public static final void p1() {
		q = new A();
	}

	public static final void p2() {
		A tp = p;
		assert tp != null;
		if (tp != null) {
			assert tp.f == 0;
		}
	}
}
