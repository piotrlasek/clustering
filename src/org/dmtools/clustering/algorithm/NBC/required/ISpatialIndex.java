package org.dmtools.clustering.algorithm.NBC.required;

import java.util.Collection;

/**
 *
 * @author Piotr Lasek, plasek@ii.pw.edu.pl
 */
public interface ISpatialIndex
{
    
    /**
     * 
     * @param object
     */
//    public void add(ISpatialObject object);
    
    /**
     * 
     * @param object
     */
//    public void remove(ISpatialObject object);
    
    /**
     * 
     */
    public String getName();
    
    /**
     * 
     * @param objects
     */
    public void add(Collection<ISpatialObject> objectsList);
    
    /**
     * 
     * @param object
     * @param count
     * @return
     */
    public Collection<ISpatialObject> getNeighbors(ISpatialObject object,
            int count, double max);
    
    /**
     * 
     * @param object
     * @param count
     * @return
     */
    public Collection<ISpatialObject> getNeighbors(ISpatialObject object,
            double max);
    
    /**
     * 
     * @param object
     * @param count
     * @return
     */
    public Collection<ISpatialObject> getNeighbors(ISpatialObject object,
            int count);
    
}
