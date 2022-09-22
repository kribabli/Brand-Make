package com.festivalbanner.digitalposterhub.Me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.festivalbanner.digitalposterhub.R;

public class ShowDigitalBusinessCard extends AppCompatActivity {
    TextView backPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_digital_business_card);
        initMethod();
        setAction();
    }

    private void initMethod() {
        backPress = findViewById(R.id.backPress);
    }

    private void setAction() {
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}