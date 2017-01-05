package org.dmtools.clustering.DBSCAN;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;

import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.CNBC.CNBCRTreePoint;
import org.dmtools.clustering.CNBC.InstanceConstraints;
import org.dmtools.clustering.CNBC.MyFrame;
import org.dmtools.clustering.old.BasicSpatialObject;
import org.dmtools.clustering.old.ClusteringLogger;
import org.dmtools.clustering.old.IClusteringData;
import org.dmtools.clustering.old.IClusteringObject;
import org.dmtools.clustering.old.ISpatialObject;
import org.dmtools.clustering.old.rtree.CustomRTree;
import org.dmtools.datamining.data.CDMFilePhysicalDataSet;

//import util.CSV;

import javax.datamining.JDMException;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalAttribute;
import javax.datamining.data.PhysicalDataSet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * 
 * @author
 *
 */
public class CDBSCAN extends DBSCANBase {

	public static final String NAME = "CDBSCAN";

	ClusteringSettings clusteringSettings;
	PhysicalDataSet physicalDataSet;
	ArrayList<double[]> data;
	Collection<PhysicalAttribute> attributes;
	int numberOfDimensions;
	double[] min = null;
	double[] max = null;

	ClusteringLogger logger = new ClusteringLogger(getName());
	InstanceConstraints ic = new InstanceConstraints();

