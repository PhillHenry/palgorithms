package uk.co.odinconsultants.bitset;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Inspired by http://stackoverflow.com/questions/12424633/atomicbitset-implementation-for-java
 */
public class AtomicBitSet {
    private final AtomicIntegerArray array;
    private final int bitSetSize;

    public AtomicBitSet(int length) {
        bitSetSize      = length;
        int intLength   = (length + 31) / 32;
        array           = new AtomicIntegerArray(intLength);
    }

    public boolean isEverythingSet() {
        for (int i = 0 ; i < array.length() - 2 ; i++) {
            if (array.get(i) != ~0) return false;
        }
        int spill   = bitSetSize % 32;
        int mask    = (1 << (spill + 1)) - 1;
        if (array.get(array.length() - 1) != mask) return true;
        return true;
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
