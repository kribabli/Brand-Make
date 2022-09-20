package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.festivalbanner.digitalposterhub.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainLoginPage extends AppCompatActivity {


    EditText phoneNumber, referralsCodeEditText;
    LinearLayout InvitedReferralsCode;
    TextView SendOtp;
    ProgressBar progressBar;
    String SmsCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login_page);

        FirebaseApp.initializeApp(this);
        init();
        setAction();

    }

    private void setAction() {
        InvitedReferralsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referralsCodeEditText.setVisibility(View.VISIBLE);
                InvitedReferralsCode.setVisibility(View.GONE);

            }
        });
        SendOtp.setOnClickListener(v -> {
            if (!phoneNumber.getText().toString().trim().isEmpty()) {
                if (phoneNumber.getText().toString().trim().length() == 10) {
                    progressBar.setVisibility(View.VISIBLE);
                    SendOtp.setVisibility(View.INVISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + phoneNumber.getText().toString(), 60, TimeUnit.SECONDS, MainLoginPage.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                    progressBar.setVisibility(View.GONE);
                                    SendOtp.setVisibility(View.VISIBLE);
                                    SmsCode = phoneAuthCredential.getSmsCode();
                                }

                                @Override
                                public void onVerificationFailed(FirebaseException e) {
                                    progressBar.setVisibility(View.GONE);
                                    SendOtp.setVisibility(View.VISIBLE);
                                    Toast.makeText(MainLoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onCodeSent(String backEndOtp, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    progressBar.setVisibility(View.GONE);
                                    SendOtp.setVisibility(View.VISIBLE);
                                    Intent intent = new Intent(MainLoginPage.this, OTPActivity.class);
                                    intent.putExtra("MobileNumber", phoneNumber.getText().toString());
                                    intent.putExtra("otp", backEndOtp);
                                    intent.putExtra("SmsCode", SmsCode);
                                    startActivity(intent);


                                }
                            }
                    );


                } else {
                    Toast.makeText(MainLoginPage.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(MainLoginPage.this, "Enter Your Mobile", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void init() {
        phoneNumber = findViewById(R.id.phoneNumber);
        referralsCodeEditText = findViewById(R.id.referralsCodeEditText);
        InvitedReferralsCode = findViewById(R.id.InvitedReferralsCode);
        SendOtp = findViewById(R.id.SendOtp);
        progressBar = findViewById(R.id.progress_bar_sendingOtp);
    }
}