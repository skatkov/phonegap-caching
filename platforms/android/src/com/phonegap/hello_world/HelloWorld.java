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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Set by <content src="index.html" /> in config.xml

        this.init();

        super.setIntegerProperty("loadUrlTimeoutValue", 160000);
        super.loadUrl("http://www.prisonfight.org");
        //super.loadUrl("file:///android_asset/www/index.html")
    }

    public void init(){
        webView = new CordovaWebView(this);
        CordovaWebViewClient webViewClient;
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

