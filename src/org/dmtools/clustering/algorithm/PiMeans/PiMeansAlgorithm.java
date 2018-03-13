package org.dmtools.clustering.algorithm.PiMeans;

import javafx.scene.input.PickResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.algorithm.piKMeans.PiKMeansAlgorithmSettings;
import org.dmtools.clustering.old.ClusteringTimer;
import util.Dump;

import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;
import java.io.IOException;
import java.util.*;

/**
 * Author: Nasim Razavi
 * Date: September 8, 2017
 */
public class PiMeansAlgorithm extends CDMBasicClusteringAlgorithm {

    // zle liczone sa maxx i maxy w PiBin

    int k;
    int maxIterations;
    int startingStratum;
    int depth;

    ClusteringTimer timer = new ClusteringTimer(PiMeansAlgorithmSettings.NAME);

    protected final static Logger log = LogManager.getLogger(PiMeansAlgorithm.class.getSimpleName());

    /**
     * @throws IOException
     */
    public PiMeansAlgorithm(ClusteringSettings clusteringSettings,
                            PhysicalDataSet physicalDataSet) {
        super(clusteringSettings, physicalDataSet);

        PiMeansAlgorithmSettings pkmas =
                (PiMeansAlgorithmSettings) clusteringSettings.getAlgorithmSettings();

        k = pkmas.getK();
        maxIterations = pkmas.getMaxIterations();
        startingStratum = pkmas.getStarting();
        depth = pkmas.getDepth();
    }

    public class BoundManager {

        HashMap<Double, PiCluster> lbDist = new HashMap<>();
        HashMap<Double, PiCluster> ubDist = new HashMap<>();

        public void putLowerBound(Double lb, PiCluster cluster) {
            lbDist.put(lb, cluster);
        }

        public void putUpperBound(Double ub, PiCluster cluster) {
            ubDist.put(ub, cluster);
        }

        public PiCluster getNearestClusterByLb() {
            Double minLbKey = Collections.min(lbDist.keySet());
            return lbDist.get(minLbKey);
        }

        public PiCluster getNearestClusterByUb() {
            Double minUbKey = Collections.min(ubDist.keySet());
            return ubDist.get(minUbKey);
        }

        public Double getLbDist() {
            List<Double> l = new ArrayList(lbDist.keySet());
            Collections.sort(l);
            Double lb0 = l.get(0);
            Double lb1 = l.get(1);
            return Math.abs(lb0 - lb1);
        }

        public Double getUbDist() {
            List<Double> l = new ArrayList(ubDist.keySet());
            Collections.sort(l);
            Double ub0 = l.get(0);
            Double ub1 = l.get(1);
            return Math.abs(ub0 - ub1);
        }
    }


    /**
     *
     * @param bin
     * @param seeds
     */
    public void clusterBin(PiBin bin, ArrayList<PiCluster> seeds) {
        //  assign bin to a cluster if it hasn't been assigned to any cluster yet
        if (bin.getCluster() == null) {

            BoundManager bm = new BoundManager();

            for (PiCluster seed : seeds) {
                double lb = bin.lowerBound(seed);
                double ub = bin.upperBound(seed);
                bm.putLowerBound(lb, seed);
                bm.putUpperBound(ub, seed);
            }

            PiCluster cLb = bm.getNearestClusterByLb();
            PiCluster cUb = bm.getNearestClusterByUb();


            //if (minUbKey >= 2*bin.getSize() && minLbKey >= 2* bin.getSize() && cLb == cUb) {
            //if (minUbKey >= 2*bin.getSize() && minLbKey >= 2* bin.getSize() && cLb == cUb) {
            if (cLb == cUb && bm.getLbDist() > 2*bin.getSize()) {
                // assing the cluster if it is possible
                log.info("Assign bin " + bin.getZoo() + " on layer " +
                        bin.getLayer() + " to cluster " + cLb.getId());
                bin.setCluster(cLb);
            } else {
                // System.out.println("Not possible...");
                for (PiBin llBin : bin.getChildBins()) {
                    clusterBin(llBin, seeds);
                }
            }
            /*else {
                // if it is not, then process the lower level bins
                for (PiBin llBin : bin.getChildBins()) {
                    clusterBin(llBin, seeds);
                }
            }*/
        }
    }

    /**
     * @return
     */
    public MiningObject run() {
        log.info("Initializing the pyramid...");
        prepareData();
        min[0] = 0;
        min[1] = 0;
        max[0] = Math.pow(2, 16) - 1;
        max[1] = Math.pow(2, 16) - 1;

        int initialLayer = 1;

        timer.indexStart();

        PiCube picube = new PiCube(16);
        picube.build(data);

        ArrayList<PiCluster> seeds = new ArrayList<>();

        /*
        for (int i = 0; i < k; i++) {
            PiCluster randomPoint = PiCluster.random(min, max);
            seeds.add(randomPoint);
        }*/

        PiCluster s1 = new PiCluster(new double[]{10000, 50000});
        s1.setId(0);
        seeds.add(s1);
        PiCluster s2 = new PiCluster(new double[]{20000, 46000});
        s2.setId(1);
        seeds.add(s2);
        PiCluster s3 = new PiCluster(new double[]{38500, 50000});
        s3.setId(2);
        seeds.add(s3);

        PiCluster s4 = new PiCluster(new double[]{52000, 31500});
        s4.setId(3);
        seeds.add(s4);
        // select the initial layer
        HashMap<Long, PiBin> layer = picube.getLayer(initialLayer);

        // do the clustering!
        for (Map.Entry<Long, PiBin> layerEntry : layer.entrySet()) {
            PiBin bin = layerEntry.getValue();
            this.clusterBin(bin, seeds);
        }

        layer = picube.getLayer(initialLayer);
        Dump.toFile(Utils.clusteredLayerToString(seeds, layer), "layer" + 4 + "clust.csv");

        log.info("Number of bins: " + layer.entrySet().size());

        timer.indexEnd();

        log.info("Clustering started...");

        timer.clusteringStart();

        // Dumping results to a file(s) and/or plotting results.
        if (dump()) {
            String logFileName = Dump.getLogFileName(PiKMeansAlgorithmSettings.NAME,
                    getPhysicalDataSet().getDescription(), getDescription());

            log.info("Writing results to " + logFileName);
            // writeResults();
            Dump.toFile(data, logFileName + ".csv", true);
        }

        if (plot()) {
            log.info("Plotting results...");
        }

        timer.clusteringEnd();

        basicMiningObject.setDescription(timer.getLog());
        return basicMiningObject;
    }

    @Override
    public String getDescription() {
        return "(k=" + k + ", mi=" + maxIterations + ")";
    }
}
