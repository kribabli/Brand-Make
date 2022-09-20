package com.festivalbanner.digitalposterhub.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.festivalbanner.digitalposterhub.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;


public class PagerAdapterMySinglePostView extends PagerAdapter {
    private Context context;
    private ArrayList<File> fileArrayList;
    String type;
    MediaController mediacontroller;
    // private ArrayList<Mode> imageList;
    private LayoutInflater inflater;
    VideoView vv_download;
    ImageView iv_play_video;
    ImageView imageView;
    MediaPlayer mediaPlayer;
    public PagerAdapterMySinglePostView(Context context, ArrayList<File> fileArrayList, String type) {
        this.context = context;
        this.fileArrayList = fileArrayList;
        this.type = type;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView((View) object);
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_pager_mypost, view, false);


        imageView  = imageLayout.findViewById(R.id.iv_pagerimg);
        vv_download = imageLayout.findViewById(R.id.vv_download);
        iv_play_video = imageLayout.findViewById(R.id.iv_play_video);
        if (type.equals("image")) {
            vv_download.setVisibility(View.GONE);
            iv_play_video.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(fileArrayList.get(position).getAbsolutePath()).into(imageView);
        } else {

            imageView.setVisibility(View.GONE);
            vv_download.setVisibility(View.VISIBLE);
            iv_play_video.setVisibility(View.GONE );
            Glide.with(context).load(fileArrayList.get(position).getAbsolutePath()).into(imageView);

            playVideo(fileArrayList.get(position).getAbsolutePath());
        }
        iv_play_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iv_play_video.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                vv_download.setVisibility(View.VISIBLE);


            }
        });
        vv_download.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                iv_play_video.setVisibility(View.GONE);
                mediaPlayer=mp;
                // vv_download.start();
            }
        });
        vv_download.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                /*if (pDialog.isShowing()) {
                    pDialog.dismiss();
                    mp.stop();
                    iv_vp_play.setVisibility(View.VISIBLE);

                }*/
                //mp.stop();
                vv_download.pause();
                // iv_vp_play.setVisibility(View.VISIBLE);

            }
        });
        imageLayout.setTag("view" + position);
        view.addView(imageLayout, 0);


        return imageLayout;
    }

    public void deletepagerdata(int position) {
        fileArrayList.remove(position);
    }

    @Override
    public int getCount() {
        return fileArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, @NotNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void playVideo(String path) {
       /* if (vv_download.isPlaying()) {
            vv_download.stopPlayback();
            vv_download.pause();
        }*/
        mediacontroller=null;
        mediacontroller = new MediaController(context);
        mediacontroller.setMediaPlayer(vv_download);
        vv_download.setMediaController(mediacontroller);
        vv_download.requestFocus();

        if (path != null) {
            vv_download.setVideoPath(path);
            vv_download.start();
        } else {
            Toast.makeText(context, "Not get the video path", Toast.LENGTH_LONG).show();
        }


    }

    public void scrool(int position){

        if(mediaPlayer!=null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();

            }
        }
        vv_download.setVisibility(View.VISIBLE);
        playVideo(fileArrayList.get(position).getAbsolutePath());
      /*  imageView.setVisibility(View.VISIBLE);
        vv_download.setVisibility(View.VISIBLE);
        iv_play_video.setVisibility(View.VISIBLE);
        Glide.with(context).load(fileArrayList.get(position).getAbsolutePath()).into(imageView);*/
    }


}