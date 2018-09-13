package org.dmtools.datamining.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMClusteringModel;
import org.dmtools.clustering.algorithm.CDBSCAN.CDBSCANAlgorithm;
import org.dmtools.clustering.algorithm.CDBSCAN.CDBSCANAlgorithmSettings;
import org.dmtools.clustering.algorithm.CNBC.CNBCAlgorithm;
import org.dmtools.clustering.algorithm.CNBC.CNBCAlgorithmSettings;
import org.dmtools.clustering.algorithm.DBSCAN.DBSCANAlgorithm;
import org.dmtools.clustering.algorithm.DBSCAN.DBSCANAlgorithmSettings;
import org.dmtools.clustering.algorithm.DBSCAN.DbScanNetTraffic;
import org.dmtools.clustering.algorithm.DBSCAN.DbScanSlicer;
import org.dmtools.clustering.algorithm.KMeans.DM.DM_KMeansAlgorithm;
import org.dmtools.clustering.algorithm.KMeans.KMeansAlgorithm;
import org.dmtools.clustering.algorithm.KMeans.KMeansAlgorithmSettings;
import org.dmtools.clustering.algorithm.KMeans.KMeansPpAlgorithm;
import org.dmtools.clustering.algorithm.KMeans.KMeansPpAlgorithmSettings;
import org.dmtools.clustering.algorithm.NBC.DM.NBCDMAlgorithm;
import org.dmtools.clustering.algorithm.NBC.NBCAlgorithm;
import org.dmtools.clustering.algorithm.NBC.NBCAlgorithmSettings;
import org.dmtools.clustering.algorithm.PiMeans.PiMeansAlgorithm;
import org.dmtools.clustering.algorithm.PiMeans.PiMeansAlgorithmSettings;
import org.dmtools.clustering.algorithm.piKMeans.PiKMeansAlgorithm;
import org.dmtools.clustering.algorithm.piKMeans.PiKMeansAlgorithmSettings;
import org.dmtools.datamining.data.CDMFilePhysicalDataSet;

