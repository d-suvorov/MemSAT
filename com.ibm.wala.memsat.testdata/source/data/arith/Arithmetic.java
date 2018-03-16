package data.arith;

public class Arithmetic {
	public static int a1 = 0;
	public static int a2 = 0;
	public static int a3 = 0;
	public static int a4 = 0;
	public static int a5 = 0;
	public static int a6 = 0;
	public static int a7 = 0;
	public static int a8 = 0;
	public static int a9 = 0;
	public static int a10 = 0;
	
	public static final void pTest() {
		int r1 = 2 + 3;
		if (r1 == 5)
			a1 = 1;
		
		int r2 = 3 - 2;
		if (r2 == 1)
			a2 = 1;
		int r3 = 2 - 3;
		if (r3 == -1)
			a3 = 1;
		
		int r4 = (-2) * 3;
		if (r4 == -6)
			a4 = 1;
		
		int r5 = 2 / 3;
		if (r5 == 0)
			a5 = 1;
		int r6 = 16 / 2;
		if (r6 == 8)
			a6 = 1;
		
		int r7 = 4 % 2;
		if (r7 == 0)
			a7 = 1;
		int r8 = 5 % 3;
		if (r8 == 2)
			a8 = 1;
		
		int r9 = (9 * 11 - 1) / 5;
		if (r9 == 19)
			a9 = 1;
		
		int r10 = -(-12);
		r10++; r10++; r10--; r10++;
		if (r10 == 14)
			a10 = 1;
	}
	
	public static final void pCheck() {
		assert a1 == 1;
		assert a2 == 1;
		assert a3 == 1;
		assert a4 == 1;
		assert a5 == 1;
		assert a6 == 1;
		assert a7 == 1;
		assert a8 == 1;
		assert a9 == 1;
		assert a10 == 1;
	}
}
