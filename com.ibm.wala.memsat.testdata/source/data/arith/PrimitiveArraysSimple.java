package data.arith;

public class PrimitiveArraysSimple {
	public static int a1 = 0;
	public static int a2 = 0;
	public static int a3 = 0;
	
	public static int[] a = new int[3];
	
	public static final void pTest() {
		a[0] = 1;
		a[1] = 2;
		a[2] = 3;
		if (a[0] == 1)
			a1 = 1;
		if (a[1] == 2)
			a2 = 1;
		if (a[2] == 3)
			a3 = 1;
	}
	
	public static final void pCheck() {
		assert a1 == 1;
		assert a2 == 1;
		assert a3 == 1;
	}
}
