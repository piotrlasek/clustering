package org.dmtools.clustering.NBCTextIndex;

import java.util.ArrayList;

public class TextObject {
	
	int clst_no = -1;
	String id;
	double ndf = -1;
	
	private int SizeOfkNB;
	private int SizeOfRkNB;
	boolean cand;
	double dist;
	int index = 0;
	
	ArrayList<TextObject> neighbors;
	ArrayList<Double> distances;
	ArrayList<TextObject> cooperators;
	

	public boolean equals(Object object){
		if(object instanceof TextObject && ((TextObject)object).id == this.id) {
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

	public TextObject() {
		neighbors = new ArrayList<TextObject>();
		distances = new ArrayList<Double>();
		cooperators = new ArrayList<TextObject>();
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @param k
	 * @return
	 */
	public ArrayList<TextObject> getNeighbors(int k) {
		int count = 0;
		ArrayList<TextObject> kNN = new ArrayList<TextObject>();
		double dist = 0;
		
		if (neighbors.size() != 0) {
			
			for(int i = 0; i < neighbors.size(); i++) {
				TextObject to = neighbors.get(i);
				double ndist = distances.get(i);
				
				if ((count >= k && ndist < dist) ||  ndist <= 0) {
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
		
		return kNN;
	}
	
	public void addNeighbor(TextObject to, double dist) {
		int i = 0;
		for(i = 0; i < neighbors.size(); i++) {
			TextObject t = neighbors.get(i);
			Double d = distances.get(i);
			if (d <= dist) {
				break;
			}
		}
		neighbors.add(i, to);
		distances.add(i, dist);
	}

}
