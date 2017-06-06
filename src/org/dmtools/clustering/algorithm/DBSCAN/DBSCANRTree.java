package org.dmtools.clustering.algorithm.DBSCAN;

import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.algorithm.CNBC.CNBCRTreePoint;
import org.dmtools.clustering.algorithm.common.ClusteringAlgorithm;
import spatialindex.spatialindex.Point;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class DBSCANRTree extends ClusteringAlgorithm {

    Double Eps = 0.0;
    Integer MinPts = 0;

    /**
     *
     */
    public void run() {
        DBSCAN();
    }

    public ArrayList<Point> getDataset() {
        return this.Dataset;
    }

    /**
     *
     */
    protected int nextId(int id) {
        return ++id;
    }

    // DBSCAN
    // -----------------------------------------------------------------------

    protected void DBSCAN() {
        // Dataset is UNCLASSIFIED
        Integer ClusterId = nextId(CDMCluster.NOISE);

        for (Point point : Dataset) {
            //BasicSpatialObject point = (DbscanSpatialObject) p;
            //point.ClId = UNCLASSIFIED;
            setClId(point, CDMCluster.UNCLASSIFIED);
        }

        // ClusterId := nextId(NOISE);
        // FOR i FROM 1 TO Dataset.size DO
        // PointToRemove := Dataset.get(i);
        for (Point Point : Dataset) {
            //DbscanSpatialObject PointToRemove = (DbscanSpatialObject) p;
            // IF PointToRemove.ClId = UNCLASSIFIED THEN
            ///if (PointToRemove.ClId == UNCLASSIFIED)
            if (getClId(Point) == CDMCluster.UNCLASSIFIED)
            // IF ExpandCluster(Dataset, PointToRemove, ClusterId, Eps, MinPts) THEN
            {
                if (ExpandCluster(Dataset, Point, ClusterId, Eps, MinPts)) {
                    // ClusterId := nextId(ClusterId)
                    ClusterId = nextId(ClusterId);
                    // END IF
                }
                // END IF
            }
        }
        // END FOR
        // END;
    } // DBSCAN

    /**
     *
     *
     */
    // ExpandCluster(Dataset, PointToRemove, ClId, Eps, MinPts) : Boolean;
    private boolean ExpandCluster(ArrayList<Point> SetOfPoints,
                                  Point Point, Integer ClId, Double Eps, Integer MinPts) {
        ArrayList<Point> seeds1 = tree.regionQuery(Point, Eps);
        ArrayList<Point> seeds = new ArrayList<Point>();
        seeds.addAll(seeds1);
        // IF seeds.size<MinPts THEN // no core point
        if (seeds.size() < MinPts) {
            // SetOfPoint.changeClId(PointToRemove,NOISE);
            changeClId(Point, CDMCluster.NOISE);
            // RETURN False;
            return false;
        }
        // ELSE // all points in seeds are density-reachable from PointToRemove
        else {
            // Dataset.changeClIds(seeds,ClId);
            changeClId(seeds, ClId);
            // seeds.delete(PointToRemove);
            //seeds.remove(PointToRemove);
            changeClId(Point, ClId);
            // WHILE seeds <> Empty DO
            while (seeds.size() > 0) {
                // currentP := seeds.first();
                CNBCRTreePoint currentP = (CNBCRTreePoint) seeds.get(0);
                // result := Dataset.regionQuery(currentP, Eps);
                ArrayList<Point> result = tree.regionQuery(currentP, Eps);
                // IF result.size >= MinPts THEN
                if (result.size() >= MinPts) {
                    // FOR i FROM 1 TO result.size DO
                    for (int i = 1; i < result.size(); i++) {
                        // resultP := result.get(i);
                        //DbscanSpatialObject resultP = (DbscanSpatialObject) result
                        //        .get(i);
                        CNBCRTreePoint resultP = (CNBCRTreePoint) result.get(i);
                        // IF resultP.ClId IN {UNCLASSIFIED, NOISE} THEN
                        if (getClId(resultP) == CDMCluster.UNCLASSIFIED
                                || getClId(resultP) == CDMCluster.NOISE) {
                            // IF resultP.ClId = UNCLASSIFIED THEN
                            if (getClId(resultP) == CDMCluster.UNCLASSIFIED) {
                                // seeds.append(resultP);
                                seeds.add(resultP);
                                // END IF;
                            }
                            // Dataset.changeClId(resultP,ClId);
                            changeClId(resultP, ClId);
                            // END IF; // UNCLASSIFIED or NOISE
                        }
                        // END FOR;
                    }
                    // END IF; // result.size >= MinPts
                }
                // seeds.delete(currentP);
                seeds.remove(0);
                // END WHILE; // seeds <> Empty
            }
            // RETURN True;
            return true;
            // END IF
        }
        // END; // ExpandCluster
    }

    /**
     *
     * @param Points
     * @param ClId
     * @return
     */
    public void changeClId(ArrayList<Point> Points, int ClId) {
        for (Point p : Points)
            changeClId(p, ClId);
    }

    /**
     *
     */
    public void changeClId(Point Point, int ClId) {
        //DbscanSpatialObject o = (DbscanSpatialObject) PointToRemove;
        //o.ClId = ClId;
        setClId(Point, ClId);
    }

    /**
     *
     * @param Point
     * @return
     */
    public int getClId(Point Point)
    {
        return ((CNBCRTreePoint)Point).getClusterId();
    }

    /**
     *
     * @param Point
     * @param ClId
     */
    public void setClId(Point Point, int ClId)
    {
        CNBCRTreePoint point = (CNBCRTreePoint) Point;
        point.setClusterId(ClId);
    }

    /**
     *
     * @param Points
     * @param ClId
     */
    public void setClId(ArrayList<CNBCRTreePoint> Points, int ClId) {
        for (CNBCRTreePoint o : Points) {
            setClId(o, ClId);
        }
    }

    /**
     *
     * @throws IOException
     */
/*    public void initRTree() throws IOException {
        tree = new RTreeIndex();
        tree.initRTree(Dataset, nDim);
    }*/

    /**
     *
     */
    /*
    public IClusteringData getResult() {
        BasicClusteringData bcd = new BasicClusteringData();
        ArrayList<IClusteringObject> al = new ArrayList<IClusteringObject>();

        for (Object o : Dataset) {
            CNBCRTreePoint mp = (CNBCRTreePoint) o;
            BasicClusteringObject bco = new BasicClusteringObject();
            BasicSpatialObject rso = new BasicSpatialObject(mp.m_pCoords);
            bco.setSpatialObject(rso);
            BasicClusterInfo bci = new BasicClusterInfo();
            bci.setClusterId(mp.getClusterId());
            bco.setClusterInfo(bci);
            al.add(bco);
        }

        bcd.set(al);

        return bcd;
    }*/
/*
    public void setData(IClusteringData data) {
        ArrayList<IClusteringObject> tmp = (ArrayList<IClusteringObject>) data
                .get();
        Dataset = new ArrayList();
        nDim = data.get().iterator().next().getSpatialObject().getValues().length;

        try {
            initRTree();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }

        int id = 0;

        // building R-Tree
        for (IClusteringObject ico : tmp) {
            CNBCRTreePoint mp = new CNBCRTreePoint(ico.getSpatialObject()
                    .getValues(), CDMCluster.UNCLASSIFIED);
            Dataset.add(id, mp);
            byte[] d = new byte[]{CDMCluster.UNCLASSIFIED};
            tree.insertData(d, mp, id);
            id++;
        }
    }*/

    public void setEps(double eps) {
        this.Eps = eps;
    }

    public void setMinPts(int minPts) {
        this.MinPts = minPts;
    }

}