import javax.datamining.Enum;
import javax.datamining.*;
import javax.datamining.base.AlgorithmSettings;
import javax.datamining.base.Task;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.resource.Connection;
import javax.datamining.resource.ConnectionMetaData;
import javax.datamining.resource.ConnectionSpec;
import javax.datamining.resource.PersistenceOption;
import javax.datamining.task.BuildTask;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class CDMFileConnection implements Connection {

	HashMap<String, MiningObject> miningObjects =
			new HashMap<String, MiningObject>();
	
	CDMFileConnectionFactory factory;
	
	ConnectionSpec connectionSpec;

	protected final static Logger log = LogManager.getLogger(CDMFileConnection.class.getSimpleName());

	/**
	 * 
	 * @param factory
	 * @param cs
	 */
	public CDMFileConnection(CDMFileConnectionFactory factory, ConnectionSpec cs)
	{
		this.factory = factory;
		this.connectionSpec = cs;
	}

	@Override
	public void close() throws JDMException {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean doesObjectExist(String arg0, NamedObject arg1)
			throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ExecutionHandle execute(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionStatus execute(Task task, Long timeOut)
			throws JDMException {
		
		BuildTask bt = (BuildTask) task;
		
		String buildDataName = bt.getBuildDataName();		
		String settingsName = bt.getBuildSettingsName();
		String outputModelName = bt.getModelName();
		
		CDMClusteringModel mcm = new CDMClusteringModel();
		
		CDMFilePhysicalDataSet pds = (CDMFilePhysicalDataSet) 
				retrieveObject(buildDataName);

		if (!pds.getDescription().contains("[CUSTOM]")) {
			pds.setSeparator(",");
			pds.readData(this);
		} else {
			pds.setDescription(pds.getDescription().replace("[CUSTOM]", ""));
			log.warn("Data set not defined!");
		}

		// Get mining algorithm
		ClusteringSettings cs = (ClusteringSettings)
				retrieveObject(settingsName);
		
		cs.setLogicalDataName(buildDataName);
		
		AlgorithmSettings as = cs.getAlgorithmSettings();
		
		MiningAlgorithm ma = as.getMiningAlgorithm();

		CDMExecutionStatus executionStatus = new CDMExecutionStatus();
		MiningObject miningObject;

        if (ma.equals(MiningAlgorithm.valueOf(KMeansPpAlgorithmSettings.NAME))) {
			KMeansPpAlgorithm pkma = new KMeansPpAlgorithm(cs, pds);
			miningObject = pkma.run();
		} else if (ma.equals(MiningAlgorithm.valueOf(PiMeansAlgorithmSettings.NAME))) {
			PiMeansAlgorithm pkma = new PiMeansAlgorithm(cs, pds);
			miningObject = pkma.run();
		} else if (ma.equals(MiningAlgorithm.valueOf(PiKMeansAlgorithmSettings.NAME))) {
			PiKMeansAlgorithm pkma = new PiKMeansAlgorithm(cs, pds);
			miningObject = pkma.run();
		} else if (ma.equals(MiningAlgorithm.valueOf(KMeansAlgorithmSettings.NAME))) {
			// kMeans
			KMeansAlgorithm kma = new KMeansAlgorithm(cs, pds); 
			miningObject = kma.run();
		} else if (ma.equals(MiningAlgorithm.valueOf(NBCAlgorithmSettings.NAME))) {
			// NBC
			NBCAlgorithm kma = new NBCAlgorithm(cs, pds); 
			miningObject = kma.run();
		} else if (ma.equals(MiningAlgorithm.valueOf(CNBCAlgorithmSettings.NAME))) {
			// NBC
			CNBCAlgorithm kma = new CNBCAlgorithm(cs, pds); 
			miningObject = kma.run();
		} else if (ma.equals(MiningAlgorithm.valueOf(DBSCANAlgorithmSettings.NAME))) {
			// NBC
			DBSCANAlgorithm kma = new DBSCANAlgorithm(cs, pds);
			miningObject = kma.run();
		} else if (ma.equals(MiningAlgorithm.valueOf(CDBSCANAlgorithmSettings.NAME))) {
			// NBC
			CDBSCANAlgorithm kma = new CDBSCANAlgorithm(cs, pds);
			miningObject = kma.run();
		} else if (ma.equals(MiningAlgorithm.valueOf("DM-NBC"))) {
			// DM-NBC
			NBCDMAlgorithm kma = new NBCDMAlgorithm(cs, pds); 
			miningObject = kma.run();
		}
		else if (ma.equals(MiningAlgorithm.valueOf("K-Means_DM"))) {
			// K-Means_DM
			DM_KMeansAlgorithm kma = new DM_KMeansAlgorithm(cs, pds); 
			miningObject = kma.run();
		} else if (ma.equals(MiningAlgorithm.valueOf("DBSCAN-NET"))) {
			// DBSCAN-NET
			DbScanNetTraffic dbScanNetTraffic = new DbScanNetTraffic(cs,  pds);
			miningObject = dbScanNetTraffic.run();
		}  else if (ma.equals(MiningAlgorithm.valueOf("DBSCAN-SLI"))) {
			// DBSCAN-NET
			DbScanSlicer mbScanSlicer = new DbScanSlicer(cs,  pds);
			miningObject = mbScanSlicer.run();
		} else {
			throw new JDMException(0, "Not supported.");
		}

		saveObject("clustering_result", miningObject, true);

		executionStatus.setDescription(miningObject.getDescription());

		saveObject(outputModelName, mcm, true);

		return executionStatus;
	}

	@Override
	public ConnectionSpec getConnectionSpec() {		
		return connectionSpec;
	}

	@Override
	public Date getCreationDate(String arg0, NamedObject arg1)
			throws JDMException {
		Date date = null;
	    Path p = Paths.get(connectionSpec.getURI());
	    BasicFileAttributes view;
		try {
			view = Files.getFileAttributeView(p, BasicFileAttributeView.class)
			          .readAttributes();
			FileTime ft = view.creationTime();
			date = new Date(ft.toMillis());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return date;
	}

	@Override
	public String getDescription(String arg0, NamedObject arg1)
			throws JDMException {

		
		return null;
	}

	@Override
	public ExecutionHandle[] getExecutionHandles(String arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Factory getFactory(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		return factory;
	}

	@Override
	public ExecutionHandle getLastExecutionHandle(String arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getLoadedData() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getLoadedModels() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public int getMaxDescriptionLength() {
		return 0;
	}

	@Override
	public int getMaxNameLength() {
		return 0;
	}

	@Override
	public ConnectionMetaData getMetaData() throws JDMException {
		ConnectionMetaData cmd = null;
		
		return cmd;
	}

	@Override
	public Collection getModelNames(MiningFunction arg0, MiningAlgorithm arg1,
			Date arg2, Date arg3) throws JDMException {
		return null;
	}

	@Override
	public NamedObject[] getNamedObjects(PersistenceOption arg0)
			throws JDMException {
		return null;
	}

	@Override
	public Collection getObjectNames(NamedObject arg0) throws JDMException {
		return null;
	}

	@Override
	public Collection getObjectNames(Date arg0, Date arg1, NamedObject arg2)
			throws JDMException {
		return null;
	}

	@Override
	public Collection getObjectNames(Date arg0, Date arg1, NamedObject arg2,
			Enum arg3) throws JDMException {
		return null;
	}

	@Override
	public MiningAlgorithm[] getSupportedAlgorithms(MiningFunction arg0)
			throws JDMException {
		return null;
	}

	@Override
	public MiningFunction[] getSupportedFunctions() throws JDMException {
		return null;
	}

	@Override
	public void removeObject(String arg0, NamedObject arg1) throws JDMException {
		
	}

	@Override
	public void renameObject(String arg0, String arg1, NamedObject arg2)
			throws JDMException {
		
	}

	@Override
	public void requestDataLoad(String arg0) throws JDMException {
		
	}

	@Override
	public void requestDataUnload(String arg0) throws JDMException {
		
	}

	@Override
	public void requestModelLoad(String arg0) throws JDMException {
		
	}

	@Override
	public void requestModelUnload(String arg0) throws JDMException {
		
	}

	@Override
	public Collection retrieveModelObjects(MiningFunction arg0,
			MiningAlgorithm arg1, Date arg2, Date arg3) throws JDMException {
		return null;
	}

	@Override
	public MiningObject retrieveObject(String miningObjectName)
			throws JDMException {
		return miningObjects.get(miningObjectName);
	}

	@Override
	public MiningObject retrieveObject(String arg0, NamedObject arg1)
			throws JDMException {
		return null;
	}

	@Override
	public Collection retrieveObjects(Date arg0, Date arg1, NamedObject arg2)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection retrieveObjects(Date arg0, Date arg1, NamedObject arg2,
			Enum arg3) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveObject(String miningObjectName, MiningObject miningObject,
			boolean overwrite)
			throws JDMException {
		
		if (overwrite && miningObjects.containsKey(miningObjectName)) {
			miningObjects.put(miningObjectName, miningObject);
		}
		else {
			miningObjects.put(miningObjectName, miningObject);
		}		
	}

	@Override
	public void setDescription(String arg0, NamedObject arg1, String arg2)
			throws JDMException {
		
	}

	@Override
	public void setLocale(Locale arg0) throws JDMException {
		
	}

	@Override
	public boolean supportsCapability(NamedObject arg0, PersistenceOption arg1)
			throws JDMException {
		
		throw new JDMException(0, "Function not supported");
	}

	@Override
	public boolean supportsCapability(MiningFunction arg0,
			MiningAlgorithm arg1, MiningTask arg2) throws JDMException {
		return false;
	}
}
