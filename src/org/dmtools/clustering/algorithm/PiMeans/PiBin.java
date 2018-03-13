package org.dmtools.clustering.algorithm.PiMeans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Piotr Lasek on 11.10.2017.
 */
public class PiBin {
    protected final static Logger log = LogManager.getLogger(PiBin.class.getSimpleName());

    private final int layer;
    private long zoo;
    private int x;
    private int y;
    private long maxX;
    private long maxY;
    private long minX;
    private long minY;
    private ArrayList<PiBin> childBins;
    private ArrayList<PiPoint> points;
    //private int pointsCount;
    private PiBin parent;
    private PiCluster cluster;

    /**
     *
     * @param count
     */
    /*public PiBin(int count) {
        this.pointsCount = count;
    }*/

    /**
     *
     * @param layer
     * @param originalZoo
     */
    public PiBin(int layer, Long originalZoo) {
        this.layer = layer;
        this.zoo = originalZoo;

        double zooAtLevel = Morton2D.zooAtLevelA(PiCube.dim, PiCube.maxDepth, layer, originalZoo);
        double firstZooInBinAtLevel = Morton2D.firstZooInBin(originalZoo, PiCube.maxDepth, layer);

        long xy[] = Morton2D.decode((long) firstZooInBinAtLevel);
        int size = this.getSize();

        x = (int) xy[0];
        y = (int) xy[1];

        this.minX = xy[0];
        this.minY = xy[1];
        this.maxX = xy[0] + size - 1;
        this.maxY = xy[1] + size - 1;
    }

    /**
     *
     * @return
     */
    public int getSize() {
        int size = (int) Morton2D.binSizeAtLevel(PiCube.maxDepth, layer);
        return size;
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

        // tests
        long pZoo = Morton2D.encode((long) point.coordinates[0], (long) point.coordinates[1]);
        long fZoo1 = Morton2D.firstZooInBin(pZoo, PiCube.maxDepth, this.layer);
        long fZoo2 = Morton2D.firstZooInBin(zoo, PiCube.maxDepth, this.layer);
        assert fZoo1 == fZoo2;
        assert minX <= point.coordinates[0];
        assert minY <= point.coordinates[1];
        assert maxX >= point.coordinates[0];
        assert maxY >= point.coordinates[1];
    }

    /**
     *
     */
    public void addChild(PiBin childBin) {
        if (childBins == null) {
            childBins = new ArrayList<>();
        }

        childBins.add(childBin);
        //
        // increasePointsCount(childBin.getPointsCount());
        if (points == null) {
            points = new ArrayList<>();
        }

        points.addAll(childBin.getPoint());

        assert childBins.size() <= 4;

        int pointsCountInChildBins = 0;
        for(PiBin b : childBins) {
           pointsCountInChildBins += b.getPointsCount();
        }

        assert this.getPointsCount() == pointsCountInChildBins;
    }

    /**
     *
     * @return
     */
    public ArrayList<PiBin> getChildBins() {
        return childBins;
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

        if (minX < x && x <= maxX) {
            lowerBound = Math.abs(minY - y);
        } else if (minY < y && y <= maxY) {
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

        if (minX < x && x <= maxX) {
            upperBound = Math.abs(maxY - y);
        } else if (minY < y && y <= maxY) {
            upperBound = Math.abs(maxX - x);
        } else {
            upperBound = Math.sqrt(Math.pow(maxX-x, 2) +
                    Math.pow(maxY-y, 2));
        }

        return upperBound;
    }

    /**
     *
     * @param parent
     */
    public void setParent(PiBin parent) {
        this.parent = parent;
    }

    /**
     *
     * @return
     */
    public int getPointsCount() {
        return points.size();
    }

    /**
     *
     * @param cluster
     */
    public void setCluster(PiCluster cluster) {
        this.cluster = cluster;
    }

    /**
     *
     * @return
     */
    public PiCluster getCluster() {
        return cluster;
    }

    /**
     *
     * @return
     */
    public Collection<? extends PiPoint> getPoint() {
        return points;
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
     * @return
     */
    public double getMaxY() {
        return maxY;
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
     * @return
     */
    public double getMinY() {
        return minY;
    }

    /**
     *
     * @return
     */
    public long getZoo() {
        return zoo;
    }

    public int getLayer() {
        return layer;
    }
}
