package org.dmtools.clustering.algorithm.CDBSCAN;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.algorithm.CNBC.CNBCRTreePoint;
import org.dmtools.clustering.algorithm.CNBC.InstanceConstraints;
import org.dmtools.clustering.algorithm.common.ClusteringAlgorithm;
import org.dmtools.clustering.model.IClusteringData;
import spatialindex.spatialindex.Point;
import util.SetConstraints;

import java.util.ArrayList;

/**
 * The C-DBSCAN algorithm base class.
 * 
 * @author Piotr Lasek
 * 
 */
public class CDBSCANRTree extends ClusteringAlgorithm {

    private final static Logger log = LogManager.getLogger(CDBSCANRTree.class.getName());

    Double Eps = 0.0;
    Integer MinPts = 0;
    private InstanceConstraints ic = new InstanceConstraints();

    /**
     *
     */
    public void run() {
        timer.clusteringStart();
        DBSCAN();
        timer.clusteringEnd();
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
        timer.setAlgorithmName(CDBSCANAlgorithmSettings.NAME);
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
        // seeds :=Dataset.regionQuery(PointToRemove,Eps);
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
        for (Point p : Points) {
            changeClId(p, ClId);
        }
    }

    /**
     * 
     */
    public void changeClId(Point Point, int ClId) {
        CNBCRTreePoint cnrtp = (CNBCRTreePoint) Point;
        setClId(cnrtp, ClId);
    }

    /**
     * 
     * @param Point
     * @return
     */
    public int getClId(Point Point) {
        return ((CNBCRTreePoint) Point).getClusterId();
    }

    /**
     * 
     * @param Point
     * @param ClId
     */
    public void setClId(Point Point, int ClId) {
        CNBCRTreePoint point = (CNBCRTreePoint) Point;
        point.setClusterId(ClId);
    }

    public void setEps(double eps) {
        this.Eps = eps;
    }

    public void setMinPts(int minPts) {
        this.MinPts = minPts;
    }

    public InstanceConstraints getConstraints() {
        return ic;
    }

    @Override
    public void setData(IClusteringData data) {
        super.setData(data);
    }

    public void setConstraints(String icInfo) {
        if (icInfo == "birch1") {
            log.info("Loading constraints for birch1");
            SetConstraints.forCNBC(Dataset, ic);
        } else if (icInfo.startsWith("random")) {
            log.info("Setting random constraints.");
            randomConstraints(3, 3, this.Dataset.size());
        } else {
            log.warn("No constraints set.");
        }
    }

    /**
     *
     * @param mlCount
     * @param clCount
     * @param datasetSize
     */
    private void randomConstraints(int mlCount, int clCount, int datasetSize) {

        int index = 0;

        for (int i = 0; i < mlCount; i++) {
            index = drawIndex(datasetSize);
            CNBCRTreePoint p0 = (CNBCRTreePoint) Dataset.get(index);
            index = drawIndex(datasetSize);
            CNBCRTreePoint p1 = (CNBCRTreePoint) Dataset.get(index);
            ic.addMustLinkPoints(p0, p1);
        }

        for (int i = 0; i < clCount; i++) {
            index = drawIndex(datasetSize);
            CNBCRTreePoint p0 = (CNBCRTreePoint) Dataset.get(index);
            index = drawIndex(datasetSize);
            CNBCRTreePoint p1 = (CNBCRTreePoint) Dataset.get(index);
            ic.addCannotLinkPoints(p0, p1);
        }
    }

    ArrayList<Integer> indexes = new ArrayList<Integer>();

    /**
     *
     * @param datasetSize
     * @return
     */
    public int drawIndex(int datasetSize) {

        int index = -1;

        do {
            index = (int) (Math.random() * datasetSize);
        } while (indexes.contains(index));

        indexes.add(index);

        return index;
    }
}
