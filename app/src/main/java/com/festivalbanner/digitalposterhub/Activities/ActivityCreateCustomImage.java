package com.festivalbanner.digitalposterhub.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.festivalbanner.digitalposterhub.Adapters.AdapterBackgroundImage;
import com.festivalbanner.digitalposterhub.Adapters.AdapterFontList;
import com.festivalbanner.digitalposterhub.Adapters.AdapterTextColourPicker;
import com.festivalbanner.digitalposterhub.AnalyticsApplication;
import com.festivalbanner.digitalposterhub.Model.ModelBackgroundImage;
import com.festivalbanner.digitalposterhub.Model.ModelColorList;
import com.festivalbanner.digitalposterhub.Model.ModelFontDetail;
import com.festivalbanner.digitalposterhub.Model.ModelFramesDetails;
import com.festivalbanner.digitalposterhub.ModelRetrofit.RecordRegister;
import com.festivalbanner.digitalposterhub.StickerClasses.DrawableSticker;
import com.festivalbanner.digitalposterhub.StickerClasses.Sticker;
import com.festivalbanner.digitalposterhub.StickerClasses.StickerView;
import com.festivalbanner.digitalposterhub.StickerClasses.TextSticker;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.festivalbanner.digitalposterhub.Utills.PaletteBar;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
import com.festivalbanner.digitalposterhub.R;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class ActivityCreateCustomImage extends AppCompatActivity {

    Context context;
    ImageView iv_customimage;
    Tracker mTracker;
    TextSticker txtsticker;
    int textStickerColor = R.color.colorBlack;
    EditText et_usertext, et_text_sticker;
    AdView mAdView;
    LinearLayout facbook_ad_banner;

    public ArrayList<ModelFontDetail> modelFontDetailArrayList;
    Bitmap bitmapsave;
    int textcolor, fontcolor;
    StickerView sticker_view;
    LinearLayout ll_content, ll_main_custom;
    int seekvalue;
    Float dx, dy;
    RelativeLayout opacitybg;
    SharedPrefrenceConfig sharedPrefrenceConfig;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static ActivityCreateCustomImage instance = null;
    PopupWindow mPopupWindow, mPopupWindowpw;
    String typStyle="";
    public ActivityCreateCustomImage() {
        instance = ActivityCreateCustomImage.this;
    }

    public static synchronized ActivityCreateCustomImage getInstance() {
        if (instance == null) {
            instance = new ActivityCreateCustomImage();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_custom_image);
        context = ActivityCreateCustomImage.this;
        bindView();

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        sharedPrefrenceConfig = new SharedPrefrenceConfig(context);
        calculationForHeight();
        //getbusinessdetail();

//        if (getuser.getPlan_type() == null || !getuser.getPlan_type().equals("Premium")) {
        if (!Constance.isPremium) {
            if (Constance.adType.equals("Ad Mob")) {
                loadAdMobAd();

            } else {
                loadFacebookAd();
            }
        }else {
            mAdView.setVisibility(View.GONE);
            facbook_ad_banner.setVisibility(View.GONE);
        }
        //touchListener(ll_content);
        Constance.isStickerAvail = false;
        if (!Constance.isStickerAvail) {
            Constance.isStickerTouch = false;
            sticker_view.setLocked(true);
        }
        touchListener(ll_content);

        modelFontDetailArrayList = new ArrayList<>();

      /*  LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_framelist.setLayoutManager(layoutManager);
*/
       /* adapterFrame = new AdapterFrames(context, getFramesList());
        adapterFrame.notifyDataSetChanged();
        rv_framelist.setAdapter(adapterFrame);
*/

        et_text_sticker.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    builder.setMessage("Are you wants to edit sticker ?");
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
                                // AlertDialogBox.AlertMessage(context, "Please enter text");
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
                                Constance.isStickerAvail = true;
                                Constance.isStickerTouch = true;
                                sticker_view.setLocked(false);
                            }

                        }
                    });
                    android.app.AlertDialog alert = builder.create();
                    alert.setCancelable(false);
                    alert.show();
                    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(Color.BLACK);
                    pbutton.setBackgroundColor(Color.WHITE);
                    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setTextColor(Color.BLACK);
                    nbutton.setBackgroundColor(Color.WHITE);

                }
                return false;
            }
        });
        sticker_view.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {


            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker1) {
                sticker_view.hideIcons(false);
                sticker_view.setLocked(false);
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                if (typStyle.equals("font")) {
                    sticker_view.hideIcons(false);
                    sticker_view.setLocked(false);
                    et_usertext.setVisibility(View.VISIBLE);
                    //    textStickerPopUp();
                    sticker = (TextSticker) sticker_view.getCurrentSticker();
                    et_usertext.setText(((TextSticker) sticker).getText().toString());
                    et_usertext.setTextColor(((TextSticker) sticker).getTextColor());
                    sticker_view.remove(sticker);
                }else {

                    sticker_view.hideIcons(false);
                    sticker_view.setLocked(false);
                    et_text_sticker.setVisibility(View.VISIBLE);
                    sticker = (TextSticker) sticker_view.getCurrentSticker();
                    et_text_sticker.setText(((TextSticker) sticker).getText());
                    et_text_sticker.setTextColor(((TextSticker) sticker).getTextColor());
                    sticker_view.remove(sticker);
                }
            }
        });

    }


    public void bindView() {
        mAdView = findViewById(R.id.adview);
        facbook_ad_banner = findViewById(R.id.facbook_ad_banner);
        //   rv_cusomFrame = findViewById(R.id.rv_cusomFrame);
        iv_customimage = findViewById(R.id.iv_customimage);
        et_usertext = findViewById(R.id.et_usertext);
        et_usertext.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sticker_view = findViewById(R.id.sticker_view);
        // ll_mainlayout = findViewById(R.id.ll_mainlayout);
        ll_content = findViewById(R.id.ll_content);
        opacitybg = findViewById(R.id.opacitybg);
        //rv_framelist = findViewById(R.id.rv_framelist);
        et_text_sticker = findViewById(R.id.et_text_sticker);
        et_text_sticker.setImeOptions(EditorInfo.IME_ACTION_DONE);
        ll_main_custom = findViewById(R.id.ll_main_custom);

    }

    public void onclickCustomFrame(View view) {
        SharedPrefrenceConfig pref = new SharedPrefrenceConfig(context);
        RecordRegister getuser = pref.getUser();
        switch (view.getId()) {
            case R.id.iv_backarrow:
                onBackPressed();
                break;
            case R.id.ll_next:

                sticker_view.hideIcons(true);
                bitmapsave = viewToBitmap(ll_content);
                Constance.createdBitmap = bitmapsave;
                Intent save = new Intent(context, ActivityPreview.class);
                save.putExtra("name", "image");
                startActivity(save);
//        if (getuser.getPlan_type() == null || !getuser.getPlan_type().equals("Premium")) {
                if (!Constance.isPremium) {
                    if (Constance.adType.equals("Ad Mob")) {
                        interstitialAdMobAd();
                        Log.d("ADssss", "Ad Mob");
                    } else {
                        interstitialFacbookAd();
                        Log.d("ADssss", "Facebook");
                    }
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
            case R.id.rl_removehinttxt:
                if (et_usertext.getVisibility() == View.VISIBLE) {
                    et_usertext.setVisibility(View.GONE);
                } else {
                    et_usertext.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_AddText:
                et_text_sticker.setVisibility(View.VISIBLE);
                et_text_sticker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        et_text_sticker.post(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
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
                et_text_sticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + "Acme.ttf"));
                Constance.FontStyle = "Acme.ttf";
                break;
            case R.id.rl_fontcolor:
                //openDailogForFontColor();

                textStickerColorPopUp("font");
                typStyle="font";
                break;
            case R.id.rl_Fontstyle:
                openDailogForFontStyle();

                break;
            case R.id.rlTextSize:
                openDailogfForFontSize();

                break;
            case R.id.rlBackgroundImageLocal:
                OpenDailogForLocalBackgroundImage();

                break;
            case R.id.rl_addimage:
                PickImageFromMobileGallery();
                break;

        }
    }

    public void onclickCustomLayout(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure to back ?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();

                    }
                });


        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    public void openDailogForFontColor() {
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
                progress = ((int) Math.round(progress / 5)) * 5;
                et_usertext.setAlpha((Float.valueOf(progress) / Float.valueOf(100)));
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

        AdapterFontList adapter = new AdapterFontList(context, getfontList(), "fontstyle");
        rvList.setAdapter(adapter);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void setFontStyle(String fontName) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);

        et_usertext.setTypeface(font);
    }

    public void openDailogfForFontSize() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("abc", "createquote");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                iv_customimage.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        if (resultCode == RESULT_OK) {
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

        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 7);
        rv_bg_color.setLayoutManager(linearLayoutManager);

      /*  Adapter_QC_SelectColorPicker adapterQCSelectColorPicker = new Adapter_QC_SelectColorPicker(context, R.layout.select_color_picker_row, DEFAULT, true, true, "textcolor");
        rvList.setAdapter(adapterQCSelectColorPicker);
*/
        AdapterTextColourPicker adapterTextColourPicker = new AdapterTextColourPicker(context, getColorList(), "bgcolor");
        rv_bg_color.setAdapter(adapterTextColourPicker);


        sbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = ((int) Math.round(progress / 5)) * 5;
                iv_customimage.setAlpha((Float.valueOf(progress) / Float.valueOf(100)));
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
        AdapterBackgroundImage adapterBackgroundImage = new AdapterBackgroundImage(context, getLocalImageList(), "customimage");
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
        AdapterTextColourPicker adapterTextColourPicker = new AdapterTextColourPicker(context, getColorList(), "overlay_custom");
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

    private void touchListener(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    dx = et_usertext.getX() - event.getRawX();
                    dy = et_usertext.getY() - event.getRawY();
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
                    et_usertext.setY(event.getRawY() + dy);
                    et_usertext.setX(event.getRawX() + dx);
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

    public void setbackgroundcolor(int color) {
        iv_customimage.setImageResource(color);
    }

    public void calculationForHeight() {
        ViewTreeObserver vto = ll_content.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    ll_content.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    ll_content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                Constance.widthOfImage = ll_content.getMeasuredWidth();//1080 horizontalview
                Constance.heightOfImage = ll_content.getMeasuredHeight();//236

                ViewGroup.LayoutParams params = ll_content.getLayoutParams();
                params.height = Constance.widthOfImage;
                params.width = Constance.widthOfImage;
                ll_content.setLayoutParams(params);
            }
        });

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
        mPopupWindowpw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindowpw.setOutsideTouchable(false);
        mPopupWindowpw.showAtLocation(ll_main_custom, Gravity.BOTTOM, 0, 0);


        final TextView dialogTitle = (TextView) customView.findViewById(R.id.cp_accent_title);
        final LinearLayout ll_text_color = (LinearLayout) customView.findViewById(R.id.ll_text_color);
        final LinearLayout ll_text_style = (LinearLayout) customView.findViewById(R.id.ll_text_style);
        final Button btn_Ok = (Button) customView.findViewById(R.id.btn_Ok);


        ll_text_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typStyle="stickerText";
                textStickerColorPopUp("stickerText");

            }
        });


        ll_text_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTextFontStyle();

            }
        });
        dialogTitle.setText("Edit Sticker");


        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_text_sticker.getText().toString().equals("") || et_text_sticker.getText().toString().equals(null)) {
                    //   AlertDialogBox.AlertMessage(context, "Please enter text");
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
                    Constance.isStickerAvail = true;
                    Constance.isStickerTouch = true;
                    sticker_view.setLocked(false);
                    mPopupWindowpw.dismiss();


                }


            }


        });


    }

    public void textStickerColorPopUp(String type) {
        et_usertext.setTextColor(fontcolor);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View customView = inflater.inflate(R.layout.select_color_popup, null);

        PaletteBar paletteBar = customView.findViewById(R.id.paletteBar);
        paletteBar.setListener(new PaletteBar.PaletteBarListener() {
            @Override
            public void onColorSelected(int color) {

                if (type.equals("font")) {
                    typStyle="font";
                    et_usertext.setTextColor(color);
                    fontcolor = color;
                } else {
                    typStyle="stickerText";
                    et_text_sticker.setTextColor(color);
                    textcolor = color;
                }


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
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(false);


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


        mPopupWindow.showAtLocation(ll_main_custom, Gravity.BOTTOM, 0, 0);
    }

    // Method (Font Style to Text)...................
    public void SetFontToText(String FontName) {
        et_text_sticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + FontName));
        Constance.FontStyle = FontName;
    }

    public void openTextFontStyle() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_fontstyle);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(false);
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

        AdapterFontList adapter = new AdapterFontList(context, getfontList(), "fontforstickertext");
        rvList.setAdapter(adapter);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

}