package com.festivalbanner.digitalposterhub.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.festivalbanner.digitalposterhub.Activities.ActivityHome;
import com.festivalbanner.digitalposterhub.Activities.ActivityViewAllList;
import com.festivalbanner.digitalposterhub.ModelRetrofit.CategoriesData;
import com.festivalbanner.digitalposterhub.ModelRetrofit.CategoriesRecord;
import com.festivalbanner.digitalposterhub.ModelRetrofit.List_of_Categories_name;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeData;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeModel;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeRecords;
import com.festivalbanner.digitalposterhub.Activities.ActivityCreateCustomImage;
import com.festivalbanner.digitalposterhub.Adapters.AdapterChild_Items;
import com.festivalbanner.digitalposterhub.Adapters.AdapterChild_Items_Categories;
import com.festivalbanner.digitalposterhub.Adapters.AdapterHomeParent;
import com.festivalbanner.digitalposterhub.Adapters.AdapterShowHomePager;
import com.festivalbanner.digitalposterhub.Adapters.AdapterVideoList;
import com.festivalbanner.digitalposterhub.Adapters.BusinessCatAdepter;
import com.festivalbanner.digitalposterhub.Adapters.GreetingCatAdepter;
import com.festivalbanner.digitalposterhub.Model.ModelHomeParent;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {

    Context context;
    View view;
    ViewPager vp_home;
    LinearLayout ll_create_custom;
    RecyclerView rv_home_festival, rv_home_video, rv_home_business, rv_greeting;
    AdapterHomeParent adapterHomeParent;
    SharedPrefrenceConfig sharedPrefrenceConfig;
    String language;
    TextView tv_all_video, tv_festival_all, tv_business_all, tv_all_greeting;
    AdapterVideoList adapterVideoList;
    ArrayList<VideoHomeData> videoCategoriesDataArrayList, businessCatList, greetingCatList, bannerList;
    ArrayList<CategoriesData> categories_name;
    AdapterChild_Items adapterChild_items;

    private static final Integer[] image = {R.drawable.pager_1, R.drawable.pager_2,
            R.drawable.pager_3};
    private ArrayList<String> imagearray = new ArrayList();
    AdapterShowHomePager adapterShowHomePager;
    CircleIndicator indicator_home;
    Timer timer;
    Tracker mTracker;
    CategoriesRecord festival_model;
    VideoHomeRecords videomodel, business_model, greeting_model;
    BusinessCatAdepter businessCatAdepter;
    GreetingCatAdepter greetingCatAdepter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
        bindView();
        //setUpViewPager();
        clickListner();

        language = sharedPrefrenceConfig.getPrefString(context, Constance.language, "");

        ActivityHome.getInstance().ll_next.setVisibility(View.GONE);
        ActivityHome.getInstance().ivBack.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        rv_home_video.setLayoutManager(linearLayoutManager);

       /* rv_home_video.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (videomodel != null && videomodel.getNext_page_url() != null) {
                    loadNextDataFromApi_video();
                }
            }
        });*/

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        rv_home_business.setLayoutManager(linearLayoutManager2);

       /* rv_home_business.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager2) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (business_model != null && business_model.getNext_page_url() != null) {
                    loadNextDataFromApi_business();
                }
            }
        });*/

        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        rv_greeting.setLayoutManager(linearLayoutManager4);

       /* rv_greeting.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager4) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (greeting_model != null && greeting_model.getNext_page_url() != null) {
                    loadNextDataFromApi_greetings();
                }
            }
        });*/

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        rv_home_festival.setLayoutManager(linearLayoutManager3);

       /* rv_home_festival.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager3) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (festival_model != null && festival_model.getNext_page_url() != null) {
                    loadNextDataFromApi_festival();
                }
            }
        });*/

        getVideoCategoriesName();
        getBusinessCat();
        getFestivalCatList();
        getGreetingCat();
        bannerSlider();

        tv_festival_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityViewAllList.class);
                i.putExtra("parentname", "Festival");
                context.startActivity(i);
            }
        });

        tv_all_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityViewAllList.class);
                i.putExtra("parentname", "Video");
                context.startActivity(i);
            }
        });

        tv_business_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityViewAllList.class);
                i.putExtra("parentname", "Business");
                context.startActivity(i);
            }
        });

        tv_all_greeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityViewAllList.class);
                i.putExtra("parentname", "Greeting");
                context.startActivity(i);
            }
        });

        return view;
    }


    public void clickListner() {
        ll_create_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(context, ActivityCreateCustomImage.class);
                //   i.putExtra("FromSinglecatActivity",0);
//                startActivity(i);
                ActivityHome.getInstance().loadFragment(new CreateCustomImageFragment());
//                 startActivity(new Intent(context, ActivityCreateCustomImage.class));
            }
        });
    }


    public void bannerSlider() {
        Api api = Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<VideoHomeModel> call = api.bannerImageList();

        call.enqueue(new Callback<VideoHomeModel>() {
            @Override
            public void onResponse(Call<VideoHomeModel> call, Response<VideoHomeModel> response) {
                if (response != null && response.body() != null) {
                    if (response.body().getResult() != null && response.body().getResult().equals("1")) {
                        if (response.body().getRecords() != null) {

                            if (response.body().getRecords().getData() != null && response.body().getRecords().getData().size() != 0) {

                                bannerList.addAll(response.body().getRecords().getData());
                                setUpViewPager();
                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();

                         /*   AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage( response.body().getMessage())
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                        }
                                    });

                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            alert.show();*/

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
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                }
                            });
                            dialog.show();
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
                        builder.create();
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                            }
                        });
                        dialog.show();
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

        vp_home = view.findViewById(R.id.vp_home);
        indicator_home = view.findViewById(R.id.indicator_home);
        rv_home_festival = view.findViewById(R.id.rv_home_festival);
        ll_create_custom = view.findViewById(R.id.ll_create_custom);
        rv_home_video = view.findViewById(R.id.rv_home_video);
        tv_all_video = view.findViewById(R.id.tv_all_video);
        rv_home_business = view.findViewById(R.id.rv_home_business);
        rv_home_festival = view.findViewById(R.id.rv_home_festival);
        tv_festival_all = view.findViewById(R.id.tv_festival_all);
        tv_business_all = view.findViewById(R.id.tv_business_all);
        tv_all_greeting = view.findViewById(R.id.tv_all_greeting);
        rv_greeting = view.findViewById(R.id.rv_greeting);
        videoCategoriesDataArrayList = new ArrayList<>();
        businessCatList = new ArrayList<>();
        greetingCatList = new ArrayList<>();
        categories_name = new ArrayList<>();
        bannerList = new ArrayList<>();

    }

    public void setUpViewPager() {
       /* for (int i = 0; i < image.length; i++)
            imagearray.add(image[i]);
*/
        adapterShowHomePager = new AdapterShowHomePager(context, bannerList);
        vp_home.setAdapter(adapterShowHomePager);
        indicator_home.setViewPager(vp_home);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                vp_home.post(new Runnable() {
                    @Override
                    public void run() {
                        vp_home.setCurrentItem((vp_home.getCurrentItem() + 1) % bannerList.size());
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);

    }

    public static ArrayList<ModelHomeParent> getHomeParentTittleList() {

        ArrayList<ModelHomeParent> data = new ArrayList<>();
        // data.add(new ModelHomeParent("Upcoming Festival & days"));
        data.add(new ModelHomeParent("Categories"));

        return data;
    }

    public void getFestivalCatList() {
        Api api = Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        String token = sharedPrefrenceConfig.getPrefString(context, Constance.CToken, "");
        Log.e("token", "token : " + token);
        Call<List_of_Categories_name> call = api.getCategoriesNameList(token);

        call.enqueue(new Callback<List_of_Categories_name>() {
            @Override
            public void onResponse(Call<List_of_Categories_name> call, Response<List_of_Categories_name> response) {
                // Log.d("check_apitoken","getResult :"+ response.body());
//                Log.d("check_apitoken","getMessage :"+response.body().getMessage());
                if (response != null && response.body() != null) {
                    if (response.body().getResult() != null && response.body().getResult().equals("1")) {
                        if (response.body().getRecords() != null) {
                            festival_model = response.body().getRecords();
                            if (response.body().getRecords().getData() != null) {
                                // categoriesRecord = response.body().getRecords();
                                rv_home_festival.setVisibility(View.VISIBLE);
                                categories_name.addAll(response.body().getRecords().getData());
                                // Constance.Next_page_url=categoriesRecord.getNext_page_url();
                                AdapterChild_Items_Categories adapterChild_items_categories = new AdapterChild_Items_Categories(context, categories_name);
                                rv_home_festival.setAdapter(adapterChild_items_categories);
                                progressDialog.dismiss();

                               /* categories_recyclerview_adapter=new Categories_Recyclerview_Adapter(layout.getContext(),categories_name,"cat");
                                // Toast.makeText(getApplicationContext(),"hello"+quotelist.size(),Toast.LENGTH_LONG).show();

                                rv_home_fragment.setAdapter(categories_recyclerview_adapter);
                              */

                                // loader.setVisibility(View.GONE);


                            } else {
                                rv_home_festival.setVisibility(View.GONE);
                          /*  AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("getdata null"+response.body().getMessage())
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                        }
                                    });

                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            alert.show();*/
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
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                }
                            });
                            dialog.show();
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
                        builder.create();
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                            }
                        });
                        dialog.show();
                        progressDialog.dismiss();
                    }
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List_of_Categories_name> call, Throwable t) {
                Toast.makeText(context, "check Your internet Connection", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }

    public void getVideoCategoriesName() {
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
                if (response != null && response.body() != null) {
                    if (response.body().getResult() != null && response.body().getResult().equals("1")) {
                        if (response.body().getRecords() != null) {
                            videomodel = response.body().getRecords();
                            if (response.body().getRecords().getData() != null && response.body().getRecords().getData().size() != 0) {
                                rv_home_video.setVisibility(View.VISIBLE);
                                Log.d("fdfsdkfk", "dsfji" + response.body().getRecords().getData().size());
                                //Toast.makeText(context,"not null data ",Toast.LENGTH_LONG).show();
                                videoCategoriesDataArrayList.addAll(response.body().getRecords().getData());
                                adapterVideoList = new AdapterVideoList(context, videoCategoriesDataArrayList, "home");
                                rv_home_video.setAdapter(adapterVideoList);
                                progressDialog.dismiss();
                            } else {
                                rv_home_video.setVisibility(View.GONE);
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
                            builder.create();
                            AlertDialog dialog = builder.create();
                            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface arg0) {
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                }
                            });
                            dialog.show();
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
                        builder.create();
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                            }
                        });
                        dialog.show();
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
                if (response != null && response.body() != null) {
                    if (response.body().getResult() != null && response.body().getResult().equals("1")) {
                        if (response.body().getRecords() != null) {
                            business_model = response.body().getRecords();
                            if (response.body().getRecords().getData() != null) {

                                rv_home_business.setVisibility(View.VISIBLE);
                                Log.d("fdfsdkfk", "dsfji" + response.body().getRecords().getData().size());
                                businessCatList.addAll(response.body().getRecords().getData());
                                businessCatAdepter = new BusinessCatAdepter(context, R.layout.item_rv_child_category, businessCatList);
                                rv_home_business.setAdapter(businessCatAdepter);
                           /* AdapterChild_Items_Categories adapterChild_items_categories=new AdapterChild_Items_Categories(context,viewalllist);
                            rv_childlist.setAdapter(adapterChild_items_categories);
                          */
                                progressDialog.dismiss();


                            } else {
                                rv_home_business.setVisibility(View.GONE);

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
                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
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
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                }
                            });
                            dialog.show();
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
                        builder.create();
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                            }
                        });
                        dialog.show();
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
                if (response != null && response.body() != null) {
                    if (response.body().getResult() != null && response.body().getResult().equals("1")) {
                        if (response.body().getRecords() != null) {
                            greeting_model = response.body().getRecords();
                            if (response.body().getRecords().getData() != null) {

                                rv_greeting.setVisibility(View.VISIBLE);
                                Log.d("fdfsdkfk", "dsfji" + response.body().getRecords().getData().size());
                                greetingCatList.addAll(response.body().getRecords().getData());
                                greetingCatAdepter = new GreetingCatAdepter(context, R.layout.item_rv_child_category, greetingCatList);
                                rv_greeting.setAdapter(greetingCatAdepter);
                           /* AdapterChild_Items_Categories adapterChild_items_categories=new AdapterChild_Items_Categories(context,viewalllist);
                            rv_childlist.setAdapter(adapterChild_items_categories);
                          */
                                progressDialog.dismiss();


                            } else {
                                rv_greeting.setVisibility(View.GONE);

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
                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
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
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                }
                            });
                            dialog.show();
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
                        builder.create();
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                            }
                        });
                        dialog.show();
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

  /*  public void loadNextDataFromApi_festival() {
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
                       *//* JSONArray jsonArray = subobj.getJSONArray("data");
                        Type typeForPostArray = new TypeToken<List<CategoriesData>>() {
                        }.getType();*//*
                        List<CategoriesData> list = festival_model.getData();


//                        Constance.Next_page_url=fasitavl_model.getNext_page_url();
//                        lastPositionofcatlist=categories_name.size();
                        if (categories_name.size() != 0) {
                            *//*tvNoData.setVisibility(View.GONE);
                            rvList.setVisibility(View.VISIBLE);*//*
                            categories_name.addAll(list);
                            progressDialog.dismiss();
                            adapterChild_items.notifyDataSetChanged();
                            *//*if (lastPositionofcatlist > categories_name.size()) {
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
                            }*//*
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
        *//*progressDialog.dismiss();*//*
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
                       *//* JSONArray jsonArray = subobj.getJSONArray("data");
                        Type typeForPostArray = new TypeToken<List<CategoriesData>>() {
                        }.getType();*//*
                        List<VideoHomeData> list = videomodel.getData();


//                        Constance.Next_page_url=fasitavl_model.getNext_page_url();
//                        lastPositionofcatlist=categories_name.size();
                        if (list.size() != 0) {
                            *//*tvNoData.setVisibility(View.GONE);
                            rvList.setVisibility(View.VISIBLE);*//*
                            videoCategoriesDataArrayList.addAll(list);
                            progressDialog.dismiss();
                            adapterVideoList.notifyDataSetChanged();
                            *//*if (lastPositionofcatlist > categories_name.size()) {
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
                            }*//*
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
        *//*progressDialog.dismiss();*//*
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
                       *//* JSONArray jsonArray = subobj.getJSONArray("data");
                        Type typeForPostArray = new TypeToken<List<CategoriesData>>() {
                        }.getType();*//*
                        List<VideoHomeData> list = business_model.getData();


//                        Constance.Next_page_url=fasitavl_model.getNext_page_url();
//                        lastPositionofcatlist=categories_name.size();
                        if (list.size() != 0) {
                            *//*tvNoData.setVisibility(View.GONE);
                            rvList.setVisibility(View.VISIBLE);*//*
                            businessCatList.addAll(list);
                            progressDialog.dismiss();
                            businessCatAdepter.notifyDataSetChanged();
                            *//*if (lastPositionofcatlist > categories_name.size()) {
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
                            }*//*
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
        *//*progressDialog.dismiss();*//*
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
                       *//* JSONArray jsonArray = subobj.getJSONArray("data");
                        Type typeForPostArray = new TypeToken<List<CategoriesData>>() {
                        }.getType();*//*
                        List<VideoHomeData> list = greeting_model.getData();


//                        Constance.Next_page_url=fasitavl_model.getNext_page_url();
//                        lastPositionofcatlist=categories_name.size();
                        if (list.size() != 0) {
                            *//*tvNoData.setVisibility(View.GONE);
                            rvList.setVisibility(View.VISIBLE);*//*
                            greetingCatList.addAll(list);
                            progressDialog.dismiss();
                            greetingCatAdepter.notifyDataSetChanged();
                            *//*if (lastPositionofcatlist > categories_name.size()) {
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
                            }*//*
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
        *//*progressDialog.dismiss();*//*
    }
*/

}
