package org.dmtools.clustering.NBCTextIndex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.mail.Folder;

public class ClusteringTextIndex {
	
	TextIndex ti;
	HashMap<Integer, ArrayList<TextObject>> groups = new HashMap<Integer, ArrayList<TextObject>>();
	
	public static String WORKSPACE = "C:\\Users\\Piotrek\\Desktop\\GRAFY\\";
	//public static String WORKSPACE = "C:\\Users\\Piotr\\Desktop\\GRAFY\\";

	/**
	 * 
	 * @param q
	 */
	public void addObjectToGroup(TextObject q, Integer newGroup) {
		// exists
		
		ArrayList<TextObject> group = groups.get(q.clst_no);
		
		if (group != null) {
			int index = group.indexOf(q);
			group.remove(index);
		}
		
		q.clst_no = newGroup;
		
		if (!groups.containsKey(q.clst_no)) {
			groups.put(q.clst_no, new ArrayList<TextObject>());
		}
        
		groups.get(q.clst_no).add(q);
	}
	
	/**
	 * 
	 * @param groups
	 */
    public void writeGroups(String algorithmName) {
        
    	String folderName = NBCTextIndex.WORKSPACE + algorithmName + "\\";

    	
    	Set<Integer> keys = groups.keySet();

    	File f = new File(folderName);
    	
    	if (f.exists()) {
    		deleteFolder(folderName);
    		f.delete();
    	}

    	if (!f.exists()) {
    		f.mkdir();
    	}
    	
    	try {
			
		    for(Integer key:keys) {
		    	ArrayList<TextObject> group = groups.get(key);
		    	
		    	
		    	File file = new File(folderName + algorithmName + "_GROUP_" + key + " (" + group.size() + ").grf");
				BufferedWriter w = new BufferedWriter(new FileWriter(file));
				
		    	// Autorzy
		    	w.write("Autorzy:\n");
		    	
		    	for(TextObject to:group) {
		    		w.write(to.id + ", ");
		    	}
		    	
		    	w.write("\n");
		    	
		    	w.write("Lider:\n");
		    	w.write("1\n");
		    	w.write("Rok:\n");
		    	w.write("2014\n");
		    	w.write("Publikacje:\n");
		    	
		    	for(TextObject to1:group) {
		    		StringBuffer sb = new StringBuffer();
		    		for(TextObject to2:group) {
		    			
		    			if (to1.cooperators.contains(to2)) {
		    				sb.append("1 ");
		    			} else {
		    				sb.append("0 ");
		    			}
		    		}
		    		w.write(sb.toString().trim());
		    		w.write("\n");
		    	}
		    	w.write("\n");
		    	w.close();
		    }
        
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	private void deleteFolder(String folderName) {
		// TODO Auto-generated method stub
		File index = new File(folderName);
		String[]entries = index.list();
		for(String s: entries){
		    File currentFile = new File(index.getPath(),s);
		    currentFile.delete();
		}
		
	}

}
