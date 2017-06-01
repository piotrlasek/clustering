import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMClusteringSettingsFactory;
import org.dmtools.datamining.data.CDMFilePhysicalDataSetFactory;
import org.dmtools.datamining.resource.CDMFileConnectionFactory;
import org.dmtools.datamining.task.CDMBuildTaskFactory;
import util.DataSet;
import util.Workspace;

import javax.datamining.ExecutionStatus;
import javax.datamining.JDMException;
import javax.datamining.base.AlgorithmSettings;
import javax.datamining.clustering.AggregationFunction;
import javax.datamining.clustering.Cluster;
import javax.datamining.clustering.ClusteringModel;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;
import javax.datamining.resource.Connection;
import javax.datamining.resource.ConnectionSpec;
import javax.datamining.rule.Rule;
import javax.datamining.task.BuildTask;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Piotr Lasek
 *
 */
public class Main {

	private final static Logger log = LogManager.getLogger("Main");

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Workspace.readArgs(args);

			// DATASET PREPARATION
			// -----------------------------------------------------------------

			log.info("Start!");

			String cdmPath = Workspace.getWorkspacePath();

			long time1 = new Date().getTime();

			String dataFilePath = Workspace.getDataFilePath();

			if (dataFilePath == null) {
				dataFilePath = "/data/experiment/birch1.txt";
				//dataFilePath = "/data/my-file-2d.txt";
                //cs.setURI(cdmPath + "/data/experiment/birch2.txt"); //brich2
                //cs.setURI(cdmPath + "/data/experiment/birch3.txt"); //brich3
				//cs.setURI(cdmPath + "/data/my-file-2d.txt"); //~10 000 elements
			}

			log.info("Preparing input data...");

			CDMFileConnectionFactory fcf = new CDMFileConnectionFactory();
			ConnectionSpec cs = fcf.getConnectionSpec();

	        cs.setURI(cdmPath + dataFilePath);

			Connection conn = null;

			try {
				conn = fcf.getConnection(cs);
			} catch (JDMException jdme) {
				log.error("The connection could not be established.");
				log.error(jdme);
				return;
			}
			
			// Creating a physical data set.
			// -----------------------------------------------------------------:w
			CDMFilePhysicalDataSetFactory pdsf = new CDMFilePhysicalDataSetFactory();
			PhysicalDataSet fpds = null;
			fpds = DataSet.setAttributes(pdsf, cs);  // TODO
			conn.saveObject("MyPhysicalDataSet", fpds, true);
			
			// ALGORITHM PREPARATION
			// -----------------------------------------------------------------
			log.info("Preparing an algorithm...");

			CDMClusteringSettingsFactory clusterintSettigsFactory = 
					new CDMClusteringSettingsFactory();

			ClusteringSettings clusteringSettings = clusterintSettigsFactory.create();
			clusteringSettings.setLogicalDataName("MyLogicalData");
			clusteringSettings.setDescription("test description");
			clusteringSettings.setAggregationFunction(AggregationFunction.euclidean);

			String algorithm = Workspace.getAlgorithm();
			HashMap<String, String> parameters = Workspace.getParameters();
			AlgorithmSettings algorithmSettings = null;

			algorithmSettings = util.ClusteringSettings.prepare(algorithm, parameters);

			log.info("	" + algorithmSettings.toString());
			clusteringSettings.setAlgorithmSettings(algorithmSettings);
			conn.saveObject("ClusteringSettings", clusteringSettings, true);/**/

			// BUILD TASK
			log.info("Preparing a build task...");
			CDMBuildTaskFactory mbtf = new CDMBuildTaskFactory();
			BuildTask bt = null;

			bt = mbtf.create("MyPhysicalDataSet", "ClusteringSettings",
					"ClusteringOutputModel");

			log.info("Executing the algorithm...");
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

			long time2 = new Date().getTime();
			 
			// THE END 
			
			// PRINT CLUSTER NAMES
			log.info("Clusters:");
			for (Cluster c : clusters) {
				String n = c.getName();
				log.info("	" + n);
			}
			
			log.info("Processing finished.");
			long diff = time2-time1;
			log.info("Time1= " + time1 + "; Time2= " + time2 + "; Diff= " + diff +  ".");
		} catch (JDMException e) {
			e.printStackTrace();
		}
	}


}