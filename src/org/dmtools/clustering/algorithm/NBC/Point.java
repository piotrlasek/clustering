package org.dmtools.clustering.algorithm.NBC;

import java.util.ArrayList;

import org.dmtools.clustering.model.ISpatialObject;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class Point extends NbcSpatialObject {

    public double dist;
    public double dist2;
    public double Eps2;
    public ArrayList<Point> Border;
    public int pos;
    public int pos2;
    public int NeighborsNo;
    public int ClusterId;
    public ArrayList<Double> Dists;
    public double tmpDist;
    public ArrayList<Integer> neighbors;
    
    public Point(ISpatialObject o) {
        super(o);
        dist = -1;
        Border = new ArrayList<Point>();
        Dists = new ArrayList<Double>();
        pos = -1;
        pos2 = -1;
        ClusterId = -1;
        NeighborsNo = 1;
        neighbors = new ArrayList<Integer>();
    }

    public Point(double[] coordinates) {
        super(coordinates);
        dist = -1;
        Border = new ArrayList<Point>();
        Dists = new ArrayList<Double>();
        pos = -1;
        pos2 = -1;
        ClusterId = -1;
        NeighborsNo = 1;
        neighbors = new ArrayList<Integer>();
    }
}
