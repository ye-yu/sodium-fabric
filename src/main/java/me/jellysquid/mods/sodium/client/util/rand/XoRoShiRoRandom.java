package me.jellysquid.mods.sodium.client.util.rand;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.AbstractRandom;

import java.io.Serial;
import java.util.Random;

// XoRoShiRo128** implementation from DSI Utilities, adopted in a minimal implementation to not
// import Apache Commons.
//
// http://xoshiro.di.unimi.it/
public class XoRoShiRoRandom extends Random implements AbstractRandom {
    @Serial
    private static final long serialVersionUID = 1L;

    private SplitMixRandom mixer;
    private long seed = Long.MIN_VALUE;
    private long p0, p1; // The initialization words for the current seed
    private long s0, s1; // The current random words
    private boolean hasSavedState; // True if we can be quickly reseed by using resetting the words

    private static final SplitMixRandom seedUniquifier = new SplitMixRandom(System.nanoTime());

    public static long randomSeed() {
        final long x;

        synchronized (XoRoShiRoRandom.seedUniquifier) {
            x = XoRoShiRoRandom.seedUniquifier.nextLong();
        }

        return x ^ System.nanoTime();
    }

    public XoRoShiRoRandom() {
        this(XoRoShiRoRandom.randomSeed());
    }

    public XoRoShiRoRandom(final long seed) {
        this.setSeed(seed);
    }

    @Override
    public long nextLong() {
        final long s0 = this.s0;

        long s1 = this.s1;

        final long result = s0 + s1;

        s1 ^= s0;

        this.s0 = Long.rotateLeft(s0, 24) ^ s1 ^ s1 << 16;
        this.s1 = Long.rotateLeft(s1, 37);

        return result;
    }

    @Override
    public int nextInt() {
        return (int) this.nextLong();
    }

    @Override
    public int nextInt(final int n) {
        return (int) this.nextLong(n);
    }

    @Override
    public long nextLong(final long n) {
        if (n <= 0) {
            throw new IllegalArgumentException("illegal bound " + n + " (must be positive)");
        }

        long t = this.nextLong();

        final long nMinus1 = n - 1;

        // Shortcut for powers of two--high bits
        if ((n & nMinus1) == 0) {
            return (t >>> Long.numberOfLeadingZeros(nMinus1)) & nMinus1;
        }

        // Rejection-based algorithm to get uniform integers in the general case
        long u = t >>> 1;

        while (u + nMinus1 - (t = u % n) < 0) {
            u = this.nextLong() >>> 1;
        }

        return t;

    }

    @Override
    public double nextDouble() {
        return Double.longBitsToDouble(0x3FFL << 52 | this.nextLong() >>> 12) - 1.0;
    }

    @Override
    public float nextFloat() {
        return (this.nextLong() >>> 40) * 0x1.0p-24f;
    }

    @Override
    public boolean nextBoolean() {
        return this.nextLong() < 0;
    }

    @Override
    public void nextBytes(final byte[] bytes) {
        int i = bytes.length, n;

        while (i != 0) {
            n = Math.min(i, 8);

            for (long bits = this.nextLong(); n-- != 0; bits >>= 8) {
                bytes[--i] = (byte) bits;
            }
        }
    }



    @Override
    public AbstractRandom derive() {
        return new XoRoShiRoRandom(this.nextLong());
    }

    @Override
    public net.minecraft.util.math.random.RandomDeriver createRandomDeriver() {
        return new RandomDeriver(this.nextLong());
    }

    public record RandomDeriver(long seed) implements net.minecraft.util.math.random.RandomDeriver {
            @SuppressWarnings("UnstableApiUsage")
            private static final HashFunction MD5_HASHER = Hashing.md5();

        public AbstractRandom createRandom(int x, int y, int z) {
                long l = MathHelper.hashCode(x, y, z);
                long m = l ^ this.seed;
                return new XoRoShiRoRandom(m);
            }

            public AbstractRandom createRandom(String string) {
                byte[] bs = MD5_HASHER.hashString(string, Charsets.UTF_8).asBytes();
                long l = Longs.fromBytes(bs[0], bs[1], bs[2], bs[3], bs[4], bs[5], bs[6], bs[7]);
                long m = Longs.fromBytes(bs[8], bs[9], bs[10], bs[11], bs[12], bs[13], bs[14], bs[15]) ^ l;
                return new XoRoShiRoRandom(m);
            }

            @VisibleForTesting
            public void addDebugInfo(StringBuilder info) {
                info.append("seed: ").append(this.seed);
            }
        }

    @Override
    public void setSeed(final long seed) {
        // Restore the previous initial state if the seed hasn't changed
        // Setting and mixing the seed is expensive, so this saves some CPU cycles
        if (this.hasSavedState && this.seed == seed) {
            this.s0 = this.p0;
            this.s1 = this.p1;
        } else {
            SplitMixRandom mixer = this.mixer;

            // Avoid allocations of SplitMixRandom
            if (mixer == null) {
                mixer = this.mixer = new SplitMixRandom(seed);
            } else {
                mixer.setSeed(seed);
            }

            this.s0 = mixer.nextLong();
            this.s1 = mixer.nextLong();

            this.p0 = this.s0;
            this.p1 = this.s1;

            this.seed = seed;
            this.hasSavedState = true;
        }
    }

    public XoRoShiRoRandom setSeedAndReturn(final long seed) {
        this.setSeed(seed);

        return this;
    }
}