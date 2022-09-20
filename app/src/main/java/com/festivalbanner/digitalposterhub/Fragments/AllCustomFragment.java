package com.festivalbanner.digitalposterhub.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.ads.AdSettings;
import com.festivalbanner.digitalposterhub.Activities.ActivityHome;
import com.festivalbanner.digitalposterhub.Activities.ActivityPreview;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.StickerClasses.StickerView;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.jcmore2.collage.CollageView;

import java.util.ArrayList;
import java.util.List;

public class AllCustomFragment extends Fragment {
    CollageView collage;
    View view;
    StickerView sticker_view;
    Context context;
    Bitmap bitmapsave;
    LinearLayout ll_content, ll_main_custom;

    public AllCustomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_custom, container, false);

        collage = view.findViewById(R.id.collage);

        ActivityHome.getInstance().ivBack.setVisibility(View.GONE);
        ActivityHome.getInstance().ll_next.setVisibility(View.VISIBLE);
        ActivityHome.getInstance().ll_next.setVisibility(View.VISIBLE);

        ActivityHome.getInstance().ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sticker_view.hideIcons(true);
                bitmapsave = viewToBitmap(ll_content);
                Constance.createdBitmap = bitmapsave;

                Intent save = new Intent(context, ActivityPreview.class);
                save.putExtra("name", "image");
                startActivity(save);
                if (!Constance.isPremium) {
                    if (Constance.adType.equals("Ad Mob")) {
//                        interstitialAdMobAd();
                        Log.d("ADssss", "Ad Mob");
                    } else {
                        interstitialFacbookAd();
                        Log.d("ADssss", "Facebook");
                    }
                }
            }
        });
        List<Integer> listRes = new ArrayList<Integer>();
        listRes.add(R.drawable.ic_logo);
        listRes.add(R.drawable.premium_image);
        collage.createCollageResources(listRes);
        collage.setFixedCollage(false);

        return view;
    }

    public void interstitialFacbookAd() {
        if (ActivityHome.getInstance().interstitialFacbookAd != null) {
            if (!ActivityHome.getInstance().interstitialFacbookAd.isAdLoaded()) {
                AdSettings.setDebugBuild(true);
                ActivityHome.getInstance().interstitialFacbookAd.loadAd();
            } else {
            }
        } else {

        }
    }

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}