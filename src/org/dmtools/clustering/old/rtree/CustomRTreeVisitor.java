package org.dmtools.clustering.old.rtree;

import java.util.ArrayList;

import org.dmtools.clustering.old.ISpatialObject;

import spatialindex.spatialindex.IData;
import spatialindex.spatialindex.INode;
import spatialindex.spatialindex.IVisitor;

public class CustomRTreeVisitor implements IVisitor {
    public int m_indexIO = 0;
    public int m_leafIO = 0;

    public ArrayList<ISpatialObject> allPoints;
    public ArrayList<ISpatialObject> neighbours;
    public ArrayList<IData> n;    

    public CustomRTreeVisitor(ArrayList<ISpatialObject> SetOfPoints) {
        neighbours = new ArrayList<ISpatialObject>();
        n = new ArrayList<IData>();
        allPoints = SetOfPoints;
    }

    public void visitData(final IData d) {
        int id = d.getIdentifier();
        ISpatialObject iso1 = allPoints.get(id);
        neighbours.add(iso1);
        n.add(d);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<ISpatialObject> getNeighbors() {
        //return (ArrayList<ISpatialObject>) neighbours.clone();
        neighbours.remove(0);
        n.remove(0);
        //return new ArrayList<ISpatialObject>(neighbours);
        return neighbours;
    }
    
    public void clear() {
        neighbours.clear();
        n.clear();
    }
    
    public void visitNode(final INode n) {
        if (n.isLeaf())
            m_leafIO++;
        else
            m_indexIO++;
    }
}
