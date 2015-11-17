package org.dmtools.clustering.CNBC;

import java.awt.Graphics;
import java.awt.Dialog.ModalityType;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;


import org.dmtools.clustering.old.*;

/**
 * 
 * @author Piotr Lasek
 * 
 */
public abstract class CNBCBase implements IClusteringAlgorithm {

    ClusteringLogger logger = new ClusteringLogger(getName());
    
    //String desc = getName();

    Graphics graphics = null;
    
    String description;

    IClusteringParameters parameters;
    IClusteringData data;

    // Algorithm attributes
    int nDim = 0; // number of dimension
    int k = 0; // number of k nearest neighbors
    int l = 0; // size of a cell
    int b = 0; // number of bits per dimension

    IClusteringObserver observer;

    ArrayList<ISpatialObject> dataset;

    static double max = 600;

    ISpatialIndex isp;

    // Abstract methods

    public abstract ArrayList<ISpatialObject> getLayer(ISpatialObject p,
            int layer_no);

    public abstract ArrayList<ISpatialObject> getNeighbors(ISpatialObject q,
            int k);

    protected abstract void createIndex();

    public void run() {
        logger.addDescription(this.getDescription());
        
        run1();
        
        observer.handleNotify(logger.getLog());
        // run2();
    }

    private void run1() {

        logger.clusteringStart();

        // CALC NDF
        CalcNDF();

        int cluster_count = 0;
        ArrayList<NbcSpatialObject> DPSet = new ArrayList<NbcSpatialObject>();

        // for each object p in Dataset
        ArrayList<NbcSpatialObject> NoiseSet = new ArrayList<NbcSpatialObject>();
        //long XY = 0;
        //int xy = 0;

        for (int i = 0; i < dataset.size(); i++) {
            NbcSpatialObject p = (NbcSpatialObject) dataset.get(i);
            if (p.clst_no != -1 || p.ndf <= 1)
                continue;
            p.clst_no = cluster_count;
            DPSet.clear();

            // for each object q in kNB(p)
            //long timeX = System.currentTimeMillis();
            ArrayList<ISpatialObject> kNBp = getNeighbors(p, k);
            //long timeY = System.currentTimeMillis();
            //XY += (timeY - timeX);

            for (int j = 0; j < kNBp.size(); j++) {
                NbcSpatialObject q = (NbcSpatialObject) kNBp.get(j);
                q.clst_no = cluster_count;
                if (q.ndf > 1)
                    DPSet.add(q);
            }

            while (!DPSet.isEmpty()) {
                NbcSpatialObject pdp = DPSet.remove(0);

                // for each object q in kNB(p)
                ArrayList<ISpatialObject> kNBpDP = getNeighbors(pdp, k);

                for (int k = 0; k < kNBpDP.size(); k++) {
                    NbcSpatialObject q = (NbcSpatialObject) kNBpDP.get(k);
                    if (q.clst_no != -1)
                        continue;
                    q.clst_no = cluster_count;
                    if (q.ndf > 1)
                        DPSet.add(q);
                }
                // DPSet.remove(p)
            }
            cluster_count++;
        }
        
        logger.clusteringEnd();   

        try {
            addLines(); // hack
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void run2() {
        // CalcNDF();

        try {
            java.io.File f = new java.io.File("c:\\aaa.txt");
            f.delete();
            f.createNewFile();

            FileOutputStream fos;
            fos = new FileOutputStream(f);

            for (int i = 0; i < dataset.size(); i++) {
                NbcSpatialObject so = (NbcSpatialObject) dataset.get(i);
                // System.out.println(so.excell());
                fos.write(so.excell().getBytes());
                fos.write("\n".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 
     * @return
     */
    public int getLayersCount(ISpatialObject p) {
        //return Cell.maxLayersCount;
    	return 0;
    }
    
    /**
	 * 
	 */
    public void CalcNDF() {
        ArrayList<ISpatialObject> candidateSet = new ArrayList<ISpatialObject>();
        int layer_no;

        // for each object p in Dataset // evaluate kNB(p)

        for (int i = 0; i < dataset.size(); i++) {
            NbcSpatialObject p = (NbcSpatialObject) dataset.get(i);

            candidateSet.clear();
            layer_no = 0;
            ArrayList<ISpatialObject> knb = new ArrayList<ISpatialObject>();
            knb = (ArrayList<ISpatialObject>) getNeighbors(p, k);

            // knb = (ArrayList<ISpatialObject>)lva.getNeighbors(p, k, NBCLVA.max);
            // knb = getLayer(p, layer_no);
            ArrayList<ISpatialObject> newInKnb = null;
            int layersCount = getLayersCount(p); 
            // while ( candidateSet.size() < k && layer_no < Cell.maxLayersCount)
            while (candidateSet.size() < k && layer_no < layersCount) {
                // if (layer_no > 0 ) { System.out.println("layer_no: " + layer_no); }
                // ArrayList<ISpatialObject> layer = null;
                ArrayList<ISpatialObject> layer = getLayer(p, layer_no);
                /*
                 * try { layer = lva.getLayer( p, layer_no ); } catch( Exception e ) { e.printStackTrace(); }
                 */
                // layer=knb;
                candidateSet.addAll(layer);
                // knb.addAll( layer );
                newInKnb = layer;
                int x = -1;
                int y = 0;
                for (ISpatialObject tmp : candidateSet) {
                    NbcSpatialObject nso = (NbcSpatialObject) tmp;
                    nso.cand = true;
                    if (nso.equals(p))
                        x = y;
                    y++;
                }

                if (layer_no == 0 && x != -1) {
                    candidateSet.remove(x);
                }

                while (!candidateSet.isEmpty()) {
                    // TODO
                    candidateSet.clear();

                    for (ISpatialObject q : newInKnb) {
                        if (knb.contains(q)) {
                            p.SizeOfkNB++;
                        }
                        NbcSpatialObject nsoq = (NbcSpatialObject) q;
                        ArrayList<ISpatialObject> layer_1 = getLayer(nsoq, 1);
                        for (ISpatialObject sol1 : layer_1) {
                            NbcSpatialObject nso1 = (NbcSpatialObject) sol1;
                            /*
                             * if (!candidateSet.contains(nso1)) {
                             * candidateSet.add(nso1); }
                             */
                            if (nso1.cand == false) {
                                nso1.cand = true;
                                candidateSet.add(nso1);
                            }
                        }
                    }
                }
                layer_no++;
            }
            // for each object q in kNB(p)
            // q.SizeOfRkNB++

            for (ISpatialObject q : knb) {
                NbcSpatialObject nsoq = (NbcSpatialObject) q;
                nsoq.SizeOfRkNB++;
            }
        }

        for (int i = 0; i < dataset.size(); i++) {
            NbcSpatialObject p = (NbcSpatialObject) dataset.get(i);
            if ((p.SizeOfkNB != 0) && (p.SizeOfkNB >= k - 1))
                // if ( (p.SizeOfkNB != 0) )
                p.ndf = (double) p.SizeOfRkNB / (double) p.SizeOfkNB;
            else
                p.ndf = 0;
        }
    }

    /**
	 * 
	 */
    public void setGraphics(Graphics g) {
        this.graphics = g;
    }

    /**
	 * 
	 */
    public void addLines() {
        /*
         * double[] cs = lva.getCellSizes(); int maxX = 600; int maxY = 600;
         * ArrayList<int[]> lines = new ArrayList<int[]>(); for (int i = 0; i <
         * maxX / cs[0]; i++) lines.add( new int[]{(int) (i*cs[0]), 0, (int)
         * (i*cs[0]), maxY} ); for (int i = 0; i < maxY / cs[1]; i++) lines.add(
         * new int[]{0, (int) (i*cs[1]), maxX, (int) (i*cs[1])});
         */
        this.observer.handleNotify(getCellSizes());
    }

    @Override
    public IClusteringData getResult() {
        BasicClusteringData bcd = new BasicClusteringData();
        ArrayList<IClusteringObject> al = new ArrayList<IClusteringObject>();
        for (Object o : dataset) {
            NbcSpatialObject mp = (NbcSpatialObject) o;
            // observer.handleNotify(mp.excell());
            BasicClusteringObject bco = new BasicClusteringObject();
            BasicSpatialObject rso = new BasicSpatialObject(mp.getCoordinates());
            bco.setSpatialObject(rso);
            BasicClusterInfo bci = new BasicClusterInfo();
            bci.setClusterId(mp.clst_no);
            bco.setClusterInfo(bci);
            al.add(bco);
        }
        bcd.set(al);
        return bcd;
    }

    @Override
    public void setData(IClusteringData data) {
        addData(data);
    }

    @Override
    public IClusteringParameters getParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setParameters(IClusteringParameters parameters) {
        logger.setParameters(parameters.toString());
        this.parameters = parameters;
        k = new Integer(parameters.getValue("k")).intValue();
        try {
            b = new Integer(parameters.getValue("b")).intValue();
        } catch (Exception e) {
            b = -1;
        }
    }

    @Override
    public void setObserver(IClusteringObserver observer) {
        this.observer = observer;
    }

    /**
     * 
     * @param data
     */
    protected void addData(IClusteringData data) {
        nDim = data.get().iterator().next().getSpatialObject().getCoordinates().length;

        this.data = (BasicClusteringData) data;

        Collection<IClusteringObject> input = data.get();
        dataset = new ArrayList<ISpatialObject>();

        for (IClusteringObject co : input) {
            dataset.add(new NbcSpatialObject(co.getSpatialObject()));
        }
        
        logger.indexStart();
        createIndex();
        isp.add(dataset);
        logger.indexEnd();
    }

    /**
     * 
     * @return
     */
    public double[] getCellSizes() {
        return ((BasicVAFile) isp).getCellSizes();
    }
    

    /**
     * 
     */
    public void addDescription(String description) {
        this.description = description;
    }
    
    /**
     * 
     */
    public String getDescription() {
        return description;
    }
    
}
