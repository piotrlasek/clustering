package org.dmtools.clustering.evaluator;

import util.Distance;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Piotr on 26.05.2017.
 */
public class SilhouetteCoefficient {

    private final ArrayList<Integer> labels;
    private final ArrayList<double[]> data;

    public class Cluster {
        public int id;
        public ArrayList<double[]> points;
        int count;
        double[] sum;
        double[] mean;
        Cluster nearestCluster;

        public Cluster() {
            points = new ArrayList();

        }

        /**
         *
         * @param p
         */
        public void addPoint(double[] p) {
            points.add(p);
            if (sum == null) {
                sum = new double[p.length];
            }

            for(int i = 0; i < sum.length; i++) {
                sum[i] += p[i];
            }

            count += 1;
        }

        public double[] getMean() {
            if (mean == null) {
                mean = new double[sum.length];
                for(int i = 0; i < mean.length; i++) {
                    mean[i] = sum[i] / count;
                }
            }
            return mean;
        }

        public void setNearestCluster(Cluster nearestCluster) {
            this.nearestCluster = nearestCluster;
        }

        public ArrayList<double[]> getPoints() {
            return points;
        }

        public Cluster getNearestCluster() {
            return nearestCluster;
        }
    }

    public void determineNearestClusters() {
        for(Integer clusterId : clusterMap.keySet()) {
            double dist = 0;
            Cluster currentCluster = clusterMap.get(clusterId);
            Cluster nearestCluster = null;
            double[] mean1 = currentCluster.getMean();
            for(Integer clusterId2 : clusterMap.keySet()) {
                if (clusterId == clusterId2)
                    continue;

                Cluster cluster2 = clusterMap.get(clusterId2);
                double[] mean2 = cluster2.getMean();
                double ndist = Distance.Distance2(mean1, mean2);
                if (nearestCluster == null) {
                    dist = ndist;
                    nearestCluster = cluster2;
                } else {
                    if (dist < ndist) {
                        dist = ndist;
                        nearestCluster = cluster2;
                    }
                }

            }
            currentCluster.setNearestCluster(nearestCluster);
        }



    }

    /**
     *
     * @return
     */
    public float compute() {
        float sc = 0;

        for (int i = 0; i < data.size(); i++) {
            double[] point = data.get(i);
            int label = labels.get(i);

            sc += singleSilhouetteCoefficient(point, label);
        }

        sc /= data.size();

        return sc;
    }


    HashMap<Integer, Cluster> clusterMap;

    /**
     *
     * @param data
     * @param labels
     */
    SilhouetteCoefficient(ArrayList<double[]> data, ArrayList<Integer> labels) {
        clusterMap = new HashMap<>();
        this.data = data;
        this.labels = labels;

        for(int i = 0; i < labels.size(); i++) {
           double[] p = data.get(i);
           Integer l = labels.get(i);
           Cluster clusterInfo = clusterMap.get(l);
           if (clusterInfo == null) {
               clusterInfo = new Cluster();
               clusterMap.put(l, clusterInfo);
           }
           clusterInfo.addPoint(p);
        }

        determineNearestClusters();

    }

    public double singleSilhouetteCoefficient(double[] point, int label) {
        double ssc = 0;

        Cluster cluster = clusterMap.get(label);
        ArrayList<double[]> points = cluster.getPoints();
        double a = getMeanValue(point, points, true);

        Cluster nearestCluster = cluster.getNearestCluster();
        ArrayList<double[]> nearestClusterPoints = nearestCluster.getPoints();
        double b = getMeanValue(point, nearestClusterPoints, false);

        ssc = (b - a) / Math.max(a, b);

        return ssc;
    }

    private double getMeanValue(double[] point, ArrayList<double[]> clusterPoints, boolean sameCluster) {
        double mean = 0;
        int count = clusterPoints.size();
        if (sameCluster) {
            count -= 1;
        }
        for (double[] p : clusterPoints) {
            mean += Math.sqrt(Distance.Distance2(point, p));
        }
        mean /= count;
        return mean;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        ArrayList<double[]> data = new ArrayList();
        int dimensions = 5;
        ArrayList<Integer> labels = new ArrayList();
        for (int i = 0; i < 1000; i++) {
            double[] point = new double[dimensions];
            for (int d = 0; d < dimensions; d++) {
                point[d] = (int) (Math.random() * 100);
            }
            data.add(point);    // generates a d dimensional data point
            labels.add((int) (Math.random() * 10)); // generates 10 clusters
        }

        SilhouetteCoefficient sc = new SilhouetteCoefficient(data, labels);
        float scv = sc.compute();

        System.out.println("Silhouette coefficient: " + scv);
    }
}
