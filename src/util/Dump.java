package util;

import org.dmtools.clustering.algorithm.NBC.NBCRTreePoint;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;



public class Dump {
    public static void toFile(ArrayList Dataset) {
        String fileName = "/home/user/workspace/edu/clustering/data/experiment/out.txt";
        ListIterator li = Dataset.listIterator();
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);
            while (li.hasNext()) {
                NBCRTreePoint p = (NBCRTreePoint) li.next();
                writer.write(p.toString()+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
