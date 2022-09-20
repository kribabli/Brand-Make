package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Utills.Admin;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;

public class PremiumPreview extends AppCompatActivity {

    Context context;

    LinearLayout ll_whatsapp_btn;
    ImageView iv_img;
    String url = "", contact = "";
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_preview);

        context = PremiumPreview.this;
        initPP();
        if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
            contact = getIntent().getStringExtra("contact");
            Glide.with(context).load(url).into(iv_img);
        }
        ll_whatsapp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Admin.tinyDB.getBoolean("login")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + contact + "&text=" + ""));
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(context).setMessage("You have to login!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context, ActivitySignIn.class);
                                    startActivity(intent);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    }).show();
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    void initPP() {

        iv_img = findViewById(R.id.iv_img);
        ll_whatsapp_btn = findViewById(R.id.ll_whatsapp_btn);
        ivBack = findViewById(R.id.ivBack);
    }


}