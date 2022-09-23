package com.festivalbanner.digitalposterhub.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.ads.AdSettings;
import com.festivalbanner.digitalposterhub.Activities.ActivityHome;
import com.festivalbanner.digitalposterhub.Activities.AllCollageView;
import com.festivalbanner.digitalposterhub.Activities.ImageToVideoActivity;
import com.festivalbanner.digitalposterhub.Me.DigitalBusinessCardActivity;
import com.festivalbanner.digitalposterhub.Me.LoGoMakerActivity;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.StickerClasses.StickerView;

public class AllCustomFragment extends Fragment {
    StickerView sticker_view;
    Context context;
    View view;
    LinearLayout create_your_own;
    LinearLayout make_your_video;
    LinearLayout image_to_video, Video_template, collage_Maker;
    LinearLayout Digital_business_Card, logo_maker;

    public AllCustomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_custom, container, false);
        create_your_own = view.findViewById(R.id.create_your_own);
        make_your_video = view.findViewById(R.id.make_your_video);
        image_to_video = view.findViewById(R.id.image_to_video);
        Video_template = view.findViewById(R.id.Video_template);
        image_to_video = view.findViewById(R.id.image_to_video);
        collage_Maker = view.findViewById(R.id.collage_Maker);
        Digital_business_Card = view.findViewById(R.id.Digital_business_Card);
        logo_maker = view.findViewById(R.id.logo_maker);

        ActivityHome.getInstance().ivBack.setVisibility(View.GONE);
        ActivityHome.getInstance().ll_next.setVisibility(View.VISIBLE);
        ActivityHome.getInstance().ll_next.setVisibility(View.VISIBLE);

        collage_Maker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AllCollageView.class);
                getContext().startActivity(intent);
            }
        });

        Digital_business_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DigitalBusinessCardActivity.class);
                getContext().startActivity(intent);
            }
        });

        logo_maker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoGoMakerActivity.class);
                getContext().startActivity(intent);
            }
        });

        image_to_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImageToVideoActivity.class);
                getContext().startActivity(intent);
            }
        });

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
}