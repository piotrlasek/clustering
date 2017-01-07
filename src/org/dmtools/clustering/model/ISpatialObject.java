package org.dmtools.clustering.model;

import org.dmtools.clustering.old.Cell;

import java.util.ArrayList;

public interface ISpatialObject {

    /**
     * 
     * @param values
     */
    public void setValues(double[] values);
    
    /**
     * 
     * @return
     */
    public double[] getValues();
    
    /**
     * 
     * @param coordinates
     */
    public void setParentCellCoordinates(int[] coordinates);
    
    /**
     * 
     * @return
     */
    public int[] getParentCellCoordinates();
    
    /**
     * 
     * @return
     */
    public Cell getParentCell();
    
    /**
     * 
     */    
    public void setParentCell(Cell c);

    /**
     * 
     * @param approximation
     */
    public void setApproximation(Long approximation);
    
    /**
     * 
     * @return
     */
    Long getApproximation();

    /**
     * 
     * @param clusterId
     */
    public void setClusterId(int clusterId);
    
    /**
     * 
     * @return
     */
    public int getClusterId();
    
    /**
     * 
     * @param k
     * @return
     */
    public ArrayList<ISpatialObject> getNeighbors(int k);
    
}
