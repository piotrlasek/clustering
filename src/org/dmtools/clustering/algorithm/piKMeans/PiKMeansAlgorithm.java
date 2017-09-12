package org.dmtools.clustering.algorithm.piKMeans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.old.ClusteringTimer;

import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalDataSet;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Author: Nasim Razavi
 * Date: September 8, 2017
 */
public class PiKMeansAlgorithm extends CDMBasicClusteringAlgorithm {

	private String dataFolder;
	HashMap<String, ArrayList<double[]>> pyramid;
	int k;
	int maxIterations;

	int allAssigned = 0;
	int startingStratum;
	int deepest;
	int depth;

	static double[] min = new double[15];
	static double[] max = new double[15];
	static ArrayList<double[]> data;
	static ArrayList<double[]> centerPoints;
	static ArrayList<ArrayList<double[]>> centerPointsLists = new ArrayList<ArrayList<double[]>>();
	;
	static ArrayList<double[]> base = new ArrayList<double[]>();
	static int numberOfDimensions = 15;

	ArrayList<ArrayList<double[]>> layeredData; // = new ArrayList<ArrayList<double[]>>();

	ClusteringTimer timer = new ClusteringTimer(PiKMeansAlgorithmSettings.NAME);

	final String COMMA_DELIMITER = ",";
	final String NEW_LINE_SEPARATOR = "\n";

	protected final static Logger log = LogManager.getLogger(PiKMeansAlgorithm.class.getSimpleName());

	/**
	 * @throws IOException
	 */
	public PiKMeansAlgorithm(ClusteringSettings clusteringSettings,
							 PhysicalDataSet physicalDataSet) {
		super(clusteringSettings, physicalDataSet);

		PiKMeansAlgorithmSettings pkmas =
				(PiKMeansAlgorithmSettings) clusteringSettings.getAlgorithmSettings();

		k = pkmas.getK();
		maxIterations = pkmas.getMaxIterations();
		startingStratum = pkmas.getStarting();
		deepest = pkmas.getDeepest();
		depth = pkmas.getDepth();
		dataFolder = physicalDataSet.getDescription();
	}

	/**
	 * @return
	 */
	public MiningObject run() {
		int current = startingStratum;
		ArrayList<double[]> labeledData;
		ArrayList<double[]> mixedBins;
		initializeTemporaryPoints();
		centerPointsLists.add(centerPoints);

		timer.setParameters(getDescription());

		log.info("Initializing pyramid...");
		timer.indexStart();
		initializePyramid();
		timer.indexEnd();

		data = loadData("stratum"+ startingStratum +".txt");

		timer.setParameters("k = " + k);

		log.info("Clustering started...");
		timer.clusteringStart();
		// Performing k-means iterations from 0 to r.
		for (int r = 0; r < maxIterations; r++) {
			layeredData = new ArrayList<ArrayList<double[]>>();
			mixedBins = new ArrayList<double[]>();
			labeledData = new ArrayList<double[]>();
			for (double[] point : data) {
				int indexOfClosestTempPoint = 0;
				indexOfClosestTempPoint = getIndexOfClosestCluster(point);
				point[15] = indexOfClosestTempPoint;
				//add to mixedPoints if index is -1
				if (indexOfClosestTempPoint == -1) {
					mixedBins.add(point);
				} else {
					labeledData.add(point);
				}
			}

			if (labeledData.size() != 0) {
				layeredData.add(labeledData);
			}

			if (mixedBins.size() == 0) {
				allAssigned = 1;
			} else {
				mineHigherResData(current, deepest, mixedBins);
			}

			updateTemporaryPoints();
		}


		// Mapping results so that the clusters could be plotted.
		log.info("Mapping results...");

		//read each layer and map it on the base
		base = loadData("base.txt");

		for (ArrayList<double[]> layer : layeredData) {
			int index = 0;

			for (double[] p : layer) {
				long OldZOrder = (long) p[10];
				int shiftBase = (depth - (int) p[14]);
				long postFix = ((long) Math.pow(2, (shiftBase * 2))) - 1;
				long zOrderShifted = OldZOrder << (shiftBase * 2);
				long lowZOrder = zOrderShifted | 0L;
				long highZOrder = zOrderShifted | postFix;

				boolean processed = false;
				while (index < base.size()) {
					//System.out.println("index: "+ index);
					while ((long) base.get(index)[10] >= lowZOrder && (long) base.get(index)[10] <= highZOrder && index < base.size()) {
						base.get(index)[15] = p[15];

						//testData.add(base.get(index));
						index++;
						processed = true;
						if (index >= base.size()) {
							break;
						}
					}
					if (processed) {
						break;
					} else {
						index++;
					}
				}
			}
		}

		timer.clusteringEnd();

		// Dumping results to a file(s) and/or plotting results.

		if (dump()) {
			log.info("Writing results...");
			writeResults();
		}

		if (plot()) {
			log.info("Plotting results...");
            /*Points points1 = new Points(base,centerPoints, 1, 15); //15 deepest
            JFrame frameFrame1 = new JFrame("Points");
            frameFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameFrame1.add(points1);
            frameFrame1.setSize(1000, 1000);
            //frame.setLocationRelativeTo(null);
            frameFrame1.setVisible(true);
            frameFrame1.toFront();*/
			//System.out.println("****base size: "+ base.size());
			//System.out.println("****data size: "+ data.size());
		}

		basicMiningObject.setDescription(timer.getLog());
		return basicMiningObject;
	}

