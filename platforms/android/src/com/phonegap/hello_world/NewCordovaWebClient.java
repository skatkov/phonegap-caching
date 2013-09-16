package com.phonegap.hello_world;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
    FileChecker localCache;

    public NewCordovaWebClient(CordovaInterface cordova,CordovaWebView view, CordovaActivity activity){
        super(cordova, view);
        this.activity = activity;
        localCache = initLocalCache();
        
        //FIXME: add threaded file download (separate from main activity)
        /*
        try {
			localCache.updateLocal(sfvToHash(new URL("http://phonegap-test.herokuapp.com/cache.sfv").openStream()));
			Log.d(TAG, "Update local cach.sfv");
        } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
        
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
        
        String path = getUrlPath(url);
        if (localCache.useCached(path)) {
        	if (path.contains(".css")){
        		return getCssWebResourceResponseFromAsset(path);
        	} else if (path.contains(".png")){
        		return getPngWebResourceResponseFromAsset(path);
        	}         	
        } 
        return super.shouldInterceptRequest(view, url);
    }

	private FileChecker initLocalCache() {
		try {
			// FIXME: change hardcode and move path to config
        	return new FileChecker(sfvToHash(activity.getAssets().open("sfv/cache.sfv")));
		} catch (IOException e) {
			Log.e(TAG, "Local sfv cache file is missing");
			return new FileChecker(null);
		}
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
			return new WebResourceResponse("text/css", "UTF-8", activity.getAssets().open(path));
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
