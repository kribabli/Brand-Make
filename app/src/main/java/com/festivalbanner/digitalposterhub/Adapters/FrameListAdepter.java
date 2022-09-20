package com.festivalbanner.digitalposterhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.festivalbanner.digitalposterhub.Activities.ActivitySingleVideoList;
import com.festivalbanner.digitalposterhub.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class FrameListAdepter extends RecyclerView.Adapter<FrameListAdepter.ViewHolder> {

    Context context;
   // ArrayList<ModelHomeChild> modelHomeChildList;
    ArrayList<Integer> arrayList;

    View view;
    private int mCheckIndex = 0;

    public FrameListAdepter(Context context, ArrayList<Integer> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_rv_singlecatvideolist, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.riv_singlecatvideolistitem.setImageResource(arrayList.get(position));

        holder.ll_folder_check.setBackground(null);
        holder.ll_folder_check.setVisibility(mCheckIndex == position ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckIndex = position;
                ActivitySingleVideoList.getInstance().setFrame(arrayList.get(position));
                ActivitySingleVideoList.getInstance().frameListAdepter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView riv_singlecatvideolistitem;
        LinearLayout ll_folder_check;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            riv_singlecatvideolistitem = itemView.findViewById(R.id.riv_singlecatvideolistitem);
            ll_folder_check = itemView.findViewById(R.id.ll_folder_check);


        }
    }


}
