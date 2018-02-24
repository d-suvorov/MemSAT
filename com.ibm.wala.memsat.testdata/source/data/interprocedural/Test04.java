package data.interprocedural;

public final class Test04 {
	public static class Box {
		int f;
		
		Box(int f) {
			this.f = f;
		}
	}
	
	static volatile int a = 1;
	
	public static final void p1() {
		final int r1 = a;
		assert r1 == 1;
	}
	
	public static final void p2() {
		a = 1;
		Box b = new Box(2);
		assert b.f == 2;
	}
}
