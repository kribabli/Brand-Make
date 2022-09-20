package com.festivalbanner.digitalposterhub.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.festivalbanner.digitalposterhub.Activities.ActivityHome;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeData;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeModel;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeRecords;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
import com.festivalbanner.digitalposterhub.Adapters.AdapterVideoList;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.festivalbanner.digitalposterhub.Utills.EndlessRecyclerOnScrollListener;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentVideos extends Fragment {

    Context context;
    View view;
    SharedPrefrenceConfig sharedPrefrenceConfig;
    RecyclerView rv_viewalllist,rv_cat_alv;
    Tracker mTracker;
    String language;
    LinearLayout ll_select_list,ll_as_festival,ll_as_category;
    TextView tv_as_festival,tv_as_category;
    ArrayList<VideoHomeData> videoCategoriesDataArrayList;
    AdapterVideoList adapterVideoList;
    VideoHomeRecords videomodel, videomodel2;
    GridLayoutManager gm_festival, gm_festival2;
    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener, endlessRecyclerOnScrollListener2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_videos, container, false);
        context = getContext();
        sharedPrefrenceConfig = new SharedPrefrenceConfig(context);
        language = sharedPrefrenceConfig.getPrefString(context, Constance.language, "");

        bindView();
        GridLayoutManager gm_festival = new GridLayoutManager(context, 3);
        GridLayoutManager gm_festival2 = new GridLayoutManager(context, 3);
        rv_viewalllist.setLayoutManager(gm_festival);
        rv_cat_alv.setLayoutManager(gm_festival2);
        ll_select_list.setVisibility(View.VISIBLE);
        ActivityHome.getInstance().ll_next.setVisibility(View.GONE);
        getVideoFestival();
          rv_viewalllist.addOnScrollListener(new EndlessRecyclerOnScrollListener(gm_festival) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (videomodel2 != null && videomodel2.getNext_page_url() != null) {
                    loadNextDataFromApi_videoF();
                }
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
                getVideoBusiness();
            }
        });

        attachmanager();
        return view;
    }
    private void attachmanager(){
        gm_festival = new GridLayoutManager(context, 3);
        gm_festival2 = new GridLayoutManager(context, 3);
        rv_viewalllist.setLayoutManager(gm_festival);

        ll_select_list.setVisibility(View.VISIBLE);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(gm_festival) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (videomodel2 != null && videomodel2.getNext_page_url() != null) {
                    loadNextDataFromApi_videoF();
                }
            }
        };
        rv_viewalllist.addOnScrollListener(endlessRecyclerOnScrollListener);


        rv_cat_alv.setLayoutManager(gm_festival2);

        endlessRecyclerOnScrollListener2 = new EndlessRecyclerOnScrollListener(gm_festival2) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (videomodel != null && videomodel.getNext_page_url() != null) {
                    loadNextDataFromApi_videoB();
                }
            }
        };
        rv_cat_alv.addOnScrollListener(endlessRecyclerOnScrollListener2);

    }
    public void bindView() {
        rv_viewalllist = view.findViewById(R.id.rv_viewalllist);
        rv_cat_alv = view.findViewById(R.id.rv_cat_alv);
        ll_select_list = view.findViewById(R.id.ll_select_list);
        ll_as_festival = view.findViewById(R.id.ll_as_festival);
        tv_as_festival = view.findViewById(R.id.tv_as_festival);
        ll_as_category = view.findViewById(R.id.ll_as_category);
        tv_as_category = view.findViewById(R.id.tv_as_category);
        videoCategoriesDataArrayList=new ArrayList<>();
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
                if (response != null && response.body() != null) {
                    if (response.body().getResult() != null && response.body().getResult().equals("1")) {
                        if (response.body().getRecords() != null) {
                            videomodel = response.body().getRecords();
                            if (response.body().getRecords().getData() != null && response.body().getRecords().getData().size() != 0) {
                                Log.d("fdfsdkfk", "dsfji" + response.body().getRecords().getData().size());
                                //Toast.makeText(context,"not null data ",Toast.LENGTH_LONG).show();
                                attachmanager();
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
                if (response != null && response.body() != null) {
                    if (response.body().getResult() != null && response.body().getResult().equals("1")) {
                        if (response.body().getRecords() != null) {
                            videomodel2 = response.body().getRecords();
                            if (response.body().getRecords().getData() != null && response.body().getRecords().getData().size() != 0) {
                                Log.d("fdfsdkfk", "dsfji" + response.body().getRecords().getData().size());
                                //Toast.makeText(context,"not null data ",Toast.LENGTH_LONG).show();
                                attachmanager();
                                videoCategoriesDataArrayList.addAll(response.body().getRecords().getData());
                                adapterVideoList = new AdapterVideoList(context, videoCategoriesDataArrayList, "festivalviewall");
                                rv_viewalllist.setVisibility(View.VISIBLE);
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

    public void loadNextDataFromApi_videoF() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
        // progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, videomodel2.getNext_page_url(), new com.android.volley.Response.Listener<String>() {
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

                        videomodel2 = gson.fromJson(subobj.toString(), type);

                       /* JSONArray jsonArray = subobj.getJSONArray("data");
                        Type typeForPostArray = new TypeToken<List<CategoriesData>>() {
                        }.getType();*/

                        List<VideoHomeData> list = videomodel2.getData();
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

    public void loadNextDataFromApi_videoB() {
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
