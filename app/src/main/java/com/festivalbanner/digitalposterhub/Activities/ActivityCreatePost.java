package com.festivalbanner.digitalposterhub.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.festivalbanner.digitalposterhub.Adapters.AdapterBackgroundImage;
import com.festivalbanner.digitalposterhub.Adapters.AdapterFontList;
import com.festivalbanner.digitalposterhub.Adapters.AdapterFrames;
import com.festivalbanner.digitalposterhub.Adapters.AdapterTextColourPicker;
import com.festivalbanner.digitalposterhub.Adapters.StickersListAdapter;
import com.festivalbanner.digitalposterhub.AnalyticsApplication;
import com.festivalbanner.digitalposterhub.Model.ModelBackgroundImage;
import com.festivalbanner.digitalposterhub.Model.ModelColorList;
import com.festivalbanner.digitalposterhub.Model.ModelFontDetail;
import com.festivalbanner.digitalposterhub.Model.ModelFramesDetails;
import com.festivalbanner.digitalposterhub.Model.StickersModel;
import com.festivalbanner.digitalposterhub.ModelRetrofit.CompanyRecord;
import com.festivalbanner.digitalposterhub.ModelRetrofit.RecordRegister;
import com.festivalbanner.digitalposterhub.ModelRetrofit.ResponseLogin;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
import com.festivalbanner.digitalposterhub.StickerClasses.DrawableSticker;
import com.festivalbanner.digitalposterhub.StickerClasses.Sticker;
import com.festivalbanner.digitalposterhub.StickerClasses.StickerView;
import com.festivalbanner.digitalposterhub.StickerClasses.TextSticker;
import com.festivalbanner.digitalposterhub.Utills.Admin;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.festivalbanner.digitalposterhub.Utills.PaletteBar;
import com.festivalbanner.digitalposterhub.Utills.PathUtills;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
import com.festivalbanner.digitalposterhub.Utills.Utils_VideoDownload;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.festivalbanner.digitalposterhub.R;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.teamup.app_sync.AppSyncTextUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCreatePost extends AppCompatActivity {

    Context context;
    ImageView iv_customimage, iv_vp_play, iv_cp_pic;
    int finalframeimage;
    Tracker mTracker;
    String FromSinglecatActivity = "";
    View layout_custom_frame1, layout_custom_frame2, layout_custom_frame3, layout_custom_frame4, layout_custom_frame5, layout_custom_frame6,
            layout_custom_frame7, layout_custom_frame8, layout_custom_frame9, layout_custom_frame10,
            layout_custom_frame11, layout_custom_frame12, layout_custom_frame13;
    TextView tv_mobilenumberlayout1, tv_mobilenumberlayout2, tv_mobilenumberlayout3, tv_mobilenumberlayout4, tv_mobilenumberlayout5, tv_mobilenumberlayout6,
            tv_calllayout7, tv_calllayout8, tv_calllayout9, tv_calllayout10, tv_calllayout11,
            tv_calllayout12, tv_calllayout13;
    TextView tv_websitelayout1, tv_websitelayout2, tv_websitelayout3, tv_websitelayout4, tv_websitelayout5, tv_websitelayout6, /*tv_websitelayout7,*/
            tv_websitelayout8, tv_websitelayout9, tv_websitelayout10, tv_websitelayout11, tv_websitelayout12, tv_websitelayout13;
    TextView tv_emaillayout1, tv_emaillayout2, tv_emaillayout3, tv_emaillayout4, tv_emaillayout5, tv_emaillayout6,/* tv_emaillayout7,*/
            tv_emaillayout8,
            tv_emaillayout9, tv_emaillayout10, tv_emaillayout11, tv_emaillayout12, tv_emaillayout13;
    //TextView tv_addresslayout4;

    ImageView iv_calllayout1, iv_calllayout2, iv_calllayout3, iv_calllayout4, iv_calllayout5, iv_calllayout6;
    ImageView iv_weblayout1, iv_weblayout2, iv_weblayout3, iv_weblayout4, iv_weblayout5, iv_weblayout6, /*iv_weblayout7,*/
            iv_weblayout8,
            iv_weblayout11, iv_weblayout12, iv_weblayout13;
    ImageView iv_emaillayout1, iv_emaillayout2, iv_emaillayout3, iv_emaillayout4, iv_emaillayout5, iv_emaillayout6, iv_emaillayout8,
            iv_emaillayout9, iv_emaillayout10, iv_emaillayout11, iv_emaillayout12, iv_emaillayout13;
    //ImageView iv_addresslayout4;
    ImageView iv_logolayout1, iv_logolayout2, iv_logolayout3, iv_logolayout4, iv_logolayout5, iv_logolayout6, iv_logolayout7, iv_logolayout8,
            iv_logolayout9, iv_logolayout10, iv_logolayout11, iv_logolayout12, iv_logolayout13;
    LinearLayout ll_f1_call, ll_f2_call, ll_f3_call, ll_f4_call, ll_f5_call, ll_f6_call, ll_f7_call, ll_f8_call,
            ll_f9_call, ll_f10_call, ll_f11_call, ll_f12_call, ll_f13_call;
    LinearLayout ll_f1_email, ll_f2_email, ll_f3_email, ll_f4_email, ll_f5_email, ll_f6_email, /*ll_f7_email,*/
            ll_f8_email,
            ll_f9_email, ll_f10_email, ll_f11_email, ll_f12_email, ll_f13_email;
    LinearLayout ll_f1_web, ll_f2_web, ll_f3_web, ll_f4_web, ll_f5_web, ll_f6_web, /*ll_f7_web,*/
            ll_f8_web, ll_f9_web,
            ll_f10_web, ll_f11_web, ll_f12_web, ll_f13_web;
    LinearLayout ll_f1_location, ll_f2_location, ll_f3_location, ll_f4_location, ll_f5_location, ll_f6_location, ll_f7_location, ll_f8_location,
            ll_f9_location, ll_f10_location, ll_f11_location, ll_f12_location, ll_f13_location;
    TextView tv_f1_name, tv_f2_name, tv_f3_name, tv_f4_name, tv_f5_name, tv_f6_name, tv_f7_name, tv_f8_name,
            tv_f9_name, tv_f10_name, tv_f11_name, tv_f12_name, tv_f13_name;
    TextView tv_locationlayout1, tv_locationlayout2, tv_locationlayout3, tv_locationlayout4, tv_locationlayout5, tv_locationlayout6, tv_locationlayout7,
            tv_locationlayout8, tv_locationlayout9, tv_locationlayout10, tv_locationlayout11, tv_locationlayout12, tv_locationlayout13;
    LinearLayout ll_f6_bottom_bg, ll_f2_bottom_bg, ll_f4_bottom_bg;
    // EditText et_usertext;
    RecyclerView rv_framelist;
    AdView mAdView;
    LinearLayout facbook_ad_banner;
    public AdapterFrames adapterFrame;
    public ArrayList<ModelFontDetail> modelFontDetailArrayList;
    Bitmap bitmapsave;
    String[] fo = new String[]
            {"abhayalibre_bold.ttf", "abhayalibre_extrabold.ttf", "abhayalibre_medium.ttf", "artifika_regular.ttf", "archivo_black.ttf",
                    "ArchivoNarrow.otf", "ABeeZee.otf", "After_Shok.ttf", "AbrilFatface.otf", "Acknowledgement.otf",
                    "Acme.ttf", "AlfaSlabOne.ttf", "AlmendraDisplay.otf", "Almendra.otf", "alpha_echo.ttf",
                    "Amadeus.ttf", "AMERSN.ttf", "ANUDI.ttf", "AquilineTwo.ttf", "Arbutus.ttf", "AlexBrush.ttf",
                    "Alisandra.ttf", "Allura.ttf", "Amarillo.ttf", "BEARPAW.ttf", "bigelowrules.ttf", "BLACKR.ttf",
                    "BOYCOTT.ttf", "BebasNeue.ttf", "BLKCHCRY.TTF", "Carousel.ttf", "Caslon_Calligraphic.ttf",
                    "CroissantOne.ttf", "Carnevalee-Freakshow.ttf", "CAROBTN.TTF", "CaviarDreams.ttf",
                    "Cocogoose.ttf", "diplomata.ttf", "deftone stylus.ttf", "Dosis.ttf", "FONTL.TTF",
                    "Hugtophia.ttf", "ICE_AGE.ttf", "Kingthings_Calligraphica.ttf", "Love Like This.ttf",
                    "MADE Canvas.otf", "Merci-Heart-Brush.ttf", "Metropolis.otf", "Montserrat.otf",
                    "MontserratAlternates.otf",
                    "norwester.otf", "ostrich.ttf", "squealer.ttf", "Titillium.otf", "Ubuntu.ttf"};

    StickerView sticker_view;
    TextSticker txtsticker;
    LinearLayout ll_border, ll_camera_pic, ll_addimage, ll_flip, ll_main, ll_sticker, ll_text_sticker, ll_set_boder;
    EditText et_text_sticker;
    RelativeLayout rl_content, rl_createquote;
    int seekvalue;
    Float dx, dy;
    public ArrayList<ModelFontDetail> arrayList;

    RelativeLayout opacitybg, rl_video;
    VideoView vv_videoshow;
    SharedPrefrenceConfig sharedPrefrenceConfig;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static ActivityCreatePost instance = null;
    String comeFrom;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    ProgressDialog pDialog;
    StickersModel[] stickerlist;
    RecordRegister getuser;

    public ActivityCreatePost() {
        instance = ActivityCreatePost.this;
    }

    public static synchronized ActivityCreatePost getInstance() {
        if (instance == null) {
            instance = new ActivityCreatePost();
        }
        return instance;
    }

    Uri photouri;
    String mCurrentPhotoPath;

    private boolean isZoomAndRotate;
    private boolean isOutSide;
    String abc;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float xCoOrdinate, yCoOrdinate;
    float[] lastEvent = null;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    int count = 0, textcolor;
    int textStickerColor = R.color.colorBlack;
    PopupWindow mPopupWindow, mPopupWindowpw;
    ProgressDialog pDialog1;
    ProgressBar pb_sp;
    public boolean showingFirst = true;
    public boolean showingsecond = true;
    MediaController mediacontroller;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    ImageView tv_remove_logo, tv_remove_email, tv_remove_call, tv_remove_web, tv_remove_name, tv_remove_location;
    Boolean logo = true, call = true, email = true, web = true, name = true, loct = true;
    boolean ismail = true, isweb = true, ismobile = true;
    File imagesDir;
    boolean allViewShow = true;
    Calendar calendar;
    int opcity = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        context = ActivityCreatePost.this;
        bindView();
        //requestPermission();
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        sharedPrefrenceConfig = new SharedPrefrenceConfig(context);
        calculationForHeight();
        getbusinessdetail();

        SharedPrefrenceConfig pref = new SharedPrefrenceConfig(context);
        RecordRegister getuser = pref.getUser();
//        if (getuser.getPlan_type() == null || !getuser.getPlan_type().equals("Premium")) {
        if (!Constance.isPremium) {
            if (Constance.adType.equals("Ad Mob")) {
                loadAdMobAd();
            } else {
                loadFacebookAd();
            }
        } else {
            mAdView.setVisibility(View.GONE);
            facbook_ad_banner.setVisibility(View.GONE);
        }

        stickerlist = new StickersModel[]{
                new StickersModel(R.drawable.pf_s1), new StickersModel(R.drawable.pf_s2),
                new StickersModel(R.drawable.pf_s3), new StickersModel(R.drawable.pf_s4),
                new StickersModel(R.drawable.pf_s10), new StickersModel(R.drawable.pf_s11),
                new StickersModel(R.drawable.pf_s12), new StickersModel(R.drawable.pf_s13),
                new StickersModel(R.drawable.pf_s14), new StickersModel(R.drawable.pf_s15),
                new StickersModel(R.drawable.pf_s16), new StickersModel(R.drawable.pf_s17),
                new StickersModel(R.drawable.pf_s18), new StickersModel(R.drawable.pf_s19),
                new StickersModel(R.drawable.pf_s20), new StickersModel(R.drawable.pf_s21),
                new StickersModel(R.drawable.pf_s22), new StickersModel(R.drawable.pf_s23),
                new StickersModel(R.drawable.pf_s24), new StickersModel(R.drawable.pf_s25),
                new StickersModel(R.drawable.pf_s26), new StickersModel(R.drawable.pf_s27),
                new StickersModel(R.drawable.pf_s28), new StickersModel(R.drawable.pf_s29),
                new StickersModel(R.drawable.pf_s30), new StickersModel(R.drawable.pf_s31),
                new StickersModel(R.drawable.pf_s32), new StickersModel(R.drawable.pf_s33),
                new StickersModel(R.drawable.pf_s34), new StickersModel(R.drawable.pf_s35),
                new StickersModel(R.drawable.pf_s36), new StickersModel(R.drawable.pf_s37),
                new StickersModel(R.drawable.pf_s38), new StickersModel(R.drawable.pf_s39),
                new StickersModel(R.drawable.pf_s40), new StickersModel(R.drawable.pf_s41),
                new StickersModel(R.drawable.pf_s42), new StickersModel(R.drawable.pf_s43),
                new StickersModel(R.drawable.pf_s44), new StickersModel(R.drawable.pf_s45),
                new StickersModel(R.drawable.pf_s46), new StickersModel(R.drawable.pf_s47),
                new StickersModel(R.drawable.pf_s48), new StickersModel(R.drawable.pf_s49),
                new StickersModel(R.drawable.pf_s50), new StickersModel(R.drawable.pf_s51),
                new StickersModel(R.drawable.pf_s52), new StickersModel(R.drawable.pf_s53),
                new StickersModel(R.drawable.pf_s54),
        };

        tv_remove_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email) {
//                    tv_remove_email.setTextColor(getResources().getColor(R.color.colorTheame));
                    tv_remove_email.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundcorner_view_greyclr));
                    email = false;
                    removeEmail(email);
                } else {
//                    tv_remove_email.setTextColor(getResources().getColor(R.color.colorWhite));
                    tv_remove_email.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
                    email = true;
                    removeEmail(email);
                }
            }
        });

        tv_remove_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logo) {
//                    tv_remove_logo.setTextColor(getResources().getColor(R.color.colorTheame));
                    tv_remove_logo.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundcorner_view_greyclr));
                    logo = false;
                    removeLogo(logo);
                } else {
//                    tv_remove_logo.setTextColor(getResources().getColor(R.color.colorWhite));
                    tv_remove_logo.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
                    logo = true;
                    removeLogo(logo);
                }
            }
        });

        tv_remove_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (call) {
//                    tv_remove_call.setTextColor(getResources().getColor(R.color.colorTheame));
                    tv_remove_call.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundcorner_view_greyclr));
                    call = false;
                    removeCall(call);
                } else {
//                    tv_remove_call.setTextColor(getResources().getColor(R.color.colorWhite));
                    tv_remove_call.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
                    call = true;
                    removeCall(call);
                }
            }
        });

        tv_remove_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (web) {
//                    tv_remove_web.setTextColor(getResources().getColor(R.color.colorTheame));
                    tv_remove_web.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundcorner_view_greyclr));
                    web = false;
                    removeWeb(web);
                } else {
//                    tv_remove_web.setTextColor(getResources().getColor(R.color.colorWhite));
                    tv_remove_web.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
                    web = true;
                    removeWeb(web);
                }

            }
        });

        tv_remove_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name) {
//                    tv_remove_name.setTextColor(getResources().getColor(R.color.colorTheame));
                    tv_remove_name.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundcorner_view_greyclr));
                    name = false;
                    removeName(name);
                } else {
//                    tv_remove_name.setTextColor(getResources().getColor(R.color.colorWhite));
                    tv_remove_name.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
                    name = true;
                    removeName(name);
                }
            }
        });

        tv_remove_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loct) {
//                    tv_remove_location.setTextColor(getResources().getColor(R.color.colorTheame));
                    tv_remove_location.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundcorner_view_greyclr));
                    loct = false;
                    removeLoct(loct);
                } else {
//                    tv_remove_location.setTextColor(getResources().getColor(R.color.colorWhite));
                    tv_remove_location.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
                    loct = true;
                    removeLoct(loct);
                }

            }
        });

        sticker_view.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker PFSticker) {
                sticker_view.hideIcons(false);
                sticker_view.setLocked(false);
            }

            @Override
            public void onStickerClicked(@NonNull Sticker PFSticker1) {
                sticker_view.hideIcons(false);
                sticker_view.setLocked(false);
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker PFSticker) {

            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker PFSticker) {

            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker PFSticker) {

            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker PFSticker) {

            }

            @Override
            public void onStickerFlipped(@NonNull Sticker PFSticker) {

            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker PFSticker) {
                Log.d("dfhsjhfhdshfd", "dfsfsdfko");
                sticker_view.hideIcons(false);
                sticker_view.setLocked(false);
                et_text_sticker.setVisibility(View.VISIBLE);
                PFSticker = (TextSticker) sticker_view.getCurrentSticker();
                et_text_sticker.setText(((TextSticker) PFSticker).getText());
                et_text_sticker.setTextColor(((TextSticker) PFSticker).getTextColor());
                sticker_view.remove(PFSticker);
            }
        });
        //touchListener(rl_content);
        Constance.isStickerAvail = false;
        if (!Constance.isStickerAvail) {
            Constance.isStickerTouch = false;
            sticker_view.setLocked(true);
        }
        touchListener(rl_content);

        modelFontDetailArrayList = new ArrayList<>();
        pb_sp.setVisibility(View.GONE);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_framelist.setLayoutManager(layoutManager);

        adapterFrame = new AdapterFrames(context, getFramesList());
        adapterFrame.notifyDataSetChanged();
        rv_framelist.setAdapter(adapterFrame);

        if (Constance.activityName.equals("greeting")) {

            iv_cp_pic.setVisibility(View.VISIBLE);
            ll_border.setVisibility(View.VISIBLE);
            ll_camera_pic.setVisibility(View.VISIBLE);
            ll_addimage.setVisibility(View.VISIBLE);
            ll_flip.setVisibility(View.VISIBLE);
            ll_sticker.setVisibility(View.VISIBLE);
            ll_text_sticker.setVisibility(View.VISIBLE);
            ll_set_boder.setVisibility(View.VISIBLE);
        }
        ll_set_boder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBorder();
            }
        });
        ll_flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.fliprotation_popup, null);

                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                if (Build.VERSION.SDK_INT >= 21) {
                    mPopupWindow.setElevation(5.0f);
                }
                TextView tvHorizonal = customView.findViewById(R.id.tvHorizontal);
                TextView tvVertical = customView.findViewById(R.id.tvVertical);

                tvHorizonal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (showingFirst) {
                            iv_cp_pic.setRotationY(180f);
                            showingFirst = false;
                        } else {
                            iv_cp_pic.setRotationY(0f);
                            showingFirst = true;
                        }
                        mPopupWindow.dismiss();
                    }

                });

                tvVertical.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (showingsecond) {
                            iv_cp_pic.setRotationX(180f);
                            showingsecond = false;
                        } else {
                            iv_cp_pic.setRotationX(0f);
                            showingsecond = true;
                        }
                        mPopupWindow.dismiss();
                    }
                });
                mPopupWindow.setOutsideTouchable(true);

                mPopupWindow.showAtLocation(ll_main, Gravity.BOTTOM, 0, 0);
            }
        });
        ll_camera_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a pf_camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    File photoThumbnailFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {

                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        photouri = FileProvider.getUriForFile(context,
                                getString(R.string.file_provider_authority),
                                photoFile);

//                       Uri photoThumbnailURI = FileProvider.getUriForFile(context,
//                               getString(R.string.file_provider_authority),
//                                photoThumbnailFile);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photouri);
                        startActivityForResult(takePictureIntent, 2);

                    }
                }
            }
        });

        et_text_sticker.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you wants to Edit Sticker");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            textStickerEditPopUp();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (et_text_sticker.getText().toString().equals("") || et_text_sticker.getText().toString().equals(null)) {
                                // AlertDialog.AlertMessage(context, "Please enter pf_text");
                            } else {
                                et_text_sticker.setVisibility(View.GONE);
                                txtsticker = new TextSticker(context);
                                et_text_sticker.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(et_text_sticker.getWindowToken(), 0);
                                    }
                                });
                                txtsticker.setText("");
                                txtsticker.getText();
                                txtsticker.setText(et_text_sticker.getText().toString());
                                txtsticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + Constance.FontStyle));
                                txtsticker.setTextColor(getResources().getColor(textStickerColor));
                                textStickerColor = R.color.colorBlack;
                                txtsticker.resizeText();
                                sticker_view.addSticker(txtsticker);
                            }
                        }
                    });

                  /*  AlertDialog alert = builder.create();
                    alert.show();
                    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(Color.BLACK);
                    pbutton.setBackgroundColor(Color.WHITE);

                    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setTextColor(Color.BLACK);
                    nbutton.setBackgroundColor(Color.WHITE);*/

                    builder.create();
                    AlertDialog dialog = builder.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                        }
                    });
                    dialog.show();

                }
                return false;
            }
        });

        ll_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStickers();
            }
        });

        ll_text_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_text_sticker.setVisibility(View.VISIBLE);
                et_text_sticker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        et_text_sticker.post(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(et_text_sticker, InputMethodManager.SHOW_IMPLICIT);
                            }
                        });
                    }
                });
                et_text_sticker.requestFocus();
                et_text_sticker.getText().clear();
                et_text_sticker.setTextColor(getResources().getColor(R.color.hint));
                et_text_sticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + "Acme.ttf"));
                et_text_sticker.setHint("your text for sticker");

            }
        });
        iv_cp_pic.setOnTouchListener(new View.OnTouchListener() {

            RelativeLayout.LayoutParams parms;
            int startwidth;
            int startheight;
            float dx = 0, dy = 0, x = 0, y = 0;
            float angle = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final ImageView view = (ImageView) v;

                viewTransformation(view, event);

                return true;

            }
        });



       /* FromSinglecatActivity = getIntent().getExtras().getString("FromSinglecatActivity");
        comeFrom = getIntent().getExtras().getString("ComeFrom");*/

      /*  FromSinglecatActivity =Constance.FromSinglecatActivity;
        comeFrom = Constance.ComeFrom;

        if (comeFrom.equals("image")) {

            rl_video.setVisibility(View.GONE);
            if (FromSinglecatActivity != null) {
                Glide.with(context).load(FromSinglecatActivity).into(iv_customimage);
            } else {
                // iv_customimage.setImageResource(getFramesList().get(0).getFrames());
                //   finalframeimage = getFramesList().get(0).getFrames();
            }
        } else {
            rl_video.setVisibility(View.VISIBLE);
            if (FromSinglecatActivity != null) {
                playVideo();
            } else {
                // iv_customimage.setImageResource(getFramesList().get(0).getFrames());
                //   finalframeimage = getFramesList().get(0).getFrames();
            }
        }*/

        iv_vp_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.dismiss();
                Log.d("wseqwqwqwqwqwqwqw", "sdss");
                vv_videoshow.start();
                iv_vp_play.setVisibility(View.GONE);
            }
        });


        PRDownloader.initialize(getApplicationContext());
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);
    }

    public void removeLogo(Boolean logoLay) {

        if (logoLay) {
            iv_logolayout1.setVisibility(View.VISIBLE);
            iv_logolayout2.setVisibility(View.VISIBLE);
            iv_logolayout3.setVisibility(View.VISIBLE);
            iv_logolayout4.setVisibility(View.VISIBLE);
            iv_logolayout5.setVisibility(View.VISIBLE);
            iv_logolayout6.setVisibility(View.VISIBLE);
            iv_logolayout7.setVisibility(View.VISIBLE);
            iv_logolayout8.setVisibility(View.VISIBLE);
            iv_logolayout9.setVisibility(View.VISIBLE);
            iv_logolayout10.setVisibility(View.VISIBLE);
            iv_logolayout11.setVisibility(View.VISIBLE);
            iv_logolayout12.setVisibility(View.VISIBLE);
            iv_logolayout13.setVisibility(View.VISIBLE);
        } else {
            iv_logolayout1.setVisibility(View.GONE);
            iv_logolayout2.setVisibility(View.GONE);
            iv_logolayout3.setVisibility(View.GONE);
            iv_logolayout4.setVisibility(View.GONE);
            iv_logolayout5.setVisibility(View.GONE);
            iv_logolayout6.setVisibility(View.GONE);
            iv_logolayout7.setVisibility(View.GONE);
            iv_logolayout8.setVisibility(View.GONE);
            iv_logolayout9.setVisibility(View.GONE);
            iv_logolayout10.setVisibility(View.GONE);
            iv_logolayout11.setVisibility(View.GONE);
            iv_logolayout12.setVisibility(View.GONE);
            iv_logolayout13.setVisibility(View.GONE);
        }
    }

    public void removeEmail(Boolean emailLay) {
        ismail = emailLay;
        if (emailLay) {
            ll_f1_email.setVisibility(View.VISIBLE);
            ll_f2_email.setVisibility(View.VISIBLE);
            ll_f3_email.setVisibility(View.VISIBLE);
            ll_f4_email.setVisibility(View.VISIBLE);
            ll_f5_email.setVisibility(View.VISIBLE);
            ll_f6_email.setVisibility(View.VISIBLE);
//            ll_f7_email.setVisibility(View.VISIBLE);
            ll_f8_email.setVisibility(View.VISIBLE);
            ll_f9_email.setVisibility(View.VISIBLE);
            ll_f10_email.setVisibility(View.VISIBLE);
            ll_f11_email.setVisibility(View.VISIBLE);
            ll_f12_email.setVisibility(View.VISIBLE);
            ll_f13_email.setVisibility(View.VISIBLE);
            iv_emaillayout11.setImageDrawable(getResources().getDrawable(R.drawable.mail));
            tv_emaillayout11.setText(Admin.tinyDB.getString("email"));

            iv_emaillayout12.setImageDrawable(getResources().getDrawable(R.drawable.mail));
            tv_emaillayout12.setText(Admin.tinyDB.getString("email"));

            iv_emaillayout13.setImageDrawable(getResources().getDrawable(R.drawable.mail));
            tv_emaillayout13.setText(Admin.tinyDB.getString("email"));

            if (isweb) {
                if (ismobile) {
                    ll_f11_call.setVisibility(View.VISIBLE);
                    ll_f12_call.setVisibility(View.VISIBLE);
                    ll_f13_call.setVisibility(View.VISIBLE);
                } else {
                    ll_f11_call.setVisibility(View.INVISIBLE);
                    ll_f12_call.setVisibility(View.INVISIBLE);
                    ll_f13_call.setVisibility(View.INVISIBLE);
                }
            } else {
                if (ismobile) {
                    ll_f11_web.setVisibility(View.VISIBLE);
                    ll_f12_web.setVisibility(View.VISIBLE);
                    ll_f13_web.setVisibility(View.VISIBLE);
                    iv_weblayout11.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_websitelayout11.setText(Admin.tinyDB.getString("number_one"));

                    iv_weblayout12.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_websitelayout12.setText(Admin.tinyDB.getString("number_one"));

                    iv_weblayout13.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_websitelayout13.setText(Admin.tinyDB.getString("number_one"));
                } else {
                    ll_f11_web.setVisibility(View.INVISIBLE);
                    ll_f12_web.setVisibility(View.INVISIBLE);
                    ll_f13_web.setVisibility(View.INVISIBLE);
                }
                ll_f11_call.setVisibility(View.INVISIBLE);
                ll_f12_call.setVisibility(View.INVISIBLE);
                ll_f13_call.setVisibility(View.INVISIBLE);
            }

        } else {
            ll_f1_email.setVisibility(View.GONE);
            ll_f2_email.setVisibility(View.GONE);
            ll_f3_email.setVisibility(View.GONE);
            ll_f6_email.setVisibility(View.GONE);
//            ll_f7_email.setVisibility(View.GONE);
            ll_f8_email.setVisibility(View.GONE);
            ll_f9_email.setVisibility(View.GONE);
            ll_f10_email.setVisibility(View.GONE);
            ll_f4_email.setVisibility(View.INVISIBLE);
            ll_f5_email.setVisibility(View.INVISIBLE);

            if (isweb) {
                if (ismobile) {
                    ll_f11_email.setVisibility(View.VISIBLE);
                    ll_f12_email.setVisibility(View.VISIBLE);
                    ll_f13_email.setVisibility(View.VISIBLE);
                    iv_emaillayout11.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_emaillayout11.setText(Admin.tinyDB.getString("number_one"));

                    iv_emaillayout12.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_emaillayout12.setText(Admin.tinyDB.getString("number_one"));

                    iv_emaillayout13.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_emaillayout13.setText(Admin.tinyDB.getString("number_one"));

                } else {
                    ll_f11_email.setVisibility(View.INVISIBLE);
                    ll_f12_email.setVisibility(View.INVISIBLE);
                    ll_f13_email.setVisibility(View.INVISIBLE);
                }

                ll_f11_call.setVisibility(View.INVISIBLE);
                ll_f12_call.setVisibility(View.INVISIBLE);
                ll_f13_call.setVisibility(View.INVISIBLE);
            } else {
                ll_f11_email.setVisibility(View.INVISIBLE);
                ll_f12_email.setVisibility(View.INVISIBLE);
                ll_f13_email.setVisibility(View.INVISIBLE);
                ll_f11_web.setVisibility(View.INVISIBLE);
                ll_f12_web.setVisibility(View.INVISIBLE);
                ll_f13_web.setVisibility(View.INVISIBLE);
                if (ismobile) {
                    ll_f11_call.setVisibility(View.VISIBLE);
                    ll_f12_call.setVisibility(View.VISIBLE);
                    ll_f13_call.setVisibility(View.VISIBLE);
                } else {
                    ll_f11_call.setVisibility(View.INVISIBLE);
                    ll_f12_call.setVisibility(View.INVISIBLE);
                    ll_f13_call.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void removeCall(Boolean callLay) {
        ismobile = callLay;
        if (callLay) {
            ll_f1_call.setVisibility(View.VISIBLE);
            ll_f2_call.setVisibility(View.VISIBLE);
            ll_f3_call.setVisibility(View.VISIBLE);
            ll_f4_call.setVisibility(View.VISIBLE);
            ll_f5_call.setVisibility(View.VISIBLE);
            ll_f6_call.setVisibility(View.VISIBLE);
            ll_f7_call.setVisibility(View.VISIBLE);
            ll_f8_call.setVisibility(View.VISIBLE);
            ll_f9_call.setVisibility(View.VISIBLE);
            ll_f10_call.setVisibility(View.VISIBLE);
            ll_f11_call.setVisibility(View.VISIBLE);
            ll_f12_call.setVisibility(View.VISIBLE);
            ll_f13_call.setVisibility(View.VISIBLE);

            if (isweb) {
                if (ismail) {
                    ll_f11_web.setVisibility(View.VISIBLE);
                    ll_f12_web.setVisibility(View.VISIBLE);
                    ll_f13_web.setVisibility(View.VISIBLE);
                    ll_f11_email.setVisibility(View.VISIBLE);
                    ll_f12_email.setVisibility(View.VISIBLE);
                    ll_f13_email.setVisibility(View.VISIBLE);

                    iv_emaillayout11.setImageDrawable(getResources().getDrawable(R.drawable.mail));
                    tv_emaillayout11.setText(Admin.tinyDB.getString("email"));
                    iv_emaillayout12.setImageDrawable(getResources().getDrawable(R.drawable.mail));
                    tv_emaillayout12.setText(Admin.tinyDB.getString("email"));
                    iv_emaillayout13.setImageDrawable(getResources().getDrawable(R.drawable.mail));
                    tv_emaillayout13.setText(Admin.tinyDB.getString("email"));

                    iv_weblayout11.setImageDrawable(getResources().getDrawable(R.drawable.web));
                    tv_websitelayout11.setText(Admin.tinyDB.getString("website"));
                    iv_weblayout12.setImageDrawable(getResources().getDrawable(R.drawable.web));
                    tv_websitelayout12.setText(Admin.tinyDB.getString("website"));
                    iv_weblayout13.setImageDrawable(getResources().getDrawable(R.drawable.web));
                    tv_websitelayout13.setText(Admin.tinyDB.getString("website"));

                    ll_f11_call.setVisibility(View.VISIBLE);
                    ll_f12_call.setVisibility(View.VISIBLE);
                    ll_f13_call.setVisibility(View.VISIBLE);

                } else {
                    ll_f11_web.setVisibility(View.VISIBLE);
                    ll_f12_web.setVisibility(View.VISIBLE);
                    ll_f13_web.setVisibility(View.VISIBLE);
                    iv_weblayout11.setImageDrawable(getResources().getDrawable(R.drawable.web));
                    tv_websitelayout11.setText(Admin.tinyDB.getString("website"));
                    iv_weblayout12.setImageDrawable(getResources().getDrawable(R.drawable.web));
                    tv_websitelayout12.setText(Admin.tinyDB.getString("website"));
                    iv_weblayout13.setImageDrawable(getResources().getDrawable(R.drawable.web));
                    tv_websitelayout13.setText(Admin.tinyDB.getString("website"));

                    ll_f11_email.setVisibility(View.VISIBLE);
                    ll_f12_email.setVisibility(View.VISIBLE);
                    ll_f13_email.setVisibility(View.VISIBLE);
                    ll_f11_call.setVisibility(View.INVISIBLE);
                    ll_f12_call.setVisibility(View.INVISIBLE);
                    ll_f13_call.setVisibility(View.INVISIBLE);
                    iv_emaillayout11.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_emaillayout11.setText(Admin.tinyDB.getString("number_one"));
                    iv_emaillayout12.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_emaillayout12.setText(Admin.tinyDB.getString("number_one"));
                    iv_emaillayout13.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_emaillayout13.setText(Admin.tinyDB.getString("number_one"));
                }

            } else {
                if (ismail) {
                    ll_f11_web.setVisibility(View.VISIBLE);
                    ll_f12_web.setVisibility(View.VISIBLE);
                    ll_f13_web.setVisibility(View.VISIBLE);
                    iv_weblayout11.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_websitelayout11.setText(Admin.tinyDB.getString("number_one"));
                    iv_weblayout12.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_websitelayout12.setText(Admin.tinyDB.getString("number_one"));
                    iv_weblayout13.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_websitelayout13.setText(Admin.tinyDB.getString("number_one"));
                    ll_f11_call.setVisibility(View.INVISIBLE);
                    ll_f12_call.setVisibility(View.INVISIBLE);
                    ll_f13_call.setVisibility(View.INVISIBLE);
                } else {
                    ll_f11_web.setVisibility(View.INVISIBLE);
                    ll_f12_web.setVisibility(View.INVISIBLE);
                    ll_f13_web.setVisibility(View.INVISIBLE);

                    ll_f11_email.setVisibility(View.INVISIBLE);
                    ll_f12_email.setVisibility(View.INVISIBLE);
                    ll_f13_email.setVisibility(View.INVISIBLE);

                    ll_f11_call.setVisibility(View.VISIBLE);
                    ll_f12_call.setVisibility(View.VISIBLE);
                    ll_f13_call.setVisibility(View.VISIBLE);
                }
            }
        } else {
            ll_f1_call.setVisibility(View.GONE);
            ll_f2_call.setVisibility(View.GONE);
            ll_f3_call.setVisibility(View.GONE);
            ll_f6_call.setVisibility(View.GONE);
            ll_f7_call.setVisibility(View.GONE);
            ll_f8_call.setVisibility(View.GONE);
            ll_f9_call.setVisibility(View.GONE);
            ll_f10_call.setVisibility(View.GONE);
            ll_f4_call.setVisibility(View.INVISIBLE);
            ll_f5_call.setVisibility(View.INVISIBLE);

            if (ismail) {
                if (isweb) {
                    ll_f11_web.setVisibility(View.VISIBLE);
                    ll_f12_web.setVisibility(View.VISIBLE);
                    ll_f13_web.setVisibility(View.VISIBLE);

                    ll_f11_email.setVisibility(View.VISIBLE);
                    ll_f12_email.setVisibility(View.VISIBLE);
                    ll_f13_email.setVisibility(View.VISIBLE);

                    iv_emaillayout11.setImageDrawable(getResources().getDrawable(R.drawable.mail));
                    tv_emaillayout11.setText(Admin.tinyDB.getString("email"));
                    iv_emaillayout12.setImageDrawable(getResources().getDrawable(R.drawable.mail));
                    tv_emaillayout12.setText(Admin.tinyDB.getString("email"));
                    iv_emaillayout13.setImageDrawable(getResources().getDrawable(R.drawable.mail));
                    tv_emaillayout13.setText(Admin.tinyDB.getString("email"));

                    iv_weblayout11.setImageDrawable(getResources().getDrawable(R.drawable.web));
                    tv_websitelayout11.setText(Admin.tinyDB.getString("website"));
                    iv_weblayout12.setImageDrawable(getResources().getDrawable(R.drawable.web));
                    tv_websitelayout12.setText(Admin.tinyDB.getString("website"));
                    iv_weblayout13.setImageDrawable(getResources().getDrawable(R.drawable.web));
                    tv_websitelayout13.setText(Admin.tinyDB.getString("website"));
                } else {
                    ll_f11_web.setVisibility(View.INVISIBLE);
                    ll_f12_web.setVisibility(View.INVISIBLE);
                    ll_f13_web.setVisibility(View.INVISIBLE);

                    ll_f11_email.setVisibility(View.VISIBLE);
                    ll_f12_email.setVisibility(View.VISIBLE);
                    ll_f13_email.setVisibility(View.VISIBLE);

                    iv_emaillayout11.setImageDrawable(getResources().getDrawable(R.drawable.mail));
                    tv_emaillayout11.setText(Admin.tinyDB.getString("email"));
                    iv_emaillayout12.setImageDrawable(getResources().getDrawable(R.drawable.mail));
                    tv_emaillayout12.setText(Admin.tinyDB.getString("email"));
                    iv_emaillayout13.setImageDrawable(getResources().getDrawable(R.drawable.mail));
                    tv_emaillayout13.setText(Admin.tinyDB.getString("email"));
                }
                ll_f11_call.setVisibility(View.INVISIBLE);
                ll_f12_call.setVisibility(View.INVISIBLE);
                ll_f13_call.setVisibility(View.INVISIBLE);
            } else {
                if (isweb) {
                    if (ismobile) {
                        ll_f11_email.setVisibility(View.VISIBLE);
                        ll_f12_email.setVisibility(View.VISIBLE);
                        ll_f13_email.setVisibility(View.VISIBLE);

                        iv_emaillayout11.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                        tv_emaillayout11.setText(Admin.tinyDB.getString("number_one"));
                        iv_emaillayout12.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                        tv_emaillayout12.setText(Admin.tinyDB.getString("number_one"));
                        iv_emaillayout13.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                        tv_emaillayout13.setText(Admin.tinyDB.getString("number_one"));
                    } else {
                        ll_f11_email.setVisibility(View.INVISIBLE);
                        ll_f12_email.setVisibility(View.INVISIBLE);
                        ll_f13_email.setVisibility(View.INVISIBLE);

                    }
                    ll_f11_call.setVisibility(View.INVISIBLE);
                    ll_f12_call.setVisibility(View.INVISIBLE);
                    ll_f13_call.setVisibility(View.INVISIBLE);
                } else {
                    ll_f11_email.setVisibility(View.INVISIBLE);
                    ll_f12_email.setVisibility(View.INVISIBLE);
                    ll_f13_email.setVisibility(View.INVISIBLE);

                    ll_f11_web.setVisibility(View.INVISIBLE);
                    ll_f12_web.setVisibility(View.INVISIBLE);
                    ll_f13_web.setVisibility(View.INVISIBLE);

                    ll_f11_call.setVisibility(View.INVISIBLE);
                    ll_f12_call.setVisibility(View.INVISIBLE);
                    ll_f13_call.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void removeWeb(Boolean webLay) {
        isweb = webLay;
        if (webLay) {
            ll_f1_web.setVisibility(View.VISIBLE);
            ll_f2_web.setVisibility(View.VISIBLE);
            ll_f3_web.setVisibility(View.VISIBLE);
            ll_f4_web.setVisibility(View.VISIBLE);
            ll_f5_web.setVisibility(View.VISIBLE);
            ll_f6_web.setVisibility(View.VISIBLE);
//            ll_f7_web.setVisibility(View.VISIBLE);
            ll_f8_web.setVisibility(View.VISIBLE);
            ll_f9_web.setVisibility(View.VISIBLE);
            ll_f10_web.setVisibility(View.VISIBLE);
            ll_f11_web.setVisibility(View.VISIBLE);
            ll_f12_web.setVisibility(View.VISIBLE);
            ll_f13_web.setVisibility(View.VISIBLE);

            iv_weblayout11.setImageDrawable(getResources().getDrawable(R.drawable.web));
            tv_websitelayout11.setText(Admin.tinyDB.getString("website"));
            iv_weblayout12.setImageDrawable(getResources().getDrawable(R.drawable.web));
            tv_websitelayout12.setText(Admin.tinyDB.getString("website"));
            iv_weblayout13.setImageDrawable(getResources().getDrawable(R.drawable.web));
            tv_websitelayout13.setText(Admin.tinyDB.getString("website"));

            if (ismail) {
                if (ismobile) {
                    ll_f11_call.setVisibility(View.VISIBLE);
                    ll_f12_call.setVisibility(View.VISIBLE);
                    ll_f13_call.setVisibility(View.VISIBLE);
                } else {
                    ll_f11_call.setVisibility(View.INVISIBLE);
                    ll_f12_call.setVisibility(View.INVISIBLE);
                    ll_f13_call.setVisibility(View.INVISIBLE);
                }

            } else {
                if (ismobile) {
                    ll_f11_call.setVisibility(View.INVISIBLE);
                    ll_f12_call.setVisibility(View.INVISIBLE);
                    ll_f13_call.setVisibility(View.INVISIBLE);

                    ll_f11_email.setVisibility(View.VISIBLE);
                    ll_f12_email.setVisibility(View.VISIBLE);
                    ll_f13_email.setVisibility(View.VISIBLE);

                    iv_emaillayout11.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_emaillayout11.setText(Admin.tinyDB.getString("number_one"));

                    iv_emaillayout12.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_emaillayout12.setText(Admin.tinyDB.getString("number_one"));

                    iv_emaillayout13.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_emaillayout13.setText(Admin.tinyDB.getString("number_one"));

                } else {
                    ll_f11_call.setVisibility(View.INVISIBLE);
                    ll_f12_call.setVisibility(View.INVISIBLE);
                    ll_f13_call.setVisibility(View.INVISIBLE);

                    ll_f11_email.setVisibility(View.INVISIBLE);
                    ll_f12_email.setVisibility(View.INVISIBLE);
                    ll_f13_email.setVisibility(View.INVISIBLE);
                }
            }

            if (ll_f2_location.getVisibility() == View.VISIBLE || ll_f2_web.getVisibility() == View.VISIBLE) {
                ll_f2_bottom_bg.setVisibility(View.VISIBLE);
            } else {
                ll_f2_bottom_bg.setVisibility(View.GONE);
            }
            if (ll_f4_location.getVisibility() == View.VISIBLE || ll_f4_web.getVisibility() == View.VISIBLE) {
                ll_f4_bottom_bg.setVisibility(View.VISIBLE);
            } else {
                ll_f4_bottom_bg.setVisibility(View.GONE);
            }
            if (ll_f6_location.getVisibility() == View.VISIBLE || ll_f6_web.getVisibility() == View.VISIBLE) {
                ll_f6_bottom_bg.setVisibility(View.VISIBLE);
            } else {
                ll_f6_bottom_bg.setVisibility(View.GONE);
            }
        } else {
            ll_f1_web.setVisibility(View.GONE);
            ll_f2_web.setVisibility(View.GONE);
            ll_f3_web.setVisibility(View.GONE);
            ll_f4_web.setVisibility(View.GONE);
            ll_f6_web.setVisibility(View.GONE);
//            ll_f7_web.setVisibility(View.GONE);
            ll_f5_web.setVisibility(View.INVISIBLE);
            ll_f8_web.setVisibility(View.GONE);
            ll_f9_web.setVisibility(View.GONE);
            ll_f10_web.setVisibility(View.GONE);

            if (ll_f2_location.getVisibility() == View.GONE && ll_f2_web.getVisibility() == View.GONE) {
                ll_f2_bottom_bg.setVisibility(View.GONE);
            } else {
                ll_f2_bottom_bg.setVisibility(View.VISIBLE);
            }

            if (ll_f4_location.getVisibility() == View.GONE && ll_f4_web.getVisibility() == View.GONE) {
                ll_f4_bottom_bg.setVisibility(View.GONE);
            } else {
                ll_f4_bottom_bg.setVisibility(View.VISIBLE);
            }

            if (ll_f6_location.getVisibility() == View.GONE && ll_f6_web.getVisibility() == View.GONE) {
                ll_f6_bottom_bg.setVisibility(View.GONE);
            } else {
                ll_f6_bottom_bg.setVisibility(View.VISIBLE);
            }

            if (ismail) {
                if (ismobile) {
                    ll_f11_web.setVisibility(View.VISIBLE);
                    ll_f12_web.setVisibility(View.VISIBLE);
                    ll_f13_web.setVisibility(View.VISIBLE);
                    iv_weblayout11.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_websitelayout11.setText(Admin.tinyDB.getString("number_one"));

                    iv_weblayout12.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_websitelayout12.setText(Admin.tinyDB.getString("number_one"));

                    iv_weblayout13.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                    tv_websitelayout13.setText(Admin.tinyDB.getString("number_one"));

                } else {
                    ll_f11_web.setVisibility(View.INVISIBLE);
                    ll_f12_web.setVisibility(View.INVISIBLE);
                    ll_f13_web.setVisibility(View.INVISIBLE);
                }

                ll_f11_call.setVisibility(View.INVISIBLE);
                ll_f12_call.setVisibility(View.INVISIBLE);
                ll_f13_call.setVisibility(View.INVISIBLE);
            } else {
                ll_f11_web.setVisibility(View.INVISIBLE);
                ll_f12_web.setVisibility(View.INVISIBLE);
                ll_f13_web.setVisibility(View.INVISIBLE);

                ll_f11_email.setVisibility(View.INVISIBLE);
                ll_f12_email.setVisibility(View.INVISIBLE);
                ll_f13_email.setVisibility(View.INVISIBLE);

                if (ismobile) {
                    ll_f11_call.setVisibility(View.VISIBLE);
                    ll_f12_call.setVisibility(View.VISIBLE);
                    ll_f13_call.setVisibility(View.VISIBLE);
                } else {
                    ll_f11_call.setVisibility(View.INVISIBLE);
                    ll_f12_call.setVisibility(View.INVISIBLE);
                    ll_f13_call.setVisibility(View.INVISIBLE);
                }

            }
        }
    }

    public void removeName(Boolean namelay) {
        if (namelay) {
            tv_f1_name.setVisibility(View.VISIBLE);
            tv_f2_name.setVisibility(View.VISIBLE);
            tv_f3_name.setVisibility(View.VISIBLE);
            tv_f4_name.setVisibility(View.VISIBLE);
            tv_f5_name.setVisibility(View.VISIBLE);
            tv_f6_name.setVisibility(View.VISIBLE);
            tv_f7_name.setVisibility(View.VISIBLE);
            tv_f8_name.setVisibility(View.VISIBLE);
            tv_f9_name.setVisibility(View.VISIBLE);
            tv_f10_name.setVisibility(View.VISIBLE);
            tv_f11_name.setVisibility(View.VISIBLE);
            tv_f12_name.setVisibility(View.VISIBLE);
            tv_f13_name.setVisibility(View.VISIBLE);
        } else {
            tv_f1_name.setVisibility(View.GONE);
            tv_f2_name.setVisibility(View.GONE);
            tv_f3_name.setVisibility(View.GONE);
            tv_f4_name.setVisibility(View.GONE);
            tv_f5_name.setVisibility(View.GONE);
            tv_f6_name.setVisibility(View.GONE);
            tv_f7_name.setVisibility(View.GONE);
            tv_f8_name.setVisibility(View.GONE);
            tv_f9_name.setVisibility(View.GONE);
            tv_f10_name.setVisibility(View.GONE);
            tv_f11_name.setVisibility(View.GONE);
            tv_f12_name.setVisibility(View.GONE);
            tv_f13_name.setVisibility(View.GONE);

        }
    }

    public void removeLoct(Boolean Loctlay) {
        if (Loctlay) {
            ll_f1_location.setVisibility(View.VISIBLE);
            ll_f2_location.setVisibility(View.VISIBLE);
            ll_f3_location.setVisibility(View.VISIBLE);
            ll_f4_location.setVisibility(View.VISIBLE);
            ll_f5_location.setVisibility(View.VISIBLE);
            ll_f6_location.setVisibility(View.VISIBLE);
            ll_f7_location.setVisibility(View.VISIBLE);
            ll_f8_location.setVisibility(View.VISIBLE);
            ll_f9_location.setVisibility(View.VISIBLE);
            ll_f10_location.setVisibility(View.VISIBLE);
            ll_f11_location.setVisibility(View.VISIBLE);
            ll_f12_location.setVisibility(View.VISIBLE);
            ll_f13_location.setVisibility(View.VISIBLE);
            if (ll_f6_location.getVisibility() == View.VISIBLE || ll_f6_web.getVisibility() == View.VISIBLE) {
                ll_f6_bottom_bg.setVisibility(View.VISIBLE);
            } else {
                ll_f6_bottom_bg.setVisibility(View.GONE);
            }
            if (ll_f2_location.getVisibility() == View.VISIBLE || ll_f2_web.getVisibility() == View.VISIBLE) {
                ll_f2_bottom_bg.setVisibility(View.VISIBLE);
            } else {
                ll_f2_bottom_bg.setVisibility(View.GONE);
            }
            if (ll_f4_location.getVisibility() == View.VISIBLE || ll_f4_web.getVisibility() == View.VISIBLE) {
                ll_f4_bottom_bg.setVisibility(View.VISIBLE);
            } else {
                ll_f4_bottom_bg.setVisibility(View.GONE);
            }
        } else {
            ll_f1_location.setVisibility(View.GONE);
            ll_f2_location.setVisibility(View.GONE);
            ll_f3_location.setVisibility(View.GONE);
            ll_f4_location.setVisibility(View.GONE);
            ll_f5_location.setVisibility(View.GONE);
            ll_f6_location.setVisibility(View.GONE);
            ll_f7_location.setVisibility(View.GONE);
            ll_f8_location.setVisibility(View.GONE);
            ll_f9_location.setVisibility(View.GONE);
            ll_f10_location.setVisibility(View.GONE);
            ll_f11_location.setVisibility(View.GONE);
            ll_f12_location.setVisibility(View.GONE);
            ll_f13_location.setVisibility(View.GONE);
            if (ll_f6_location.getVisibility() == View.GONE && ll_f6_web.getVisibility() == View.GONE) {
                ll_f6_bottom_bg.setVisibility(View.GONE);
            } else {
                ll_f6_bottom_bg.setVisibility(View.VISIBLE);
            }
//            ll_f6_bottom_bg
            if (ll_f2_location.getVisibility() == View.GONE && ll_f2_web.getVisibility() == View.GONE) {
                ll_f2_bottom_bg.setVisibility(View.GONE);
            } else {
                ll_f2_bottom_bg.setVisibility(View.VISIBLE);
            }
            if (ll_f4_location.getVisibility() == View.GONE && ll_f4_web.getVisibility() == View.GONE) {
                ll_f4_bottom_bg.setVisibility(View.GONE);
            } else {
                ll_f4_bottom_bg.setVisibility(View.VISIBLE);
            }
        }
    }

    public void textStickerEditPopUp() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        final View customView = inflater.inflate(R.layout.edit_text_sticker_popup, null);

        mPopupWindowpw = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

//
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindowpw.setElevation(5.0f);
        }
        mPopupWindowpw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mPopupWindowpw.setOutsideTouchable(true);
        mPopupWindowpw.showAtLocation(ll_main, Gravity.BOTTOM, 0, 0);


        final TextView dialogTitle = (TextView) customView.findViewById(R.id.cp_accent_title);
        final LinearLayout ll_text_color = (LinearLayout) customView.findViewById(R.id.ll_text_color);
        final LinearLayout ll_text_style = (LinearLayout) customView.findViewById(R.id.ll_text_style);
        final Button btn_Ok = (Button) customView.findViewById(R.id.btn_Ok);


        ll_text_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textStickerColorPopUp();
            }
        });

        ll_text_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFontList();
            }
        });
        dialogTitle.setText("Edit PF_Sticker");

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_text_sticker.getText().toString().equals("") || et_text_sticker.getText().toString().equals(null)) {
                    Toast.makeText(context, "Please enter pf_text", Toast.LENGTH_LONG).show();
                    mPopupWindowpw.dismiss();

                } else {
                    et_text_sticker.setVisibility(View.GONE);
                    txtsticker = new TextSticker(context);
                    txtsticker.setText("");
                    et_text_sticker.post(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(et_text_sticker.getWindowToken(), 0);
                        }
                    });
                    // Toast.makeText(context, et_text_sticker.getText().toString(), Toast.LENGTH_LONG).show();
                    txtsticker.setText(et_text_sticker.getText().toString());
                    txtsticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + Constance.FontStyle));
                    txtsticker.setTextColor(textcolor);
                    textStickerColor = R.color.colorBlack;
                    txtsticker.resizeText();
                    sticker_view.addSticker(txtsticker);
                    mPopupWindowpw.dismiss();
                }
            }
        });

    }

    public void openFontList() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        View customView = inflater.inflate(R.layout.dialog_fontstyle, null);

        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(true);

        RecyclerView rvList = customView.findViewById(R.id.rv_font_style);
        Button btnOk = customView.findViewById(R.id.btnOk);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvList.setLayoutManager(linearLayoutManager);

        AdapterFontList adapterFontList = new AdapterFontList(context, getfontList(), "greetingstyle");
        rvList.setAdapter(adapterFontList);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();

            }
        });

        mPopupWindow.showAtLocation(ll_main, Gravity.BOTTOM, 0, 0);

    }

    public void setFontStyle(String fontName) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);

        et_text_sticker.setTypeface(font);
    }

    public void textStickerColorPopUp() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View customView = inflater.inflate(R.layout.select_color_popup, null);

        PaletteBar PFPaletteBar = customView.findViewById(R.id.paletteBar);
        PFPaletteBar.setListener(new PaletteBar.PaletteBarListener() {
            @Override
            public void onColorSelected(int color) {
                et_text_sticker.setTextColor(color);
                textcolor = color;

            }
        });

        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(true);


        final TextView dialogTitle = (TextView) customView.findViewById(R.id.cp_accent_title);
        final Button btnOk = (Button) customView.findViewById(R.id.btnOk);
        final Button btnCancel = (Button) customView.findViewById(R.id.btnCancel);
