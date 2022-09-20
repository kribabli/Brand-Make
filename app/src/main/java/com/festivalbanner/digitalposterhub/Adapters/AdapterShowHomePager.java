package com.festivalbanner.digitalposterhub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeData;
import com.festivalbanner.digitalposterhub.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class AdapterShowHomePager extends PagerAdapter {

    private ArrayList<VideoHomeData> images;
    private LayoutInflater inflater;
    private Context context;

    public AdapterShowHomePager(Context context, ArrayList<VideoHomeData> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View myImageLayout = inflater.inflate(R.layout.item_showhome_pager, container, false);
        RoundedImageView myImage = myImageLayout.findViewById(R.id.imageslide);
//       ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.imageslide);

        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        Bitmap mBitmapSampled = BitmapFactory.decodeFile(images.get(position),options);
        myImage.setImageBitmap(mBitmapSampled);*/
        //myImage.setImageResource(images.get(position).getImage_url());

        Glide.with(context).load(images.get(position).getImage_url()).into(myImage);

       // Picasso.get().load("https://festivalpost.s3.ap-south-1.amazonaws.com/storage/advetisement-image/2021/05/5URPoz0cAy2mxrHTqpZawSPvMeSn4q0V7Emkqkuv.jpg").placeholder(R.drawable.placeholder).resize(300,300).into(myImage);

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=images.get(position).getLink();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                if (images.get(position).getLink() !=null){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(browserIntent);
                }

            }
        });
        container.addView(myImageLayout, 0);
        return myImageLayout;    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
