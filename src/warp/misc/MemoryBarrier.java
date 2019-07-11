package warp.misc;
/**
 * This is a basic read/write barrier for situations where data is accessed by more than
 * one thread but only by one thread at any given point in time.
 *
 * This uses Java volatile to ensure updates are written to main memory when release() is
 * called and data is read from main memory when acquire() is called.
 */
final public class MemoryBarrier {
    private volatile boolean barrier;

    /**
     * Call this before reading/writing any properties of this instance.
     */
    public boolean acquire() {
        this.barrier = true;
        return this.barrier;
    }

    /**
     * Call this once a thread has finished working with an instance of this class. This will
     * make all updates visible to other threads.
     */
    public void release() {
        this.barrier = false;
    }
}