/*
        final RecyclerView rvList = (RecyclerView) customView.findViewById(R.id.rvList);
*/
        dialogTitle.setText("Text Color");

      /*  GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 7);
        rvList.setLayoutManager(linearLayoutManager);*/


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();

            }
        });


        mPopupWindow.showAtLocation(ll_main, Gravity.BOTTOM, 0, 0);
    }

    public void setBorder() {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.border_size_popup, null);

        ImageView plusbtn, minusbtn;
        plusbtn = customView.findViewById(R.id.ivPlus);
        minusbtn = customView.findViewById(R.id.ivMinus);
        EditText etcount = customView.findViewById(R.id.etBordersize);
        PaletteBar PFPaletteBar = customView.findViewById(R.id.paletteBar);
        final LinearLayout ll_selectborder = customView.findViewById(R.id.ll_selectborder);


        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }


        PFPaletteBar.setListener(new PaletteBar.PaletteBarListener() {
            @Override
            public void onColorSelected(int color) {
                ll_border.setBackgroundColor(color);

            }
        });

        etcount.setCursorVisible(false);


        plusbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean iscolor = true;


                if (iscolor) {


                    ll_selectborder.setBackgroundColor(Color.BLUE);
                    count++;

                    if (count <= 25) {


                        ll_selectborder.setBackgroundColor(Color.parseColor("#000000"));
                        etcount.setText(String.valueOf(count));
                        int margin = Integer.parseInt(etcount.getText().toString());
                        RelativeLayout.LayoutParams parameter = (RelativeLayout.LayoutParams) rl_createquote.getLayoutParams();
                        parameter.setMargins(margin, margin, margin, margin); // left, top, right, bottom
                        rl_createquote.setLayoutParams(parameter);
                        abc = etcount.getText().toString();


                    } else {
                        count = 25;
                        ll_selectborder.setBackgroundColor(Color.parseColor("#000000"));

                    }
                }


            }
        });

        minusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean iscolor = true;

                if (iscolor) {
                    ll_selectborder.setBackgroundColor(Color.BLUE);
                    count--;
                    if (count >= 0) {

                        ll_selectborder.setBackgroundColor(Color.parseColor("#000000"));
                        etcount.setText(String.valueOf(count));
                        int margin = Integer.parseInt(etcount.getText().toString());
                        RelativeLayout.LayoutParams parameter = (RelativeLayout.LayoutParams) rl_createquote.getLayoutParams();
                        parameter.setMargins(margin, margin, margin, margin); // left, top, right, bottom
                        rl_createquote.setLayoutParams(parameter);
                        abc = etcount.getText().toString();

                    } else {
                        count = 0;
                        ll_selectborder.setBackgroundColor(Color.parseColor("#000000"));
                    }

                }


            }
        });
        etcount.setText(abc);

        mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.showAtLocation(ll_main, Gravity.BOTTOM, 0, 0);
    }

    public void openStickers() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.sticker_pop_up, null);

        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }

        mPopupWindow.setOutsideTouchable(true);


        RecyclerView rvList = customView.findViewById(R.id.rvList);


        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        StickersListAdapter stickersListAdapter = new StickersListAdapter(context, stickerlist, "popup");
        rvList.setAdapter(stickersListAdapter);


        mPopupWindow.showAtLocation(ll_main, Gravity.BOTTOM, 0, 0);
    }

    public void setEmoji(int position) {

        sticker_view.addSticker(new DrawableSticker(getResources().getDrawable(stickerlist[position].getImgId())));
        mPopupWindow.dismiss();
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void viewTransformation(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xCoOrdinate = view.getX() - event.getRawX();
                yCoOrdinate = view.getY() - event.getRawY();

                start.set(event.getX(), event.getY());
                isOutSide = false;
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
                isZoomAndRotate = false;
                if (mode == DRAG) {
                    float x = event.getX();
                    float y = event.getY();
                }
            case MotionEvent.ACTION_OUTSIDE:
                isOutSide = true;
                mode = NONE;
                lastEvent = null;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOutSide) {
                    if (mode == DRAG) {
                        isZoomAndRotate = false;
                        view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    }
                    if (mode == ZOOM && event.getPointerCount() == 2) {
                        float newDist1 = spacing(event);
                        if (newDist1 > 10f) {
                            float scale = newDist1 / oldDist * view.getScaleX();
                            view.setScaleX(scale);
                            view.setScaleY(scale);
                        }
                        if (lastEvent != null) {
                            newRot = rotation(event);
                            view.setRotation((float) (view.getRotation() + (newRot - d)));
                        }
                    }
                }
                break;
        }
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // Method (Photo Rotation)...................
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(ActivityCreatePost.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        } else {

        }
    }

    public void bindView() {
        mAdView = findViewById(R.id.adview);
        facbook_ad_banner = findViewById(R.id.facbook_ad_banner);
        arrayList = new ArrayList<>();
        //   rv_cusomFrame = findViewById(R.id.rv_cusomFrame);
        iv_customimage = findViewById(R.id.iv_customimage);
        iv_cp_pic = findViewById(R.id.iv_cp_pic);
        // et_usertext = findViewById(R.id.et_usertext);
        //et_usertext.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sticker_view = findViewById(R.id.sticker_view);
        et_text_sticker = findViewById(R.id.et_text_sticker);
        et_text_sticker.setImeOptions(EditorInfo.IME_ACTION_DONE);
        // ll_mainlayout = findViewById(R.id.ll_mainlayout);
        rl_content = findViewById(R.id.rl_content);
        opacitybg = findViewById(R.id.opacitybg);
        rv_framelist = findViewById(R.id.rv_framelist);
        rl_video = findViewById(R.id.rl_video);
        vv_videoshow = findViewById(R.id.vv_videoshow);
        iv_vp_play = findViewById(R.id.iv_vp_play);
        pb_sp = findViewById(R.id.pb_sp);
        ll_border = findViewById(R.id.ll_border);
        ll_addimage = findViewById(R.id.ll_addimage);
        ll_camera_pic = findViewById(R.id.ll_camera_pic);
        ll_flip = findViewById(R.id.ll_flip);
        ll_main = findViewById(R.id.ll_main);
        ll_sticker = findViewById(R.id.ll_sticker);
        ll_text_sticker = findViewById(R.id.ll_text_sticker);
        ll_set_boder = findViewById(R.id.ll_set_boder);
        rl_createquote = findViewById(R.id.rl_createquote);

        layout_custom_frame1 = findViewById(R.id.layout_custom_frame1);
        layout_custom_frame2 = findViewById(R.id.layout_custom_frame2);
        layout_custom_frame3 = findViewById(R.id.layout_custom_frame3);
        layout_custom_frame4 = findViewById(R.id.layout_custom_frame4);
        layout_custom_frame5 = findViewById(R.id.layout_custom_frame5);
        layout_custom_frame6 = findViewById(R.id.layout_custom_frame6);
        layout_custom_frame7 = findViewById(R.id.layout_custom_frame7);
        layout_custom_frame8 = findViewById(R.id.layout_custom_frame8);
        layout_custom_frame9 = findViewById(R.id.layout_custom_frame9);
        layout_custom_frame10 = findViewById(R.id.layout_custom_frame10);
        layout_custom_frame11 = findViewById(R.id.layout_custom_frame11);
        layout_custom_frame12 = findViewById(R.id.layout_custom_frame12);
        layout_custom_frame13 = findViewById(R.id.layout_custom_frame13);


        tv_mobilenumberlayout1 = findViewById(R.id.tv_mobilenumberlayout1);
        tv_mobilenumberlayout2 = findViewById(R.id.tv_mobilenumberlayout2);
        tv_mobilenumberlayout3 = findViewById(R.id.tv_mobilenumberlayout3);
        tv_mobilenumberlayout4 = findViewById(R.id.tv_mobilenumberlayout4);
        tv_mobilenumberlayout5 = findViewById(R.id.tv_mobilenumberlayout5);
        tv_mobilenumberlayout6 = findViewById(R.id.tv_mobilenumberlayout6);

        tv_websitelayout1 = findViewById(R.id.tv_websitelayout1);
        tv_websitelayout2 = findViewById(R.id.tv_websitelayout2);
        tv_websitelayout3 = findViewById(R.id.tv_websitelayout3);
        tv_websitelayout4 = findViewById(R.id.tv_websitelayout4);
        tv_websitelayout5 = findViewById(R.id.tv_websitelayout5);
        tv_websitelayout6 = findViewById(R.id.tv_websitelayout6);
//        tv_websitelayout7 = findViewById(R.id.tv_websitelayout7);
        tv_websitelayout8 = findViewById(R.id.tv_websitelayout8);
        tv_websitelayout9 = findViewById(R.id.tv_websitelayout9);
        tv_websitelayout10 = findViewById(R.id.tv_websitelayout10);
        tv_websitelayout11 = findViewById(R.id.tv_websitelayout11);
        tv_websitelayout12 = findViewById(R.id.tv_websitelayout12);
        tv_websitelayout13 = findViewById(R.id.tv_websitelayout13);

        tv_emaillayout1 = findViewById(R.id.tv_emaillayout1);
        tv_emaillayout2 = findViewById(R.id.tv_emaillayout2);
        tv_emaillayout3 = findViewById(R.id.tv_emaillayout3);
        tv_emaillayout4 = findViewById(R.id.tv_emaillayout4);
        tv_emaillayout5 = findViewById(R.id.tv_emaillayout5);
        tv_emaillayout6 = findViewById(R.id.tv_emaillayout6);
//        tv_emaillayout7 = findViewById(R.id.tv_emaillayout7);
        tv_emaillayout8 = findViewById(R.id.tv_emaillayout8);
        tv_emaillayout9 = findViewById(R.id.tv_emaillayout9);
        tv_emaillayout10 = findViewById(R.id.tv_emaillayout10);
        tv_emaillayout11 = findViewById(R.id.tv_emaillayout11);
        tv_emaillayout12 = findViewById(R.id.tv_emaillayout12);
        tv_emaillayout13 = findViewById(R.id.tv_emaillayout13);

        iv_calllayout1 = findViewById(R.id.iv_calllayout1);
        iv_calllayout2 = findViewById(R.id.iv_calllayout2);
        iv_calllayout3 = findViewById(R.id.iv_calllayout3);
        iv_calllayout4 = findViewById(R.id.iv_calllayout4);
        iv_calllayout5 = findViewById(R.id.iv_calllayout5);
        iv_calllayout6 = findViewById(R.id.iv_calllayout6);
        tv_calllayout7 = findViewById(R.id.tv_calllayout7);
        tv_calllayout8 = findViewById(R.id.tv_calllayout8);
        tv_calllayout9 = findViewById(R.id.tv_calllayout9);
        tv_calllayout10 = findViewById(R.id.tv_calllayout10);
        tv_calllayout11 = findViewById(R.id.tv_calllayout11);
        tv_calllayout12 = findViewById(R.id.tv_calllayout12);
        tv_calllayout13 = findViewById(R.id.tv_calllayout13);

        iv_weblayout1 = findViewById(R.id.iv_weblayout1);
        iv_weblayout2 = findViewById(R.id.iv_weblayout2);
        iv_weblayout3 = findViewById(R.id.iv_weblayout3);
        iv_weblayout4 = findViewById(R.id.iv_weblayout4);
        iv_weblayout5 = findViewById(R.id.iv_weblayout5);
        iv_weblayout6 = findViewById(R.id.iv_weblayout6);
//        iv_weblayout7 = findViewById(R.id.iv_weblayout7);
        iv_weblayout8 = findViewById(R.id.iv_weblayout8);
        iv_weblayout11 = findViewById(R.id.iv_weblayout11);
        iv_weblayout12 = findViewById(R.id.iv_weblayout12);
        iv_weblayout13 = findViewById(R.id.iv_weblayout13);

        iv_emaillayout1 = findViewById(R.id.iv_emaillayout1);
        iv_emaillayout2 = findViewById(R.id.iv_emaillayout2);
        iv_emaillayout3 = findViewById(R.id.iv_emaillayout3);
        iv_emaillayout4 = findViewById(R.id.iv_emaillayout4);
        iv_emaillayout5 = findViewById(R.id.iv_emaillayout5);
        iv_emaillayout6 = findViewById(R.id.iv_emaillayout6);
        iv_emaillayout8 = findViewById(R.id.iv_emaillayout8);
        iv_emaillayout9 = findViewById(R.id.iv_emaillayout9);
        iv_emaillayout10 = findViewById(R.id.iv_emaillayout10);
        iv_emaillayout11 = findViewById(R.id.iv_emaillayout11);
        iv_emaillayout12 = findViewById(R.id.iv_emaillayout12);
        iv_emaillayout13 = findViewById(R.id.iv_emaillayout13);

     /*   tv_addresslayout4 = findViewById(R.id.tv_addresslayout4);
        iv_addresslayout4 = findViewById(R.id.iv_addresslayout4);*/

        iv_logolayout1 = findViewById(R.id.iv_logolayout1);
        iv_logolayout2 = findViewById(R.id.iv_logolayout2);
        iv_logolayout3 = findViewById(R.id.iv_logolayout3);
        iv_logolayout4 = findViewById(R.id.iv_logolayout4);
        iv_logolayout5 = findViewById(R.id.iv_logolayout5);
        iv_logolayout6 = findViewById(R.id.iv_logolayout6);
        iv_logolayout7 = findViewById(R.id.iv_logolayout7);
        iv_logolayout8 = findViewById(R.id.iv_logolayout8);
        iv_logolayout9 = findViewById(R.id.iv_logolayout9);
        iv_logolayout10 = findViewById(R.id.iv_logolayout10);
        iv_logolayout11 = findViewById(R.id.iv_logolayout11);
        iv_logolayout12 = findViewById(R.id.iv_logolayout12);
        iv_logolayout13 = findViewById(R.id.iv_logolayout13);


        tv_remove_logo = findViewById(R.id.tv_remove_logo);
        tv_remove_email = findViewById(R.id.tv_remove_email);
        tv_remove_call = findViewById(R.id.tv_remove_call);
        tv_remove_web = findViewById(R.id.tv_remove_web);
        tv_remove_name = findViewById(R.id.tv_remove_name);
        tv_remove_location = findViewById(R.id.tv_remove_location);


        ll_f1_call = findViewById(R.id.ll_f1_call);
        ll_f1_email = findViewById(R.id.ll_f1_email);
        ll_f1_web = findViewById(R.id.ll_f1_web);
        ll_f1_location = findViewById(R.id.ll_f1_location);
        tv_f1_name = findViewById(R.id.tv_f1_name);
        tv_locationlayout1 = findViewById(R.id.tv_locationlayout1);

        ll_f2_call = findViewById(R.id.ll_f2_call);
        ll_f2_email = findViewById(R.id.ll_f2_email);
        ll_f2_web = findViewById(R.id.ll_f2_web);
        ll_f2_location = findViewById(R.id.ll_f2_location);
        tv_f2_name = findViewById(R.id.tv_f2_name);
        tv_locationlayout2 = findViewById(R.id.tv_locationlayout2);
        ll_f2_bottom_bg = findViewById(R.id.ll_f2_bottom_bg);

        ll_f3_call = findViewById(R.id.ll_f3_call);
        ll_f3_email = findViewById(R.id.ll_f3_email);
        ll_f3_web = findViewById(R.id.ll_f3_web);
        ll_f3_location = findViewById(R.id.ll_f3_location);
        tv_f3_name = findViewById(R.id.tv_f3_name);
        tv_locationlayout3 = findViewById(R.id.tv_locationlayout3);


        ll_f4_call = findViewById(R.id.ll_f4_call);
        ll_f4_email = findViewById(R.id.ll_f4_email);
        ll_f4_web = findViewById(R.id.ll_f4_web);
        ll_f4_location = findViewById(R.id.ll_f4_location);
        tv_f4_name = findViewById(R.id.tv_f4_name);
        tv_locationlayout4 = findViewById(R.id.tv_locationlayout4);
        ll_f4_bottom_bg = findViewById(R.id.ll_f4_bottom_bg);

        ll_f5_call = findViewById(R.id.ll_f5_call);
        ll_f5_email = findViewById(R.id.ll_f5_email);
        ll_f5_web = findViewById(R.id.ll_f5_web);
        ll_f5_location = findViewById(R.id.ll_f5_location);
        tv_f5_name = findViewById(R.id.tv_f5_name);
        tv_locationlayout5 = findViewById(R.id.tv_locationlayout5);

        ll_f6_call = findViewById(R.id.ll_f6_call);
        ll_f6_email = findViewById(R.id.ll_f6_email);
        ll_f6_web = findViewById(R.id.ll_f6_web);
        ll_f6_location = findViewById(R.id.ll_f6_location);
        tv_f6_name = findViewById(R.id.tv_f6_name);
        tv_locationlayout6 = findViewById(R.id.tv_locationlayout6);
        ll_f6_bottom_bg = findViewById(R.id.ll_f6_bottom_bg);

        ll_f7_call = findViewById(R.id.ll_f7_call);
//        ll_f7_web = findViewById(R.id.ll_f7_web);
//        ll_f7_email = findViewById(R.id.ll_f7_email);
        ll_f7_location = findViewById(R.id.ll_f7_location);
        tv_f7_name = findViewById(R.id.tv_f7_name);
        tv_locationlayout7 = findViewById(R.id.tv_locationlayout7);

        ll_f8_call = findViewById(R.id.ll_f8_call);
        ll_f8_email = findViewById(R.id.ll_f8_email);
        ll_f8_web = findViewById(R.id.ll_f8_web);
        ll_f8_location = findViewById(R.id.ll_f8_location);
        tv_f8_name = findViewById(R.id.tv_f8_name);
        tv_locationlayout8 = findViewById(R.id.tv_locationlayout8);

        tv_f9_name = findViewById(R.id.tv_f9_name);
        ll_f9_call = findViewById(R.id.ll_f9_call);
        ll_f9_web = findViewById(R.id.ll_f9_web);
        ll_f9_email = findViewById(R.id.ll_f9_email);
        ll_f9_location = findViewById(R.id.ll_f9_location);
        tv_locationlayout9 = findViewById(R.id.tv_locationlayout9);

        tv_f10_name = findViewById(R.id.tv_f10_name);
        ll_f10_call = findViewById(R.id.ll_f10_call);
        ll_f10_web = findViewById(R.id.ll_f10_web);
        ll_f10_email = findViewById(R.id.ll_f10_email);
        ll_f10_location = findViewById(R.id.ll_f10_location);
        tv_locationlayout10 = findViewById(R.id.tv_locationlayout10);

        tv_f11_name = findViewById(R.id.tv_f11_name);
        ll_f11_call = findViewById(R.id.ll_f11_call);
        ll_f11_web = findViewById(R.id.ll_f11_web);
        ll_f11_email = findViewById(R.id.ll_f11_email);
        ll_f11_location = findViewById(R.id.ll_f11_location);
        tv_locationlayout11 = findViewById(R.id.tv_locationlayout11);

        tv_f12_name = findViewById(R.id.tv_f12_name);
        ll_f12_call = findViewById(R.id.ll_f12_call);
        ll_f12_web = findViewById(R.id.ll_f12_web);
        ll_f12_email = findViewById(R.id.ll_f12_email);
        ll_f12_location = findViewById(R.id.ll_f12_location);
        tv_locationlayout12 = findViewById(R.id.tv_locationlayout12);

        tv_f13_name = findViewById(R.id.tv_f13_name);
        ll_f13_call = findViewById(R.id.ll_f13_call);
        ll_f13_web = findViewById(R.id.ll_f13_web);
        ll_f13_email = findViewById(R.id.ll_f13_email);
        ll_f13_location = findViewById(R.id.ll_f13_location);
        tv_locationlayout13 = findViewById(R.id.tv_locationlayout13);

    }

    public void playVideo() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        pDialog.dismiss();

      /*  try {
            mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(vv_playvideo);
            vv_playvideo.setMediaController(mediacontroller);
            vv_playvideo.setVideoPath(videoCategoriesDataArrayList.get(0).getVideo_url());

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        if (FromSinglecatActivity != null) {
            vv_videoshow.setVideoPath(FromSinglecatActivity);
        } else {
            Toast.makeText(context, "Not get the video path", Toast.LENGTH_LONG).show();
        }
        mediacontroller = new MediaController(context);
        mediacontroller.setMediaPlayer(vv_videoshow);
        vv_videoshow.setMediaController(mediacontroller);
        vv_videoshow.requestFocus();
        vv_videoshow.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                //vv_playvideo.start();
            }
        });
        vv_videoshow.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                    mp.stop();
                    iv_vp_play.setVisibility(View.VISIBLE);

                }
                //mp.stop();
                vv_videoshow.pause();
                iv_vp_play.setVisibility(View.VISIBLE);

            }
        });

        //  vv_playvideo.start();

    }

    public void onclickCustomFrame(View view) {
        switch (view.getId()) {
            case R.id.iv_backarrow:
                onBackPressed();
                break;
            case R.id.ll_next:
                if (ll_f1_call.getVisibility() == View.GONE && tv_f1_name.getVisibility() == View.GONE &&
                        iv_logolayout1.getVisibility() == View.GONE && ll_f1_email.getVisibility() == View.GONE &&
                        ll_f1_web.getVisibility() == View.GONE && ll_f1_location.getVisibility() == View.GONE) {
                    allViewShow = false;
                } else {
                    allViewShow = true;
                }

                if (allViewShow) {
                    if (!comeFrom.equals("image")) {
                        iv_customimage.setVisibility(View.GONE);
                        iv_vp_play.setVisibility(View.GONE);

                    } else {
                        rl_video.setVisibility(View.GONE);
                        sticker_view.hideIcons(true);
                        sticker_view.setLocked(true);
                    }

                    sticker_view.hideIcons(true);
                    bitmapsave = viewToBitmap(rl_content);

                    if (!Constance.FileSaveVideoDirectory.exists()) {
                        Constance.FileSaveVideoDirectory.mkdir();
                    }

                    Constance.createdBitmap = scaleBitmap(bitmapsave, 1080, 1080);
                    // File file = new File(Constance.FileSaveVideoDirectory, "demoImage.png");
                  /*  String timeImg = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    try {
                        Uri uri1=saveImage(context,Constance.createdBitmap,Constance.FolderName,timeImg+".png");
                        Constance.savedImagePath = PathUtills.getPath(ActivityCreatePost.this, uri1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }*/

               /* if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    Constance.createdBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                    //Constance.createdBitmap = bitmapsave;
                    //Constance.savedImagePath = file.getPath();
                    if (comeFrom.equals("image")) {
                        //pb_sp.setVisibility(View.GONE);
                        Intent save = new Intent(context, ActivityPreview.class);
                        save.putExtra("name", "image");
                        startActivity(save);

                    } else {
                        Constance.createdBitmap = scaleBitmap(bitmapsave, 1080, 1080);
                        Log.d("dhfshsdf", "dfs" + Constance.createdBitmap);
                   /* pDialog1 = new ProgressDialog(this);
                    pDialog1.setMessage("Video is Creating...");
                    pDialog1.setIndeterminate(false);
                    pDialog1.setCancelable(false);
                    pDialog1.show();*/
                        // String fileName = URLUtil.guessFileName(item.getVideo_link(), "", "video/*");
                        String filename = String.valueOf(Calendar.getInstance().getTimeInMillis());
                        filename = "/" + filename + ".mp4";
                        String location = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + Constance.FolderName + filename;
