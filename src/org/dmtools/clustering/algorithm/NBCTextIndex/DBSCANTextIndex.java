package org.dmtools.clustering.algorithm.NBCTextIndex;

import java.util.ArrayList;

import javax.datamining.MiningObject;


/**
 * 
 * @author Piotr Lasek
 *
 */
public class DBSCANTextIndex extends ClusteringTextIndex {
	
	Double Eps = 0.0;
	Integer MinPts = 0;
	
    public final int UNCLASSIFIED = -2;
    public final int NOISE = -1;

    /**
     * 
     * @param Eps
     * @param MinPts
     * @param ti
     */
	public DBSCANTextIndex(Double Eps, Integer MinPts, TextIndex ti) {
		this.Eps = Eps;
		this.MinPts = MinPts;
		this.ti = ti;
	}

	/**
	 * 
	 */
	public MiningObject run() {
		
		DBSCAN();
		return null;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	Integer nextId(Integer id) {
		id++;
		return id;
	}
	
	/**
	 * 
	 */
	protected void DBSCAN() {
        // SetOfPoints is UNCLASSIFIED
        Integer ClusterId = nextId(NOISE);
        
        for (TextObject point : ti.dataset) {
            //BasicSpatialObject point = (DbscanSpatialObject) p;
            //point.ClId = UNCLASSIFIED;
            point.clst_no = UNCLASSIFIED;
        }

        // ClusterId := nextId(NOISE);
        // FOR i FROM 1 TO SetOfPoints.size DO
        // PointToRemove := SetOfPoints.get(i);
        for (TextObject Point : ti.dataset) {
            //DbscanSpatialObject PointToRemove = (DbscanSpatialObject) p;
            // IF PointToRemove.ClId = UNCLASSIFIED THEN
            ///if (PointToRemove.ClId == UNCLASSIFIED)
            if (Point.clst_no == UNCLASSIFIED)
            // IF ExpandCluster(SetOfPoints, PointToRemove, ClusterId, Eps, MinPts) THEN
            {
                if (ExpandCluster(Point, ClusterId, Eps, MinPts)) {
                    // ClusterId := nextId(ClusterId)
                    ClusterId = nextId(ClusterId);
                    // END IF
                }
                // END IF
            }
        }
        
        // END FOR
        // END;
    } // DBSCAN

	/**
	 * 
	 * @param points
	 * @param clusterId
	 */
	public void changeClId(ArrayList<TextObject> points, Integer clusterId) {
		for(TextObject point:points) {
			changeClId(point, clusterId);
		}
	}
	
	/**
	 * 
	 * @param point
	 * @param clusterId
	 */
	public void changeClId(TextObject q, Integer clusterId) {
		addObjectToGroup(q, clusterId);
		q.clst_no = clusterId;		
	}
	
	/**
	 * 
	 * @param point
	 * @return
	 */
	public Integer getClId(TextObject point) {
		return point.clst_no;
	}
	
	/**
	 * 
	 * @param point
	 * @param Eps
	 * @return
	 */
	ArrayList<TextObject> regionQuery(TextObject point, Double Eps) {
		ArrayList<TextObject> region = new ArrayList<TextObject>();
		
		int index = 0;
		for(TextObject n:point.neighbors) {
			Double d = point.distances.get(index);
			if (1-d <= Eps) { // !!!!!!!!!!!!!!!!!!!
				if (n.clst_no == UNCLASSIFIED) {
					region.add(n);
				}
			} else {
				break;
			}
			index++;
		}
		
		return region;
	}
	
    // ExpandCluster(SetOfPoints, PointToRemove, ClId, Eps, MinPts) : Boolean;
    private boolean ExpandCluster(
            TextObject Point, Integer ClId, Double Eps, Integer MinPts) {
        // seeds :=SetOfPoints.regionQuery(PointToRemove,Eps);
        ArrayList<TextObject> seeds = regionQuery(Point, Eps);
        // IF seeds.size<MinPts THEN // no core point

        if (seeds.size() < MinPts) {
            // SetOfPoint.changeClId(PointToRemove,NOISE);
            changeClId(Point, NOISE);
            // RETURN False;
            return false;
        }
        // ELSE // all points in seeds are density-reachable from PointToRemove
        else {
            // SetOfPoints.changeClIds(seeds,ClId);
            changeClId(seeds, ClId);
            // seeds.delete(PointToRemove);
            //seeds.remove(PointToRemove);
            changeClId(Point, ClId);
            // WHILE seeds <> Empty DO
            while (seeds.size() > 0) {
                // currentP := seeds.first();
            	TextObject currentP = seeds.get(0);
                // result := SetOfPoints.regionQuery(currentP, Eps);
                ArrayList<TextObject> result = regionQuery(currentP, Eps);
                // IF result.size >= MinPts THEN
                if (result.size() >= MinPts) {
                    // FOR i FROM 1 TO result.size DO
                    for (int i = 1; i < result.size(); i++) {
                        // resultP := result.get(i);
                        //DbscanSpatialObject resultP = (DbscanSpatialObject) result
                        //        .get(i);
                    	TextObject resultP = result.get(i);
                        // IF resultP.ClId IN {UNCLASSIFIED, NOISE} THEN
                        if (getClId(resultP) == UNCLASSIFIED
                                || getClId(resultP) == NOISE) {
                            // IF resultP.ClId = UNCLASSIFIED THEN
                            if (getClId(resultP) == UNCLASSIFIED) {
                                // seeds.append(resultP);
                                seeds.add(resultP);
                                // END IF;
                            }
                            // SetOfPoints.changeClId(resultP,ClId);
                            changeClId(resultP, ClId);
                            // END IF; // UNCLASSIFIED or NOISE
                        }
                        // END FOR;
                    }
                    // END IF; // result.size >= MinPts
                }
                // seeds.delete(currentP);
                seeds.remove(0);
                // END WHILE; // seeds <> Empty
            }
            // RETURN True;
            return true;
            // END IF
        }
        // END; // ExpandCluster
    }
}
