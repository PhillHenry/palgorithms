package uk.co.odinconsultants.bitset;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AtomicBitSetTest {

    private final int LENGTH = 1024;

    private AtomicBitSet toTest = new AtomicBitSet(LENGTH);

    @Test
    public void initialValuesShouldBeFalse() {
        for (int i = 0 ; i < LENGTH ; i++) {
            assertThat(toTest.get(i), is(false));
        }
    }

    @Test
    public void getShouldReturnWhatIsSet() {
        assertThat(toTest.get(0), is(false));
        assertThat(toTest.set(0), is(true));
        assertThat(toTest.get(0), is(true));
    }

}
