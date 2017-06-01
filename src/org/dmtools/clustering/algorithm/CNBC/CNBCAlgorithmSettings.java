package org.dmtools.clustering.algorithm.CNBC;

import org.dmtools.clustering.CDMBaseAlgorithmSettings;

import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class CNBCAlgorithmSettings extends CDMBaseAlgorithmSettings implements AlgorithmSettings {

	public static String NAME = "C-NBC";
	private int k;
	private String ic = null; // instance constraints

	static {
		try {
			MiningAlgorithm.addExtension(NAME);
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

	public String getIC() {
		return ic;
	}

	public void setIC(String ic) {
		this.ic = ic;
	}

	@Override
	public String toString() {
		return NAME + ", k = " + k;
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
}

