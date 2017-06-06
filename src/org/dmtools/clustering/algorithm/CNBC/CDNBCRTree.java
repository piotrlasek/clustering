package org.dmtools.clustering.algorithm.CNBC;

import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.algorithm.common.InstanceConstraintsAlgorithm;
import org.dmtools.clustering.algorithm.common.MyVisitor;
import org.dmtools.clustering.model.IConstraintObject;
import spatialindex.spatialindex.IShape;
import spatialindex.spatialindex.Point;

import java.util.ArrayList;
import java.util.ListIterator;


/**
 * 
 * @author Piotr Lasek
 */
public class CDNBCRTree extends InstanceConstraintsAlgorithm {

    private int k;

    /**
     *
     * @param k
     */
    public void setK(int k) {
        this.k = k;
    }

    /**
     * The algorithm.
     */
    public void run() {
        ArrayList<CNBCRTreePoint> NoiseSet = new ArrayList();
        int cluster_count = 0;

        CalcNDF();

        NoiseSet.clear();
        cluster_count = 0;
        ArrayList<CNBCRTreePoint> DPSet = new ArrayList();

        // Setting cannot-link points to DEFERRED
        ArrayList<IConstraintObject> def = new ArrayList<>();
        def.addAll(ic.cl1);
        def.addAll(ic.cl2);

        markDeferred(def);

        // for each object p in Dataset
        ListIterator li = Dataset.listIterator();
        while (li.hasNext()) {
            CNBCRTreePoint p = (CNBCRTreePoint) li.next();
            // if (p.getClusterId() != NULL or p.ndf < 1)) continue;
            if (p.getClusterId() != CDMCluster.UNCLASSIFIED ||
                    // # p.getClusterId() == CDMCluster.DEFERRED ||
                    p.ndf < 1) {
                continue;
            }
            p.setClusterId(cluster_count); // label a new cluster
            DPSet.clear(); // initialize DPSet

            MyVisitor kNN = new MyVisitor();
            tree.nearestNeighborQuery(k, p, kNN);

            // applying cannot-link constraints
            ArrayList<CNBCRTreePoint> knNNeighbours = new ArrayList();
            for(Point pic : knNNeighbours) knNNeighbours.add((CNBCRTreePoint) pic);

            boolean existsCannotLink = ic.existsCannotLink(knNNeighbours);
            // CANNOT-LINK
            if (existsCannotLink) {
                for (int i = 0; i < kNN.neighbours.size(); i++) {
                    CNBCRTreePoint pcl1 = (CNBCRTreePoint) kNN.neighbours.get(i);
                    //pcl1.setClusterId(CDMCluster.NOISE);
                }
                continue;
            } // end of applying cannot-link constraints
            else {
                for (int i = 0; i < kNN.neighbours.size(); i++) {
                    // q.clst_no = cluster_count
                    CNBCRTreePoint q = (CNBCRTreePoint) kNN.neighbours.get(i);

                    if (q.getClusterId() == CDMCluster.UNCLASSIFIED) {
                        q.setClusterId(cluster_count);
                        if (q.ndf >= 1) {
                            DPSet.add(q);

                            // MUST-LINK
                            ArrayList<IConstraintObject> mls = ic.getMustLinkObjects(q);
                            if (mls.size() > 0) {
                                for (IConstraintObject ico : mls) {
                                    DPSet.add((CNBCRTreePoint) ico);
                                }
                                // DPSet.addAll(mls);
                            }
                        }
                    }
                }
            }

            // while (DPSet is not empty) // expanding the cluster
            while (!DPSet.isEmpty()) {
                // p = DPset.getFirstObject();
                CNBCRTreePoint pD = (CNBCRTreePoint) DPSet.remove(0);

                // for each object q in kNB(pD)
                MyVisitor kNNpD = new MyVisitor();
                tree.nearestNeighborQuery(k, pD, kNNpD);

                ListIterator<Point> liKNNpD =
                        kNNpD.neighbours.listIterator();

                while (liKNNpD.hasNext()) {
                    CNBCRTreePoint q = (CNBCRTreePoint) liKNNpD.next();
                    // if (q.clst_no!NULL) continue
                    if (q.getClusterId() != CDMCluster.UNCLASSIFIED)
                        continue;

                    q.setClusterId(cluster_count); // TODO cluster count

                    // if (q.ndf >= 1) DPSet.add(q)
                    if (q.ndf >= 1) {
                        DPSet.add(q);
                        // MUST-LINK
                        ArrayList<IConstraintObject> mls = ic.getMustLinkObjects(q);
                        if (mls.size() > 0) {
                            for (IConstraintObject ico : mls) {
                                DPSet.add((CNBCRTreePoint) ico);
                            }
//	                    	DPSet.addAll(mls);
                        }
                    }

                    ArrayList<IConstraintObject> mls = ic.getMustLinkObjects(q);
                    for (IConstraintObject ico : mls) {
                        DPSet.add((CNBCRTreePoint) ico);
                    }
//                    DPSet.addAll(mls);
                }
                // DPSet.remove(p);
            }
            cluster_count++;
        }

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

        /*
        for (IConstraintObject ico : deferred) {
            CNBCRTreePoint icoTreePoint = new CNBCRTreePoint(ico.getValues());
            if (Dataset.contains(icoTreePoint)) {
                int i = Dataset.indexOf(icoTreePoint);
                CNBCRTreePoint nrtp;
                nrtp = (CNBCRTreePoint) Dataset.get(i);
                nrtp.setClusterId(ico.getClusterId());
            }
        }

        // for each object p in Dataset // label noise
        li = Dataset.listIterator();
        while (li.hasNext()) {
            // if (p.getClusterId()=NULL) NoiseSet.add(p)
            CNBCRTreePoint p = (CNBCRTreePoint) li.next();
            if (p.getClusterId() == CDMCluster.UNCLASSIFIED)
                p.setClusterId(CDMCluster.NOISE);
        }
        */
    }

