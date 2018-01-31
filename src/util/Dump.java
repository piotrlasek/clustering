package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.model.IClusteringObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;



public class Dump {

    private final static Logger log = LogManager.getLogger(Dump.class.getSimpleName());

    /**
     *
     * @param string
     * @param fName
     */
    public static void toFile(String string, String fName) {
        String filePath = Workspace.getWorkspacePath() + "/results/" + fName;
        log.info("Saving results to: " + filePath);
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            writer.write(string);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param Dataset
     * @param fName
     * @param ignoreNoise
     */
    public static void toFile(Collection<IClusteringObject> Dataset, String fName, boolean ignoreNoise) {
        String filePath = Workspace.getWorkspacePath() + "/results/" + fName;
        log.info("Saving results to: " + filePath);
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            for (IClusteringObject ico : Dataset) {
                double[] coord = ico.getSpatialObject().getValues();
                int clusterId = ico.getClusterInfo().getClusterId();

                if (ignoreNoise && clusterId < 0)
                    continue;

                String toSave = Arrays.toString(coord);
                toSave = toSave.replace("[", "").replace("]", "");
                toSave += ", " + clusterId;
                writer.write(toSave+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param Dataset
     * @param fName
     * @param ignoreNoise
     */
    public static void toFile(ArrayList<double[]> Dataset, String fName, boolean ignoreNoise) {
        String filePath = Workspace.getWorkspacePath() + "/results/" + fName;
        log.info("Saving results to: " + filePath);
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            for (double[] ico : Dataset) {
                String toSave = "" + (long) ico[0] + ", " + (long) ico[1];

                writer.write(toSave+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param algorithmName
     * @param fileName
     * @param parameters
     * @return
     */
    public static String getLogFileName(String algorithmName, String fileName, String parameters) {
        String fileNameNoExt = fileName.split("\\.")[0];
        String dumpFileName = fileNameNoExt + "_" + algorithmName + "_" + parameters;
        return dumpFileName;
    }

    /**
     *
     * @param description
     */
    public static void saveTimes(String description) {
        String filePath = Workspace.getWorkspacePath() + "/results/times.txt";
        try {
            FileWriter fw = new FileWriter(filePath, true);
            fw.write(description + "\n");
            fw.close();
        } catch(IOException ioe) {
            log.error("IOException: " + ioe.getMessage());
        }
        log.info("Times appended to: " + filePath);
    }
}
