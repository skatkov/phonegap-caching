package com.phonegap.hello_world;

import java.net.MalformedURLException;
import java.net.URL;

public class ProjectHelper {
	public static String getUrlPath(String strUrl){
    	if (strUrl.contains("http:") || strUrl.contains("https:")){
			try {
				return new URL(strUrl).getPath();
			} catch (MalformedURLException e) {
				return "/";
			}			
		} else {
			return "/";
		} 
    }
}
