package pl.iidml;

import java.util.HashMap;

import javax.datamining.JDMException;
import javax.datamining.resource.Connection;
import javax.datamining.resource.ConnectionCapability;
import javax.datamining.resource.ConnectionFactory;
import javax.datamining.resource.ConnectionSpec;


public class FileConnectionFactory implements ConnectionFactory {

	HashMap<String, FileConnection> fileConnections;
	
	@Override
	public Connection getConnection() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection getConnection(ConnectionSpec cs) throws JDMException {
		// TODO Auto-generated method stub
		FileConnection fc = null;
		
		try
		{
			fc = new FileConnection(this, cs);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return fc;
	}

	@Override
	public Connection getConnection(javax.resource.cci.Connection arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConnectionSpec getConnectionSpec() {
		// TODO Auto-generated method stub
		return new FileConnectionSpec();
	}

	@Override
	public boolean supportsCapability(ConnectionCapability arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}

}
