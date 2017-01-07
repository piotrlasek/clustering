/**
 * File         : Cell. java
 * Author       : Piotr Lasek
 * Creation date: before 17.01.2008
 */

package org.dmtools.clustering.old;

import org.dmtools.clustering.model.ISpatialObject;

import java.util.ArrayList;


/**
 * This class represents the Cell.
 * @author pls
 */
public class Cell
{
    /**
     * 
     */
    private Long approximation;
    
    /**
     * 
     */
    private ArrayList<ISpatialObject> objects;

    /**
     * This is the list of the layers. Each layer has a list of the cells which
     * belong to this cell.
     */
    private ArrayList<ArrayList<Cell>> neighbourCells;
    
    /**
     * The maximum number of layers that are stored n the neighbourCells list.
     */
    public static int maxLayersCount = -1 ;
    
    /**
     * 
     */
    public Cell()
    {
        objects = new ArrayList< ISpatialObject >();

        neighbourCells = new ArrayList<ArrayList<Cell>>();

        // TODO for (int i = 0; i < Cell.maxLayersCount + 1; i++)
        for (int i = 0; i < Cell.maxLayersCount; i++) {
            neighbourCells.add(new ArrayList< Cell >());
        }
    }
    
    /**
     * 
     * @param aObject
     */
    public void addObject(ISpatialObject aObject)
    {
        objects.add( aObject );
    }
    
    /**
     * 
     * @return
     */
    public int getCount()
    {
        return objects.size();
    }
    
    /**
     * 
     * @return
     */
    public int getObjectsCount()
    {
        return objects.size();
    }
    
    /**
     * 
     * @param approximation
     */
    public void setApproximation(Long approximation)
    {
        this.approximation = approximation;
    }
    
    /**
     * 
     * @return
     */
    public Long getApproximation()
    {
        return approximation;
    }
    
    /**
     * 
     * @param layerNumber
     * @return
     */
    public ArrayList<Cell> getNeighborCells(int layerNumber)
    {
        if (layerNumber >= neighbourCells.size()) {
            while(neighbourCells.size() < layerNumber+1) {
                neighbourCells.add(new ArrayList<Cell>());
            }
        }
        return neighbourCells.get(layerNumber);
    }
    
    /**
     * 
     * @return
     */
    public int getNeighborLayersCount()
    {
        return neighbourCells.size();
    }
    
    public void addLayer(int diff)
    {
        //neighbourCells.add(layerIndex, new ArrayList<Cell>());
        for (int i = 0; i < diff; i++)
            neighbourCells.add(new ArrayList<Cell>());
    }
    
    /**
     * 
     * @return
     */
    public ArrayList<ISpatialObject> getObjects()
    {
        return objects;
    }

}
