package org.dmtools.clustering.algorithm.PiMeans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.algorithm.piKMeans.PiKMeansAlgorithmSettings;
import org.dmtools.clustering.model.IClusteringData;
import org.dmtools.clustering.old.ClusteringTimer;
import org.dmtools.datamining.data.CDMFilePhysicalDataSet;
import util.Dump;

import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalAttribute;
import javax.datamining.data.PhysicalDataSet;
import java.io.IOException;
import java.util.*;

/**
 * Author: Nasim Razavi, Piotr Lasek
 * Date: September 8, 2017
 *
 * TODO: 1. Prepare datasets
 *       2. Run experiments
 *       3. Start writing the paper
 */
public class PiMeansAlgorithm extends CDMBasicClusteringAlgorithm {

    int k;
    int maxIterations;
    int startingStratum;
    int depth;

    ClusteringTimer timer = new ClusteringTimer(PiMeansAlgorithmSettings.NAME);

    protected final static Logger log = LogManager.getLogger(
            PiMeansAlgorithm.class.getSimpleName());

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

    /**
     *
     */
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
    public void clusterBin(PiBin bin, ArrayList<PiCluster> seeds, int run) {
        BoundManager bm = new BoundManager();

        for (PiCluster seed : seeds) {
            double lb = bin.lowerBound(seed);
            double ub = bin.upperBound(seed);
            bm.putLowerBound(lb, seed);
            bm.putUpperBound(ub, seed);
        }

        PiCluster cLb = bm.getNearestClusterByLb();
        PiCluster cUb = bm.getNearestClusterByUb();

        // TODO: check the second condition and prove it.
        if (cLb == cUb && bm.getLbDist() > 2*bin.getSize()) {
            // assign the bin to the cluster if possible
            /*log.info("Assign bin " + bin.getZoo() + " on layer " +
                    bin.getLayer() + " to cluster " + cLb.getId());*/
            bin.setClusters(cLb, run);
            cLb.addPoints(bin.getPoints());
        } else {
            // process lower level bins
            if (bin.getChildBins() != null)
            for (PiBin llBin : bin.getChildBins()) {
                if (llBin != null)
                    clusterBin(llBin, seeds, run);
            }
        }
    }

    /**
     *
     */
    /*public void updateSeeds(PiBin bin, HashMap<Integer, double[]> coords, HashMap<Integer, Integer> count, int run) {
       if (bin.getClusters(run) != null) {
           Integer cId = bin.getClusters(run).getId();
           double[] coo = null;
           Integer cnt = null;

           if (coords.containsKey(cId)) {
              coo = coords.get(cId);
           } else {
               coo = new double[2];
           }

           if (count.containsKey(cId)) {
               cnt = count.get(cId);
           } else {
               cnt = 0;
           }

           coo[0] += bin.getCentreSum()[0];
           coo[1] += bin.getCentreSum()[1];
           cnt += bin.getPointsCount();

           coords.put(cId, coo);
           count.put(cId, cnt);
       } else {
           for(PiBin b : bin.getChildBins()) {
               updateSeeds(b, coords, count, run);
           }
       }
    }*/

