package org.dmtools.clustering.algorithm.PiMeans;

import java.util.ArrayList;

/**
 * Created by Piotr Lasek on 11.10.2017.
 */
public class PiBin {
    int x;
    int y;
    int maxX;
    int maxY;
    int minX;
    int minY;

    PiBin parent;
    ArrayList<PiBin> childBins;

    int pointsCount;

    ArrayList<PiPoint> points;

    /**
     *
     * @param count
     */
    public PiBin(int count) {
        this.pointsCount = count;
    }

    /**
     *
     */
    public void addChild(PiBin childBin) {
        if (childBins == null) {
            childBins = new ArrayList<>();
        }
        childBins.add(childBin);
        pointsCount += childBin.getPointsCount();
    }

    /**
     *
     * @param i
     */
    public void increasePointsCount(int i) {
        pointsCount += i;
    }

    /**
     *
     * @return
     */
    public int getPointsCount() {
        return pointsCount;
    }
}
