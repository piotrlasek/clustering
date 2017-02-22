package util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class GenConstraints {
    private static ArrayList<ArrayList<String>> getConstraints(String name) {
        String filePath = "/home/user/workspace/edu/clustering/data/experiment/" + name;
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
        String fileName = "/home/user/workspace/edu/clustering/data/experiment/" + name;
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName, true);
            writer.write(line + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void forBrich1() {
        ArrayList<ArrayList<String>> list = getConstraints("constraintsBrich1Unprocessed.txt");
        for (int i = 0; i < list.get(0).size(); i++) {
            if(i <= 89) {
                String p0 = list.get(0).get(i);
                String p1 = list.get(0).get(i + 10);
                String line = p0 + "\t" + p1 + "\t0";
                toFile(line, "constraintsBrich1.txt");
            }

            if(i%10 < 9) {
                String p0 = list.get(0).get(i);
                String p1 = list.get(0).get(i + 1);
                String line = p0 + "\t" + p1 + "\t0";
                toFile(line, "constraintsBrich1.txt");
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
//        forBrich1();
//        forBrich2();
//        forBrich3();
    }
}

