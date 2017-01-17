package util;

import org.dmtools.clustering.algorithm.NBC.NBCRTreePoint;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.StringTokenizer;


public class Dump {
    public static void toFile(ArrayList Dataset) {
        String fileName = "/home/user/workspace/edu/clustering/data/experiment/out.txt";
        ListIterator li = Dataset.listIterator();
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);
            while (li.hasNext()) {
                NBCRTreePoint p = (NBCRTreePoint) li.next();
                String line = p.toString();
                
                ArrayList<String> result = new ArrayList<String>();
                StringTokenizer t = new StringTokenizer(line, "(): group,", true);
                while (t.hasMoreTokens()) {
                    String token = t.nextToken();
                    result.add(token);

                }
                String toSave = (result.get(1)+","+result.get(4)+"\t"+result.get(17));
                writer.write(toSave+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
