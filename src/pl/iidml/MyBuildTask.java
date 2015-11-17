package pl.iidml;

import java.util.Date;
import java.util.Map;

import javax.datamining.ExecutionHandle;
import javax.datamining.JDMException;
import javax.datamining.NamedObject;
import javax.datamining.VerificationReport;
import javax.datamining.task.BuildTask;


public class MyBuildTask implements BuildTask {
	
	private String buildDataName;
	private String buildSettingsName;
	private String modelName;

	@Override
	public ExecutionHandle getExecutionHandle() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VerificationReport verify() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getCreationDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCreatorInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NamedObject getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDescription(String arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getApplicationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map getBuildDataMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBuildDataName() {
		return buildDataName;
	}

	@Override
	public String getBuildSettingsName() {
		return buildSettingsName;
	}

	@Override
	public String getInputModelName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModelDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModelName() {
		return modelName;
	}

	@Override
	public Map getValidationDataMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValidationDataName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setApplicationName(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBuildDataMap(Map arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBuildDataName(String arg0) throws JDMException {		
		buildDataName = arg0;

	}

	@Override
	public void setBuildSettingsName(String arg0) throws JDMException {
		buildSettingsName = arg0;
	}

	@Override
	public void setInputModelName(String arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setModelDescription(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setModelName(String arg0) throws JDMException {
		modelName = arg0;

	}

	@Override
	public void setValidationDataMap(Map arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValidationDataName(String arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

}
