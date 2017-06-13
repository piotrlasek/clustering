package org.dmtools.clustering.algorithm.NBC.DM;

import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.CDMClusteringModel;
import org.dmtools.clustering.model.*;
import org.dmtools.clustering.old.BasicClusteringParameters;
import org.dmtools.clustering.old.DataSourceManager;
import org.dmtools.clustering.old.DataView2D;

import javax.datamining.JDMException;
import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalAttribute;
import javax.datamining.data.PhysicalDataSet;
import java.util.ArrayList;
import java.util.Collection;


/**
 * 
 * @author Piotr Lasek
 *
 */
public class NBCDMAlgorithm extends CDMBasicClusteringAlgorithm implements IClusteringObserver {
	
	int k = 0;
	ArrayList<double[]> data;
	int numberOfDimensions; 
	double[] min = null;
	double[] max = null;
	Collection<PhysicalAttribute> attributes;
	ArrayList<double[]> tempPoints;
	int maxRuns = 4;
	int[] clusterCount;
	
	//DimensionsMatrix DM;
	
	public NBCDMAlgorithm(ClusteringSettings clusteringSettings,
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

		System.out.println("numberOfDimensions= " + numberOfDimensions);
		prepareData();

		CDMClusteringModel ccm = new CDMClusteringModel();

		IClusteringAlgorithm ca = null;

		 // wstawiamy NBC do JDM

		DataView2D dv = new DataView2D();

		NBCDiffusionIndex nbc = new NBCDiffusionIndex();
		nbc.setObserver(dv);

		BasicClusteringParameters params = new BasicClusteringParameters();
		DataSourceManager dsm = new DataSourceManager();

		dsm.readData("abc", data);
		
		dsm.setActiveDataSource("abc");

	    IClusteringDataSource cds = dsm.getActiveDataSource();

		params.setValue("k", new Integer(k).toString());		 
		nbc.setParameters(params);

		InstanceConstraints constraints = new InstanceConstraints();
		//constraints.addMustLinkPoints(1035, 3408);
		//constraints.addMustLinkPoints(1035, 836);
		//constraints.addMustLinkPoints(1028, 1024);
		
		//constraints.addCannotLinkPoints(20, 2774);
		//constraints.addCannotLinkPoints(17, 2908);
		//constraints.addCannotLinkPoints(102, 246);
		//constraints.addCannotLinkPoints(102, 251);
		//constraints.addCannotLinkPoints(1001, 1500);
		//constraints.addCannotLinkPoints(1500, 2000);
		
			
		constraints.addRelative(1158, 1008, 4084);
		//constraints.addRelative(100, 130, 60);
		//constraints.addRelative(120, 125, 108);
		//constraints.addRelative(500, 650, 1050);
		//constraints.addRelative(500, 1001, 1500);
		
		printConstraints(constraints);
		
		//DimensionsMatrix dim =  new DimensionsMatrix(data, constraints, numberOfDimensions);

		
		nbc.setdata(data);
		
		nbc.setData(cds.getData()); 


		IClusteringData cd = nbc.getResult();
		Collection<IClusteringObject> result = cd.get();
		
		RelativeMatrix rel = new RelativeMatrix(data, constraints, numberOfDimensions);
		
		//nbc.setDM(dim.D_Distance);
		
		nbc.setDM(rel.D_Relative);
		
		nbc.run();

		
		cd = nbc.getResult();	
		result = cd.get();
		ArrayList<double[]> data2 = new ArrayList<double[]>(result.size());
		int i =0;
		for(IClusteringObject o : result)
		{
			double[] coord = o.getSpatialObject().getValues();
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
		ArrayList<double[]> fml = new ArrayList<double[]>();
		ArrayList<double[]> fcl = new ArrayList<double[]>();
		ArrayList<double[]> frc = new ArrayList<double[]>();
		
		for(int ii = 0; ii < constraints.ml1.size(); ii++) {
			fml.add(data.get(constraints.ml1.get(ii)));
			System.out.println(">>>> " + data.get(constraints.ml1.get(ii))[0] + ", " + data.get(constraints.ml1.get(ii))[1]);
			fml.add(data.get(constraints.ml2.get(ii)));
			System.out.println(">>>> " + data.get(constraints.ml2.get(ii))[0] + ", " + data.get(constraints.ml2.get(ii))[1]);
		}
		
		for(int ii = 0; ii < constraints.cl1.size(); ii++) {
			fcl.add(data.get(constraints.cl1.get(ii)));
			fcl.add(data.get(constraints.cl2.get(ii)));
		}
		
		for(int ii = 0; ii < constraints.r1.size(); ii++)
		{
			frc.add(data.get(constraints.r1.get(ii)));
			frc.add(data.get(constraints.r2.get(ii)));
			frc.add(data.get(constraints.r3.get(ii)));
		}
		
//		MyFrame.plotResult(result, max[0], null, null, null, null);

		return null;
	}

	@Override
	public String getDescription() {
		return null;
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
	
	private void printConstraints(InstanceConstraints constraints)
	{
		for(int i = 0; i < constraints.ml1.size(); i++){
			int ml1 = constraints.ml1.get(i);
			int ml2 = constraints.ml2.get(i);
			System.out.println("ML("+ ml1 +"|" + ml2 + ") : (" + data.get(ml1)[0] + 
					"," + data.get(ml1)[1] + ") (" + data.get(ml2)[0] + "," + data.get(ml2)[1] + ") ");
		}
		for(int i = 0; i < constraints.cl2.size(); i++){
			int cl1 = constraints.cl1.get(i);
			int cl2 = constraints.cl2.get(i);
			System.out.println("CL("+ cl1 +"|" + cl2 + ") : (" + data.get(cl1)[0] + 
					"," + data.get(cl1)[1] + ") (" + data.get(cl2)[0] + "," + data.get(cl2)[1] + ") ");
		}
		for(int i = 0; i < constraints.r1.size(); i++){
			int r1 = constraints.r1.get(i);
			int r2 = constraints.r2.get(i);
			int r3 = constraints.r3.get(i);
			System.out.println("ab|c("+ r1 +", " + r2 + "," + r3+") : (" + data.get(r1)[0] + 
					"," + data.get(r1)[1] + ") (" + data.get(r2)[0] + "," + data.get(r2)[1] + ") (" + data.get(r3)[0] + "," + data.get(r3)[1] + ")");
		}
	}


}


