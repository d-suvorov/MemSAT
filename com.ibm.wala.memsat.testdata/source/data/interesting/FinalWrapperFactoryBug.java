package data.interesting;

public class FinalWrapperFactoryBug {
  public static final class Singleton {
    int x;
    
    Singleton() {
      x = 1;
    }
  }

  private FinalWrapper wrapper;

  public Singleton get() {
    FinalWrapper w = wrapper;
    if (w == null) { // check 1
      synchronized(this) {
        w = wrapper;
        if (w == null) { // check2
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
    assert rs1.x == 0;
  }
  
  public static final void p2() {
    Singleton rs2 = factory.get();
    assert rs2.x == 1;  
  }
}
