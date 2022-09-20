package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.festivalbanner.digitalposterhub.ModelRetrofit.RecordRegister;
import com.festivalbanner.digitalposterhub.Adapters.PagerAdapterMySinglePostView;
import com.festivalbanner.digitalposterhub.AnalyticsApplication;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class ActivityMyPostSingleView extends AppCompatActivity {

    Context context;
    private ArrayList<File> fileArrayList;
    PagerAdapterMySinglePostView pagerAdapterMySinglePostView;
    int mypostposition;
    AdView mAdView;
    Tracker mTracker;
    LinearLayout facbook_ad_banner;
    public Bitmap bitmapsave;
    public Date currentTime;
    public boolean isForShareGlobal;
    ProgressDialog dialog;
    String[] permissionsRequired = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int PERMISSION_CALLBACK_CONSTANT = 200;
    public String type;
    VideoView vv_download_video;
    ImageView iv_download_image, iv_play_dv;
    MediaController mediacontroller;
    ProgressDialog pDialog;
    String selectedvideopath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_single_view);

        context = ActivityMyPostSingleView.this;
        bindView();

        SharedPrefrenceConfig pref = new SharedPrefrenceConfig(context);
        RecordRegister getuser = pref.getUser();
//        if (getuser.getPlan_type() == null || !getuser.getPlan_type().equals("Premium")) {
        if (!Constance.isPremium) {
            if (Constance.adType.equals("Ad Mob")) {
                loadAdMobAd();

            } else {
                loadFacebookAd();
            }
        }else {
            mAdView.setVisibility(View.GONE);
            facbook_ad_banner.setVisibility(View.GONE);

        }

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        fileArrayList = new ArrayList<>();
        type = getIntent().getExtras().getString("type");

        mypostposition = getIntent().getExtras().getInt("mypostposition", 0);

        if (type.equals("image")) {
            fileArrayList = Constance.mypostImageList;
            iv_play_dv.setVisibility(View.GONE);
            vv_download_video.setVisibility(View.GONE);
            iv_download_image.setVisibility(View.VISIBLE);
            Glide.with(context).load(fileArrayList.get(mypostposition).getAbsolutePath()).placeholder(R.drawable.placeholder).into(iv_download_image);
        } else {
            fileArrayList = Constance.mypostVideoList;
            iv_play_dv.setVisibility(View.VISIBLE);
            vv_download_video.setVisibility(View.VISIBLE);
            iv_download_image.setVisibility(View.VISIBLE);
            Glide.with(context).load(fileArrayList.get(mypostposition).getAbsolutePath()).placeholder(R.drawable.placeholder).into(iv_download_image);
            selectedvideopath = fileArrayList.get(mypostposition).getAbsolutePath();
            playVideo();
        }

        iv_play_dv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.dismiss();
                Log.d("wseqwqwqwqwqwqwqw", "sdss");
                vv_download_video.start();
                iv_play_dv.setVisibility(View.GONE);
                iv_download_image.setVisibility(View.GONE);

            }
        });
        vv_download_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                //vv_playvideo.start();
            }
        });
        vv_download_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                    mp.stop();
                    iv_play_dv.setVisibility(View.VISIBLE);
                    iv_download_image.setVisibility(View.VISIBLE);
                }
                //mp.stop();
                vv_download_video.pause();
                iv_play_dv.setVisibility(View.VISIBLE);

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + "Google Analytics Testing");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void bindView() {
        mAdView = findViewById(R.id.adview);
        facbook_ad_banner = findViewById(R.id.facbook_ad_banner);
        vv_download_video = findViewById(R.id.vv_download_video);
        iv_download_image = findViewById(R.id.iv_download_image);
        iv_play_dv = findViewById(R.id.iv_play_dv);
    }

    public void playVideo() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        mediacontroller = new MediaController(context);
        mediacontroller.setMediaPlayer(vv_download_video);
        vv_download_video.setMediaController(mediacontroller);
        vv_download_video.requestFocus();
      /*  try {
            mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(vv_playvideo);
            vv_playvideo.setMediaController(mediacontroller);
            vv_playvideo.setVideoPath(videoCategoriesDataArrayList.get(0).getVideo_url());

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        if (selectedvideopath != null) {
            vv_download_video.setVideoPath(selectedvideopath);
        } else {
            Toast.makeText(context, "Not get the video path", Toast.LENGTH_LONG).show();
        }

        //  vv_playvideo.start();

    }

    public void loadAdMobAd() {

        //        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                // Check the LogCat to get your test device ID
//                .addTestDevice("C04B1BFFB0774708339BC273F8A43708")
//                .build();
//
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//        });
//
//        mAdView.loadAd(adRequest);
    }

    public void loadFacebookAd() {

        mAdView.setVisibility(View.GONE);
        facbook_ad_banner.setVisibility(View.VISIBLE);
        com.facebook.ads.AdView adFaceView = new com.facebook.ads.AdView(context, getResources().getString(R.string.facebook_banner_id), AdSize.BANNER_HEIGHT_50);

        AdSettings.setDebugBuild(true);

        // Add the ad view to your activity layout
        facbook_ad_banner.addView(adFaceView);


        adFaceView.loadAd();

    }

    public void onclickMyPostSingleView(View view) {
        switch (view.getId()) {
            case R.id.ll_delete:
                dailogdelete();
                break;
            case R.id.ll_share:
                //ImageView iv_pager = vp_singlepost.findViewWithTag("view" + vp_singlepost.getCurrentItem()).findViewById(R.id.iv_pagerimg);
                if (type.equals("image")) {
                    shareImage(context, fileArrayList.get(mypostposition).getPath());

                } else {
                    shareVideo(context, fileArrayList.get(mypostposition).getPath());
                }

                break;
            case R.id.iv_backarrow:
                onBackPressed();
                break;
        }
    }

    public static void shareImage(Context context, String filePath) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "download this image");
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), filePath, "", null);
            Uri screenshotUri = Uri.parse(path);
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image/*");
            context.startActivity(Intent.createChooser(intent, "Share File via..."));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void shareVideo(Context context, String path) {

        Intent share = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(context, getString(R.string.file_provider_authority), new File(path));
        share.setType("Video/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share via"));
    }

    public void dailogdelete() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        final android.app.AlertDialog alertDialog = builder.create();
        builder.setMessage("Do you want to delete?")
                .setCancelable(false)
                .setTitle("Delete ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        File fdelete = new File(fileArrayList.get(mypostposition).getPath());
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                Toast.makeText(context, "File Succesfully deleted ", Toast.LENGTH_LONG).show();
                                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(fileArrayList.get(mypostposition).getPath()))));

                            } else {
                                Toast.makeText(context, "File not deleted ", Toast.LENGTH_LONG).show();
                            }
                        }
                        dialog.dismiss();
                      /*  pagerAdapterMySinglePostView.deletepagerdata(vp_singlepost.getCurrentItem());
                        pagerAdapterMySinglePostView.notifyDataSetChanged();*/
                        if (fileArrayList.size() == 0) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                            builder.setMessage("No Data Found")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            onBackPressed();
                                        }
                                    });

                            //Creating dialog box
                            androidx.appcompat.app.AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                        }

                        finish();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


}