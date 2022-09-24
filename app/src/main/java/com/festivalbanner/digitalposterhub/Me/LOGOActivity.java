package com.festivalbanner.digitalposterhub.Me;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.festivalbanner.digitalposterhub.R;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class LOGOActivity extends AppCompatActivity {
    RelativeLayout editBtn;
    public static Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logoactivity);
        editBtn = findViewById(R.id.editBtn);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActionMethod();
            }
        });
    }

    //Work for Logo-----------------
    public void setActionMethod() {
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(LOGOActivity.this)
                        .crop()                        //Crop image(Optional), Check Customization for more option
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            imgUri = data.getData();
            if (!imgUri.equals("")) {
                startActivity(new Intent(this, FinalLOGOActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}