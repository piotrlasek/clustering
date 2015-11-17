package org.dmtools.clustering;

import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;

import org.dmtools.datamining.data.CDMAlgorithm;

/**
 * 
 * @author Piotr Lasek
 *
 */
public abstract class CDMBasicClusteringAlgorithm implements CDMAlgorithm {

	MiningObject result;
	ClusteringSettings clusteringSettings;
	PhysicalDataSet physicalDataSet;
	
	public CDMBasicClusteringAlgorithm(ClusteringSettings clusteringSettings,
			PhysicalDataSet physicalDataSet)
	{
		this.clusteringSettings = clusteringSettings;
		this.physicalDataSet = physicalDataSet;
	}
	
	@Override
	/**
	 * 
	 */
	public MiningObject run() {
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public PhysicalDataSet getPhysicalDataSet() {
		return physicalDataSet;
	}
}
