package org.dmtools.clustering.algorithm.DBSCAN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.datamining.JDMException;
import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalAttribute;
import javax.datamining.data.PhysicalDataSet;

import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.algorithm.NBC.Point;
import org.dmtools.clustering.model.IClusteringData;
import org.dmtools.clustering.model.IClusteringObject;
import org.dmtools.clustering.model.IClusteringObserver;
import org.dmtools.clustering.model.ISpatialObject;
import org.dmtools.datamining.data.CDMFilePhysicalDataSet;

import util.Distance;

// This implementation uses triangle inequality property
// for clustering network traffic data.

/**
 * 
 * @author Piotr Lasek
 *
 */
public class DbScanNetTraffic extends CDMBasicClusteringAlgorithm
	implements IClusteringObserver {

	// System variables
	public static final String NAME = "DbScanNetTraffic     ";
	private static final int NOISE = -1;
	private static final int UNCLASSIFIED = -2;
    Collection<IClusteringObject> input;

    // DBSCAN variables
    ArrayList<Point> D = new ArrayList<Point>();
    ArrayList<Point> D1 = new ArrayList<Point>();
    Point referencePoint;
    double[] maxCoordinates;
    double[] minCoordinates;
	private ArrayList<double[]> data;
	private Collection<PhysicalAttribute> attributes;
	private double[] min;
	private double[] max;
	private int numberOfDimensions;
	private static Double Eps = null;
	private static Integer MinPts = null;
	
	/**
	 * 
	 * @param clusteringSettings
	 * @param physicalDataSet
	 */
    public DbScanNetTraffic(ClusteringSettings clusteringSettings,
			PhysicalDataSet physicalDataSet) {
		
    	super(clusteringSettings, physicalDataSet);
		
    	try {
			attributes = physicalDataSet.getAttributes();
		} catch (JDMException e) {
			e.printStackTrace();
		}
    	
		//k = (int) clusteringSettings.getMinClusterCaseCount();
		MinPts = 1000;
		Eps = 0.4d;
		numberOfDimensions = attributes.size();
		min = new double[numberOfDimensions];
		max = new double[numberOfDimensions];
	}
    
    /**
     * 
     */
    @Override
	public MiningObject run() {
    	prepareDataNet();
    	sortAllPointsInD(this.referencePoint);
    	TI_DBSCAN();
		return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    /**
     * 
     */
	public void prepareDataNet() {
		ArrayList<Object[]> rawData =
				((CDMFilePhysicalDataSet) getPhysicalDataSet()).getData();

		data = new ArrayList<double[]>();

		for(Object[] rawRecord : rawData) {
			double[] record = new double[attributes.size() ];
			
			for(int d = 0; d < attributes.size(); d++) {
				
				Object o = rawRecord[d];
				Integer v = null;
				
				switch (d) {
				case 0: { // ipFlags
					v = Integer.parseInt(
						o.toString().replace("0x",  ""), 16);
				}
					break;
					
				case 1: { // tcpFlags
					if (o == null || o.toString().length() == 0) {
						v = -1;  
					} else {
						v = Integer.parseInt(
							o.toString().replace("0x",  ""), 16);
					}
				}
					break;
					
				case 2: { // length
					 v = Integer.parseInt(o.toString());
					 
					 if (v >=    0 && v <=   19) v =  1; else 
				 	 if (v >=   20 && v <=   39) v =  2; else 
					 if (v >=   40 && v <=   79) v =  3; else 
			     	 if (v >=   80 && v <=  159) v =  4; else 
				 	 if (v >=  160 && v <=  319) v =  5; else 
			 		 if (v >=  320 && v <=  639) v =  6; else 
		 			 if (v >=  640 && v <= 1279) v =  7; else											 
	 				 if (v >= 1280 && v <= 2559) v =  8; else 
 					 if (v >= 2560 && v <= 5119) v =  9; else 
					 if (v >  5119)             v = 10; 
				}
					break;
					
				case 3: { // ttl
					v = Integer.parseInt(o.toString());
					
					 if (v >=    1 && v <=   31) v =  1; else 
				 	 if (v >=   32 && v <=   47) v =  2; else 
					 if (v >=   48 && v <=   63) v =  3; else 
			     	 if (v >=   64 && v <=   71) v =  4; else 
				 	 if (v >=   72 && v <=   95) v =  5; else 
			 		 if (v >=   96 && v <=  111) v =  6; else 
		 			 if (v >=  112 && v <=  127) v =  7; else											 
	 				 if (v >=  128 && v <=  143) v =  8; else 
 					 if (v >=  144 && v <=  159) v =  9; else 
					 if (v >=  160 && v <=  191) v = 10; else 
					 if (v >=  192 && v <=  255) v = 11; 
				}
					break;
					
				case 4: { // icmType
					if (o == null || o.toString().length() == 0) {
						v = -1;
 					} else {
 						v = Integer.parseInt(o.toString());
 					}
				}
					break;
					
				case 5: { // udpLength 
					if (o == null || o.toString().length() == 0) {
						v = 1;
 					} else {
					 v = Integer.parseInt(o.toString());
					
				 	 if (v >=    1 && v <=    62) v =  2; else 
					 if (v >=   63 && v <=   125) v =  3; else 
			     	 if (v >=  126 && v <=   250) v =  4; else 
				 	 if (v >=  251 && v <=   500) v =  5; else 
			 		 if (v >=  501 && v <=  1000) v =  6; else 
		 			 if (v >= 1001 && v <=  2000) v =  7; else											 
	 				 if (v >= 2001 && v <=  4000) v =  8; else 
 					 if (v >= 4001 && v <=  8000) v =  9; else 
					 if (v >= 8001 && v <= 65535) v = 10; 
 					}
				}	
					break;
					
				case 6: { // decision
					v = 0;
				}
					
					break;
				}
				
				record[d] = v;
				
				// set min and max values in in dimensions
				if (min[d] == 0) {
					min[d] = record[d];
				} else {
					if (min[d] > record[d]) min[d] = record[d];
					if (max[d] < record[d]) max[d] = record[d];
				}
			}
			data.add(record);
		}
		
		
		// reference point
		double[] rp = new double[attributes.size()];
	
		for(int d = 0; d < attributes.size(); d++) {
			rp[d] = min[d];
		}
	
		referencePoint = new Point(rp);
	}
	
    /**
     * 
     */
    protected void TI_DBSCAN() {
    	
        Integer ClusterId = nextId(NOISE);

        for (int i = 0; i < D.size(); i++) {
            Point p = D.get(i);
            p.dist = Distance.DistancePacket(p, referencePoint);
            p.ClusterId = DbScanNetTraffic.UNCLASSIFIED;
        }

        for (int i = 0; i < D.size(); i++) {
            Point p = D.get(i);
            if (p != null && p.ClusterId == DbScanNetTraffic.UNCLASSIFIED) {
                if (TI_ExpandCluster(D, p, ClusterId, Eps, MinPts)) {
                    ClusterId = nextId(ClusterId);
                }
            }
        }
    }

    /**
     * 
     * @param D
     * @param p
     * @param Eps
     * @param MinPts
     * @return
     */
    public ArrayList<Point> TI_Neighborhood(ArrayList<Point> D, Point p,
            double Eps, int MinPts) {
        ArrayList<Point> backwardNeighborhood = TI_Backward_Neighborhood(D, p,
                Eps, MinPts);
        ArrayList<Point> forwardNeighborhood = TI_Forward_Neighborhood(D, p,
                Eps, MinPts);
        ArrayList<Point> neighborhood = new ArrayList<Point>(
                forwardNeighborhood);
        neighborhood.addAll(backwardNeighborhood);
        return neighborhood;
    }

    /**
     * 
     * @param D
     * @param p
     * @param Eps
     * @param MinPts
     * @return
     */
    public ArrayList<Point> TI_Backward_Neighborhood(ArrayList<Point> D,
            Point p, double Eps, int MinPts) {
        ArrayList<Point> seeds = new ArrayList<Point>();
        // backwardThreshold = p.dist - Eps;
        double backwardThreshold = p.dist - Eps;
        // for each point q in the ordered set D starting from
        // the point immediately preceding point p until
        // the first point in D do
        for (int i = p.pos - 1; i >= 0; i--) {
            Point q = D.get(i);
            if (q != null) {
                // if q.dist < backwardThreshold then // p.dist � q.dist > Eps
                // break;
                // endif
                if (q.dist < backwardThreshold) {
                    break;
                }
                // if Distance2(q, p) <= Eps2 then
                // append q to seeds;
                // endif
                if (Distance.DistancePacket2(q, p) <= (Eps * Eps)) {
                    seeds.add(q);
                }
            }
            // endfor
        }
        // return seeds
        return seeds;
    }

    /**
     * @param D
     * @param p
     * @param Eps
     * @param MinPts
     * @return
     */
    public ArrayList<Point> TI_Forward_Neighborhood(ArrayList<Point> D,
            Point p, double Eps, int MinPts) {
        // function TI-Forward-Neighborhood(D, point p, Eps, MinPts)
        /* assert: D is ordered non-decreasingly w.r.t. dist */
        // seeds = {};
        ArrayList<Point> seeds = new ArrayList<Point>();
        // forwardThreshold = Eps + p.dist;
        double forwardThreshold = Eps + p.dist;
        // for each point q in the ordered set D starting from
        // the point immediately following point p until
        // the last point in D do
        for (int i = p.pos + 1; i < D.size(); i++) {
            Point q = D.get(i);
            if (q != null) {
                // if q.dist > forwardThreshold then // q.dist � p.dist > Eps?
                // break;
                // endif
                if (q.dist > forwardThreshold) {
                    break;
                }
                // if Distance2(q, p) <= Eps2 then
                // append q to seeds;
                // endif
                if (Distance.DistancePacket2(q, p) <= (Eps * Eps)) {
                    seeds.add(q);
                }
            }
            // endfor
            // return seeds
        }
        return seeds;
    }

    /**
     * 
     * 
     * @param SetOfPoints
     * @param Point
     * @param ClusterId
     * @param Eps
     * @param MinPts
     * @return
     */
    // ExpandCluster(Dataset, PointToRemove, ClId, Eps, MinPts) : Boolean;
    protected boolean TI_ExpandCluster(ArrayList<Point> D, Point p,
            Integer ClId, Double Eps, Integer MinPts) {
        // function TI-ExpandCluster(D, point p, ClId, Eps, MinPts)
        /* Assert: TI-Neighborhood does not include p */
        // seeds = TI-Neighborhood(D, p, Eps, MinPts);
        ArrayList<Point> seeds = TI_Neighborhood(D, p, Eps, MinPts);
        // ArrayList<PointToRemove> seeds = TI_Neighborhood(D, p, Eps, MinPts);
        // p.NeighborsNo = p.NeighborsNo + |seeds|; // include p itself
        p.NeighborsNo = p.NeighborsNo + seeds.size();
        // if p.NeighborsNo < MinPts then
        if (p.NeighborsNo < MinPts) {
            // p.ClusterId = NOISE;
            p.ClusterId = DbScanNetTraffic.NOISE;
            // for each point q in seeds do
            for (Point q : seeds) {
                // append p to q.Border
                q.Border.add(p);
                // q.NeighborsNo = q.NeighborsNo + 1
                q.NeighborsNo = q.NeighborsNo + 1;
                // endfor
            }
            // move p from D to D�; // D� stores analyzed points
            // return FALSE
            return false;
            // else
        } else {
            // p.ClusterId = ClId;
            p.ClusterId = ClId;
            // for each point q in seeds do
            for (Point q : seeds) {
                // q.ClusterId = ClId;
                q.ClusterId = ClId;
                // q.NeighborsNo = q.NeighborsNo + 1;
                q.NeighborsNo = q.NeighborsNo + 1;
                // endfor
            }
            // for each point q in p.Border do
            while (p.Border.size() > 0) {
                Point q = p.Border.get(0);
                // D�.q.ClusterId = ClId; //assign cluster id to q in D�
                q.ClusterId = ClId;
                D1.add(q);
                q.pos2 = D1.size() - 1;
                // delete q from p.Border;
                p.Border.remove(0);
                // endfor
            }
            // move p from D to D�; // D� stores analyzed points
            // PointToRemove p1 = D.remove(p.pos);
            Point p1 = D.get(p.pos);
            D.set(p1.pos, null);
            p1.pos = -1;
            D1.add(p1);
            p1.pos2 = D1.size() - 1;
            // while |seeds| > 0 do
            while (seeds.size() > 0) {
                // curPoint = first point in seeds;
                Point curPoint = seeds.get(0);
                // curSeeds = TI-Neighborhood(D, curPoint, Eps, MinPts);
                ArrayList<Point> curSeeds = TI_Neighborhood(D, curPoint, Eps,
                        MinPts);
                // curPoint.NeighborsNo = curPoint.NeighborsNo + |curSeeds|;
                curPoint.NeighborsNo = curPoint.NeighborsNo + curSeeds.size();
                // if curPoint.NeighborsNo < MinPts then
                if (curPoint.NeighborsNo < MinPts) {
                    // for each point q in curSeeds do
                    for (Point q : curSeeds) {
                        // append p to q.Border
                        q.Border.add(p);
                        // q.NeighborsNo = q.NeighborsNo + 1
                        q.NeighborsNo = q.NeighborsNo + 1;
                        // endfor
                    }
                    // for each point q in p.Border do
                    // delete q from p.Border;
                    // endfor
                    p.Border.clear();
                    // else
                } else {
                    // for each point q in curSeeds do
                    while (curSeeds.size() > 0) {
                        // for(PointToRemove q:curSeeds) {
                        Point q = curSeeds.get(0);
                        // q.NeighborsNo = q.NeighborsNo + 1;
                        q.NeighborsNo = q.NeighborsNo + 1;
                        // if q.ClusterId = UNCLASSIFIED then
                        if (q.ClusterId == DbScanNetTraffic.UNCLASSIFIED) {
                            // q.ClusterId = ClId;
                            q.ClusterId = ClId;
                            // move q from curSeeds to seeds;
                            Point q1 = curSeeds.remove(0);
                            seeds.add(q1);
                            // else
                        } else {
                            // delete q from curSeeds;
                            curSeeds.remove(0);
                            // endif
                        }
                        // endfor
                    }

                    // for each point q in p.Border do

                    while (p.Border.size() > 0) {
                        Point q = p.Border.get(0);
                        // D�.q.ClusterId = ClId;//assign cl.id to q in D�
                        D1.get(q.pos2).ClusterId = ClId;
                        // delete q from p.Border;
                        p.Border.remove(q.pos);
                        // endfor
                    }
                    // endif
                }
                // move curPoint from D to D�; // D� stores analyzed points
                D1.add(curPoint);
                curPoint.pos2 = D1.size() - 1;
                // delete curPoint from seeds;
                seeds.remove(0);
                // endwhile
            }
            // return TRUE
            return true;
            // endif
        }
    }

    /**
     * 
     * @param clusterId
     * @return
     */
    private int nextId(int clusterId) {
    	clusterId++;
		return clusterId;
	}

    /**
     * 
     * @param Point
     * @param ClId
     */
    public void setClId(ISpatialObject Point, int ClId) {
        Point.setClusterId(ClId);
    }

    // ------------------------------------------------------------------------

    /**
     * 
     * @param refPoint
     * @return 
     */
    public void sortAllPointsInD(Point referencePoint) {
        D = createSortedTableD(referencePoint);
    }

    /**
     * 
     * @param referencePoint
     * @return
     */
    public ArrayList<Point> createSortedTableD(Point referencePoint) {

    	ArrayList<Point> sortedData = new ArrayList<Point>();
    	double maxDist = 0;
    	
    	HashMap<Double, ArrayList<Point>> distances = 
    			new HashMap<Double, ArrayList<Point>>();
    	
        for (double[] o : data) {

            Point point = new Point(o);

            double distance = Distance.DistancePacket(referencePoint, point);
            double distance2 = Distance.DistancePacket2(referencePoint, point);
            point.dist = distance;
            point.dist2 = distance2;
            
            ArrayList<Point> l = distances.get(distance);
            if (l == null) {
            	l = new ArrayList<Point>();
            	l.add(point);
            	distances.put(distance,  l);
            } else {
            	l.add(point);
            }
            
            /*if (sortedData.size() == 0) {
                sortedData.add(point);
            } else if (point.dist >= maxDist) {
            	maxDist = point.dist;
            	sortedData.add(point);
            }
            else {
                for (int i = 0; i < sortedData.size(); i++) {
                    PointToRemove tip = sortedData.get(i);

                    if (distance <= tip.dist) {
                        // insert before the current point
                        sortedData.add(i, point);
                        break;
                    } else if (i == sortedData.size() - 1) {
                        // insert at the end
                    	sortedData.add(point);
                    	maxDist = point.dist;
                        break;
                    } else {
                        // compare to the next point
                        ;
                    }
                }
            }*/
        }
        
        Set<Double> keys = distances.keySet();
        ArrayList<Double> keysS = new ArrayList<Double>();
        
        for(Double k : keys) {
        	if (keysS.size() == 0) {
        		keysS.add(k);
        	} else {
	        	for(int i = 0; i < keysS.size(); i++) {
	        		Double k1 = keysS.get(i);
	        		if (k <= k1) {
	        			keysS.add(i, k);
	        			break;
	        		} else if (i == keysS.size() - 1) {
	        			keysS.add(k);
	        			break;
	        		}
	        	}
        	}
        }
        
        
        //Double[] keysD = (Double[]) keys.toArray();
        //Arrays.sort(keysD);
        
        for (Double k : keysS) {
        	sortedData.addAll(distances.get(k));
        }
        
        for (int i = 0; i < sortedData.size(); i++) {
            Point px = sortedData.get(i);
            px.pos = i;
        }
        
        return sortedData;
    }


    /**
     * 
     */
    protected void determineBorderCoordinates() {
        minCoordinates = input.iterator().next().getSpatialObject().
                getValues().clone();
        maxCoordinates = minCoordinates.clone(); 

        for (IClusteringObject o : input) {
            double[] coordinates = o.getSpatialObject().getValues();
            for(int i = 0; i < coordinates.length; i++) {
                
                if (minCoordinates[i] > coordinates[i]) {
                    minCoordinates[i] = coordinates[i];
                }

                if (maxCoordinates[i] < coordinates[i]) {
                    maxCoordinates[i] = coordinates[i];
                }
            }
        }
    }

	@Override
	public void handleNotify(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleNotify(Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleNotify(IClusteringData data) {
		// TODO Auto-generated method stub
		
	}
}
