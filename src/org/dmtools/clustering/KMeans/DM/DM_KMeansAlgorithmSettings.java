package org.dmtools.clustering.KMeans.DM;

import javax.datamining.MiningAlgorithm;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;

public class DM_KMeansAlgorithmSettings implements AlgorithmSettings {

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

