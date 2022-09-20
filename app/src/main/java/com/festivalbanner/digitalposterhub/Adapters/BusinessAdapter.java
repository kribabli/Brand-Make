package com.festivalbanner.digitalposterhub.Adapters;

import static com.bumptech.glide.Glide.with;
import static com.festivalbanner.digitalposterhub.R.drawable.image_place_holder;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.festivalbanner.digitalposterhub.Activities.AfterBusinessSelection;
import com.festivalbanner.digitalposterhub.Activities.BusinessSelection;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
import com.festivalbanner.digitalposterhub.Utills.Admin;
import com.festivalbanner.digitalposterhub.Utills.BusinessItem;
import com.teamup.app_sync.AppSyncBackkgroundTint;

import java.util.List;


public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.ViewHolder> {

    public List<BusinessItem> blog_list;
    public Context context;

    public BusinessAdapter(List<BusinessItem> blog_list) {
        this.blog_list = blog_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_business, parent, false);

        context = parent.getContext();

        return new ViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        Handle_set_data(position, holder);

        Handle_clicker(position, holder);

//        Handle_color(position, holder);

    }

    private void Handle_clicker(int position, ViewHolder holder) {
        holder.liner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusinessSelection.business_id = blog_list.get(position).getId();
                BusinessSelection.business_cat = blog_list.get(position).getBusinessname();
                ((Activity) context).finish();
                Admin.Handle_activity_opener(context, AfterBusinessSelection.class);
            }
        });
    }

    private void Handle_set_data(int position, ViewHolder holder) {
        holder.business_name_txt.setText("" + blog_list.get(position).getBusinessname());
        with(context).load(Base_Url.BASE_URL2 + "public/storage/" + blog_list.get(position).getBusinessimage()).placeholder(image_place_holder).into(holder.business_img);
    }

    private void Handle_color(int position, ViewHolder holder) {
        if (position % 2 == 0) {
            AppSyncBackkgroundTint.setBackgroundTint(R.color.whiite, holder.liner, context);
        }
    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView business_img, imageView2, imageView3;
        TextView business_name_txt, Txt2, Txt3, Txt4, Txt5;
        private View mView;
        Button Btn1, Btn2, Btn3, Btn4;
        ProgressBar progressBar;
        LinearLayoutCompat liner;
        CardView cardView;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            liner = itemView.findViewById(R.id.liner);
            business_name_txt = itemView.findViewById(R.id.business_name_txt);
            business_img = itemView.findViewById(R.id.business_img);

        }


    }


}
