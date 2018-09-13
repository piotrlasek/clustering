package org.dmtools.clustering.algorithm.NBC;


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
public class NBCAlgorithmSettings extends CDMBaseAlgorithmSettings implements AlgorithmSettings {

	private int k;
	public static final String NAME = "NBC";

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

