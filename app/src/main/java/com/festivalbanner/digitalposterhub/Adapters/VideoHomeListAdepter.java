package com.festivalbanner.digitalposterhub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.festivalbanner.digitalposterhub.Activities.ActivitySingleVideoList;
import com.festivalbanner.digitalposterhub.ModelRetrofit.CategoriesData;
import com.festivalbanner.digitalposterhub.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoHomeListAdepter extends RecyclerView.Adapter<VideoHomeListAdepter.ViewHolder> {

    Context context;
   // ArrayList<ModelHomeChild> modelHomeChildList;
    ArrayList<CategoriesData> modelHomeChildList;
    View view;

    public VideoHomeListAdepter(Context context, ArrayList<CategoriesData> modelHomeChildList) {
        this.context = context;
        this.modelHomeChildList = modelHomeChildList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_rv_child_category, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final String childitemtittle=modelHomeChildList.get(position).getName();
        holder.tv_childitem_tittle.setText(childitemtittle);
      //  Glide.with(context).load(modelHomeChildList.get(position).getImage_url()).into(holder.iv_childitemimage);
        Picasso.get().load(modelHomeChildList.get(position).getImage_url()).placeholder(R.drawable.placeholder).into(holder.iv_childitemimage);
        final String catnameid=modelHomeChildList.get(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(context, ActivitySingleVideoList.class);
                //Constance.childDataList=modelHomeChildList;
                i.putExtra("childitemtittle",childitemtittle);
                i.putExtra("catnameid",catnameid);

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelHomeChildList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_childitemimage;
        TextView tv_childitem_tittle, childitem_date;
        LinearLayout ll_childitem_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_childitemimage = itemView.findViewById(R.id.iv_childitemimage);
            tv_childitem_tittle = itemView.findViewById(R.id.tv_childitem_tittle);
            childitem_date = itemView.findViewById(R.id.childitem_date);
            ll_childitem_date = itemView.findViewById(R.id.ll_childitem_date);

        }
    }


}
