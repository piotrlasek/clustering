package org.dmtools.datamining.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.datamining.JDMErrorCodes;
import javax.datamining.JDMException;
import javax.datamining.resource.Connection;
import javax.datamining.resource.ConnectionCapability;
import javax.datamining.resource.ConnectionFactory;
import javax.datamining.resource.ConnectionSpec;
import java.io.File;
import java.util.HashMap;

public class CDMFileConnectionFactory implements ConnectionFactory {

	HashMap<String, CDMFileConnection> fileConnections;

	protected final static Logger log = LogManager.getLogger(CDMFileConnectionFactory.class.getSimpleName());

	@Override
	public javax.datamining.resource.Connection getConnection() throws JDMException {
		return null;
	}

	@Override
	public javax.datamining.resource.Connection getConnection(ConnectionSpec cs) throws JDMException {
		CDMFileConnection fc = new CDMFileConnection(this, cs);

		if (cs.getURI().contains("[CUSTOM]")) {
			log.warn("Dataset was not defined correctly! " +
					"Continuing in case the algorithm can load data.");
		} else {
			File f = new File(cs.getURI());

			if (!f.exists() || f.isDirectory()) {
				throw new JDMException(JDMErrorCodes.JDM_CONNECTION_FAILURE,
						"File " + cs.getURI() + " does not exist!");
			}
		}

		return fc;
	}

	@Override
	public ConnectionSpec getConnectionSpec() {
		return new CDMFileConnectionSpec();
	}

	@Override
	public boolean supportsCapability(ConnectionCapability arg0)
			throws JDMException {
		return false;
	}

	@Override
	public Connection getConnection(javax.resource.cci.Connection arg0)
			throws JDMException {
		return null;
	}

}
