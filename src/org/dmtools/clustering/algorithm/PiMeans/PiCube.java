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

    public static int maxDepth = 16;
    public static int dim = 2;

    ArrayList<HashMap<Long, PiBin>> layers;

    private final static Logger log = LogManager.getLogger(PiCube.class.getSimpleName());

    /**
     *
     * @param maxDepth
     */
    public PiCube(int maxDepth) {
        layers = new ArrayList<>(PiCube.maxDepth);
        for(int i = 0; i < PiCube.maxDepth; i++) {
            layers.add(new HashMap<>());
        }
    }

    /**
     *
     * @param data
     */
    public void build(ArrayList<double[]> data) {
        log.info("Building a pi-cube.");

        // TODO: Check if baseLevel can be equal to maxDepth.
        int baseLevel = PiCube.maxDepth;

        // base layer
        HashMap<Long, PiBin> baseLayer = layers.get(baseLevel-1);

        for(double[] record : data) {
            Long originalZoo = Morton2D.encode((long) record[0], (long) record[1]);
            PiBin bin;

            if (!baseLayer.containsKey(originalZoo)) {
                bin = new PiBin(baseLevel, originalZoo);
            } else {
                bin = baseLayer.get(originalZoo);
            }

            PiPoint piPoint = new PiPoint(record, bin);
            bin.addPoint(piPoint);
            baseLayer.put(originalZoo, bin);
        }

        // higher layers
        for(int level = baseLevel; level > 1; level--) {
            int higherLevel = level-1;
            HashMap<Long, PiBin> layer = layers.get(level - 1);
            HashMap<Long, PiBin> hlLayer = layers.get(higherLevel - 1);

            for(Map.Entry<Long, PiBin> pair : layer.entrySet()) {
                PiBin bin = pair.getValue();
                Long originalZoo = bin.getZoo();

                Long hlZoo = (long) Morton2D.zooAtLevelA(PiCube.dim, baseLevel, higherLevel, originalZoo);
                PiBin hlBin;

                if (!hlLayer.containsKey(hlZoo)) {
                    hlBin = new PiBin(higherLevel, originalZoo);
                } else {
                    hlBin = hlLayer.get(hlZoo);
                }

                hlBin.addChild(bin);
                bin.setParent(hlBin);
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