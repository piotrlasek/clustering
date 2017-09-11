package org.dmtools.clustering.algorithm.KMeans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.CDMClusteringModel;
import org.dmtools.clustering.algorithm.CNBC.InstanceConstraints;
import org.dmtools.clustering.algorithm.DBSCAN.DBSCANBase;
import org.dmtools.clustering.algorithm.common.PlotPanel;
import org.dmtools.clustering.model.IClusteringData;
import org.dmtools.clustering.old.ClusteringTimer;
import org.dmtools.datamining.base.ScatterAdd;
import org.dmtools.datamining.data.CDMFilePhysicalDataSet;
import org.dmtools.datamining.resource.CDMBasicMiningObject;
import spatialindex.spatialindex.Point;

import javax.datamining.JDMException;
import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalAttribute;
import javax.datamining.data.PhysicalDataSet;
import java.util.*;


public class KMeansAlgorithm extends CDMBasicClusteringAlgorithm {
	
	int k = 0;

	int numberOfDimensions;
	double[] min = null;
	double[] max = null;
	Collection<PhysicalAttribute> attributes;
	ArrayList<double[]> tempPoints;
	int maxIterations;
	int[] clusterCount;
	ClusteringTimer logger = new ClusteringTimer(getName());
	PhysicalDataSet physicalDataSet;
	protected CDMBasicMiningObject basicMiningObject = new CDMBasicMiningObject();
	protected final static Logger log = LogManager.getLogger(CDMBasicClusteringAlgorithm.class.getSimpleName());

	/**
	 *
	 * @return
	 */
	public String getName() {
		return KMeansAlgorithmSettings.NAME;
	}

	/**
	 *
	 * @param clusteringSettings
	 * @param physicalDataSet
	 */
	public KMeansAlgorithm(ClusteringSettings clusteringSettings,
			PhysicalDataSet physicalDataSet)
	{
		super(clusteringSettings, physicalDataSet);
		KMeansAlgorithmSettings kmas = (KMeansAlgorithmSettings) clusteringSettings.getAlgorithmSettings();

		this.physicalDataSet = physicalDataSet;

		k = kmas.getK();
		maxIterations = kmas.getMaxIterations();
		log.info("k = " + k);
		clusterCount = new int[k];

		// get attributes
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

	/**
	 *
	 * @return
	 */
	public MiningObject runAlgorithm() {
		CDMClusteringModel ccm = new CDMClusteringModel();

		prepareData();

		initializeTemporaryPoints();
		
		for(int i = 0; i < maxIterations; i++) {
			log.info("Iteration: " + i);
			for(double[] point : data) {
				int indexOfClosestTempPoint = 0;
				indexOfClosestTempPoint = getIndexOfClosestCluster(point);
				point[numberOfDimensions] = indexOfClosestTempPoint; 
			}

			log.info("Updating cluster centers.");
			updateTemporaryPoints();

			ScatterAdd sa = new ScatterAdd("k-Means", data, tempPoints);
			sa.setSize(400, 500);
			sa.setVisible(true);
			sa.toFront();
		}

		log.info("Printing the result.");

		return basicMiningObject;
	}

	@Override
	public String getDescription() {
		return "(k = )";
	}

	/**
	 * 
	 * @param point
	 * @return
	 */
	private int getIndexOfClosestCluster(double[] point) {
		
		ArrayList<Double> dists = new ArrayList();

		int indexOfClosestTempPoint = 0;
		
		for(int i = 0; i < k; i++) {
			double[] ti = tempPoints.get(i);
			dists.add(dist(point, ti));
		}


		Double minDist = Collections.min(dists);
		indexOfClosestTempPoint = dists.indexOf(minDist);

		/*
		for(int i = 1; i < k; i++) {
			if (dists[i - 1] > dists[i]) {
				indexOfClosestTempPoint = i;
			}
		}*/
		
		return indexOfClosestTempPoint;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public double dist(double[] a, double[] b) {
		double temp = 0;

		for(int i = 0; i < Math.min(a.length, b.length); i++) {
			temp += Math.pow(a[i] - b[i], 2);
		}
		
		return Math.sqrt(temp);
	}

	/**
	 * Clears information about number of objects assigned to a cluster.
	 */
	private void clearClusterCount() {
		for(int i  = 0; i < k; i++) {
			clusterCount[i] = 0;
		}
	}
	
	/**
	 * Recomputes centers of clusters as an average values of all coordinates
	 * of points assigned to a given cluster.
	 */
	private void updateTemporaryPoints() {
		updateClusterCount();
		zeroTemporaryPoints();
		
		for(double[] point : data) {
			int clusterId = (int) point[numberOfDimensions];

			for(int d  = 0; d < numberOfDimensions; d++) {
				tempPoints.get(clusterId)[d] += point[d];
			}
		}
		
		for(int i = 0; i < k; i++) {
			double[] point = tempPoints.get(i);
			
			if (clusterCount[i] > 0) {
				for(int j = 0; j < numberOfDimensions; j++) {
					point[j] = point[j] / clusterCount[i];
				}
			}
		}
		
	}	

	/**
	 * 
	 */
	private void initializeTemporaryPoints() {
		tempPoints = new ArrayList<>();
		
		for(int i = 0; i < k; i++) {
			double[] tempPoint = new double[numberOfDimensions];
			
			for (int j = 0; j < numberOfDimensions; j++) {
				tempPoint[j] = randomFromRange(j);
			}
			log.info("Center " + i + ": " + Arrays.toString(tempPoint));
			tempPoints.add(tempPoint);
		}
	}

	/**
	 *
	 */
	private void updateClusterCount() {
		clearClusterCount();
		
		for(double[] point : data) {
			int clusterId = (int) point[numberOfDimensions];
			clusterCount[clusterId] = clusterCount[clusterId] + 1;
		}
	}
	
	/**
	 * 
	 */
	private void zeroTemporaryPoints() {
		
		for(int i = 0; i < k; i++) {
			if (clusterCount[i] != 0) {
				double[] tempPoint = tempPoints.get(i);
				
				for (int j = 0; j < numberOfDimensions; j++) {
					tempPoint[j] = 0;
				}
			}
		}
	}

	/**
	 * 
	 * @param j
	 * @return
	 */
	private double randomFromRange(int j) {
		Random random = new Random();
		random.setSeed(System.nanoTime());
		double r = random.nextDouble();

		log.info("----------");
		log.info(r);
		log.info(min[j]);
		log.info(max[j]);

		double rfr = min[j] + (r * (max[j] - min[j]));

		return rfr;
	}

	public PhysicalDataSet getPhysicalDataSet() {
		return physicalDataSet;
	}


}


