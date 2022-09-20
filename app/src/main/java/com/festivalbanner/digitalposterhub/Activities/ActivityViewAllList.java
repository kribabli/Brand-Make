package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.festivalbanner.digitalposterhub.ModelRetrofit.CategoriesData;
import com.festivalbanner.digitalposterhub.ModelRetrofit.CategoriesRecord;
import com.festivalbanner.digitalposterhub.ModelRetrofit.RecordRegister;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeData;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeModel;
import com.festivalbanner.digitalposterhub.Adapters.AdapterCategoryList;
import com.festivalbanner.digitalposterhub.Adapters.AdapterVideoList;
import com.festivalbanner.digitalposterhub.Adapters.AdapterViewAllList;
import com.festivalbanner.digitalposterhub.Adapters.BusinessCatAdepter;
import com.festivalbanner.digitalposterhub.Adapters.GreetingCatAdepter;
import com.festivalbanner.digitalposterhub.AnalyticsApplication;
import com.festivalbanner.digitalposterhub.ModelRetrofit.List_of_Categories_name;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeRecords;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
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
import java.util.List;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityViewAllList extends AppCompatActivity {

    Context context;

    AdView mAdView;
    LinearLayout facbook_ad_banner, ll_select_list, ll_as_festival, ll_as_category;
    TextView tv_viewalltittlename, tv_as_festival, tv_as_category;
    RecyclerView rv_viewalllist, rv_cat_alv;
    //ArrayList<ModelHomeChild> viewalllist;
    ArrayList<CategoriesData> viewalllist;
    AdapterViewAllList adapterViewAllList;
    String parentname, language;
    ImageView iv_backarrow, iv_al_language;
    Tracker mTracker;
    SharedPrefrenceConfig sharedPrefrenceConfig;
    AdapterVideoList adapterVideoList;
    ArrayList<VideoHomeData> businessCatList, videoCategoriesDataArrayList, greetingCatList;

    VideoHomeRecords videomodel, business_model, greeting_model;
    CategoriesRecord festival_model;
    GreetingCatAdepter greetingCatAdepter;
    BusinessCatAdepter businessCatAdepter;


    public PopupWindowHelper popupWindowHelper;
    View popupview_down;
    String frameName[] = {"All","English", "Hindi", "Gujarati"};
    public static ActivityViewAllList instance = null;

    private final String TAG = ActivityViewAllList.class.getSimpleName();

    public ActivityViewAllList() {
        instance = ActivityViewAllList.this;
    }

    public static synchronized ActivityViewAllList getInstance() {
        if (instance == null) {
            instance = new ActivityViewAllList();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_list);

        context = ActivityViewAllList.this;
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
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
        parentname = getIntent().getExtras().getString("parentname");
        tv_viewalltittlename.setText(parentname);

        sharedPrefrenceConfig = new SharedPrefrenceConfig(context);

        language = sharedPrefrenceConfig.getPrefString(context, Constance.language, "");
        viewalllist = new ArrayList<>();

        if (parentname.equals("Business")) {
            rv_viewalllist.setVisibility(View.VISIBLE);
            GridLayoutManager gm_festival = new GridLayoutManager(context, 3);
            rv_viewalllist.setLayoutManager(gm_festival);
            rv_viewalllist.addOnScrollListener(new EndlessRecyclerOnScrollListener(gm_festival) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (business_model != null && business_model.getNext_page_url() != null) {
                        loadNextDataFromApi_business();
                    }
                }
            });
            getBusinessCat();
        }
        if (parentname.equals("Video")) {
            rv_viewalllist.setVisibility(View.VISIBLE);
            ll_select_list.setVisibility(View.VISIBLE);
            GridLayoutManager gm_festival = new GridLayoutManager(context, 3);
            rv_viewalllist.setLayoutManager(gm_festival);
            rv_viewalllist.addOnScrollListener(new EndlessRecyclerOnScrollListener(gm_festival) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (videomodel != null && videomodel.getNext_page_url() != null) {
                        loadNextDataFromApi_video();
                    }
                }
            });
            getVideoFestival();
        }
        if (parentname.equals("Festival")) {
            rv_viewalllist.setVisibility(View.VISIBLE);
            GridLayoutManager gm_festival = new GridLayoutManager(context, 3);
            rv_viewalllist.setLayoutManager(gm_festival);
            rv_viewalllist.addOnScrollListener(new EndlessRecyclerOnScrollListener(gm_festival) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (festival_model != null && festival_model.getNext_page_url() != null) {
                        loadNextDataFromApi_festival();
                    }
                }
            });
            getCategoriesViewAll();
        }
        if (parentname.equals("Greeting")) {
            rv_viewalllist.setVisibility(View.VISIBLE);
            GridLayoutManager gm_festival = new GridLayoutManager(context, 3);
            rv_viewalllist.setLayoutManager(gm_festival);
            rv_viewalllist.addOnScrollListener(new EndlessRecyclerOnScrollListener(gm_festival) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (greeting_model != null && greeting_model.getNext_page_url() != null) {
                        loadNextDataFromApi_greetings();
                    }
                }
            });
            getGreetingCat();
        }

        iv_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
                AdapterCategoryList adapterCategory = new AdapterCategoryList(context, frameName, "allList");
                rv_cat_dd_raw.setAdapter(adapterCategory);
            }
        });

        ll_as_festival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv_viewalllist.setVisibility(View.VISIBLE);
                rv_cat_alv.setVisibility(View.GONE);
                tv_as_festival.setTextColor(getResources().getColor(R.color.colorWhite));
                tv_as_category.setTextColor(getResources().getColor(R.color.colorTheame));
                ll_as_festival.setBackground(getResources().getDrawable(R.drawable.roundcorner_button));
                ll_as_category.setBackground(null);
                GridLayoutManager gm_festival = new GridLayoutManager(context, 3);
                rv_viewalllist.setLayoutManager(gm_festival);
                rv_viewalllist.addOnScrollListener(new EndlessRecyclerOnScrollListener(gm_festival) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        if (videomodel != null && videomodel.getNext_page_url() != null) {
                            loadNextDataFromApi_video();
                        }
                    }
                });
                getVideoFestival();
            }
        });
        ll_as_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv_cat_alv.setVisibility(View.VISIBLE);
                rv_viewalllist.setVisibility(View.GONE);
                tv_as_category.setTextColor(getResources().getColor(R.color.colorWhite));
                tv_as_festival.setTextColor(getResources().getColor(R.color.colorTheame));
                ll_as_festival.setBackground(null);
                ll_as_category.setBackground(getResources().getDrawable(R.drawable.roundcorner_button));
                GridLayoutManager gm_festival2 = new GridLayoutManager(context, 3);
                rv_cat_alv.setLayoutManager(gm_festival2);
                rv_cat_alv.addOnScrollListener(new EndlessRecyclerOnScrollListener(gm_festival2) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        if (videomodel != null && videomodel.getNext_page_url() != null) {
                            loadNextDataFromApi_video();
                        }
                    }
                });
                getVideoBusiness();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + "Google Analytics Testing");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void loadList() {

        if (parentname.equals("Business")) {
            getBusinessCat();
        }
        if (parentname.equals("Video")) {
            getVideoBusiness();
        }
        if (parentname.equals("Festival")) {
            getCategoriesViewAll();
        }
    }

    public void getCategoriesViewAll() {
        viewalllist.clear();
        Log.d("check_apitoken", "1" + sharedPrefrenceConfig.getapitoken().getApi_token());
        Api api = Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<List_of_Categories_name> call = api.getCategoriesNameList(sharedPrefrenceConfig.getapitoken().getApi_token());

        call.enqueue(new Callback<List_of_Categories_name>() {
            @Override
            public void onResponse(Call<List_of_Categories_name> call, Response<List_of_Categories_name> response) {
                Log.d("check_apitoken", "2: " + sharedPrefrenceConfig.getapitoken().getApi_token());
                if (response !=null && response.body()!=null){
                    if (response.body().getResult() != null && response.body().getResult().equals("1") ) {
                        if (response.body().getRecords() != null) {
                            festival_model = response.body().getRecords();

                            if (response.body().getRecords().getData() != null&& response.body().getRecords().getData().size() != 0) {

                                CategoriesRecord record = response.body().getRecords();
                                viewalllist.addAll(response.body().getRecords().getData());
                                adapterViewAllList = new AdapterViewAllList(context, viewalllist);
                                rv_viewalllist.setAdapter(adapterViewAllList);
                           /* AdapterChild_Items_Categories adapterChild_items_categories=new AdapterChild_Items_Categories(context,viewalllist);
                            rv_childlist.setAdapter(adapterChild_items_categories);
                          */
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


            }

            @Override
            public void onFailure(Call<List_of_Categories_name> call, Throwable t) {
                Toast.makeText(context, "check Your internet Connection", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

    }

    public void getGreetingCat() {
        Api api = Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<VideoHomeModel> call = api.getGreetingCategoriesList(sharedPrefrenceConfig.getPrefString(context, Constance.CToken, null));

        call.enqueue(new Callback<VideoHomeModel>() {
            @Override
            public void onResponse(Call<VideoHomeModel> call, Response<VideoHomeModel> response) {
                if (response !=null && response.body()!=null){
                    if (response.body().getResult() != null && response.body().getResult().equals("1") ) {
                        if (response.body().getRecords() != null) {
                            greeting_model = response.body().getRecords();
                            if (response.body().getRecords().getData() != null&& response.body().getRecords().getData().size() != 0) {

                                rv_viewalllist.setVisibility(View.VISIBLE);
                                Log.d("fdfsdkfk", "dsfji" + response.body().getRecords().getData().size());
                                greetingCatList.addAll(response.body().getRecords().getData());
                                greetingCatAdepter = new GreetingCatAdepter(context, R.layout.seeall_list_rawl, greetingCatList);
                                rv_viewalllist.setAdapter(greetingCatAdepter);

                                /* AdapterChild_Items_Categories adapterChild_items_categories=new AdapterChild_Items_Categories(context,viewalllist);
                            rv_childlist.setAdapter(adapterChild_items_categories);
                          */

                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                rv_viewalllist.setVisibility(View.GONE);

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage( response.body().getMessage())
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

    public void getBusinessCat() {
        Api api = Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<VideoHomeModel> call = api.getBusinessCategoriesList(sharedPrefrenceConfig.getPrefString(context, Constance.CToken, null));

        call.enqueue(new Callback<VideoHomeModel>() {
            @Override
            public void onResponse(Call<VideoHomeModel> call, Response<VideoHomeModel> response) {
                if (response !=null && response.body()!=null) {
                    if (response.body().getResult() != null && response.body().getResult().equals("1")) {
                        if (response.body().getRecords() != null) {
                            business_model = response.body().getRecords();
                            if (response.body().getRecords().getData() != null&& response.body().getRecords().getData().size() != 0) {

                                Log.d("fdfsdkfk", "dsfji" + response.body().getRecords().getData().size());
                                businessCatList.addAll(response.body().getRecords().getData());
                                businessCatAdepter = new BusinessCatAdepter(context, R.layout.seeall_list_rawl, businessCatList);
                                rv_viewalllist.setAdapter(businessCatAdepter);
                           /* AdapterChild_Items_Categories adapterChild_items_categories=new AdapterChild_Items_Categories(context,viewalllist);
                            rv_childlist.setAdapter(adapterChild_items_categories);
                          */
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

            }

            @Override
            public void onFailure(Call<VideoHomeModel> call, Throwable t) {
                Toast.makeText(context, "check Your internet Connection", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

    }

    public void getVideoBusiness() {
        videoCategoriesDataArrayList.clear();
        Api api = Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<VideoHomeModel> call = api.getBusinessCategoriesVideoList(sharedPrefrenceConfig.getPrefString(context, Constance.CToken, null));

        call.enqueue(new Callback<VideoHomeModel>() {
            @Override
            public void onResponse(Call<VideoHomeModel> call, Response<VideoHomeModel> response) {
                if (response !=null && response.body()!=null) {
                    if (response.body().getResult() != null && response.body().getResult().equals("1")) {
                        if (response.body().getRecords() != null) {
                            videomodel = response.body().getRecords();
                            if (response.body().getRecords().getData() != null && response.body().getRecords().getData().size() != 0) {
                                Log.d("fdfsdkfk", "dsfji" + response.body().getRecords().getData().size());
                                //Toast.makeText(context,"not null data ",Toast.LENGTH_LONG).show();
                                videoCategoriesDataArrayList.addAll(response.body().getRecords().getData());
                                adapterVideoList = new AdapterVideoList(context, videoCategoriesDataArrayList, "businessviewall");
                                rv_cat_alv.setAdapter(adapterVideoList);
                                progressDialog.dismiss();
                            } else {


                           /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Video Category is coming soon")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dialog.cancel();

                                            startActivity(new Intent(context, ActivityHome.class));


                                        }
                                    });

                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            alert.show();*/
                                progressDialog.dismiss();
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
                            progressDialog.dismiss();
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

            }

            @Override
            public void onFailure(Call<VideoHomeModel> call, Throwable t) {
                Toast.makeText(context, "check Your internet Connection", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

    }

    public void getVideoFestival() {
        videoCategoriesDataArrayList.clear();
        Api api = Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<VideoHomeModel> call = api.festivaslVideoCatList(sharedPrefrenceConfig.getPrefString(context, Constance.CToken, null));

        call.enqueue(new Callback<VideoHomeModel>() {
            @Override
            public void onResponse(Call<VideoHomeModel> call, Response<VideoHomeModel> response) {
                if (response !=null && response.body()!=null){
                    if (response.body().getResult() != null && response.body().getResult().equals("1") ) {
                        if (response.body().getRecords() != null) {
                            videomodel = response.body().getRecords();
                            if (response.body().getRecords().getData() != null && response.body().getRecords().getData().size() != 0) {
                                Log.d("fdfsdkfk", "dsfji" + response.body().getRecords().getData().size());
                                //Toast.makeText(context,"not null data ",Toast.LENGTH_LONG).show();
                                videoCategoriesDataArrayList.addAll(response.body().getRecords().getData());
                                adapterVideoList = new AdapterVideoList(context, videoCategoriesDataArrayList, "festivalviewall");
                                rv_viewalllist.setAdapter(adapterVideoList);
                                progressDialog.dismiss();
                            } else {


                           /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Video Category is coming soon")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dialog.cancel();

                                            startActivity(new Intent(context, ActivityHome.class));


                                        }
                                    });

                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            alert.show();*/
                                progressDialog.dismiss();
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
                            progressDialog.dismiss();
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

    public void bindView() {
        mAdView = findViewById(R.id.adview);
        facbook_ad_banner = findViewById(R.id.facbook_ad_banner);
        tv_viewalltittlename = findViewById(R.id.tv_viewalltittlename);
        rv_viewalllist = findViewById(R.id.rv_viewalllist);
        iv_backarrow = findViewById(R.id.iv_backarrow);
        iv_al_language = findViewById(R.id.iv_al_language);
        ll_select_list = findViewById(R.id.ll_select_list);
        tv_as_festival = findViewById(R.id.tv_as_festival);
        ll_as_festival = findViewById(R.id.ll_as_festival);
        ll_as_category = findViewById(R.id.ll_as_category);
        tv_as_category = findViewById(R.id.tv_as_category);
        rv_cat_alv = findViewById(R.id.rv_cat_alv);
        businessCatList = new ArrayList<>();
        greetingCatList = new ArrayList<>();
        videoCategoriesDataArrayList = new ArrayList<>();
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
                            viewalllist.addAll(list);
                            progressDialog.dismiss();
                            adapterViewAllList.notifyDataSetChanged();
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
        });
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
                       /* JSONArray jsonArray = subobj.getJSONArray("data");
                        Type typeForPostArray = new TypeToken<List<CategoriesData>>() {
                        }.getType();*/
                        List<VideoHomeData> list = greeting_model.getData();


//                        Constance.Next_page_url=fasitavl_model.getNext_page_url();
//                        lastPositionofcatlist=categories_name.size();
                        if (list.size() != 0) {
                            /*tvNoData.setVisibility(View.GONE);
                            rvList.setVisibility(View.VISIBLE);*/
                            greetingCatList.addAll(list);
                            progressDialog.dismiss();
                            greetingCatAdepter.notifyDataSetChanged();
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
        });
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


//                        Constance.Next_page_url=fasitavl_model.getNext_page_url();
//                        lastPositionofcatlist=categories_name.size();
                        if (list.size() != 0) {
                            /*tvNoData.setVisibility(View.GONE);
                            rvList.setVisibility(View.VISIBLE);*/
                            businessCatList.addAll(list);
                            progressDialog.dismiss();
                            businessCatAdepter.notifyDataSetChanged();
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
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        //progressBar.setVisibility(View.GONE);
        /*progressDialog.dismiss();*/
    }

    public void loadNextDataFromApi_video() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
        // progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, videomodel.getNext_page_url(), new com.android.volley.Response.Listener<String>() {
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

                        videomodel = gson.fromJson(subobj.toString(), type);
                       /* JSONArray jsonArray = subobj.getJSONArray("data");
                        Type typeForPostArray = new TypeToken<List<CategoriesData>>() {
                        }.getType();*/
                        List<VideoHomeData> list = videomodel.getData();


//                        Constance.Next_page_url=fasitavl_model.getNext_page_url();
//                        lastPositionofcatlist=categories_name.size();
                        if (list.size() != 0) {
                            /*tvNoData.setVisibility(View.GONE);
                            rvList.setVisibility(View.VISIBLE);*/
                            videoCategoriesDataArrayList.addAll(list);
                            progressDialog.dismiss();
                            adapterVideoList.notifyDataSetChanged();
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
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        //progressBar.setVisibility(View.GONE);
        /*progressDialog.dismiss();*/
    }
}