package org.dmtools.clustering.algorithm.PiMeans;

import java.util.Random;

/**
 * Created by Piotr Lasek on 16.11.2017.
 */
public class PiCluster extends PiPoint {

    public static int nextId = 0;
    private int id;

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
}

