package data.interesting;

public class AtomicCounterNaiveTest {
    public static AtomicInteger atomic = new AtomicInteger(0);
    
    public static final void p1() {
        final int increment = 42;
        int prev = atomic.get();
        atomic.compareAndSet(prev, prev + increment);
    }

    public static final void p2() {
        final int increment = 42;
        int prev = atomic.get();
        atomic.compareAndSet(prev, prev + increment);
    }

    public static final void pCheck() {
        int value = atomic.get();
        assert value == 42 + 42;
    }
}
