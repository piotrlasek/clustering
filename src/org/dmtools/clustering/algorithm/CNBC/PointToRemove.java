package org.dmtools.clustering.algorithm.CNBC;

import org.dmtools.clustering.model.ISpatialObject;

import java.util.ArrayList;

public class PointToRemove extends NbcSpatialObject {

    public double dist;
    public double dist2;
    public double Eps2;
    public ArrayList<PointToRemove> Border;
    public int pos;
    public int pos2;
    public int NeighborsNo;
    public int ClusterId;
    public ArrayList<Double> Dists;
    public double tmpDist;
    public ArrayList<Integer> neighbors;
    
    public PointToRemove(ISpatialObject o) {
        super(o);
        dist = -1;
        Border = new ArrayList<PointToRemove>();
        Dists = new ArrayList<Double>();
        pos = -1;
        pos2 = -1;
        ClusterId = -1;
        NeighborsNo = 1;
        neighbors = new ArrayList<Integer>();
    }

    public PointToRemove(double[] coordinates) {
        super(coordinates);
        dist = -1;
        Border = new ArrayList<PointToRemove>();
        Dists = new ArrayList<Double>();
        pos = -1;
        pos2 = -1;
        ClusterId = -1;
        NeighborsNo = 1;
        neighbors = new ArrayList<Integer>();
    }
}
