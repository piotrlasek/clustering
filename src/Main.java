import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMClusteringSettingsFactory;
import org.dmtools.datamining.data.CDMFilePhysicalDataSetFactory;
import org.dmtools.datamining.resource.CDMFileConnectionFactory;
import org.dmtools.datamining.task.CDMBuildTaskFactory;
import util.DataSet;
import util.Dump;
import util.Workspace;

import javax.datamining.ExecutionStatus;
import javax.datamining.JDMException;
import javax.datamining.base.AlgorithmSettings;
import javax.datamining.clustering.AggregationFunction;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;
import javax.datamining.resource.Connection;
import javax.datamining.resource.ConnectionSpec;
import javax.datamining.task.BuildTask;
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

			if (args.length == 0) {
				args = new String[]{
						//"algorithm=C-NBC",
						//"algorithm=pikMeans",
						"algorithm=piMeans",
						//"algorithm=kMeans",
						//"data=\\data\\my-file-2d.txt",
						//"data=\\data\\CheckinsN.csv",
						"data=\\data\\CheckinsS.csv",
						//"data=[CUSTOM]\\data\\checkins-pyramid\\",
						//"data=[CUSTOM]\\data\\TaxiData\\",
						//"data=\\data\\experiment\\birch2.txt",
						//"parameters=Eps:10;MinPts:4;dump:yes;plot:yes;ic:random_10"
						//"parameters=k:10;dump;plot;close_plot;ic:random_4"
						//"parameters=k:10;dump;plot;ic:random_4"
						//"parameters=Eps:10;MinPts:4;dump:yes;ic:random_10"
						//"parameters=Eps:6000;MinPts:15;dump;plot;ic:birch2"
						"parameters=k:1;maxIterations:25;deepest:15;depth:15;starting:3;dump"
					};
			}

			Workspace.readArgs(args);

			// DATASET PREPARATION
			// -----------------------------------------------------------------

			log.info("Start!");

			String cdmPath = Workspace.getWorkspacePath();

			long time1 = new Date().getTime();

			String dataFilePath = Workspace.getDataFilePath();

			log.info("Preparing input data...");

			CDMFileConnectionFactory fcf = new CDMFileConnectionFactory();
			ConnectionSpec cs = fcf.getConnectionSpec();

	        cs.setURI(cdmPath + dataFilePath);

			Connection conn = fcf.getConnection(cs);

			// Creating a physical data set.
			// -----------------------------------------------------------------
            if (conn != null) {
				CDMFilePhysicalDataSetFactory pdsf = new CDMFilePhysicalDataSetFactory();
				PhysicalDataSet fpds = null;
				fpds = DataSet.setAttributes(pdsf, cs);  // TODO
				conn.saveObject("MyPhysicalDataSet", fpds, true);
			} else {
				log.warn("The dataset was not defined!");
			}
			
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
			AlgorithmSettings algorithmSettings =
                util.ClusteringSettings.prepare(algorithm, parameters);

			clusteringSettings.setAlgorithmSettings(algorithmSettings);
			conn.saveObject("ClusteringSettings", clusteringSettings, true);/**/

			// BUILD TASK
			log.info("Preparing a build task...");
			CDMBuildTaskFactory mbtf = new CDMBuildTaskFactory();
			BuildTask bt = mbtf.create("MyPhysicalDataSet", "ClusteringSettings",
                "ClusteringOutputModel");

			// EXECUTE
			// -----------------------------------------------------------------
			log.info("Executing the algorithm...");
			ExecutionStatus executionStatus = conn.execute(bt, null); // null - time out

			// GET RESULTS
			// -----------------------------------------------------------------
			// clusteringModel = (ClusteringModel) conn.retrieveObject("ClusteringOutputModel");
			// rules = (Collection<Rule>) cm.getRules();
			// clusters = (Collection<Cluster>) cm.getClusters();

			// print times
			log.info(executionStatus.getDescription());

			// log description in file
			Dump.saveTimes(executionStatus.getDescription());

			// THE END
			// -----------------------------------------------------------------
            log.info("Done.");

		} catch (JDMException e) {
			e.printStackTrace();
		}
	}


}