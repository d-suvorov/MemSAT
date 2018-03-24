package data.interesting;

public class BlockingFactoryReadUninitialized {
  public static final class Singleton {
    int x;
    
    Singleton() {
      x = 1;
    }
  }
  
  private Singleton instance;

  public Singleton get() {
    synchronized(this) {
      if (instance == null) {
        instance = new Singleton();
      }
      return instance;
    }
  }
  
  public static BlockingFactoryReadUninitialized factory = new BlockingFactoryReadUninitialized();

  public static final void p1() {
    Singleton rs1 = factory.get();
    assert rs1.x == 0;
  }

  public static final void p2() {
    Singleton rs2 = factory.get();
    assert rs2.x == 1;
  }
}
