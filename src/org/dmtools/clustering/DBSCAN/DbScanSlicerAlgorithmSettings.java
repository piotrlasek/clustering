package org.dmtools.clustering.DBSCAN;

import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class DbScanSlicerAlgorithmSettings implements AlgorithmSettings {

	static {
		try {
			MiningAlgorithm.addExtension("DBSCAN-SLI");
		} catch (JDMException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public MiningAlgorithm getMiningAlgorithm() {
		// TODO Auto-generated method stub
		MiningAlgorithm ma = null;
		try {
			ma = MiningAlgorithm.valueOf("DBSCAN-SLI");
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

