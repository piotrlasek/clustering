package org.dmtools.clustering.old;

import org.dmtools.clustering.model.ISpatialObject;

import java.util.ArrayList;

public class BasicSpatialObject implements ISpatialObject {
	
	private double[] coordinates;
	private int[] parentCellCoordinates;
	private long approximation;
	private int value;
	private Cell cell;

	public BasicSpatialObject(double[] coordinates) {
		this.coordinates = coordinates;
		value = -1;
	}

	public void setApproximation(Long approximation) {
		this.approximation = approximation;
	}

	public Long getApproximation() {
		return this.approximation;
	}

	public int getClusterId() {
		return value;
	}
	
	public void setClusterId(int clusterId)
	{
	    this.value = clusterId;
	}

	public double[] getValues() {
		return coordinates;
	}

	public int[] getCellCoordinates() {
		return this.parentCellCoordinates;
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < this.coordinates.length; i++) {
			if (i == 0) {
				s += "(" + coordinates[i];
			} else if (i == this.coordinates.length - 1) {
				s += "," + coordinates[i] + ")";
			} else {
				s += "," + coordinates[i];
			}
		}
		return s;
	}

	/**
	 * 
	 * @param values
	 */
	public void setValues(double[] values) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
     * 
     */
	public void setParentCellCoordinates(int[] coordinates) {
		parentCellCoordinates = coordinates;
	}

	/**
	 * 
	 * @return
	 */
	public int[] getParentCellCoordinates() {
		return parentCellCoordinates;
	}

	/**
	 * 
	 * @param value
	 */
	public void setValue(Object value) {
	    this.value = new Integer(value.toString());
	}

	/**
	 * 
	 * @param k
	 * @return
	 */
	public ArrayList<ISpatialObject> getNeighbors(int k) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public Cell getParentCell() {
	    return cell;
	}

    @Override
    public void setParentCell(Cell c) {
        cell = c;
        
    }

}
