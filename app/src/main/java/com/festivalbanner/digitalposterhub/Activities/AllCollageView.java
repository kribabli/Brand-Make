package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.festivalbanner.digitalposterhub.R;
import com.jcmore2.collage.CollageView;

import java.util.ArrayList;
import java.util.List;

public class AllCollageView extends AppCompatActivity {
    CollageView collageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_collage_view);
        collageView=findViewById(R.id.collage);

        List<Integer> listRes = new ArrayList<Integer>();
        listRes.add(R.drawable.tr2);
        listRes.add(R.drawable.tr3);
        listRes.add(R.drawable.tr4);

        collageView.createCollageResources(listRes);
        collageView.setFixedCollage(false);


    }
}