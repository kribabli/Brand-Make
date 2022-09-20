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
import com.festivalbanner.digitalposterhub.ModelRetrofit.CategoriesData;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterViewAllList extends RecyclerView.Adapter<AdapterViewAllList.ViewHolder> {

    Context context;
    // ArrayList<ModelHomeChild> modelHomeChildList;
    ArrayList<CategoriesData> modelHomeChildList;

    View view;

    /*public AdapterViewAllList(Context context, ArrayList<ModelHomeChild> modelHomeChildList) {
        this.context = context;
        this.modelHomeChildList = modelHomeChildList;

    }*/
    public AdapterViewAllList(Context context, ArrayList<CategoriesData> modelHomeChildList) {
        this.context = context;
        this.modelHomeChildList = modelHomeChildList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_rv_viewall, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        // final String childitemtittle = modelHomeChildList.get(position).getChilditemtittle();
        final String childitemtittle = modelHomeChildList.get(position).getName();
        holder.tv_viewall_tittle.setText(childitemtittle);
        //   Picasso.get().load(modelHomeChildList.get(position).getChilditemimage()).placeholder(R.drawable.placeholder).into(holder.iv_viewallimage);
        Picasso.get().load(modelHomeChildList.get(position).getImage_url()).placeholder(R.drawable.placeholder).into(holder.riv_viewallimage);
        final String catnameid = modelHomeChildList.get(position).getId();
      /* if(childitemtittle.equals("Upcoming Festival & days"))
       {
           holder.ll_viewall_date.setVisibility(View.VISIBLE);

       }else
       {
           holder.ll_viewall_date.setVisibility(View.GONE);

       }*/

        if (modelHomeChildList.get(position).getFestival_date() != null && !modelHomeChildList.get(position).getFestival_date().equals("")) {
            holder.tv_date.setVisibility(View.VISIBLE);
            holder.tv_date.setText(modelHomeChildList.get(position).getFestival_date());
        } else {
            holder.tv_date.setVisibility(View.GONE);
        }
        SharedPrefrenceConfig pref = new SharedPrefrenceConfig(context);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (modelHomeChildList.get(position).getDetail_display().equals("Yes")) {
                    String plantype = pref.getUser().getPlan_type();
                    if (!plantype.equals("Expired")) {
                        Intent i = new Intent(context, ActivitySingleCategoyList.class);
                        // Constance.childDataList = modelHomeChildList;
                        i.putExtra("childitemtittle", childitemtittle);
                        i.putExtra("catType", "festival");
                        i.putExtra("catnameid", catnameid);
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
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(modelHomeChildList.get(position).getDetail_message())
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });

                    //Creating dialog box
                    builder.create();
                    AlertDialog dialog = builder.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorTheame_text));
                        }
                    });
                    dialog.show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelHomeChildList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView riv_viewallimage;
        TextView tv_viewall_tittle;
        LinearLayout ll_viewall_date;
        TextView tv_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            riv_viewallimage = itemView.findViewById(R.id.riv_viewallimage);
            tv_viewall_tittle = itemView.findViewById(R.id.tv_viewall_tittle);
            tv_date = itemView.findViewById(R.id.tv_date);
            /*// ll_viewall_date = itemView.findViewById(R.id.ll_viewall_date);*/

        }
    }


}
