package data.interesting;

public class FinalWrapperFactoryInitializedI {
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
  
  public static FinalWrapperFactoryInitializedI factory = new FinalWrapperFactoryInitializedI();

  public static final void p1() {
    Singleton rs1 = factory.get(1);
    assert rs1.x == 1;
  }

  public static final void p2() {
    Singleton rs2 = factory.get(2);
    assert rs2.x == 1;
  }
}
