package com.festivalbanner.digitalposterhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.festivalbanner.digitalposterhub.Activities.ActivitySingleCategoyList;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeData;
import com.festivalbanner.digitalposterhub.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GreetingListAdapter extends RecyclerView.Adapter<GreetingListAdapter.ViewHolder> {

    Context context;
   // ArrayList<ModelHomeChild> modelHomeChildList;
    ArrayList<VideoHomeData> modelHomeChildList;

    View view;
    private int mCheckIndex = 0;

    public GreetingListAdapter(Context context, ArrayList<VideoHomeData> modelHomeChildList) {
        this.context = context;
        this.modelHomeChildList = modelHomeChildList;
    }/*public AdapterSingleCatList(Context context, ArrayList<ModelHomeChild> modelHomeChildList) {
        this.context = context;
        this.modelHomeChildList = modelHomeChildList;
    }*/


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_rv_singlecatlist, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

     //   Picasso.get().load(modelHomeChildList.get(position).getChilditemimage()).placeholder(R.drawable.placeholder).into(holder.iv_singlecatlistitem);
        Picasso.get().load(modelHomeChildList.get(position).getImage_url()).placeholder(R.drawable.placeholder).into(holder.riv_singlecatlistitem);
        holder.ll_folder_check.setVisibility(mCheckIndex == position ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckIndex = position;
                ActivitySingleCategoyList.getInstance().setselectedimage(modelHomeChildList.get(position).getImage_url());
                ActivitySingleCategoyList.getInstance().greetingListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelHomeChildList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView riv_singlecatlistitem;
        LinearLayout ll_folder_check;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            riv_singlecatlistitem = itemView.findViewById(R.id.riv_singlecatlistitem);
            ll_folder_check = itemView.findViewById(R.id.ll_folder_check);


        }
    }


}
