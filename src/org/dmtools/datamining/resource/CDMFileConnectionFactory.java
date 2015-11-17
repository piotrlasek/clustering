package org.dmtools.datamining.resource;

import java.util.HashMap;

import javax.datamining.JDMException;
import javax.datamining.resource.Connection;
import javax.datamining.resource.ConnectionCapability;
import javax.datamining.resource.ConnectionFactory;
import javax.datamining.resource.ConnectionSpec;

public class CDMFileConnectionFactory implements ConnectionFactory {

	HashMap<String, CDMFileConnection> fileConnections;
	
	@Override
	public javax.datamining.resource.Connection getConnection() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.datamining.resource.Connection getConnection(ConnectionSpec cs) throws JDMException {
		// TODO Auto-generated method stub
		CDMFileConnection fc = null;
		
		try {
			fc = new CDMFileConnection(this, cs);
			
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
		return fc;
	}

	@Override
	public ConnectionSpec getConnectionSpec() {
		// TODO Auto-generated method stub
		return new CDMFileConnectionSpec();
	}

	@Override
	public boolean supportsCapability(ConnectionCapability arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Connection getConnection(javax.resource.cci.Connection arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

}
