package org.dmtools.clustering;

/**
 * Created by Piotr on 01.06.2017.
 */
public class CDMBaseAlgorithmSettings {
    private boolean plot = false;
    private boolean dump = false;


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
}