	public CDBSCAN(ClusteringSettings clusteringSettings, PhysicalDataSet physicalDataSet) {
		this.clusteringSettings = clusteringSettings;
		this.physicalDataSet = physicalDataSet;

		try {
			attributes = physicalDataSet.getAttributes();
		} catch (JDMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		numberOfDimensions = attributes.size();

		min = new double[numberOfDimensions];
		max = new double[numberOfDimensions];
	}

	@Override
	public void run() {
		System.out.println(getDescription());
		// Read data-set
		prepareData();
		// Prepare experiment constraints.		
		generateConstraints();
		// Main		
		C_DBSCAN();
		//Post process
		IClusteringData cd = this.getResult();
		Collection<IClusteringObject> result = cd.get();
		visualizeResult(result);
//		CSV.saveResults(result, "CDBSCAN");
	}

	private void visualizeResult(Collection<IClusteringObject> result) {
		// Show result
		MyFrame mf = new MyFrame(result, ic, null, null, null);
		mf.setPreferredSize(new Dimension(700, 600));
		JFrame f = new JFrame();
		JScrollPane scrollPane = new JScrollPane(mf);
		mf.setScrollPane(scrollPane);
		scrollPane.setAutoscrolls(true);
		f.add(scrollPane);
		f.pack();
		f.setTitle("CDBSCAN");
		f.setSize(new Dimension(700, 600));
		f.setVisible(true);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private void generateConstraints() {
//		ic.add(new double[] { 231, 104 }, new double[] { 273, 132 }, false);
//		ic.add(new double[] { 127, 146 }, new double[] { 165, 174 }, false);
//		ic.add(new double[] { 129, 155 }, new double[] { 189, 172 }, false);
//
//		 ic.add(new double[]{231, 104}, new double[]{273, 132}, true);
//		 ic.add(new double[]{148, 78}, new double[]{183, 86}, true);
//		 ic.add(new double[]{188, 140}, new double[]{162, 166}, true);

		// ic.add(new double[]{192,240}, new double[]{298,220}, true);
		// ic.add(new double[]{222, 284}, new double[]{280, 316}, false);
		// ic.add(new double[]{220, 278}, new double[]{244, 316}, false);
		//
		// ic.add(new double[]{200, 20}, new double[]{400, 20}, false);
		// ic.add(new double[]{30, 200}, new double[]{30, 400}, true);

	}

	@Override
	public void setData(IClusteringData data) {
		super.setData(data);
	}

	@Override
	public String getName() {
		return CDBSCAN.NAME;
	}

	@Override
	protected void createIndex() {
		this.isp = new CustomRTree();
	}

	@Override
	public String getDescription() {
		return "CDBSCAN Algorithm";
	}

	public void addLines() {
		if (observer != null) {
			observer.handleNotify((Object) null);
		}
	}

	/**
	 *
	 * @return
	 */
	public InstanceConstraints getConstraints() {
		return ic;
	}

	/**
	 *
	 */
	public void prepareData() {
		ArrayList<Object[]> rawData = ((CDMFilePhysicalDataSet) getPhysicalDataSet()).getData();
		data = new ArrayList<double[]>();

		SetOfPoints = new ArrayList<ISpatialObject>();

		int i = 0;
		for (Object[] rawRecord : rawData) {
			double[] record = new double[attributes.size() + 1];
			int d = 0;
			for (PhysicalAttribute attribute : attributes) {
				record[d] = new Double(rawData.get(i)[d].toString());
				if (min[d] == 0)
					min[d] = record[d];
				else if (min[d] > record[d])
					min[d] = record[d];
				if (max[d] < record[d])
					max[d] = record[d];
				d++;
			}
			record[d] = -1; // UNCLUSTERED
			// data.add(record);
			CNBCRTreePoint mp = new CNBCRTreePoint(record, CDMCluster.UNCLASSIFIED);
			SetOfPoints.add(mp);
			i++;
		}

		logger.indexStart();
		createIndex();
		isp.add((ArrayList<ISpatialObject>) SetOfPoints);
		logger.indexEnd();

	}

	public PhysicalDataSet getPhysicalDataSet() {
		return physicalDataSet;
	}

	private ArrayList<ISpatialObject> regionQuery(ISpatialObject Point, double Eps) {
		ArrayList<ISpatialObject> neighbors = (ArrayList<ISpatialObject>) isp.getNeighbors(Point, Eps);
		return neighbors;
	}

	// ================================================================================
	
	protected void C_DBSCAN() {
		Integer ClusterId = nextId(CDMCluster.NOISE);

		/** Arraylist of Deferred points */
		ArrayList<ISpatialObject> Rd = initializedPointsAndAssignDeferredPoints();
		for (ISpatialObject point : SetOfPoints) {
			if (getClId(point) == CDMCluster.UNCLASSIFIED || getClId(point) == CDMCluster.DEFERRED)
				if (!isDefferedPoint(point, Rd)) {
					if (C_ExpandCluster(SetOfPoints, point, ClusterId, Eps, MinPts)) {
						ClusterId = nextId(ClusterId);
					}
				}
		}
		assignDefferedPoints2(Rd);
	}

	// ================================================================================
	
	private boolean isDefferedPoint(ISpatialObject p, ArrayList<ISpatialObject> Rd) {
		for (ISpatialObject dp : Rd) {
			if (dp.getCoordinates()[0] == p.getCoordinates()[0] && dp.getCoordinates()[1] == p.getCoordinates()[1])
				return true;
		}
		return false;
	}

	// ================================================================================
	
	private void assignDefferedPoints(ArrayList<ISpatialObject> Rd) {
		for (ISpatialObject q : Rd) {
			/** ArrayList of parentPoints */
			ArrayList<ISpatialObject> p = getPointParent(q);

			if (p.size() == 0) {
				setClId(q, CDMCluster.NOISE);
				continue;
			}
			
			int gp = getNearestCluster(p);
			
			if (gp == CDMCluster.UNCLASSIFIED) {
				setClId(q, CDMCluster.NOISE);
				continue;
			}
			ArrayList<ISpatialObject> pCannot = new ArrayList<ISpatialObject>();
			pCannot = ic.getCannotLinkObjects((CNBCRTreePoint) p.get(0));

			int gpCannot = getNearestCluster(pCannot);
			if (gp != gpCannot)
				setClId(q, CDMCluster.NOISE);
			else
				changeClId(q, gp);
		}
	}

	// ================================================================================
	private void assignDefferedPoints2(ArrayList<ISpatialObject> Rd) {
		for (ISpatialObject q : Rd) {
			/** ArrayList of parentPoints */

			int gp = getNearestCluster(q);
			if (gp == CDMCluster.UNCLASSIFIED) {
				setClId(q, CDMCluster.NOISE);
				continue;
			}
			ArrayList<ISpatialObject> qCannot = new ArrayList<ISpatialObject>();
			qCannot = ic.getCannotLinkObjects(q);

			int gpCannot = getNearestCluster(qCannot);

			if (gp == gpCannot){
				setClId(q, CDMCluster.NOISE);
			}
			else
				changeClId(q, gp);
		}
	}

	// ================================================================================
	/**
	 * 
	 * @param points
	 * @return
	 */
	
	protected int getNearestCluster(ArrayList<ISpatialObject> points) {
		// Arbitrary maximum distance
		int minDistance = 10000;
		int nearestCluster = CDMCluster.UNCLASSIFIED;
		for (ISpatialObject p : points) {
			for (ISpatialObject clusteredPoint : SetOfPoints) {
				if (distance(p, clusteredPoint) < minDistance && getClId(clusteredPoint) >= 0
						&& distance(p, clusteredPoint) <= Eps) {
					minDistance = distance(p, clusteredPoint);
					nearestCluster = getClId(clusteredPoint);
				}
			}
		}
		// no cluster found
		return nearestCluster;
	}

	// ================================================================================
	protected int getNearestCluster(ISpatialObject p) {
		int minDistance = 10000;
		int nearestCluster = CDMCluster.UNCLASSIFIED;
		for (ISpatialObject clusteredPoint : SetOfPoints) {
			// TODO: make sure the coordinated can be compared
			if (distance(p, clusteredPoint) < minDistance && getClId(clusteredPoint) >= 0
					&& distance(p, clusteredPoint) <= Eps) {
				minDistance = distance(p, clusteredPoint);
				nearestCluster = getClId(clusteredPoint);
			}
		}
		// no cluster found
		return nearestCluster;
	}

	// ================================================================================
	
	protected int distance(ISpatialObject p1, ISpatialObject p2) {
		double d;
		d = (2 * Math.abs(p1.getCoordinates()[0] - p2.getCoordinates()[0]))
				+ (2 * Math.abs(p1.getCoordinates()[1] - p2.getCoordinates()[1]));
		d = Math.sqrt(d);
		return (int) d;
	}

	// ================================================================================

	/**
	 * 
	 */
	protected ArrayList<ISpatialObject> getPointParent(ISpatialObject point) {

		ArrayList<ISpatialObject> pNN = regionQuery(point, Eps);
		ArrayList<ISpatialObject> parentList = new ArrayList<ISpatialObject>();

		for (ISpatialObject p : pNN) {
			if (ic.getCannotLinkObjects(p).size() != 0)
				parentList.add(p);
		}

		return parentList;
	}

	// ================================================================================
	/**
	 * 
	 */
	
	private ArrayList<ISpatialObject> initializedPointsAndAssignDeferredPoints() {
		ArrayList<ISpatialObject> Rd = new ArrayList<ISpatialObject>();
		for (ISpatialObject point : SetOfPoints) {
			setClId(point, CDMCluster.UNCLASSIFIED);
			if (ic.getCannotLinkObjects(point).size() != 0) {
				Rd.add(point);
				setClId(point, CDMCluster.DEFERRED);
			}
		}
		return Rd;
	} // C_DBSCAN
	
		// ================================================================================
		// ExpandCluster(SetOfPoints, Point, ClId, Eps, MinPts) : Boolean;

	protected boolean C_ExpandCluster(ArrayList<ISpatialObject> SetOfPoints, ISpatialObject Point, Integer ClId,
			Double Eps, Integer MinPts) {
		ArrayList<ISpatialObject> seeds1 = regionQuery(Point, Eps);
		ArrayList<ISpatialObject> seeds = new ArrayList<ISpatialObject>();
		seeds.addAll(seeds1);
		if (seeds.size() < MinPts) {
			changeClId(Point, CDMCluster.NOISE);
			return false;
		} else {
			if (ic.getMustLinkObjects(Point).size() != 0) {
				ArrayList<ISpatialObject> mLinkPoints = new ArrayList<ISpatialObject>();
				mLinkPoints = ic.getMustLinkObjects(Point);
				for (ISpatialObject pp : mLinkPoints) {
					double[] cc = new double[3];
					cc[0] = pp.getCoordinates()[0];
					cc[1] = pp.getCoordinates()[1];
					cc[2] = -1;
					pp.setCoordinates(cc);
					pp.setValue(Point.getValue());
					BasicSpatialObject bp = new BasicSpatialObject(cc);
					seeds.add(bp);
				}
			}

			changeClId(seeds, ClId);
			changeClId(Point, ClId);
			while (seeds.size() > 0) {
				ISpatialObject currentP = seeds.get(0);
				ArrayList<ISpatialObject> curSeeds = regionQuery(currentP, Eps);
				if (curSeeds.size() >= MinPts) {
					for (int i = 1; i < curSeeds.size(); i++) {
						ISpatialObject q = curSeeds.get(i);
						/////////////////////////
						if (getClId(q) == CDMCluster.UNCLASSIFIED)
							if (ic.getMustLinkObjects(q).size() != 0) {
								ArrayList<ISpatialObject> mLinkPoints = new ArrayList<ISpatialObject>();
								mLinkPoints = ic.getMustLinkObjects(q);
								for (ISpatialObject pp : mLinkPoints) {
									double[] cc = new double[3];
									cc[0] = pp.getCoordinates()[0];
									cc[1] = pp.getCoordinates()[1];
									cc[2] = -1;
									pp.setCoordinates(cc);
									pp.setValue(q.getValue());
									BasicSpatialObject bp = new BasicSpatialObject(cc);
									changeClId(bp, ClId);
									seeds.add(bp);
								}
							}
						if (getClId(q) == CDMCluster.UNCLASSIFIED) {
							changeClId(q, ClId);
							seeds.add(q);
						}
						if (getClId(q) == CDMCluster.NOISE) {
							changeClId(q, ClId);
						}
					}
				}
				seeds.remove(0);
			}
			return true;
		}
	}
}
