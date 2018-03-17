package data.arith;

public class PrimitiveArraysRace1 {
	public static int a1 = 0;
	public static int a2 = 0;
	public static int a3 = 0;
	
	public static int[] a = new int[3];
	
	static {
		a[0] = 0;
		a[1] = 0;
		a[2] = 0;
	}
	
	public static final void pTest() {
		a[0] = 1;
		a[1] = 2;
		a[2] = 3;
	}
	
	public static final void pCheck() {
		assert a[0] == 1;
		assert a[1] == 2;
		assert a[2] == 3;
	}
}
