package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.festivalbanner.digitalposterhub.ModelRetrofit.CategoriesData;
import com.festivalbanner.digitalposterhub.ModelRetrofit.CategoriesRecord;
import com.festivalbanner.digitalposterhub.ModelRetrofit.List_of_Categories_name;
import com.festivalbanner.digitalposterhub.ModelRetrofit.RecordRegister;
import com.festivalbanner.digitalposterhub.ModelRetrofit.ResponseLogin;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeData;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeModel;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeRecords;
import com.festivalbanner.digitalposterhub.Adapters.AdapterCategoryList;
import com.festivalbanner.digitalposterhub.Adapters.AdapterSingleCatList;
import com.festivalbanner.digitalposterhub.Adapters.BusinessListAdapter;
import com.festivalbanner.digitalposterhub.Adapters.GreetingListAdapter;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySingleCategoyList extends AppCompatActivity {

    Context context;
    AdView mAdView;
    LinearLayout facbook_ad_banner;
    RecyclerView rv_singlecatlist;
    TextView tv_singlecatname;
    String childitemtittle, catnameid, catType = "All";
    ImageView iv_backarrow, iv_firstimage, iv_al_language;
    ArrayList<CategoriesData> modelsingleChildList;
    ArrayList<CategoriesData> temp = new ArrayList<>();
    ArrayList<VideoHomeData> businessList, greetingImage;
    ArrayList<VideoHomeData> temp2 = new ArrayList<>();
    public AdapterSingleCatList adapterSingleCatList;
    public BusinessListAdapter businessListAdapter;
    public GreetingListAdapter greetingListAdapter;
    LinearLayout ll_next_singlecatlist;
    public static String finalimage;
    public static ActivitySingleCategoyList instance = null;
    SharedPrefrenceConfig sharedPrefrenceConfig;
    LinearLayout ll_selectedimageview;
    Tracker mTracker;
    String language;

    public PopupWindowHelper popupWindowHelper;

    View popupview_down;
    String frameName[] = {"All", "English", "Hindi", "Gujarati"};
    VideoHomeRecords business_model, greeting_model;
    CategoriesRecord festival_model;
    TextView tv_all, tv_guj, tv_hindi, tv_eng;

    public ActivitySingleCategoyList() {
        instance = ActivitySingleCategoyList.this;
    }

    public static synchronized ActivitySingleCategoyList getInstance() {
        if (instance == null) {
            instance = new ActivitySingleCategoyList();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_categoy_list);
        context = ActivitySingleCategoyList.this;
        bindView();
        sharedPrefrenceConfig = new SharedPrefrenceConfig(context);
        language = sharedPrefrenceConfig.getPrefString(context, Constance.language, "");
        calculationForHeight();
        SharedPrefrenceConfig pref = new SharedPrefrenceConfig(context);
        RecordRegister getuser = pref.getUser();

//        if (getuser.getPlan_type() == null || !getuser.getPlan_type().equals("Premium")) {
        if (!Constance.isPremium) {
            if (Constance.adType.equals("Ad Mob")) {
                loadAdMobAd();
            } else {
                loadFacebookAd();
            }
        } else {
            mAdView.setVisibility(View.GONE);
            facbook_ad_banner.setVisibility(View.GONE);
        }

        getprofiledetails();
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        childitemtittle = getIntent().getExtras().getString("childitemtittle");
        catType = getIntent().getExtras().getString("catType");
        catnameid = getIntent().getExtras().getString("catnameid");
        Log.d("fdfjsfi", "jfdsfisf" + catnameid);

        tv_singlecatname.setText(childitemtittle);
        GridLayoutManager gm = new GridLayoutManager(context, 3);
        rv_singlecatlist.setLayoutManager(gm);

        modelsingleChildList = new ArrayList<>();

        if (catType.equals("festival")) {
            getcatimagelist();
            rv_singlecatlist.addOnScrollListener(new EndlessRecyclerOnScrollListener(gm) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (festival_model != null && festival_model.getNext_page_url() != null) {
                        loadNextDataFromApi_festival();
                    }
                }
            });
        }

        if (catType.equals("business")) {
            getBusinesslist();
            rv_singlecatlist.addOnScrollListener(new EndlessRecyclerOnScrollListener(gm) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (business_model != null && business_model.getNext_page_url() != null) {
                        loadNextDataFromApi_business();
                    }
                }
            });
        }

        if (catType.equals("greeting")) {
            getGreetinImageslist();
            rv_singlecatlist.addOnScrollListener(new EndlessRecyclerOnScrollListener(gm) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (greeting_model != null && greeting_model.getNext_page_url() != null) {
                        loadNextDataFromApi_greetings();
                    }
                }
            });
        }

        // modelsingleChildList.clear();
        // modelsingleChildList.addAll(Constance.childDataList);
       /* adapterSingleCatList = new AdapterSingleCatList(context, modelsingleChildList);
        rv_singlecatlist.setAdapter(adapterSingleCatList);*/
        //   iv_firstimage.setImageResource(modelsingleChildList.get(0).getChilditemimage());
        //  finalimage = modelsingleChildList.get(0).getChilditemimage();

        iv_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ll_next_singlecatlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constance.ComeFrom = "image";
                Constance.FromSinglecatActivity = finalimage;
                Log.d("sdasjdiaisdi", "sdhjsd" + Constance.FromSinglecatActivity);
                if (Admin.tinyDB.getBoolean("login")) {
                    Log.d("dfdjfjdfj", "dfuidufiu" + sharedPrefrenceConfig.getPrefString(context, Constance.Status, ""));

                    Constance.activityName = catType;
                    Intent i = new Intent(context, ActivityCreatePost.class);
                    startActivity(i);
                }
                // ActivityCreateCustomImage.getInstance().setbackgroundSingleImageResource(finalimage);
            }
        });

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

                AdapterCategoryList adapterCategory = new AdapterCategoryList(context, frameName, "single");
                rv_cat_dd_raw.setAdapter(adapterCategory);
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

    private void setdefaultlang() {
        if (catType.equals("festival")) {

            if (modelsingleChildList != null && modelsingleChildList.size() > 0) {
                adapterSingleCatList = new AdapterSingleCatList(context, modelsingleChildList);
                rv_singlecatlist.setAdapter(adapterSingleCatList);
            }
        } else if (catType.equals("business")) {

            if (businessList != null && businessList.size() > 0) {
                businessListAdapter = new BusinessListAdapter(context, businessList);
                rv_singlecatlist.setAdapter(businessListAdapter);
            }

        } else if (catType.equals("greeting")) {

            if (greetingImage != null && greetingImage.size() > 0) {
                businessListAdapter = new BusinessListAdapter(context, greetingImage);
                rv_singlecatlist.setAdapter(businessListAdapter);
            }

        }
    }

    private void filterlanguagearray(String language) {
        if (catType.equals("festival")) {
            temp = new ArrayList<>();
            temp.clear();
            if (modelsingleChildList != null && modelsingleChildList.size() > 0) {
                for (int i = 0; i < modelsingleChildList.size(); i++) {
                    if (modelsingleChildList.get(i).getLanguage().equals(language)) {
                        temp.add(modelsingleChildList.get(i));
                    }
                }
                adapterSingleCatList = new AdapterSingleCatList(context, temp);
                rv_singlecatlist.setAdapter(adapterSingleCatList);
            }
        } else if (catType.equals("business")) {
            temp2 = new ArrayList<>();
            temp2.clear();
            if (businessList != null && businessList.size() > 0) {
                for (int i = 0; i < businessList.size(); i++) {
                    if (businessList.get(i).getLanguage().equals(language)) {
                        temp2.add(businessList.get(i));
                    }
                }
                businessListAdapter = new BusinessListAdapter(context, temp2);
                rv_singlecatlist.setAdapter(businessListAdapter);
            }
        } else if (catType.equals("greeting")) {
            temp2 = new ArrayList<>();
            temp2.clear();
            if (greetingImage != null && greetingImage.size() > 0) {
                for (int i = 0; i < greetingImage.size(); i++) {
                    if (greetingImage.get(i).getLanguage().equals(language)) {
                        temp2.add(greetingImage.get(i));
                    }
                }
                businessListAdapter = new BusinessListAdapter(context, temp2);
                rv_singlecatlist.setAdapter(businessListAdapter);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("fhauhfdsfsdf", "dhasufhuashn");
        mTracker.setScreenName("Image~" + "Google Analytics Testing");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void loadList() {

        language = sharedPrefrenceConfig.getPrefString(context, Constance.language, "");
        if (catType.equals("festival")) {
            getcatimagelist();
        }

        if (catType.equals("business")) {
            getBusinesslist();
        }
        if (catType.equals("greeting")) {
            getGreetinImageslist();
        }
    }

    public void getprofiledetails() {
        Api api = Base_Url.getClient().create(Api.class);
        String token = sharedPrefrenceConfig.getPrefString(context, Constance.CToken, null);
        if (token != null && !token.equals("")) {
            Call<ResponseLogin> call = api.getProfileDetails(token);
            call.enqueue(new Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                    if (response != null && response.body() != null) {
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
                                AlertDialog alert = builder.create();
                                alert.show();
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
                            AlertDialog alert = builder.create();
                            alert.show();
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

    public void bindView() {
        mAdView = findViewById(R.id.adview);
        facbook_ad_banner = findViewById(R.id.facbook_ad_banner);
        rv_singlecatlist = findViewById(R.id.rv_singlecatlist);
        tv_singlecatname = findViewById(R.id.tv_singlecatname);
        iv_backarrow = findViewById(R.id.iv_backarrow);
        iv_firstimage = findViewById(R.id.iv_firstimage);
        ll_next_singlecatlist = findViewById(R.id.ll_next_singlecatlist);
        ll_selectedimageview = findViewById(R.id.ll_selectedimageview);
        iv_al_language = findViewById(R.id.iv_al_language);
        businessList = new ArrayList<>();
        greetingImage = new ArrayList<>();

        iv_al_language.setVisibility(View.GONE);
        tv_all = findViewById(R.id.tv_all);
        tv_guj = findViewById(R.id.tv_guj);
        tv_hindi = findViewById(R.id.tv_hindi);
        tv_eng = findViewById(R.id.tv_eng);

    }

    public void getcatimagelist() {
        modelsingleChildList.clear();
        Log.d("check_apitoken", "1" + sharedPrefrenceConfig.getapitoken().getApi_token());
        Api api = Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<List_of_Categories_name> call = api.getCategoriesImageList(sharedPrefrenceConfig.getPrefString(context, Constance.CToken, null), catnameid, language);

        call.enqueue(new Callback<List_of_Categories_name>() {
            @Override
            public void onResponse(Call<List_of_Categories_name> call, Response<List_of_Categories_name> response) {
                if (response != null && response.body() != null) {
                    if (response.body().getResult() != null && response.body().getResult().equals("1")) {
                        if (response.body().getRecords() != null) {
                            festival_model = response.body().getRecords();
                            if (response.body().getRecords().getData() != null && response.body().getRecords().getData().size() > 0) {

                                // CategoriesRecord record = response.body().getRecords();

                                modelsingleChildList.addAll(response.body().getRecords().getData());
                                adapterSingleCatList = new AdapterSingleCatList(context, modelsingleChildList);
                                rv_singlecatlist.setAdapter(adapterSingleCatList);
                                if (modelsingleChildList.size() != 0) {
                                    Glide.with(context).load(modelsingleChildList.get(0).getImage_url()).placeholder(R.drawable.placeholder).into(iv_firstimage);
                                    finalimage = modelsingleChildList.get(0).getImage_url();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("Items Coming soon....")
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    onBackPressed();
                                                }
                                            });
                                    //Creating dialog box
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }

                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage(response.body().getMessage())
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });

                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                alert.show();
                            }

                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("getrecord null" + response.body().getMessage())
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                        }
                                    });

                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        progressDialog.dismiss();
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
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List_of_Categories_name> call, Throwable t) {
                Log.e("error", "response : " + t.getMessage());
//                Toast.makeText(context, "check Your internet Connection", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

    }

    public void getBusinesslist() {
        businessList.clear();
        Api api = Base_Url.getClient().create(Api.class);

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Call<VideoHomeModel> call = api.getBusinessList(sharedPrefrenceConfig.getPrefString(context, Constance.CToken, null), catnameid, language);
        call.enqueue(new Callback<VideoHomeModel>() {
            @Override
            public void onResponse(Call<VideoHomeModel> call, Response<VideoHomeModel> response) {
                if (response != null && response.body() != null) {
                    if (response.body().getResult() != null && response.body().getResult().equals("1")) {
                        if (response.body().getRecords() != null) {
                            business_model = response.body().getRecords();
                            if (response.body().getRecords().getData() != null) {
                                // CategoriesRecord record = response.body().getRecords();
                                businessList.addAll(response.body().getRecords().getData());
                                businessListAdapter = new BusinessListAdapter(context, businessList);
                                rv_singlecatlist.setAdapter(businessListAdapter);
                                if (businessList.size() != 0) {
                                    Glide.with(context).load(businessList.get(0).getImage_url()).placeholder(R.drawable.placeholder).into(iv_firstimage);
                                    finalimage = businessList.get(0).getImage_url();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("Items Coming soon....")
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    onBackPressed();
                                                }
                                            });
                                    //Creating dialog box
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage(response.body().getMessage())
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                alert.show();
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
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("getresult null" + response.body().getMessage())
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        alert.show();
                        progressDialog.dismiss();
                    }
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<VideoHomeModel> call, Throwable t) {
                Toast.makeText(context, "check Your internet Connection", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    public void getGreetinImageslist() {
        Log.d("fdfjsfi", "jfdsfisf" + catnameid);

        greetingImage.clear();
        Api api = Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<VideoHomeModel> call = api.getGreetingImageList(sharedPrefrenceConfig.getPrefString(context, Constance.CToken, null), catnameid, language);
        call.enqueue(new Callback<VideoHomeModel>() {
            @Override
            public void onResponse(Call<VideoHomeModel> call, Response<VideoHomeModel> response) {
                if (response != null && response.body() != null) {
                    if (response.body().getResult() != null && response.body().getResult().equals("1")) {
                        if (response.body().getRecords() != null) {
                            greeting_model = response.body().getRecords();
                            if (response.body().getRecords().getData() != null) {

                                // CategoriesRecord record = response.body().getRecords();

                                greetingImage.addAll(response.body().getRecords().getData());

                                greetingListAdapter = new GreetingListAdapter(context, greetingImage);
                                rv_singlecatlist.setAdapter(greetingListAdapter);
                                if (greetingImage.size() != 0) {
                                    Glide.with(context).load(greetingImage.get(0).getImage_url()).placeholder(R.drawable.placeholder).into(iv_firstimage);
                                    finalimage = greetingImage.get(0).getImage_url();
                                } else {
                                    progressDialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("Items Coming soon....")
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    onBackPressed();
                                                }
                                            });

                                    //Creating dialog box
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }

                                progressDialog.dismiss();


                            } else {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage(response.body().getMessage())
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });

                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                alert.show();
                            }


                        } else {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("getrecord null" + response.body().getMessage())
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                        }
                                    });

                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            alert.show();
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
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<VideoHomeModel> call, Throwable t) {
                Toast.makeText(context, "check Your internet Connection", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

    }

    public void setselectedimage(String childitemimage) {
        finalimage = childitemimage;
        Glide.with(context).load(childitemimage).placeholder(R.drawable.placeholder).into(iv_firstimage);
        //  iv_firstimage.setImageResource(childitemimage);
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
        ViewTreeObserver vto = ll_selectedimageview.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    ll_selectedimageview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    ll_selectedimageview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int heightOfImage;
                int widthOfImage;
                widthOfImage = ll_selectedimageview.getMeasuredWidth();//1080 horizontalview
                heightOfImage = ll_selectedimageview.getMeasuredHeight();//236

                ViewGroup.LayoutParams params = ll_selectedimageview.getLayoutParams();
                params.height = widthOfImage;
                params.width = widthOfImage;
                ll_selectedimageview.setLayoutParams(params);
            }
        });

       /* // displayheight
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayMetrics_height = displayMetrics.heightPixels;
        int displayMetrics_width = displayMetrics.widthPixels;
        Log.d("mGLTextureView","displayMetrics_height :"+displayMetrics_height);
        Log.d("mGLTextureView","displayMetrics_width :"+displayMetrics_width);

        //linearlayout view
        ViewTreeObserver vto = mGLTextureView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    ll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    ll.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width  = mGLTextureView.getMeasuredWidth();//1080 horizontalview
                videoview_height = mGLTextureView.getMeasuredHeight();//236
                Log.d("mGLTextureView","videoview_height :"+videoview_height);
                //Log.d("mGLTextureView","displaywidth :"+width);

            }
        });

        //horizontal view
        ViewTreeObserver vto1 = horizontal.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    horizontal.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    horizontal.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width  = horizontal.getMeasuredWidth();//1080 horizontalview
                 horizontal_viewheight = horizontal.getMeasuredHeight();//236
                Log.d("mGLTextureView","horizontal_viewheight :"+ horizontal_viewheight);
                //Log.d("mGLTextureView","displaywidth :"+width);

            }
        });

        int plus=dpToPx(50)+dpToPx(80)+ dpToPx(50)+dpToPx(14)+dpToPx(14);
        int finalplus=horizontal_viewheight+plus;
        Log.d("mGLTextureView","plus :"+plus);

        int minus=displayMetrics_height-finalplus;
       // int lastminus=minus-StatusBar_height;
        int lastminus=minus-statusBarHeight(getResources());
        Constance.heightOfVideo=lastminus;

        Log.d("mGLTextureView","minus :"+minus);
        Log.d("mGLTextureView","lastminus :"+lastminus);
*/
    }

    public void loadNextDataFromApi_festival() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
        // progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, festival_model.getNext_page_url(), new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //    Toast.makeText(getApplicationContext(),"HELLO"+response,Toast.LENGTH_LONG).show();
                try {
                    JSONObject mainobj = new JSONObject(response);
                    if (mainobj.getString("message").equals("success")) {
                        JSONObject subobj = mainobj.getJSONObject("records");

                        Gson gson = new Gson();
                        Type type = new TypeToken<CategoriesRecord>() {
                        }.getType();

                        festival_model = gson.fromJson(subobj.toString(), type);
                       /* JSONArray jsonArray = subobj.getJSONArray("data");
                        Type typeForPostArray = new TypeToken<List<CategoriesData>>() {
                        }.getType();*/
                        List<CategoriesData> list = festival_model.getData();


//                        Constance.Next_page_url=fasitavl_model.getNext_page_url();
//                        lastPositionofcatlist=categories_name.size();
                        if (list.size() != 0) {
                            /*tvNoData.setVisibility(View.GONE);
                            rvList.setVisibility(View.VISIBLE);*/
                            modelsingleChildList.addAll(list);
                            progressDialog.dismiss();
                            adapterSingleCatList.notifyDataSetChanged();
                            /*if (lastPositionofcatlist > categories_name.size()) {
                                loadNextDataFromApi(holder);
                                //  Toast.makeText(getApplicationContext(),"HELLO"+list.size(),Toast.LENGTH_LONG).show();
                                // loader.setVisibility(View.GONE);
                                progressDialog.dismiss();
                            } else {
                                if (lastPositionofcatlist != 0) {
                                    holder.rv_childlist.scrollToPosition(lastPositionofcatlist);
                                    lastPositionofcatlist = 0;
                                    // loader.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                }
                                progressDialog.dismiss();
                            }*/
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
                params.put("festival_image_category_id", catnameid);
                params.put("language", language);
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
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, business_model.getNext_page_url(), new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //    Toast.makeText(getApplicationContext(),"HELLO"+response,Toast.LENGTH_LONG).show();
                try {
                    JSONObject mainobj = new JSONObject(response);
                    if (mainobj.getString("message").equals("success")) {
                        JSONObject subobj = mainobj.getJSONObject("records");

                        Gson gson = new Gson();
                        Type type = new TypeToken<VideoHomeRecords>() {
                        }.getType();

                        business_model = gson.fromJson(subobj.toString(), type);
                       /* JSONArray jsonArray = subobj.getJSONArray("data");
                        Type typeForPostArray = new TypeToken<List<CategoriesData>>() {
                        }.getType();*/
                        List<VideoHomeData> list = business_model.getData();

                        if (list.size() != 0) {
                            businessList.addAll(list);
                            progressDialog.dismiss();
                            businessListAdapter.notifyDataSetChanged();
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
                params.put("business_image_category_id", catnameid);
                params.put("language", language);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        //progressBar.setVisibility(View.GONE);
        /*progressDialog.dismiss();*/
    }

    public void loadNextDataFromApi_greetings() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
        // progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, greeting_model.getNext_page_url(), new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //    Toast.makeText(getApplicationContext(),"HELLO"+response,Toast.LENGTH_LONG).show();
                try {
                    JSONObject mainobj = new JSONObject(response);
                    if (mainobj.getString("message").equals("success")) {
                        JSONObject subobj = mainobj.getJSONObject("records");

                        Gson gson = new Gson();
                        Type type = new TypeToken<VideoHomeRecords>() {
                        }.getType();

                        greeting_model = gson.fromJson(subobj.toString(), type);
                        List<VideoHomeData> list = greeting_model.getData();

                        if (list.size() != 0) {
                            greetingImage.addAll(list);
                            progressDialog.dismiss();
                            greetingListAdapter.notifyDataSetChanged();
                        }
                        progressDialog.dismiss();
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
                params.put("greeting_id", catnameid);
                params.put("language", language);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        //progressBar.setVisibility(View.GONE);
        /*progressDialog.dismiss();*/
    }

}