//                        String location = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) "/" + "VideoDemo.mp4";
                        Constance.savedVideoPath = location;
                        if (new File(location).exists()) {
                            Toast.makeText(context, "Already Downloaded", Toast.LENGTH_SHORT).show();
                        } else {
                            new Utils_VideoDownload(context, FromSinglecatActivity, 0, 0, filename);
                        }

                        Log.d("djjjjj", "onDownloadComplete" + Constance.savedVideoPath);

                        //dwonloadVideo();

                    }
                } else {
                    new AlertDialog.Builder(context)
                            .setMessage("Please show your business information.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                }

                break;
            case R.id.rlBackgroundColor:
                openDailogForBackgroundcolour();
                break;
            case R.id.rlOverlay:
                openDailogForOverlayBg();
                break;
            case R.id.rl_addlogo:
                openGallery();
                break;

            case R.id.rlBackgroundImageLocal:
                OpenDailogForLocalBackgroundImage();

                break;
            case R.id.ll_addimage:
                PickImageFromMobileGallery();
                break;

        }
    }

    private Uri saveImage(Context context, Bitmap bitmap, @NonNull String folderName, @NonNull String fileName) throws IOException {
        OutputStream fos = null;
        File imageFile = null;
        Uri imageUri = null;
        File imagesDir1 = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "girl");
        imagesDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() + File.separator + folderName);

        if (!imagesDir.exists())
            imagesDir.mkdir();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/*");
                contentValues.put(
                        MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + folderName);
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);


                if (imageUri == null)
                    throw new IOException("Failed to create new MediaStore record.");

                fos = resolver.openOutputStream(imageUri);
            } else {
                imagesDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).toString() + File.separator + folderName);

                if (!imagesDir.exists())
                    imagesDir.mkdir();

                imageFile = new File(imagesDir, fileName);
                fos = new FileOutputStream(imageFile);
            }


            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos))
                throw new IOException("Failed to save bitmap.");
            fos.flush();
        } finally {
            if (fos != null)
                fos.close();
        }
        if (imageFile != null) {//pre Q
            MediaScannerConnection.scanFile(context, new String[]{imageFile.toString()}, null, null);
            imageUri = Uri.fromFile(imageFile);
        }

        return imageUri;
    }


    Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        float originalWidth = bitmap.getWidth();
        float originalHeight = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        float scalex = wantedWidth / originalWidth;
        float scaley = wantedHeight / originalHeight;
        float xTranslation = 0.0f;
        float yTranslation = (wantedHeight - originalHeight * scaley) / 2.0f;
        m.postTranslate(xTranslation, yTranslation);
        m.preScale(scalex, scaley);
        // m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, m, paint);
        return output;
    }

    public void onclickCustomLayout(View view) {
        switch (view.getId()) {

        }
    }


    public void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("abc", "createquote");

        if (requestCode == 2 && resultCode == RESULT_OK) {
            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();

            Bitmap bitmap = null;
            try {
                // PF_Constances.cameraImage = true;
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photouri);
                //PF_Constances.bmp = bitmap;
                iv_cp_pic.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (Constance.activityName.equals("greeting")) {
                    iv_cp_pic.setImageURI(resultUri);

                } else {
                    iv_customimage.setImageURI(resultUri);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            if (photoUri != null) {
                try {
                    Bitmap currentImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    Drawable d = new BitmapDrawable(getResources(), currentImage);
                    DrawableSticker drawableSticker = new DrawableSticker(d);
                    sticker_view.addSticker(drawableSticker);
                    Constance.isStickerAvail = true;
                    Constance.isStickerTouch = true;
                    sticker_view.setLocked(false);
                    //  iv_customimage.setImageBitmap(currentImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*public void checkPermission() {

        if (ActivityCompat.checkSelfPermission(context, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ActivityCreateCustomImage.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        onBackPressed();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
        } else {

        }
    }*/

    public void loadAdMobAd() {

        //        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                // Check the LogCat to get your test device ID
//                .addTestDevice("C04B1BFFB0774708339BC273F8A43708")
//                .build();
//
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//        });
//
//        mAdView.loadAd(adRequest);
    }

    public void loadFacebookAd() {

        mAdView.setVisibility(View.GONE);
        facbook_ad_banner.setVisibility(View.VISIBLE);
        com.facebook.ads.AdView adFaceView = new com.facebook.ads.AdView(context, getResources().getString(R.string.facebook_banner_id), AdSize.BANNER_HEIGHT_50);

        AdSettings.setDebugBuild(true);

        // Add the ad view to your activity layout
        facbook_ad_banner.addView(adFaceView);


        adFaceView.loadAd();

    }

    @Override
    public void onResume() {
        super.onResume();

        mTracker.setScreenName("Image~" + "Google Analytics Testing");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        pb_sp.setVisibility(View.GONE);
        FromSinglecatActivity = Constance.FromSinglecatActivity;
        comeFrom = Constance.ComeFrom;
        Log.d("sdasjdiaisdi", "sdhjsd" + FromSinglecatActivity);
        if (comeFrom.equals("image")) {

            rl_video.setVisibility(View.GONE);
            if (FromSinglecatActivity != null) {
                Log.d("sdasjdiaisdi", "sdhjsd");
                iv_customimage.setVisibility(View.VISIBLE);
                Glide.with(context).load(FromSinglecatActivity).into(iv_customimage);
            } else {
                // iv_customimage.setImageResource(getFramesList().get(0).getFrames());
                //   finalframeimage = getFramesList().get(0).getFrames();
            }
        } else {

            rl_video.setVisibility(View.VISIBLE);
            if (FromSinglecatActivity != null) {
                playVideo();
            } else {
                // iv_customimage.setImageResource(getFramesList().get(0).getFrames());
                //   finalframeimage = getFramesList().get(0).getFrames();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (new File(Constance.savedVideoPath).exists()) {
            new File(Constance.savedVideoPath).delete();
        }
        if (new File(Constance.savedImagePath).exists()) {
            new File(Constance.savedImagePath).delete();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (new File(Constance.savedVideoPath).exists()) {
            new File(Constance.savedVideoPath).delete();
        }
        if (new File(Constance.savedImagePath).exists()) {
            new File(Constance.savedImagePath).delete();
        }
    }

    public static ArrayList<ModelColorList> getColorList() {
        ArrayList<ModelColorList> data = new ArrayList<>();
        /* data.add(new ModelColorList(R.color.colorWhite));*/
        data.add(new ModelColorList(R.color.color1));
        data.add(new ModelColorList(R.color.colorWhite));
        data.add(new ModelColorList(R.color.color6));
        data.add(new ModelColorList(R.color.color7));
        data.add(new ModelColorList(R.color.color3));
        data.add(new ModelColorList(R.color.color12));
        data.add(new ModelColorList(R.color.colorBlack));
        data.add(new ModelColorList(R.color.color2));
        data.add(new ModelColorList(R.color.color4));
        data.add(new ModelColorList(R.color.color5));
        data.add(new ModelColorList(R.color.color9));
        data.add(new ModelColorList(R.color.color8));
        data.add(new ModelColorList(R.color.color10));
        data.add(new ModelColorList(R.color.color11));


        return data;
    }


    public void openDailogForBackgroundcolour() {


        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailog_bg_color);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        final TextView tvOpacity = dialog.findViewById(R.id.tvOpacity);
        final SeekBar sbOpacity = dialog.findViewById(R.id.sbOpacity);
        RecyclerView rv_bg_color = dialog.findViewById(R.id.rv_bg_color);
        TextView tv_dailog_tittle = dialog.findViewById(R.id.tv_dailog_tittle);

        tv_dailog_tittle.setText("Change Text Color");
        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 7);
        rv_bg_color.setLayoutManager(linearLayoutManager);

      /*  Adapter_QC_SelectColorPicker adapterQCSelectColorPicker = new Adapter_QC_SelectColorPicker(context, R.layout.select_color_picker_row, DEFAULT, true, true, "textcolor");
        rvList.setAdapter(adapterQCSelectColorPicker);
*/
        AdapterTextColourPicker adapterTextColourPicker = new AdapterTextColourPicker(context, getColorList(), "bgTextcolor");
        rv_bg_color.setAdapter(adapterTextColourPicker);

        tvOpacity.setText(opcity + "%");
        sbOpacity.setProgress(opcity);
        sbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                progress = ((int) Math.round(progress / 5)) * 5;
                opcity = progress;
                tvOpacity.setText(progress + "%");
                Float valu = Float.valueOf(progress);
                setOpcity((valu / 100));
//                setOpcity((Float.valueOf(progress) / Float.valueOf(100)));
//                iv_customimage.setAlpha((Float.valueOf(progress) / Float.valueOf(100)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setOpcity(float process) {
        tv_mobilenumberlayout1.setAlpha(process);
        tv_mobilenumberlayout2.setAlpha(process);
        tv_mobilenumberlayout3.setAlpha(process);
        tv_mobilenumberlayout4.setAlpha(process);
        tv_mobilenumberlayout5.setAlpha(process);
        tv_mobilenumberlayout6.setAlpha(process);
        tv_calllayout7.setAlpha(process);
        tv_calllayout8.setAlpha(process);

        tv_emaillayout1.setAlpha(process);
        tv_emaillayout2.setAlpha(process);
        tv_emaillayout3.setAlpha(process);
        tv_emaillayout4.setAlpha(process);
        tv_emaillayout5.setAlpha(process);
        tv_emaillayout6.setAlpha(process);
//        tv_emaillayout7.setAlpha(process);
        tv_emaillayout8.setAlpha(process);

        tv_websitelayout1.setAlpha(process);
        tv_websitelayout2.setAlpha(process);
        tv_websitelayout3.setAlpha(process);
        tv_websitelayout4.setAlpha(process);
        tv_websitelayout5.setAlpha(process);
        tv_websitelayout6.setAlpha(process);
//        tv_websitelayout7.setAlpha(process);
        tv_websitelayout8.setAlpha(process);

        tv_locationlayout1.setAlpha(process);
        tv_locationlayout2.setAlpha(process);
        tv_locationlayout3.setAlpha(process);
        tv_locationlayout4.setAlpha(process);
        tv_locationlayout5.setAlpha(process);
        tv_locationlayout6.setAlpha(process);
        tv_locationlayout7.setAlpha(process);
        tv_locationlayout8.setAlpha(process);

        tv_f1_name.setAlpha(process);
        tv_f2_name.setAlpha(process);
        tv_f3_name.setAlpha(process);
        tv_f4_name.setAlpha(process);
        tv_f5_name.setAlpha(process);
        tv_f6_name.setAlpha(process);
        tv_f7_name.setAlpha(process);
        tv_f8_name.setAlpha(process);

    }

    public void PickImageFromMobileGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
