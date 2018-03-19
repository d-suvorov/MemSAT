package data.interesting;

public class MultipleObjects {
	public static final class Box {
		int f;
		
		Box(int f) {
			this.f = f;
		}
	}
	
	public static int a = 0;
	
	public static final void p1() {
		assert a == 0;
	}
	
	public static final void p2() {
		Box b = new Box(2);
		assert b.f == 2;
		Box bb = new Box(1);
		assert bb.f == 1;
	}
}
