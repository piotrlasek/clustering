package org.dmtools.clustering.algorithm.PiMeans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by Piotr Lasek on 11.10.2017.
 */
public class PiBin {
    protected final static Logger log = LogManager.getLogger(PiBin.class.getSimpleName());
    private final int layer;

    private long zoo;

    private int x;
    private int y;

    private Double maxX;
    private Double maxY;
    private Double minX;
    private Double minY;

    ArrayList<PiBin> childBins;

    ArrayList<PiPoint> points;

    int pointsCount;
    private PiBin parent;

    /**
     *
     * @param count
     */
    /*public PiBin(int count) {
        this.pointsCount = count;
    }*/

    /**
     *
     * @param count
     */
    public PiBin(int count, /*double[] min, double[] max,*/ int layer) {
        this.pointsCount = count;
        this.layer = layer;
        this.maxX = null;
        this.maxY = null;
        this.minX = null;
        this.minY = null;
    }

    /**
     *
     */
    public void addChild(PiBin childBin) {
        if (childBins == null) {
            childBins = new ArrayList<>();
        }
        childBins.add(childBin);
        double x = childBin.getX();
        double y = childBin.getY();

        updateMinMaxXY(x, y);

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

    /**
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     *
     * @return
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     *
     * @param maxX
     */
    public void setMaxX(Double maxX) {
        this.maxX = maxX;
    }

    /**
     *
     * @return
     */
    public double getMaxY() {
        return maxY;
    }

    /**
     *
     * @param maxY
     */
    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    /**
     *
     * @return
     */
    public double getMinX() {
        return minX;
    }

    /**
     *
     * @param minX
     */
    public void setMinX(Double minX) {
        this.minX = minX;
    }

    /**
     *
     * @return
     */
    public double getMinY() {
        return minY;
    }

    /**
     *
     * @param minY
     */
    public void setMinY(double minY) {
        this.minY = minY;
    }

    /**
     *
     * @return
     */
    public long getZoo() {
        return zoo;
    }

    /**
     *
     * @param zoo
     */
    public void setZoo(long zoo) {
        this.zoo = zoo;
    }

    /**
     *
     * @return
     */
    public double lowerBound(PiCluster piCluster) {
        double[] coordinates = piCluster.coordinates;
        double lowerBound = -1;
        double x = coordinates[0];
        double y = coordinates[1];

        if (minX < x && x < maxX) {
            lowerBound = Math.abs(minY - y);
        } else if (minY < y && y < maxY) {
            lowerBound = Math.abs(minX - x);
        } else {
            lowerBound = Math.sqrt(Math.pow(minX-x, 2) +
                Math.pow(minY-y, 2));
        }

        return lowerBound;
    }

    /**
     *
     * @return
     */
    public double upperBound(PiCluster piCluster) {
        double[] coordinates = piCluster.coordinates;
        double upperBound = -1;
        double x = coordinates[0];
        double y = coordinates[1];

        if (minX < x && x < maxX) {
            upperBound = Math.abs(maxY - y);
        } else if (minY < y && y < maxY) {
            upperBound = Math.abs(maxX - x);
        } else {
            upperBound = Math.sqrt(Math.pow(minX-x, 2) +
                    Math.pow(minY-y, 2));
        }

        return upperBound;
    }

    /**
     *
     * @param point
     */
    public void addPoint(PiPoint point) {
        if (points == null) {
            points = new ArrayList();
        }
        points.add(point);
        updateMinMaxXY(point.coordinates[0], point.coordinates[1]);
        pointsCount++;
    }

    /**
     *
     * @param x
     */
    private void updateMinX(double x) {
        if (x < minX) minX = x;
    }

    /**
     *
     * @param x
     */
    private void updateMaxX(double x) {
        if (x > maxX) maxX = x;
    }

    /**
     *
     * @param y
     */
    private void updateMinY(double y) {
        if (y < minY) minY = y;
    }

    /**
     *
     * @param y
     */
    private void updateMaxY(double y) {
        if (y > maxY) maxY = y;
    }

    /**
     *
     * @param x
     * @param y
     */
    private void updateMinMaxXY(double x, double y) {
        if (minX == null && maxX == null && minY == null && maxY == null) {
            minX = maxX = x;
        } else {
            updateMinX(x);
            updateMinY(y);
            updateMaxX(x);
            updateMaxY(y);
        }
    }

    /**
     *
     * @param parent
     */
    public void setParent(PiBin parent) {
        this.parent = parent;
    }
}
