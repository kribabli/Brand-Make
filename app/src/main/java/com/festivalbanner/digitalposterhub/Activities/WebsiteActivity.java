package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.festivalbanner.digitalposterhub.R;

public class WebsiteActivity extends AppCompatActivity {
    WebView wv_website;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        wv_website=findViewById(R.id.wv_website);
        wv_website.getSettings().setLoadsImagesAutomatically(true);
        wv_website.getSettings().setJavaScriptEnabled(true);
        wv_website.getSettings().setPluginState(WebSettings.PluginState.OFF);
        wv_website.getSettings().setLoadWithOverviewMode(true);
        wv_website.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv_website.getSettings().setUseWideViewPort(true);
        wv_website.getSettings().setUserAgentString("Android Mozilla/5.0 AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
        wv_website.getSettings().setAllowFileAccess(true);
        wv_website.getSettings().setAllowFileAccess(true);
        wv_website.getSettings().setAllowContentAccess(true);
        wv_website.getSettings().supportZoom();
        wv_website.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv_website.loadUrl("https://evepar.app");

    }
}