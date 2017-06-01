package org.dmtools.clustering.algorithm.common;

import spatialindex.spatialindex.IData;
import spatialindex.spatialindex.INode;
import spatialindex.spatialindex.IVisitor;
import spatialindex.spatialindex.Point;

import java.util.ArrayList;

/**
 * Created by Piotr Lasek on 01.06.2017.
 */
public class MyVisitor implements IVisitor {

    public static ArrayList<Point> SetOfPoints;

    public int m_indexIO = 0;
    public int m_leafIO = 0;
    public int kNB = 0;

    public ArrayList<Point> neighbours = new ArrayList();

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
