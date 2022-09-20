package com.festivalbanner.digitalposterhub.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.festivalbanner.digitalposterhub.Activities.ActivitySingleCategoyList;
import com.festivalbanner.digitalposterhub.Activities.PremiumActivity;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeData;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BusinessCatAdepter extends RecyclerView.Adapter<BusinessCatAdepter.ViewHolder> {

    Context context;
    // ArrayList<ModelHomeChild> modelHomeChildList;
    ArrayList<VideoHomeData> modelHomeChildList;
    int resource;
    View view;

    /*public AdapterViewAllList(Context context, ArrayList<ModelHomeChild> modelHomeChildList) {
        this.context = context;
        this.modelHomeChildList = modelHomeChildList;

    }*/
    public BusinessCatAdepter(Context context, int resource, ArrayList<VideoHomeData> modelHomeChildList) {
        this.context = context;
        this.resource = resource;
        this.modelHomeChildList = modelHomeChildList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(resource, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        final String childitemtittle = modelHomeChildList.get(position).getName();
        holder.tv_childitem_tittle.setText(childitemtittle);

        Picasso.get().load(modelHomeChildList.get(position).getImage_url()).placeholder(R.drawable.placeholder).into(holder.riv_childitemimage);
        final String catnameid = modelHomeChildList.get(position).getId();


        SharedPrefrenceConfig pref = new SharedPrefrenceConfig(context);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String plantype = pref.getUser().getPlan_type();
                if (!plantype.equals("Expired")) {
                    Intent i = new Intent(context, ActivitySingleCategoyList.class);
                    // Constance.childDataList = modelHomeChildList;
                    i.putExtra("childitemtittle", childitemtittle);
                    i.putExtra("catnameid", catnameid);
                    i.putExtra("catType", "business");
                    context.startActivity(i);
                } else {
                    new AlertDialog.Builder(context)
                            .setCancelable(false)
                            .setMessage("Your plan is expire so can you buy again for next 1 years.")
                            .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pref.logout();
                                    pref.clear();
                                    ((FragmentActivity) context).finishAffinity();
                                }
                            }).setNegativeButton("Buy premium plan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, PremiumActivity.class);
                            context.startActivity(intent);
                            ((FragmentActivity) context).finishAffinity();
                        }
                    }).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelHomeChildList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView riv_childitemimage;
        TextView tv_childitem_tittle;
        LinearLayout ll_viewall_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            riv_childitemimage = itemView.findViewById(R.id.riv_childitemimage);
            tv_childitem_tittle = itemView.findViewById(R.id.tv_childitem_tittle);
            /*// ll_viewall_date = itemView.findViewById(R.id.ll_viewall_date);*/

        }
    }


}
