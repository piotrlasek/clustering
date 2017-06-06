package org.dmtools.clustering.old;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ClusteringTimer {
    
    long indexCreationTime = 0;
    long clusteringTime = 0;
    
    HashMap<String, Long> times;
    
    ArrayList<String> descriptions;
    
    String algorithmName = "<algorithm_name>";
    
    String parameters = "<algorithm_parameters>";

    public ClusteringTimer(String algorithmName) {
        this.algorithmName = algorithmName;
        times = new HashMap<String, Long>();
        descriptions = new ArrayList<String>();
    }

    public ClusteringTimer() {
        times = new HashMap<String, Long>();
        descriptions = new ArrayList<String>();
    }

    public void start(String s) {
        Long start = System.currentTimeMillis();
        times.put(s, start);
    }
    
    public void end(String s) {
        Long end = System.currentTimeMillis();
        Long start = times.get(s);
        times.put(s, end - start);
    }

    public void merge(ClusteringTimer timerToMerge) {
        Iterator<String> keys = timerToMerge.times.keySet().iterator();
        while(keys.hasNext()) {
            String k = keys.next();

            if (!this.times.keySet().contains(k)) {
                Long time = timerToMerge.times.get(k);
                this.times.put(k, time);
            }
        }
    }
    
    public void setAlgorithmName(String name) {
        algorithmName = name;
    }
    
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }    
    
    public void indexStart() {
        start("indexCreationTime");
    }
    
    public void indexEnd() {
        end("indexCreationTime");
    }
    
    public void clusteringStart() {
        start("clusteringTime");
    }
    
    public void clusteringEnd() {
        end("clusteringTime");
    }
    
    public void addDescription(String description) {
        descriptions.add(description);
    }
    
    public void sortingStart()
    {
        start("sorting");
    }
    
    public void sortingEnd()
    {
        end("sorting");
    }
    
    
    public String getLog() {
        StringBuffer sb = new StringBuffer();
        
        sb.append(algorithmName);
        sb.append("\t");
        sb.append(parameters.toString());
        sb.append("\t");
        sb.append(times.get("indexCreationTime"));
        sb.append("\t");
        Object sorting = times.get("sorting");
        if (sorting != null)
        {
            sb.append(sorting);
            sb.append("\t");
        }

        sb.append(times.get("clusteringTime"));
        sb.append("\t");

        Object deferred = times.get("deferred");
        if (deferred != null) {
            sb.append(deferred);
            sb.append("\t");
        }

        int size = descriptions.size();
        for(int i = 0; i < size; i++) {
            String s = descriptions.get(i);
            if (s != null) {
                sb.append("\t");
                sb.append(s);
            }
        }
        
        return sb.toString();
    }
}
