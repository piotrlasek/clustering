package org.dmtools.clustering.algorithm.DBSCAN;

import lvaindex.vafile.Cell;
import lvaindex.vafile.ISpatialObject;

import java.util.ArrayList;

/**
 * Created by Piotr on 17.11.2015.
 */

    public class BasicSpatialObject implements ISpatialObject {
        private double[] coordinates;
        private int[] parentCellCoordinates;
        private long approximation;
        private int value;
        private Cell cell;

        public BasicSpatialObject(double[] coordinates) {
            this.coordinates = coordinates;
            this.value = -1;
        }

        public void setApproximation(Long approximation) {
            this.approximation = approximation.longValue();
        }

        public Long getApproximation() {
            return Long.valueOf(this.approximation);
        }

        public Integer getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public double[] getCoordinates() {
            return this.coordinates;
        }

        public int[] getCellCoordinates() {
            return this.parentCellCoordinates;
        }

        public String toString() {
            String s = "";

            for(int i = 0; i < this.coordinates.length; ++i) {
                if(i == 0) {
                    s = s + "(" + this.coordinates[i];
                } else if(i == this.coordinates.length - 1) {
                    s = s + "," + this.coordinates[i] + ")";
                } else {
                    s = s + "," + this.coordinates[i];
                }
            }

            return s;
        }

        public void setCoordinates(double[] coordinates) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setParentCellCoordinates(int[] coordinates) {
            this.parentCellCoordinates = coordinates;
        }

        public int[] getParentCellCoordinates() {
            return this.parentCellCoordinates;
        }

        public void setValue(Object value) {
            this.value = (new Integer(value.toString())).intValue();
        }

        public ArrayList<ISpatialObject> getNeighbors(int k) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Cell getParentCell() {
            return this.cell;
        }

        public void setParentCell(Cell c) {
            this.cell = c;
        }
    }
