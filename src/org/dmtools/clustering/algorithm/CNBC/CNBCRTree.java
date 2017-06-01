package org.dmtools.clustering.algorithm.CNBC;

import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.model.*;
import org.dmtools.clustering.old.*;
import spatialindex.rtree.RTree;
import spatialindex.spatialindex.IData;
import spatialindex.spatialindex.INode;
import spatialindex.spatialindex.ISpatialIndex;
import spatialindex.spatialindex.IVisitor;
import spatialindex.storagemanager.IBuffer;
import spatialindex.storagemanager.MemoryStorageManager;
import spatialindex.storagemanager.PropertySet;
import spatialindex.storagemanager.RandomEvictionsBuffer;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;


/**
 * 
 * @author Piotr Lasek
 */
public class CNBCRTree implements IClusteringAlgorithm {
    
    public static final String NAME = "C-NBC-RTree";

    
    String description;

    IClusteringObserver observer;
    ISpatialIndex tree;
    IClusteringParameters parameters;

    ArrayList<CNBCRTreePoint> Dataset;
    
    ClusteringTimer logger = new ClusteringTimer();
    
    InstanceConstraints ic = new InstanceConstraints();

    int nDim = 0;
    //int id = 0;
    int k;
    //String desc = "NBC (RTree): ";

