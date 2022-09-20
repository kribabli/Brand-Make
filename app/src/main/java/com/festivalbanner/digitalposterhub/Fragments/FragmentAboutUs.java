package com.festivalbanner.digitalposterhub.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.festivalbanner.digitalposterhub.Activities.ActivityHome;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.google.android.gms.analytics.Tracker;

public class FragmentAboutUs extends Fragment {

    Context context;
    View view;
    Tracker mTracker;
    TextView tv_aboutus;
    WebView wv_website;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_aboutus, container, false);
        context = getContext();
        bindView();
        tv_aboutus.setText(Constance.aboutUs);


        ActivityHome.getInstance().ivBack.setVisibility(View.VISIBLE);
        wv_website.getSettings().setLoadsImagesAutomatically(true);
        wv_website.getSettings().setJavaScriptEnabled(true);
        wv_website.getSettings().setPluginState(WebSettings.PluginState.OFF);
        wv_website.getSettings().setLoadWithOverviewMode(true);
        wv_website.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv_website.getSettings().setUseWideViewPort(true);
        wv_website.getSettings().setUserAgentString("Android Mozilla/5.0 AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
        wv_website.getSettings().setAllowFileAccess(true);
//        wv_website.getSettings().setAllowFileAccess(true);
        wv_website.getSettings().setAllowContentAccess(true);
        wv_website.getSettings().supportZoom();
        wv_website.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv_website.loadUrl(Constance.aboutusurl);
        return view;
    }
    public void bindView()
    {
        tv_aboutus=view.findViewById(R.id.tv_aboutus);
        wv_website=view.findViewById(R.id.wv_website);

    }
}
