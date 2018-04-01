package data.interesting;

public class FinalWrapperFactoryBug {
  private FinalWrapper wrapper;

  public Singleton get() {
    FinalWrapper w = wrapper;
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
    public Singleton instance;
    public FinalWrapper(Singleton instance) {
      this.instance = instance;
    }
  }
  
  public static FinalWrapperFactoryBug factory = new FinalWrapperFactoryBug();
  
  public static final void p1() {
    Singleton rs1 = factory.get();
    assert rs1 != null;
    if (rs1 != null) {
      assert rs1.x == 1;
    }
  }

  public static final void p2() {
    Singleton rs2 = factory.get();
    assert rs2 != null;
    if (rs2 != null) {
      assert rs2.x == 0;
    }
  }
}
