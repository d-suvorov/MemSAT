package data.interesting;

public class FinalWrapperDebug1 {
  private FinalWrapper wrapper;

  public void put1() {
    synchronized (this) {
      FinalWrapper w = new FinalWrapper(
        new Singleton()
      );
      wrapper = w;
    }
  }
  
  public Singleton get2() {
    FinalWrapper w = wrapper;
    assert w != null;
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
  
  public static FinalWrapperDebug0 factory = new FinalWrapperDebug0();
  
  public static final void p1() {
    factory.put1();
  }

  public static final void p2() {
    Singleton rs2 = factory.get2();
    assert rs2 != null;
    if (rs2 != null) {
      int rx2 = rs2.x;
      assert rx2 == 1;
    }
  }
}
