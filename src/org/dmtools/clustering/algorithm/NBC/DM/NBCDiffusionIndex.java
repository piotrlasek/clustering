package org.dmtools.clustering.algorithm.NBC.DM;

import Jama.Matrix;
import org.dmtools.clustering.model.*;
import org.dmtools.clustering.old.*;
import spatialindex.spatialindex.ISpatialIndex;

import java.awt.*;
import java.util.ArrayList;
import java.util.ListIterator;


/**
 * 
 * @author Piotr Lasek
 */
public class NBCDiffusionIndex implements IClusteringAlgorithm {
    
    public static final String NAME = "NBC-RTree                     ";
    
    String description;

    IClusteringObserver observer;
    ISpatialIndex tree;
    IClusteringParameters parameters;

    ArrayList<DiffusionObject> Dataset;
    
    ClusteringTimer logger = new ClusteringTimer();

    int nDim = 0;
    //int id = 0;
    int k;
    //String desc = "NBC (RTree): ";
    
    Matrix Dimensions;
    ArrayList<double[]> Data;
    //InstanceConstraints Constraints;
    

    /** Creates a new instance of NBC */
    public void run() {
        logger.addDescription(this.getDescription());
        //long begin_time1 = System.currentTimeMillis();
        logger.clusteringStart();

        ArrayList<DiffusionObject> NoiseSet = new ArrayList();
        int cluster_count = 0;

        // 		
        //long begin_time2 = System.currentTimeMillis();
        CalcNDF();
        //long end_time2 = System.currentTimeMillis();
        long time3 = System.currentTimeMillis();
        //desc += " CaldNDF = " + (end_time2 - begin_time2) + " ms";
        //long time4 = System.currentTimeMillis();
        NoiseSet.clear();
        cluster_count = 0;
        ArrayList<DiffusionObject> DPSet = new ArrayList();

        //long XY = 0;
        //int xy = 0;

        // for each object p in Dataset
        ListIterator li = Dataset.listIterator();
        while (li.hasNext()) {
        	DiffusionObject p = (DiffusionObject) li.next();
            // if (p.clst_no != NULL or p.ndf < 1)) continue;
            if (p.clst_no != -1 || p.ndf < 1)
            {
            	//System.out.println("p.id= " + p.id + "; p.ndf= " + p.ndf);
                continue;
            }
            p.clst_no = cluster_count; // label a new cluster
            DPSet.clear(); // initialize DPSet

            ArrayList<DiffusionObject> kNN = p.getNeighbors2(k);

            for (int i = 0; i < kNN.size(); i++) {
                // q.clst_no = cluster_count
            	DiffusionObject q = (DiffusionObject) kNN.get(i);
                q.clst_no = cluster_count;
                if (q.ndf >= 1)
                {
                	//System.out.println("q.id= " + q.id + "; q.ndf= " + q.ndf);
                    DPSet.add(q);
                }
            }

            // while (DPSet is not empty) // expanding the cluster
            
            while (!DPSet.isEmpty()) {
                // p = DPset.getFirstObject();
            	DiffusionObject pD = (DiffusionObject) DPSet.remove(0);

                // for each object q in kNB(pD)
            	ArrayList<DiffusionObject> kNNpD = pD.getNeighbors2(k);
                //tree.nearestNeighborQuery(k, pD, kNNpD);                
            	ListIterator liKNNpD = kNNpD.listIterator();
                while (liKNNpD.hasNext()) {
                	DiffusionObject q = (DiffusionObject) liKNNpD.next();
                    // if (q.clst_no!NULL) continue
                	//System.out.println("pD.id=" +  pD.id + "; q.id= " + q.id + "; q.ndf= " + q.ndf);
                    if (q.clst_no != -1)
                        continue;

                    q.clst_no = cluster_count;

                    // if (q.ndf >= 1) DPSet.add(q)
                    if (q.ndf >= 1)
                        DPSet.add(q);
                }
                // DPSet.remove(p);
            }
            cluster_count++;
        }

        System.out.println("cluster_count = " + cluster_count);
        
        // for each object p in Dataset // label noise
        li = Dataset.listIterator();
        while (li.hasNext()) {
            // if (p.clst_no=NULL) NoiseSet.add(p)
        	DiffusionObject p = (DiffusionObject) li.next();
            if (p.clst_no == -1)
                p.clst_no = -2;
        }

        //long end_time1 = System.currentTimeMillis();
        // ----------------------
        // now = new Date();
        // observer.handleNotify("Clusters created.");
        // observer.handleNotify("NBC finished - . K = " + k +
        // ", number of clusters: "
        // + (cluster_count - 1));
        // double after = now.getTime();
        // observer.handleNotify("[size: " + Dataset.size() + ", time: " + (int)
        // (after - before) + " ms, k:"
        // + k + ", clusters:" + (cluster_count-1) + "]");
        logger.clusteringEnd();
        
        System.out.println(logger.getLog());
        
        //desc += ", Total = " + (end_time1 - begin_time1 - (time4 - time3))
        //        + " ms "  + parameters.toString();
        observer.handleNotify(logger.getLog());
        addLines();

        //System.out.println("==>>> xy = " + xy + ", " + "XY = " + XY);
    }

