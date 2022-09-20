package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.festivalbanner.digitalposterhub.ModelRetrofit.ResponseLogin;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {


    Context context;

    EditText et_rp_mobno,et_rp_otp,et_rp_newpass,et_rp_comfirmpass;
    LinearLayout ll_rp_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_o_t_p);

        context=ResetPasswordActivity.this;
        initRP();
        ll_rp_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validation()){
                    resetPassword();
                }
            }
        });

    }

    void initRP(){
        et_rp_mobno=findViewById(R.id.et_rp_mobno);
        et_rp_otp=findViewById(R.id.et_rp_otp);
        et_rp_newpass=findViewById(R.id.et_rp_newpass);
        et_rp_comfirmpass=findViewById(R.id.et_rp_comfirmpass);
        ll_rp_send=findViewById(R.id.ll_rp_send);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(context, ForgetPasswordActivity.class);
        startActivity(i);
        finish();
    }

    public boolean validation() {


        if (et_rp_mobno.getText().toString().equals("") || et_rp_mobno == null) {
            Toast.makeText(getApplicationContext(), "Please enter Mobile No", Toast.LENGTH_LONG).show();
            return false;

        }

        if (et_rp_newpass.getText().toString().equals("") || et_rp_newpass == null) {
            Toast.makeText(getApplicationContext(), "Please enter New Password", Toast.LENGTH_LONG).show();
            return false;
        }

        if (et_rp_comfirmpass.getText().toString().equals("") || et_rp_comfirmpass == null) {
            Toast.makeText(getApplicationContext(), "Please enter Confirm Password", Toast.LENGTH_LONG).show();
            return false;

        }

        if (!et_rp_comfirmpass.getText().toString().equals(et_rp_newpass.getText().toString())) {
            Toast.makeText(getApplicationContext(), "New Password and Confirm Password are not same", Toast.LENGTH_LONG).show();
            return false;
        }



        else {
            return true;
        }

    }


    public void resetPassword() {
        Api api = Base_Url.getClient().create(Api.class);

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        Call<ResponseLogin> call = api.resetPassword(et_rp_mobno.getText().toString(),et_rp_otp.getText().toString(),et_rp_newpass.getText().toString());

        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if (response !=null && response.body()!=null) {
                    ResponseLogin loginResponse = response.body();
                    if (loginResponse.getResult() != null && loginResponse.getResult().equals("1")) {

                        Intent i = new Intent(context, ActivitySignIn.class);
                        startActivity(i);
                        finish();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(response.body().getMessage())
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        // Constance.tv_login_logout="Logout";
                                    }
                                });

                        //Creating dialog box
                        builder.create();
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                            }
                        });
                        dialog.show();
                    }
                }
                // progressBar.setVisibility(View.GONE);
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Toast.makeText(context, "Check Your Internet Connection !!!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }
}