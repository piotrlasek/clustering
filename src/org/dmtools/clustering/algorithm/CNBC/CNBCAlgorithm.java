package org.dmtools.clustering.algorithm.CNBC;

import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
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
		timer.indexStart();
		nbc.setData(data);
		timer.indexEnd();
		nbc.setConstraints(ic);
		timer.clusteringStart();
		nbc.run();
		timer.clusteringEnd();

		timer.merge(nbc.getInternalTimer());

		IClusteringData cd = nbc.getResult();

		ArrayList<Point> result = nbc.getDataset();

		if (dump()) {
		    String dumpFileName = Dump.getDumpFileName(CNBCAlgorithmSettings.NAME,
					getPhysicalDataSet().getDescription(), "(k=" + k + ")");
			Dump.toFile(cd.get(), dumpFileName, true);
		}

		if (plot()) {
			InstanceConstraints constraints = nbc.getConstraints();
			MyFrame2.plotResult(result, max[0], constraints, null, null, null);
		}

		basicMiningObject.setDescription(timer.getLog());
		return basicMiningObject;
	}

}


