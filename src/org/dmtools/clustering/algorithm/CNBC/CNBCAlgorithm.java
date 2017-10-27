package org.dmtools.clustering.algorithm.CNBC;

import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.algorithm.common.PlotPanel;
import org.dmtools.clustering.model.IClusteringData;
import spatialindex.spatialindex.Point;
import util.Dump;

import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;
import java.util.ArrayList;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class CNBCAlgorithm extends CDMBasicClusteringAlgorithm {

	int k = 0;
	String ic = null;

	/**
	 *
	 * @param clusteringSettings
	 * @param physicalDataSet
	 */
	public CNBCAlgorithm(ClusteringSettings clusteringSettings, PhysicalDataSet physicalDataSet) {
		super(clusteringSettings, physicalDataSet);
		
		CNBCAlgorithmSettings cnas = (CNBCAlgorithmSettings) clusteringSettings.getAlgorithmSettings();
		k = cnas.getK();
		ic = cnas.getIC();
	}

	/**
	 *
	 * @return
	 */
	@Override
	public MiningObject run() {

		IClusteringData data = prepareData();

		CDNBCRTree nbc = new CDNBCRTree();

		timer.setAlgorithmName(CNBCAlgorithmSettings.NAME);
		timer.setParameters("k=" + k);

		nbc.setK(k);
		log.info(CNBCAlgorithmSettings.NAME + " indexing.");
		timer.indexStart();
		nbc.setData(data);
		timer.indexEnd();
		log.info(CNBCAlgorithmSettings.NAME + " done.");
		log.info(CNBCAlgorithmSettings.NAME + " setting constraints.");
		nbc.setConstraints(ic);
		log.info(CNBCAlgorithmSettings.NAME + " done.");
		log.info(CNBCAlgorithmSettings.NAME + " clustering.");
		timer.clusteringStart();
		nbc.run();
		timer.clusteringEnd();
		log.info(CNBCAlgorithmSettings.NAME + " done.");

		timer.merge(nbc.getInternalTimer());

		IClusteringData cd = nbc.getResult();

		ArrayList<Point> result = nbc.getDataset();

		String logFileName = Dump.getLogFileName(CNBCAlgorithmSettings.NAME,
				getPhysicalDataSet().getDescription(), getDescription() + " (clusters=" + nbc.clusterCount() + ")");

		if (dump()) {
			Dump.toFile(cd.get(), logFileName + ".csv", true);
		}

		if (plot()) {
			InstanceConstraints constraints = nbc.getConstraints();
			PlotPanel.plotResult(result, max[0], max[1], constraints, null, null, null, logFileName + ".png", closePlot(), nbc.clusterCount());
		}

		basicMiningObject.setDescription(timer.getLog() + "\t" + nbc.clusterCount());
		return basicMiningObject;
	}

	@Override
	public String getDescription() {
		return "(k=" + k + ")";
	}


}


