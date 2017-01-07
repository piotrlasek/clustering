package org.dmtools.clustering.algorithm.DBSCAN;

import org.dmtools.clustering.old.rtree.CustomRTree;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class DBSCANRTree extends DBSCANBase {

    public static final String NAME = "DBSCAN-RTree";

    @Override
    public String getName() {
        return DBSCANRTree.NAME;
    }
    
    @Override
    protected void createIndex() {
        this.isp = new CustomRTree();
    }
    
    public void addLines() {
        if (observer != null) {
            observer.handleNotify((Object) null);
        }
    }
}
