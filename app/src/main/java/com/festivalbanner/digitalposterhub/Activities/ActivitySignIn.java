package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.festivalbanner.digitalposterhub.AnalyticsApplication;
import com.festivalbanner.digitalposterhub.ModelRetrofit.ResponseLogin;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
import com.festivalbanner.digitalposterhub.Utills.Admin;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
import com.festivalbanner.digitalposterhub.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.onesignal.OneSignal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySignIn extends AppCompatActivity {

    Context context;
    EditText et_password, et_email;
    SharedPrefrenceConfig sharedprefconfig;
    Tracker mTracker;
    LinearLayout ll_forgetpass_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        context = ActivitySignIn.this;
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        OneSignal.initWithContext(this);
        if (isNetworkAvailable()) {

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("You aren't connected to the internet.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            //close the app
                            finish();
                        }
                    });

            //Creating dialog box

            AlertDialog alert = builder.create();
            alert.show();
            // Toast.makeText(getApplicationContext(), "You aren't connected to the internet.", Toast.LENGTH_SHORT).show();
        }

        boolean islogoin = Admin.tinyDB.getBoolean("login");
       /* if (islogoin) {
            Intent intent = new Intent(context, ActivityHome.class);
            // intent.putExtra("quote_text",0);
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }*/

        sharedprefconfig = new SharedPrefrenceConfig(context);

        et_password = findViewById(R.id.et_password);
        et_email = findViewById(R.id.et_email);
        ll_forgetpass_btn = findViewById(R.id.ll_forgetpass_btn);

        ll_forgetpass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, ForgetPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /* @Override
     protected void onStart() {
         super.onStart();
         if (sharedprefconfig.isLoggedIn()) {
             Intent intent = new Intent(context, ActivityHome.class);
             // intent.putExtra("quote_text",0);
             // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             startActivity(intent);
         }
     }*/

    public void onClickSignIn(View view) {
        switch (view.getId()) {
            case R.id.ll_signin_btn:
                signIn();
                /*Intent i=new Intent(context,ActivityHome.class);
                startActivity(i);*/
                break;
            case R.id.tv_signup:
                Intent signup = new Intent(context, ActivitySignUp.class);
                startActivity(signup);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if (sharedprefconfig.isLoggedIn()) {
            Intent intent = new Intent(context, ActivityHome.class);
            // intent.putExtra("quote_text",0);
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }*/
     /*   boolean islogoin=Admin.tinyDB.getBoolean("login");
        if (islogoin) {
            Intent intent = new Intent(context, ActivityHome.class);
            // intent.putExtra("quote_text",0);
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }*/
    }

    private void signIn() {
        Api api = Base_Url.getClient().create(Api.class);

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        Call<ResponseLogin> call = api.signIn(et_email.getText().toString(), et_password.getText().toString());

        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if (response != null && response.body() != null) {
                    ResponseLogin loginResponse = response.body();
                    if (loginResponse.getResult() != null && loginResponse.getResult().equals("1")) {
                        if (loginResponse.getApi_token() != null) {
                            String api_token = response.body().getApi_token();
                            Log.e("token", "token : " + api_token);
                            //sharedprefconfig.saveapitoken(loginResponse);

                            if (loginResponse.getRecord() != null) {

                        /*    SharedPrefrenceConfig.savebooleanPreferance(context,"checkLogin",true);
                            Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                            sharedprefconfig.saveapitoken(loginResponse);
                            sharedprefconfig.saveUser(loginResponse.getRecord());
                            progressDialog.dismiss();*/

                                if (response.body().getRecord().getContact_verify().equals("0")) {
                                    Intent intent = new Intent(context, OTPActivity.class);
                                    intent.putExtra("mobileNo", response.body().getRecord().getContact());
                                    Log.e("otp", "otp : " + response.body().getRecord().getSms_otp());
                                    intent.putExtra("otp", response.body().getRecord().getSms_otp());
                                    progressDialog.dismiss();
                                    startActivity(intent);
                                } else {
                                    SharedPrefrenceConfig.savebooleanPreferance(context, "checkLogin", true);
                                    sharedprefconfig.savebooleanPreferance(context, "checkLogin", true);
                                    sharedprefconfig.saveStringPreferance(context, Constance.Status, response.body().getRecord().getStatus());
                                    Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    sharedprefconfig.saveapitoken(loginResponse);
                                    sharedprefconfig.saveUser(loginResponse.getRecord());
                                    sharedprefconfig.saveStringPreferance(context, Constance.CToken, response.body().getApi_token());
                                    progressDialog.dismiss();
                                    Intent i = new Intent(context, ActivityHome.class);
                                    startActivity(i);
                                }

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
                        builder.setMessage(response.body().getMessage())
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        // Constance.tv_login_logout="Logout";
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    progressDialog.dismiss();
                    // progressBar.setVisibility(View.GONE);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Toast.makeText(context, "Check Your Internet Connection !!!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + "Google Analytics Testing");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

/*

    @Override
    public void onBackPressed() {
        //   super.onBackPressed();
        new AlertDialog.Builder(this)
                .setTitle("Close App")
                .setMessage("Are you sure you want to close this App?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
*/

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}