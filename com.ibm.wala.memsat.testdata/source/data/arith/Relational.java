package data.arith;

public class Relational {
	public static int a1 = 0;
	public static int a2 = 0;
	public static int a3 = 0;
	public static int a4 = 0;
	public static int a5 = 0;
	public static int a6 = 0;
	public static int a7 = 0;
	
	public static final void pTest() {
		boolean r1 = 2 == 3;
		if (!r1)
			a1 = 1;
		boolean r2 = 2 != 3;
		if (r2)
			a2 = 1;
		boolean r3 = 2 < 3;
		if (r3)
			a3 = 1;
		boolean r4 = 2 <= 3;
		if (r4)
			a4 = 1;
		boolean r5 = 2 > 3;
		if (!r5)
			a5 = 1;
		boolean r6 = 2 >= 3;
		if (!r6)
			a6 = 1;
		
		boolean r7 = (10 * 10 - 50) >= (5 * 5 * 3 - 25);
		if (r7)
			a7 = 1;
	}
	
	public static final void pCheck() {
		assert a1 == 1;
		assert a2 == 1;
		assert a3 == 1;
		assert a4 == 1;
		assert a5 == 1;
		assert a6 == 1;
		assert a7 == 1;
	}
}
