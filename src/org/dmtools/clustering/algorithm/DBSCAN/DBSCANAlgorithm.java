package org.dmtools.clustering.algorithm.DBSCAN;

import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.algorithm.common.PlotPanel;
import org.dmtools.clustering.model.IClusteringData;
import org.dmtools.clustering.model.IClusteringObject;
import util.Dump;

import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;
import java.util.Collection;

/**
 *
 */
public class DBSCANAlgorithm extends CDMBasicClusteringAlgorithm {


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
        dbscan.setEps(Eps);
        dbscan.setMinPts(MinPts);

        timer.setAlgorithmName(DBSCANAlgorithmSettings.NAME);
        timer.setParameters(getDescription());

        timer.indexStart();
        dbscan.setData(data);
        timer.indexEnd();

        log.info(DBSCANAlgorithmSettings.NAME + " run.");
        timer.clusteringStart();
        dbscan.run();
        timer.clusteringEnd();

        log.info(DBSCANAlgorithmSettings.NAME + " finished.");

        IClusteringData cd = dbscan.getResult();

        String logFileName = Dump.getLogFileName(DBSCANAlgorithmSettings.NAME, getPhysicalDataSet().getDescription(),
                getDescription() + " (clusters=" + dbscan.clusterCount() + ")");

        if (dump()) {
            Dump.toFile(cd.get(), logFileName + ".csv", true); //data to dump
        }

        // Show result
        if (plot()) {
            Collection<IClusteringObject> result = cd.get();
            PlotPanel.plotResult(dbscan.getDataset(), max[0], max[1], null, null, null, null, logFileName + ".png", closePlot(), dbscan.clusterCount());
        }

        basicMiningObject.setDescription(timer.getLog() + "\t" + dbscan.clusterCount());
        return basicMiningObject;
    }

    @Override
    public String getDescription() {
        return "(Eps=" + Eps + ", minPts=" + MinPts + ")";
    }

}
