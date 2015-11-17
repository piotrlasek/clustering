package pl.iidml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.datamining.Enum;
import javax.datamining.ExecutionHandle;
import javax.datamining.ExecutionStatus;
import javax.datamining.Factory;
import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.MiningFunction;
import javax.datamining.MiningObject;
import javax.datamining.MiningTask;
import javax.datamining.NamedObject;
import javax.datamining.base.Task;
import javax.datamining.clustering.Cluster;
import javax.datamining.resource.Connection;
import javax.datamining.resource.ConnectionCapability;
import javax.datamining.resource.ConnectionFactory;
import javax.datamining.resource.ConnectionMetaData;
import javax.datamining.resource.ConnectionSpec;
import javax.datamining.resource.PersistenceOption;
import javax.datamining.task.BuildTask;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;


public class FileConnection implements Connection {

	HashMap<String, MiningObject> miningObjects =
			new HashMap<String, MiningObject>();
	
	FileConnectionFactory factory;
	
	ConnectionSpec connectionSpec;
	
	
	
	public FileConnection(FileConnectionFactory factory, ConnectionSpec cs)
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
	public ExecutionStatus execute(Task task, Long timeOut) throws JDMException {
		// TODO Auto-generated method stub
		Cluster c;
		
		BuildTask bt = (BuildTask) task;
		String buildDataName = bt.getBuildDataName();
		String settingsName = bt.getBuildSettingsName();
		String outputModelName = bt.getModelName();
		
		MyClusteringModel mcm = new MyClusteringModel();
	
		FilePhysicalDataSet pds = (FilePhysicalDataSet)
				retrieveObject(buildDataName);
		
		ClusteringSettings cs = (ClusteringSettings)
				retrieveObject(settingsName);
		
		MiningAlgorithm ma = cs.getAlgorithmSettings().getMiningAlgorithm();
		
		if (ma.equals(MiningAlgorithm.kMeans))
		{
			
			mcm.clusters = new ArrayList<Cluster>();
			MyCluster mc = new MyCluster();
			
			mcm.clusters.add(mc);
			
			mc = new MyCluster();
			mcm.clusters.add(mc);
			
			
		}
				
		saveObject(outputModelName, mcm, true);
		
		System.out.println(cs.getName());
		
		return null;
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
		
		return connectionSpec.getURI();
		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxDescriptionLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxNameLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ConnectionMetaData getMetaData() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getModelNames(MiningFunction arg0, MiningAlgorithm arg1,
			Date arg2, Date arg3) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NamedObject[] getNamedObjects(PersistenceOption arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getObjectNames(NamedObject arg0) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getObjectNames(Date arg0, Date arg1, NamedObject arg2)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getObjectNames(Date arg0, Date arg1, NamedObject arg2,
			Enum arg3) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MiningAlgorithm[] getSupportedAlgorithms(MiningFunction arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MiningFunction[] getSupportedFunctions() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeObject(String arg0, NamedObject arg1) throws JDMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renameObject(String arg0, String arg1, NamedObject arg2)
			throws JDMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestDataLoad(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestDataUnload(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestModelLoad(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestModelUnload(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection retrieveModelObjects(MiningFunction arg0,
			MiningAlgorithm arg1, Date arg2, Date arg3) throws JDMException {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		
		if (overwrite && miningObjects.containsKey(miningObjectName))
		{
			miningObjects.put(miningObjectName, miningObject);
		}
		else
		{
			miningObjects.put(miningObjectName, miningObject);
		}		
	}

	@Override
	public void setDescription(String arg0, NamedObject arg1, String arg2)
			throws JDMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocale(Locale arg0) throws JDMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean supportsCapability(NamedObject arg0, PersistenceOption arg1)
			throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportsCapability(MiningFunction arg0,
			MiningAlgorithm arg1, MiningTask arg2) throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}
}
