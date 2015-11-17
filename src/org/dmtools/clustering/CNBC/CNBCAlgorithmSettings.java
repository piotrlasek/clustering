package org.dmtools.clustering.CNBC;

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

	static {
		try {
			MiningAlgorithm.addExtension("C-NBC");
		} catch (JDMException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public MiningAlgorithm getMiningAlgorithm() {
		// TODO Auto-generated method stub
		MiningAlgorithm ma = null;
		try {
			ma = MiningAlgorithm.valueOf("C-NBC");
		} catch (JDMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return ma;
	}

	@Override
	public VerificationReport verify() {
		// TODO Auto-generated method stub
		return null;
	}
}

