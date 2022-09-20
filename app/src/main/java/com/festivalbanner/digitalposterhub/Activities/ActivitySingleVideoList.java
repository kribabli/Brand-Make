package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.festivalbanner.digitalposterhub.Adapters.AdapterSingleCatList;
import com.festivalbanner.digitalposterhub.Adapters.BusinessListAdapter;
import com.festivalbanner.digitalposterhub.ModelRetrofit.List_of_Video_Category_Name;
import com.festivalbanner.digitalposterhub.ModelRetrofit.RecordRegister;
import com.festivalbanner.digitalposterhub.ModelRetrofit.ResponseLogin;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoCategoriesData;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoCategoriesRecord;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.festivalbanner.digitalposterhub.Adapters.AdapterCategoryList;
import com.festivalbanner.digitalposterhub.Adapters.AdapterFrames;
import com.festivalbanner.digitalposterhub.Adapters.AdapterSingleCatVideoList;
import com.festivalbanner.digitalposterhub.Adapters.FrameListAdepter;
import com.festivalbanner.digitalposterhub.AnalyticsApplication;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
import com.festivalbanner.digitalposterhub.Utills.Admin;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.festivalbanner.digitalposterhub.Utills.EndlessRecyclerOnScrollListener;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySingleVideoList extends AppCompatActivity {

    Context context;
    AdView mAdView;
    LinearLayout facbook_ad_banner;
    RecyclerView rv_singlevideoitems;
    TextView tv_singlevideocatname;
    String childitemtittle, catnameid,catType="All";
    ImageView iv_backarrow, iv_vp_play, iv_al_language;
    VideoView vv_playvideo;
    LinearLayout ll_selectedvideo, ll_next_singlevideo;
    SharedPrefrenceConfig sharedPrefrenceConfig;
    MediaController mediacontroller;
    ProgressDialog pDialog;
    String selectedvideopath, language;
    ArrayList<VideoCategoriesData> videoCategoriesDataArrayList;
    ArrayList<VideoCategoriesData> temp=new ArrayList<>();
    public AdapterSingleCatVideoList adapterSingleCatVideoList;
    public FrameListAdepter frameListAdepter;
    boolean df = false;
    RelativeLayout rl_frame;
    int frame;
    ProgressBar pb_video;
    public AdapterFrames adapterFrame;
    Tracker mTracker;
    public PopupWindowHelper popupWindowHelper;
    View popupview_down;
    String frameName[] = {"All", "English", "Hindi", "Gujarati"};
    VideoCategoriesRecord model;
    TextView tv_all, tv_guj, tv_hindi, tv_eng;

    public static ActivitySingleVideoList instance = null;

    public ActivitySingleVideoList() {
        instance = ActivitySingleVideoList.this;
    }

    public static synchronized ActivitySingleVideoList getInstance() {
        if (instance == null) {
            instance = new ActivitySingleVideoList();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video_list);

        context = ActivitySingleVideoList.this;
        bindView();
        sharedPrefrenceConfig = new SharedPrefrenceConfig(context);
        language = sharedPrefrenceConfig.getPrefString(context, Constance.language, "");


        calculationForHeight(); SharedPrefrenceConfig pref = new SharedPrefrenceConfig(context);
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

        getprofiledetails();

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        childitemtittle = getIntent().getExtras().getString("childitemtittle");
        catnameid = getIntent().getExtras().getString("catnameid");

        tv_singlevideocatname.setText(childitemtittle);
        GridLayoutManager gm = new GridLayoutManager(context, 3);
        rv_singlevideoitems.setLayoutManager(gm);

      /*  frameListAdepter = new FrameListAdepter(context, arrayList);
        rv_frame_list.setLayoutManager(new GridLayoutManager(context, 3));
        rv_frame_list.setAdapter(frameListAdepter);
        setFrame(arrayList.get(0));
        frame = arrayList.get(0);
        Log.d("frsrsgh", "" + frame);*/


        videoCategoriesDataArrayList = new ArrayList<>();

        iv_al_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                popupview_down = LayoutInflater.from(context).inflate(R.layout.category_dropdown_raw, null);
                popupWindowHelper = new PopupWindowHelper(popupview_down);
                popupWindowHelper.showAsDropDown(v, 0, 0);
                RecyclerView rv_cat_dd_raw = popupview_down.findViewById(R.id.rv_cat_dd_raw);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
                rv_cat_dd_raw.setLayoutManager(gridLayoutManager);
                rv_cat_dd_raw.setHasFixedSize(true);

                AdapterCategoryList adapterCategory = new AdapterCategoryList(context, frameName, "video");
                rv_cat_dd_raw.setAdapter(adapterCategory);
            }
        });

        if (Constance.ComeFrom.equals("festivalviewall")) {
            Log.d("dfdfsfd", "dfdfsfd1");
            getFestivalvideolist();
            rv_singlevideoitems.addOnScrollListener(new EndlessRecyclerOnScrollListener(gm) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (model != null && model.getNext_page_url() != null) {
//                        loadNextDataFromApi_business();
                        loadNextDataFromApi_festival();
                    }
                }
            });
        } else {
            getBusinessVideolist();
            rv_singlevideoitems.addOnScrollListener(new EndlessRecyclerOnScrollListener(gm) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (model != null && model.getNext_page_url() != null) {
                        loadNextDataFromApi_business();
//                        loadNextDataFromApi_festival();
                    }
                }
            });
        }

        iv_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ll_next_singlevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constance.ComeFrom = "video";
                Constance.FromSinglecatActivity = selectedvideopath;
                if (Admin.tinyDB.getBoolean("login")) {

                    if (sharedPrefrenceConfig.getPrefString(context, Constance.Status, "").equals("Active")) {
                        Constance.activityName = "video";
                        Intent i = new Intent(context, ActivityCreatePost.class);
                        startActivity(i);
                    } else {
                        sharedPrefrenceConfig.saveStringPreferance(context, Constance.CToken, "");
                        sharedPrefrenceConfig.savebooleanPreferance(context, "checkLogin", false);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("your token expired so try to login........ ")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dialog.cancel();
                                    }
                                });

                        //Creating dialog box
                        builder.create();
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                            }
                        });
                        dialog.show();

                    }

              /*  i.putExtra("FromSinglecatActivity", selectedvideopath);
                i.putExtra("ComeFrom", "video");*/

                } else {


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("you need a account to continue ... ")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent i = new Intent(context, ActivitySignIn.class);
                                    startActivity(i);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
                    //Creating dialog box
                    builder.create();
                    AlertDialog dialog = builder.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                        }
                    });
                    dialog.show();
                }


              /*  pb_video.setVisibility(View.VISIBLE);
                rl_frame.setDrawingCacheEnabled(true);
                rl_frame.buildDrawingCache(true);


                Bitmap savedBmp = viewToBitmap(rl_frame);

                Bitmap newsaveBmp = scaleBitmap(savedBmp, 1000, 1000);
                try {
                    //Write file
                   *//* String root = Environment.getExternalStorageDirectory()
                            .toString();
                    File myDir = new File(root + "/kiki");
                    myDir.mkdirs();*//*

                    if (!Constance.FileSaveVideoDirectory.exists()) {
                        Constance.FileSaveVideoDirectory.mkdir();
                    }
                    File file = new File(Constance.FileSaveVideoDirectory, "demoImage.png");

                    if (file.exists())
                        file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        newsaveBmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        dwonloadVideo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("dfjhfh", "hfgd" + selectedvideopath);*/

            }
        });
        tv_all.setTextColor(getResources().getColor(R.color.white));
        tv_guj.setTextColor(getResources().getColor(R.color.colorTheame));
        tv_hindi.setTextColor(getResources().getColor(R.color.colorTheame));
        tv_eng.setTextColor(getResources().getColor(R.color.colorTheame));

        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_all.setBackground(getResources().getDrawable(R.drawable.round_view_greyborder));
                tv_guj.setBackground(getResources().getDrawable(R.drawable.bg_themeborder));
                tv_hindi.setBackground(getResources().getDrawable(R.drawable.bg_themeborder));
                tv_eng.setBackground(getResources().getDrawable(R.drawable.bg_themeborder));
                tv_all.setTextColor(getResources().getColor(R.color.white));
                tv_guj.setTextColor(getResources().getColor(R.color.colorTheame));
                tv_hindi.setTextColor(getResources().getColor(R.color.colorTheame));
                tv_eng.setTextColor(getResources().getColor(R.color.colorTheame));

                setdefaultlang();
            }
        });
        tv_guj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_all.setBackground(getResources().getDrawable(R.drawable.bg_themeborder));
                tv_guj.setBackground(getResources().getDrawable(R.drawable.round_view_greyborder));
                tv_hindi.setBackground(getResources().getDrawable(R.drawable.bg_themeborder));
                tv_eng.setBackground(getResources().getDrawable(R.drawable.bg_themeborder));
                tv_all.setTextColor(getResources().getColor(R.color.colorTheame));
                tv_guj.setTextColor(getResources().getColor(R.color.white));
                tv_hindi.setTextColor(getResources().getColor(R.color.colorTheame));
                tv_eng.setTextColor(getResources().getColor(R.color.colorTheame));
                filterlanguagearray("Gujarati");
            }
        });
        tv_hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_all.setBackground(getResources().getDrawable(R.drawable.bg_themeborder));
                tv_guj.setBackground(getResources().getDrawable(R.drawable.bg_themeborder));
                tv_hindi.setBackground(getResources().getDrawable(R.drawable.round_view_greyborder));
                tv_eng.setBackground(getResources().getDrawable(R.drawable.bg_themeborder));
                filterlanguagearray("Hindi");
                tv_all.setTextColor(getResources().getColor(R.color.colorTheame));
                tv_guj.setTextColor(getResources().getColor(R.color.colorTheame));
                tv_hindi.setTextColor(getResources().getColor(R.color.white));
                tv_eng.setTextColor(getResources().getColor(R.color.colorTheame));
            }
        });
        tv_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_all.setBackground(getResources().getDrawable(R.drawable.bg_themeborder));
                tv_guj.setBackground(getResources().getDrawable(R.drawable.bg_themeborder));
                tv_hindi.setBackground(getResources().getDrawable(R.drawable.bg_themeborder));
                tv_eng.setBackground(getResources().getDrawable(R.drawable.round_view_greyborder));
                tv_all.setTextColor(getResources().getColor(R.color.colorTheame));
                tv_guj.setTextColor(getResources().getColor(R.color.colorTheame));
                tv_hindi.setTextColor(getResources().getColor(R.color.colorTheame));
                tv_eng.setTextColor(getResources().getColor(R.color.white));
                filterlanguagearray("English");
            }
        });
    }
    private void filterlanguagearray(String language) {
        temp = new ArrayList<>();
        temp.clear();
        if (videoCategoriesDataArrayList != null && videoCategoriesDataArrayList.size() > 0) {
            for (int i = 0; i < videoCategoriesDataArrayList.size(); i++) {
                if (videoCategoriesDataArrayList.get(i).getLanguage().equals(language)) {
                    temp.add(videoCategoriesDataArrayList.get(i));
                }
            }
            adapterSingleCatVideoList = new AdapterSingleCatVideoList(context, temp);
            rv_singlevideoitems.setAdapter(adapterSingleCatVideoList);
        }
    }
    private void setdefaultlang() {
        if (videoCategoriesDataArrayList != null && videoCategoriesDataArrayList.size() > 0) {
            adapterSingleCatVideoList = new AdapterSingleCatVideoList(context, videoCategoriesDataArrayList);
            rv_singlevideoitems.setAdapter(adapterSingleCatVideoList);
        }
    }

    public void bindView() {
        mAdView = findViewById(R.id.adview);
        facbook_ad_banner = findViewById(R.id.facbook_ad_banner);
        rv_singlevideoitems = findViewById(R.id.rv_singlevideoitems);
        tv_singlevideocatname = findViewById(R.id.tv_singlevideocatname);
        iv_backarrow = findViewById(R.id.iv_backarrow);
       /* iv_firstimage = findViewById(R.id.iv_firstimage);
        ll_next_singlecatlist = findViewById(R.id.ll_next_singlecatlist);*/
        ll_selectedvideo = findViewById(R.id.ll_selectedvideo);
        vv_playvideo = findViewById(R.id.iv_videoshow);
        iv_vp_play = findViewById(R.id.iv_vp_play);
        ll_next_singlevideo = findViewById(R.id.ll_next_singlevideo);
        rl_frame = findViewById(R.id.rl_frame);
        pb_video = findViewById(R.id.pb_video);
        iv_al_language = findViewById(R.id.iv_al_language);
        iv_al_language.setVisibility(View.GONE);
        tv_all = findViewById(R.id.tv_all);
        tv_guj = findViewById(R.id.tv_guj);
        tv_hindi = findViewById(R.id.tv_hindi);
        tv_eng = findViewById(R.id.tv_eng);
    }

    public void getprofiledetails() {
        Api api = Base_Url.getClient().create(Api.class);
        String token=sharedPrefrenceConfig.getPrefString(context, Constance.CToken, null);
        if (token !=null && !token.equals("")) {
            Call<ResponseLogin> call = api.getProfileDetails(token);
            call.enqueue(new Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                    if (response !=null && response.body()!=null){
                        ResponseLogin getprofile = response.body();
                        if (getprofile.getResult() != null && getprofile.getResult().equals("1")) {
                            if (getprofile.getRecord() != null) {
                                sharedPrefrenceConfig.saveStringPreferance(context, Constance.Status, response.body().getRecord().getStatus());


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("record null : " + response.body().getMessage())
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });

                                //Creating dialog box
                                builder.create();
                                AlertDialog dialog = builder.create();
                                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface arg0) {
                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                    }
                                });
                                dialog.show();
                            }

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(response.body().getMessage())
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                        }
                                    });

                            //Creating dialog box
                            builder.create();
                            AlertDialog dialog = builder.create();
                            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface arg0) {
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                }
                            });
                            dialog.show();
                        }
                    }


                }

                @Override
                public void onFailure(Call<ResponseLogin> call, Throwable t) {
                    Toast.makeText(context, "check Network Connection", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void loadList() {

        language = sharedPrefrenceConfig.getPrefString(context, Constance.language, "");
        if (Constance.ComeFrom.equals("festivalviewall")) {
            Log.d("dfdfsfd", "dfdfsfd1");
            getFestivalvideolist();
        } else {
            getBusinessVideolist();
        }
    }


    public void getBusinessVideolist() {
        videoCategoriesDataArrayList.clear();
        //Log.d("check_apitoken","1"+sharedPrefrenceConfig.getapitoken().getApi_token());
        Api api = Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<List_of_Video_Category_Name> call = api.businessVideoList(sharedPrefrenceConfig.getPrefString(context, Constance.CToken, null), catnameid, language);

        call.enqueue(new Callback<List_of_Video_Category_Name>() {
            @Override
            public void onResponse(Call<List_of_Video_Category_Name> call, Response<List_of_Video_Category_Name> response) {
                if (response !=null && response.body()!=null){
                    if (response.body().getResult() != null && response.body().getResult().equals("1") ) {
                        if (response.body().getRecords() != null) {
                            model = response.body().getRecords();
                            if (response.body().getRecords().getData() != null) {

                                videoCategoriesDataArrayList.addAll(response.body().getRecords().getData());
                                adapterSingleCatVideoList = new AdapterSingleCatVideoList(context, videoCategoriesDataArrayList);
                                rv_singlevideoitems.setAdapter(adapterSingleCatVideoList);
                                if (videoCategoriesDataArrayList.size() != 0) {
                                    selectedvideopath = videoCategoriesDataArrayList.get(0).getVideo_url();
                                    playVideo();
                                    Log.d("checkvideo", "" + videoCategoriesDataArrayList.get(0).getVideo_url());

                              /*  Glide.with(context).load(videoCategoriesDataArrayList.get(0).getImage_url()).placeholder(R.drawable.placeholder).into(iv_firstimage);
                                finalimage=videoCategoriesDataArrayList.get(0).getImage_url();
                          */
                                } else {
                                    progressDialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("Video Coming soon....")
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    onBackPressed();
                                                }
                                            });

                                    //Creating dialog box
                                    builder.create();
                                    AlertDialog dialog = builder.create();
                                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                        @Override
                                        public void onShow(DialogInterface arg0) {
                                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                        }
                                    });
                                    dialog.show();
                                }

                                progressDialog.dismiss();


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage( response.body().getMessage())
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });

                                //Creating dialog box
                                builder.create();
                                AlertDialog dialog = builder.create();
                                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface arg0) {
                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                    }
                                });
                                dialog.show();
                            }


                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("getrecord null" + response.body().getMessage())
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                        }
                                    });

                            //Creating dialog box
                            builder.create();
                            AlertDialog dialog = builder.create();
                            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface arg0) {
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                }
                            });
                            dialog.show();
                        }
                    } else {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("getresult null" + response.body().getMessage())
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });

                        //Creating dialog box
                        builder.create();
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                            }
                        });
                        dialog.show();

                    }
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<List_of_Video_Category_Name> call, Throwable t) {
                Toast.makeText(context, "check Your internet Connection", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

    }

    public void getFestivalvideolist() {
        videoCategoriesDataArrayList.clear();
        //Log.d("check_apitoken","1"+sharedPrefrenceConfig.getapitoken().getApi_token());
        Api api = Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Log.d("dfdfsfd", "dfdfsfd2");
        Log.d("dfdfsfd", "dfdfsfd" + sharedPrefrenceConfig.getPrefString(context, Constance.CToken, null));
        Log.d("dfdfsfd", "dfdf" + catnameid);
        Log.d("dfdfsfd", "dfdf" + language);


        Call<List_of_Video_Category_Name> call = api.festivalVideoList(sharedPrefrenceConfig.getPrefString(context, Constance.CToken, null), catnameid, language);

        call.enqueue(new Callback<List_of_Video_Category_Name>() {
            @Override
            public void onResponse(Call<List_of_Video_Category_Name> call, Response<List_of_Video_Category_Name> response) {
                if (response !=null && response.body()!=null){
                    if (response.body().getResult() != null && response.body().getResult().equals("1") ) {
                        if (response.body().getRecords() != null) {
                            model=response.body().getRecords();
                            if (response.body().getRecords().getData() != null) {

                                videoCategoriesDataArrayList.addAll(response.body().getRecords().getData());
                                adapterSingleCatVideoList = new AdapterSingleCatVideoList(context, videoCategoriesDataArrayList);
                                rv_singlevideoitems.setAdapter(adapterSingleCatVideoList);
                                if (videoCategoriesDataArrayList.size() != 0) {
                                    selectedvideopath = videoCategoriesDataArrayList.get(0).getVideo_url();
                                    playVideo();
                                    Log.d("checkvideo", "" + videoCategoriesDataArrayList.get(0).getVideo_url());

                              /*  Glide.with(context).load(videoCategoriesDataArrayList.get(0).getImage_url()).placeholder(R.drawable.placeholder).into(iv_firstimage);
                                finalimage=videoCategoriesDataArrayList.get(0).getImage_url();
                          */
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("Video Coming soon....")
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    onBackPressed();
                                                }
                                            });

                                    //Creating dialog box
                                    builder.create();
                                    AlertDialog dialog = builder.create();
                                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                        @Override
                                        public void onShow(DialogInterface arg0) {
                                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                        }
                                    });
                                    dialog.show();
                                }

                                progressDialog.dismiss();


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage( response.body().getMessage())
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });

                                //Creating dialog box
                                builder.create();
                                AlertDialog dialog = builder.create();
                                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface arg0) {
                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                    }
                                });
                                dialog.show();
                            }


                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("getrecord null" + response.body().getMessage())
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                        }
                                    });

                            //Creating dialog box
                            builder.create();
                            AlertDialog dialog = builder.create();
                            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface arg0) {
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                }
                            });
                            dialog.show();
                        }
                    } else {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("getresult null" + response.body().getMessage())
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });

                        //Creating dialog box
                        builder.create();
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                            }
                        });
                        dialog.show();
                    }
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List_of_Video_Category_Name> call, Throwable t) {
                Toast.makeText(context, "check Your internet Connection", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

    }
    public void loadNextDataFromApi_festival() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        // progressBar.setVisibility(View.VISIBLE);

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, model.getNext_page_url(), new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //    Toast.makeText(getApplicationContext(),"HELLO"+response,Toast.LENGTH_LONG).show();
                try {
                    JSONObject mainobj = new JSONObject(response);
                    if (mainobj.getString("message").equals("success")) {
                        JSONObject subobj = mainobj.getJSONObject("records");

                        Gson gson = new Gson();
                        Type type = new TypeToken<VideoCategoriesRecord>() {
                        }.getType();

                        model = gson.fromJson(subobj.toString(), type);
                       /* JSONArray jsonArray = subobj.getJSONArray("data");
                        Type typeForPostArray = new TypeToken<List<CategoriesData>>() {
                        }.getType();*/
                        List<VideoCategoriesData> list = model.getData();

                        if (list.size() != 0) {
                            videoCategoriesDataArrayList.addAll(list);
                            progressDialog.dismiss();
                            adapterSingleCatVideoList.notifyDataSetChanged();
                        }
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs
                Toast.makeText(context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("token",Prefs.getPrefString(context,Constance.auth_token,""));
                params.put("festival_video_category_id", catnameid);
                params.put("language",language);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        //progressBar.setVisibility(View.GONE);
        /*progressDialog.dismiss();*/
    }

    public void loadNextDataFromApi_business() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        // progressBar.setVisibility(View.VISIBLE);

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, model.getNext_page_url(), new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //    Toast.makeText(getApplicationContext(),"HELLO"+response,Toast.LENGTH_LONG).show();
                try {
                    JSONObject mainobj = new JSONObject(response);
                    if (mainobj.getString("message").equals("success")) {
                        JSONObject subobj = mainobj.getJSONObject("records");

                        Gson gson = new Gson();
                        Type type = new TypeToken<VideoCategoriesRecord>() {
                        }.getType();

                        model = gson.fromJson(subobj.toString(), type);
                       /* JSONArray jsonArray = subobj.getJSONArray("data");
                        Type typeForPostArray = new TypeToken<List<CategoriesData>>() {
                        }.getType();*/
                        List<VideoCategoriesData> list = model.getData();

                        if (list.size() != 0) {
                            videoCategoriesDataArrayList.addAll(list);
                            progressDialog.dismiss();
                            adapterSingleCatVideoList.notifyDataSetChanged();
                        }
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs
                Toast.makeText(context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("token",Prefs.getPrefString(context,Constance.auth_token,""));
                params.put("business_video_category_id", catnameid);
                params.put("language",language);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        //progressBar.setVisibility(View.GONE);
        /*progressDialog.dismiss();*/
    }


    public void playVideo() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        iv_vp_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.dismiss();
                Log.d("wseqwqwqwqwqwqwqw", "sdss");
                vv_playvideo.start();
                iv_vp_play.setVisibility(View.GONE);
            }
        });
      /*  try {
            mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(vv_playvideo);
            vv_playvideo.setMediaController(mediacontroller);
            vv_playvideo.setVideoPath(videoCategoriesDataArrayList.get(0).getVideo_url());

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        if (selectedvideopath != null) {
            vv_playvideo.setVideoPath(selectedvideopath);
        } else {
            Toast.makeText(context, "Not get the video path", Toast.LENGTH_LONG).show();
        }
        mediacontroller = new MediaController(context);
        mediacontroller.setMediaPlayer(vv_playvideo);
        vv_playvideo.setMediaController(mediacontroller);
        vv_playvideo.requestFocus();
        //  vv_playvideo.start();
        vv_playvideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                //vv_playvideo.start();
            }
        });
        vv_playvideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                    mp.stop();
                    iv_vp_play.setVisibility(View.VISIBLE);

                }
                //mp.stop();
                vv_playvideo.pause();
                iv_vp_play.setVisibility(View.VISIBLE);

            }
        });
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

    public void calculationForHeight() {
        ViewTreeObserver vto = ll_selectedvideo.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    ll_selectedvideo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    ll_selectedvideo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int heightOfImage;
                int widthOfImage;
                widthOfImage = ll_selectedvideo.getMeasuredWidth();//1080 horizontalview
                heightOfImage = ll_selectedvideo.getMeasuredHeight();//236

                ViewGroup.LayoutParams params = ll_selectedvideo.getLayoutParams();
                params.height = widthOfImage;
                params.width = widthOfImage;
                ll_selectedvideo.setLayoutParams(params);
            }
        });


    }

    void dwonloadVideo() {
        if (!Constance.FileSaveVideoDirectory.exists()) {
            Constance.FileSaveVideoDirectory.mkdir();
        }
        PRDownloader.download(selectedvideopath, Constance.FileSaveVideoDirectory.getPath(), "VideoDemo.mp4")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        Log.d("djjjjj", "onStartOrResume");


                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                        Log.d("djjjjj", "setOnPauseListener");
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        Log.d("djjjjj", "setOnCancelListener");

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        Log.d("djjjjj", "setOnProgressListener" + progress.toString());

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Log.d("djjjjj", "onDownloadComplete");
                        Intent i = new Intent(context, ActivityVideoPreview.class);
                        Log.d("checkvideo", "intent: " + selectedvideopath);
                        i.putExtra("selectedvideopath", selectedvideopath);
                        Log.d("frsrsgh", "" + frame);
                        i.putExtra("framepath", frame);
                        startActivity(i);
                        onBackPressed();
                        File nn = new File(Constance.FileSaveVideoDirectory.getPath() + File.separator + "VideoDemo.mp4");

                        pb_video.setVisibility(View.GONE);

                        /*if (share) {
                            progressDialog.dismiss();
                            String filePath = cvb.getPath()+File.separator + name ;
                            Log.d("dhfshfdsh",""+filePath);
                            if (filePath != null) {
                                Uri mainUri = Uri.parse(filePath);
                                File path=new File(filePath);
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("video/mp4");
                                // Uri uri = Uri.fromFile(Path);
                                Uri uri = FileProvider.getUriForFile(context, context.getString(R.string.file_provider_authority), path);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                context.startActivity(Intent.createChooser(shareIntent, "Share Video"));
                               *//* Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("video/mp4");
                                sharingIntent.putExtra(Intent.EXTRA_STREAM, mainUri);
                                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                try {
                                    context.startActivity(Intent.createChooser(sharingIntent, "Share Video using"));
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(context, "Application not found to open this file", Toast.LENGTH_LONG).show();
                                }*//*
                            }
                        }*/
                    }

                    @Override
                    public void onError(Error error) {
                        Log.d("djjjjj", "onError");

                    }


                });
    }


    Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        float originalWidth = bitmap.getWidth();
        float originalHeight = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        float scalex = wantedWidth / originalWidth;
        float scaley = wantedHeight / originalHeight;
        float xTranslation = 0.0f;
        float yTranslation = (wantedHeight - originalHeight * scaley) / 2.0f;
        m.postTranslate(xTranslation, yTranslation);
        m.preScale(scalex, scaley);
        // m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, m, paint);
        return output;
    }

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + "Google Analytics Testing");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        iv_vp_play.setVisibility(View.VISIBLE);
    }

    public void setselectedvideo(String video_url) {
        selectedvideopath = video_url;
        playVideo();
    }

    public void setFrame(int frame1) {
        rl_frame.setBackgroundResource(frame1);
        Log.d("frsrsgh", "" + frame);
        frame = frame1;
    }
}