package com.phonegap.hello_world;

import java.io.IOException;
import java.net.MalformedURLException;
import android.net.Uri;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
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
    Activity activity;
    FileChecker localCache;
    CordovaResourceApi resourceApi;

    public NewCordovaWebClient(CordovaInterface cordova,CordovaWebView view, CordovaActivity activity){
        super(cordova, view);
        
        this.activity = activity;
        this.localCache = initLocalCache();
        this.resourceApi = view.getResourceApi();
        
        updateLocalCache();
                
        Log.d(TAG, "New cordova interface is running");
    }

	private void updateLocalCache() {
		new Thread(new Runnable() {
            public void run() {
            	try {
            		//FIXME: remove hardcode and move filepath to Config
            		OpenForReadResult result = resourceApi.openForRead(Uri.parse("http://phonegap-test.herokuapp.com/sfv/cache.sfv"));
        			localCache.updateLocal(sfvToHash(result.inputStream));
                    Log.d(TAG, "Updated local cache with remote cache");
        		} catch (IOException e) {
        			Log.e(TAG, e.getStackTrace().toString());
        		}
            }
          }).start();
	}


    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        Log.d(TAG, "shouldInterceptRequest -> called, URL is " + url.toString());
        
        
        String path = getUrlPath(url);
        if (localCache.useCached(path)) {
        	return getWebResourceResponseFromAsset("file:///android_asset/" + path);	
        } 
        return super.shouldInterceptRequest(view, url);
    }

	private FileChecker initLocalCache() {
		try {
			// FIXME: change hardcode and move path to config
			// resourceApi breaks on MimeTypeFromExtension(), so just use getAssets() 
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
