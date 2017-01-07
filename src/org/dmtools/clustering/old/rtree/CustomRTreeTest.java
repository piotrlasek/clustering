package org.dmtools.clustering.old.rtree;

import java.util.ArrayList;

import org.dmtools.clustering.model.ISpatialIndex;
import org.dmtools.clustering.model.ISpatialObject;

import junit.framework.TestCase;

public class CustomRTreeTest extends TestCase {

    ISpatialIndex isi1;
    ISpatialIndex isi2;
    
    /**
     * 
     */
    public void test1() {
        System.out.println("--- Test 1 count ---");
        CustomRTreePoint bso = new CustomRTreePoint(new double[] { 0, 0 });

        int count = 2;
        ArrayList<ISpatialObject> neighbors1 =
            (ArrayList<ISpatialObject>) isi1.getNeighbors(bso, count);        
        
        ArrayList<ISpatialObject> neighbors2 =
            (ArrayList<ISpatialObject>) isi2.getNeighbors(bso, count);
                
        printResult(bso, neighbors1, neighbors2);
        assertEquals(neighbors1.size(), neighbors2.size());
    }
    
    public void test2() {
        System.out.println("--- Test 2 eps   ---");
        CustomRTreePoint bso = new CustomRTreePoint(new double[] { 0, 0 });

        double eps = 10; 
        ArrayList<ISpatialObject> neighbors1 =
            (ArrayList<ISpatialObject>) isi1.getNeighbors(bso, eps);        
        ArrayList<ISpatialObject> neighbors2 =
            (ArrayList<ISpatialObject>) isi2.getNeighbors(bso, eps);
        
        printResult(bso, neighbors1, neighbors2);
        assertEquals(neighbors1.size(), neighbors2.size());
        
    }
    
    // -------------------------------------------------------------------------
    

    private void printResult(CustomRTreePoint bso,
            ArrayList<ISpatialObject> neighbors1, ArrayList<ISpatialObject> neighbors2) {
        System.out.println("q = " + s(bso));
        System.out.println("---");
        for(int i = 0; i < neighbors1.size(); i++)
        {
            ISpatialObject o1 = neighbors1.get(i);
            ISpatialObject o2 = neighbors2.get(i);
            System.out.println("n = " + s(o1) + " .. " + s(o2) );
        }
    }
    
    protected void setUp() throws Exception {
        ArrayList<ISpatialObject> ds = new ArrayList<ISpatialObject>();

        CustomRTreePoint bso = new CustomRTreePoint(new double[] { 0, 0 });
        ds.add(bso);
        bso = new CustomRTreePoint(new double[] { 1, 1 });
        ds.add(bso);
        bso = new CustomRTreePoint(new double[] { 1, 2 });
        ds.add(bso);
        bso = new CustomRTreePoint(new double[] { 1, 3 });
        ds.add(bso);
        bso = new CustomRTreePoint(new double[] { 3, 1 });
        ds.add(bso);
        bso = new CustomRTreePoint(new double[] { 2, 2 });
        ds.add(bso);
        bso = new CustomRTreePoint(new double[] { 0, 0.2 });
        ds.add(bso);
        bso = new CustomRTreePoint(new double[] { 0.2, 0 });
        ds.add(bso);

        // RTree
        isi1 = new CustomRTree();
                
        // LVA-Index
        int nDim = 2;
        int b = 2;
        int n = 5;
        int[] bits = new int[nDim];
        for (int i = 0; i < nDim; i++)
            bits[i] = b;
        /// isi2 = new LVAIndex(nDim, bits, n);
        
        isi1.add(ds);
        //// isi2.add(ds);        
    }
    
    protected void tearDown() throws Exception {
        
    }
    
    public String s(ISpatialObject so)
    {
        double[] d = so.getValues();
        String s="";
        for(int i =0;i < d.length; i++) {
            if (i>0) s += "," + d[i];
            else s+= d[i];
        }
        return s;
    }
}
