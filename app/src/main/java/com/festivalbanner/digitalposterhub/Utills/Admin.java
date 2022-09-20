package com.festivalbanner.digitalposterhub.Utills;

import static java.lang.Thread.currentThread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.festivalbanner.digitalposterhub.R;


public class Admin {

   public static TinyDB tinyDB;

   public static int getLineNumber() {
      return currentThread().getStackTrace()[3].getLineNumber();
   }

   public static void intializeLocalRoom(Context context) {
      tinyDB = new TinyDB(context);
   }

   public static void OverrideNow(Context context) {
      ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
   }

   public static void HandleToolBar(Context context, String title, ImageView backImg, TextView titleTxt) {
      titleTxt.setText("" + title);
      backImg.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            ((Activity) context).finish();
            OverrideNow(context);
         }
      });
   }



   public static void Handle_activity_opener(Context context, Class opener_class) {
      ((Activity) context).startActivity(new Intent(context, opener_class));
      OverrideNow(context);
   }
}