    /**
     * Creates a new instance of the NBC algorithm.
     */
    public void run() {
        logger.addDescription(this.getDescription());
        //long begin_time1 = System.currentTimeMillis();
        logger.clusteringStart();

        ArrayList<CNBCRTreePoint> NoiseSet = new ArrayList();
        int cluster_count = 0;

        CalcNDF();
        long time3 = System.currentTimeMillis();
        NoiseSet.clear();
        cluster_count = 0;
        ArrayList<CNBCRTreePoint> DPSet = new ArrayList();
        
        // Setting cannot-link points to NOISE
        
        for(IConstraintObject icp : ic.cl1) {
            CNBCRTreePoint p = (CNBCRTreePoint) icp;
	        MyVisitor kNN = new MyVisitor();
	        tree.nearestNeighborQuery(k, p, kNN);
	        
	        for (int i = 0; i < kNN.neighbours.size(); i++) {
	        	CNBCRTreePoint pcl1 = kNN.neighbours.get(i);
	            pcl1.setClusterId(CDMCluster.NOISE);
	        }
        }
        
        for(IConstraintObject icp : ic.cl2) {
            CNBCRTreePoint p = (CNBCRTreePoint) icp;
	        MyVisitor kNN = new MyVisitor();
	        tree.nearestNeighborQuery(k, p, kNN);
	        
	        for (int i = 0; i < kNN.neighbours.size(); i++) {
	        	CNBCRTreePoint pcl1 = kNN.neighbours.get(i);
                pcl1.setClusterId(CDMCluster.NOISE);
	        }
        }

        // for each object p in Dataset
        ListIterator li = Dataset.listIterator();
        while (li.hasNext()) {
            CNBCRTreePoint p = (CNBCRTreePoint) li.next();
            // if (p.clst_no != NULL or p.ndf < 1)) continue;
            if (p.getClusterId() != CDMCluster.UNCLASSIFIED || p.ndf < 1)
                continue;
            p.setClusterId(cluster_count); // label a new cluster
            DPSet.clear(); // initialize DPSet

            MyVisitor kNN = new MyVisitor();
            tree.nearestNeighborQuery(k, p, kNN);
            
            // applying cannot-link constraints
            boolean existsCannotLink = ic.existsCannotLink(kNN.neighbours);
            // CANNOT-LINK
            if (existsCannotLink){
            	for (int i = 0; i < kNN.neighbours.size(); i++) {
            		CNBCRTreePoint pcl1 = (CNBCRTreePoint) kNN.neighbours.get(i);
            		if (pcl1.getClusterId() != CDMCluster.UNCLASSIFIED) {
            			System.out.println("XXX");
            		}
                    pcl1.setClusterId(CDMCluster.NOISE);
                }
                continue;
            } // end of applying cannot-link constraints
            else {
	            for (int i = 0; i < kNN.neighbours.size(); i++) {
	                // q.clst_no = cluster_count
	                CNBCRTreePoint q = (CNBCRTreePoint) kNN.neighbours.get(i);
	                q.setClusterId(cluster_count);
	                if (q.ndf >= 1) {
	                    DPSet.add(q);

	                    // MUST-LINK
	                    ArrayList<IConstraintObject> mls = ic.getMustLinkObjects(q);
	                    if (mls.size() > 0) {
                            for(IConstraintObject icp : mls) {
                               DPSet.add((CNBCRTreePoint) icp);
                            }
	                    	// DPSet.addAll(mls);
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

                ListIterator<CNBCRTreePoint> liKNNpD =
                		kNNpD.neighbours.listIterator();

                while (liKNNpD.hasNext()) {
                    CNBCRTreePoint q = (CNBCRTreePoint) liKNNpD.next();
                    // if (q.clst_no!NULL) continue
                    if (q.getClusterId() != CDMCluster.UNCLASSIFIED)
                        continue;

                    q.setClusterId(cluster_count);

                    // if (q.ndf >= 1) DPSet.add(q)
                    if (q.ndf >= 1) {
                        DPSet.add(q);
                        // MUST-LINK
	                    ArrayList<IConstraintObject> mls = ic.getMustLinkObjects(q);
	                    if (mls.size() > 0) {
                            for(IConstraintObject icp : mls) {
                               DPSet.add((CNBCRTreePoint) icp);
                            }
	                    	//DPSet.addAll(mls);
	                    }
                    }

                    ArrayList<IConstraintObject> mls = ic.getMustLinkObjects(q);
                    for(IConstraintObject icp : mls) {
                       DPSet.add((CNBCRTreePoint) icp);
                    }
                    // DPSet.addAll(mls);
                }
                // DPSet.remove(p);
            }
            cluster_count++;
        }

        // for each object p in Dataset // label noise
        li = Dataset.listIterator(); // TODO add dump method to Dataset
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

    /**
     * 
     * @throws IOException
     */
    public void initRTree() throws IOException {
        /*PropertySet ps = new PropertySet();
        Boolean b = new Boolean(true);
        ps.setProperty("Overwrite", b);
        ps.setProperty("FileName", "nbc-rtree");
        Integer i = new Integer(128);
        ps.setProperty("PageSize", i);*/
        
        PropertySet ps = new PropertySet();
        ps.setProperty("FileName", "nbc-rtree");
        ps.setProperty("FillFactor", 0.1);
        ps.setProperty("IndexCapacity", 32);
        ps.setProperty("LeafCapacity", 32);
        ps.setProperty("Dimension", nDim);

        MemoryStorageManager memmanag = new MemoryStorageManager();
        IBuffer mem = new RandomEvictionsBuffer(memmanag, 40000, false);

        tree = new RTree(ps, mem);
    }

    /**
     * The implementation of the IVisitor interface.
     */
    class MyVisitor implements IVisitor {
        public int m_indexIO = 0;
        public int m_leafIO = 0;
        public int kNB = 0;

        ArrayList<CNBCRTreePoint> neighbours = new ArrayList<CNBCRTreePoint>();
        ArrayList<IData> n = new ArrayList<IData>();

        public void reset() {
            kNB = 0;
            neighbours.clear();
        }

        public void visitNode(final INode n) {
            if (n.isLeaf())
                m_leafIO++;
            else
                m_indexIO++;
        }

        public void visitData(final IData d) {
            kNB++;
            int id = d.getIdentifier();
            neighbours.add(Dataset.get(id));
            n.add(d);
        }
    }

    /**
     *
     * @return
     */
    public InstanceConstraints getConstraints() {
    	return ic;
    }

    @Override
    /**
     *
     */
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
        // Dump.toFile(Dataset, "cnbc-rtree.csv", true); //data to dump

        return bcd;
    }

    @Override
    public void setData(IClusteringData data) {
        logger.indexStart();
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
        logger.indexEnd();
    }

    @Override
    public IClusteringParameters getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(IClusteringParameters parameters) {
        this.parameters = parameters;
        k = new Integer(parameters.getValue("k")).intValue();
        logger.setParameters(parameters.toString());
    }

    @Override
    public void setObserver(IClusteringObserver observer) {
        // TODO Auto-generated method stub
        this.observer = observer;
    }

    public void setGraphics(Graphics g) {

    }

    public void addLines() {
        this.observer.handleNotify((Object) null);
    }

    @Override
    public String getName() {
        return CNBCRTree.NAME;
    }

    /**
     * 
     */
    public void addDescription(String description) {
        this.description = description;
    }
    
    /**
     * 
     */
    public String getDescription() {
        return description;
    }
    
}
