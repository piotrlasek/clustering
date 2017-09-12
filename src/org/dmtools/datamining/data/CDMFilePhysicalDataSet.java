package org.dmtools.datamining.data;

import org.dmtools.datamining.CDMException;
import org.dmtools.datamining.base.CDMAbstractMiningObject;

import javax.datamining.JDMException;
import javax.datamining.NamedObject;
import javax.datamining.data.AttributeDataType;
import javax.datamining.data.PhysicalAttribute;
import javax.datamining.data.PhysicalAttributeRole;
import javax.datamining.data.PhysicalDataSet;
import javax.datamining.resource.Connection;
import javax.datamining.statistics.AttributeStatisticsSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class CDMFilePhysicalDataSet extends CDMAbstractMiningObject implements
		PhysicalDataSet {

	String URI;
	String separator;
	boolean readFirstRow;
	Connection connection;
	CDMDataRecord[] records;
	ArrayList<Object[]> rows;
	ArrayList<PhysicalAttribute> attributes;
	ArrayList<CDMFilePhysicalDataRecord> dataSet;

	/**
	 * 
	 */
	public CDMFilePhysicalDataSet() {
		attributes = new ArrayList<PhysicalAttribute>();
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Object[]> getData() {
		return rows;
	}

	/**
	 * 
	 * @param conn
	 * @throws CDMException
	 */
	public void readData(Connection conn) throws CDMException {
		if (separator == null) {
			throw new CDMException("Separator not defined!");
		}

		connection = conn;

		String fileName = connection.getConnectionSpec().getURI();

		BufferedReader br = null;

		rows = new ArrayList<Object[]>();

		try {
			br = new BufferedReader(new FileReader(fileName));
			String line = null;
			line = br.readLine();
			if (line.contains("SLICER")) {
				readSlicer(br, rows);
			} else {
				line = br.readLine();
				while ((line = br.readLine()) != null) {
					Object[] cols = line.split(separator);
					rows.add(cols);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param br
	 * @param rows
	 * @throws IOException 
	 */
	public void readSlicer(BufferedReader br, ArrayList<Object[]> rows)
			throws IOException {
		String line = br.readLine();
		long i = 0;
		while ((line = br.readLine()) != null) {
			line = line.replace("(",  "");
			line = line.replace(")", "");
			Object[] cols = line.split(",");
			rows.add(cols);
			i++;
			if (i % 1000000 == 0) {
				System.out.println(i);
			}
		}
	}

	/**
	 * 
	 * @param separator
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * 
	 * @param readFirstRow
	 */
	public void readFirstRow(boolean readFirstRow) {
		this.readFirstRow = readFirstRow;
	}

	@Override
	public String getName() {
		String fileName = connection.getConnectionSpec().getURI();
		return fileName;
	}

	@Override
	public String getObjectIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NamedObject getObjectType() {
		return NamedObject.physicalDataSet;
	}

	@Override
	public void addAttribute(PhysicalAttribute arg0) throws JDMException {
		attributes.add(arg0);
	}

	@Override
	public void addAttributes(PhysicalAttribute[] arg0) throws JDMException {
		throw new CDMException("addAttributes() not supported.");
	}

	@Override
	public PhysicalAttribute getAttribute(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		throw new CDMException("getAttribute() not supported.");
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
		return attributes;
	}

	@Override
	public String getURI() {
		return URI;
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
