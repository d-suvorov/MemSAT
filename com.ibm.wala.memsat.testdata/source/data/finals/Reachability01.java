package data.finals;

public class Reachability01 {
static class B {
    int f;
    
    B() {
      f = 1;
    }
  }
  
  static class D {
    final B b;
    
    D() {
      b = new B();
    }
  }
  
  public static D d = null;
  
  public static void p1() {
    d = new D();
  }
  
  public static void p2() {
    D rd = d;
    assert rd != null;
    if (rd != null) {
      B rb = rd.b;
      assert rb != null;
      if (rb != null) {
        assert rb.f == 1;
      }
    }
  }
}
