package data.interprocedural;

public final class Test04 {
	public static class Box {
		int f;
		
		Box(int f) {
			this.f = f;
		}
	}
	
	static int a = 0;
	
	public static final void p1() {
		final int r1 = a;
		assert r1 == 0;
	}
	
	public static final void p2() {
		a = 0;
		Box b = new Box(1);
		assert b.f == 1;
	}
}
