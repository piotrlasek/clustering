/*
 * 
 * 
 * 
 * 
 */

package org.dmtools.clustering.old;

import org.dmtools.clustering.model.ISpatialIndex;
import org.dmtools.clustering.model.ISpatialObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 *
 * @author Piotr Lasek
 */
public abstract class BasicVAFile implements ISpatialIndex
{
    // -------------------------------------------------------------------------
    // ATTRIBUTES
    // -------------------------------------------------------------------------
    
    /**
     * Number of dimensions.
     */
    int dimensionsCount;
    
    /**
     * Number of bits per dimension.
     */
    public int bits[];
    
    /**
     * Min value in the i-th dimension.
     */
    public double[] minValues;
    
    /**
     * Max value in the i-th dimension.
     */
    public double[] maxValues;
    
    /**
     * Coordinates of cells.
     */
//    public int[][] p;
    
    /**
     * A list of non-empty cells approximations.
     */
    public ArrayList<Long> nonEmptyCells;
    
    /**
     * A list of objects.
     */
    ArrayList<ISpatialObject> objects;
    
    /**
     * A list of cells.
     */
    ArrayList<ArrayList> cells;
    
    /**
     * Cell manager
     */
    //CellManager cm;
    
    LvaHashTree lht;
    
    /**
     * 
     */
    public VectorDist[] dst;
    
    /**
     * 
     */
    double[] sizes;
    
    /**
     * 
     */
    ArrayList<ArrayList<Double>> l;
    
    /**
     * 
     */
    ArrayList<ArrayList<Double>> u;
    
    
    // -------------------------------------------------------------------------
    // METHODS
    // -------------------------------------------------------------------------
    
    /**
     *
     * @param aNumberOfDimensions
     */
    public BasicVAFile(int aNumberOfDimensions, int[] bitsPerDimensions)
    {
        dimensionsCount = aNumberOfDimensions;
        bits = bitsPerDimensions;
        
        minValues = new double[dimensionsCount];
        maxValues = new double[dimensionsCount];
        
        nonEmptyCells = new ArrayList<Long>();
        
        // create first level of cells
        cells = new ArrayList<ArrayList>(getNumberOfCellsPerDimension(0));
        
        //cm = new CellManager(bitsPerDimensions, aNumberOfDimensions);
        lht = new LvaHashTree();
        
//        // fill p table
//        p = new int[dimensionsCount][];
//        for (int i = 0; i < dimensionsCount; i++)
//        {
//            int nocpd = this.getNumberOfCellsPerDimension(i);
//            p[i] = new int[nocpd];
//            p[i][0] = minValues[i];
//            for(int j = 1; j < nocpd; j++)
//            {
//                p[i][j] = p[i][j-1] + (int) Math.pow(2, bits[i]);
//            }
//        }
        l = new ArrayList<ArrayList<Double>>(dimensionsCount);
        u = new ArrayList<ArrayList<Double>>(dimensionsCount);
        
        for (int i = 0; i < dimensionsCount; i++)
        {
            ArrayList<Double> ll = new ArrayList<Double>();
            ArrayList<Double> uu = new ArrayList<Double>();
            for(int j = 0; j < this.getNumberOfCellsPerDimension(i); j++)
            {
                ll.add(new Double(-1));
                uu.add(new Double(-1));
            }
            l.add(ll);
            u.add(uu);
         }
    }
    
    
    /**
     * 
     * @param objects
     */
    public void add(Collection<ISpatialObject> objectsList)
    {
        objects = (ArrayList<ISpatialObject>) objectsList;
        
        // update min/max
        for (ISpatialObject so:objects)
        {
            for (int i = 0; i < dimensionsCount; i++)
            {
                double x = so.getValues()[i];
                if (x < minValues[i]) minValues[i] = x;
                if (x >= maxValues[i]) maxValues[i] = x + 1;
            }
        }
        
        sizes = new double[dimensionsCount];
        for (int i = 0; i < dimensionsCount; i++)
        {
            sizes[i] = (maxValues[i] - minValues[i])/Math.pow(2, bits[i]);
        }
        
        for (ISpatialObject so:objects)
        {
            insert(so);
        }
        
        /*for (ISpatialObject so:objects)
        {
            int nod = dimensionsCount;
            for (int i = 0; i < nod; i++)
            {
                int x = so.getValues()[i];

                if (x > maxValues[i] || maxValues[i] == -1)
                {
                    maxValues[i] = x;
                    double noc = getNumberOfCellsPerDimension(i);
                    double max = maxValues[i];
                    double ceil = Math.ceil(max/noc);
                    max = (int) (noc*ceil);                    
                    maxValues[i] = (int) max;
                }
            }
            insert(so);
        }*/
    }
    
