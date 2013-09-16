package com.phonegap.hello_world;

import java.io.IOException;
import java.io.InputStream;

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

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NewCordovaWebClient extends IceCreamCordovaWebViewClient {
    Activity activity;

    public NewCordovaWebClient(CordovaInterface cordova,CordovaWebView view, CordovaActivity activity){
        super(cordova, view);
        this.activity = activity;
        Log.d("DEBUG", "New cordova interface is running");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url){
        Log.d("NewCordovaWebClient", "shouldOverrideUrlLoading -> called, URL is " + url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        Log.d("NewCordovaWebClient", "shouldInterceptRequest -> called, URL is " + url.toString());
        String path = getUrlPath(url);
        Log.d("NewCordovaWebClient", "url path is "  + path);
        if (path.contains(".css")){
        	try {
        		InputStream file = activity.getAssets().open("css/main.css");
        		Log.d("NewCordovaWebClient", "rewriting main.css " + file.toString());
				return new WebResourceResponse("text/css", "UTF-8", file);
			} catch (IOException e) {
				Log.e("NewCordovaWebClient", e.getMessage());
				// FIXME: probably, null is not a good idea to return
				return null;
			}
        } else {        	
        	return super.shouldInterceptRequest(view, url);
        }
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.e("NewCordovaWebClient", "onReceivedError -> called, URL is " + failingUrl);
        Toast.makeText(this.activity, description + " URL: " + failingUrl , Toast.LENGTH_SHORT).show();
    }    
}
