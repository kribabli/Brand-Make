package com.festivalbanner.digitalposterhub.Utills;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;

import com.festivalbanner.digitalposterhub.ModelRetrofit.CategoriesData;
import com.festivalbanner.digitalposterhub.ModelRetrofit.CompanyDetails;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
import com.festivalbanner.digitalposterhub.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Constance {

    public static String adType = "Ad Mob";
//    public static String adType = "facebook";
    public static String FontStyle="ABeeZee.otf";
    public static String CToken = "CToken";
    public static String Status = "Status";
    public static String userStatus = "userStatus";
    public static String fragmentName = "";
    public static String language="ABeeZee.otf";
    public static Bitmap createdBitmap;
    public static String savedVideoPath="";
    public static String savedImagePath="";
    public static boolean AllowToOpenAdvertise = false;
    public static boolean isFirstTimeOpen = true;
    public static int heightOfImage;
    public static int widthOfImage;
    public static String checkFragment="";
    public static String ComeFrom="";
    public static String activityName="";
    public static String FromSinglecatActivity="";
    public static String Next_page_url=null;
    public static String name_type;

    // public static ArrayList<ModelHomeChild> childDataList=new ArrayList<>();
   public static ArrayList<CategoriesData> childDataList=new ArrayList<>();
  //  public static ArrayList<ModelHomeChild> viewAllDataList=new ArrayList<>();
    public static ArrayList<CategoriesData> viewAllDataList=new ArrayList<>();
    public static ArrayList<CompanyDetails> companyDetailsArrayList=new ArrayList<>();
    public static ArrayList<File> mypostImageList=new ArrayList<>();
    public static ArrayList<File> mypostVideoList=new ArrayList<>();

    public static String FolderName ="Brand Maker";
    public static String FolderNameSave ="/Brand Maker";
   // public static File FileSaveDirectory = Environment.getExternalStoragePublicDirectory("/"+FolderNameSave+ "/");
    public static File FileSaveDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" +Constance.FolderName);
    public static File FileSaveVideoDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" +Constance.FolderName);
    public static String FolderNameShare ="/Brand Maker";
    public static int Backgroundcolor=R.color.colorWhite;
    public static boolean isBackgroundcolor = false;
    public static boolean isOpacity = false;

    public static boolean isPremium = false;
    public static boolean isStickerTouch = false;
    public static boolean isStickerAvail = false;
    public static int selectedbackgroundcolor;
    public static String mobileNo = "";
    public static String emailId = "";

    public static  String aboutusurl= Base_Url.BASE_URL+"page/about-us";
    public static String PolicyUrl = "https://www.privacypolicygenerator.info/live.php?token=q1DSLf0DXrYfkWpXF7m4ZzcD7upAYjYf";
    public static String moreAppurl = "https://play.google.com/store/apps/details?id=";

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    public static void privacyPolicy(Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        alert.setTitle(R.string.app_name);
        final WebView wv = new WebView(context);
        wv.loadUrl(PolicyUrl);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return false;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
                Uri uri = request.getUrl();
                webView.loadUrl(uri.toString());
                return false;
            }
        });
        alert.setView(wv);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public static String shareapp_url = "Check out the App at: https://play.google.com/store/apps/details?id=";
    public static String rateapp = "http://play.google.com/store/apps/details?id=";
    public static String aboutUs = "Your content should be the voice of your brand. And, at Brand make, we make sure that we surpass your expectations while meeting your audience’s needs. We believe content which is engaging, relevant, and informative is the core to establishing your brand’s reputation online.\n" +
            "This is why we invest time in researching your target audience. We take into consideration several factors such as individual preferences, demographics, platform usage, and trends to build a content plan that yields results.\n"+
            "\n"+
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.\n"+
            "\n"+
            "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

    public static boolean isAvailable(Intent intent, Context context) {
        final PackageManager mgr = context.getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


}
