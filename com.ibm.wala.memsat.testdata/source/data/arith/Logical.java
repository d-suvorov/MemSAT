package data.arith;

public class Logical {
	public static int a1 = 0;
	public static int a2 = 0;
	public static int a3 = 0;
	public static int a4 = 0;
	
	public static final void pTest() {
		boolean r1 = !false;
		if (r1)
			a1 = 1;
		
		boolean r2 = false && true;
		if (!r2)
			a2 = 1;
		
		boolean r3 = false || true;
		if (r3)
			a3 = 1;
		
		boolean lhs = 2 < 3;
		boolean rhs = 2 * 2 == 5;
		if (lhs || rhs)
			a4 = 1;
	}
	
	public static final void pCheck() {
		assert a1 == 1;
		assert a2 == 1;
		assert a3 == 1;
		assert a4 == 1;
	}
}
