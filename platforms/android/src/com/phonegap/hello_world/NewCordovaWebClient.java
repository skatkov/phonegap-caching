package com.phonegap.hello_world;

import java.io.IOException;
import java.net.MalformedURLException;
import android.net.Uri;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.Toast;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.CordovaResourceApi.OpenForReadResult;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.IceCreamCordovaWebViewClient;
import static com.phonegap.hello_world.ProjectHelper.getUrlPath;
import com.phonegap.hello_world.FileChecker;
import static com.phonegap.hello_world.ProjectHelper.sfvToHash;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NewCordovaWebClient extends IceCreamCordovaWebViewClient {
	private final String TAG = "NewCordovaWebClient";
    HelloWorld activity;
    FileChecker localCache;
    CordovaResourceApi resourceApi;
    private Handler handler;
    

    public NewCordovaWebClient(CordovaInterface cordova,CordovaWebView view, CordovaActivity activity){
        super(cordova, view);
        
        this.activity = (HelloWorld) activity;
        //#2 - App loads local cache manifest of SFV format into hashmap (url, checksum)
        this.localCache = initLocalCache();
        this.resourceApi = view.getResourceApi();
        this.handler = new Handler();
        
        updateLocalCache();
                
        Log.d(TAG, "New cordova interface is running");
    }
    

	private void updateLocalCache() {
			new Thread(new Task()).start();
        }
	
	class Task implements Runnable {
		@Override
		public void run(){
			try {
	    		//FIXME: remove hardcode and move filepath to Config
	    		//#3 app loads remote cache manifest in sfv format
	    		OpenForReadResult result = resourceApi.openForRead(Uri.parse("http://phonegap-test.herokuapp.com/sfv/cache.sfv"), true);
				//#4 app removes any url from cache map where checksum has changed
	    		localCache.updateLocal(sfvToHash(result.inputStream));
	            Log.d(TAG, "Updated local cache with remote cache");
			} catch (IOException e) {
				Log.e(TAG, e.getStackTrace().toString());
			} 
			handler.post(new Runnable() {
                @Override
	            	public void run() {
                		Log.d(TAG, "Sync is finished");
                        activity.loadUrlNow();
	                }
                });

		}
	}


    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        Log.d(TAG, "shouldInterceptRequest -> called, URL is " + url.toString());
         
        String path = getUrlPath(url);
        //#6 app intercepts any url that is contained in the cache map and loads that file from local assets
        return localCache.useCached(path) ?
        	 getWebResourceResponseFromAsset("file:///android_asset/" + path) : super.shouldInterceptRequest(view, url);
    }

	private FileChecker initLocalCache() {
		try {
			// FIXME: change hardcode and move path to config
			// resourceApi breaks on getMimeTypeFromExtension(), so just use getAssets() 
        	return new FileChecker(sfvToHash(activity.getAssets().open("sfv/cache.sfv")));
		} catch (IOException e) {
			Log.e(TAG, "Local sfv cache file is missing");
			return new FileChecker(null);
		}
	}

	private WebResourceResponse getWebResourceResponseFromAsset(String path) {
		try {
			OpenForReadResult result = resourceApi.openForRead(Uri.parse(path));
			return new WebResourceResponse(result.mimeType, "UTF-8", result.inputStream);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage() + " missing in cache, downloading...");
			return null;
		}
	}


    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.e(TAG, "onReceivedError -> called, URL is " + failingUrl);
        //TODO: Good place to fallback failed links (like youtube embed)
        Toast.makeText(this.activity, description + " URL: " + failingUrl , Toast.LENGTH_SHORT).show();
    }    
}
