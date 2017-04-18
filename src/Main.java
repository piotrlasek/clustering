import org.dmtools.clustering.CDMClusteringSettingsFactory;
import org.dmtools.clustering.algorithm.KMeans.KMeansAlgorithmSettings;
import org.dmtools.datamining.data.CDMFilePhysicalDataSetFactory;
import org.dmtools.datamining.data.CDMPhysicalAttributeFactory;
import org.dmtools.datamining.resource.CDMFileConnectionFactory;
import org.dmtools.datamining.task.CDMBuildTaskFactory;
import util.Workspace;

import javax.datamining.ExecutionStatus;
import javax.datamining.JDMException;
import javax.datamining.clustering.AggregationFunction;
import javax.datamining.clustering.Cluster;
import javax.datamining.clustering.ClusteringModel;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.AttributeDataType;
import javax.datamining.data.PhysicalAttribute;
import javax.datamining.data.PhysicalDataSet;
import javax.datamining.resource.Connection;
import javax.datamining.resource.ConnectionSpec;
import javax.datamining.rule.Rule;
import javax.datamining.task.BuildTask;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

/**
 * @author Piotr Lasek
 *
 */
public class Main {

	public static void main(String[] args) {
		try {
			// DATASET PREPARATION
			// -----------------------------------------------------------------
			
			String cdmPath = Workspace.getWorkspacePath();
			System.out.println("Current dir: " + cdmPath);
	        
			System.out.println("Start!");
			
			long time1 = new Date().getTime();
			
			CDMFileConnectionFactory fcf = new CDMFileConnectionFactory();
			ConnectionSpec cs = fcf.getConnectionSpec();
	        
			//cs.setURI(cdmPath + "/data/manual/my-file-2d.txt"); //~3 000 elements

	        //cs.setURI(cdmPath + "\\data\\iris\\iris.data-2");
	        //cs.setURI(cdmPath + "\\data\\my\\new");
	        //cs.setURI(cdmPath + "\\data\\my2\\new");
	        
	        //source http://cs.joensuu.fi/sipu/datasets/
	        //cs.setURI(cdmPath + "\\data\\birch\\birch1-10000.txt");
			//cs.setURI(cdmPath + "\\data\\new-data\\breast.txt");
	        //cs.setURI(cdmPath + "\\data\\new-data\\r15.txt");
	        //cs.setURI(cdmPath + "\\data\\new-data\\spiral.txt");
	        //cs.setURI(cdmPath + "\\data\\new-data\\a3.txt"); //~30 000 elements
	        //cs.setURI(cdmPath + "\\data\\new-data\\Aggregation.txt"); //~780 elements
	        //cs.setURI(cdmPath + "\\data\\new-data\\DIM032.txt"); //~1 000 elements
	        //cs.setURI(cdmPath + "\\data\\new-data\\s-sets1.txt"); //~5 000 elements
	        //cs.setURI(cdmPath + "\\data\\new-data\\yeast.txt"); //~1 480 elements
	        //cs.setURI(cdmPath + "\\data\\new-data\\pathbased.txt"); //~300 elements
	        //cs.setURI(cdmPath + "\\data\\new-data\\jain.txt"); //~370 elements
	        //cs.setURI(cdmPath + "\\data\\new-data\\flame.txt"); //~240 elements
	        //cs.setURI(cdmPath + "\\data\\new-data\\a1.txt"); //~3 000 elements
			//cs.setURI(cdmPath + "/data/new-data/D31.txt"); //~3 000 elements

			// net traffic
	        // cs.setURI(cdmPath + "/data/net-traffic/sample24_1000000.csv");
			//cs.setURI("/Users/piotr/Desktop/plik.txt");
	        //cs.setURI(cdmPath + "\\data\\new-data\\t7.10k-002.txt"); //~6 000 elements
			cs.setURI(cdmPath + "/data/my-file-2d.txt"); //~10 000 elements
//	        cs.setURI(cdmPath + "/data/experiment/birch1.txt"); //brich1
//	        cs.setURI(cdmPath + "/data/experiment/birch2.txt"); //brich2
//	        cs.setURI(cdmPath + "/data/experiment/birch3.txt"); //brich3

			Connection conn = null;
			conn = fcf.getConnection(cs);
			
			// Create physical data set.
			CDMFilePhysicalDataSetFactory pdsf = new CDMFilePhysicalDataSetFactory();
			PhysicalDataSet fpds = null;
			fpds = setAttributes(pdsf, cs);  // TODO
			
			conn.saveObject("MyPhysicalDataSet", fpds, true);
			
			// ALGORITHM PREPARATION
			// -----------------------------------------------------------------
						
			CDMClusteringSettingsFactory clusterintSettigsFactory = 
					new CDMClusteringSettingsFactory();
			

			KMeansAlgorithmSettings algorithmSettings = new KMeansAlgorithmSettings();
			/**/
			
//			NBCAlgorithmSettings algorithmSettings = new NBCAlgorithmSettings();

			
			/*CNBCDMAlgorithmSettings algorithmSettings = new CNBCDMAlgorithmSettings();
			/**/
			
			/*
			NBCDMAlgorithmSettings algorithmSettings = new NBCDMAlgorithmSettings();
			/**/
			
			/*
			DM_KMeansAlgorithmSettings algorithmSettings = 
				ew DM_KMeansAlgorithmSettings(); /**/


			/*DbScanNetTrafficAlgorithmSettings algorithmSettings =
					new DbScanNetTrafficAlgorithmSettings();/**/

			/*DbScanSlicerAlgorithmSettings algorithmSettings =
					new DbScanSlicerAlgorithmSettings();/**/

			ClusteringSettings clusteringSettings = null;
			clusteringSettings = clusterintSettigsFactory.create();
//			clusteringSettings.setMinClusterCaseCount(25); // set parameter k
			//clusteringSettings.setMinClusterCaseCount(100); // set parameter k
			clusteringSettings.setMinClusterCaseCount(6); // set parameter k

			clusteringSettings.setLogicalDataName("MyLogicalData");
			clusteringSettings.setDescription("test description");
			clusteringSettings.setAggregationFunction(
					AggregationFunction.euclidean);

			// NBCDMAlgorithmSettings algorithmSettings = new NBCDMAlgorithmSettings();
			//CNBCAlgorithmSettings algorithmSettings = new CNBCAlgorithmSettings();
			//NBCAlgorithmSettings algorithmSettings = new NBCAlgorithmSettings();

			clusteringSettings.setAlgorithmSettings(algorithmSettings);
			conn.saveObject("ClusteringSettings", clusteringSettings, true);/**/

			// BUILD TASK
			CDMBuildTaskFactory mbtf = new CDMBuildTaskFactory();
			BuildTask bt = null;

			bt = mbtf.create("MyPhysicalDataSet", "ClusteringSettings",
					"ClusteringOutputModel");
			
			// EXECUTE
			// -----------------------------------------------------------------
			Long timeOut = null;
			ExecutionStatus eh = null; 
			Collection<Cluster> clusters = null;
			Collection<Rule> rules = null;
			ClusteringModel cm = null;
		
			eh = conn.execute(bt, timeOut);
			cm = (ClusteringModel) conn.retrieveObject("ClusteringOutputModel");
			rules = (Collection<Rule>) cm.getRules();
			clusters = (Collection<Cluster>) cm.getClusters();
			System.out.println("Mined clusters:");
			long time2 = new Date().getTime();
			 
			// THE END 
			
			// PRINT CLUSTER NAMES
			
			for (Cluster c : clusters) {
				String n = c.getName();
				System.out.println(n);
			}
			
			System.out.println("End.");
			long diff = time2-time1;
			System.out.println("Time1= " + time1 + "; Time2= " + time2 + "; Diff= " + diff +  ".");
		} catch (JDMException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param pdsf
	 * @param cs
	 * @return
	 * @throws JDMException
	 */
	private static PhysicalDataSet setAttributes(
			CDMFilePhysicalDataSetFactory pdsf, ConnectionSpec cs) throws JDMException {
		
		PhysicalDataSet fpds;
		fpds = pdsf.create("DMT Physical Data Set", false);

		Scanner sc = null;
		String line = null;
		Integer numberOfAttributes = null;
		
		CDMPhysicalAttributeFactory paf = new CDMPhysicalAttributeFactory();
		
		try {
			sc = new Scanner(new File(cs.getURI()));
			line = sc.nextLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Try to read number of attributes from a file
		try {
			System.out.println("Reading a number of attributes...");
			numberOfAttributes = new Integer(line);
			for(int i = 0; i < numberOfAttributes; i++) {
				PhysicalAttribute pa = paf.create("a" + i,
						AttributeDataType.doubleType);
				fpds.addAttribute(pa);
			}
		} catch (Exception e) {
			System.out.println("WARNING: Number of attributes not " +
					"determined (1).");
		}
		
		// Try to determine number of attributes
		if (numberOfAttributes == null) {
			try {
				System.out.println("Determining a number of attributes...");
				
				String[] attributes = null;
				
				attributes = line.split(",");
				
				if (attributes == null || attributes.length == 0) {
					attributes = line.split(";");
				}
				
				if (attributes != null && attributes.length > 0) {
				
					for(int i = 0; i < attributes.length; i++) {
						PhysicalAttribute pa = paf.create(attributes[i].trim(),
								AttributeDataType.doubleType);
						fpds.addAttribute(pa);
					}
				}
			} catch (Exception e) {
				System.out.println("WARNING: Number of attributes not " +
						"determined (2).");
			}
		}

		return fpds;
	}
}