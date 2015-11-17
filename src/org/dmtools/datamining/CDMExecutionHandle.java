package org.dmtools.datamining;

import java.util.Collection;
import java.util.Date;

import javax.datamining.ExecutionStatus;
import javax.datamining.JDMException;

public class CDMExecutionHandle implements javax.datamining.ExecutionHandle {

	@Override
	public boolean containsWarning() throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer getDurationInSeconds() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionStatus getLatestStatus() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getStartTime() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getStatus(Date arg0) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTaskName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionStatus[] getWarnings() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionStatus terminate() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionStatus waitForCompletion(int arg0) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

}
