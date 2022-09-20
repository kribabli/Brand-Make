package com.festivalbanner.digitalposterhub.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.festivalbanner.digitalposterhub.Activities.ActivityHome;
import com.festivalbanner.digitalposterhub.R;
import com.google.android.gms.analytics.Tracker;

public class VideoShowFragment extends Fragment {

    View view;
    String videoPath;
    Context context;
    VideoView vv_download;
    ImageView iv_pagerimg, iv_play_video;
    MediaController mediacontroller;
    ProgressDialog pDialog;
    MediaPlayer mediaPlayer;
    Tracker mTracker;
    public VideoShowFragment(String path) {
        videoPath = path;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video_show, container, false);
        initvv();
        context = getContext();
        mediacontroller = new MediaController(context);
        mediacontroller.setMediaPlayer(vv_download);
        vv_download.setMediaController(mediacontroller);
        vv_download.requestFocus();

        ActivityHome.getInstance().ivBack.setVisibility(View.GONE);
        Glide.with(context).load(videoPath).into(iv_pagerimg);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        iv_play_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.dismiss();
                Log.d("wseqwqwqwqwqwqwqw", "sdss"+videoPath);
                vv_download.start();
                iv_play_video.setVisibility(View.GONE);
                iv_pagerimg.setVisibility(View.GONE);
            }
        });

        if (videoPath != null) {
            vv_download.setVideoPath(videoPath);
        } else {
            Toast.makeText(context, "Not get the video path", Toast.LENGTH_LONG).show();
        }

        //  vv_playvideo.start();
        vv_download.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                Log.d("wseqwqwqwqwqwqwqw", "drfwerf");
                mediaPlayer=mp;
                //vv_playvideo.start();
            }
        });
        vv_download.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                    mp.stop();
                    iv_play_video.setVisibility(View.VISIBLE);

                }
                //mp.stop();
                vv_download.pause();
                iv_play_video.setVisibility(View.VISIBLE);

            }
        });
        return view;
    }

    void initvv() {
        vv_download = view.findViewById(R.id.vv_download);
        iv_pagerimg = view.findViewById(R.id.iv_pagerimg);
        iv_play_video = view.findViewById(R.id.iv_play_video);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    /*  @Override
    public void onDestroy() {
        super.onDestroyView();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }*/
}