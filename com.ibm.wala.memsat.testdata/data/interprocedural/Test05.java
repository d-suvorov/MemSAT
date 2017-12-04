package data.interprocedural;

public final class Test05 {
	public static class Box {
		int f;
		
		Box(int f) {
			this.f = f;
		}
	}
	
	static volatile Box b;
	
	public static final void p1() {
		b = new Box(1);
	}
	
	public static final void p2() {
		Box r = b;
		int rf = 0;
		if (r != null) {
			rf = r.f;
		}
		assert rf == 1;
	}
}
