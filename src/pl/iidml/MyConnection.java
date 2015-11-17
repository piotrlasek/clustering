package pl.iidml;

import java.util.Collection;
import java.util.Date;
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
import javax.datamining.resource.Connection;
import javax.datamining.resource.ConnectionMetaData;
import javax.datamining.resource.ConnectionSpec;
import javax.datamining.resource.PersistenceOption;

/**
 * 
 */

/**
 * @author Piotrek
 *
 */
public class MyConnection implements Connection {

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#close()
	 */
	@Override
	public void close() throws JDMException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#doesObjectExist(java.lang.String, javax.datamining.NamedObject)
	 */
	@Override
	public boolean doesObjectExist(String arg0, NamedObject arg1)
			throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#execute(java.lang.String)
	 */
	@Override
	public ExecutionHandle execute(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#execute(javax.datamining.base.Task, java.lang.Long)
	 */
	@Override
	public ExecutionStatus execute(Task arg0, Long arg1) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getConnectionSpec()
	 */
	@Override
	public ConnectionSpec getConnectionSpec() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getCreationDate(java.lang.String, javax.datamining.NamedObject)
	 */
	@Override
	public Date getCreationDate(String arg0, NamedObject arg1)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getDescription(java.lang.String, javax.datamining.NamedObject)
	 */
	@Override
	public String getDescription(String arg0, NamedObject arg1)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getExecutionHandles(java.lang.String)
	 */
	@Override
	public ExecutionHandle[] getExecutionHandles(String arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getFactory(java.lang.String)
	 */
	@Override
	public Factory getFactory(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getLastExecutionHandle(java.lang.String)
	 */
	@Override
	public ExecutionHandle getLastExecutionHandle(String arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getLoadedData()
	 */
	@Override
	public String[] getLoadedData() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getLoadedModels()
	 */
	@Override
	public String[] getLoadedModels() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getLocale()
	 */
	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getMaxDescriptionLength()
	 */
	@Override
	public int getMaxDescriptionLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getMaxNameLength()
	 */
	@Override
	public int getMaxNameLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getMetaData()
	 */
	@Override
	public ConnectionMetaData getMetaData() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getModelNames(javax.datamining.MiningFunction, javax.datamining.MiningAlgorithm, java.util.Date, java.util.Date)
	 */
	@Override
	public Collection getModelNames(MiningFunction arg0, MiningAlgorithm arg1,
			Date arg2, Date arg3) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getNamedObjects(javax.datamining.resource.PersistenceOption)
	 */
	@Override
	public NamedObject[] getNamedObjects(PersistenceOption arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getObjectNames(javax.datamining.NamedObject)
	 */
	@Override
	public Collection getObjectNames(NamedObject arg0) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getObjectNames(java.util.Date, java.util.Date, javax.datamining.NamedObject)
	 */
	@Override
	public Collection getObjectNames(Date arg0, Date arg1, NamedObject arg2)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getObjectNames(java.util.Date, java.util.Date, javax.datamining.NamedObject, javax.datamining.Enum)
	 */
	@Override
	public Collection getObjectNames(Date arg0, Date arg1, NamedObject arg2,
			Enum arg3) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getSupportedAlgorithms(javax.datamining.MiningFunction)
	 */
	@Override
	public MiningAlgorithm[] getSupportedAlgorithms(MiningFunction arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#getSupportedFunctions()
	 */
	@Override
	public MiningFunction[] getSupportedFunctions() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#removeObject(java.lang.String, javax.datamining.NamedObject)
	 */
	@Override
	public void removeObject(String arg0, NamedObject arg1) throws JDMException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#renameObject(java.lang.String, java.lang.String, javax.datamining.NamedObject)
	 */
	@Override
	public void renameObject(String arg0, String arg1, NamedObject arg2)
			throws JDMException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#requestDataLoad(java.lang.String)
	 */
	@Override
	public void requestDataLoad(String arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#requestDataUnload(java.lang.String)
	 */
	@Override
	public void requestDataUnload(String arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#requestModelLoad(java.lang.String)
	 */
	@Override
	public void requestModelLoad(String arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#requestModelUnload(java.lang.String)
	 */
	@Override
	public void requestModelUnload(String arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#retrieveModelObjects(javax.datamining.MiningFunction, javax.datamining.MiningAlgorithm, java.util.Date, java.util.Date)
	 */
	@Override
	public Collection retrieveModelObjects(MiningFunction arg0,
			MiningAlgorithm arg1, Date arg2, Date arg3) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#retrieveObject(java.lang.String)
	 */
	@Override
	public MiningObject retrieveObject(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#retrieveObject(java.lang.String, javax.datamining.NamedObject)
	 */
	@Override
	public MiningObject retrieveObject(String arg0, NamedObject arg1)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#retrieveObjects(java.util.Date, java.util.Date, javax.datamining.NamedObject)
	 */
	@Override
	public Collection retrieveObjects(Date arg0, Date arg1, NamedObject arg2)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#retrieveObjects(java.util.Date, java.util.Date, javax.datamining.NamedObject, javax.datamining.Enum)
	 */
	@Override
	public Collection retrieveObjects(Date arg0, Date arg1, NamedObject arg2,
			Enum arg3) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#saveObject(java.lang.String, javax.datamining.MiningObject, boolean)
	 */
	@Override
	public void saveObject(String arg0, MiningObject arg1, boolean arg2)
			throws JDMException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#setDescription(java.lang.String, javax.datamining.NamedObject, java.lang.String)
	 */
	@Override
	public void setDescription(String arg0, NamedObject arg1, String arg2)
			throws JDMException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#supportsCapability(javax.datamining.NamedObject, javax.datamining.resource.PersistenceOption)
	 */
	@Override
	public boolean supportsCapability(NamedObject arg0, PersistenceOption arg1)
			throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.datamining.resource.Connection#supportsCapability(javax.datamining.MiningFunction, javax.datamining.MiningAlgorithm, javax.datamining.MiningTask)
	 */
	@Override
	public boolean supportsCapability(MiningFunction arg0,
			MiningAlgorithm arg1, MiningTask arg2) throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}

}
