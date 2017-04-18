package org.dmtools.clustering.algorithm.KMeans;

import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.CDMClusteringModel;
import org.dmtools.datamining.base.ScatterAdd;
import org.dmtools.datamining.data.CDMFilePhysicalDataSet;

import javax.datamining.JDMException;
import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalAttribute;
import javax.datamining.data.PhysicalDataSet;
import java.util.*;


public class KMeansAlgorithm extends CDMBasicClusteringAlgorithm {
	
	int k = 0;
	ArrayList<double[]> data;
	int numberOfDimensions; 
	double[] min = null;
	double[] max = null;
	Collection<PhysicalAttribute> attributes;
	ArrayList<double[]> tempPoints;
	int maxRuns = 20;
	int[] clusterCount;
	
	public KMeansAlgorithm(ClusteringSettings clusteringSettings,
			PhysicalDataSet physicalDataSet)
	{
		super(clusteringSettings, physicalDataSet);
		k = (int) clusteringSettings.getMinClusterCaseCount();
		System.out.println("k = " + k);
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

	@Override
	public MiningObject run() {
		CDMClusteringModel ccm = new CDMClusteringModel();

		prepareData();
		initializeTemporaryPoints();
		
		for(int i = 0; i < maxRuns; i++) {
			for(double[] point : data) {
				int indexOfClosestTempPoint = 0;
				indexOfClosestTempPoint = getIndexOfClosestCluster(point);
				point[numberOfDimensions] = indexOfClosestTempPoint; 
			}
			
			ScatterAdd sa = new ScatterAdd("K-Means - step " + (i + 1), data, tempPoints);
			sa.setSize(400, 500);
			sa.setVisible(true);
			sa.toFront();
			
			updateTemporaryPoints();
		}
		
		CDMCluster cluster = new CDMCluster();
		return ccm;
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
		return min[j] + (Math.random() * (max[j] - min[j]));
	}
	
	/**
	 * 
	 */
	public void prepareData()
	{
		ArrayList<Object[]> rawData =
				((CDMFilePhysicalDataSet) this.getPhysicalDataSet()).getData();
		data = new ArrayList<double[]>();
		
		int i = 0;
		for(Object[] rawRecord : rawData) {
			double[] record = new double[attributes.size() + 1];
			int d = 0;
			for(PhysicalAttribute attribute : attributes) {
				record[d] = new Double(rawData.get(i)[d].toString());
				if (min[d] == 0)
					min[d] = record[d];
				else							
					if (min[d] > record[d]) min[d] = record[d];
				if (max[d] < record[d]) max[d] = record[d];
				d++;					
			}
			record[d] = -1; // UNCLUSTERED
			data.add(record);
			i++;
		}
	}
}


