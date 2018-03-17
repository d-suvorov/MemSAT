package data.arith;

public class Bitwise {
	public static int a1 = 0;
	public static int a2 = 0;
	public static int a3 = 0;
	public static int a4 = 0;
	public static int a5 = 0;
	
	public static final void pTest() {
		int r1 = 7 & 5;
        if (r1 == 5)
        	a1 = 1;

        int r2 = 7 | 5;
        if (r2 == 7)
        	a2 = 1;

        int r3 = 7 ^ 5;
        if (r3 == 2)
        	a3 = 1;

        int r4 = 3 << 2;
        if (r4 == 12)
        	a4 = 1;

        int r5 = 7 >> 2;
        if (r5 == 1)
        	a5 = 1;
	}
	
	public static final void pCheck() {
		assert a1 == 1;
		assert a2 == 1;
		assert a3 == 1;
		assert a4 == 1;
		assert a5 == 1;
	}
}
