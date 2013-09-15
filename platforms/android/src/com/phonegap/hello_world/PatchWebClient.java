package com.phonegap.hello_world;

import android.annotation.TargetApi;
import android.content.res.AssetManager;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.FileHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

public class PatchWebClient extends CordovaWebViewClient {

    protected AssetManager am;

    protected CordovaInterface cord;

    public PatchWebClient(CordovaInterface cordova, CordovaWebView view, AssetManager am) {

        super(cordova, view);
        Log.d("DEBUG", "starting PatchWebClient");
        this.am = am;
        this.cord = cordova;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url){
        Log.d("DEBUG", "URL is " + url);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        Log.d("DEBUG", "shouldInterceptRequest -> called");

        if (url.contains(".css")){
            Log.d("DEBUG", "Rewrite CSS");
            String mimetype = FileHelper.getMimeType(url, this.cord);

            WebResourceResponse wr = new WebResourceResponse(mimetype, "UTF-8",new StringBufferInputStream("body { background-color: #F781F3; }"));
            return wr;
        } else if (url.substring(0, 7).equalsIgnoreCase("file://") && !url.contains("file:///android_asset") && url.contains("?")) {
            String filePath = url.substring(7);

            Log.d("DEBUG", "---------------PATH " + filePath);
            try {

                String mimetype = FileHelper.getMimeType(url, this.cord);
                File file = new File(filePath);
                InputStream is = new BufferedInputStream(new FileInputStream(file));
                WebResourceResponse wr = new WebResourceResponse(mimetype, "UTF-8", is);

                return wr;
            } catch (IOException e) {
                Log.d("DEBUG", e.getMessage());
            }
        }

        return super.shouldInterceptRequest(view, url);
    }
}