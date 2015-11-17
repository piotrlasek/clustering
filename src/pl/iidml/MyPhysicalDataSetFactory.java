package pl.iidml;

import javax.datamining.JDMException;
import javax.datamining.data.PhysicalDataSet;
import javax.datamining.data.PhysicalDataSetCapability;
import javax.datamining.data.PhysicalDataSetFactory;

/**
 * 
 */

/**
 * @author Piotrek
 *
 */
public class MyPhysicalDataSetFactory implements PhysicalDataSetFactory {

	/* (non-Javadoc)
	 * @see javax.datamining.data.PhysicalDataSetFactory#create(java.lang.String, boolean)
	 */
	@Override
	public PhysicalDataSet create(String arg0, boolean arg1)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.data.PhysicalDataSetFactory#supportsCapability(javax.datamining.data.PhysicalDataSetCapability)
	 */
	@Override
	public boolean supportsCapability(PhysicalDataSetCapability arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}


}
