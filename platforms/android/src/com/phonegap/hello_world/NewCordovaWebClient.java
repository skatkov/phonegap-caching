package com.phonegap.hello_world;

import java.io.File;
import java.io.IOException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.Toast;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.IceCreamCordovaWebViewClient;
import static com.phonegap.hello_world.ProjectHelper.getUrlPath;
import com.phonegap.hello_world.FileChecker;
import static com.phonegap.hello_world.ProjectHelper.sfvToHash;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NewCordovaWebClient extends IceCreamCordovaWebViewClient {
	private final String TAG = "NewCordovaWebClient";
    Activity activity;

    public NewCordovaWebClient(CordovaInterface cordova,CordovaWebView view, CordovaActivity activity){
        super(cordova, view);
        this.activity = activity;
        Log.d("DEBUG", "New cordova interface is running");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url){
        Log.d(TAG, "shouldOverrideUrlLoading -> called, URL is " + url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        Log.d(TAG, "shouldInterceptRequest -> called, URL is " + url.toString());
        FileChecker localCache = new FileChecker(sfvToHash(new File("./assets/sfv/local.sfv")));
        
        String path = getUrlPath(url);
        if (localCache.useCached(path)){
        	if (path.contains(".css")){
        		return getCssWebResourceResponseFromAsset(path);
        	} else if (path != null && path.contains(".png")){
        		return getPngWebResourceResponseFromAsset(path);
        	}         	
        } 
        return super.shouldInterceptRequest(view, url);
    }

	private WebResourceResponse getPngWebResourceResponseFromAsset(String path) {
		try {
			return new WebResourceResponse("image/png", "UTF-8", activity.getAssets().open(path));
		} catch (IOException e) {
			Log.e(TAG, e.getMessage() + " missing in cache, downloading...");
			return null;
		}
	}

	private WebResourceResponse getCssWebResourceResponseFromAsset(String path) {
		try {
			Log.d(TAG, "Lets replace css with assets " + path.toString());
			return new WebResourceResponse("text/css", "UTF-8", activity.getAssets().open(path));
		} catch (IOException e) {
			Log.e(TAG, e.getMessage() + " missing in cache, downloading...");
			return null;
		}
	}

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.e(TAG, "onReceivedError -> called, URL is " + failingUrl);
        Toast.makeText(this.activity, description + " URL: " + failingUrl , Toast.LENGTH_SHORT).show();
    }    
}
