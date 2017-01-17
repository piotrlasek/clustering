package util;


import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.algorithm.CNBC.CNBCRTreePoint;
import org.dmtools.clustering.algorithm.CNBC.InstanceConstraints;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SetConstraints {

    private static ArrayList<ArrayList<String>> getConstraints() {
        String filePath = "/home/user/workspace/edu/clustering/data/experiment/constraints.txt";
        Scanner s = null;
        try {
            s = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        while (s.hasNextLine()){
            String oneLine = s.nextLine();
            String line[] = oneLine.split("\t");
            ArrayList<String> oneLineArray= new ArrayList<String>();
            oneLineArray.add(line[0]);
            oneLineArray.add(line[1]);
            oneLineArray.add(line[2]);

            list.add(oneLineArray);
        }
        s.close();
        return list;
    }

    public static void forCDBSCAN(InstanceConstraints ic){
        ArrayList<ArrayList<String>> list = getConstraints();
        for(int i = 0; i < list.size(); i++) {
            ArrayList<String> currentLine = list.get(i);

            String firstPoint[] = currentLine.get(0).split(",");
            double firstPointX = Double.parseDouble(firstPoint[0]);
            double firstPointY = Double.parseDouble(firstPoint[1]);

            String secondPoint[] = currentLine.get(1).split(",");
            double secondPointX = Double.parseDouble(secondPoint[0]);
            double secondPointY = Double.parseDouble(secondPoint[1]);

            int type = Integer.parseInt(currentLine.get(2));

// ERROR InstanceConstraints Points in ic,add
//             if ( type == 0) {
//                 ic.add(new double[] { firstPointX, firstPointY }, new double[] { secondPointX, secondPointY }, false);
//             } else if (type == 1) {
//                 ic.add(new double[] { firstPointX, firstPointY }, new double[] { secondPointX, secondPointY }, true);
//             } else {
//                 System.out.println("error when set constraints");
//             }
        }
    }


    public static void forCNBC(ArrayList Dataset, InstanceConstraints ic){
        CNBCRTreePoint p0, p1;
        ArrayList<ArrayList<String>> list = getConstraints();
        for(int i = 0; i < list.size(); i++){
            ArrayList<String> currentLine = list.get(i);

            String firstPoint[] = currentLine.get(0).split(",");
            double firstPointX = Double.parseDouble(firstPoint[0]);
            double firstPointY = Double.parseDouble(firstPoint[1]);

            String secondPoint[] = currentLine.get(1).split(",");
            double secondPointX = Double.parseDouble(secondPoint[0]);
            double secondPointY = Double.parseDouble(secondPoint[1]);

            int type = Integer.parseInt(currentLine.get(2));

            if ( type == 0) {
                p0 = new CNBCRTreePoint(new double[]{firstPointX, firstPointY}, CDMCluster.UNCLASSIFIED);
                p1 = new CNBCRTreePoint(new double[]{secondPointX, secondPointY}, CDMCluster.UNCLASSIFIED);

                p0 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p0));
                p1 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p1));

                ic.addCannotLinkPoints(p0, p1);
            } else if (type == 1) {
                p0 = new CNBCRTreePoint(new double[]{firstPointX, firstPointY}, CDMCluster.UNCLASSIFIED);
                p1 = new CNBCRTreePoint(new double[]{secondPointX, secondPointY}, CDMCluster.UNCLASSIFIED);

                p0 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p0));
                p1 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p1));

                ic.addMustLinkPoints(p0, p1);
            } else {
                System.out.println("error when set constraints");
            }
        }
    }

}
