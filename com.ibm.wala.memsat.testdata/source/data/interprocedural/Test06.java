package data.interprocedural;

public final class Test06 {
	public static class Box {
		volatile int f;
		
		Box(int f) {
			this.f = f;
		}
	}
	
	static volatile Box b;
	
	public static final void p1() {
		b = new Box(2);
		assert b.f == 1;
	}
	
	public static final void p2() {
		Box r = b;
		if (r != null) {
			r.f = 1;
		}
	}
}
