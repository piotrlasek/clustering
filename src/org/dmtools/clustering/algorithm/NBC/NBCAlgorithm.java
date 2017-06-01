package org.dmtools.clustering.algorithm.NBC;

import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.algorithm.CNBC.MyFrame;
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

		IClusteringData data = prepareData();

		NBCRTree nbc = new NBCRTree();

		nbc.setK(k);
		nbc.setData(data);
		nbc.run();

		IClusteringData result = nbc.getResult();

		if (dump()) {
			String dumpFileName = Dump.getDumpFileName(NBCAlgorithmSettings.NAME,
					getPhysicalDataSet().getDescription(), "(k=" + k + ")");
			Dump.toFile(result.get(), dumpFileName, true);
		}

		if (plot()) {
			MyFrame.plotResult(result.get(), max[0], null, null, null, null);
		}

		return null;
	}

}