	/**
	 *
	 */
	protected void writeResults() {
		try {
			FileWriter fileWriterData = new FileWriter("data" + ".txt");
			FileWriter fileWriterCenter = new FileWriter("center_points" + ".txt");
			int num = 0;
			//System.out.println("****base size: "+ base.size());
			for (double[] point : base) {
				num++;
				fileWriterData.append(String.valueOf(point[0]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[1]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[2]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[3]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[4]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[5]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[6]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[7]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[8]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[9]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[10]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[11]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[12]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[13]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[14]));
				fileWriterData.append(COMMA_DELIMITER);
				fileWriterData.append(String.valueOf(point[15]));
				fileWriterData.append(NEW_LINE_SEPARATOR);
			}

			//System.out.println("****num: "+ num);
			fileWriterData.flush();
			fileWriterData.close();

			for (double[] point : centerPoints) {
				num++;
				fileWriterCenter.append(String.valueOf(point[0]));
				fileWriterCenter.append(COMMA_DELIMITER);
				fileWriterCenter.append(String.valueOf(point[1]));
				//fileWriterCenter.append(COMMA_DELIMITER);
				//fileWriterCenter.append(String.valueOf(point[3]));
				fileWriterCenter.append(NEW_LINE_SEPARATOR);
			}

			fileWriterCenter.flush();
			fileWriterCenter.close();
		} catch (IOException ioex) {
			log.error(ioex);
		}
	}

	/**
	 *
	 */
	private void initializePyramid() {
		if (pyramid == null) {
			pyramid = new HashMap<>();

			try {
				loadData("base.txt");
				for (int i = 0; i < 15; i++) {
					loadData("stratum" + i + ".txt");
				}

			} catch (Exception e) {
				log.error(e);
			}
		}
	}

	/**
	 *
	 * @param fileName
	 * @return
	 */
	private ArrayList<double[]> loadData(String fileName) {

		ArrayList<double[]> stratum = pyramid.get(fileName);

		try {
			if (stratum == null) {
				double[] point;
				ArrayList<double[]> rawData = new ArrayList<double[]>();

				FileInputStream fstream;
				BufferedReader dataset;

				String strLine;

				String[] attribute;

				fstream = new FileInputStream(dataFolder + fileName);
				dataset = new BufferedReader(new InputStreamReader(fstream));

				ArrayList<String> strLines = new ArrayList<String>();
				int d = 0;
				int i = 0;
				//Read File Line By Line
				while ((strLine = dataset.readLine()) != null) {
					double[] record = new double[numberOfDimensions + 1];
					strLines.add(strLine);
					attribute = strLine.split(",");
					record[0] = Double.parseDouble(attribute[0]);
					record[1] = Double.parseDouble(attribute[1]);
					record[2] = Double.parseDouble(attribute[2]);
					record[3] = Double.parseDouble(attribute[3]);
					record[4] = Double.parseDouble(attribute[4]);
					record[5] = Double.parseDouble(attribute[5]);
					record[6] = Double.parseDouble(attribute[6]);
					record[7] = Double.parseDouble(attribute[7]);
					record[8] = Double.parseDouble(attribute[8]);
					record[9] = Double.parseDouble(attribute[9]);
					record[10] = Double.parseDouble(attribute[10]);
					record[11] = Double.parseDouble(attribute[11]);
					record[12] = Double.parseDouble(attribute[12]);
					record[13] = Double.parseDouble(attribute[13]);
					record[14] = Double.parseDouble(attribute[14]);

					if (i == 0) {
						for (d = 0; d < 15; d++) {
							min[d] = record[d];
							max[d] = record[d];
						}
						i++;
					}
					for (d = 0; d < 15; d++) {
						if (min[d] > record[d]) min[d] = record[d];
						if (max[d] < record[d]) max[d] = record[d];
					}

					rawData.add(record);
				}

				dataset.close();
				stratum = rawData;
				pyramid.put(fileName, stratum);
			}
		} catch (Exception e) {
			log.error(e);
		}

		return stratum;
	}

