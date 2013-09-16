package com.phonegap.hello_world;

import java.net.MalformedURLException;
import java.net.URL;

public class ProjectHelper {
	public static String getUrlPath(String strUrl){
    		try {
				String path = new URL(strUrl).getPath().substring(1);
				return	path.equals("") ?  null : path;
			} catch (MalformedURLException e) {
				return null;
			}			
    }
}
