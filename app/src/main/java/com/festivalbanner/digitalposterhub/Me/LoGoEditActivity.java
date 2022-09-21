package com.festivalbanner.digitalposterhub.Me;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.festivalbanner.digitalposterhub.Fragments.CreateCustomImageFragment;
import com.festivalbanner.digitalposterhub.R;

public class LoGoEditActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    String image, title;
    TextView backPress, download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lo_go_edit);

        initMethod();
        setAction();
    }

    private void initMethod() {
        backPress = findViewById(R.id.backPress);
        download = findViewById(R.id.download);
    }

    private void setAction() {
        image = getIntent().getStringExtra("image");
        title = getIntent().getStringExtra("title");

        fragmentManager = getSupportFragmentManager();
        CreateCustomImageFragment createCustomImageFragment1 = new CreateCustomImageFragment();
        Bundle args = new Bundle();
        args.putString("image", image);
        args.putString("title", title);
        createCustomImageFragment1.setArguments(args);
        loadFragment(createCustomImageFragment1, fragmentManager);

        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadFragment(Fragment f1, FragmentManager fm) {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, f1);
        ft.commit();
    }
}