	/**
	 *
	 */
	private void initializeTemporaryPoints() {
		centerPoints = new ArrayList<>();
		for (int i = 0; i < k; i++) {
			double[] tempPoint = new double[16];

			for (int j = 0; j < 2; j++) {
				tempPoint[j] = randomFromRange(j);
			}
			centerPoints.add(tempPoint);
		}
	}

	private double randomFromRange(int index) {
		return min[index] + (Math.random() * (max[index] - min[index]));
	}

	/**
	 * @param point
	 * @return
	 */
	private int getIndexOfClosestCluster(double[] point) {
		int adjacent = 0;
		double binSize;
		ArrayList<Double> lowBoundDists = new ArrayList();
		ArrayList<Double> highBoundDists = new ArrayList();
		double[] lowerPoint = new double[2];
		double[] upperPoint = new double[2];
		double[] farthestDist = new double[4];
		double[] borderPoint = new double[2];
		ArrayList<double[]> test = new ArrayList<>();

		test.add(point);

		double midPoint;

		int indexOfClosestTempPoint = 0;
		int minLowIndex = 0;
		int minHighIndex = 0;
		int maxLowIndex = 0;
		int assigned = 0;

		for (int i = 0; i < k; i++) {
			double[] ti = centerPoints.get(i);
			lowerPoint[0] = point[2];
			lowerPoint[1] = point[3];
			upperPoint[0] = point[8];
			upperPoint[1] = point[9];
			binSize = dist(lowerPoint, upperPoint);

			if (point[13] > 1) {
				if ((point[6] <= ti[0] && point[8] >= ti[0]) && point[7] >= ti[1]) { //The bin is at North
					lowerPoint[0] = ti[0];
					lowerPoint[1] = point[7];
					midPoint = (point[8] - point[6]) / 2;
					if (ti[0] <= midPoint) {
						//NE edge point
						upperPoint[0] = point[4];
						upperPoint[1] = point[5];
					} else {
						//NW edge point
						upperPoint[0] = point[2];
						upperPoint[1] = point[3];
					}
				} else if ((point[2] <= ti[0] && point[4] >= ti[0]) && point[3] <= ti[1]) { //The bin is at South
					lowerPoint[0] = ti[0];
					lowerPoint[1] = point[3];
					midPoint = (double) (point[4] - point[2]) / 2;
					if (ti[0] <= midPoint) {
						//SE edge point
						upperPoint[0] = point[8];
						upperPoint[1] = point[9];
					} else {
						//SW edge point
						upperPoint[0] = point[6];
						upperPoint[1] = point[7];
					}
				} else if (point[4] <= ti[0] && (point[5] >= ti[1] && point[9] <= ti[1])) { //The bin is at West
					lowerPoint[0] = point[4];
					lowerPoint[1] = ti[1];
					midPoint = (double) (point[9] - point[5]) / 2;
					if (ti[1] <= midPoint) {
						//NW edge point
						upperPoint[0] = point[2];
						upperPoint[1] = point[3];
					} else {
						//SW edge point
						upperPoint[0] = point[6];
						upperPoint[1] = point[7];
					}
				} else if (point[2] >= ti[0] && (point[3] >= ti[1] && point[7] <= ti[1])) { //The bin is at East
					lowerPoint[0] = point[2];
					lowerPoint[1] = ti[1];
					midPoint = (double) (point[7] - point[3]) / 2;
					if (ti[1] <= midPoint) {
						//NE edge point
						upperPoint[0] = point[4];
						upperPoint[1] = point[5];
					} else {
						//SW edge point
						upperPoint[0] = point[8];
						upperPoint[1] = point[9];
					}
				} else if (point[8] <= ti[0] && point[9] >= ti[1]) { //point is a NW neighbor
					lowerPoint[0] = point[8];
					lowerPoint[1] = point[9];
					upperPoint[0] = point[2];
					upperPoint[1] = point[3];
				} else if (point[6] >= ti[0] && point[7] >= ti[1]) { //point is a NE neighbor
					lowerPoint[0] = point[6];
					lowerPoint[1] = point[7];
					upperPoint[0] = point[4];
					upperPoint[1] = point[5];
				} else if (point[4] <= ti[0] && point[5] <= ti[1]) { //point is a SW neighbor
					lowerPoint[0] = point[4];
					lowerPoint[1] = point[5];
					upperPoint[0] = point[6];
					upperPoint[1] = point[7];
				} else if (point[2] >= ti[0] && point[3] <= ti[1]) { //point is a SE neighbor
					lowerPoint[0] = point[2];
					lowerPoint[1] = point[3];
					upperPoint[0] = point[8];
					upperPoint[1] = point[9];
				} else if (ti[0] >= point[2] && ti[0] <= point[4] && ti[1] >= point[7] && ti[1] <= point[3]) {
					//NW point
					borderPoint[0] = point[2];
					borderPoint[1] = point[3];
					farthestDist[0] = dist(borderPoint, ti);
					//NE point
					borderPoint[0] = point[4];
					borderPoint[1] = point[5];
					farthestDist[1] = dist(borderPoint, ti);
					//SW point
					borderPoint[0] = point[6];
					borderPoint[1] = point[7];
					farthestDist[2] = dist(borderPoint, ti);
					//SE point
					borderPoint[0] = point[8];
					borderPoint[1] = point[9];
					farthestDist[3] = dist(borderPoint, ti);

					double maxDist = farthestDist[0];
					int indexPoint = 0;
					int d = 0;
					for (i = 0; i < 4; i++) {
						if (farthestDist[i] > maxDist) {
							maxDist = farthestDist[i];
							indexPoint = i;
						}
					}

					upperPoint[0] = point[(2 * indexPoint) + 2];
					upperPoint[1] = point[(2 * indexPoint) + 3];

					double minDist = farthestDist[0];
					indexPoint = 0;
					for (i = 0; i < 4; i++) {
						if (farthestDist[i] < minDist) {
							minDist = farthestDist[i];
							indexPoint = i;
						}
					}

					lowerPoint[0] = point[(2 * indexPoint) + 2];
					lowerPoint[1] = point[(2 * indexPoint) + 3];

				} else {
					assigned = -1;
				}
			} else {
				lowerPoint[0] = point[0];
				lowerPoint[1] = point[1];
				upperPoint[0] = point[0];
				upperPoint[1] = point[1];
			}
			//dists.add(dist(point, ti));
			if (dist(lowerPoint, ti) <= binSize) {
				adjacent++;
			}
			lowBoundDists.add(dist(lowerPoint, ti));
			highBoundDists.add(dist(upperPoint, ti));
		}

		// TODO:
		Double minLowDist = Collections.min(lowBoundDists);
		Double maxLowDist = Collections.max(lowBoundDists);
		Double minHighDist = Collections.min(highBoundDists);
		minLowIndex = lowBoundDists.indexOf(minLowDist);
		maxLowIndex = lowBoundDists.indexOf(maxLowDist);
		minHighIndex = highBoundDists.indexOf(minHighDist);

		if (assigned == 0) {
			if (adjacent < 2) {
				if (minLowIndex == minHighIndex) { //both lower and upper distances agree on one cluster
					indexOfClosestTempPoint = minHighIndex;

				} else {
					indexOfClosestTempPoint = -1;
				}
			} else {
				//we need to go one stratum down for bin point
				indexOfClosestTempPoint = -1; //label of possibly mixed bins
			}
		} else {
			indexOfClosestTempPoint = -1;
		}

		return indexOfClosestTempPoint;
	}