//                .setAspectRatio(1, 1)
                .start((Activity) context);

    }


    public void OpenDailogForLocalBackgroundImage() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailog_bg_local_image);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

        Button btnok = dialog.findViewById(R.id.btnOk);
        RecyclerView rl_bglocalimage = dialog.findViewById(R.id.rl_bglocalimage);

        rl_bglocalimage.setLayoutManager(new GridLayoutManager(context, 2));
        AdapterBackgroundImage adapterBackgroundImage = new AdapterBackgroundImage(context, getLocalImageList(), "createpost");
        rl_bglocalimage.setAdapter(adapterBackgroundImage);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    public static ArrayList<ModelBackgroundImage> getLocalImageList() {
        ArrayList<ModelBackgroundImage> data = new ArrayList<>();
        data.add(new ModelBackgroundImage(R.drawable.img_1));
        data.add(new ModelBackgroundImage(R.drawable.img_2));
        data.add(new ModelBackgroundImage(R.drawable.img_3));
        data.add(new ModelBackgroundImage(R.drawable.pager_1));
        data.add(new ModelBackgroundImage(R.drawable.pager_2));

        return data;
    }

    public static ArrayList<ModelFramesDetails> getFramesList() {
        ArrayList<ModelFramesDetails> data = new ArrayList<>();
        data.add(new ModelFramesDetails(R.drawable.frm_9));
        data.add(new ModelFramesDetails(R.drawable.frm_10));
        data.add(new ModelFramesDetails(R.drawable.frm_11));
        data.add(new ModelFramesDetails(R.drawable.frm_12));
        data.add(new ModelFramesDetails(R.drawable.frm_13));
        data.add(new ModelFramesDetails(R.drawable.f1));
        data.add(new ModelFramesDetails(R.drawable.f2));
        data.add(new ModelFramesDetails(R.drawable.f7));
        data.add(new ModelFramesDetails(R.drawable.f8));
        data.add(new ModelFramesDetails(R.drawable.f9));
        data.add(new ModelFramesDetails(R.drawable.f10));
        data.add(new ModelFramesDetails(R.drawable.f3));
        data.add(new ModelFramesDetails(R.drawable.f5));

        return data;
    }

    public void setbackgroundLocalImage(int image) {
        iv_customimage.setImageResource(image);
        //  Picasso.get().load(image).into(iv_customimage);
    }


    public void openDailogForOverlayBg() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailog_bg_color);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

        final Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        final SeekBar sbOpacity = dialog.findViewById(R.id.sbOpacity);
        final RecyclerView rv_bg_color = dialog.findViewById(R.id.rv_bg_color);
        final TextView tvOpacity = dialog.findViewById(R.id.tvOpacity);
        final TextView tv_dailog_tittle = dialog.findViewById(R.id.tv_dailog_tittle);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 7);
        rv_bg_color.setLayoutManager(linearLayoutManager);
        AdapterTextColourPicker adapterTextColourPicker = new AdapterTextColourPicker(context, getColorList(), "overlay");
        rv_bg_color.setAdapter(adapterTextColourPicker);
        tv_dailog_tittle.setText("Overlay");

        sbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // progress = ((int) Math.round(progress / 5)) * 5;

                tvOpacity.setText(progress + "%");
                opacitybg.setAlpha((Float.valueOf(progress) / Float.valueOf(100)));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbOpacity.setProgress(20);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void setOverlayBackground(int colour) {
        opacitybg.setBackgroundResource(colour);
    }

    /*public void openDailogForFontColor() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailog_bg_color);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        final TextView tvOpacity = dialog.findViewById(R.id.tvOpacity);
        TextView tv_dailog_tittle = dialog.findViewById(R.id.tv_dailog_tittle);
        final SeekBar sbOpacity = dialog.findViewById(R.id.sbOpacity);
        RecyclerView rv_bg_color = dialog.findViewById(R.id.rv_bg_color);

        tv_dailog_tittle.setText("Change Font Colour");

        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 7);
        rv_bg_color.setLayoutManager(linearLayoutManager);
        AdapterTextColourPicker adapterTextColourPicker = new AdapterTextColourPicker(context, getColorList(), "fontcolor");
        rv_bg_color.setAdapter(adapterTextColourPicker);

        sbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = ( (int) Math.round(progress / 5) ) * 5;
                et_usertext.setAlpha(( Float.valueOf(progress) / Float.valueOf(100) ));
                tvOpacity.setText(progress + "%");


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setFontColor(int colour) {
        et_usertext.setTextColor(context.getColor(colour));
    }

    public void openDailogForFontStyle() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_fontstyle);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //  lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

        RecyclerView rvList = dialog.findViewById(R.id.rv_font_style);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvList.setLayoutManager(linearLayoutManager);

        AdapterFontList adapter = new AdapterFontList(context, getfontList());
        rvList.setAdapter(adapter);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
*/

    public static ArrayList<ModelFontDetail> getfontList() {
        ArrayList<ModelFontDetail> data = new ArrayList<>();
        String[] fontnamelist = new String[]
                {"abhayalibre_bold.ttf", "abhayalibre_extrabold.ttf", "abhayalibre_medium.ttf", "artifika_regular.ttf", "archivo_black.ttf",
                        "ArchivoNarrow.otf", "ABeeZee.otf", "After_Shok.ttf", "AbrilFatface.otf", "Acknowledgement.otf",
                        "Acme.ttf", "AlfaSlabOne.ttf", "AlmendraDisplay.otf", "Almendra.otf", "alpha_echo.ttf",
                        "Amadeus.ttf", "AMERSN.ttf", "ANUDI.ttf", "AquilineTwo.ttf", "Arbutus.ttf", "AlexBrush.ttf",
                        "Alisandra.ttf", "Allura.ttf", "Amarillo.ttf", "BEARPAW.ttf", "bigelowrules.ttf", "BLACKR.ttf",
                        "BOYCOTT.ttf", "BebasNeue.ttf", "BLKCHCRY.TTF", "Carousel.ttf", "Caslon_Calligraphic.ttf",
                        "CroissantOne.ttf", "Carnevalee-Freakshow.ttf", "CAROBTN.TTF", "CaviarDreams.ttf",
                        "Cocogoose.ttf", "diplomata.ttf", "deftone stylus.ttf", "Dosis.ttf", "FONTL.TTF",
                        "Hugtophia.ttf", "ICE_AGE.ttf", "Kingthings_Calligraphica.ttf", "Love Like This.ttf",
                        "MADE Canvas.otf", "Merci-Heart-Brush.ttf", "Metropolis.otf", "Montserrat.otf",
                        "MontserratAlternates.otf",
                        "norwester.otf", "ostrich.ttf", "squealer.ttf", "Titillium.otf", "Ubuntu.ttf"};
/*
        String[] fontList = new String[]{"ArchivoNarrow.otf", "ABeeZee.otf", "After_Shok.ttf", "AbrilFatface.otf", "Acknowledgement.otf", "Acme.ttf", "AlfaSlabOne.ttf", "AlmendraDisplay.otf", "Almendra.otf", "alpha_echo.ttf", "Amadeus.ttf", "AMERSN.ttf", "ANUDI.ttf", "AquilineTwo.ttf", "Arbutus.ttf", "AlexBrush.ttf", "Alisandra.ttf", "Allura.ttf", "Amarillo.ttf", "BEARPAW.ttf", "bigelowrules.ttf", "BLACKR.ttf", "BOYCOTT.ttf", "BebasNeue.ttf", "BLKCHCRY.TTF", "Carousel.ttf", "Caslon_Calligraphic.ttf", "CroissantOne.ttf", "Carnevalee-Freakshow.ttf", "CAROBTN.TTF", "CaviarDreams.ttf", "Cocogoose.ttf", "diplomata.ttf", "deftone stylus.ttf", "Dosis.ttf", "FONTL.TTF", "Hugtophia.ttf", "ICE_AGE.ttf", "Kingthings_Calligraphica.ttf", "Love Like This.ttf", "MADE Canvas.otf", "Merci-Heart-Brush.ttf", "Metropolis.otf", "Montserrat.otf", "MontserratAlternates.otf", "norwester.otf", "ostrich.ttf", "squealer.ttf", "Titillium.otf", "Ubuntu.ttf"};
*/

        for (int i = 0; i < fontnamelist.length; i++) {

            data.add(new ModelFontDetail(fontnamelist[i], fontnamelist[i]));
        }
        return data;

    }

  /*  public void setFontStyle(String fontName) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);

        et_usertext.setTypeface(font);
    }*/

   /* public void openDailogfForFontSize() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailog_fontsize);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //   lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        final SeekBar sbopacity_fontsize = dialog.findViewById(R.id.sbOpacity_fontsize);
        final TextView tvOpacity = dialog.findViewById(R.id.tvOpacity);

        sbopacity_fontsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekvalue = progress;
                tvOpacity.setText(progress + "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                et_usertext.setTextSize(seekvalue);
            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
*/

    private void touchListener(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                  /*  dx = et_usertext.getX() - event.getRawX();
                    dy = et_usertext.getY() - event.getRawY();*/
                    if (Constance.isStickerAvail) {
                        // Log.e("ACTION_DOWN Share.isStickerTouch", Share.isStickerTouch + "");

                        if (Constance.isStickerTouch || !Constance.isStickerTouch) {
                            Constance.isStickerTouch = false;
                            sticker_view.setLocked(true);
                        }
                    }
                   /* else
                    {

                    }*/
                    // Toast.makeText(getContext(), "you just touch the screen :-)", Toast.LENGTH_SHORT).show();
                } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    if (Constance.isStickerAvail) {

                        // Log.e("ACTION_UP Share.isStickerTouch", Share.isStickerTouch + "");
                        if (!Constance.isStickerTouch) {
                            Constance.isStickerTouch = true;
                            sticker_view.setLocked(false);
                        }
                    }
                } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                  /*  et_usertext.setY(event.getRawY() + dy);
                    et_usertext.setX(event.getRawX() + dx);*/
                    if (Constance.isStickerAvail) {
                        if (!Constance.isStickerTouch || Constance.isStickerTouch) {
                            Log.e("image move", "sticker lock");
                            Constance.isStickerTouch = false;
                            sticker_view.setLocked(true);
                        }
                    }/*else
                    {

                    }*/
                }
                return true;
            }
        });
    }

    public void interstitialAdMobAd() {
        //        if (ActivityHome.getInstance().mInterstitialAd != null) {
//            if (ActivityHome.getInstance().mInterstitialAd.isLoaded()) {
//                Log.d("shsjks", "sdhsjkhd");
//                ActivityHome.getInstance().mInterstitialAd.show();
//            } else {
//            }
//        } else {
//
//        }
    }


    void dwonloadVideo() {
        if (!Constance.FileSaveVideoDirectory.exists()) {
            Constance.FileSaveVideoDirectory.mkdir();
        }
        Log.d("djsj", "djd" + FromSinglecatActivity);
        PRDownloader.download(FromSinglecatActivity, Constance.FileSaveVideoDirectory.getPath(), "VideoDemo.mp4")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        Log.d("djjjjj", "onStartOrResume");


                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                        Log.d("djjjjj", "setOnPauseListener");
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        Log.d("djjjjj", "setOnCancelListener");

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        Log.d("djjjjj", "setOnProgressListener" + progress.toString());

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        pDialog1.dismiss();
                        Constance.savedVideoPath = Constance.FileSaveVideoDirectory.getPath() + "/VideoDemo.mp4";
                        Log.d("djjjjj", "onDownloadComplete" + Constance.savedVideoPath);
                        Intent save = new Intent(context, ActivityPreview.class);
                        save.putExtra("name", "video");
                        startActivity(save);
                      /*  Intent i = new Intent(context, ActivityVideoPreview.class);
                        Log.d("checkvideo", "intent: " + selectedvideopath);
                        i.putExtra("selectedvideopath", selectedvideopath);
                        Log.d("frsrsgh", "" + frame);
                        i.putExtra("framepath", frame);
                        startActivity(i);
                        onBackPressed();
                        File nn=new File(Constance.FileSaveVideoDirectory.getPath()+File.separator+"VideoDemo.mp4");

                        pb_video.setVisibility(View.GONE);*/

                        /*if (share) {
                            progressDialog.dismiss();
                            String filePath = cvb.getPath()+File.separator + name ;
                            Log.d("dhfshfdsh",""+filePath);
                            if (filePath != null) {
                                Uri mainUri = Uri.parse(filePath);
                                File path=new File(filePath);
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("video/mp4");
                                // Uri uri = Uri.fromFile(Path);
                                Uri uri = FileProvider.getUriForFile(context, context.getString(R.string.file_provider_authority), path);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                context.startActivity(Intent.createChooser(shareIntent, "Share Video"));
                               *//* Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("video/mp4");
                                sharingIntent.putExtra(Intent.EXTRA_STREAM, mainUri);
                                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                try {
                                    context.startActivity(Intent.createChooser(sharingIntent, "Share Video using"));
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(context, "Application not found to open this file", Toast.LENGTH_LONG).show();
                                }*//*
                            }
                        }*/
                    }

                    @Override
                    public void onError(Error error) {
                        Log.d("djjjjj", "onError" + error.toString());

                    }


                });
    }

    public void interstitialFacbookAd() {

        if (ActivityHome.getInstance().interstitialFacbookAd != null) {
            if (!ActivityHome.getInstance().interstitialFacbookAd.isAdLoaded()) {

                AdSettings.setDebugBuild(true);
                ActivityHome.getInstance().interstitialFacbookAd.loadAd();
            } else {

            }
        } else {

        }
    }

    public void setSelectedFrame(int position) {


/*

        tv_remove_email.setTextColor(getResources().getColor(R.color.colorWhite));
        tv_remove_email.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
        tv_remove_call.setTextColor(getResources().getColor(R.color.colorWhite));
        tv_remove_call.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
        tv_remove_web.setTextColor(getResources().getColor(R.color.colorWhite));
        tv_remove_web.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
        tv_remove_logo.setTextColor(getResources().getColor(R.color.colorWhite));
        tv_remove_logo.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
*/

        switch (position) {
            case 5:

                layout_custom_frame2.setVisibility(View.GONE);
                layout_custom_frame1.setVisibility(View.VISIBLE);
                layout_custom_frame3.setVisibility(View.GONE);
                layout_custom_frame4.setVisibility(View.GONE);
                layout_custom_frame5.setVisibility(View.GONE);
                layout_custom_frame6.setVisibility(View.GONE);
                layout_custom_frame7.setVisibility(View.GONE);
                layout_custom_frame8.setVisibility(View.GONE);
                layout_custom_frame9.setVisibility(View.GONE);
                layout_custom_frame10.setVisibility(View.GONE);
                layout_custom_frame11.setVisibility(View.GONE);
                layout_custom_frame12.setVisibility(View.GONE);
                layout_custom_frame13.setVisibility(View.GONE);

                break;
            case 6:
                layout_custom_frame1.setVisibility(View.GONE);
                layout_custom_frame2.setVisibility(View.VISIBLE);
                layout_custom_frame3.setVisibility(View.GONE);
                layout_custom_frame4.setVisibility(View.GONE);
                layout_custom_frame5.setVisibility(View.GONE);
                layout_custom_frame6.setVisibility(View.GONE);
                layout_custom_frame7.setVisibility(View.GONE);
                layout_custom_frame8.setVisibility(View.GONE);
                layout_custom_frame9.setVisibility(View.GONE);
                layout_custom_frame10.setVisibility(View.GONE);
                layout_custom_frame11.setVisibility(View.GONE);
                layout_custom_frame12.setVisibility(View.GONE);
                layout_custom_frame13.setVisibility(View.GONE);

                break;
            // frame 3(f7)
            case 7:
                layout_custom_frame1.setVisibility(View.GONE);
                layout_custom_frame2.setVisibility(View.GONE);
                layout_custom_frame3.setVisibility(View.VISIBLE);
                layout_custom_frame4.setVisibility(View.GONE);
                layout_custom_frame5.setVisibility(View.GONE);
                layout_custom_frame6.setVisibility(View.GONE);
                layout_custom_frame7.setVisibility(View.GONE);
                layout_custom_frame8.setVisibility(View.GONE);
                layout_custom_frame9.setVisibility(View.GONE);
                layout_custom_frame10.setVisibility(View.GONE);
                layout_custom_frame11.setVisibility(View.GONE);
                layout_custom_frame12.setVisibility(View.GONE);
                layout_custom_frame13.setVisibility(View.GONE);

                break;
            //frame 4 (f8)
            case 8:
                layout_custom_frame1.setVisibility(View.GONE);
                layout_custom_frame2.setVisibility(View.GONE);
                layout_custom_frame3.setVisibility(View.GONE);
                layout_custom_frame4.setVisibility(View.VISIBLE);
                layout_custom_frame5.setVisibility(View.GONE);
                layout_custom_frame6.setVisibility(View.GONE);
                layout_custom_frame7.setVisibility(View.GONE);
                layout_custom_frame8.setVisibility(View.GONE);
                layout_custom_frame9.setVisibility(View.GONE);
                layout_custom_frame10.setVisibility(View.GONE);
                layout_custom_frame11.setVisibility(View.GONE);
                layout_custom_frame12.setVisibility(View.GONE);
                layout_custom_frame13.setVisibility(View.GONE);

                break;
            //frame 4 (f9)
            case 9:
                layout_custom_frame1.setVisibility(View.GONE);
                layout_custom_frame2.setVisibility(View.GONE);
                layout_custom_frame3.setVisibility(View.GONE);
                layout_custom_frame4.setVisibility(View.GONE);
                layout_custom_frame5.setVisibility(View.VISIBLE);
                layout_custom_frame6.setVisibility(View.GONE);
                layout_custom_frame7.setVisibility(View.GONE);
                layout_custom_frame8.setVisibility(View.GONE);
                layout_custom_frame9.setVisibility(View.GONE);
                layout_custom_frame10.setVisibility(View.GONE);
                layout_custom_frame11.setVisibility(View.GONE);
                layout_custom_frame12.setVisibility(View.GONE);
                layout_custom_frame13.setVisibility(View.GONE);

                break;
            //frame 4 (f10)
            case 10:
                layout_custom_frame1.setVisibility(View.GONE);
                layout_custom_frame2.setVisibility(View.GONE);
                layout_custom_frame3.setVisibility(View.GONE);
                layout_custom_frame4.setVisibility(View.GONE);
                layout_custom_frame5.setVisibility(View.GONE);
                layout_custom_frame6.setVisibility(View.VISIBLE);
                layout_custom_frame7.setVisibility(View.GONE);
                layout_custom_frame8.setVisibility(View.GONE);
                layout_custom_frame9.setVisibility(View.GONE);
                layout_custom_frame10.setVisibility(View.GONE);
                layout_custom_frame11.setVisibility(View.GONE);
                layout_custom_frame12.setVisibility(View.GONE);
                layout_custom_frame13.setVisibility(View.GONE);

                break;
            //frame 4 (f3)
            case 11:
                layout_custom_frame1.setVisibility(View.GONE);
                layout_custom_frame2.setVisibility(View.GONE);
                layout_custom_frame3.setVisibility(View.GONE);
                layout_custom_frame4.setVisibility(View.GONE);
                layout_custom_frame5.setVisibility(View.GONE);
                layout_custom_frame6.setVisibility(View.GONE);
                layout_custom_frame7.setVisibility(View.VISIBLE);
                layout_custom_frame8.setVisibility(View.GONE);
                layout_custom_frame9.setVisibility(View.GONE);
                layout_custom_frame10.setVisibility(View.GONE);
                layout_custom_frame11.setVisibility(View.GONE);
                layout_custom_frame12.setVisibility(View.GONE);
                layout_custom_frame13.setVisibility(View.GONE);

                break; //frame 4 (f3)
            case 12:
                layout_custom_frame1.setVisibility(View.GONE);
                layout_custom_frame2.setVisibility(View.GONE);
                layout_custom_frame3.setVisibility(View.GONE);
                layout_custom_frame4.setVisibility(View.GONE);
                layout_custom_frame5.setVisibility(View.GONE);
                layout_custom_frame6.setVisibility(View.GONE);
                layout_custom_frame7.setVisibility(View.GONE);
                layout_custom_frame8.setVisibility(View.VISIBLE);
                layout_custom_frame9.setVisibility(View.GONE);
                layout_custom_frame10.setVisibility(View.GONE);
                layout_custom_frame11.setVisibility(View.GONE);
                layout_custom_frame12.setVisibility(View.GONE);
                layout_custom_frame13.setVisibility(View.GONE);
                break;
            case 0:
                layout_custom_frame1.setVisibility(View.GONE);
                layout_custom_frame2.setVisibility(View.GONE);
                layout_custom_frame3.setVisibility(View.GONE);
                layout_custom_frame4.setVisibility(View.GONE);
                layout_custom_frame5.setVisibility(View.GONE);
                layout_custom_frame6.setVisibility(View.GONE);
                layout_custom_frame7.setVisibility(View.GONE);
                layout_custom_frame8.setVisibility(View.GONE);
                layout_custom_frame9.setVisibility(View.VISIBLE);
                layout_custom_frame10.setVisibility(View.GONE);
                layout_custom_frame11.setVisibility(View.GONE);
                layout_custom_frame12.setVisibility(View.GONE);
                layout_custom_frame13.setVisibility(View.GONE);
                break;
            case 1:
                layout_custom_frame1.setVisibility(View.GONE);
                layout_custom_frame2.setVisibility(View.GONE);
                layout_custom_frame3.setVisibility(View.GONE);
                layout_custom_frame4.setVisibility(View.GONE);
                layout_custom_frame5.setVisibility(View.GONE);
                layout_custom_frame6.setVisibility(View.GONE);
                layout_custom_frame7.setVisibility(View.GONE);
                layout_custom_frame8.setVisibility(View.GONE);
                layout_custom_frame9.setVisibility(View.GONE);
                layout_custom_frame10.setVisibility(View.VISIBLE);
                layout_custom_frame11.setVisibility(View.GONE);
                layout_custom_frame12.setVisibility(View.GONE);
                layout_custom_frame13.setVisibility(View.GONE);
                break;
            case 2:
                layout_custom_frame1.setVisibility(View.GONE);
                layout_custom_frame2.setVisibility(View.GONE);
                layout_custom_frame3.setVisibility(View.GONE);
                layout_custom_frame4.setVisibility(View.GONE);
                layout_custom_frame5.setVisibility(View.GONE);
                layout_custom_frame6.setVisibility(View.GONE);
                layout_custom_frame7.setVisibility(View.GONE);
                layout_custom_frame8.setVisibility(View.GONE);
                layout_custom_frame9.setVisibility(View.GONE);
                layout_custom_frame10.setVisibility(View.GONE);
                layout_custom_frame11.setVisibility(View.VISIBLE);
                layout_custom_frame12.setVisibility(View.GONE);
                layout_custom_frame13.setVisibility(View.GONE);
                break;
            case 3:
                layout_custom_frame1.setVisibility(View.GONE);
                layout_custom_frame2.setVisibility(View.GONE);
                layout_custom_frame3.setVisibility(View.GONE);
                layout_custom_frame4.setVisibility(View.GONE);
                layout_custom_frame5.setVisibility(View.GONE);
                layout_custom_frame6.setVisibility(View.GONE);
                layout_custom_frame7.setVisibility(View.GONE);
                layout_custom_frame8.setVisibility(View.GONE);
                layout_custom_frame9.setVisibility(View.GONE);
                layout_custom_frame10.setVisibility(View.GONE);
                layout_custom_frame11.setVisibility(View.GONE);
                layout_custom_frame12.setVisibility(View.VISIBLE);
                layout_custom_frame13.setVisibility(View.GONE);
                break;
            case 4:
                layout_custom_frame1.setVisibility(View.GONE);
                layout_custom_frame2.setVisibility(View.GONE);
                layout_custom_frame3.setVisibility(View.GONE);
                layout_custom_frame4.setVisibility(View.GONE);
                layout_custom_frame5.setVisibility(View.GONE);
                layout_custom_frame6.setVisibility(View.GONE);
                layout_custom_frame7.setVisibility(View.GONE);
                layout_custom_frame8.setVisibility(View.GONE);
                layout_custom_frame9.setVisibility(View.GONE);
                layout_custom_frame10.setVisibility(View.GONE);
                layout_custom_frame11.setVisibility(View.GONE);
                layout_custom_frame12.setVisibility(View.GONE);
                layout_custom_frame13.setVisibility(View.VISIBLE);
                break;
        }
        //  iv_customimage.setImageResource(item);
        adapterFrame.notifyDataSetChanged();
    }

    /* public void setbackgroundcolor(int color) {
         tv_mobilenumberlayout1.setText(color);
        // iv_customimage.setImageResource(color);
     }*/
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setTextbackgroundcolor(int colour) {
        tv_mobilenumberlayout1.setTextColor(context.getColor(colour));
        tv_mobilenumberlayout2.setTextColor(context.getColor(colour));
        tv_mobilenumberlayout3.setTextColor(context.getColor(colour));
        tv_mobilenumberlayout4.setTextColor(context.getColor(colour));
        tv_mobilenumberlayout5.setTextColor(context.getColor(colour));
        tv_mobilenumberlayout6.setTextColor(context.getColor(colour));
        tv_calllayout7.setTextColor(context.getColor(colour));
        tv_calllayout8.setTextColor(context.getColor(colour));
        tv_calllayout9.setTextColor(context.getColor(colour));
        tv_calllayout10.setTextColor(context.getColor(colour));
        tv_calllayout11.setTextColor(context.getColor(colour));
        tv_calllayout12.setTextColor(context.getColor(colour));
        tv_calllayout13.setTextColor(context.getColor(colour));

        tv_emaillayout1.setTextColor(context.getColor(colour));
        tv_emaillayout2.setTextColor(context.getColor(colour));
        tv_emaillayout3.setTextColor(context.getColor(colour));
        tv_emaillayout4.setTextColor(context.getColor(colour));
        tv_emaillayout5.setTextColor(context.getColor(colour));
        tv_emaillayout6.setTextColor(context.getColor(colour));
//        tv_emaillayout7.setTextColor(context.getColor(colour));
        tv_emaillayout8.setTextColor(context.getColor(colour));
        tv_emaillayout9.setTextColor(context.getColor(colour));
        tv_emaillayout10.setTextColor(context.getColor(colour));
        tv_emaillayout11.setTextColor(context.getColor(colour));
        tv_emaillayout12.setTextColor(context.getColor(colour));
        tv_emaillayout13.setTextColor(context.getColor(colour));

        tv_websitelayout1.setTextColor(context.getColor(colour));
        tv_websitelayout2.setTextColor(context.getColor(colour));
        tv_websitelayout3.setTextColor(context.getColor(colour));
        tv_websitelayout4.setTextColor(context.getColor(colour));
        tv_websitelayout5.setTextColor(context.getColor(colour));
        tv_websitelayout6.setTextColor(context.getColor(colour));
//        tv_websitelayout7.setTextColor(context.getColor(colour));
        tv_websitelayout8.setTextColor(context.getColor(colour));
        tv_websitelayout9.setTextColor(context.getColor(colour));
        tv_websitelayout10.setTextColor(context.getColor(colour));
        tv_websitelayout11.setTextColor(context.getColor(colour));
        tv_websitelayout12.setTextColor(context.getColor(colour));
        tv_websitelayout13.setTextColor(context.getColor(colour));

        tv_locationlayout1.setTextColor(context.getColor(colour));
        tv_locationlayout2.setTextColor(context.getColor(colour));
        tv_locationlayout3.setTextColor(context.getColor(colour));
        tv_locationlayout4.setTextColor(context.getColor(colour));
        tv_locationlayout5.setTextColor(context.getColor(colour));
        tv_locationlayout6.setTextColor(context.getColor(colour));
        tv_locationlayout7.setTextColor(context.getColor(colour));
        tv_locationlayout8.setTextColor(context.getColor(colour));
        tv_locationlayout9.setTextColor(context.getColor(colour));
        tv_locationlayout10.setTextColor(context.getColor(colour));
        tv_locationlayout11.setTextColor(context.getColor(colour));
        tv_locationlayout12.setTextColor(context.getColor(colour));
        tv_locationlayout13.setTextColor(context.getColor(colour));

        tv_f1_name.setTextColor(context.getColor(colour));
        tv_f2_name.setTextColor(context.getColor(colour));
        tv_f3_name.setTextColor(context.getColor(colour));
        tv_f4_name.setTextColor(context.getColor(colour));
        tv_f5_name.setTextColor(context.getColor(colour));
        tv_f6_name.setTextColor(context.getColor(colour));
        tv_f7_name.setTextColor(context.getColor(colour));
        tv_f8_name.setTextColor(context.getColor(colour));
        tv_f9_name.setTextColor(context.getColor(colour));
        tv_f11_name.setTextColor(context.getColor(colour));
        tv_f10_name.setTextColor(context.getColor(colour));
        tv_f12_name.setTextColor(context.getColor(colour));
        tv_f13_name.setTextColor(context.getColor(colour));


/*
        tv_addresslayout4.setTextColor(context.getColor(colour));
        iv_addresslayout4.setColorFilter(context.getColor(colour));*/

        /*iv_emaillayout1.setColorFilter(context.getColor(colour));
        iv_emaillayout2.setColorFilter(context.getColor(colour));
        iv_emaillayout3.setColorFilter(context.getColor(colour));
        iv_emaillayout4.setColorFilter(context.getColor(colour));
        iv_emaillayout5.setColorFilter(context.getColor(colour));
        iv_emaillayout6.setColorFilter(context.getColor(colour));
        iv_emaillayout8.setColorFilter(context.getColor(colour));
        iv_calllayout1.setColorFilter(context.getColor(colour));
        iv_calllayout2.setColorFilter(context.getColor(colour));
        iv_calllayout3.setColorFilter(context.getColor(colour));
        iv_calllayout4.setColorFilter(context.getColor(colour));
        iv_calllayout5.setColorFilter(context.getColor(colour));
        iv_calllayout6.setColorFilter(context.getColor(colour));

        iv_weblayout1.setColorFilter(context.getColor(colour));
        iv_weblayout2.setColorFilter(context.getColor(colour));
        iv_weblayout3.setColorFilter(context.getColor(colour));
        iv_weblayout4.setColorFilter(context.getColor(colour));
        iv_weblayout5.setColorFilter(context.getColor(colour));
        iv_weblayout6.setColorFilter(context.getColor(colour));
        iv_weblayout7.setColorFilter(context.getColor(colour));
        iv_weblayout8.setColorFilter(context.getColor(colour));*/


    }

    public void calculationForHeight() {
        ViewTreeObserver vto = rl_content.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    rl_content.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    rl_content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                Constance.widthOfImage = rl_content.getMeasuredWidth();//1080 horizontalview
                Constance.heightOfImage = rl_content.getMeasuredHeight();//236

                ViewGroup.LayoutParams params = rl_content.getLayoutParams();
                params.height = Constance.widthOfImage;
                params.width = Constance.widthOfImage;
                rl_content.setLayoutParams(params);
            }
        });

       /* // displayheight
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayMetrics_height = displayMetrics.heightPixels;
        int displayMetrics_width = displayMetrics.widthPixels;
        Log.d("mGLTextureView","displayMetrics_height :"+displayMetrics_height);
        Log.d("mGLTextureView","displayMetrics_width :"+displayMetrics_width);

        //linearlayout view
        ViewTreeObserver vto = mGLTextureView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    ll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    ll.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width  = mGLTextureView.getMeasuredWidth();//1080 horizontalview
                videoview_height = mGLTextureView.getMeasuredHeight();//236
                Log.d("mGLTextureView","videoview_height :"+videoview_height);
                //Log.d("mGLTextureView","displaywidth :"+width);

            }
        });

        //horizontal view
        ViewTreeObserver vto1 = horizontal.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    horizontal.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    horizontal.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width  = horizontal.getMeasuredWidth();//1080 horizontalview
                 horizontal_viewheight = horizontal.getMeasuredHeight();//236
                Log.d("mGLTextureView","horizontal_viewheight :"+ horizontal_viewheight);
                //Log.d("mGLTextureView","displaywidth :"+width);

            }
        });

        int plus=dpToPx(50)+dpToPx(80)+ dpToPx(50)+dpToPx(14)+dpToPx(14);
        int finalplus=horizontal_viewheight+plus;
        Log.d("mGLTextureView","plus :"+plus);

        int minus=displayMetrics_height-finalplus;
       // int lastminus=minus-StatusBar_height;
        int lastminus=minus-statusBarHeight(getResources());
        Constance.heightOfVideo=lastminus;

        Log.d("mGLTextureView","minus :"+minus);
        Log.d("mGLTextureView","lastminus :"+lastminus);
*/
    }

    public void getbusinessdetail() {

        Handle_company_details();

        Handle_email();

        Handle_address();

        Handle_name();

        Handle_contact();

        Handle_logo();
        
        /*Api api = Base_Url.getClient().create(Api.class);
        Call<ResponseLogin> call = api.getBusinessDetails(sharedPrefrenceConfig.getPrefString(context, Constance.CToken, null));
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if (response !=null && response.body() !=null){
                    ResponseLogin getprofile = response.body();
                    if (getprofile.getResult() != null && getprofile.getResult().equals("1")) {
                        if (getprofile.getRecord() != null) {
                            if (getprofile.getRecord().getCompany() != null) {
                                companyRecord = getprofile.getRecord().getCompany();

                              
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setCancelable(false);
                                builder.setTitle("Sorry !!")
                                        .setMessage("For making post please add your business details first.")
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                                Intent i = new Intent(context, AddBusinessActivity.class);
                                                startActivity(i);
                                                finish();
                                           *//* et_businessemail.setText("");
                                            et_businessname.setText("");
                                            et_businessmobile.setText("");
                                            et_businessaddress.setText("");
                                            Glide.with(context).load(R.drawable.userholder).into(iv_selectedlogo);
*//*
                                            }
                                        });

                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("record null : " + response.body().getMessage())
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                      *//*  et_businessemail.setText("");
                                        et_businessname.setText("");
                                        et_businessmobile.setText("");
                                        et_businessaddress.setText("");*//*
                                            //Glide.with(context).load(R.drawable.userholder).into(iv_selectedlogo);

                                        }
                                    });

                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(response.body().getMessage())
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });

                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Toast.makeText(context, "check Network Connection", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void Handle_logo() {
        if (AppSyncTextUtils.check_empty_and_null(Admin.tinyDB.getString("path"))) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.userholder);
            requestOptions.error(R.drawable.userholder);
            Glide.with(context).load(Admin.tinyDB.getString("path")).apply(requestOptions).into(iv_logolayout1);
            Glide.with(context).load(Admin.tinyDB.getString("path")).apply(requestOptions).into(iv_logolayout2);
            Glide.with(context).load(Admin.tinyDB.getString("path")).apply(requestOptions).into(iv_logolayout3);
            Glide.with(context).load(Admin.tinyDB.getString("path")).apply(requestOptions).into(iv_logolayout4);
            Glide.with(context).load(Admin.tinyDB.getString("path")).apply(requestOptions).into(iv_logolayout5);
            Glide.with(context).load(Admin.tinyDB.getString("path")).apply(requestOptions).into(iv_logolayout6);
            Glide.with(context).load(Admin.tinyDB.getString("path")).apply(requestOptions).into(iv_logolayout7);
            Glide.with(context).load(Admin.tinyDB.getString("path")).apply(requestOptions).into(iv_logolayout8);
            Glide.with(context).load(Admin.tinyDB.getString("path")).apply(requestOptions).into(iv_logolayout9);
            Glide.with(context).load(Admin.tinyDB.getString("path")).apply(requestOptions).into(iv_logolayout10);
            Glide.with(context).load(Admin.tinyDB.getString("path")).apply(requestOptions).into(iv_logolayout11);
            Glide.with(context).load(Admin.tinyDB.getString("path")).apply(requestOptions).into(iv_logolayout12);
            Glide.with(context).load(Admin.tinyDB.getString("path")).apply(requestOptions).into(iv_logolayout13);
        } else {
            removeLogo(false);
        }
    }

    private void Handle_contact() {
        if (AppSyncTextUtils.check_empty_and_null(Admin.tinyDB.getString("number_one"))) {
            tv_mobilenumberlayout1.setText("" + Admin.tinyDB.getString("number_one"));
            tv_mobilenumberlayout2.setText("" + Admin.tinyDB.getString("number_one"));
            tv_mobilenumberlayout3.setText("" + Admin.tinyDB.getString("number_one"));
            tv_mobilenumberlayout4.setText("" + Admin.tinyDB.getString("number_one"));
            tv_mobilenumberlayout5.setText("" + Admin.tinyDB.getString("number_one"));
            tv_mobilenumberlayout6.setText("" + Admin.tinyDB.getString("number_one"));
            tv_calllayout7.setText("" + Admin.tinyDB.getString("number_one"));
            tv_calllayout8.setText("" + Admin.tinyDB.getString("number_one"));
            tv_calllayout9.setText("" + Admin.tinyDB.getString("number_one"));
            tv_calllayout10.setText("" + Admin.tinyDB.getString("number_one"));
            tv_calllayout11.setText("" + Admin.tinyDB.getString("number_one"));
            tv_calllayout12.setText("" + Admin.tinyDB.getString("number_one"));
            tv_calllayout13.setText("" + Admin.tinyDB.getString("number_one"));

        } else {
            ismobile = false;
            tv_mobilenumberlayout1.setText("");
            tv_mobilenumberlayout2.setText("");
            tv_mobilenumberlayout3.setText("");
            tv_mobilenumberlayout4.setText("");
            tv_mobilenumberlayout5.setText("");
            tv_mobilenumberlayout6.setText("");
            tv_calllayout7.setText("");
            tv_calllayout8.setText("");

            iv_calllayout1.setVisibility(View.GONE);
            iv_calllayout2.setVisibility(View.GONE);
            iv_calllayout3.setVisibility(View.GONE);
            iv_calllayout4.setVisibility(View.GONE);
            iv_calllayout5.setVisibility(View.GONE);
            iv_calllayout6.setVisibility(View.GONE);
            ll_f7_call.setVisibility(View.GONE);
            ll_f8_call.setVisibility(View.GONE);
            ll_f9_call.setVisibility(View.GONE);
            ll_f10_call.setVisibility(View.GONE);
            ll_f11_call.setVisibility(View.INVISIBLE);
            ll_f12_call.setVisibility(View.INVISIBLE);
            ll_f13_call.setVisibility(View.INVISIBLE);

        }
    }

    private void Handle_name() {
        if (AppSyncTextUtils.check_empty_and_null(Admin.tinyDB.getString("name"))) {
            tv_f1_name.setText(Admin.tinyDB.getString("name"));
            tv_f2_name.setText(Admin.tinyDB.getString("name"));
            tv_f3_name.setText(Admin.tinyDB.getString("name"));
            tv_f4_name.setText(Admin.tinyDB.getString("name"));
            tv_f5_name.setText(Admin.tinyDB.getString("name"));
            tv_f6_name.setText(Admin.tinyDB.getString("name"));
            tv_f7_name.setText(Admin.tinyDB.getString("name"));
            tv_f8_name.setText(Admin.tinyDB.getString("name"));
            tv_f9_name.setText(Admin.tinyDB.getString("name"));
            tv_f10_name.setText(Admin.tinyDB.getString("name"));
            tv_f11_name.setText(Admin.tinyDB.getString("name"));
            tv_f12_name.setText(Admin.tinyDB.getString("name"));
            tv_f13_name.setText(Admin.tinyDB.getString("name"));

            //tv_addresslayout4.setText("" + Admin.tinyDB.getString("address"));
        } else {
                                /*tv_addresslayout4.setText("");
                                iv_addresslayout4.setVisibility(View.GONE);*/
            tv_f1_name.setVisibility(View.GONE);
            tv_f2_name.setVisibility(View.GONE);
            tv_f3_name.setVisibility(View.GONE);
            tv_f4_name.setVisibility(View.GONE);
            tv_f5_name.setVisibility(View.GONE);
            tv_f6_name.setVisibility(View.GONE);
            tv_f7_name.setVisibility(View.GONE);
            tv_f8_name.setVisibility(View.GONE);
            tv_f9_name.setVisibility(View.GONE);
            tv_f10_name.setVisibility(View.GONE);
            tv_f11_name.setVisibility(View.GONE);
            tv_f12_name.setVisibility(View.GONE);
            tv_f13_name.setVisibility(View.GONE);

        }
    }

    private void Handle_address() {
        if (AppSyncTextUtils.check_empty_and_null(Admin.tinyDB.getString("address"))) {
            tv_locationlayout1.setText(Admin.tinyDB.getString("address"));
            tv_locationlayout2.setText(Admin.tinyDB.getString("address"));
            tv_locationlayout3.setText(Admin.tinyDB.getString("address"));
            tv_locationlayout4.setText(Admin.tinyDB.getString("address"));
            tv_locationlayout5.setText(Admin.tinyDB.getString("address"));
            tv_locationlayout6.setText(Admin.tinyDB.getString("address"));
            tv_locationlayout7.setText(Admin.tinyDB.getString("address"));
            tv_locationlayout8.setText(Admin.tinyDB.getString("address"));
            tv_locationlayout9.setText(Admin.tinyDB.getString("address"));
            tv_locationlayout10.setText(Admin.tinyDB.getString("address"));
            tv_locationlayout11.setText(Admin.tinyDB.getString("address"));
            tv_locationlayout12.setText(Admin.tinyDB.getString("address"));
            tv_locationlayout13.setText(Admin.tinyDB.getString("address"));
            //tv_addresslayout4.setText("" + Admin.tinyDB.getString("address"));
        } else {
                                /*tv_addresslayout4.setText("");
                                iv_addresslayout4.setVisibility(View.GONE);*/
            ll_f1_location.setVisibility(View.GONE);
            ll_f2_location.setVisibility(View.GONE);
            ll_f3_location.setVisibility(View.GONE);
            ll_f4_location.setVisibility(View.GONE);
            ll_f5_location.setVisibility(View.GONE);
            ll_f6_location.setVisibility(View.GONE);
            ll_f7_location.setVisibility(View.GONE);
            ll_f8_location.setVisibility(View.GONE);
            ll_f9_location.setVisibility(View.GONE);
            ll_f10_location.setVisibility(View.GONE);
            ll_f11_location.setVisibility(View.GONE);
            ll_f12_location.setVisibility(View.GONE);
            ll_f13_location.setVisibility(View.GONE);
        }
    }

    private void Handle_email() {
        if (AppSyncTextUtils.check_empty_and_null(Admin.tinyDB.getString("email"))) {
            tv_emaillayout1.setText("" + Admin.tinyDB.getString("email"));
            tv_emaillayout2.setText("" + Admin.tinyDB.getString("email"));
            tv_emaillayout3.setText("" + Admin.tinyDB.getString("email"));
            tv_emaillayout4.setText("" + Admin.tinyDB.getString("email"));
            tv_emaillayout5.setText("" + Admin.tinyDB.getString("email"));
            tv_emaillayout6.setText("" + Admin.tinyDB.getString("email"));
//                                    tv_emaillayout7.setText("" + Admin.tinyDB.getString("email"));
            tv_emaillayout8.setText("" + Admin.tinyDB.getString("email"));
            tv_emaillayout9.setText("" + Admin.tinyDB.getString("email"));
            tv_emaillayout10.setText("" + Admin.tinyDB.getString("email"));
            tv_emaillayout11.setText("" + Admin.tinyDB.getString("email"));
            tv_emaillayout12.setText("" + Admin.tinyDB.getString("email"));
            tv_emaillayout13.setText("" + Admin.tinyDB.getString("email"));
        } else {
            ismail = false;
            tv_emaillayout1.setText("");
            tv_emaillayout2.setText("");
            tv_emaillayout3.setText("");
            tv_emaillayout4.setText("");
            tv_emaillayout5.setText("");
            tv_emaillayout6.setText("");
//                                    tv_emaillayout7.setText("");
            tv_emaillayout8.setText("");

            iv_emaillayout1.setVisibility(View.GONE);
            iv_emaillayout2.setVisibility(View.GONE);
            iv_emaillayout3.setVisibility(View.GONE);
            iv_emaillayout4.setVisibility(View.GONE);
            iv_emaillayout5.setVisibility(View.GONE);
            iv_emaillayout6.setVisibility(View.GONE);
//                                    tv_emaillayout7.setVisibility(View.GONE);
            iv_emaillayout8.setVisibility(View.GONE);
            ll_f9_email.setVisibility(View.GONE);
            ll_f10_email.setVisibility(View.GONE);
                             /*   ll_f11_email.setVisibility(View.INVISIBLE);
                                ll_f12_email.setVisibility(View.INVISIBLE);
                                ll_f13_email.setVisibility(View.INVISIBLE);*/

            if (!Admin.tinyDB.getString("website").equals("")) {

                ll_f11_call.setVisibility(View.INVISIBLE);
                ll_f12_call.setVisibility(View.INVISIBLE);
                ll_f13_call.setVisibility(View.INVISIBLE);

                iv_weblayout11.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                iv_weblayout12.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                iv_weblayout13.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                tv_websitelayout11.setText(Admin.tinyDB.getString("number_one"));
                tv_websitelayout12.setText(Admin.tinyDB.getString("number_one"));
                tv_websitelayout13.setText(Admin.tinyDB.getString("number_one"));
            }
        }
    }

    private void Handle_company_details() {
        if (AppSyncTextUtils.check_empty_and_null(Admin.tinyDB.getString("website"))) {
            tv_websitelayout1.setText("" + Admin.tinyDB.getString("website"));
            tv_websitelayout2.setText("" + Admin.tinyDB.getString("website"));
            tv_websitelayout3.setText("" + Admin.tinyDB.getString("website"));
            tv_websitelayout4.setText("" + Admin.tinyDB.getString("website"));
            tv_websitelayout5.setText("" + Admin.tinyDB.getString("website"));
            tv_websitelayout6.setText("" + Admin.tinyDB.getString("website"));
//                                    tv_websitelayout7.setText("" + Admin.tinyDB.getString("website"));
            tv_websitelayout8.setText("" + Admin.tinyDB.getString("website"));
            tv_websitelayout9.setText("" + Admin.tinyDB.getString("website"));
            tv_websitelayout10.setText("" + Admin.tinyDB.getString("website"));
            tv_websitelayout11.setText("" + Admin.tinyDB.getString("website"));
            tv_websitelayout12.setText("" + Admin.tinyDB.getString("website"));
            tv_websitelayout13.setText("" + Admin.tinyDB.getString("website"));

        } else {
            isweb = false;
            tv_websitelayout1.setText("");
            tv_websitelayout2.setText("");
            tv_websitelayout3.setText("");
            tv_websitelayout4.setText("");
            tv_websitelayout5.setText("");
//                                tv_websitelayout6.setText("");
//                                    tv_websitelayout7.setText("");
            tv_websitelayout8.setText("");

            iv_weblayout1.setVisibility(View.GONE);
            iv_weblayout2.setVisibility(View.GONE);
            iv_weblayout3.setVisibility(View.GONE);
            iv_weblayout4.setVisibility(View.GONE);
            iv_weblayout5.setVisibility(View.GONE);
//                                iv_weblayout6.setVisibility(View.GONE);
//                                    iv_weblayout7.setVisibility(View.GONE);
            iv_weblayout8.setVisibility(View.GONE);
            ll_f6_web.setVisibility(View.GONE);
            ll_f9_web.setVisibility(View.GONE);
            ll_f10_web.setVisibility(View.GONE);
                              /*  ll_f11_web.setVisibility(View.INVISIBLE);
                                ll_f12_web.setVisibility(View.INVISIBLE);
                                ll_f13_web.setVisibility(View.INVISIBLE);*/
            if (!Admin.tinyDB.getString("email").equals("")) {
                ll_f11_call.setVisibility(View.INVISIBLE);
                ll_f12_call.setVisibility(View.INVISIBLE);
                ll_f13_call.setVisibility(View.INVISIBLE);
                iv_weblayout11.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                iv_weblayout12.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                iv_weblayout13.setImageDrawable(getResources().getDrawable(R.drawable.mobile));
                tv_websitelayout11.setText(Admin.tinyDB.getString("number_one"));
                tv_websitelayout12.setText(Admin.tinyDB.getString("number_one"));
                tv_websitelayout13.setText(Admin.tinyDB.getString("number_one"));
            }


        }
    }
}