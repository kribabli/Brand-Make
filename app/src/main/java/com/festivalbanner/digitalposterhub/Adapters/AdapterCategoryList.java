package com.festivalbanner.digitalposterhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.festivalbanner.digitalposterhub.Activities.ActivitySingleCategoyList;
import com.festivalbanner.digitalposterhub.Activities.ActivitySingleVideoList;
import com.festivalbanner.digitalposterhub.Activities.ActivityViewAllList;
import com.festivalbanner.digitalposterhub.Activities.ActivityHome;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;

public class AdapterCategoryList extends RecyclerView.Adapter<AdapterCategoryList.ViewHolder> {

    private Context context;
//    LayoutInflater layoutInflater;
    String frameName[];

    String type;

    public AdapterCategoryList(Context context, String frameName[], String type) {
        this.context = context;
        this.type = type;
        this.frameName = frameName;
//        this.layoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.dropdwon_raw, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        SharedPrefrenceConfig sharedprefconfig = new SharedPrefrenceConfig(context);

        holder.tv_dd_cat_name.setText(frameName[position]);
        holder.tv_dd_cat_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* if (frameName[position].equals("All")) {
                    ActivityHome.getInstance().popupWindowHelper.dismiss();
                    sharedprefconfig.saveStringPreferance(context, Constance.language, "All");
//                    sharedprefconfig.saveStringPreferance(context, Constance.language, "Gujarati,Hindi,English");
                } else {
                    ActivityHome.getInstance().popupWindowHelper.dismiss();
                    sharedprefconfig.saveStringPreferance(context, Constance.language,frameName[position]);
                }
                if (type.equals("allList")) {
                    ActivitySingleCategoyList.getInstance().popupWindowHelper.dismiss();
                    ActivitySingleCategoyList.getInstance().loadList();
                }
                if (type.equals("home")) {
                    ActivityHome.getInstance().popupWindowHelper.dismiss();
                }
                if (type.equals("video")){
                    ActivitySingleVideoList.getInstance().popupWindowHelper.dismiss();
                    ActivitySingleVideoList.getInstance().loadList();
                }*/


                if (type.equals("allList")) {
//                    ActivitySingleCategoyList.getInstance().popupWindowHelper.dismiss();
                    if (frameName[position].equals("All")) {
                        ActivityViewAllList.getInstance().popupWindowHelper.dismiss();
                        sharedprefconfig.saveStringPreferance(context, Constance.language, "All");
//                    sharedprefconfig.saveStringPreferance(context, Constance.language, "Gujarati,Hindi,English");
                    } else {
                        ActivityViewAllList.getInstance().popupWindowHelper.dismiss();
                        sharedprefconfig.saveStringPreferance(context, Constance.language,frameName[position]);
                    }
                    ActivityViewAllList.getInstance().loadList();
                }

                if (type.equals("single")) {
//                    ActivitySingleCategoyList.getInstance().popupWindowHelper.dismiss();

                    if (frameName[position].equals("All")) {
                        ActivitySingleCategoyList.getInstance().popupWindowHelper.dismiss();
                        sharedprefconfig.saveStringPreferance(context, Constance.language, "All");
//                    sharedprefconfig.saveStringPreferance(context, Constance.language, "Gujarati,Hindi,English");

                    } else {
                        ActivitySingleCategoyList.getInstance().popupWindowHelper.dismiss();
                        sharedprefconfig.saveStringPreferance(context, Constance.language,frameName[position]);
                    }
                    ActivitySingleCategoyList.getInstance().loadList();
                }
                if (type.equals("home")) {
//                    ActivityHome.getInstance().popupWindowHelper.dismiss();
                    if (frameName[position].equals("All")) {
                        ActivityHome.getInstance().popupWindowHelper.dismiss();
                        sharedprefconfig.saveStringPreferance(context, Constance.language, "All");
//                    sharedprefconfig.saveStringPreferance(context, Constance.language, "Gujarati,Hindi,English");

                    } else {
                        ActivityHome.getInstance().popupWindowHelper.dismiss();
                        sharedprefconfig.saveStringPreferance(context, Constance.language,frameName[position]);
                    }
                }

                if (type.equals("video")){
//                    ActivitySingleVideoList.getInstance().popupWindowHelper.dismiss();
                    if (frameName[position].equals("All")) {
                        ActivitySingleVideoList.getInstance().popupWindowHelper.dismiss();
                        sharedprefconfig.saveStringPreferance(context, Constance.language, "All");
//                    sharedprefconfig.saveStringPreferance(context, Constance.language, "Gujarati,Hindi,English");
                    } else {
                        ActivitySingleVideoList.getInstance().popupWindowHelper.dismiss();
                        sharedprefconfig.saveStringPreferance(context, Constance.language,frameName[position]);
                    }
                    ActivitySingleVideoList.getInstance().loadList();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return frameName.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_dd_cat_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_dd_cat_name = itemView.findViewById(R.id.tv_dd_cat_name);
        }
    }
}
