package data.interesting;

public class AtomicInteger {
    private int value;

    public AtomicInteger(int value) {
        this.value = value;
    }
    
    public final int get() {
        synchronized (this) {
            return value;
        }
    }
    
    public final void set(int update) {
        synchronized (this) {
            value = update;
        }
    }
    
    public final boolean compareAndSet(int expect, int update) {
        synchronized (this) {
            if (value == expect) {
                 value = update;
                 return true;
            }
            return false;
        }
    }
}
