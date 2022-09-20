package com.festivalbanner.digitalposterhub.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.festivalbanner.digitalposterhub.Activities.ActivityHome;
import com.festivalbanner.digitalposterhub.ModelRetrofit.UserRegister;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackFragment extends Fragment {


    View view;
    Context context;
    EditText et_f_name,et_f_contact,et_f_discription;
    SharedPrefrenceConfig sharedprefconfig;
    LinearLayout ll_f_send;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_feedback, container, false);
        context=getContext();
        initF();
        ActivityHome.getInstance().ivBack.setVisibility(View.VISIBLE);
        sharedprefconfig = new SharedPrefrenceConfig(context);
        et_f_name.setText(sharedprefconfig.getUser().getName());
        et_f_contact.setText(sharedprefconfig.getUser().getContact());
        et_f_discription.setMaxLines(10);
        ll_f_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });


        return  view;
    }

    void initF(){
        et_f_name=view.findViewById(R.id.et_f_name);
        et_f_contact=view.findViewById(R.id.et_f_contact);
        et_f_discription=view.findViewById(R.id.et_f_discription);
        ll_f_send=view.findViewById(R.id.ll_f_send);
    }

    public void sendFeedback()
    {
        Api api= Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<UserRegister> call = api.feedback(sharedprefconfig.getapitoken().getApi_token(),et_f_name.getText().toString()
                ,et_f_contact.getText().toString(),et_f_discription.getText().toString());

        call.enqueue(new Callback<UserRegister>() {
            @Override
            public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
                if (response !=null && response.body()!=null) {
                    if (response.body().getResult() != null && response.body().getResult().equals("1")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(response.body().getMessage())
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        progressDialog.hide();

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
                        progressDialog.hide();

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
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserRegister> call, Throwable t) {
                Toast.makeText(context,"check Your internet Connection",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

    }
}