    /**
     *
     */
    @Override
    public IClusteringData prepareData() {
        ArrayList<Object[]> rawData =
                ((CDMFilePhysicalDataSet) getPhysicalDataSet()).getData();

        Collection<PhysicalAttribute> attributes = null;

        data = new ArrayList();

        int i = 0;

        try {
            attributes = getPhysicalDataSet().getAttributes();

            for (Object[] rawRecord : rawData) {
                double[] record = new double[attributes.size() + 1];
                int d = 0;
                for (PhysicalAttribute attribute : attributes) {
                    record[d] = new Double(rawData.get(i)[d].toString());
                    if (min[d] == 0)
                        min[d] = record[d];
                    else {
                        if (min[d] > record[d]) min[d] = record[d];
                        if (max[d] < record[d]) max[d] = record[d];
                    }
                    d++;
                }
                record[d] = -1; // UNCLUSTERED
                data.add(record);
                i++;
            }

        } catch (Exception e) {
            log.error("An error occured at record number: " + i);
            log.error(e.getStackTrace());
            System.exit(0);
        }

        return null;
    }
    /**
     * @return
     */
    public MiningObject run() {
        timer.setParameters("(k=" + k + ", mi=" + maxIterations + ")");
        log.info("Reading data...");
        prepareData();
        min[0] = 0;
        min[1] = 0;
        max[0] = Math.pow(2, 16) - 1;
        max[1] = Math.pow(2, 16) - 1;

        int initialLayer = 0;

        // building the pi-cube
        timer.indexStart();

        log.info("Building the pi-cube...");
        PiCube picube = new PiCube(16);
        picube.build(data);

        timer.indexEnd();

        ArrayList<PiCluster> seeds = new ArrayList<>();

        // select the initial layer
        HashMap<Long, PiBin> layer = picube.getLayer(initialLayer);

        initializeSeeds(seeds);

        log.info("Clustering started...");

        timer.clusteringStart();
        for (int run = 0; run < maxIterations; run++) {

            // do the first iteration of clustering
            for (Map.Entry<Long, PiBin> layerEntry : layer.entrySet()) {
                PiBin bin = layerEntry.getValue();
                this.clusterBin(bin, seeds, run);
            }

            if (!(run == maxIterations - 1))
                updateSeeds3(seeds/*, run, layer*/);
        }
        timer.clusteringEnd();

        log.info("Finished.");

        // Dumping results to a file(s) and/or plotting results.
        if (dump()) {
            String logFileName = Dump.getLogFileName(PiKMeansAlgorithmSettings.NAME,
                    getPhysicalDataSet().getDescription(), getDescription());

            log.info("Writing results to " + logFileName);

            // dumping bins
            layer = picube.getLayer(initialLayer);

            for (int i = 0; i < maxIterations; i++) {
                Dump.toFile(Utils.clusteredLayerToString(seeds, layer, i),
                        logFileName + "_iter_" + i + ".csv");
            }

            // dumping the rest...
            Dump.toFile(data, logFileName + ".csv", true);
        }

        basicMiningObject.setDescription(timer.getLog());
        return basicMiningObject;
    }

        /**
         *
         */
    private void initializeSeeds(ArrayList<PiCluster> seeds) {

        for (int i = 0; i < k; i++) {
            int clusterId = PiCluster.nextId;
            PiCluster randomPoint = PiCluster.random(min, max);
            seeds.add(clusterId, randomPoint);
        }

        /*PiCluster s1 = new PiCluster(new double[]{10000, 50000});
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
        seeds.add(s4);*/

        /*
        PiCluster s1 = new PiCluster(new double[]{11714, 47249});
        s1.setId(0);
        seeds.add(s1);
        PiCluster s2 = new PiCluster(new double[]{17550, 46485});
        s2.setId(1);
        seeds.add(s2);
        PiCluster s3 = new PiCluster(new double[]{33960, 48401});
        s3.setId(2);
        seeds.add(s3);
        PiCluster s4 = new PiCluster(new double[]{57785, 44348});
        s4.setId(3);
        seeds.add(s4);
        */
    }

    /**
     *
     */
    private void updateSeeds3(ArrayList<PiCluster> seeds) {

        for (PiCluster c : seeds) {
            double[] ncoord = c.coordinates;
            double cnt = 0;
            if (c.getPoints() != null) {
                for (PiPoint p : c.getPoints()) {
                    double[] coo = p.coordinates;
                    ncoord[0] += coo[0];
                    ncoord[1] += coo[1];
                }
                cnt = c.getPoints().size();
            }
            ncoord[0] /= cnt;
            ncoord[1] /= cnt;
            c.setCoordinates(ncoord);
            c.clear();
        }
    }

    /**
     *
     * @param seeds
     */
    private void updateSeeds(ArrayList<PiCluster> seeds) {
        HashMap<Integer, double[]> coords = new HashMap<>();
        HashMap<Integer, Integer> count = new HashMap<>();

        for (PiCluster c : seeds) {
            coords.put(c.getId(), new double[2]);
            count.put(c.getId(), 0);
        }

        for (PiCluster c : seeds) {
            Integer id = c.getId();
            if (c.getPoints() != null)
            for (PiPoint p : c.getPoints()) {
                double[] coo = coords.get(id);
                Integer cou = count.get(id);
                coo[0] += p.coordinates[0];
                coo[1] += p.coordinates[1];
                cou++;
                coords.put(id, coo);
                count.put(id, cou);
            }
        }

        for (Integer cId : coords.keySet()) {
            double[] co = coords.get(cId);
            Integer cn = count.get(cId);
            co[0] /= cn;
            co[1] /= cn;
            coords.put(cId, co);
        }

        seeds.clear();

        for (Integer cId : coords.keySet()) {
            double[] c = coords.get(cId);
            PiCluster s = new PiCluster(c);
            s.setId(cId);
            seeds.add(s);
            //log.info(s);
        }
    }

    @Override
    public String getDescription() {
        return "(k=" + k + ", mi=" + maxIterations + ")";
    }
}
