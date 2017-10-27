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
    public void build(ArrayList<double[]> data) {
        log.info("Building a picube.");
        HashMap<Long, PiBin> baseLayer = layers.get(maxDepth - 1);

        for(double[] record : data) {
            Long zoo = Morton2D.encode((long) record[0], (long) record[1]);
            PiBin bin = new PiBin(1);
            if (!baseLayer.containsKey(zoo)) {
                baseLayer.put(zoo, bin);
            } else {
                PiBin existingBin = baseLayer.get(zoo);
                existingBin.increasePointsCount(1);
                baseLayer.put(zoo, bin);
            }
        }

        for(int level = maxDepth-1; level > 0; level--) {
            HashMap<Long, PiBin> layer = layers.get(level);
            HashMap<Long, PiBin> hlLayer = layers.get(level-1);

            for(Map.Entry<Long, PiBin> pair : layer.entrySet()) {
                Long zoo = pair.getKey();
                PiBin bin = pair.getValue();

                Long hlZoo = (long) Morton2D.zooAtLevelA(2, maxDepth, level-1, zoo);
                PiBin hlBin = null;

                if (!hlLayer.containsKey(hlZoo)) {
                    hlBin = new PiBin(bin.getPointsCount());
                } else {
                    hlBin = hlLayer.get(hlZoo);
                }
                hlBin.addChild(bin);

                long[] xy = Morton2D.decode(hlZoo);
                // hlBin

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