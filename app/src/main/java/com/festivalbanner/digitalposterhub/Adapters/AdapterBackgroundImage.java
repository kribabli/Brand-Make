package com.festivalbanner.digitalposterhub.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.festivalbanner.digitalposterhub.Activities.ActivityCreatePost;
import com.festivalbanner.digitalposterhub.Fragments.CreateCustomImageFragment;
import com.festivalbanner.digitalposterhub.Model.ModelBackgroundImage;
import com.festivalbanner.digitalposterhub.R;

import java.util.ArrayList;

public class AdapterBackgroundImage extends RecyclerView.Adapter<AdapterBackgroundImage.ViewHolder> {

    Context context;
    private ArrayList<ModelBackgroundImage> modelBackgroundImageArrayList;
    String activityname;

    public AdapterBackgroundImage(Context context, ArrayList<ModelBackgroundImage> modelBackgroundImageArrayList,String activityname) {
        this.context = context;
        this.modelBackgroundImageArrayList = modelBackgroundImageArrayList;
        this.activityname = activityname;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_bg_image, parent, false);
       ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.iv_bg_img.setImageResource(modelBackgroundImageArrayList.get(position).getImage());

       // holder.ivclr.setBackgroundColor(modelColorListArrayList.get(position).getColour());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(activityname.equals("customimage"))
                {
                    CreateCustomImageFragment.getInstance().setbackgroundLocalImage(modelBackgroundImageArrayList.get(position).getImage());

                }else
                {
                    ActivityCreatePost.getInstance().setbackgroundLocalImage(modelBackgroundImageArrayList.get(position).getImage());

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelBackgroundImageArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_bg_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_bg_img=itemView.findViewById(R.id.iv_bg_img);
        }
    }
}
