package data.finals;

public class SimplePublicationObject0 {	
	public static class A {
		final Object f;
		
		A() {
			f = new Object();
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
			// Prohibited by the finals semantics
			assert ta.f == null;
		}
	}
}
