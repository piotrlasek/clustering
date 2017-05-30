package org.dmtools.clustering.algorithm.DBSCAN;

import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;

public class DBSCANAlgorithmSettings implements AlgorithmSettings {

	private static String name = DBSCANAlgorithm.NAME;
	private int minPts;
	private double eps;

	static {
		try {
			MiningAlgorithm.addExtension(name);
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
			return MiningAlgorithm.valueOf(name);
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
