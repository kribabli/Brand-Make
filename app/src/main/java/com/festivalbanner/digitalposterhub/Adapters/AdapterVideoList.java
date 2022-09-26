package com.festivalbanner.digitalposterhub.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.festivalbanner.digitalposterhub.Activities.ActivitySingleVideoList;
import com.festivalbanner.digitalposterhub.Activities.PremiumActivity;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeData;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class AdapterVideoList extends RecyclerView.Adapter<AdapterVideoList.ViewHolder> {
    Context context;
    // ArrayList<ModelHomeChild> modelHomeChildList;
    ArrayList<VideoHomeData> videoCategoriesDataArrayList;
    String comefrom;
    View view;

    public AdapterVideoList(Context context, ArrayList<VideoHomeData> videoCategoriesDataArrayList, String comefrom) {
        this.context = context;
        this.comefrom = comefrom;
        this.videoCategoriesDataArrayList = videoCategoriesDataArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (comefrom.equals("home")) {
            view = inflater.inflate(R.layout.item_rv_child_category, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_rv_video, parent, false);
        }
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final String childitemtittle = videoCategoriesDataArrayList.get(position).getName();
        holder.tv_childitem_tittle.setText(childitemtittle);
        Log.d("dhshdd", "dshsa" + childitemtittle);
        //  Picasso.get().load(videoCategoriesDataArrayList.get(position).getVideo_url()).placeholder(R.drawable.placeholder).into(holder.iv_video);
        Glide.with(context).load(videoCategoriesDataArrayList.get(position).getImage_url()).placeholder(R.drawable.placeholder).into(holder.riv_childitemimage);
        final String catnameid = videoCategoriesDataArrayList.get(position).getId();
        if (videoCategoriesDataArrayList.get(position).getFestival_date() != null && !videoCategoriesDataArrayList.get(position).getFestival_date().equals("")) {
            holder.tv_date.setVisibility(View.VISIBLE);
            holder.tv_date.setText(videoCategoriesDataArrayList.get(position).getFestival_date());
        } else {
            holder.tv_date.setVisibility(View.GONE);
        }
        SharedPrefrenceConfig pref = new SharedPrefrenceConfig(context);
        holder.itemView.setOnClickListener(view -> {
            String plantype = pref.getUser().getPlan_type();
            if (!plantype.equals("Expired")) {
                if (comefrom.equals("businessviewall")) {
                    Constance.ComeFrom = comefrom;
                    Intent i = new Intent(context, ActivitySingleVideoList.class);
                    // Constance.childDataList = modelHomeChildList;
                    i.putExtra("childitemtittle", childitemtittle);
                    i.putExtra("catnameid", catnameid);
                    context.startActivity(i);
                } else {

                    if (videoCategoriesDataArrayList.get(position).getDetail_display().equals("Yes")) {
                        if (comefrom.equals("home")) {
                            comefrom = "festivalviewall";
                        }
                        Constance.ComeFrom = comefrom;
                        Intent i = new Intent(context, ActivitySingleVideoList.class);
                        // Constance.childDataList = modelHomeChildList;
                        i.putExtra("childitemtittle", childitemtittle);
                        i.putExtra("catnameid", catnameid);
                        context.startActivity(i);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(videoCategoriesDataArrayList.get(position).getDetail_message())
                                .setPositiveButton("ok", (dialog, id) -> dialog.cancel());

                        //Creating dialog box
                        builder.create();
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(arg0 -> {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                        });
                        dialog.show();
                    }
                }
            } else {
                new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setMessage("Your plan is expire so can you buy again for next 1 years.")
                        .setPositiveButton("Logout", (dialog, which) -> {
                            pref.logout();
                            pref.clear();
                            ((FragmentActivity) context).finishAffinity();
                        }).setNegativeButton("Buy premium plan", (dialog, which) -> {
                            Intent intent = new Intent(context, PremiumActivity.class);
                            context.startActivity(intent);
                            ((FragmentActivity) context).finishAffinity();
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoCategoriesDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView riv_childitemimage;
        TextView tv_childitem_tittle, tv_date;
        LinearLayout ll_viewall_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            riv_childitemimage = itemView.findViewById(R.id.riv_childitemimage);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_childitem_tittle = itemView.findViewById(R.id.tv_childitem_tittle);
            /*// ll_viewall_date = itemView.findViewById(R.id.ll_viewall_date);*/

        }
    }
}
