package util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class GenConstraints {
    private static ArrayList<ArrayList<String>> getConstraints(String name) {
        String filePath = Workspace.getWorkspacePath() + "/data/experiment/" + name;
        Scanner s = null;
        try {
            s = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        ArrayList<String> cannotLinkPointArray = new ArrayList<String>();
        ArrayList<String> mustLinkPointArray = new ArrayList<String>();

        while (s.hasNextLine()) {
            String oneLine = s.nextLine();
            if (oneLine.equals("0")) {
                while (s.hasNext()) {
                    oneLine = s.nextLine();
                    if (oneLine.equals("1") || oneLine.equals("")) {
                        break;
                    } else {
                        cannotLinkPointArray.add(oneLine);
                    }
                }
                list.add(cannotLinkPointArray);
            }

            if (oneLine.equals("")) {
                oneLine = s.nextLine();
            }
            if (oneLine.equals("1")) {
                while (s.hasNext()) {
                    oneLine = s.nextLine();
                    mustLinkPointArray.add(oneLine);
                }
                list.add(mustLinkPointArray);
            }
        }
        s.close();

        return list;
    }


    private static void toFile(String line, String name) {

        String fileName = Workspace.getWorkspacePath() + "/data/experiment/" + name;
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName, true);
            writer.write(line + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private static void forBrich1() {

        // left-top to bottom-right
        ArrayList<ArrayList<String>> list = getConstraints("constraintsBirch1-Unprocessed-A.txt");
        ArrayList<String> cannotLinkConstraints = list.get(0);
        int size = cannotLinkConstraints.size();

        for (int i = 0; i < size; i++) {
            String p00 = cannotLinkConstraints.get(i);

            if ((i+1) % 20 != 0) {
                String p10 = cannotLinkConstraints.get(i + 1);
                String line = p00 + "\t" + p10 + "\t0";
                line = p00 + "\t" + p10 + "\t0";
                toFile(line, "constraintsbrich1.txt");
            }

//            if (i + 1 <= 90) {
//                String p01 = cannotLinkConstraints.get(i + 10);
//                String line = p00 + "\t" + p01 + "\t0";
//                toFile(line, "constraintsBrich1.txt");
//            }
//
//            if ((i+1) % 10 != 0) {
//                string p10 = cannotlinkconstraints.get(i + 1);
//                string line = p00 + "\t" + p10 + "\t0";
//                line = p00 + "\t" + p10 + "\t0";
//                tofile(line, "constraintsbrich1.txt");
//            }
        }
        // right-top to bottom-left


        list = getConstraints("constraintsBirch1-Unprocessed-B.txt");
        cannotLinkConstraints = list.get(0);
        size = cannotLinkConstraints.size();

        for (int i = 0; i < size; i++) {
            String p00 = cannotLinkConstraints.get(i);

            if ((i + 1) % 20 != 0) {
                String p10 = cannotLinkConstraints.get(i + 1);
                String line = p00 + "\t" + p10 + "\t0";
                line = p00 + "\t" + p10 + "\t0";
                toFile(line, "constraintsbrich1.txt");
            }
        }
    }

    private static void forBrich2() {
        ArrayList<ArrayList<String>> list = getConstraints("constraintsBrich2Unprocessed.txt");
        for (int i = 0; i < list.get(0).size(); i++) {
            String p0 = list.get(0).get(i);
            String p1 = list.get(0).get(i + 1);
            String line = p0 + "\t" + p1 + "\t0";
            toFile(line, "constraintsBrich2.txt");
            i++;
        }
    }

    private static void forBrich3() {
        ArrayList<ArrayList<String>> list = getConstraints("constraintsBrich3Unprocessed.txt");
        for (int i = 0; i < list.get(0).size(); i++) {
            String p0 = list.get(0).get(i);
            String p1 = list.get(0).get(i + 1);
            String line = p0 + "\t" + p1 + "\t0";
            toFile(line, "constraintsBrich3.txt");
            i++;
        }
        for (int i = 0; i < list.get(1).size(); i++) {
            String p0 = list.get(1).get(i);
            String p1 = list.get(1).get(i + 1);
            String line = p0 + "\t" + p1 + "\t1";
            toFile(line, "constraintsBrich3.txt");
            i++;
        }
    }

    public static void main(String[] args) {
        forBrich1();
//        forBrich2();
//        forBrich3();
    }
}

