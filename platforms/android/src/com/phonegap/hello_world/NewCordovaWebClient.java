package com.phonegap.hello_world;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.Toast;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.IceCreamCordovaWebViewClient;

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
        return super.shouldInterceptRequest(view, url);
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.e("NewCordovaWebClient", "onReceivedError -> called, URL is " + failingUrl);
        Toast.makeText(this.activity, description + " URL: " + failingUrl , Toast.LENGTH_SHORT).show();
    }


}
