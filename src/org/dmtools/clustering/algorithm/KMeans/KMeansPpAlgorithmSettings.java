package org.dmtools.clustering.algorithm.KMeans;

import org.dmtools.clustering.CDMBaseAlgorithmSettings;

import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;

public class KMeansPpAlgorithmSettings extends CDMBaseAlgorithmSettings implements AlgorithmSettings {

	public static final String NAME = "k-Means++";
	private int k;
	private int maxIterations;

	@Override
	public MiningAlgorithm getMiningAlgorithm() {
		try {
			return MiningAlgorithm.valueOf(NAME);
		} catch (JDMException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 */
	static {
		try {
			MiningAlgorithm.addExtension(NAME);
		} catch (JDMException e) {
			e.printStackTrace();
		}
	}

	@Override
	public VerificationReport verify() {
		return null;
	}

	public void setK(int k) {
		this.k = k;
	}

	public int getK() {
		return k;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public int getMaxIterations() {
		return maxIterations;
	}
}

