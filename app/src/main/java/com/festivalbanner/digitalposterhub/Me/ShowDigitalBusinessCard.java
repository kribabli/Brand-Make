package com.festivalbanner.digitalposterhub.Me;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.festivalbanner.digitalposterhub.BuildConfig;
import com.festivalbanner.digitalposterhub.R;

public class ShowDigitalBusinessCard extends AppCompatActivity {
    TextView backPress;
    LinearLayout linearLayout2;
    ImageView share, whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_digital_business_card);
        initMethod();
        setAction();
    }

    private void initMethod() {
        backPress = findViewById(R.id.backPress);
        linearLayout2 = findViewById(R.id.linearLayout2);
        share = findViewById(R.id.share);
        whatsapp = findViewById(R.id.whatsapp);
    }

    private void setAction() {
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowDigitalBusinessCard.this, AddDigitalBusinessDetails.class);
                startActivity(intent);
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareOnWhatsappMethod();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareMethod();
            }
        });
    }

    private void shareOnWhatsappMethod() {
        try {
            //Share on Whatsapp Only
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Brand Make");
            String shareMessage = "\nHello! Here's My Digital Business Card.\nYou can click on the link to open it.\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            shareIntent.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareMethod() {
        try {
            //Share
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Brand Make");
            String shareMessage = "\nHello! Here's My Digital Business Card.\nYou can click on the link to open it.\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}