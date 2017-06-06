package org.dmtools.clustering.algorithm.NBC;

import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.algorithm.common.MyVisitor;
import org.dmtools.clustering.algorithm.common.RTreeIndex;
import org.dmtools.clustering.model.IClusteringData;
import org.dmtools.clustering.model.IClusteringObject;
import org.dmtools.clustering.model.IClusteringObserver;
import org.dmtools.clustering.model.IClusteringParameters;
import org.dmtools.clustering.old.*;
import spatialindex.spatialindex.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * 
 * @author Piotr Lasek
 */
public class NBCRTree {
    
    public static final String NAME = "NBC-RTree";
    
    String description;

    IClusteringObserver observer;
    RTreeIndex tree;
    IClusteringParameters parameters;

    ArrayList<Point> Dataset;

    int nDim = 0;
    //int id = 0;
    int k;
    //String desc = "NBC (RTree): ";

    double maxx = 0;

    public void setK(int k) {
        this.k = k;
    }

    /**
     * Creates a new instance of NBC.
     */
    public void run() {
        ArrayList<NBCRTreePoint> NoiseSet = new ArrayList();
        int cluster_count = 0;

        //long begin_time2 = System.currentTimeMillis();
        CalcNDF();
        //long end_time2 = System.currentTimeMillis();
        long time3 = System.currentTimeMillis();
        //desc += " CaldNDF = " + (end_time2 - begin_time2) + " ms";
        //long time4 = System.currentTimeMillis();
        NoiseSet.clear();
        cluster_count = 0;
        ArrayList<NBCRTreePoint> DPSet = new ArrayList();

        //long XY = 0;
        //int xy = 0;

        // for each object p in Dataset
        ListIterator li = Dataset.listIterator();
        while (li.hasNext()) {
            NBCRTreePoint p = (NBCRTreePoint) li.next();
            // if (p.clst_no != NULL or p.ndf < 1)) continue;
            if (p.getClusterId() != -1 || p.ndf < 1)
                continue;
            p.setClusterId(cluster_count); // label a new cluster
            DPSet.clear(); // initialize DPSet

            // for each object q in kNB(p)
            //long timeX = System.currentTimeMillis();
            MyVisitor kNN = new MyVisitor();
            tree.nearestNeighborQuery(k, p, kNN);
            //long timeY = System.currentTimeMillis();
            //XY += (timeY - timeX);

            for (int i = 0; i < kNN.neighbours.size(); i++) {
                // q.clst_no = cluster_count
                NBCRTreePoint q = (NBCRTreePoint) kNN.neighbours.get(i);
                q.setClusterId(cluster_count);
                if (q.ndf >= 1)
                    DPSet.add(q);
            }

            // while (DPSet is not empty) // expanding the cluster
            while (!DPSet.isEmpty()) {
                // p = DPset.getFirstObject();
                NBCRTreePoint pD = (NBCRTreePoint) DPSet.remove(0);

                // for each object q in kNB(pD)
                MyVisitor kNNpD = new MyVisitor();
                tree.nearestNeighborQuery(k, pD, kNNpD);
                ListIterator liKNNpD = kNNpD.neighbours.listIterator();
                while (liKNNpD.hasNext()) {
                    NBCRTreePoint q = (NBCRTreePoint) liKNNpD.next();
                    // System.out.println("pD.id=" +  pD.toString() + ";
                    // q.id= " + q.toString() + "; q.ndf= " + q.ndf);
                    // if (q.clst_no!NULL) continue
                    if (q.getClusterId() != -1)
                        continue;

                    q.setClusterId(cluster_count);

                    // if (q.ndf >= 1) DPSet.add(q)
                    if (q.ndf >= 1)
                        DPSet.add(q);
                }
                // DPSet.remove(p);
            }
            cluster_count++;
        }

        // for each object p in Dataset // label noise
        li = Dataset.listIterator();
        while (li.hasNext()) {
            // if (p.clst_no=NULL) NoiseSet.add(p)
            NBCRTreePoint p = (NBCRTreePoint) li.next();
            if (p.getClusterId() == CDMCluster.UNCLASSIFIED)
                p.setClusterId(CDMCluster.NOISE);
        }
    }

    private void CalcNDF() {
        ListIterator li = Dataset.listIterator();
        li = Dataset.listIterator();
        ArrayList CandidateSet = new ArrayList();
        MyVisitor kNN = new MyVisitor();

        // for each object p in Dataset
        while (li.hasNext()) {
            NBCRTreePoint p = (NBCRTreePoint) li.next();
            CandidateSet.clear();

            kNN.reset();
            tree.nearestNeighborQuery(k, p, kNN);
            p.numberOfkNB = kNN.kNB;
            // System.out.println("p: " + p.excell());

            // for each new object q in kNB(p)
            ListIterator lni = kNN.neighbours.listIterator();
            while (lni.hasNext()) {
                NBCRTreePoint q = (NBCRTreePoint) lni.next();
                q.numberOfRkNB++;
                // System.out.println("q: " + q.excell());
            }
        }

        // for each object q in kNB(p)
        li = Dataset.listIterator();
        while (li.hasNext()) {
            NBCRTreePoint p = (NBCRTreePoint) li.next();
            p.ndf = (double) p.numberOfRkNB / p.numberOfkNB;

//            System.out.println("p.SizeOfRkNB= " + p.numberOfRkNB +
//        			"; p.SizeOfkNB= " + p.numberOfkNB +
//        			"; p.ndf= " + p.ndf);
        }


        // printing
        // li = Dataset.listIterator();
        // while ( li.hasNext() )
        // {
        // MyPoint p = (MyPoint) li.next();
        // System.out.println(p.excell());
        // }
    }

    /**
     * 
     * @throws IOException
     */
    public void initRTree() throws IOException {
        tree = new RTreeIndex();
        tree.initRTree(Dataset, nDim);
    }

    public IClusteringData getResult() {
        BasicClusteringData bcd = new BasicClusteringData();
        ArrayList<IClusteringObject> al = new ArrayList<IClusteringObject>();
        for (Object o : Dataset) {
            NBCRTreePoint mp = (NBCRTreePoint) o;
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
    }

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
            double[] values = ico.getSpatialObject().getValues();
            if (values[0] > maxx) maxx = values[0];
            NBCRTreePoint mp = new NBCRTreePoint(ico.getSpatialObject()
                    .getValues(), -1);
            Dataset.add(id, mp);
            byte[] d = new byte[]{-1};
            tree.insertData(d, mp, id);
            id++;
        }
    }
}