    /**
     * ND - Not deferred
     */
    private CNBCRTreePoint getNearestClusterPointND(CNBCRTreePoint p, int kn) {
        CNBCRTreePoint nearestPoint = null;

        ArrayList<CNBCRTreePoint> neighbors = getNeighbors(p, kn);

        for (CNBCRTreePoint nb : neighbors) {
            if (nb.getClusterId() > CDMCluster.NOISE && !nb.wasDeferred()) {
                nearestPoint = nb;
                break;
            }
        }

        return nearestPoint;
    }

    /**
     * @param p
     * @param kn
     * @return
     */
    private CNBCRTreePoint getNearestClusterPoint(CNBCRTreePoint p, int kn) {
        CNBCRTreePoint nearestPoint = null;

        ArrayList<CNBCRTreePoint> neighbors = getNeighbors(p, kn);

        for (CNBCRTreePoint nb : neighbors) {
            if (nb.getClusterId() > CDMCluster.NOISE) {
                nearestPoint = nb;
                break;
            }
        }

        return nearestPoint;
    }

    /**
     *
     * @param p
     * @param kn
     * @return
     */
    private CNBCRTreePoint getNearestCL(CNBCRTreePoint p, int kn) {
        CNBCRTreePoint cl = null;

        ArrayList<CNBCRTreePoint> neighbors = getNeighbors(p, 2 * kn);

        for (CNBCRTreePoint nb : neighbors) {
            if (nb.isCannotLinkPoint()) {
                cl = nb;
                break;
            }
        }

        return cl;
    }

    /**
     * @param def
     */
    private void markDeferred(ArrayList<IConstraintObject> def) {
        for (IConstraintObject p : def) {
            MyVisitor kNN = new MyVisitor();
            tree.nearestNeighborQuery(k, (IShape) p, kNN);

            p.setClusterId(CDMCluster.DEFERRED);
            p.setParentCannotLinkPoint(p);
            p.wasDeferred(true);
            deferred.add(p);

            if (!deferred.contains(p)) {
                deferred.add(p);
            }

            for (int i = 0; i < kNN.neighbours.size(); i++) {
                CNBCRTreePoint pcl1 = (CNBCRTreePoint) kNN.neighbours.get(i);
                pcl1.setClusterId(CDMCluster.DEFERRED);
                pcl1.setParentCannotLinkPoint(p);
                pcl1.wasDeferred(true);
                if (!deferred.contains(pcl1)) {
                    deferred.add(pcl1);
                }
            }
        }
    }

