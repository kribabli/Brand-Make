package com.festivalbanner.digitalposterhub.Adapters;

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

import com.festivalbanner.digitalposterhub.R;
import com.teamup.app_sync.AppSyncBackkgroundTint;

import java.util.List;


public class BlankAdapter extends RecyclerView.Adapter<BlankAdapter.ViewHolder> {

    public List<String> blog_list;
    public Context context;

    public BlankAdapter(List<String> blog_list) {
        this.blog_list = blog_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main, parent, false);

        context = parent.getContext();

        return new ViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        Handle_set_data(position, holder);

//        Handle_color(position, holder);

    }

    private void Handle_set_data(int position, ViewHolder holder) {

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

        private ImageView imageView1, imageView2, imageView3;
        TextView Txt1, Txt2, Txt3, Txt4, Txt5;
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

        }


    }


}
