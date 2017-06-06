package org.dmtools.clustering.algorithm.DBSCAN;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.algorithm.CNBC.MyFrame;
import org.dmtools.clustering.model.IClusteringData;
import org.dmtools.clustering.model.IClusteringObject;
import util.Dump;

import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;
import java.util.Collection;

public class DBSCANAlgorithm extends CDMBasicClusteringAlgorithm {

    private final static Logger log = LogManager.getLogger(DBSCANAlgorithm.class.getName());

    private double Eps;
    private int MinPts;

    public DBSCANAlgorithm(ClusteringSettings clusteringSettings, PhysicalDataSet physicalDataSet) {
        super(clusteringSettings, physicalDataSet);

        DBSCANAlgorithmSettings das = (DBSCANAlgorithmSettings) clusteringSettings.getAlgorithmSettings();
        Eps = das.getEps();
        MinPts = das.getMinPts();
    }

    @Override
    public MiningObject run() {
        log.info(DBSCANAlgorithmSettings.NAME + " start.");

        log.info("Preparing data...");
        IClusteringData data = prepareData();

        DBSCANRTree dbscan = new DBSCANRTree();

        timer.setAlgorithmName(DBSCANAlgorithmSettings.NAME);
        timer.setParameters("Eps=" + Eps + ", MinPts" + MinPts);

        timer.indexStart();
        dbscan.setData(data);
        timer.indexEnd();

        dbscan.setEps(Eps);
        dbscan.setMinPts(MinPts);

        log.info(DBSCANAlgorithmSettings.NAME + " run.");

        timer.clusteringStart();
        dbscan.run();
        timer.clusteringEnd();

        log.info(DBSCANAlgorithmSettings.NAME + " finished.");

        IClusteringData cd = dbscan.getResult();

        if (dump()) {
            String dumpFileName = Dump.getDumpFileName(DBSCANAlgorithmSettings.NAME, getPhysicalDataSet().getDescription(),
                    "(eps=" + Eps + ", minPts=" + MinPts + ")");
            Dump.toFile(cd.get(), dumpFileName, true); //data to dump
        }

        // Show result
        if (plot()) {
            Collection<IClusteringObject> result = cd.get();
            MyFrame.plotResult(result, max[0], null, null, null, null);
        }

        basicMiningObject.setDescription(timer.getLog());
        return basicMiningObject;
    }

}
