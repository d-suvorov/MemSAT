package data.interesting;

public class FinalWrapperFactory {
  public static final class Singleton {
    int x;
    
    Singleton() {
      x = 1;
    }
  }

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
    public final Singleton instance;
    public FinalWrapper(Singleton instance) {
      this.instance = instance;
    }
  }
  
  public static FinalWrapperFactory factory = new FinalWrapperFactory();
  
  public static final void p1() {
    Singleton rs1 = factory.get();
    assert rs1.x == 1;
  }

  public static final void p2() {
    Singleton rs2 = factory.get();
    assert rs2.x == 1;
  }
}
