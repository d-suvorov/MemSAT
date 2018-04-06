package data.interesting;

public class AtomicCounterTest {
    public static AtomicInteger atomic = new AtomicInteger(0);
    
    public static final void increment(int increment) {
        boolean done = false;
        while (!done) {
            int prev = atomic.get();
            done = atomic.compareAndSet(prev, prev + increment);
        }
    }
    
    public static final void p1() {
        increment(42);
    }

    public static final void p2() {
        increment(42);
    }

    public static final void pCheck() {
        int value = atomic.get();
        assert value == 42 + 42;
    }
}