	/**
	 * @param a
	 * @param b
	 * @return
	 */
	public double dist(double[] a, double[] b) {
		double temp = 0;

		//for(int i = 0; i < Math.min(a.length, b.length); i++)
		for (int i = 0; i < 2; i++) { //find the distance based on only coordinates
			temp += Math.pow(a[i] - b[i], 2);
		}

		return Math.sqrt(temp);
	}


	/**
	 * For getting data from deeper strata.
	 *
	 * @param current
	 * @param deepest
	 * @param mixedData
	 */
	void mineHigherResData(int current, int deepest, ArrayList<double[]> mixedData) {
		ArrayList<double[]> highrResData = new ArrayList<double[]>();
		ArrayList<double[]> currentMixeddata = new ArrayList<double[]>();
		ArrayList<double[]> labeledData = new ArrayList<double[]>();
		ArrayList<double[]> test = new ArrayList<double[]>();
		long OldZOrder;
		long zOrderShifted;
		long lowZOrder, highZOrder;
		int indexOfClosestTempPoint;
		boolean processed = false;

		if (current == deepest) { // there is no more level to process
			return;
		} else {
			current++;
			highrResData = loadData("stratum" + current + ".txt");

			//read all underlying bins
			int index = 0;

			for (double[] point : mixedData) {
				OldZOrder = (long) point[10];
				zOrderShifted = (OldZOrder << 2);
				lowZOrder = zOrderShifted | 0L;
				highZOrder = zOrderShifted | 3L;

				while (index < highrResData.size()) {
					processed = false;
					while ((long) highrResData.get(index)[10] >= lowZOrder && (long) highrResData.get(index)[10] <= highZOrder && index < highrResData.size()) {
						//System.out.println("low_zorder: "+highrResData.get(index)[10]);
						indexOfClosestTempPoint = getIndexOfClosestCluster(highrResData.get(index));
						highrResData.get(index)[15] = indexOfClosestTempPoint;
						test.add(highrResData.get(index));
						if (indexOfClosestTempPoint == -1) {
							currentMixeddata.add(highrResData.get(index));
						} else {
							labeledData.add(highrResData.get(index));
							//testData.add(highrResData.get(index));
						}
						index++;
						if (index >= highrResData.size()) {
							processed = true;
							break;
						}
						processed = true;
					}
					if (processed) {
						break;
					} else {
						index++;
					}
				}


			}

			if (labeledData.size() != 0) {
				layeredData.add(labeledData);
			}

			if (currentMixeddata.size() > 0) {
				//for(double[] point: currentMixeddata)
				//System.out.println("zOrderRRRRRRRRRR: "+point[10]);
				mineHigherResData(current, deepest, currentMixeddata);
				//mixedBins.add(currentMixeddata);
			}

            /*Points points1 = new Points(test,centerPoints, 0);
			JFrame frameFrame1 = new JFrame("Points");
            frameFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameFrame1.add(points1);
            frameFrame1.setSize(1000, 1000);
            //frame.setLocationRelativeTo(null);
            frameFrame1.setVisible(true);
            frameFrame1.toFront();*/
		}

	}

