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

		timer.indexStart();

		PiCube picube = new PiCube(32);
		picube.build(data, min, max);

		HashMap<Long, PiBin> layer = picube.getLayer(6);
		Dump.toFile(Utils.layerToString(layer), "layer6.csv");

		ArrayList<PiCluster> seeds = new ArrayList<>();

		for (int i = 0; i < k; i++) {
			PiCluster randomPoint = PiCluster.random(min, max);
			seeds.add(randomPoint);
		}

		log.info("Number of bins: " + layer.entrySet().size());

		int bc = 0;

		for(Map.Entry<Long, PiBin> layerEntry : layer.entrySet()) {
			PiBin bin = layerEntry.getValue();

			for(PiCluster seed : seeds) {
				double lb = bin.lowerBound(seed);
				double ub = bin.upperBound(seed);
				 if (lb == ub) {
					log.info(bc + ":\t" + bin.getPointsCount() + "\t" +
							bin.getMaxX() + ",\t" + bin.getMinX() + ",\t" +
							bin.getMaxY() + ",\t" + bin.getMinY());
					lb = bin.lowerBound(seed);
					ub = bin.upperBound(seed);
				}
			}
			bc++;
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

	@Override
	public String getDescription() {
		return "(k=" + k + ", mi=" + maxIterations + ")";
	}
}
