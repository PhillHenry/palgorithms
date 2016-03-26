package uk.co.odinconsultants.bitset;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Inspired by http://stackoverflow.com/questions/12424633/atomicbitset-implementation-for-java
 */
public class AtomicBitSet {
    private final AtomicIntegerArray array;

    public AtomicBitSet(int length) {
        int intLength = (length + 31) / 32;
        array = new AtomicIntegerArray(intLength);
    }

    /**
     * @return true if this thread made the change
     */
    public boolean set(long n) {
        int bit = 1 << n;
        int idx = (int) (n >>> 5);
        while (true) {
            int num = array.get(idx);
            int num2 = num | bit;
            if (num == num2) {
                return false;
            } else {
                boolean iChanged = array.compareAndSet(idx, num, num2);
                if (iChanged) return true;
            }
        }
    }

    public boolean get(long n) {
        int bit = 1 << n;
        int idx = (int) (n >>> 5);
        int num = array.get(idx);
        return (num & bit) != 0;
    }
}