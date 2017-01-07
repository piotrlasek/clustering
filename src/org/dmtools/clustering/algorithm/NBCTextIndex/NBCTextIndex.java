package org.dmtools.clustering.algorithm.NBCTextIndex;

import java.util.ArrayList;
import java.util.Set;

import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;


/**
 * 
 * @author Piotr Lasek
 *
 */
public class NBCTextIndex extends ClusteringTextIndex {
	
	int k = 0;

	public NBCTextIndex(ClusteringSettings clusteringSettings,
			PhysicalDataSet physicalDataSet) {
		
		//super(clusteringSettings, physicalDataSet);
		k = (int) clusteringSettings.getMinClusterCaseCount();
	}
	
	public NBCTextIndex(int k, TextIndex ti) {
		//super(null, null);
		this.k = k;
		this.ti = ti;
	}

	/**
	 * 
	 */
	public MiningObject run() {
		
		// read data
		//prepareData();
		
		ArrayList<TextObject> NoiseSet = new ArrayList<TextObject>();
        int cluster_count = 0;

        // 		
        CalcNDF();

        NoiseSet.clear();
        cluster_count = 0;
        
        ArrayList<TextObject> DPSet = new ArrayList<TextObject>();

        // for each object p in ti.dataset
        for (TextObject p:ti.dataset) {
            // if (p.clst_no != NULL or p.ndf < 1)) continue;
        	
        	/*if (p.id.contains("Pancerz")) {
        		System.out.println();
        	}*/
        	
            if (p.clst_no != -1 || p.ndf < 1)
                continue;
            
            p.clst_no = cluster_count; // label a new cluster
            
            saveClusteredPoint(p);
            
            DPSet.clear(); // initialize DPSet

            // for each object q in kNB(p)          
            ArrayList<TextObject> kNN = p.getNeighbors(k);

            for (int i = 0; i < kNN.size(); i++) {
                // q.clst_no = cluster_count
            	TextObject q = (TextObject) kNN.get(i);
            	
            	/*if (q.id.contains("Pancerz")) {
            		System.out.println();
            	}*/
            	
            	if (q.clst_no != -1) 
            		continue;
            	
                if (q.ndf >= 1) {
                    q.clst_no = cluster_count;                
                    saveClusteredPoint(q);

                    DPSet.add(q);
                }
            }

            // while (DPSet is not empty) // expanding the cluster
            while (!DPSet.isEmpty()) {
                // p = DPset.getFirstObject();
            	TextObject pD = (TextObject) DPSet.remove(0);

            	/*if (pD.id.contains("Pancerz")) {
            		System.out.println();
            	}*/
            	
                // for each object q in kNB(pD)
                ArrayList<TextObject> kNNpD = pD.getNeighbors(k);
                
                for(TextObject q:kNNpD) {
                	/*if (q.id.contains("Pancerz")) {
                		System.out.println();
                	}*/
                	
                    // if (q.clst_no!NULL) continue
                    if (q.clst_no != -1)
                        continue;

                    // if (q.ndf >= 1) DPSet.add(q)
                    if (q.ndf >= 1) {
                        q.clst_no = cluster_count;
                        saveClusteredPoint(q);
                        DPSet.add(q);
                    }
                }
                // DPSet.remove(p);
            }
            cluster_count++;
        }

        // for each object p in ti.dataset // label noise
        for (TextObject p:ti.dataset) {
            if (p.clst_no == -1) {
                p.clst_no = -2;
                saveClusteredPoint(p);
            }
        }
		
		// groups
        //Iterator groupIds = (Iterator) groups.keySet().iterator();
        Set<Integer> keys = groups.keySet();
        
        for(Integer key:keys) {
        	ArrayList<TextObject> group = groups.get(key);
        	System.out.print("GROUP " + key + " (" + group.size() + ") " + ": ");
        	for(TextObject to:group) {
        		System.out.print(to.id + ", ");
        	}
        	System.out.println();
        }
        
		return null;
	}

	private void saveClusteredPoint(TextObject p) {
		if (!groups.containsKey(p.clst_no)) groups.put(p.clst_no, new ArrayList<TextObject>());
		groups.get(p.clst_no).add(p);
	}
	

	
	/**
	 * 
	 */
	private void CalcNDF() {
        ArrayList<TextObject> kNN = new ArrayList<TextObject>();
        
        // for each object p in ti.dataset
        for (TextObject p:ti.dataset) {
            kNN.clear();

            kNN = p.getNeighbors(k);
            p.setSizeOfkNB(kNN.size());

            // for each new object q in kNB(p)
            for(TextObject q : kNN) {
                q.increaseSizeOfRkNB();
            }
        }

        // for each object q in kNB(p)
        for (TextObject p:ti.dataset) {
        	if (p.getSizeOfRkNB() == 0 || p.getSizeOfkNB() == 0) {
        		p.ndf = 0;
        	} else {
        		p.ndf = (double) p.getSizeOfRkNB() / (double) p.getSizeOfkNB();
        	}
        }
    }

}
