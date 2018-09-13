package org.dmtools.clustering.algorithm.DBSCAN;

import org.dmtools.clustering.CDMBaseAlgorithmSettings;

import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;

public class DBSCANAlgorithmSettings extends CDMBaseAlgorithmSettings implements AlgorithmSettings {

	public static final String NAME = "DBSCAN";
	private int minPts;
	private double eps;

	static {
		try {
			MiningAlgorithm.addExtension(NAME);
		} catch (JDMException e) {
			e.printStackTrace();
		}
	}

	public int getMinPts() {
		return minPts;
	}

	public double getEps() {
		return eps;
	}

	@Override
	public VerificationReport verify() {
		return null;
	}

	@Override
	public MiningAlgorithm getMiningAlgorithm() {
		try {
			return MiningAlgorithm.valueOf(NAME);
		} catch (JDMException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setMinPts(int minPts) {
		this.minPts = minPts;
	}

	public void setEps(double eps) {
		this.eps = eps;
	}
}
