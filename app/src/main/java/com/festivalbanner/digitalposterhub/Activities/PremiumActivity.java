package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.festivalbanner.digitalposterhub.ModelRetrofit.Response_PaymntPlan;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PremiumActivity extends AppCompatActivity {

    Context context;
    TextView tv_p_send;
    ImageView iv_planimg;
    String premiumUrl="",imageUrl="",contact="";
    ProgressDialog dialog;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        context=PremiumActivity.this;
        initP();

        dialog=new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();

        tv_p_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, PremiumPreview.class);
                i.putExtra("url",premiumUrl);
                i.putExtra("contact",contact);
                startActivity(i);
                finish();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getPlan();
    }

    private void getPlan() {
        SharedPrefrenceConfig sharedprefconfig = new SharedPrefrenceConfig(context);

        String token = sharedprefconfig.getapitoken().getApi_token();
        Api api = Base_Url.getClient().create(Api.class);
        Call<Response_PaymntPlan> call = api.getplan(token);
        call.enqueue(new Callback<Response_PaymntPlan>() {
            @Override
            public void onResponse(Call<Response_PaymntPlan> call, Response<Response_PaymntPlan> response) {
                if (response != null && response.body() != null) {
                    if (response.body().getRecords() != null) {
                        if (response.body().getRecords().getData() != null) {
                            if (response.body().getRecords().getData().get(0).getPlan_image_url() !=null) {
                                imageUrl = response.body().getRecords().getData().get(0).getPlan_image_url();
                                Glide.with(context).load(imageUrl).into(iv_planimg);
                            }
                            if (response.body().getRecords().getData().get(0).getPayment_image_url() !=null) {
                                premiumUrl = response.body().getRecords().getData().get(0).getPayment_image_url();

                            }
                            if (response.body().getRecords().getData().get(0).getContact() !=null) {
                                contact = response.body().getRecords().getData().get(0).getContact();
                            }
                            dialog.dismiss();
                        }
                    }
                    dialog.dismiss();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Response_PaymntPlan> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    void initP() {
        tv_p_send = findViewById(R.id.tv_p_send);

        iv_planimg = findViewById(R.id.iv_planimg);
        ivBack = findViewById(R.id.ivBack);
    }
}