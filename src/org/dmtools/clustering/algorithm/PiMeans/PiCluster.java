package org.dmtools.clustering.algorithm.PiMeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/**
 * Created by Piotr Lasek on 16.11.2017.
 */
public class PiCluster extends PiPoint {

    public static int nextId = 0;
    private int id;
    ArrayList<PiPoint> points;

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static PiCluster random(double[] min, double[] max) {
        double[] randomCoordinates = new double[2];

        Random r = new Random();
        int x = r.nextInt((int) max[0] - (int) min[0]) + (int) min[0];
        int y = r.nextInt((int) max[1] - (int) min[1]) + (int) min[1];

        randomCoordinates[0] = x;
        randomCoordinates[1] = y;

        PiCluster piCluster = new PiCluster(randomCoordinates);
        piCluster.setId(PiCluster.nextId++);
        return piCluster;
    }

    /**
     *
     * @param coordinates
     */
    public PiCluster(double[] coordinates) {
        super(coordinates, null);
    }

    /**
     *
     * @param coordinates
     */
    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public void clear() {
        this.points.clear();
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param points
     */
    public void addPoints(Collection<? extends PiPoint> points) {
        if (this.points == null)
            this.points = new ArrayList<>();

        this.points.addAll(points);
    }

    public String toString() {
        return this.getId() + ": " + Arrays.toString(coordinates);
    }

    public ArrayList<PiPoint> getPoints() {
        return points;
    }
}

