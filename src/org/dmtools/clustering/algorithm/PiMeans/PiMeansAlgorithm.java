package org.dmtools.clustering.algorithm.PiMeans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.algorithm.piKMeans.PiKMeansAlgorithmSettings;
import org.dmtools.clustering.old.ClusteringTimer;
import util.Dump;

import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: Nasim Razavi
 * Date: September 8, 2017
 */
public class PiMeansAlgorithm extends CDMBasicClusteringAlgorithm {

	// zle liczone sa maxx i maxy w PiBin

	int k;
	int maxIterations;
	int startingStratum;
	int depth;

	ClusteringTimer timer = new ClusteringTimer(PiMeansAlgorithmSettings.NAME);

	protected final static Logger log = LogManager.getLogger(PiMeansAlgorithm.class.getSimpleName());

	/**
	 * @throws IOException
	 */
	public PiMeansAlgorithm(ClusteringSettings clusteringSettings,
                            PhysicalDataSet physicalDataSet) {
		super(clusteringSettings, physicalDataSet);

		PiMeansAlgorithmSettings pkmas =
				(PiMeansAlgorithmSettings) clusteringSettings.getAlgorithmSettings();

		k = pkmas.getK();
		maxIterations = pkmas.getMaxIterations();
		startingStratum = pkmas.getStarting();
		depth = pkmas.getDepth();
	}



	/**
	 * @return
	 */
	public MiningObject run() {
		log.info("Initializing pyramid...");
        prepareData();
        normalizeData(4);

		timer.indexStart();

		PiCube picube = new PiCube(32);
		picube.build(data, min, max);

		HashMap<Long, PiBin> layer = picube.getLayer(10);
		Dump.toFile(Utils.layerToString(layer), "layer10.csv");

		ArrayList<PiCluster> seeds = new ArrayList<>();

		for (int i = 0; i < k; i++) {
			PiCluster randomPoint = PiCluster.random(min, max);
			seeds.add(randomPoint);
		}

		for(Map.Entry<Long, PiBin> layerEntry : layer.entrySet()) {
			PiBin bin = layerEntry.getValue();
			log.info(bin.getZoo());

			for(PiCluster seed : seeds) {
				double lb = bin.lowerBound(seed);
				double ub = bin.upperBound(seed);
				log.info(lb + ", " + ub);
			}
			log.info("---");
		}

		Set<Long> zoos = layer.keySet();

	/*	for(Long z : zoos) {
			PiBin b = layer.get(z);
			PiCluster c = new PiCluster(new double[]{0,0});
			double ub = b.upperBound(c);
			log.info(b.getX() + ", " + b.getY() + " -> " + ub);
		} */

		timer.indexEnd();

		log.info("Clustering started...");

		timer.clusteringStart();

		timer.clusteringEnd();

		// Dumping results to a file(s) and/or plotting results.
		if (dump()) {
			String logFileName = Dump.getLogFileName(PiKMeansAlgorithmSettings.NAME,
					getPhysicalDataSet().getDescription(), getDescription() );

			log.info("Writing results to " + logFileName);
			// writeResults();
			Dump.toFile(data, logFileName + ".csv", true);
		}

		if (plot()) {
			log.info("Plotting results...");
		}

		basicMiningObject.setDescription(timer.getLog());
		return basicMiningObject;
	}

	/**
	 * "Normalizes" data in an ugly way.
	 */
	public void normalizeData(long maxSize)
	{
		double[] min = getMin();
		double[] max = getMax();

		double[] nMin = new double[2];
		double[] nMax = new double[2];
		nMin[0] = nMax[0] = nMin[1] = nMax[1] = 0;

		for(double[] record : data) {
			record[0] -= min[0];
			record[0] *= 1000000;
			record[1] -= min[1];
			record[1] *= 1000000;
			//log.info(Arrays.toString(record));
		}

		for(double[] record : data) {
		    for (int i = 0; i < 2; i++) {
				if (nMin[i] > record[i]) nMin[i] = record[i];
				if (nMax[i] < record[i]) nMax[i] = record[i];
			}
		}

		this.min = nMin;
		this.max = nMax;
	}

	@Override
	public String getDescription() {
		return "(k=" + k + ", mi=" + maxIterations + ")";
	}
}
