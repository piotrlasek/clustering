package pl.iidml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.datamining.JDMException;
import javax.datamining.NamedObject;
import javax.datamining.data.AttributeDataType;
import javax.datamining.data.PhysicalAttribute;
import javax.datamining.data.PhysicalAttributeRole;
import javax.datamining.data.PhysicalDataSet;
import javax.datamining.statistics.AttributeStatisticsSet;


public class FilePhysicalDataSet implements PhysicalDataSet {

	ArrayList<FilePhysicalDataRecord> dataSet;
	
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
		return NamedObject.physicalDataSet;
	}

	@Override
	public void setDescription(String arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAttribute(PhysicalAttribute arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAttributes(PhysicalAttribute[] arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public PhysicalAttribute getAttribute(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		PhysicalAttribute pa;
		
		return null;
	}

	@Override
	public PhysicalAttribute getAttribute(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getAttributeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer getAttributeIndex(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getAttributeNames(AttributeDataType arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getAttributeNames(PhysicalAttributeRole arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttributeStatisticsSet getAttributeStatistics() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getAttributes() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void importMetaData() throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAllAttributes() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAttribute(String arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

}
