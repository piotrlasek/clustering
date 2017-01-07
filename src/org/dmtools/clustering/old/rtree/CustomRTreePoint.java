package org.dmtools.clustering.old.rtree;

import java.util.ArrayList;

import org.dmtools.clustering.old.BasicSpatialObject;
import org.dmtools.clustering.old.Cell;
import org.dmtools.clustering.model.ISpatialObject;

import spatialindex.spatialindex.Point;

public class CustomRTreePoint extends Point implements ISpatialObject {

    boolean visited = false;

    BasicSpatialObject bso;

    /** Creates a new instance of MyPoint */
    public CustomRTreePoint(double[] pCoords, int c) {
        super(pCoords);
        bso = new BasicSpatialObject(pCoords);
    }

    public CustomRTreePoint(double[] pCoords) {
        super(pCoords);
        bso = new BasicSpatialObject(pCoords);
    }

    @Override
    public Long getApproximation() {
        return bso.getApproximation();
    }

    @Override
    public double[] getValues() {
        return m_pCoords;
    }

    @Override
    public ArrayList<ISpatialObject> getNeighbors(int k) {
        return bso.getNeighbors(k);
    }

    @Override
    public int[] getParentCellCoordinates() {
        return bso.getParentCellCoordinates();
    }

    @Override
    public void setApproximation(Long approximation) {
        bso.setApproximation(approximation); 
    }

    @Override
    public void setValues(double[] values) {
        bso.setValues(values);
    }

    @Override
    public void setParentCellCoordinates(int[] coordinates) {
        bso.setParentCellCoordinates(coordinates);        
    }

    @Override
    public void setClusterId(int clusterId) {
        bso.setClusterId(clusterId);
    }

    @Override
    public int getClusterId() {
        return bso.getClusterId();
    }

    @Override
    public Cell getParentCell() {
        return bso.getParentCell();
    }

    @Override
    public void setParentCell(Cell c) {
        bso.setParentCell(c);
        
    }
        
}
