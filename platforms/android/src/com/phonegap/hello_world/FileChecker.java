package com.phonegap.hello_world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class FileChecker {
	private HashMap <String, String> localSfv = null;

    public FileChecker(HashMap<String, String> localSfv) {
    	this.localSfv = localSfv;
    }
    
    public static HashMap<String, String> sfvToHash(File sfvFile){
    	HashMap<String, String> hash = new HashMap<String, String>();
    	BufferedReader reader = null;
    	
    	try {
    		reader = new BufferedReader(new FileReader(sfvFile));
    		String lineText = null;
    		
    		while ((lineText = reader.readLine() ) != null){
    			String[] str = lineText.split(" ");
    			hash.put(str[0], str[1]);
    		}
    	} catch (FileNotFoundException e){
    		e.getStackTrace();
    	} catch (IOException e){
    		e.getStackTrace();
    	} finally {
    		closeReader(reader);
    	}
    	
		return hash;
    }

	private static void closeReader(BufferedReader reader) {
		try {
			if (reader != null){
				reader.close();    			    				
			}
		} catch (IOException e) {
			e.getStackTrace();
		}
	}

	public void updateLocal(HashMap<String, String> remoteHash) {
		Iterator<String> result = compareEntries(this.localSfv, remoteHash).iterator();
		while (result.hasNext()){
			// remove keys that have different checksum
			this.localSfv.remove(result.next());
		}
	}
	
	private static <K extends Comparable<? super K>, V>
	List<String> compareEntries(final Map<K, V> map1,
	    final Map<K, V> map2){
		//return list of keys that have different checksum
	    final Collection<K> allKeys = new HashSet<K>();
	    allKeys.addAll(map1.keySet());
	    allKeys.addAll(map2.keySet());
	    List<String> result = new ArrayList<String>();
	    for(final K key : allKeys){
	    	if (checksumDifferent(map1, map2, key)){
	    		result.add((String) key);
	    	}
	    }
	    return result;
	}

	private static <K extends Comparable<? super K>, V> boolean checksumDifferent(
			final Map<K, V> map1, final Map<K, V> map2, final K key) {
		return !(map1.containsKey(key) == map2.containsKey(key) &&
		    Boolean.valueOf(equal(map1.get(key), map2.get(key))));
	}

	private static boolean equal(final Object obj1, final Object obj2){
	    return obj1 == obj2 || (obj1 != null && obj1.equals(obj2));
	}

	public boolean useCached(String filePath) {
		return this.localSfv.get(filePath) == null ? false : true;
	}
}
