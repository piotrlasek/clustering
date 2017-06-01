package org.dmtools.clustering.algorithm.DBSCAN;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.algorithm.CNBC.CNBCRTreePoint;
import org.dmtools.clustering.model.IClusteringData;
import org.dmtools.clustering.model.IClusteringObject;
import org.dmtools.clustering.old.*;
import spatialindex.rtree.RTree;
import spatialindex.spatialindex.IData;
import spatialindex.spatialindex.INode;
import spatialindex.spatialindex.IVisitor;
import spatialindex.storagemanager.IBuffer;
import spatialindex.storagemanager.MemoryStorageManager;
import spatialindex.storagemanager.PropertySet;
import spatialindex.storagemanager.RandomEvictionsBuffer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class DBSCANRTree {

    private final static Logger log = LogManager.getLogger("DBSCANRTree");

    ClusteringLogger logger = new ClusteringLogger(DBSCANAlgorithm.NAME);

    ArrayList<CNBCRTreePoint> SetOfPoints;
    int nDim = 0;
    Double Eps = 0.0;
    Integer MinPts = 0;
    IClusteringData data;

    private RTree tree;

    public void run() {
        logger.indexStart();

        logger.indexEnd();
        logger.clusteringStart();
        DBSCAN();
        logger.clusteringEnd();
    }

    public ArrayList<CNBCRTreePoint> getDataset() {
        return this.SetOfPoints;
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
        // SetOfPoints is UNCLASSIFIED
        Integer ClusterId = nextId(CDMCluster.NOISE);

        for (CNBCRTreePoint point : SetOfPoints) {
            //BasicSpatialObject point = (DbscanSpatialObject) p;
            //point.ClId = UNCLASSIFIED;
            setClId(point, CDMCluster.UNCLASSIFIED);
        }

        // ClusterId := nextId(NOISE);
        // FOR i FROM 1 TO SetOfPoints.size DO
        // Point := SetOfPoints.get(i);
        for (CNBCRTreePoint Point : SetOfPoints) {
            //DbscanSpatialObject Point = (DbscanSpatialObject) p;
            // IF Point.ClId = UNCLASSIFIED THEN
            ///if (Point.ClId == UNCLASSIFIED)
            if (getClId(Point) == CDMCluster.UNCLASSIFIED)
            // IF ExpandCluster(SetOfPoints, Point, ClusterId, Eps, MinPts) THEN
            {
                if (ExpandCluster(SetOfPoints, Point, ClusterId, Eps, MinPts)) {
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
    // ExpandCluster(SetOfPoints, Point, ClId, Eps, MinPts) : Boolean;
    private boolean ExpandCluster(ArrayList<CNBCRTreePoint> SetOfPoints,
                                  CNBCRTreePoint Point, Integer ClId, Double Eps, Integer MinPts) {
        // seeds :=SetOfPoints.regionQuery(Point,Eps);
        ArrayList<CNBCRTreePoint> seeds1 = regionQuery(Point, Eps);
        ArrayList<CNBCRTreePoint> seeds = new ArrayList<CNBCRTreePoint>();
        seeds.addAll(seeds1);
        // IF seeds.size<MinPts THEN // no core point
        if (seeds.size() < MinPts) {
            // SetOfPoint.changeClId(Point,NOISE);
            changeClId(Point, CDMCluster.NOISE);
            // RETURN False;
            return false;
        }
        // ELSE // all points in seeds are density-reachable from Point
        else {
            // SetOfPoints.changeClIds(seeds,ClId);
            changeClId(seeds, ClId);
            // seeds.delete(Point);
            //seeds.remove(Point);
            changeClId(Point, ClId);
            // WHILE seeds <> Empty DO
            while (seeds.size() > 0) {
                // currentP := seeds.first();
                CNBCRTreePoint currentP = seeds.get(0);
                // result := SetOfPoints.regionQuery(currentP, Eps);
                ArrayList<CNBCRTreePoint> result = regionQuery(currentP, Eps);
                // IF result.size >= MinPts THEN
                if (result.size() >= MinPts) {
                    // FOR i FROM 1 TO result.size DO
                    for (int i = 1; i < result.size(); i++) {
                        // resultP := result.get(i);
                        //DbscanSpatialObject resultP = (DbscanSpatialObject) result
                        //        .get(i);
                        CNBCRTreePoint resultP = result.get(i);
                        // IF resultP.ClId IN {UNCLASSIFIED, NOISE} THEN
                        if (getClId(resultP) == CDMCluster.UNCLASSIFIED
                                || getClId(resultP) == CDMCluster.NOISE) {
                            // IF resultP.ClId = UNCLASSIFIED THEN
                            if (getClId(resultP) == CDMCluster.UNCLASSIFIED) {
                                // seeds.append(resultP);
                                seeds.add(resultP);
                                // END IF;
                            }
                            // SetOfPoints.changeClId(resultP,ClId);
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
     * @param Point
     * @param Eps
     * @return
     */
    private ArrayList<CNBCRTreePoint> regionQuery(CNBCRTreePoint Point,
                                                  double Eps) {
        ArrayList<CNBCRTreePoint> neibhgours = new ArrayList();
        MyVisitor kNN = new MyVisitor();
        tree.nearestNeighborQuery(Eps, Point, kNN, SetOfPoints.size());
        return kNN.neighbours;
    }

    /**
     *
     * @param Points
     * @param ClId
     * @return
     */
    public void changeClId(ArrayList<CNBCRTreePoint> Points, int ClId) {
        for (CNBCRTreePoint p : Points)
            changeClId(p, ClId);
    }

    /**
     *
     */
    public void changeClId(CNBCRTreePoint Point, int ClId) {
        //DbscanSpatialObject o = (DbscanSpatialObject) Point;
        //o.ClId = ClId;
        setClId(Point, ClId);
    }

    /**
     *
     * @param Point
     * @return
     */
    public int getClId(CNBCRTreePoint Point)
    {
        return Point.getClusterId();
    }

    /**
     *
     * @param Point
     * @param ClId
     */
    public void setClId(CNBCRTreePoint Point, int ClId)
    {
        Point.setClusterId(ClId);
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
            neighbours.add(SetOfPoints.get(id));
            n.add(d);
        }
    }

    /**
     *
     */
    public IClusteringData getResult() {
        BasicClusteringData bcd = new BasicClusteringData();
        ArrayList<IClusteringObject> al = new ArrayList<IClusteringObject>();

        for (Object o : SetOfPoints) {
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

    public void setData(IClusteringData data) {
        logger.indexStart();
        ArrayList<IClusteringObject> tmp = (ArrayList<IClusteringObject>) data
                .get();
        SetOfPoints = new ArrayList();
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
            SetOfPoints.add(id, mp);
            byte[] d = new byte[]{CDMCluster.UNCLASSIFIED};
            tree.insertData(d, mp, id);
            id++;
        }
        logger.indexEnd();
    }

    public void setEps(double eps) {
        this.Eps = eps;
    }

    public void setMinPts(int minPts) {
        this.MinPts = minPts;
    }

}

