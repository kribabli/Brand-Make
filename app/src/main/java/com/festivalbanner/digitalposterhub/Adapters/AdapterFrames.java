package com.festivalbanner.digitalposterhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.festivalbanner.digitalposterhub.Activities.ActivityCreatePost;
import com.festivalbanner.digitalposterhub.Model.ModelFramesDetails;
import com.festivalbanner.digitalposterhub.R;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class AdapterFrames extends RecyclerView.Adapter<AdapterFrames.MyViewHolder> {

    Context context;
    ArrayList<ModelFramesDetails> modelFramesDetailsArrayList;
    private int[] drawable = new int[]
            {R.drawable.frame_1, R.drawable.frame_2};
    private int mCheckIndex = 0;

    public AdapterFrames(Context context,ArrayList<ModelFramesDetails> modelFramesDetailsArrayList) {
        this.context = context;
        this.modelFramesDetailsArrayList = modelFramesDetailsArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_frames, parent, false);

        return new MyViewHolder(view);
    }

    /*private int getItem(int pos) {
        return this.drawable[pos];
    }*/

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Glide.with(context).load(modelFramesDetailsArrayList.get(position).getFrames()).into(holder.iv_frame);
      //  Picasso.get().load(modelFramesDetailsArrayList.get(position).getFrames()).into(holder.iv_frame);
        holder.ll_frameselected.setVisibility(mCheckIndex == position ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckIndex=position;
                ActivityCreatePost.getInstance().setSelectedFrame(position);
              //  ActivityCreateCustomImage.getInstance().adapterFrame.notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return modelFramesDetailsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_frame;
        LinearLayout ll_frameselected;

        public MyViewHolder(View itemView) {
            super(itemView);

            iv_frame = itemView.findViewById(R.id.iv_frame);
            ll_frameselected = itemView.findViewById(R.id.ll_frameselected);
        }
    }

}
