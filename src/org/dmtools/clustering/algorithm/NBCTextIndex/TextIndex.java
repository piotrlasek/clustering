package org.dmtools.clustering.algorithm.NBCTextIndex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TextIndex {
	
	ArrayList<TextObject> dataset;
	
	public TextIndex() {
		dataset = new ArrayList<TextObject>();
	}
	
	public void readFile(String path, String measure) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(path));
	    try {
	        StringBuilder sb = new StringBuilder();
	        // pomijamy lini� "Autorzy:"
	        
	        String line = br.readLine();
	        line = br.readLine();
	        
	        while (!line.contains("Lider:")) {
	        	String[] names = line.split(", ");
	        	
	        	for(String name:names) {
	        		TextObject to = new TextObject();
	        		to.setId(name);
	        		to.index = dataset.size();
	        		dataset.add(to);
	        	}
	        	//System.out.println(line);
	        	line = br.readLine();
	        }
	        
	        line = br.readLine();
	        line = br.readLine();
	        line = br.readLine();
	        line = br.readLine();
	        line = br.readLine();
	        	
	        int[][] matrix = new int[dataset.size()][dataset.size()];
	        
	        int row = 0;
	        while(line != null) {
	        	
	        	String[] elements = line.split(" ");
	        	int column = 0;
	        	for(String element:elements) {
	        		Integer v = new Integer(element);
	        		matrix[column][row] = v;
	        		column++;
	        	}
	        	row++;	   
	        	line = br.readLine();
	        }
	        
	        int index = 0;
	        for(TextObject to:dataset) {
	        	int column = index;
	        	
	        	ArrayList<Integer> Nu = new ArrayList<Integer>();
	        	for(int r = 0; r < dataset.size(); r++) {
	        		if(matrix[column][r] != 0) {
	        			Nu.add(r);
	        		}
	        	}
	        	
	        	// szukamy s�siad�w
	        	for(int c = 0; c < dataset.size(); c++) {
	        		if (c != column) {
	        			ArrayList<Integer> Nv = new ArrayList<Integer>();
	        			
	        			for(int r = 0; r < dataset.size(); r++) {
	    	        		if(matrix[c][r] != 0) {
	    	        			Nv.add(r);
	    	        		}
	    	        	}
	        			
	        			int symDiff = getSymmetricDifference(Nu, Nv);
	        			// int D = ???
	        			int sumNuNv1 = getSum(Nu, Nv);
	        			int sumNuNv2 = Nu.size() + Nv.size();

	        			double nominator = 0;
	        			double denominator = 0;
	        			
	        			// d1
	        			// double nominator = (double) symDiff;
	        			// double denominator = (double) D;
	        			
	        			if (measure.equals("d2")) {
		        			// d2
		        			nominator = (double) symDiff;
		        			denominator = (double) sumNuNv1;
	        			} else if (measure.equals("d3")) {
	        				// d3
	        				nominator = (double) symDiff;
	        				denominator = (double) sumNuNv2;
	        			} else throw new Exception("Incorrect measure.");
	        					
	        			// d4
	        			// double nominator = (double) symDiff;
	        			// double denominator = (double) sumNuNv2;

	        			if (denominator != 0) {
	        				
		        			double d = (double) nominator / (double) denominator;
		        			double s = 1 - d;
		        			
		        			if (s != 0) {    				

			        			TextObject objectV = dataset.get(c);
			        			if (objectV.id.contains("Gao Jian-Ming")) {
			        				System.out.println("xx");
			        			}
			        			
			        			to.addNeighbor(objectV, s);
		        			}
	        			}
	        		}
	        	}
	        	
	        	index++;
	        	if (index % 100 == 0) System.out.println(index + " / " + dataset.size());
	        	//System.out.println("abc");
	        }
	        	
	       
	        String everything = sb.toString();
	    } finally {
	        br.close();
	    }
		
	}
	
	/**
	 * 
	 * @param path
	 * @throws Exception
	 */
	public void readNeighbors(String path) throws Exception {
		
		// read text objects
		BufferedReader br = new BufferedReader(new FileReader(path));
	    try {
	        
	        String line = br.readLine();

	        while(line != null) {
     	
	        	String[] elements = line.split("#");
	        	String[] tos = elements[0].split("\\|");
	        	
	        	TextObject to = new TextObject();
	        	to.id = tos[0];
	        	to.index = new Integer(tos[1]);
	        	
	        	dataset.add(to);
	        	
	        	line = br.readLine();
	        }
	    } finally {
	        br.close();
	    }
	    
	    
	    // read neighbors
	    br = new BufferedReader(new FileReader(path));
	    try {
	        
	        String line = br.readLine();

	        int index = 0;
	        while(line != null) {
     	
	        	String[] elements = line.split("#");
	        	String[] tos = elements[0].split("\\|");
	        	
	        	TextObject to = dataset.get(index++);
	        	
	        	ArrayList<TextObject> neighbors = new ArrayList<TextObject>();
	        	ArrayList<Double> distances = new ArrayList<Double>();
	        	
	        	for(int i = 1; i < elements.length; i++) {
	        		String ob = elements[i];
	        		String[] obs = ob.split("\\|");
	        		
	        		int neighborIndex = new Integer(obs[1]);
	        		
	        		TextObject t = dataset.get(neighborIndex);
	        		t.id  = obs[0];
	        		t.index = neighborIndex;
	        		Double d = new Double(obs[2]);
	        		neighbors.add(t);
	        		distances.add(d);	        		
	        	}
	        	
	        	to.neighbors = neighbors;
	        	to.distances = distances;
	        	
	        	line = br.readLine();
	        }
	    } finally {
	        br.close();
	    }
	}
	
	/**
	 * 
	 * @param Nu
	 * @param Nv
	 * @return
	 */
	public int getSymmetricDifference(ArrayList<Integer> A, ArrayList<Integer> B) {
		int sd = 0;
		
		ArrayList<Integer> A_B = new ArrayList<Integer>();
		ArrayList<Integer> B_A = new ArrayList<Integer>();
		ArrayList<Integer> A_B$B_A = new ArrayList<Integer>();
		
		A_B.addAll(A);
		B_A.addAll(B);
		
		for(Integer a:A) {
			if (B.contains(a)) {
				A_B.remove(a);
			}
		}
		
		for(Integer b:B) {
			if (A.contains(b)) {
				B_A.remove(b);
			}
		}
		 
		A_B$B_A.addAll(A_B);
		A_B$B_A.addAll(B_A);
		
		/*for(Integer i:A_B$B_A) {
			System.out.print(i + " ");
		}*/
		
		return A_B$B_A.size();
	}
	
	/**
	 * 
	 * @param Nu
	 * @param Nv
	 * @return
	 */
	public int getSum(ArrayList<Integer> A, ArrayList<Integer> B) {
		int sd = 0;
		
		ArrayList<Integer> A_B = new ArrayList<Integer>();
		
		A_B.addAll(A);
		
		for(Integer b:B) {
			if (!A_B.contains(b)) {
				A_B.add(b);
			}
		}
		
		return A_B.size();
	}
	
	/**
	 * 
	 * @param Nu
	 * @param Nv
	 * @return
	 */
	public int getD(int Nu, int Nv) {
		return Math.max(Nu, Nv);
	}
	
	/**
	 * 
	 * @param c
	 * @param i
	 * @return
	 */
	public int getN(int[] c, int i) {
		int N = 0;
		int index = 0;
		for (int n:c) {
			if (index != i) {
				n += c[index];
			}
			index++;
		}
		return N;
	}
	
	/**
	 * 
	 */
	public void testSymmetricDifference()
	{
		ArrayList<Integer> A = new ArrayList<Integer>();
		ArrayList<Integer> B = new ArrayList<Integer>();
		A.add(1); A.add(2); A.add(3);
		B.add(2); B.add(4);
		
		int x = getSymmetricDifference(A, B);
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void readCooperators(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		//dataset.clear();
	    try {
	        StringBuilder sb = new StringBuilder();
	        // pomijamy lini� "Autorzy:"
	        
	        String line = br.readLine();
	        line = br.readLine();
	        
	        while (!line.contains("Lider:")) {
	        	String[] names = line.split(", ");
	        	
	        	for(String name:names) {
	        		TextObject to = new TextObject();
	        		to.setId(name);
	        		to.index = dataset.size();
	        		dataset.add(to);
	        	}
	        	//System.out.println(line);
	        	line = br.readLine();
	        }
	        
	        line = br.readLine();
	        line = br.readLine();
	        line = br.readLine();
	        line = br.readLine();
	        line = br.readLine();
	        		        
	        int row = 0;
	        while(line != null) {
	        	
	        	String[] elements = line.split(" ");
	        	int column = 0;
	        	for(String element:elements) {
	        		TextObject to = dataset.get(column);
	        		Integer v = new Integer(element);
	        		if (v != 0 && column != row) {
		        		TextObject co = dataset.get(row);
	        			to.cooperators.add(co);
	        		}	        		
	        		column++;
	        	}
	        	row++;	   
	        	line = br.readLine();
	        }
	        
	    } finally {
	        br.close();
	    }	
	}	
	
	
	/**
	 * 
	 * @param path
	 */
	public void writeResult(String path) {
		
	    try {
	    	//What ever the file path is.
			File file = new File(path);
			BufferedWriter w = new BufferedWriter(new FileWriter(file));
	    	       		
			int index = 0;
			for(TextObject to:dataset) {
				w.write("" + to.id);
				w.write("|");
				w.write("" + to.index);
				w.write("#");
				
				for(int i = 0; i < to.neighbors.size(); i++) {
					TextObject n = to.neighbors.get(i);
					double dis = to.distances.get(i);
					w.write("" + n.id);
					w.write("|");
					w.write("" + n.index);
					w.write("|");
					w.write("" + dis);
					w.write("#");
				}
				
				w.write("\n");
				index++;
		    }
			
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createIndex(String file, String measure) {
		
		TextIndex ti = new TextIndex();
		
		String path = NBCTextIndex.WORKSPACE + file + ".grf";
		
	    try {
			ti.readFile(path, measure);			
			ti.writeResult(NBCTextIndex.WORKSPACE + "output-"+measure+"-" + file + ".txt");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void cluster(String algorithm, String measure, String file) {
		TextIndex ti = new TextIndex();
		NBCTextIndex nbc = new NBCTextIndex(13, ti);		
		DBSCANTextIndex dbscan = new DBSCANTextIndex(0.3, 5	, ti);
		
		try {
			ti.readNeighbors(NBCTextIndex.WORKSPACE + "output-" + measure + "-" + file + ".txt");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		System.out.println("START");
		
		if (algorithm.equals("NBC")) {
			nbc.run();
		} else if (algorithm.equals("DBSCAN")) {
			dbscan.run();	    
		}
		
		try {
			ti.readCooperators(NBCTextIndex.WORKSPACE + file + ".grf");
			
			if (algorithm.equals("NBC")) {
				nbc.writeGroups(algorithm+"-"+measure+ "-"+ file);
			} else if (algorithm.equals("DBSCAN")) {
				dbscan.writeGroups(algorithm+"-"+measure + "-" + file);	    
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("END");
	}
	
	public static void main(String[] args) {
		
		String file = "RSDSI2014";
		//String file = "RSDSII2014";
		String measure = "d3";
		
		//createIndex(file, measure);
		
		cluster("NBC", measure, file);
		//cluster("DBSCAN", measure, file);
	    
	}	
}
