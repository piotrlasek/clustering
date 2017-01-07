package org.dmtools.clustering.old.rtree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.dmtools.clustering.model.ISpatialIndex;
import org.dmtools.clustering.model.ISpatialObject;

import spatialindex.rtree.RTree;
import spatialindex.storagemanager.IBuffer;
import spatialindex.storagemanager.MemoryStorageManager;
import spatialindex.storagemanager.PropertySet;
import spatialindex.storagemanager.RandomEvictionsBuffer;


public class CustomRTree implements ISpatialIndex {
    
    public static final String NAME = "R-Tree";
    
    int nDim = 0;
    
    RTree tree;
    CustomRTreeVisitor visitor;
    ArrayList<ISpatialObject> Dataset;
    
    @Override
    public String getName() {
        return CustomRTree.NAME;
    }

    
    @Override
    public void add(Collection<ISpatialObject> objectsList) {
        
        nDim = objectsList.iterator().next().getValues().length;
        Dataset = new ArrayList<ISpatialObject>();
        
        try {
            initRTree();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
        
        int id = 0; // index of point in data set
        
        // building R-Tree
        for (ISpatialObject iso : objectsList) {
            CustomRTreePoint rtp = new CustomRTreePoint(iso.getValues());
            byte[] d = new byte[]{-1};
            Dataset.add(id, iso);
            tree.insertData(d, rtp, id);
            id++;
        }
        
        visitor = new CustomRTreeVisitor(Dataset);
    }

    @Override
    public Collection<ISpatialObject> getNeighbors(ISpatialObject object,
            int count, double max) {
        // TODO: exception type to be changed
        throw new NullPointerException("Not supported");
        
    }
    
    @Override
    public Collection<ISpatialObject> getNeighbors(ISpatialObject object,
            double max) {
        CustomRTreePoint ctp = new CustomRTreePoint(object.getValues());
        visitor.clear();
        tree.nearestNeighborQuery(max, ctp, visitor, Dataset.size());        
        return visitor.getNeighbors();
    }
    
    @Override
    public Collection<ISpatialObject> getNeighbors(ISpatialObject object,
            int count) {
        CustomRTreePoint ctp = new CustomRTreePoint(object.getValues());
        visitor.clear();
        tree.nearestNeighborQuery(count, ctp, visitor);
        return visitor.getNeighbors();
    }

    /**
     * 
     * @param data
     */
    /*public void setData(IClusteringData data) {
        ArrayList<IClusteringObject> tmp = (ArrayList<IClusteringObject>) data
                .get();
        Dataset = new ArrayList<CustomRTreePoint>();
        nDim = data.get().iterator().next().getSpatialObject().getValues().length;
        try {
            initRTree();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
        
        int id = 0; // index of object in "Dataset"
        
        // building R-Tree
        for (IClusteringObject ico : tmp) {
            CustomRTreePoint mp = new CustomRTreePoint(ico.getSpatialObject().getValues());
            byte[] d = new byte[]{-1};
            Dataset.add(mp);
            tree.insertData(d, mp, id);
            id++;
        }
        
        visitor = new CustomRTreeVisitor(Dataset);
    }*/
    
    /**
     * 
     * @throws IOException
     */
    public void initRTree() throws IOException {
        PropertySet ps = new PropertySet();
        Boolean b = new Boolean(true);
        ps.setProperty("Overwrite", b);

        // overwrite the file if it exists.
        ps.setProperty("FileName", "nbc-rtree");
        Integer i = new Integer(128);
        ps.setProperty("PageSize", i);

        PropertySet ps2 = new PropertySet();

        Double f = new Double(0.1);

        ps2.setProperty("FillFactor", f);

        i = new Integer(32);

        ps2.setProperty("IndexCapacity", i);
        ps2.setProperty("LeafCapacity", i);
        // Index capacity and leaf capacity may be different.

        i = new Integer(nDim);
        ps2.setProperty("Dimension", i);

        MemoryStorageManager memmanag = new MemoryStorageManager();
        IBuffer mem = new RandomEvictionsBuffer(memmanag, 40000, false);

        tree = new RTree(ps2, mem);
    }
}
