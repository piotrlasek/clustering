package org.dmtools.clustering.algorithm.CNBC;

import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.CDMClusteringModel;
import org.dmtools.clustering.model.IClusteringAlgorithm;
import org.dmtools.clustering.model.IClusteringData;
import org.dmtools.clustering.model.IClusteringDataSource;
import org.dmtools.clustering.model.IClusteringObserver;
import org.dmtools.clustering.old.BasicClusteringParameters;
import org.dmtools.clustering.old.DataSourceManager;
import org.dmtools.clustering.old.DataView2D;
import org.dmtools.datamining.data.CDMFilePhysicalDataSet;

import javax.datamining.JDMException;
import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalAttribute;
import javax.datamining.data.PhysicalDataSet;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;


/**
 * 
 * @author Piotr Lasek
 *
 */
public class CNBCAlgorithm extends CDMBasicClusteringAlgorithm implements IClusteringObserver {

	int k = 0;
	ArrayList<double[]> data;
	int numberOfDimensions; 
	double[] min = null;
	double[] max = null;
	Collection<PhysicalAttribute> attributes;

	public CNBCAlgorithm(ClusteringSettings clusteringSettings,
			PhysicalDataSet physicalDataSet)
	{
		super(clusteringSettings, physicalDataSet);
		
		try {
			attributes = physicalDataSet.getAttributes();
		} catch (JDMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		k = (int) clusteringSettings.getMinClusterCaseCount();
		//k = 10;
		
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

//		CNBCRTree nbc = new CNBCRTree();
		CDNBCRTree nbc = new CDNBCRTree();
		nbc.setObserver(dv);

		BasicClusteringParameters params = new BasicClusteringParameters();
		DataSourceManager dsm = new DataSourceManager();

		dsm.readData("abc", data);
		dsm.setActiveDataSource("abc");

        IClusteringDataSource cds = dsm.getActiveDataSource();

		params.setValue("k", new Integer(k).toString());		 
		nbc.setParameters(params);

		nbc.setData(cds.getData());

		nbc.run();

		InstanceConstraints ic = nbc.getConstraints();

		ArrayList<CNBCRTreePoint> result = nbc.getDataset();

//		ArrayList<double[]> dataToPlot = new ArrayList();
//
//        for(CNBCRTreePoint p : result) {
//            double[] point = new double[p.getValues().length + 1];
//            point[0] = p.getValues()[0];
//			point[1] = p.getValues()[1];
//            point[point.length-1] = p.getClusterId();
//            dataToPlot.add(point);
//        }
//
//        ScatterAdd sa = new ScatterAdd("C-NBC", dataToPlot, null);
//        sa.setSize(400, 500);
//        sa.setVisible(true);

		// Show result
		MyFrame2 mf = new MyFrame2(result, ic, null, null, null);
		mf.setPreferredSize(new Dimension(700,  600));
		JFrame f = new JFrame();
		JScrollPane scrollPane = new JScrollPane(mf);
		mf.setScrollPane(scrollPane);
		scrollPane.setAutoscrolls(true);
		f.add(scrollPane);
		f.pack();
        f.setSize(new Dimension(700, 600));
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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


	public void prepareData() {
		ArrayList<Object[]> rawData =
				((CDMFilePhysicalDataSet) getPhysicalDataSet()).getData();
		data = new ArrayList<double[]>();
		
		int i = 0;
		for(Object[] rawRecord : rawData)
		{
			double[] record = new double[attributes.size() + 1];
			if (record.length < 2) {
				System.out.println("ERROR");
			}
			if (rawData.get(i).length < 2) {
				System.out.println("ERROR 2");
			}
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


