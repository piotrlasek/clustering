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

	public static final String C_DBSCAN = "C-DBSCAN";

	static {
		try {
			MiningAlgorithm.addExtension(C_DBSCAN);
		} catch (JDMException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public MiningAlgorithm getMiningAlgorithm() {
		// TODO Auto-generated method stub
		MiningAlgorithm ma = null;
		try {
			ma = MiningAlgorithm.valueOf(C_DBSCAN);
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

