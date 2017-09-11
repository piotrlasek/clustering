package org.dmtools.clustering.algorithm.KMeans;

import org.dmtools.clustering.CDMBaseAlgorithmSettings;

import javax.datamining.MiningAlgorithm;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;

public class KMeansAlgorithmSettings extends CDMBaseAlgorithmSettings implements AlgorithmSettings {

	public static String NAME = "k-Means";
	private int k;
	private int maxIterations;

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

