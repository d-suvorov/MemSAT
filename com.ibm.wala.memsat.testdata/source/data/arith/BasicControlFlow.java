package data.arith;

public class BasicControlFlow {
	public static int ind = 0;
	
	public static void pTest() {
        int a = 11 * 10;
        int b = 45 + 33;
        int c = (a < b) ? 42 : 43;

        if (c == 43)
            foo();
    }

    private static void foo() {
        int x = 66 / 2;
        if (ind < x)
            ind = 1;
    }
    
    public static void pCheck() {
    	assert ind == 1;
    }
}
