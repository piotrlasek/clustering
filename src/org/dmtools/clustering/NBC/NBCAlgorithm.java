package org.dmtools.clustering.NBC;

import java.awt.Dimension;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;

import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.MiningObject;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;
import javax.datamining.clustering.AggregationFunction;
import javax.datamining.clustering.AttributeComparisonFunction;
import javax.datamining.clustering.ClusteringModel;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalAttribute;
import javax.datamining.data.PhysicalDataSet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.CDMClusteringModel;
import org.dmtools.clustering.CNBC.MyFrame;
import org.dmtools.clustering.old.BasicClusteringParameters;
import org.dmtools.clustering.old.DataSourceManager;
import org.dmtools.clustering.old.DataView2D;
import org.dmtools.clustering.old.IClusteringAlgorithm;
import org.dmtools.clustering.old.IClusteringData;
import org.dmtools.clustering.old.IClusteringDataSource;
import org.dmtools.clustering.old.IClusteringObject;
import org.dmtools.clustering.old.IClusteringObserver;
import org.dmtools.clustering.old.IClusteringParameters;
import org.dmtools.datamining.base.ScatterAdd;
import org.dmtools.datamining.data.CDMAlgorithm;
import org.dmtools.datamining.data.CDMFilePhysicalDataSet;
import org.dmtools.datamining.data.CDMPhysicalAttribute;
import org.dmtools.clustering.CDMBasicClusteringModel;
import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.util.ShapeUtilities;


/**
 * 
 * @author Piotr Lasek
 *
 */
public class NBCAlgorithm extends CDMBasicClusteringAlgorithm implements IClusteringObserver {
	
	int k = 0;
	ArrayList<double[]> data;
	int numberOfDimensions; 
	double[] min = null;
	double[] max = null;
	Collection<PhysicalAttribute> attributes;
	ArrayList<double[]> tempPoints;
	int maxRuns = 4;
	int[] clusterCount;
	
	public NBCAlgorithm(ClusteringSettings clusteringSettings,
			PhysicalDataSet physicalDataSet) {
		super(clusteringSettings, physicalDataSet);
		
		try {
			attributes = physicalDataSet.getAttributes();
		} catch (JDMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		k = (int) clusteringSettings.getMinClusterCaseCount();
		
		numberOfDimensions = attributes.size();
		
		min = new double[numberOfDimensions];
		max = new double[numberOfDimensions];
	}

	@Override
	public MiningObject run() {

		prepareData();

		CDMClusteringModel ccm = new CDMClusteringModel();

		IClusteringAlgorithm ca = null;

		 // wstawiamy NBC do JDM

		DataView2D dv = new DataView2D();

		NBCRTree nbc = new NBCRTree();
		nbc.setObserver(dv);

		BasicClusteringParameters params = new BasicClusteringParameters();
		DataSourceManager dsm = new DataSourceManager();

		dsm.readData("abc", data);
		dsm.setActiveDataSource("abc");

		 /*String id="";
         try {
             id = dsm.readFromFile(
            		 "C:\\Users\\Piotr\\Filr\\Moje pliki\\UNIWERSYTET\\"
            		 + "PROJEKTY\\DMEngine\\data\\my-file-2d.txt",
            		 "2D"); // wczytac plik do data source managera
             dsm.setActiveDataSource(id);
         } catch (Exception e) {
             e.printStackTrace();
         }*/

        IClusteringDataSource cds = dsm.getActiveDataSource();

		params.setValue("k", new Integer(k).toString());		 
		nbc.setParameters(params);

		nbc.setData(cds.getData());

		nbc.run();

		IClusteringData cd = nbc.getResult();

		Collection<IClusteringObject> result = cd.get();
		ArrayList<double[]> data2 = new ArrayList<double[]>(result.size());
		int i =0;
		for(IClusteringObject o : result)
		{
			double[] coord = o.getSpatialObject().getCoordinates();
			double[] r = new double[coord.length + 1];
			System.arraycopy(coord, 0, r, 0, coord.length);
			r[r.length - 1] = o.getClusterInfo().getClusterId(); 
			data2.add(r);			
			i++;
		}
		
		/*dv.setData(cd);
		dv.showDataSource();*/

//		ScatterAdd sa = new ScatterAdd("NBC", data2, null);
//		sa.setSize(400, 500);
//		sa.setVisible(true);
//		sa.toFront();

		// Show result
		MyFrame mf = new MyFrame(result, null, null, null, null);
		mf.setPreferredSize(new Dimension(700,  600));
		JFrame f = new JFrame();
		JScrollPane scrollPane = new JScrollPane(mf);
		mf.setScrollPane(scrollPane);
		scrollPane.setAutoscrolls(true);
		f.add(scrollPane);
		f.pack();
		f.setSize(new Dimension(700, 600));
		f.setVisible(true);

		CDMCluster cluster = new CDMCluster();
		return ccm;
	}

	@Override
	public void handleNotify(Object object) {
		if (object != null) System.out.println(object.toString());
	}

	@Override
	public void handleNotify(IClusteringData data) {
		if (data != null) System.out.println(data.toString());
	}

	@Override
	public void handleNotify(String message) {
		if (message != null) System.out.println(message.toString());
	}

	public void prepareData()
	{
		ArrayList<Object[]> rawData =
				((CDMFilePhysicalDataSet) getPhysicalDataSet()).getData();
		data = new ArrayList<double[]>();
		
		int i = 0;
		for(Object[] rawRecord : rawData) {
			double[] record = new double[attributes.size() + 1];
			int d = 0;
			for(PhysicalAttribute attribute : attributes)
			{
				record[d] = new Double(rawData.get(i)[d].toString());
				if (min[d] == 0)
					min[d] = record[d];
				else							
				if (min[d] > record[d]) min[d] = record[d];
				if (max[d] < record[d]) max[d] = record[d];
				d++;					
			}
			record[d] = -1; // UNCLUSTERED
			data.add(record);
			i++;
		}
	}
}