    /**
     * @param Rd
     */
    private void recluster3(ArrayList<IConstraintObject> Rd) throws Exception {
        int deferredSize = Rd.size();
        int iteration = 0;
        int clusters = 0;
        int noise = 0;

        // check for assigned objects

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

            iteration++;

            if (q.getClusterId() >= CDMCluster.NOISE) {
                continue;
            }

            CNBCRTreePoint nearestClusterPoint = getNearestClusterPointND(q, 2*k);
            int clusterId_nq = nearestClusterPoint.getClusterId();

            IConstraintObject p = q.getParentCannotLinkPoint();

            ArrayList<IConstraintObject> P_ = ic.getCannotLinkObjects(p);

            boolean assigned = false;

            for (IConstraintObject tp_ : P_) {
                CNBCRTreePoint p_ = (CNBCRTreePoint) tp_;
                CNBCRTreePoint point_gp_ = getNearestClusterPointND(p_, 3*k);
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
                        ArrayList<CNBCRTreePoint> reverseNeighbours =
                                getNeighbors(nearestClusterPoint, k);
                        if (reverseNeighbours.contains(q)) {
                            // Here, we're checking the reachability.
                            q.setClusterId(clusterId_nq);
                            assigned = true;
                            clusters++;
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
                noise++;
            }
        }

        log.info("Deffered:   " + deferredSize);
        log.info("Iterations: " + iteration);
        log.info("Clusters:   " + clusters);
        log.info("Noise:      " + noise);
    }

    /**
     * @param in
     */
    private void recluster2(ArrayList<IConstraintObject> in) {
        ArrayList<CNBCRTreePoint> toRemove = new ArrayList<CNBCRTreePoint>();
        int step = 0;
        do {
            toRemove.clear();

            for (IConstraintObject p : in) {

                if (((CNBCRTreePoint) p).ndf >= 1) {
                    ArrayList<CNBCRTreePoint> neighbors = getNeighbors((CNBCRTreePoint) p, 2 * k);

                    for (CNBCRTreePoint q : neighbors) {
                        if (q.ndf >= 1 && q.getClusterId() > CDMCluster.NOISE && !q.wasDeferred()) {

                            boolean cl1 = false;

                            if (ic.cannotAsignToGroup(p, q.getClusterId())) {
                                cl1 = true;
                            }

                            if (ic.cannotAsignToGroup(getNearestCL((CNBCRTreePoint) p, k), q.getClusterId())) {
                                cl1 = true;
                            }
						 
						/*   if (q.parentCL != null && ic.cannotLink(p.parentCL, q.parentCL)) {
							 cl1 = true;
							}*/

                            if (!cl1 && q.ndf >= 1 /*&& q.referseNeighbors.contains(p)*/) {
                                //if (p.ndf >= 1) {
                                p.setClusterId(q.getClusterId());
                                p.wasDeferred(true);
                                toRemove.add((CNBCRTreePoint) p);
                                break;
//	        				} else {
//	        					p.wasDeferred = true;
//	        					p.getClusterId() = NOISE;
//	        					toRemove.add(p);
//	        					break;
//	        				}
                            }
                        }

                    }
                }
                //System.out.println();
            }

            in.removeAll(toRemove);

        } while (toRemove.size() > 0);
    }

