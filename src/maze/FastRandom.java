package maze;

import java.io.Serializable;
import java.util.Random;

/** 
 * 32-bit xorshift PRNG with a period of 2<sup>32</sup> - 1. See the following
 * paper by George Marsaglia for more information:
 * <p>
 * http://www.jstatsoft.org/v08/i14/paper
 * <p>
 * This class is not thread-safe. It is for this reason that this class does
 * not extend the thread-safe {@link Random}. (One might reasonably assume
 * that subclasses of {@code Random} are also thread-safe.)
 */
public class FastRandom implements Serializable {
    private static final long serialVersionUID = -3608711381789241205L;
    
    private int x;
    
    /** Creates a PRNG with a random seed. */
    public FastRandom() {
        Random rnd = new Random();
        do {
            x = rnd.nextInt();
        } while (x == 0);
    }

    /**
     * Creates a PRNG with the specified seed.
     * 
     * @param  seed a nonzero seed
     * @throws IllegalArgumentException if seed is zero
     */
    public FastRandom(int seed) {
        setSeed(seed);
    }
    
    /**
     * Sets the seed for this PRNG.
     * 
     * @param  seed a nonzero seed
     * @throws IllegalArgumentException if seed is zero
     */
    public void setSeed(int seed) {
        if (seed == 0) {
            throw new IllegalArgumentException("Seed must not be zero");
        }
        x = seed;
    }
    
    /** Returns a pseudorandom, nonzero integer. */
    public int nextInt() {
        x ^= x << 13;
        x ^= x >>> 17;
        return x ^= x << 5;
    }
    
    /**
     * Returns a pseudorandom integer in the range [0, n).
     * 
     * @param  n exclusive upper bound
     * @throws IllegalArgumentException if n is not positive
     */
    public int nextInt(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        int random, result;
        do {
            random = nextInt() >>> 1;
            result = random % n;
        } while (random - result + n - 1 < 0);
        return result;
    }
    
    /**
     * Returns a pseudorandom integer in the range [m, n).
     *
     * @param  m inclusive lower bound
     * @param  n exclusive upper bound
     * @throws IllegalArgumentException if n is not greater than m
     */ 
    public int nextInt(int m, int n) {
        if (n <= m) {
            throw new IllegalArgumentException("n must be greater than m");
        }
        return nextInt(n - m) + m;
    }
    
    /** Returns a pseudorandom boolean value. */
    public boolean nextBoolean() {
        return (nextInt() & 1) != 0;
    }
}
