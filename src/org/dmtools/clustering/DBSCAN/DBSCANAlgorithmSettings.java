package org.dmtools.clustering.DBSCAN;

import javax.datamining.MiningAlgorithm;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;

public class DBSCANAlgorithmSettings implements AlgorithmSettings {

	@Override
	public MiningAlgorithm getMiningAlgorithm() {
		return MiningAlgorithm.kMeans;
	}

	@Override
	public VerificationReport verify() {
		return null;
	}
}
