package com.festivalbanner.digitalposterhub.Activities;

import static com.festivalbanner.digitalposterhub.Utills.Admin.getLineNumber;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.festivalbanner.digitalposterhub.Adapters.BusinessAdapter;
import com.festivalbanner.digitalposterhub.ModelRetrofit.Response_PaymntPlan;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
import com.festivalbanner.digitalposterhub.Utills.Admin;
import com.festivalbanner.digitalposterhub.Utills.BusinessItem;
import com.festivalbanner.digitalposterhub.Utills.BusinessesResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessSelection extends AppCompatActivity {

    Context context;
    SwipeRefreshLayout swiper;
    RecyclerView recycler;
    BusinessAdapter adapter;
    ArrayList<BusinessItem> list;

    public static String business_id = "";
    public static String business_cat = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_selection);
        Admin.HandleToolBar(this, "select Your Business", findViewById(R.id.go_back_img), findViewById(R.id.title_head_txt));

        context = this;

        Handle_init_views();

        Handle_model_recycler();

        Handle_swiper();
    }

    private void Handle_swiper() {
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handle_model_recycler();
            }
        });
    }

    private void Handle_init_views() {
        swiper = findViewById(R.id.swiper);
        recycler = findViewById(R.id.recycler);
    }

    private void Handle_model_recycler() {
        list = new ArrayList<>();
        adapter = new BusinessAdapter(list);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        recycler.setAdapter(adapter);
        swiper.setRefreshing(true);

        Api api = Base_Url.getClient2().create(Api.class);
        Call<BusinessesResponse> call = api.get_businesses();
        call.enqueue(new Callback<BusinessesResponse>() {
            @Override
            public void onResponse(Call<BusinessesResponse> call, Response<BusinessesResponse> response) {
                Log.wtf("Hulk-" + getClass().getName() + "-" + getLineNumber(), ""+response.raw().request().url());
                swiper.setRefreshing(false);
                if (response.body()!=null) {
                    list.clear();
                    list.addAll(response.body().getMessage());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<BusinessesResponse> call, Throwable t) {
                swiper.setRefreshing(false);
            }
        });
    }
}