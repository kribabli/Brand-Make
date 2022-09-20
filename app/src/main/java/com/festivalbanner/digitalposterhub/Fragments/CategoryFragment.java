package com.festivalbanner.digitalposterhub.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.festivalbanner.digitalposterhub.Activities.ActivityHome;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeData;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeModel;
import com.festivalbanner.digitalposterhub.Adapters.BusinessCatAdepter;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeRecords;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.festivalbanner.digitalposterhub.Utills.EndlessRecyclerOnScrollListener;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
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

public class CategoryFragment extends Fragment {
    View view;
    RecyclerView rv_viewalllist;
    Context context;
    SharedPrefrenceConfig sharedPrefrenceConfig;
    String language;
    ArrayList<VideoHomeData> businessCatList;
    VideoHomeRecords model;
    BusinessCatAdepter businessCatAdepter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category, container, false);
        initBCat();
        context = getContext();
        sharedPrefrenceConfig = new SharedPrefrenceConfig(context);
        ActivityHome.getInstance().ivBack.setVisibility(View.GONE);
        ActivityHome.getInstance().ll_next.setVisibility(View.GONE);
        language = sharedPrefrenceConfig.getPrefString(context, Constance.language, "");

        GridLayoutManager gm = new GridLayoutManager(context, 3);
        rv_viewalllist.setLayoutManager(gm);
        getBusinessCat();
        rv_viewalllist.addOnScrollListener(new EndlessRecyclerOnScrollListener(gm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (model != null && model.getNext_page_url() != null) {
                    loadNextDataFromApi();
                }
            }
        });

        return view;
    }

    void initBCat() {
        rv_viewalllist = view.findViewById(R.id.rv_viewalllist);
        businessCatList = new ArrayList<>();
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
                            model = response.body().getRecords();
                            if (response.body().getRecords().getData() != null && response.body().getRecords().getData().size() > 0) {
                                businessCatList.addAll(response.body().getRecords().getData());
                                businessCatAdepter = new BusinessCatAdepter(context, R.layout.seeall_list_rawl, businessCatList);
                                rv_viewalllist.setAdapter(businessCatAdepter);
                           /* AdapterChild_Items_Categories adapterChild_items_categories=new AdapterChild_Items_Categories(context,viewalllist);
                            rv_childlist.setAdapter(adapterChild_items_categories);
                          */
                                progressDialog.dismiss();


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

    public void loadNextDataFromApi() {
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
                        Type type = new TypeToken<VideoHomeRecords>() {
                        }.getType();

                        model = gson.fromJson(subobj.toString(), type);
                       /* JSONArray jsonArray = subobj.getJSONArray("data");
                        Type typeForPostArray = new TypeToken<List<CategoriesData>>() {
                        }.getType();*/
                        List<VideoHomeData> list = model.getData();


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

}