package org.dmtools.clustering.algorithm.NBC;

import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.algorithm.CNBC.CNBCRTreePoint;
import org.dmtools.clustering.algorithm.common.ClusteringAlgorithm;
import org.dmtools.clustering.algorithm.common.MyVisitor;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * 
 * @author Piotr Lasek
 */
public class NBCRTree extends ClusteringAlgorithm {
    
    int k;

    public void setK(int k) {
        this.k = k;
    }

    /**
     * Creates a new instance of NBC.
     */
    public void run() {
        ArrayList<CNBCRTreePoint> NoiseSet = new ArrayList();
        CalcNDF();
        NoiseSet.clear();
        ArrayList<CNBCRTreePoint> DPSet = new ArrayList();

        //long XY = 0;
        //int xy = 0;

        // for each object p in Dataset
        ListIterator li = Dataset.listIterator();
        while (li.hasNext()) {
            CNBCRTreePoint p = (CNBCRTreePoint) li.next();
            // if (p.clst_no != NULL or p.ndf < 1)) continue;
            if (p.getClusterId() != CDMCluster.UNCLASSIFIED ||
                    p.ndf < 1) {
                continue;
            }
            p.setClusterId(clusterCount); // label a new cluster
            DPSet.clear(); // initialize DPSet

            // for each object q in kNB(p)
            //long timeX = System.currentTimeMillis();
            MyVisitor kNN = new MyVisitor();
            tree.nearestNeighborQuery(k, p, kNN);
            //long timeY = System.currentTimeMillis();
            //XY += (timeY - timeX);

            for (int i = 0; i < kNN.neighbours.size(); i++) {
                // q.clst_no = cluster_count
                CNBCRTreePoint q = (CNBCRTreePoint) kNN.neighbours.get(i);
                q.setClusterId(clusterCount);
                if (q.ndf >= 1)
                    DPSet.add(q);
            }

            // while (DPSet is not empty) // expanding the cluster
            while (!DPSet.isEmpty()) {
                // p = DPset.getFirstObject();
                CNBCRTreePoint pD = (CNBCRTreePoint) DPSet.remove(0);

                // for each object q in kNB(pD)
                MyVisitor kNNpD = new MyVisitor();
                tree.nearestNeighborQuery(k, pD, kNNpD);
                ListIterator liKNNpD = kNNpD.neighbours.listIterator();
                while (liKNNpD.hasNext()) {
                    CNBCRTreePoint q = (CNBCRTreePoint) liKNNpD.next();
                    // System.out.println("pD.id=" +  pD.toString() + ";
                    // q.id= " + q.toString() + "; q.ndf= " + q.ndf);
                    // if (q.clst_no!NULL) continue
                    if (q.getClusterId() != CDMCluster.UNCLASSIFIED)
                        continue;

                    q.setClusterId(clusterCount);

                    // if (q.ndf >= 1) DPSet.add(q)
                    if (q.ndf >= 1)
                        DPSet.add(q);
                }
                // DPSet.remove(p);
            }
            clusterCount++;
        }

        // for each object p in Dataset // label noise
        li = Dataset.listIterator();
        while (li.hasNext()) {
            // if (p.clst_no=NULL) NoiseSet.add(p)
            CNBCRTreePoint p = (CNBCRTreePoint) li.next();
            if (p.getClusterId() == CDMCluster.UNCLASSIFIED)
                p.setClusterId(CDMCluster.NOISE);
        }

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
