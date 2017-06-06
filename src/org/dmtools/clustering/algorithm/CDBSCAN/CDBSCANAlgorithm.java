package org.dmtools.clustering.algorithm.CDBSCAN;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.algorithm.CNBC.InstanceConstraints;
import org.dmtools.clustering.algorithm.CNBC.MyFrame2;
import org.dmtools.clustering.model.IClusteringData;
import org.dmtools.datamining.resource.CDMBasicMiningObject;
import spatialindex.spatialindex.Point;
import util.Dump;

import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;
import java.util.ArrayList;

/**
 * Created by Piotr Lasek on 30.05.2017.
 */
public class CDBSCANAlgorithm extends CDMBasicClusteringAlgorithm {

    private final static Logger log = LogManager.getLogger("CDBSCANAlgorithm");

    private double Eps;
    private int MinPts;

    String ic = null;

    /**
     *
     * @param clusteringSettings
     * @param physicalDataSet
     */
    public CDBSCANAlgorithm(ClusteringSettings clusteringSettings, PhysicalDataSet physicalDataSet) {
        super(clusteringSettings, physicalDataSet);

        CDBSCANAlgorithmSettings cdas = (CDBSCANAlgorithmSettings) clusteringSettings.getAlgorithmSettings();
        Eps = cdas.getEps();
        MinPts = cdas.getMinPts();
        ic = cdas.getIC();
    }

    /**
     *
     * @return
     */
    @Override
    public MiningObject run() {
        log.info(CDBSCANAlgorithmSettings.NAME + " start.");
        CDBSCANRTree dbscan = new CDBSCANRTree();

        log.info(CDBSCANAlgorithmSettings.NAME + " preparing data.");
        IClusteringData data = prepareData();

        timer.setAlgorithmName(CDBSCANAlgorithmSettings.NAME);
        timer.setParameters("Eps=" + Eps + ", MinPts=" + MinPts);

        timer.indexStart();
        dbscan.setData(data);
        timer.indexEnd();

        dbscan.setConstraints(ic);
        dbscan.setEps(Eps);
        dbscan.setMinPts(MinPts);

        log.info(CDBSCANAlgorithmSettings.NAME + " run");
        timer.clusteringStart();
        dbscan.run();
        timer.clusteringEnd();

        // merging with internal measurements
        timer.merge(dbscan.getInternalTimer());

        log.info(CDBSCANAlgorithmSettings.NAME + " finished.");

        IClusteringData resultToDump = dbscan.getResult();

        if (dump()) {
            String dumpFileName = Dump.getDumpFileName(CDBSCANAlgorithmSettings.NAME, getPhysicalDataSet().getDescription(),
                    "(Eps="+ Eps + ", minPts=" + MinPts + ")");
            Dump.toFile(resultToDump.get(), dumpFileName, true);
        }

        if (plot()) {
            // Show result
            ArrayList<Point> result = dbscan.getDataset();
            InstanceConstraints constraints = dbscan.getConstraints();
            MyFrame2.plotResult(result, max[0], constraints, null, null, null);
        }

        CDMBasicMiningObject basicMiningObject = new CDMBasicMiningObject();
        basicMiningObject.setDescription(timer.getLog());

        return basicMiningObject;
    }

}
