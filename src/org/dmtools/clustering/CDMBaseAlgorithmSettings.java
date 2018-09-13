package org.dmtools.clustering;

/**
 * Created by Piotr Lasek on 01.06.2017.
 */
public class CDMBaseAlgorithmSettings {
    private boolean plot = false;
    private boolean dump = false;
    private boolean closePlot = false;


    public boolean plot() {
        return plot;
    }

    public void setPlot(boolean plot) {
        this.plot = plot;
    }

    public boolean dump() {
        return dump;
    }

    public void setDump(boolean dump) {
        this.dump = dump;
    }

    public boolean closePlot() {
        return closePlot;
    }

    public void setClosePlot(boolean closePlot) {
        this.closePlot = closePlot;
    }
}