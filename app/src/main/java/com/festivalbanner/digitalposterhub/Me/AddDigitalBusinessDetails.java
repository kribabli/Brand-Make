package com.festivalbanner.digitalposterhub.Me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.festivalbanner.digitalposterhub.R;

public class AddDigitalBusinessDetails extends AppCompatActivity {
    TextView backPress, save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_digital_business_details);
        initMethod();
        setAction();
    }

    private void initMethod() {
        backPress = findViewById(R.id.back_Button);
        save = findViewById(R.id.save_button);
    }

    private void setAction() {
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    private void validation() {

    }
}