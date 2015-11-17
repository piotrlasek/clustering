package org.dmtools.clustering.old;

import java.util.ArrayList;

public interface ISpatialObject {

    /**
     * 
     * @param coordinates
     */
    public void setCoordinates(double[] coordinates);
    
    /**
     * 
     * @return
     */
    public double[] getCoordinates();
    
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
     * @param value
     */
    public void setValue(int value);
    
    /**
     * 
     * @return
     */
    public int getValue();
    
    /**
     * 
     * @param k
     * @return
     */
    public ArrayList<ISpatialObject> getNeighbors(int k);
    
}
