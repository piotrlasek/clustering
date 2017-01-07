package org.dmtools.clustering.algorithm.KMeans;

import javax.datamining.MiningAlgorithm;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;

public class KMeansAlgorithmSettings implements AlgorithmSettings {

	@Override
	public MiningAlgorithm getMiningAlgorithm() {
		// TODO Auto-generated method stub
		return MiningAlgorithm.kMeans;
	}

	@Override
	public VerificationReport verify() {
		// TODO Auto-generated method stub
		return null;
	}
}