    /**
     * 
     * @param iso
     */
    abstract void insert(ISpatialObject iso);
    
    /**
     * 
     * 
     * @param al
     * @param dimension
     */
    private void fillDimension(ArrayList al, int dimension)
    {
        for(int i = 0; i < getNumberOfCellsPerDimension(dimension); i++)
        {
            al.add(new ArrayList());
        }
    }
    
    /**
     * 
     * 
     * @param al
     * @param dimension
     */
    private void fillDimensionWithCells(ArrayList<Cell> al, int dimension)
    {
        for(int i = 0; i < getNumberOfCellsPerDimension(dimension); i++)
        {
            al.add(new Cell());
        }
    }
    
    /**
     * 
     * @param diff
     * @param c1
     * @param c2
     * @return
     */
    private boolean coordinatesDifferBy(int diff, int[] c1, int[] c2)
    {
        int numberOfMax = 0;
        for (int i = 0; i < c1.length; i++)
        {
            double tmpDiff = Math.abs(c1[i] - c2[i]);
            
            // at least one coordinate equals +/- diff
            if (tmpDiff == diff)
                numberOfMax++;
            //else
            if (tmpDiff > diff)
            {
                return false;
            }
        }
        
        if (numberOfMax >= 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 
     * @param cells
     * @return
     */
    public Integer getNumberOfObjects(ArrayList<Cell> cells)
    {
        Integer count = 0;
        for (Cell c:cells)
        {
            count += c.getObjectsCount();
        }
        return count;
    }
    
    /**
     * 
     * @param aDimension
     * @return
     */
    protected int getNumberOfCellsPerDimension(int dimension)
    {
        int n = (int)(long) Math.pow(2, bits[dimension]);
        return n;
    }
    
    /**
     * 
     * 
     * 
     * @param aCoordinates
     */
    protected Cell getCell(int[] coordinates)
    {
        /*ArrayList al = (ArrayList<ArrayList>) cells;

        if (al.size() != this.getNumberOfCellsPerDimension(0))
        {
            fillDimension(al, 0);
        }

        for (int i = 0; i < coordinates.length - 2; i++)
        {
           try
           {
               al = (ArrayList) al.get(coordinates[i]);
           }
           catch (Exception e)
           {
               e.printStackTrace();
               System.exit( 0 );
           }
           
           if (al.size() != this.getNumberOfCellsPerDimension(i+1))
           {
               fillDimension(al, i+1);
           }
        }

        ArrayList<Cell> alc =
                (ArrayList<Cell>) al.get(coordinates[coordinates.length-2]); 

        if (alc.size() != this.getNumberOfCellsPerDimension(
                coordinates.length-1))
        {
            fillDimensionWithCells(alc, coordinates.length - 1);
        }

        Cell c = (Cell) alc.get(coordinates[coordinates.length - 1]);

        return c;*/
    	
    	/*Cell c = cm.getCell(coordinates);
    	if (c == null)
    	{
    		c = new Cell();
    		cm.setCell(c);
    	}*/
    	
    	Cell c = lht.getCell(coordinates);
    	if (c == null) {
    	   c = lht.newCell(coordinates);
    	}
    	return c;
    }

    /**
     * 
     * @param coordinates
     * @return
     */
    protected Cell getCell2(int[] coordinates)
    {
        /*ArrayList al = (ArrayList<ArrayList>) cells;
        Cell c = null;
        
        if (coordinates[coordinates.length-2] < al.size())
        {
        ArrayList<Cell> alc =
                (ArrayList<Cell>) al.get(coordinates[coordinates.length-2]);
        
        if (coordinates[coordinates.length-1] < alc.size())
        	{
        	c = (Cell) alc.get(coordinates[coordinates.length - 1]);
        	}
        }

        return c;*/
    	/*Cell c = cm.getCell(coordinates);*/
    	Cell c = lht.getCell(coordinates);
    	return c;
    }

    /**
     *
     * @param vector
     * @return
     */
    protected Long approximate(double[] vector)
    {
        long approx = 0;

        for (int i = 0; i < dimensionsCount; i++)
        {
            int n = bits[ i ];

            approx = approx << n;

            double length = maxValues[i] - minValues[i];
            double count = Math.pow(2, n);
            double size = length / count;
            double coord = vector[i] - minValues[i];

            long app = (long) Math.floor(coord / size);

            approx |= app;
        }
        return approx;
    }
    
    /**
     * 
     * @param aApproximation
     * @return
     */
    protected int[] approximationToCoordinates(Long aApproximation)
    {
       int[] coordinates = new int[dimensionsCount];
       long offset = 0;
       
       for (int i = dimensionsCount - 1; i >= 0; i--)
       {
           long tmp = 0;
           for(int j = 0; j < bits[i]; j++)
           {
               tmp = tmp << 1;
               tmp |= 1;
           }
           tmp = tmp << offset;
           tmp = tmp & aApproximation;
           tmp = tmp >> offset;
           offset += bits[i];
           coordinates[i] = (int)tmp;
       }
      return coordinates;
    }
    
    /**
     * 
     */
    protected ArrayList<Cell> scan(Long cellApproximation, int layer)
    {
        ArrayList<Cell> neighborhood = new ArrayList<Cell>();
        int[] cell = approximationToCoordinates(cellApproximation);
        for (Long nonEmptyCellApproximation:nonEmptyCells)
        {
            int[] tmp =
                    approximationToCoordinates(nonEmptyCellApproximation);

            if (coordinatesDifferBy(layer, cell, tmp))
            {
                Cell c = getCell(
                     this.approximationToCoordinates(
                        nonEmptyCellApproximation)
                        //tmp
                    );
                neighborhood.add(c);
            }
        }
        return neighborhood;
    }
    
    protected ArrayList<Cell> scanFast(Long cellApproximation, int layer)
    {
        ArrayList<Cell> neighborhood = new ArrayList<Cell>();
        int[] cell = approximationToCoordinates(cellApproximation);
        for (Long nonEmptyCellApproximation:nonEmptyCells)
        {
            int[] tmp =
                    approximationToCoordinates(nonEmptyCellApproximation);
     
            for (int i = 1; i <= layer; i++)
            {
                coordinatesDifferBy(i, cell, tmp);
                Cell c = getCell(
                        this.approximationToCoordinates(
                           nonEmptyCellApproximation)
                           //tmp
                       );
                   neighborhood.add(c);
            }
        }
        return neighborhood;
    }
    
    /**
     * 
     * @return
     */
    public Collection<ISpatialObject> getSpatialObjects()
    {
        return objects;
    }
    
    /**
     * 
     * @param a
     * @param b
     * @return
     */
    public double getDist(ISpatialObject a, ISpatialObject b)
    {
        double dist = 0;
        double[] ac = a.getValues();
        double[] bc = b.getValues();
        for(int i = 0; i < dimensionsCount; i++)
        {
            dist += Math.pow(ac[i] - bc[i], 2);
        }
        return Math.sqrt(dist);
    }
    
    /**
     * 
     */
    public void printDist()
    {
        for (int i = 0; i < dst.length; i++)
        {
            System.out.print(dst[i]);
        }
        System.out.println();
    }
    
    /**
     * 
     * @param k
     * @param max
     */
    public void initCandidate(int k, double max)
    {
        dst = new VectorDist[k];
        
        for(int i = 0; i < dst.length; i++)
        {
            dst[i] = new VectorDist();
            dst[i].dist = max;
        }
    }
    
    /**
     * 
     * @param k
     * @param p
     * @param dist
     * @return
     */
    public double candidate(int k, ISpatialObject p, double dist)
    {
        if (dist < dst[k-1].dist)
        {
            dst[k-1].dist = dist;
            dst[k-1].vector = p;
            Arrays.sort(dst);
        }
        return dst[k-1].dist;
    }

    /**
     * 
     * @param pObject
     * @param qObject
     * @return
     */
    public double lowerBound(ISpatialObject pObject, ISpatialObject qObject)
    {
        double lBnd = 0;
        double[] q = qObject.getValues();
        int approx[] = pObject.getParentCellCoordinates();
        //approximationToCoordinates(apx);

        for (int i = 0; i < dimensionsCount; i++)
        {           
            double lx = (Double) l.get(i).get(approx[i]);
            double ux = (Double) u.get(i).get(approx[i]);

            if (q[i] < lx)
                lBnd += Math.pow((lx - q[i]), 2);
            else if (q[i] > ux)
                lBnd += Math.pow((q[i] - ux), 2);
            else if (q[i] <= ux || q[i] >= lx)
                lBnd += 0;
        }
        return Math.sqrt( lBnd );
    }


    /**
     * 
     * @return
     */
    public double[] getCellSizes()
    {
    	return sizes;
    }
    
    public Collection<ISpatialObject> getNeighbors(ISpatialObject q,
            double max) {
        return getNeighbors(q, objects.size(), max);
    }
    
    
    public Collection<ISpatialObject> getNeighbors(ISpatialObject q,
            int count) {
        double max = 0;
        
        for (double mv:maxValues) {
            if (mv > max) max = mv;
        }
        
        return getNeighbors(q, count, max);
    }
}
