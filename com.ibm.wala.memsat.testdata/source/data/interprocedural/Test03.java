package data.interprocedural;

public final class Test03 {
	static volatile int a = 0;
	
	public static final void p1() {
		foo();
		final int r1 = a;
		assert r1 == 0;
	}
	
	public static final void p2() {
		a = 0;
	}
	
	public static final void foo() {
		a = 1;
	}
}
