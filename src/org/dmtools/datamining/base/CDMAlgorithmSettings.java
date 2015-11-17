package org.dmtools.datamining.base;

import javax.datamining.MiningAlgorithm;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;

public abstract class CDMAlgorithmSettings implements AlgorithmSettings {
	
	@Override
	public MiningAlgorithm getMiningAlgorithm() {
		return null;
	}

	@Override
	public VerificationReport verify() {
		return null;
	}

}
