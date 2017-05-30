package org.dmtools.clustering.algorithm.CNBC;

import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class CNBCAlgorithmSettings implements AlgorithmSettings {

	private int k;
	private static String name = "C-NBC";

	static {
		try {
			MiningAlgorithm.addExtension(name);
		} catch (JDMException e) {
			e.printStackTrace();
		}
	}

	public void setK(int k) {
		this.k = k;
	}

	public int getK() {
		return k;
	}

	@Override
	public String toString() {
		return name + ", k = " + k;
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
}

