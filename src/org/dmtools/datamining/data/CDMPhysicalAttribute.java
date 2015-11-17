package org.dmtools.datamining.data;

import javax.datamining.data.AttributeDataType;
import javax.datamining.data.PhysicalAttribute;
import javax.datamining.data.PhysicalAttributeRole;

public class CDMPhysicalAttribute implements PhysicalAttribute {

	String fDescription;
	String fName;
	AttributeDataType fDataType;
	PhysicalAttributeRole fRole;	
	
	@Override
	public String getDescription() {
		return fDescription;
	}

	@Override
	public String getName() {
		return fName;
	}

	@Override
	public AttributeDataType getDataType() {
		return fDataType;
	}

	@Override
	public PhysicalAttributeRole getRole() {
		return fRole;
	}

	@Override
	public void setDataType(AttributeDataType arg0) {
		fDataType = arg0;
	}

	@Override
	public void setDescription(String arg0) {
		fDescription = arg0;
	}

	@Override
	public void setName(String arg0) {
		fName = arg0;
	}

	@Override
	public void setRole(PhysicalAttributeRole arg0) {
		fRole = arg0;
	}

	
	
}
