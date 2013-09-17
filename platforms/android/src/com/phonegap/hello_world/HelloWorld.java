package com.phonegap.hello_world;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaChromeClient;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;

public class HelloWorld extends CordovaActivity {
    CordovaWebView webView;
    CordovaWebViewClient webViewClient;
    

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        //#1 - Initialization of cardova app
        this.init();
        
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
        	loadUrlNow();
        }
    }
    
    public void loadUrlNow(){
    	// #5 app loads host page in cordova webclient (dummy website from ./heroku-static-site)
    	super.setIntegerProperty("loadUrlTimeoutValue", 160000);
        super.loadUrl("http://phonegap-test.herokuapp.com");
    }

    public void init(){
        webView = new CordovaWebView(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            webViewClient = new CordovaWebViewClient(this, webView);
        } else {
            webViewClient = new NewCordovaWebClient(this, webView, this);
        }
        WebSettings settings = webView.getSettings();

        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        

        this.init(webView, webViewClient, new CordovaChromeClient(this, webView));
    }
}

