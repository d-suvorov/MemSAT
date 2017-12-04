package data.interprocedural;

public final class Test02 {
	static int a = 0;
	
	public static final void p1() {
		final int r1 = a;
		foo();
		assert r1 == 0;
	}
	
	public static final void p2() {
		a = 0;
	}
	
	public static final void foo() {
		a = 0;
	}
}
