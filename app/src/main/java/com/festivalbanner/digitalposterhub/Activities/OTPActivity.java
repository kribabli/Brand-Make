package com.festivalbanner.digitalposterhub.Activities;

import static android.util.Log.wtf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {
    TextView tv_mobileNumber, textResendOtp;
    EditText inputOtp1, inputOtp2, inputOtp3, inputOtp4, inputOtp5, inputOtp6;
    Button submitBtn;
    ProgressBar progress_bar_Verify;
    String otp;
    String smsCode = "";
    SharedPrefrenceConfig sharedprefconfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        sharedprefconfig = new SharedPrefrenceConfig(this);
        init();
        tv_mobileNumber.setText(String.format("+91-%s", getIntent().getStringExtra("MobileNumber")));
        otp = getIntent().getStringExtra("otp");
        smsCode = getIntent().getStringExtra("SmsCode");

        setAction();

    }

    private void setAction() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputOtp1.getText().toString().trim().isEmpty() && !inputOtp2.getText().toString().trim().isEmpty() && !inputOtp3.getText().toString().trim().isEmpty() && !inputOtp4.getText().toString().trim().isEmpty() && !inputOtp5.getText().toString().trim().isEmpty() && !inputOtp6.getText().toString().trim().isEmpty()) {

                    try {
                        inputOtp1.setText("" + smsCode.substring(0, 1));
                        inputOtp2.setText("" + smsCode.substring(1, 2));
                        inputOtp3.setText("" + smsCode.substring(2, 3));
                        inputOtp4.setText("" + smsCode.substring(3, 4));
                        inputOtp5.setText("" + smsCode.substring(4, 5));
                        inputOtp6.setText("" + smsCode.substring(5, 6));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    smsCode = inputOtp1.getText().toString().trim() + inputOtp2.getText().toString().trim() + inputOtp3.getText().toString().trim() + inputOtp4.getText().toString().trim() + inputOtp5.getText().toString().trim() + inputOtp6.getText().toString().trim();

                    if (otp != null) {
                        progress_bar_Verify.setVisibility(View.VISIBLE);
                        submitBtn.setVisibility(View.GONE);

                        wtf("Hulk-" + getClass().getName() + "-" + Admin.getLineNumber(), "otp: " + otp);
                        wtf("Hulk-" + getClass().getName() + "-" + Admin.getLineNumber(), "smsCode: " + smsCode);

                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                otp, smsCode
                        );

                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Admin.tinyDB.putBoolean("login", true);
                                    Intent intent = new Intent(getApplicationContext(), ActivityHome.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(OTPActivity.this, "enter correct otp", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    } else {
                        Toast.makeText(OTPActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OTPActivity.this, "Please enter all number ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        numberOtpMove();
    }

    private void numberOtpMove() {

        inputOtp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputOtp2.requestFocus();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputOtp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputOtp3.requestFocus();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputOtp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputOtp4.requestFocus();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputOtp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputOtp5.requestFocus();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputOtp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputOtp6.requestFocus();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @SuppressLint("WrongViewCast")
    private void init() {
        tv_mobileNumber = findViewById(R.id.tv_mobileNumber);
        textResendOtp = findViewById(R.id.textResendOtp);
        inputOtp1 = findViewById(R.id.inputOtp1);
        inputOtp2 = findViewById(R.id.inputOtp2);
        inputOtp3 = findViewById(R.id.inputOtp3);
        inputOtp4 = findViewById(R.id.inputOtp4);
        inputOtp5 = findViewById(R.id.inputOtp5);
        inputOtp6 = findViewById(R.id.inputOtp6);
        submitBtn = findViewById(R.id.submitBtn);
        progress_bar_Verify = findViewById(R.id.progress_bar_Verify);


    }


}