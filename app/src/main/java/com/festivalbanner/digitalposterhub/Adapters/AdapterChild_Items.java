package com.festivalbanner.digitalposterhub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.festivalbanner.digitalposterhub.Activities.ActivitySingleCategoyList;
import com.festivalbanner.digitalposterhub.ModelRetrofit.CategoriesData;
import com.festivalbanner.digitalposterhub.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class AdapterChild_Items extends RecyclerView.Adapter<AdapterChild_Items.ViewHolder> {

    Context context;
   // ArrayList<ModelHomeChild> modelHomeChildList;
    ArrayList<CategoriesData> modelHomeChildList;

    View view;

    public AdapterChild_Items(Context context, ArrayList<CategoriesData> modelHomeChildList) {
        this.context = context;
        this.modelHomeChildList = modelHomeChildList;

    }/*  public AdapterChild_Items(Context context, ArrayList<ModelHomeChild> modelHomeChildList) {
        this.context = context;
        this.modelHomeChildList = modelHomeChildList;

    }*/


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_rv_child, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

       // final String childitemtittle = modelHomeChildList.get(position).getChilditemtittle();
        final String childitemtittle = modelHomeChildList.get(position).getName();
        holder.tv_childitem_tittle.setText(childitemtittle);
        Picasso.get().load(modelHomeChildList.get(position).getImage()).placeholder(R.drawable.placeholder).into(holder.iv_childitemimage);
        holder.ll_childitem_date.setVisibility(View.VISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ActivitySingleCategoyList.class);
                Constance.childDataList = modelHomeChildList;
                i.putExtra("childitemtittle", childitemtittle);
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
