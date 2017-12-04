package data.interprocedural;

public final class Test06 {
	public static class Box {
		int f;
		
		Box(int f) {
			this.f = f;
		}
	}
	
	static Box b;
	
	public static final void p1() {
		b = new Box(1);
		assert b.f == 0;
	}
	
	public static final void p2() {
		Box r = b;
		if (r != null) {
			r.f = 0;
		}
	}
}
