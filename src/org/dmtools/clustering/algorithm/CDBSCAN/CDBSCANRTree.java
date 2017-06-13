package org.dmtools.clustering.algorithm.CDBSCAN;

import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.algorithm.CNBC.CNBCRTreePoint;
import org.dmtools.clustering.algorithm.common.InstanceConstraintsAlgorithm;
import org.dmtools.clustering.model.IConstraintObject;
import spatialindex.spatialindex.IShape;
import spatialindex.spatialindex.Point;

import java.util.ArrayList;

/**
 * The C-DBSCAN algorithm base class.
 * 
 * @author Piotr Lasek
 * 
 */
public class CDBSCANRTree extends InstanceConstraintsAlgorithm {

    Double Eps = 0.0;
    Integer MinPts = 0;

    int delta = 3;

    /**
     *
     */
    public void run() {
        DBSCAN();
    }

    /**
     * 
     */
    protected int nextId(int id) {
        return ++id;
    }


    /**
     * @param def
     */
    private void markDeferred(ArrayList<IConstraintObject> def) {
        for (IConstraintObject p : def) {
            ArrayList<Point> epsNN = tree.regionQuery((IShape) p, 2*Eps);

            p.setClusterId(CDMCluster.DEFERRED);
            p.setParentCannotLinkPoint(p);
            p.wasDeferred(true);
            deferred.add(p);

            if (!deferred.contains(p)) {
                deferred.add(p);
            }

            for (int i = 0; i < epsNN.size(); i++) {
                CNBCRTreePoint pcl1 = (CNBCRTreePoint) epsNN.get(i);
                pcl1.setClusterId(CDMCluster.DEFERRED);
                pcl1.setParentCannotLinkPoint(p);
                pcl1.wasDeferred(true);
                if (!deferred.contains(pcl1)) {
                    deferred.add(pcl1);
                }
            }
        }
    }

    // DBSCAN
    // -----------------------------------------------------------------------

    protected void DBSCAN() {
        // Dataset is UNCLASSIFIED
        Integer ClusterId = nextId(CDMCluster.NOISE);
        
        for (Point point : Dataset) {
            CNBCRTreePoint p = (CNBCRTreePoint) point;
            setClId(p, CDMCluster.UNCLASSIFIED);
            p.ndf = 1;
        }

        // Setting cannot-link points to DEFERRED
        ArrayList<IConstraintObject> def = new ArrayList<>();
        def.addAll(ic.cl1);
        def.addAll(ic.cl2);

        markDeferred(def);

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
                    clusterCount++;
                    // END IF
                }
                // END IF
            }
        }
        // END FOR
        // END;

        deferred.addAll(ic.cl1);
        deferred.addAll(ic.cl2);

        try {
            internalTimer.start("deferred");
            recluster3(deferred);
            internalTimer.end("deferred");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }


    } // DBSCAN

    private void recluster3(ArrayList<IConstraintObject> Rd) throws Exception {
        for (IConstraintObject q : Rd) {
            if (q.getClusterId() > CDMCluster.NOISE) {
                System.out.println("Cluster id: " + q.getClusterId());
                throw new Exception("At this point cluster id should be equal to DEFERRED or to NOISE");
            }
        }

        // Cloning
        ArrayList<IConstraintObject> Rdc = (ArrayList<IConstraintObject>) Rd.clone();

        for (IConstraintObject tq : Rdc) {
            CNBCRTreePoint q = (CNBCRTreePoint) tq;

            if (q.getClusterId() >= CDMCluster.NOISE) {
                continue;
            }

            CNBCRTreePoint nearestClusterPoint = getNearestClusterPointND(q, delta * Eps);

            if (nearestClusterPoint != null) {
                int clusterId_nq = nearestClusterPoint.getClusterId();

                IConstraintObject p = q.getParentCannotLinkPoint();

                ArrayList<IConstraintObject> P_ = ic.getCannotLinkObjects(p);

                boolean assigned = false;

                for (IConstraintObject tp_ : P_) {
                    CNBCRTreePoint p_ = (CNBCRTreePoint) tp_;
                    CNBCRTreePoint point_gp_ = getNearestClusterPointND(p_, delta * Eps);
                    // TODO: What if no clusters were created?

                    if (point_gp_ != null) {
                        int clusterId_gp_ = point_gp_.getClusterId();

                        if (clusterId_nq != clusterId_gp_) {
                            // Clusters are different so q can be assigned to clusterId_nq (nearest cluster)
                            // however, we have to check whether the point is density reachable from the nearest
                            // cluster.

                            // UPDATE: q.ndf does not have to be >= 1
                            // if (q.ndf >= 1) {
                            // We also have to check whether the point is dense.
                            ArrayList<Point> reverseNeighbours =
                                    getNeighbors(nearestClusterPoint, Eps);
                            if (reverseNeighbours.size() > MinPts /*&& reverseNeighbours.contains(q)*/) {
                                // Here, we're checking the reachability.
                                q.setClusterId(clusterId_nq);

                                //log.info("Assigning point " + q.toString() + " to cluster " + clusterId_nq);
                                assigned = true;
                            } /*else {
                            q.setClusterId(CDMCluster.NOISE);
                            assigned = true;
                        }*/
                            //}
                        }
                    }
                }
                if (!assigned) {
                    q.setClusterId(CDMCluster.NOISE);
                }
            } else {
                log.warn("Nearest cluster point not found.");
                q.setClusterId(CDMCluster.NOISE);
            }

        }
    }

    /**
     * ND - Not deferred
     */
    private CNBCRTreePoint getNearestClusterPointND(CNBCRTreePoint p, double radius) {
        CNBCRTreePoint nearestPoint = null;

        ArrayList<Point> neighbors = getNeighbors(p, radius);

        for (Point n : neighbors) {
            CNBCRTreePoint nb = (CNBCRTreePoint) n;
            if (nb.getClusterId() > CDMCluster.NOISE/* && !nb.wasDeferred()*/) {
                nearestPoint = nb;
                break;
            }
        }

        return nearestPoint;
    }

    /**
     * @param p
     * @param radius
     * @return
     */
    public ArrayList<Point> getNeighbors(CNBCRTreePoint p, double radius) {
        ArrayList<Point> neighbours = tree.regionQuery(p, radius);

        // returning the same point
        neighbours.add(p);

        return neighbours;
    }
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

        // adding must-link points
        ArrayList<Point> tmp = new ArrayList<>();
        for(Point q : seeds) {
            ArrayList<IConstraintObject> mls = ic.getMustLinkObjects((IConstraintObject) q);
            for (IConstraintObject ico : mls) {
                tmp.add((CNBCRTreePoint) ico);
            }
        }

        for (Point ico : tmp) {
           if (!seeds.contains(ico)) {
               seeds.add(ico);
           }
        }

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
                                // adding must-link to seeds
                                ArrayList<IConstraintObject> mls = ic.getMustLinkObjects(resultP);
                                for (IConstraintObject ico : mls) {
                                    if (!seeds.contains(ico))
                                        seeds.add((CNBCRTreePoint) ico);
                                }
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
        if (!cnrtp.wasDeferred()) {
            setClId(cnrtp, ClId);
        }
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

}
