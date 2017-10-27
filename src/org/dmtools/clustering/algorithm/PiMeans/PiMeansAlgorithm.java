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
import java.util.HashMap;

/**
 * Author: Nasim Razavi
 * Date: September 8, 2017
 */
public class PiMeansAlgorithm extends CDMBasicClusteringAlgorithm {

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
		picube.build(data);

		HashMap<Long, PiBin> layer = picube.getLayer(10);

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
	 *
	 */
	public void normalizeData(long maxSize) {
		double[] min = getMin();
		double[] max = getMax();

		for(double[] record : data) {
			record[0] -= min[0];
			record[0] *= 1000000;
			record[1] -= min[1];
			record[1] *= 1000000;
		}
	}

	@Override
	public String getDescription() {
		return "(k=" + k + ", mi=" + maxIterations + ")";
	}
}
