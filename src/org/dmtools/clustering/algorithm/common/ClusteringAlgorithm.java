package org.dmtools.clustering.algorithm.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.algorithm.CNBC.CNBCRTreePoint;
import org.dmtools.clustering.model.IClusteringData;
import org.dmtools.clustering.model.IClusteringObject;
import org.dmtools.clustering.old.*;
import spatialindex.spatialindex.Point;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Piotr Lasek on 01.06.2017.
 */
public class ClusteringAlgorithm {

    protected final static Logger log =
        LogManager.getLogger(ClusteringAlgorithm.class.getSimpleName());

    public int nDim = 0;
    public ArrayList<Point> Dataset;
    public RTreeIndex tree;
    public double maxx;
    protected ClusteringTimer internalTimer = new ClusteringTimer();
    protected int clusterCount = 0;

    /**
     *
     * @throws IOException
     */
    public void initRTree() throws IOException {
        tree = new RTreeIndex();
        tree.initRTree(Dataset, nDim);
    }

    /**
     *
     * @param data
     */
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

        // building R-Tree

        int id = 0;

        for (IClusteringObject ico : tmp) {
            double[] values = ico.getSpatialObject().getValues();
            if (values[0] > maxx) maxx = values[0];
            CNBCRTreePoint mp = new CNBCRTreePoint(ico.getSpatialObject()
                    .getValues(), CDMCluster.UNCLASSIFIED);
            Dataset.add(id, mp);
            byte[] d = new byte[]{CDMCluster.UNCLASSIFIED};
            tree.insertData(d, mp, id);
            id++;
        }
    }

    /**
     *
     */
    public IClusteringData getResult() {
        BasicClusteringData bcd = new BasicClusteringData();
        ArrayList<IClusteringObject> al = new ArrayList<IClusteringObject>();

        for (Object o : Dataset) {
            CNBCRTreePoint mp = (CNBCRTreePoint) o;
            BasicClusteringObject bco = new BasicClusteringObject();

            if (mp.wasDeferred()) {
                bco.addParameter("wasDeferred", "true");
            }

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

    /**
     *
     * @return
     */
    public ArrayList<Point> getDataset() {
        return Dataset;
    }

    /**
     *
     * @return
     */
    public ClusteringTimer getInternalTimer() {
        return internalTimer;
    }

    /**
     *
     * @return
     */
    public int clusterCount() {
        return clusterCount;
    }
}
