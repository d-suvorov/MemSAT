package data.interesting;

public class FinalWrapperDebug3 {
  private FinalWrapper wrapper;
  
  public Singleton get(int tid) {
    FinalWrapper w = wrapper;
    if (tid == 2) {
      assert w != null;
    }
    if (w == null) {
      synchronized(this) {
        w = wrapper;
        if (w == null) {
          w = new FinalWrapper(new Singleton());
          wrapper = w;
        }
      }
    }
    return w.instance;
  }

  private static class FinalWrapper {
    public final Singleton instance;
    public FinalWrapper(Singleton instance) {
      this.instance = instance;
    }
  }
  
  public static FinalWrapperDebug3 factory = new FinalWrapperDebug3();
  
  public static final void p1() {
    factory.get(1);
  }

  public static final void p2() {
    Singleton rs2 = factory.get(2);
    assert rs2 != null;
    if (rs2 != null) {
      int rx2 = rs2.x;
      assert rx2 == 1;
    }
  }
}
