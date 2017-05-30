package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.algorithm.CDBSCAN.CDBSCANAlgorithmSettings;
import org.dmtools.clustering.algorithm.CNBC.CNBCAlgorithmSettings;
import org.dmtools.clustering.algorithm.DBSCAN.DBSCANAlgorithmSettings;
import org.dmtools.clustering.algorithm.DBSCAN.DbScanNetTrafficAlgorithmSettings;
import org.dmtools.clustering.algorithm.DBSCAN.DbScanSlicerAlgorithmSettings;
import org.dmtools.clustering.algorithm.KMeans.DM.DM_KMeansAlgorithmSettings;
import org.dmtools.clustering.algorithm.KMeans.KMeansAlgorithmSettings;
import org.dmtools.clustering.algorithm.NBC.DM.NBCDMAlgorithmSettings;
import org.dmtools.clustering.algorithm.NBC.NBCAlgorithmSettings;

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
        if (algorithm == null) {
            log.warn("Using a hardcoded algorithm");
           algorithm = "DBSCAN";
           parameters.put("Eps", "20");
           parameters.put("MinPts", "10");
        }
        switch (algorithm) {
            case "NBC":
                algorithmSettings = new NBCAlgorithmSettings();
                int kNBC = new Integer(parameters.get("k"));
                ((NBCAlgorithmSettings) algorithmSettings).setK(kNBC);
                break;
            case "C-NBC":
                algorithmSettings = new CNBCAlgorithmSettings();
                int kCNBC = new Integer(parameters.get("k"));
                ((CNBCAlgorithmSettings) algorithmSettings).setK(kCNBC);
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
                ((CDBSCANAlgorithmSettings) algorithmSettings).setEps(EpsCDB);
                ((CDBSCANAlgorithmSettings) algorithmSettings).setMinPts(MinPtsCDB);
                break;
            case "NBCDMA":
                algorithmSettings = new NBCDMAlgorithmSettings();
                // TODO: Parameters
                break;
            case "K-Means":
                algorithmSettings = new KMeansAlgorithmSettings();
                // TODO: Parameters
                break;
            case "DM_K-Means":
                algorithmSettings = new DM_KMeansAlgorithmSettings();
                // TODO: Parameters
                break;
            case "DBSCAN-NetTraffic":
                algorithmSettings = new DbScanNetTrafficAlgorithmSettings();
                // TODO: Parameters
                break;
            case "DBSCAN-Slicer":
                algorithmSettings = new DbScanSlicerAlgorithmSettings();
                // TODO: Parameters
        }
        return algorithmSettings;
    }
}
