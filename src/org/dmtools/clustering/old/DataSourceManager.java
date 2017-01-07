package org.dmtools.clustering.old;

import org.dmtools.clustering.model.IClusteringDataSource;
import org.dmtools.clustering.model.IClusteringObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class DataSourceManager
{
	String activeDataSource;
	
	HashMap<String, IClusteringDataSource> dataSources;
//	MainWindow mw;
	
	/**
	 * 
	 */
	public DataSourceManager(/*MainWindow mw*/)
	{
		dataSources = new HashMap<String, IClusteringDataSource>();
		activeDataSource = "";
//		this.mw = mw;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public IClusteringDataSource getDataSourceAt(int index)
	{
		if (dataSources != null && index < dataSources.size() && index >= 0 )
		{
			return dataSources.get(index);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 
	 * @param dataSource
	 */
	public void addDataSource(String id, IClusteringDataSource dataSource)
	{
		dataSources.put(id, dataSource);
	}
	
	public void showDataSource(String id)
	{
		this.activeDataSource = id;
		dataSources.get(id).showDataSource();
	}
	
	public void saveDataSource(int index)
	{
		dataSources.get(index).saveDataSource();
	}
	
	public void closeDataSource(int index)
	{
		dataSources.remove(index);
	}
	
	public void close()
	{
	    Set<String> keys = dataSources.keySet();
	    
	    Iterator<String> it = keys.iterator();
	    while(it.hasNext()) {
	        String k = it.next();
	        IClusteringDataSource cds = dataSources.get(k);
	        cds.close();
	    }
	    
	}
	
	public String readData(String id, ArrayList<double[]> data)
	{
		BasicClusteringData bcd = new BasicClusteringData();
		Collection<IClusteringObject> list = new ArrayList<IClusteringObject>();
		
		for(double[] record: data)
		{
			int nAttr = record.length-1;

			double[] coords = new double[nAttr];

			for (int i = 0; i < nAttr; i++)
				coords[i] = new Double(record[i]).doubleValue();

			BasicClusteringObject bco = new BasicClusteringObject();
			BasicSpatialObject so = new BasicSpatialObject(coords);
			BasicClusterInfo bci = new BasicClusterInfo();
			bci.setClusterId(new Double(record[record.length - 1]).intValue());
			bco.setSpatialObject(so);
			bco.setClusterInfo(bci);
			list.add(bco);
		}

		bcd.set(list);
		
		BasicDataSource bds = new BasicDataSource();
		bds.setData(bcd);
		dataSources.put(id, bds);
		
		return id;
	}
	
	public String readFromFile(String path, String suggestedType) throws Exception
	{
		BasicClusteringData bcd = new BasicClusteringData();
		Collection<IClusteringObject> list = new ArrayList<IClusteringObject>();
		
		int nAttr = -1;// new Integer(sNumAttr[1]).intValue();
		
		try {
			FileInputStream fin = new FileInputStream(path);
			BufferedReader myInput = new BufferedReader(new InputStreamReader(
					fin));
			String thisLine;
			
			while ((thisLine = myInput.readLine()) != null)
			{
			        thisLine = thisLine.trim();
			        
				String s[] = thisLine.split(";");
				if (s.length <= 1) {
					s = thisLine.split(":");
				} 
				if (s.length <= 1) {
                                        s = thisLine.split("   ");
                                }       
				if (s.length <= 1) {
                                        s = thisLine.split(",");
                                }
				

				if (nAttr == -1) {
					nAttr = s.length - 1;
				}

				if ((s.length - 1) != nAttr) {
					throw new Exception("Incorrect number of attributes: "
							+ (s.length - 1) + " != " + nAttr);
				}

				double[] coords = new double[nAttr];

				for (int i = 0; i < nAttr; i++)
					coords[i] = new Double(s[i]).doubleValue();

				BasicClusteringObject bco = new BasicClusteringObject();
				BasicSpatialObject so = new BasicSpatialObject(coords);
				BasicClusterInfo bci = new BasicClusterInfo();
				bci.setClusterId(new Double(s[s.length - 1]).intValue());
				bco.setSpatialObject(so);
				bco.setClusterInfo(bci);
				list.add(bco);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		bcd.set(list);
		
		String id = path + dataSources.size();
		
		// choose appropriate data source type
		if (nAttr == 2 && suggestedType.equals("2D"))
		{
			//DataView2D dv = new DataView2D(mw);
			//dv.setData(bcd);
			//dataSources.put(id, dv);
			//dv.setId(id);
		}
		else
		{
			//DataViewTable dv = new DataViewTable(mw);
		//	//dv.setData(bcd);
		//	dataSources.put(id, dv);
			//dv.setId(id);
		}
		
		BasicDataSource bds = new BasicDataSource();
		bds.setData(bcd);
		dataSources.put(id, bds);
		
		return id;
	}
	
	public void saveToFile()
	{
		
	}
	
	public String createNewDataSource(String type) throws Exception
	{
		BasicClusteringData bcd = new BasicClusteringData();
		Collection<IClusteringObject> list = new ArrayList<IClusteringObject>();
		
		String id = "data_source_" + this.dataSources.size();
		
		bcd.set(list);
		
		// choose appropriate data source type
		/*if (type.equals("2D"))
		{
			DataView2D dv = new DataView2D(mw);
			dv.setData(bcd);
			dataSources.put(id, dv);
			dv.setId(id);
		}
		else
		{
			DataViewTable dvt = new DataViewTable(mw);
			dvt.setData(bcd);
			dataSources.put(id, dvt);
			dvt.setId(id);
		}*/
		
		BasicDataSource bds = new BasicDataSource();
		
		dataSources.put(id,  bds);
		
		return id;
	}
	
	
	public void setActiveDataSource(String id)
	{
		this.activeDataSource = id;
	}
	
	public IClusteringDataSource getActiveDataSource()
	{
		return dataSources.get(activeDataSource);
	}
}
