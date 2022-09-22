package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;

import com.festivalbanner.digitalposterhub.R;
import com.jcmore2.collage.CollageView;

import java.util.ArrayList;
import java.util.List;


public class AllCollageView extends AppCompatActivity {
    CollageView Collage_view1,Collage_view2;
    GridView grid_view;
    ImageView backButton,card_view2_image1,card_view2_image2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_collage_view);
        Collage_view1=findViewById(R.id.Collage_view1);
        backButton=findViewById(R.id.backButton);
        card_view2_image1=findViewById(R.id.card_view2_image1);
        card_view2_image2=findViewById(R.id.card_view2_image2);



        List<Integer> listRes = new ArrayList<Integer>();
        listRes.add(R.drawable.tr2);
        listRes.add(R.drawable.tr3);
        listRes.add(R.drawable.tr4);

        Collage_view1.createCollageResources(listRes);
        Collage_view1.setFixedCollage(false);
        setAction();

    }

    private void setAction() {
        backButton.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    /*public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }*/
}