	/**
	 * Updating cluster centers.
	 */
	private void updateTemporaryPoints() {
		//updateClusterCount();
		ArrayList<double[]> centerPointsBackup = new ArrayList<>(centerPoints);
		zeroTemporaryPoints();

		//process points in different layers if their label is not -1
		//ArrayList<double[]> tempPoints;
		double[][] totalPoint = new double[k][2];
		double[] countPoint = new double[k];

		for (int i = 0; i < k; i++) {
			totalPoint[i][0] = 0;
			totalPoint[i][1] = 0;
			countPoint[i] = 0;
		}

		for (int i = 0; i < layeredData.size(); i++) {
			for (double[] point : layeredData.get(i)) {
				int clusterId = (int) point[15];
				for (int d = 0; d < 2; d++) {
					//System.out.println("pintX: "+point[d]);
					//if(clusterId > 3 || clusterId < 0){
					//System.out.println("wrong..............");
					//}
					//System.out.println("custId: "+clusterId);
					totalPoint[clusterId][d] += point[d];
					//System.out.println("d: "+d);
				}
				countPoint[clusterId]++;
			}
		}

		for (int i = 0; i < k; i++) {
			if (countPoint[i] != 0) {
				for (int j = 0; j < 2; j++) {
					centerPoints.get(i)[j] = totalPoint[i][j] / countPoint[i];
				}
			} else {
				centerPoints.get(i)[0] = centerPointsBackup.get(i)[0];
				centerPoints.get(i)[1] = centerPointsBackup.get(i)[1];
			}
		}
	}

	/**
	 * Resetting center points.
	 */
	void zeroTemporaryPoints() {
		for (double[] point : centerPoints) {
			for (int i = 0; i < centerPoints.size(); i++) {
				for (int j = 0; j < numberOfDimensions + 1; j++) {
					centerPoints.get(i)[j] = 0;
				}
			}
		}
		
		/*for(int i = 0; i < centerPoints.size(); i++){
			System.out.println("tmp x: "+centerPoints.get(i)[0]+" y: "+centerPoints.get(i)[1]);
		}*/
	}

	@Override
	public String getDescription() {
		return "(k=" + k + ", mi=" + maxIterations + ")";
	}
}
