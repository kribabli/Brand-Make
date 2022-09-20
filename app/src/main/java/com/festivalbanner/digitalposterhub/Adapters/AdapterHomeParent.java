package com.festivalbanner.digitalposterhub.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.festivalbanner.digitalposterhub.Activities.ActivityViewAllList;
import com.festivalbanner.digitalposterhub.ModelRetrofit.CategoriesData;
import com.festivalbanner.digitalposterhub.ModelRetrofit.CategoriesRecord;
import com.festivalbanner.digitalposterhub.ModelRetrofit.List_of_Categories_name;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
import com.festivalbanner.digitalposterhub.Model.ModelHomeChild;
import com.festivalbanner.digitalposterhub.Model.ModelHomeParent;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.festivalbanner.digitalposterhub.Utills.EndlessRecyclerOnScrollListener;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterHomeParent extends RecyclerView.Adapter<AdapterHomeParent.ViewHolder> {

    Context context;
    ArrayList<ModelHomeParent> modelHomeParentArrayList;
    View view;
    AdapterChild_Items adapterChild_items;
    ArrayList<CategoriesData> categories_name;
    SharedPrefrenceConfig sharedPrefrenceConfig;
    CategoriesRecord categoriesRecord;
    String language;
    int lastPositionofcatlist;
    public AdapterHomeParent(Context context, ArrayList<ModelHomeParent> modelHomeParentArrayList) {
        this.context = context;
        this.modelHomeParentArrayList = modelHomeParentArrayList;
        language=sharedPrefrenceConfig.getPrefString(context,Constance.language,"");

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_rv_homeparent, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final String parentcatname = modelHomeParentArrayList.get(position).getParentitemtittle();
        holder.tv_parenttittle.setText(parentcatname);

       /* if (parentcatname.equals("Upcoming Festival & days"))
        {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.rv_childlist.setLayoutManager(layoutManager);
          *//*  adapterChild_items=new AdapterChild_Items(context,null);
            holder.rv_childlist.setAdapter(adapterChild_items);*//*

        }else
        {*/
             sharedPrefrenceConfig=new SharedPrefrenceConfig(context);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 3);
        holder.rv_childlist.setLayoutManager(linearLayoutManager);

       // holder.rv_childlist.setLayoutManager(new GridLayoutManager(context, 3));
            categories_name=new ArrayList<>();
        holder.rv_childlist.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (categoriesRecord.getNext_page_url() != null) {
                    loadNextDataFromApi(holder);
                }
                else
                {
                    //Toast.makeText(context,"next url not called",Toast.LENGTH_LONG).show();
                }
            }
        });

        getCategoriesname(holder);
          //  holder.rv_childlist.setLayoutManager(new GridLayoutManager(context,la));
           /* AdapterChild_Items_Categories adapterChild_items_categories=new AdapterChild_Items_Categories(context,getChildCategoriesList());
            holder.rv_childlist.setAdapter(adapterChild_items_categories);*/

       /* }*/

        holder.tv_viewalllist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (parentcatname.equals("Upcoming Festival & days"))
                {
                   // Constance.viewAllDataList=getChildFestivalList();
                }else
                {
                  //  Constance.viewAllDataList=getChildCategoriesList();
                }*/
                Intent i=new Intent(context, ActivityViewAllList.class);
                i.putExtra("parentname",parentcatname);
                context.startActivity(i);

            }
        });

    }


    @Override
    public int getItemCount() {
        return modelHomeParentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_parenttittle, tv_viewalllist;
        RecyclerView rv_childlist;
        RelativeLayout rl_parentview;
        ProgressBar loader;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_viewalllist = itemView.findViewById(R.id.tv_viewalllist);
            tv_parenttittle = itemView.findViewById(R.id.tv_parenttittle);
            rl_parentview = itemView.findViewById(R.id.rl_parentview);
            loader = itemView.findViewById(R.id.loader);
            rv_childlist = itemView.findViewById(R.id.rv_childlist);


        }
    }
    public void getCategoriesname(ViewHolder holder) {
        Log.d("check_apitoken","1"+sharedPrefrenceConfig.getapitoken().getApi_token());
        Api api= Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<List_of_Categories_name> call = api.getCategoriesNameList(sharedPrefrenceConfig.getapitoken().getApi_token());

        call.enqueue(new Callback<List_of_Categories_name>() {
            @Override
            public void onResponse(Call<List_of_Categories_name> call, Response<List_of_Categories_name> response) {
                Log.d("check_apitoken","2: "+sharedPrefrenceConfig.getapitoken().getApi_token());
               // Log.d("check_apitoken","getResult :"+ response.body());
//                Log.d("check_apitoken","getMessage :"+response.body().getMessage());
                if (response !=null && response.body()!=null) {
                    if (response.body().getResult() != null && response.body().getResult().equals("1")) {
                        if (response.body().getRecords() != null) {

                            if (response.body().getRecords().getData() != null) {
                                categoriesRecord = response.body().getRecords();

                                categories_name.addAll(response.body().getRecords().getData());
                                Constance.Next_page_url = categoriesRecord.getNext_page_url();
                                if (categories_name.size() != 0) {
                                    AdapterChild_Items_Categories adapterChild_items_categories = new AdapterChild_Items_Categories(context, categories_name);
                                    holder.rv_childlist.setAdapter(adapterChild_items_categories);
                                    progressDialog.dismiss();

                               /* categories_recyclerview_adapter=new Categories_Recyclerview_Adapter(layout.getContext(),categories_name,"cat");
                                // Toast.makeText(getApplicationContext(),"hello"+quotelist.size(),Toast.LENGTH_LONG).show();

                                rv_home_fragment.setAdapter(categories_recyclerview_adapter);
                              */
                                    if (lastPositionofcatlist > categories_name.size()) {
                                        loadNextDataFromApi(holder);

                                        // loader.setVisibility(View.GONE);
                                    } else {
                                        holder.rv_childlist.scrollToPosition(lastPositionofcatlist);
                                        // loader.setVisibility(View.GONE);
                                    }
                                    // loader.setVisibility(View.GONE);

                                }


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
            public void onFailure(Call<List_of_Categories_name> call, Throwable t) {
                Toast.makeText(context,"check Your internet Connection",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

    }
    public void loadNextDataFromApi(ViewHolder holder)
    {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
        // progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest=new StringRequest(Request.Method.GET, categoriesRecord.getNext_page_url(), new com.android.volley.Response.Listener<String>() {
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

                        categoriesRecord = gson.fromJson(subobj.toString(), type);
                        JSONArray jsonArray = subobj.getJSONArray("data");
                        Type typeForPostArray = new TypeToken<List<CategoriesData>>() {
                        }.getType();
                        List<CategoriesData> list = gson.fromJson(jsonArray.toString(), typeForPostArray);

                        categories_name.addAll(list);
                        Constance.Next_page_url=categoriesRecord.getNext_page_url();
                        lastPositionofcatlist=categories_name.size();
                        if (categories_name.size() != 0) {
                            /*tvNoData.setVisibility(View.GONE);
                            rvList.setVisibility(View.VISIBLE);*/
                            adapterChild_items.notifyDataSetChanged();
                            if (lastPositionofcatlist > categories_name.size()) {
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
                            }
                        }
                    }
                } catch( JSONException e){
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying the error in toast if occurrs
                Toast.makeText(context,"Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        //progressBar.setVisibility(View.GONE);
        /*progressDialog.dismiss();*/
    }
    public static ArrayList<ModelHomeChild> getChildCategoriesList() {
        ArrayList<ModelHomeChild> data = new ArrayList<>();
        data.add(new ModelHomeChild("Good Morning",R.drawable.img_1));
        data.add(new ModelHomeChild("Good Night",R.drawable.img_2));
        data.add(new ModelHomeChild("Good Morning",R.drawable.img_3));
        data.add(new ModelHomeChild("Good Night",R.drawable.img_1));
        data.add(new ModelHomeChild("Good Morning",R.drawable.img_2));
        data.add(new ModelHomeChild("Good Night",R.drawable.img_3));
        data.add(new ModelHomeChild("Good Morning",R.drawable.img_1));
        data.add(new ModelHomeChild("Good Night",R.drawable.img_3));
        return data;
    }
/*
    public static ArrayList<ModelHomeChild> getChildFestivalList() {
        ArrayList<ModelHomeChild> data = new ArrayList<>();
        data.add(new ModelHomeChild("Holi",R.drawable.img_3));
        data.add(new ModelHomeChild("Diwali",R.drawable.img_1));
        data.add(new ModelHomeChild("Navratri",R.drawable.img_2));
        data.add(new ModelHomeChild("Holi",R.drawable.img_3));
        data.add(new ModelHomeChild("Diwali",R.drawable.img_1));
        data.add(new ModelHomeChild("Navratri",R.drawable.img_2));

        return data;
    }
*/

}
