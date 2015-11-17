package org.dmtools.datamining.data;

import javax.datamining.JDMException;
import javax.datamining.data.PhysicalDataSet;
import javax.datamining.data.PhysicalDataSetCapability;
import javax.datamining.data.PhysicalDataSetFactory;


/**
 * @author Piotr Lasek
 *
 */
public class CDMFilePhysicalDataSetFactory implements PhysicalDataSetFactory {

	String fileName;
	
	/* (non-Javadoc)
	 * @see javax.datamining.data.PhysicalDataSetFactory#create(java.lang.String, boolean)
	 */
	@Override
	public PhysicalDataSet create(String uri, boolean arg1)
			throws JDMException {
		CDMFilePhysicalDataSet fpds = new CDMFilePhysicalDataSet();
		fpds.setDescription(uri);
		return fpds;
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
