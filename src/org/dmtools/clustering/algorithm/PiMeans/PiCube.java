package org.dmtools.clustering.algorithm.PiMeans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Piotr Lasek on 20.09.2017.
 */
public class PiCube {

    int maxDepth;
    ArrayList<HashMap<Long, PiBin>> layers;

    private final static Logger log = LogManager.getLogger(PiCube.class.getSimpleName());

    /**
     *
     * @param maxDepth
     */
    public PiCube(int maxDepth) {
        this.maxDepth = maxDepth;
        layers = new ArrayList<>(maxDepth);
        for(int i = 0; i < maxDepth; i++) {
            layers.add(new HashMap<>());
        }
    }

    /**
     *
     * @param data
     */
    public void build(ArrayList<double[]> data, double[] min, double[] max) {
        log.info("Building a pi-cube.");
        HashMap<Long, PiBin> baseLayer = layers.get(maxDepth - 1);

        for(double[] record : data) {
            Long zoo = Morton2D.encode((long) record[0], (long) record[1]);
            PiBin bin = null;

            if (!baseLayer.containsKey(zoo)) {
                // TODO:
                //źle ustawiane są wartości min i max
                bin = new PiBin(0, /*min, max, */maxDepth);
            } else {
                bin = baseLayer.get(zoo);
            }

            bin.setZoo(zoo);
            PiPoint piPoint = new PiPoint(record, bin);
            bin.addPoint(piPoint);
            baseLayer.put(zoo, bin);
        }

        for(int level = maxDepth-1; level > 0; level--) {
            HashMap<Long, PiBin> layer = layers.get(level);
            HashMap<Long, PiBin> hlLayer = layers.get(level-1);

            if (level == 10) {
               System.out.println("abc");
            }
            for(Map.Entry<Long, PiBin> pair : layer.entrySet()) {
                PiBin bin = pair.getValue();
                Long originalZoo = bin.getZoo();

                Long hlZoo = (long) Morton2D.zooAtLevelA(2, maxDepth, level-1, originalZoo);
                PiBin hlBin;

                if (!hlLayer.containsKey(hlZoo)) {
                    // TODO
                    // tutaj tez min i max sa zle ustawiane
                    // nalezy to jakos uspojnic
                    hlBin = new PiBin(bin.getPointsCount(), /*min, max,*/ level);
                } else {
                    hlBin = hlLayer.get(hlZoo);
                }

                hlBin.addChild(bin);
                bin.setParent(hlBin);
                hlBin.setZoo(originalZoo);

                long[] xy = Morton2D.decode(hlZoo);
                hlBin.setX((int) xy[0]);
                hlBin.setY((int) xy[1]);

                hlLayer.put(hlZoo, hlBin);
            }
        }

        log.info("Done.");
    }

    /**
     *
     * @param layer
     * @return
     */
    public HashMap<Long, PiBin> getLayer(int layer) {
        return layers.get(layer);
    }


    /**
     *
     * @param args
     */
    public static void main(String[] args) {

    }
}