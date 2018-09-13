package org.dmtools.clustering.algorithm.KMeans;

import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.model.IClusteringData;
import org.dmtools.clustering.old.ClusteringTimer;
import org.dmtools.datamining.base.ScatterAdd;

import javax.datamining.JDMException;
import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;
import java.util.ArrayList;
import java.util.Collections;

import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Author: Piotr Lasek
 * Date: November 17, 2015
 */
public class KMeansPpAlgorithm extends CDMBasicClusteringAlgorithm {

	// parameters
	int k;
	int maxIterations;

	Instances instances;

	ClusteringTimer timer = new ClusteringTimer(getName());

	/**
	 * @return
	 */
	public String getName() {
		return KMeansPpAlgorithmSettings.NAME;
	}

	/**
	 * @param clusteringSettings
	 * @param physicalDataSet
	 */
	public KMeansPpAlgorithm(ClusteringSettings clusteringSettings,
                             PhysicalDataSet physicalDataSet) {
		super(clusteringSettings, physicalDataSet);

		KMeansPpAlgorithmSettings kmas = (KMeansPpAlgorithmSettings) clusteringSettings.getAlgorithmSettings();

		this.physicalDataSet = physicalDataSet;

		k = kmas.getK();

		maxIterations = kmas.getMaxIterations();

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
		SimpleKMeans kmeans = new SimpleKMeans();

		timer.setParameters("(k=" + k + ", mi=" + maxIterations + ")");

        try {
			timer.indexStart();
			prepareData();
			kmeans.setMaxIterations(k);
            kmeans.setNumClusters(maxIterations);
            timer.indexEnd();

            timer.clusteringStart();
			kmeans.buildClusterer(instances);

			// TODO: invoke cluster instance

			timer.clusteringEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}

		basicMiningObject.setDescription(timer.getLog());
		return basicMiningObject;
	}

	@Override
	public IClusteringData prepareData() {
		IClusteringData idata = super.prepareData();

		ArrayList<Attribute> attributes = new ArrayList();
		attributes.add(new Attribute("x"));
		attributes.add(new Attribute("y"));

		instances = new Instances("KMeansPPInstances", attributes, 0);

		for (double[] r : data) {
			Instance i = new DenseInstance(2);
			i.setValue(0, r[0]);
			i.setValue(1, r[1]);
			instances.add(i);
		}

		return idata;
	}

	@Override
	public String getDescription() {
		return "(k = " + k + ")";
	}

	public PhysicalDataSet getPhysicalDataSet() {
		return physicalDataSet;
	}
}


