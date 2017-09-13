package org.dmtools.clustering.algorithm.KMeans;

import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.old.ClusteringTimer;

import javax.datamining.JDMException;
import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Author: Piotr Lasek
 * Date: November 17, 2015
 */
public class KMeansAlgorithm extends CDMBasicClusteringAlgorithm {

	// parameters
	int k;
	int maxIterations;

	ArrayList<double[]> tempPoints;
	int[] clusterCount;

	ClusteringTimer timer = new ClusteringTimer(getName());

	/**
	 * @return
	 */
	public String getName() {
		return KMeansAlgorithmSettings.NAME;
	}

	/**
	 * @param clusteringSettings
	 * @param physicalDataSet
	 */
	public KMeansAlgorithm(ClusteringSettings clusteringSettings,
						   PhysicalDataSet physicalDataSet) {
		super(clusteringSettings, physicalDataSet);
		KMeansAlgorithmSettings kmas = (KMeansAlgorithmSettings) clusteringSettings.getAlgorithmSettings();

		this.physicalDataSet = physicalDataSet;

		k = kmas.getK();
		maxIterations = kmas.getMaxIterations();

		clusterCount = new int[k];

		// get attributes
		try {
			attributes = physicalDataSet.getAttributes();
		} catch (JDMException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	public MiningObject run() {

		timer.setParameters("(k=" + k + ", mi=" + maxIterations + ")");
		timer.indexStart();
		prepareData();
		initializeTemporaryPoints();
		timer.indexEnd();

		timer.clusteringStart();
		for (int i = 0; i < maxIterations; i++) {
			// log.info("Iteration: " + i);
			for (double[] point : data) {
				int indexOfClosestTempPoint = 0;
				indexOfClosestTempPoint = getIndexOfClosestCluster(point);
				point[numberOfDimensions] = indexOfClosestTempPoint;
			}

			//log.info("Updating cluster centers.");
			updateTemporaryPoints();
		}
		timer.clusteringEnd();

		/*if (plot()) {
			log.info("Printing the final result.");
			ScatterAdd sa = new ScatterAdd("k-Means", data, tempPoints);
			sa.setSize(400, 500);
			sa.setVisible(true);
			sa.toFront();
		}*/

		basicMiningObject.setDescription(timer.getLog());
		return basicMiningObject;
	}

	@Override
	public String getDescription() {
		return "(k = " + k + ")";
	}

	/**
	 * @param point
	 * @return
	 */
	private int getIndexOfClosestCluster(double[] point) {
		ArrayList<Double> distances = new ArrayList();
		int indexOfClosestTempPoint = 0;

		for (int i = 0; i < k; i++) {
			double[] ti = tempPoints.get(i);
			distances.add(dist(point, ti));
		}

		Double minDist = Collections.min(distances);
		indexOfClosestTempPoint = distances.indexOf(minDist);

		return indexOfClosestTempPoint;
	}

	/**
	 * @param a
	 * @param b
	 * @return
	 */
	public double dist(double[] a, double[] b) {
		double temp = 0;

		for (int i = 0; i < Math.min(a.length, b.length); i++) {
			temp += Math.pow(a[i] - b[i], 2);
		}

		return Math.sqrt(temp);
	}

	/**
	 * Clears information about number of objects assigned to a cluster.
	 */
	private void clearClusterCount() {
		for (int i = 0; i < k; i++) {
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

		for (double[] point : data) {
			int clusterId = (int) point[numberOfDimensions];

			for (int d = 0; d < numberOfDimensions; d++) {
				tempPoints.get(clusterId)[d] += point[d];
			}
		}

		for (int i = 0; i < k; i++) {
			double[] point = tempPoints.get(i);

			if (clusterCount[i] > 0) {
				for (int j = 0; j < numberOfDimensions; j++) {
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

		for (int i = 0; i < k; i++) {
			double[] tempPoint = new double[numberOfDimensions];

			for (int j = 0; j < numberOfDimensions; j++) {
				tempPoint[j] = randomFromRange(j);
			}
			tempPoints.add(tempPoint);
		}
	}

	/**
	 *
	 */
	private void updateClusterCount() {
		clearClusterCount();

		for (double[] point : data) {
			int clusterId = (int) point[numberOfDimensions];
			clusterCount[clusterId] = clusterCount[clusterId] + 1;
		}
	}

	/**
	 *
	 */
	private void zeroTemporaryPoints() {
		for (int i = 0; i < k; i++) {
			if (clusterCount[i] != 0) {
				double[] tempPoint = tempPoints.get(i);

				for (int j = 0; j < numberOfDimensions; j++) {
					tempPoint[j] = 0;
				}
			}
		}
	}

	/**
	 * @param j
	 * @return
	 */
	private double randomFromRange(int j) {
		double rfr = min[j] + (Math.random() * (max[j] - min[j]));
		return rfr;
	}

	public PhysicalDataSet getPhysicalDataSet() {
		return physicalDataSet;
	}


}


