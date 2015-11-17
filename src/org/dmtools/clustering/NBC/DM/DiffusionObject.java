package org.dmtools.clustering.NBC.DM;

import java.util.ArrayList;

public class DiffusionObject {

	
	int clst_no = -1;
	String id;
	int id_number;
	double ndf = -1;
	
	private int SizeOfkNB;
	private int SizeOfRkNB;
	boolean cand;
	double dist;
	int index = 0;
	
	double[] m_pCoords;
	
	ArrayList<DiffusionObject> neighbors;
	ArrayList<Double> distances;
	ArrayList<DiffusionObject> cooperators;
	
	public void setCoords(double [] coord)
	{
		this.m_pCoords = new double[coord.length];
		
		int i = 0;
		for(double val : coord)
		{
			this.m_pCoords[i] = val;
			i++;
		}
	}
	
	

	public boolean equals(Object object){
		if(object instanceof DiffusionObject && ((DiffusionObject)object).id == this.id) {
		    return true;
		} else {
		    return false;
		}
	}
	
	public void increaseSizeOfRkNB() {
		SizeOfRkNB = SizeOfRkNB + 1;
	}

	public int getSizeOfRkNB() {
		return SizeOfRkNB;
	}

	public void setSizeOfkNB(int size) {
		SizeOfkNB = size;
	}

	public int getSizeOfkNB() {
		return SizeOfkNB;
	}

	public DiffusionObject() {
		neighbors = new ArrayList<DiffusionObject>();
		distances = new ArrayList<Double>();
		cooperators = new ArrayList<DiffusionObject>();
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setId_Number(int i){
		this.id_number = i;
	}
	
	/**
	 * 
	 * @param k
	 * @return
	 */
	public ArrayList<DiffusionObject> getNeighbors(int k) {
		int count = 0;
		ArrayList<DiffusionObject> kNN = new ArrayList<DiffusionObject>();
		double dist = 0;
		//System.out.println("obj.id =" + id + "; neighbors.size()" + neighbors.size());
		if (neighbors.size() != 0) {
			
			for(int i = neighbors.size() -1 ; i >= 0; i--) {
				DiffusionObject to = neighbors.get(i);
				double ndist = distances.get(i);
				
				if ((count >= k && ndist > dist)) {
					break;
				} else {
					if (to.clst_no < 0) {
						kNN.add(to);
						dist = ndist;
						count++;
					}
				}
			}
		}
		/*
		System.out.println("obj.id =" + id + "; size=" + neighbors.size());
    	int ind =0;
    	for (DiffusionObject obj : kNN)
    	{
    		System.out.print("id= " + obj.id +  "| ");
    		ind++;
    	}
    	System.out.println(" + ");/**/
    	
		return kNN;		
	}
	
	public ArrayList<DiffusionObject> getNeighbors2(int k) {
		int count = 0;
		ArrayList<DiffusionObject> kNN = new ArrayList<DiffusionObject>();
		double dist = 0;
		//System.out.println("obj.id =" + id + "; neighbors.size()" + neighbors.size());
		if (neighbors.size() != 0) {
			
			for(int i = neighbors.size() -1 ; i >= 0; i--) {
				DiffusionObject to = neighbors.get(i);
				double ndist = distances.get(i);
				
				if ((count >= k && ndist > dist)) {
					break;
				} else {
					kNN.add(to);
					dist = ndist;
					count++;					
				}
			}
		}
		/*
		System.out.println("obj.id =" + id + "; size=" + neighbors.size());
    	int ind =0;
    	for (DiffusionObject obj : kNN)
    	{
    		System.out.print("id= " + obj.id +  "| ");
    		ind++;
    	}
    	System.out.println(" + ");/**/
    	
		return kNN;		
	}
	
	public void addNeighbor(DiffusionObject to, double dist) {
		int i = 0;
		for(i = 0; i < neighbors.size(); i++) {
			DiffusionObject t = neighbors.get(i);
			Double d = distances.get(i);
			if (d <= dist) {
				break;
			}
		}
		neighbors.add(i, to);
		distances.add(i, dist);
	}
}
