package org.dmtools.clustering.algorithm.DBSCAN;

import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;

/**
 * @author	Dariush Eskandari, Sadra Abrishamkar, Piotr Lasek
 * @date	March, 2016
 */
public class CDBSCANAlgorithmSettings implements AlgorithmSettings {

	public static final String name = "C-DBSCAN";
	private double Eps;
	private double MinPts;

	static {
		try {
			MiningAlgorithm.addExtension(name);
		} catch (JDMException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public MiningAlgorithm getMiningAlgorithm() {
		// TODO Auto-generated method stub
		MiningAlgorithm ma = null;
		try {
			ma = MiningAlgorithm.valueOf(name);
		} catch (JDMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return ma;
	}

	@Override
	public String toString() {
		return name + ", Eps = " + Eps + ", MinPts = " + MinPts;
	}

	@Override
	public VerificationReport verify() {
		// TODO Auto-generated method stub
		return null;
	}

	public double getEps() {
		return Eps;
	}

	public void setEps(double eps) {
		Eps = eps;
	}

	public double getMinPts() {
		return MinPts;
	}

	public void setMinPts(double minPts) {
		MinPts = minPts;
	}
}

