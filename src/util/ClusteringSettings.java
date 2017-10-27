package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMBaseAlgorithmSettings;
import org.dmtools.clustering.algorithm.CDBSCAN.CDBSCANAlgorithmSettings;
import org.dmtools.clustering.algorithm.CNBC.CNBCAlgorithmSettings;
import org.dmtools.clustering.algorithm.DBSCAN.DBSCANAlgorithmSettings;
import org.dmtools.clustering.algorithm.DBSCAN.DbScanNetTrafficAlgorithmSettings;
import org.dmtools.clustering.algorithm.DBSCAN.DbScanSlicerAlgorithmSettings;
import org.dmtools.clustering.algorithm.KMeans.DM.DM_KMeansAlgorithmSettings;
import org.dmtools.clustering.algorithm.KMeans.KMeansAlgorithmSettings;
import org.dmtools.clustering.algorithm.NBC.DM.NBCDMAlgorithmSettings;
import org.dmtools.clustering.algorithm.NBC.NBCAlgorithmSettings;
import org.dmtools.clustering.algorithm.PiMeans.PiMeansAlgorithmSettings;
import org.dmtools.clustering.algorithm.piKMeans.PiKMeansAlgorithmSettings;

import javax.datamining.base.AlgorithmSettings;
import java.util.HashMap;

/**
 * Created by Piotr Lasek on 30.05.2017.
 */
public class ClusteringSettings {

    private final static Logger log = LogManager.getLogger("ClusteringSettings");

    /**
     *
     * @param algorithm
     * @param parameters
     * @return
     */
    public static AlgorithmSettings prepare(String algorithm, HashMap<String, String> parameters) {
        AlgorithmSettings algorithmSettings = null;

       switch (algorithm) {
           case "kMeans":
               algorithmSettings = new KMeansAlgorithmSettings();
               int k  = new Integer(parameters.get("k"));
               int maxIterations = new Integer(parameters.get("maxIterations"));
               ((KMeansAlgorithmSettings) algorithmSettings).setK(k);
               ((KMeansAlgorithmSettings) algorithmSettings).setMaxIterations(maxIterations);
               break;
           case "piMeans": {
               algorithmSettings = new PiMeansAlgorithmSettings();
               int pk = new Integer(parameters.get("k"));
               int pmaxIterations = new Integer(parameters.get("maxIterations"));
               int deepest = new Integer(parameters.get("deepest"));
               int depth = new Integer(parameters.get("depth"));
               int starting = new Integer(parameters.get("starting"));
               ((PiMeansAlgorithmSettings) algorithmSettings).setK(pk);
               ((PiMeansAlgorithmSettings) algorithmSettings).setDepth(depth);
               ((PiMeansAlgorithmSettings) algorithmSettings).setDeepest(deepest);
               ((PiMeansAlgorithmSettings) algorithmSettings).setStarting(starting);
               ((PiMeansAlgorithmSettings) algorithmSettings).setMaxIterations(pmaxIterations);
           }
               break;
           case "pikMeans": {
               algorithmSettings = new PiKMeansAlgorithmSettings();
               int pk = new Integer(parameters.get("k"));
               int pmaxIterations = new Integer(parameters.get("maxIterations"));
               int deepest = new Integer(parameters.get("deepest"));
               int depth = new Integer(parameters.get("depth"));
               int starting = new Integer(parameters.get("starting"));
               ((PiKMeansAlgorithmSettings) algorithmSettings).setK(pk);
               ((PiKMeansAlgorithmSettings) algorithmSettings).setDepth(depth);
               ((PiKMeansAlgorithmSettings) algorithmSettings).setDeepest(deepest);
               ((PiKMeansAlgorithmSettings) algorithmSettings).setStarting(starting);
               ((PiKMeansAlgorithmSettings) algorithmSettings).setMaxIterations(pmaxIterations);
           }
               break;
            case "NBC":
                algorithmSettings = new NBCAlgorithmSettings();
                int kNBC = new Integer(parameters.get("k"));
                ((NBCAlgorithmSettings) algorithmSettings).setK(kNBC);
                break;
            case "C-NBC":
                algorithmSettings = new CNBCAlgorithmSettings();
                int kCNBC = new Integer(parameters.get("k"));
                String icNBC = parameters.get("ic");
                ((CNBCAlgorithmSettings) algorithmSettings).setK(kCNBC);
                ((CNBCAlgorithmSettings) algorithmSettings).setIC(icNBC);
                break;
            case "DBSCAN":
                algorithmSettings = new DBSCANAlgorithmSettings();
                double EpsDB = new Double(parameters.get("Eps"));
                int MinPtsDB = new Integer(parameters.get("MinPts"));
                ((DBSCANAlgorithmSettings) algorithmSettings).setEps(EpsDB);
                ((DBSCANAlgorithmSettings) algorithmSettings).setMinPts(MinPtsDB);
                break;
            case "C-DBSCAN":
                algorithmSettings = new CDBSCANAlgorithmSettings();
                double EpsCDB = new Double(parameters.get("Eps"));
                int MinPtsCDB = new Integer(parameters.get("MinPts"));
                int deltaCDB = new Integer(parameters.get("d"));
                String icCDB = parameters.get("ic");
                ((CDBSCANAlgorithmSettings) algorithmSettings).setEps(EpsCDB);
                ((CDBSCANAlgorithmSettings) algorithmSettings).setMinPts(MinPtsCDB);
                ((CDBSCANAlgorithmSettings) algorithmSettings).setIC(icCDB);
                ((CDBSCANAlgorithmSettings) algorithmSettings).setDelta(deltaCDB);
                break;
            case "NBCDMA":
                algorithmSettings = new NBCDMAlgorithmSettings();
                break;
            case "DM_K-Means":
                algorithmSettings = new DM_KMeansAlgorithmSettings();
                break;
            case "DBSCAN-NetTraffic":
                algorithmSettings = new DbScanNetTrafficAlgorithmSettings();
                break;
            case "DBSCAN-Slicer":
                algorithmSettings = new DbScanSlicerAlgorithmSettings();
        }

        CDMBaseAlgorithmSettings baseAlgorithmSettings = (CDMBaseAlgorithmSettings) algorithmSettings;

        if (parameters.containsKey("plot")) {
            baseAlgorithmSettings.setPlot(true);
        }
        if (parameters.containsKey("dump")) {
            baseAlgorithmSettings.setDump(true);
        }
        if (parameters.containsKey("close_plot")) {
            baseAlgorithmSettings.setClosePlot(true);
        }

        return algorithmSettings;
    }
}
