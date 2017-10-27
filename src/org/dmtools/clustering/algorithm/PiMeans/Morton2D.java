package org.dmtools.clustering.algorithm.PiMeans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Piotr Lasek on 01.08.2017.
 */
public class Morton2D {
    public static final Logger log = LogManager.getLogger(Morton2D.class);

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public static long encode(long x, long y) {
        long d;
        x = (x | (x << 16)) & 0x0000FFFF0000FFFFL;
        x = (x | (x << 8)) & 0x00FF00FF00FF00FFL;
        x = (x | (x << 4)) & 0x0F0F0F0F0F0F0F0FL;
        x = (x | (x << 2)) & 0x3333333333333333L;
        x = (x | (x << 1)) & 0x5555555555555555L;
        y = (y | (y << 16)) & 0x0000FFFF0000FFFFL;
        y = (y | (y << 8)) & 0x00FF00FF00FF00FFL;
        y = (y | (y << 4)) & 0x0F0F0F0F0F0F0F0FL;
        y = (y | (y << 2)) & 0x3333333333333333L;
        y = (y | (y << 1)) & 0x5555555555555555L;
        d = x | (y << 1);
        return d;
    }

    /**
     *
     * @param morton2d
     * @return
     */
    public static long[] decode(long morton2d) {
        long[] d = new long[2];

        d[0] = DecodeMorton2X(morton2d);
        d[1] = DecodeMorton2Y(morton2d);

        return d;
    }

    /**
     *
     * @param x
     * @return
     */
    static long Compact1By1(long x) {
        x &= 0x55555555;                  // x = -f-e -d-c -b-a -9-8 -7-6 -5-4 -3-2 -1-0
        x = (x ^ (x >>  1)) & 0x33333333; // x = --fe --dc --ba --98 --76 --54 --32 --10
        x = (x ^ (x >>  2)) & 0x0f0f0f0f; // x = ---- fedc ---- ba98 ---- 7654 ---- 3210
        x = (x ^ (x >>  4)) & 0x00ff00ff; // x = ---- ---- fedc ba98 ---- ---- 7654 3210
        x = (x ^ (x >>  8)) & 0x0000ffff; // x = ---- ---- ---- ---- fedc ba98 7654 3210
        return x;
    }

    /**
     *
     * @param code
     * @return
     */
    public static long DecodeMorton2X(long code) {
        return Compact1By1(code >> 0);
    }

    /**
     *
     * @param code
     * @return
     */
    public static long DecodeMorton2Y(long code) {
        return Compact1By1(code >> 1);
    }

    /**
     *
     * @param dim
     * @param deepest
     * @param level
     * @param zoo
     * @return
     */
    public static double zooAtLevelA(int dim, int deepest, int level, long zoo) {
        // return floor(zoo / power(power(CAST((2) as bigint), dim), abs(deepest - level)));
        double b = Math.abs(deepest - level);
        double a = Math.pow(2, dim);
        double p = Math.pow(a, b);
        double zal = zoo / p;
        return Math.floor(zal);
    }
}