    /**
     * TODO: This method is not used in the algorithm!
     */
    private void recluster() {
        ArrayList<CNBCRTreePoint> toRemove = new ArrayList<CNBCRTreePoint>();
        int step = 0;
        do {
            toRemove.clear();

            for (IConstraintObject p : deferred) {

                if (((CNBCRTreePoint) p).ndf >= 1) {
                    ArrayList<CNBCRTreePoint> neighbors = getNeighbors((CNBCRTreePoint) p, k);

                    for (CNBCRTreePoint q : neighbors) {
                        if (q.ndf >= 1 && q.getClusterId() >= CDMCluster.NOISE && !q.wasDeferred()) {
                            boolean cl1 = false;
            		
                		 
                		 /*
                		 ArrayList<CNBCRTreePoint> nn = getNeighbors(p,  k);
                		 
                		 for(CNBCRTreePoint ctp:nn) {
                			 if (ic.cannotAsignToGroup(ctp,  q.clst_no)) {
                				 cl1 = true;
                				 break;
                			 }
                		 }
                		 */

                            if (ic.cannotAsignToGroup(p, q.getClusterId())) {
                                cl1 = true;
                            }

                            if (ic.cannotAsignToGroup(p.getParentCannotLinkPoint(), q.getClusterId())) {
                                cl1 = true;
                            }

                            if (ic.cannotLink(p.getParentCannotLinkPoint(), q.getParentCannotLinkPoint())) {
                                cl1 = true;
                            }

                            if (!cl1) {
                                p.setClusterId(q.getClusterId());
                                p.wasDeferred(true);
                                toRemove.add((CNBCRTreePoint) p);
                            }

                            break;
                        } else {
                            p.setClusterId(CDMCluster.NOISE);
                            p.wasDeferred(true);
                            toRemove.add((CNBCRTreePoint) p);
                        }
                    }
                }
            }

            deferred.removeAll(toRemove);
            step++;

        } while (toRemove.size() > 0 && step < 5);

        for (IConstraintObject tp : deferred) {


            ArrayList<CNBCRTreePoint> nbs = getNeighbors((CNBCRTreePoint) tp, k);

            for (CNBCRTreePoint n : nbs) {
                if (!n.wasDeferred() && n.getClusterId() > CDMCluster.NOISE) {

                }
            }
        }
    }

    /**
     * @param p
     * @param q
     * @param clusterId
     * @return
     */
    public boolean canBeAssigned(CNBCRTreePoint p, CNBCRTreePoint q, int clusterId) {
        boolean can = true;

        boolean c1 = ic.cannotLinkExt(p, q);

        return can;
    }


    /**
     * @param p
     * @param k
     * @return
     */
    public CNBCRTreePoint getFirtsNeighbor(CNBCRTreePoint p, int k) {
        ArrayList<CNBCRTreePoint> points = getNeighbors(p, k);
        CNBCRTreePoint px = null;
        for (CNBCRTreePoint pt : points) {
            if (pt.ndf >= 1 && pt.getClusterId() > CDMCluster.NOISE) {
                px = pt;
                break;
            }
        }
        return px;
    }

    /**
     * @param p
     * @param k
     * @return
     */
    public ArrayList<CNBCRTreePoint> getNeighbors(CNBCRTreePoint p, int k) {
        MyVisitor $1NN = new MyVisitor();
        tree.nearestNeighborQuery(k + 1, p, $1NN);

        ListIterator<Point> n =
                $1NN.neighbours.listIterator();

        ArrayList<CNBCRTreePoint> neighbors = new ArrayList<CNBCRTreePoint>();

        // returning the same point
        neighbors.add(p);

        if (n.hasNext()) n.next();

        while (n.hasNext()) {
            neighbors.add((CNBCRTreePoint) n.next());
        }


        return neighbors;

    }

    /**
     *
     */
    private void CalcNDF() {
        ListIterator li = Dataset.listIterator();
        li = Dataset.listIterator();
        ArrayList CandidateSet = new ArrayList();
        MyVisitor kNN = new MyVisitor();

        // for each object p in Dataset
        while (li.hasNext()) {
            CNBCRTreePoint p = (CNBCRTreePoint) li.next();
            CandidateSet.clear();

            kNN.reset();
            tree.nearestNeighborQuery(k, p, kNN);
            p.numberOfkNB = kNN.kNB;
            // System.out.println("p: " + p.excell());

            // for each new object q in kNB(p)
            ListIterator lni = kNN.neighbours.listIterator();
            while (lni.hasNext()) {
                CNBCRTreePoint q = (CNBCRTreePoint) lni.next();
                q.numberOfRkNB++;
                q.reverseNeighbors.add(p);
                // System.out.println("q: " + q.excell());
            }
        }

        // for each object q in kNB(p)
        li = Dataset.listIterator();
        while (li.hasNext()) {
            CNBCRTreePoint p = (CNBCRTreePoint) li.next();
            p.ndf = (double) p.numberOfRkNB / p.numberOfkNB;
        }
    }

}
