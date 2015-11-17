package org.dmtools.datamining.data;

import javax.datamining.MiningObject;

public interface CDMAlgorithm {
	
	/**
	 * This is a main function of any mining algorithm. 
	 * 
	 * @return Mining object, for example a model constructed during
	 * a clustering process.
	 */
	public MiningObject run();
	
}
