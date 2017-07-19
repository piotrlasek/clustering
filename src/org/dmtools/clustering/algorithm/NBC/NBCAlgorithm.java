package org.dmtools.clustering.algorithm.NBC;

import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.algorithm.common.PlotPanel;
import org.dmtools.clustering.model.IClusteringData;
import util.Dump;

import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;


/**
 * 
 * @author Piotr Lasek
 *
 */
public class NBCAlgorithm extends CDMBasicClusteringAlgorithm {
	
	int k = 0;

	/**
	 *
	 * @param clusteringSettings
	 * @param physicalDataSet
	 */
	public NBCAlgorithm(ClusteringSettings clusteringSettings, PhysicalDataSet physicalDataSet) {
		super(clusteringSettings, physicalDataSet);

		NBCAlgorithmSettings nas = (NBCAlgorithmSettings) clusteringSettings.getAlgorithmSettings();
		k = nas.getK();
	}

	/**
	 *
	 * @return
	 */
	@Override
	public MiningObject run() {
		log.info(NBCAlgorithmSettings.NAME + " preparing data.");
		IClusteringData data = prepareData();
		log.info(NBCAlgorithmSettings.NAME + " done.");

		NBCRTree nbc = new NBCRTree();
		nbc.setK(k);

        timer.setAlgorithmName(NBCAlgorithmSettings.NAME);
        timer.setParameters("k=" + k);

		log.info(NBCAlgorithmSettings.NAME + " indexing.");
		timer.indexStart();
		nbc.setData(data);
		timer.indexEnd();
		log.info(NBCAlgorithmSettings.NAME + " done.");

		log.info(NBCAlgorithmSettings.NAME + " run.");
		timer.clusteringStart();
		nbc.run();
		timer.clusteringEnd();
		log.info(NBCAlgorithmSettings.NAME + " finished.");

		IClusteringData result = nbc.getResult();

		String logFileName = Dump.getLogFileName(NBCAlgorithmSettings.NAME,
				getPhysicalDataSet().getDescription(),getDescription() + " (clusters=" + nbc.clusterCount() + ")");

		if (dump()) {
			Dump.toFile(result.get(), logFileName + ".csv", true);
		}

		if (plot()) {
			PlotPanel.plotResult(nbc.getDataset(), max[0], max[1], null, null, null, null, logFileName + ".png", closePlot(), nbc.clusterCount());
		}

		basicMiningObject.setDescription(timer.getLog() + "\t" + nbc.clusterCount());
		return basicMiningObject;
	}

	@Override
	public String getDescription() {
		return "(k=" + k + ")";
	}
}


