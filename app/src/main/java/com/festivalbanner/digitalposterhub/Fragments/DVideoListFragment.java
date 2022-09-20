package com.festivalbanner.digitalposterhub.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.festivalbanner.digitalposterhub.Adapters.AdapterMyPostList;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.util.ArrayList;

public class DVideoListFragment extends Fragment {

    View view;
    RecyclerView rv_mypostlist;
    AdapterMyPostList adapterMyPostList;
    public static RelativeLayout rl_imagenotfound;
    private ArrayList<File> fileArrayList;
    Context context;
    ProgressBar pb_post_video;
    Tracker mTracker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_d_video_list, container, false);
        context = getContext();
        bindView();
        pb_post_video.setVisibility(View.VISIBLE);
        rv_mypostlist.setHasFixedSize(true);
        rv_mypostlist.setLayoutManager(new GridLayoutManager(context, 2));
        return view;
    }

    public void bindView() {
        rv_mypostlist = view.findViewById(R.id.rv_mypostlist);
        rl_imagenotfound = view.findViewById(R.id.rl_imagenotfound);
        pb_post_video = view.findViewById(R.id.pb_post_video);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (fileArrayList.size() == 0) {
            rl_imagenotfound.setVisibility(View.VISIBLE);
        } else {
            rl_imagenotfound.setVisibility(View.GONE);
        }
    }
}