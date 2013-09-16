package com.phonegap.hello_world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ProjectHelper {
	public static String getUrlPath(String strUrl){
    		try {
				String path = new URL(strUrl).getPath().substring(1);
				return	path.equals("") ?  null : path;
			} catch (MalformedURLException e) {
				return null;
			}			
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


}