    private void CalcNDF() {
    	ArrayList<DiffusionObject> kNN = new ArrayList<DiffusionObject>();
    	int[] list = new int[Dataset.size()];
    	for (int i:list)
    		i = 0;
        
        // for each object p in ti.dataset
    	int i = 0;
        for (DiffusionObject p:Dataset) {
            kNN.clear();

            kNN = p.getNeighbors(k);
            p.setSizeOfkNB(kNN.size());

            // for each new object q in kNB(p)
            int j= 0;
            for(DiffusionObject q : kNN) {            	
                q.increaseSizeOfRkNB(); 
                
                
                list[q.id_number]++;
                //System.out.println("q.id= " +  q.id +  "; list[" + q.id_number + "]= " + list[q.id_number]);
                j++;
            }
            //System.out.println("[" + i + "|" + p.getSizeOfRkNB() + "]");
            i++;
        }

        // for each object q in kNB(p)
        for (DiffusionObject p:Dataset) {
        	
        	if (p.getSizeOfRkNB() == 0 || p.getSizeOfkNB() == 0) {
        		p.ndf = 0;
        	} else {
        		p.ndf = (double) p.getSizeOfRkNB() / (double) p.getSizeOfkNB();
        		
        	}
        	/*
        	int RkNB = list[p.id_number];
        	
        	if( RkNB == 0 || p.getSizeOfkNB() == 0) {
        		p.ndf = 0;
        	} else {
        		p.ndf = (double) RkNB / (double) p.getSizeOfkNB();
        	}*//*
        	System.out.println("p.id= " +  p.id + "; p.SizeOfRkNB= " + p.getSizeOfRkNB() + 
        			"|" + p.getSizeOfRkNB() + 
        			"; p.SizeOfkNB= " + p.getSizeOfkNB() + 
        			"; p.ndf= " + p.ndf);/**/
        }
    }

    
    
    @Override
    public IClusteringData getResult() {
        BasicClusteringData bcd = new BasicClusteringData();
        ArrayList<IClusteringObject> al = new ArrayList<IClusteringObject>();
        for (Object o : Dataset) {
        	DiffusionObject mp = (DiffusionObject) o;
            BasicClusteringObject bco = new BasicClusteringObject();
            BasicSpatialObject rso = new BasicSpatialObject(mp.m_pCoords/*
                                                                         * ,mp.
                                                                         * clst_no
                                                                         */);
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
        logger.indexStart();
        ArrayList<IClusteringObject> tmp = (ArrayList<IClusteringObject>) data
                .get();
        Dataset = new ArrayList();
        nDim = data.get().iterator().next().getSpatialObject().getValues().length;
        
        
        int id = 0;
        
        
        for (int i = 0; i < Dimensions.getRowDimension(); i++)
        {
        	DiffusionObject obj = new DiffusionObject();
        	obj.setId(String.valueOf(i));
        	obj.setId_Number(i);
        	obj.setCoords(Data.get(i));
        	Dataset.add(obj);
        }
        
        System.out.println("Dataset size = " + Dataset.size());
        
        for (int i = 0; i < Dataset.size(); i++)
        {
        	for(int j = 0; j < Dataset.size(); j++)
        	{
        		if(i == j)
        			continue;
        		
        		Dataset.get(i).addNeighbor(Dataset.get(j), (Dimensions.get(i, j)));
        		//if(i == 0)
        			//System.out.println("Dimensions.get(0," + j + ")= " + Math.abs(Dimensions.get(i, j)));
        	}
        	//System.out.println("Dataset[" + i + "] neighbor size = " + Dataset.get(i).neighbors.size());
        	//System.out.println("Dataset[" + i + "] neighbor size = " + Dataset.get(i).getNeighbors(k).size());
        	
        	/**/
        	//System.out.println("obj.id =" + Dataset.get(i).id);
        	int ind =0;
        	for (DiffusionObject obj : Dataset.get(i).neighbors)
        	{
        		System.out.print("id= " + obj.id + "; dist= " + Dataset.get(i).distances.get(ind) + "| ");
        		ind++;
        	}
        	//System.out.println(" + ");/**/
        }
        
        
    }

    @Override
    public IClusteringParameters getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(IClusteringParameters parameters) {
        /*
         * ParametersFrame pf = new ParametersFrame( parameters );
         * pf.setModalityType(ModalityType.DOCUMENT_MODAL); pf.setVisible( true
         * );
         */
        this.parameters = parameters;
        k = new Integer(parameters.getValue("k")).intValue();
        logger.setParameters(parameters.toString());
    }

    @Override
    public void setObserver(IClusteringObserver observer) {
        // TODO Auto-generated method stub
        this.observer = observer;
    }

    public void setGraphics(Graphics g) {

    }

    public void addLines() {
        this.observer.handleNotify((Object) null);
    }

    @Override
    public String getName() {
        return NBCDiffusionIndex.NAME;
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
    
    public void setDM(Matrix dm)
    {
    	this.Dimensions = dm;
    }
    
    public void setdata(ArrayList<double[]> dd)
    {
    	this.Data = dd;
    }
    
}
