package data.interesting;

public class FinalWrapperFactory {
  public static final class Singleton {
    int x;
    
    Singleton() {
      x = 1;
    }
  }
  
  private static FinalWrapper wrapper;

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
    public final Singleton instance;
    public FinalWrapper(Singleton instance) {
      this.instance = instance;
    }
  }
  
  public static final void p1() {
    
  }
  
  public static final void p2() {
  
  }
}
