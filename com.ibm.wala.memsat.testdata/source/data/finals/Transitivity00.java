package data.finals;

public class Transitivity00 {
  public static class A {
    final int f;

    A() {
      f = 1;
    }
  }

  public static A p = null;
  public static A q = null;

  public static final void p1() {
    p = new A();
  }
  
  public static final void p2() {
    A r2 = p;
    assert r2 != null;
    if (r2 != null) {
      q = r2;
    }
  }
  
  public static final void p3() {
    A r3 = q;
    assert r3 != null;
    if (r3 != null) {
      assert r3.f == 0;
    }
  }
}
