package data.interesting;

public class AtomicCounterTest {
    public static AtomicInteger atomic = new AtomicInteger(0);
    
    public static volatile int p1Done = 0;
    public static volatile int p2Done = 0;
    
    public static final void increment(int increment) {
        boolean done = false;
        while (!done) {
            int prev = atomic.get();
            done = atomic.compareAndSet(prev, prev + increment);
        }
        assert done;
    }
    
    public static final void p1() {
        increment(1);
        p1Done = 1;
    }

    public static final void p2() {
        increment(1);
        p2Done = 1;
    }

    public static final void pCheck() {
        assert p1Done == 1;
        assert p2Done == 1;
        int value = atomic.get();
        assert value == 2;
    }
}