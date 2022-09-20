package com.festivalbanner.digitalposterhub.Activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.festivalbanner.digitalposterhub.Adapters.AdapterCategoryList;
import com.festivalbanner.digitalposterhub.AnalyticsApplication;
import com.festivalbanner.digitalposterhub.Fragments.AllCustomFragment;
import com.festivalbanner.digitalposterhub.Fragments.CategoryFragment;
import com.festivalbanner.digitalposterhub.Fragments.CreateCustomImageFragment;
import com.festivalbanner.digitalposterhub.Fragments.FeedbackFragment;
import com.festivalbanner.digitalposterhub.Fragments.FragmentAboutUs;
import com.festivalbanner.digitalposterhub.Fragments.FragmentAddBusiness;
import com.festivalbanner.digitalposterhub.Fragments.FragmentContactUs;
import com.festivalbanner.digitalposterhub.Fragments.FragmentHome;
import com.festivalbanner.digitalposterhub.Fragments.FragmentMyPost;
import com.festivalbanner.digitalposterhub.Fragments.FragmentMyProfile;
import com.festivalbanner.digitalposterhub.Fragments.FragmentPolicy;
import com.festivalbanner.digitalposterhub.Fragments.FragmentVideos;
import com.festivalbanner.digitalposterhub.Fragments.ProfileFragment;
import com.festivalbanner.digitalposterhub.ModelRetrofit.RecordRegister;
import com.festivalbanner.digitalposterhub.ModelRetrofit.ResponseLogin;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
import com.festivalbanner.digitalposterhub.Utills.Admin;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
import com.festivalbanner.digitalposterhub.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Timer;
import java.util.TimerTask;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHome extends AppCompatActivity {

    Context context;
    DrawerLayout drawerLayout;
    public ImageView ivBack;
    public ImageView iv_menuhome, iv_userimg, iv_language;
    public TextView tv_titletoolbar;
    String check_fragmentname;
    SharedPrefrenceConfig sharedprefconfig;
    // private ColorResource colorResource;
    View popupview_down;
    String frameName[] = {"All", "English", "Hindi", "Gujarati"};

    public PopupWindowHelper popupWindowHelper;
    AdView mAdView;
    LinearLayout facbook_ad_banner, ll_bnav_home, ll_bnav_category, ll_bnav_custom,
            ll_bnav_video, ll_bnav_post, ll_bnav_profile;
    ImageView iv_nav_home, iv_nav_ctgry, iv_nav_cstm, iv_nav_video, iv_nav_mypost, iv_nav_profile;
    public com.facebook.ads.InterstitialAd interstitialFacbookAd;
    public InterstitialAd mInterstitialAd;
    Timer timer;
    TimerTask hourlyTask;
    Button tv_buy_pre;
    TextView tv_nav_logout, tv_bnav_home, tv_bnav_cat, tv_bnav_custom,
            tv_bnav_video, tv_bnav_post, tv_bnav_profile;
    public LinearLayout ll_next;
    private final String TAG = ActivityHome.class.getSimpleName();
    public static ActivityHome instance = null;

    public ActivityHome() {
        instance = ActivityHome.this;
    }

    public static synchronized ActivityHome getInstance() {
        if (instance == null) {
            instance = new ActivityHome();
        }
        return instance;
    }

    Tracker mTracker;
    RecordRegister getuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_home);

        context = ActivityHome.this;
        sharedprefconfig = new SharedPrefrenceConfig(context);
        getuser = sharedprefconfig.getUser();
        bindView();
        boolean islogoin = Admin.tinyDB.getBoolean("login");
        tv_titletoolbar.setText(getResources().getString(R.string.app_name));
        ActivityHome.getInstance().ll_next.setVisibility(View.GONE);

        ll_next.setVisibility(View.GONE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sharedprefconfig = new SharedPrefrenceConfig(context);
        tv_bnav_home.setVisibility(View.VISIBLE);
        tv_bnav_cat.setVisibility(View.GONE);
        tv_bnav_custom.setVisibility(View.GONE);
        tv_bnav_video.setVisibility(View.GONE);
        tv_bnav_post.setVisibility(View.GONE);
        tv_bnav_profile.setVisibility(View.GONE);

        iv_nav_home.setSelected(true);
        iv_nav_ctgry.setSelected(false);
        iv_nav_cstm.setSelected(false);
        iv_nav_video.setSelected(false);
        iv_nav_mypost.setSelected(false);
        iv_nav_profile.setSelected(false);

        tv_bnav_home.setSelected(true);
        tv_bnav_cat.setSelected(false);
        tv_bnav_custom.setSelected(false);
        tv_bnav_video.setSelected(false);
        tv_bnav_post.setSelected(false);
        tv_bnav_profile.setSelected(false);

        ll_bnav_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_titletoolbar.setText("Post");

                loadFragment(new FragmentMyPost());
                tv_bnav_post.setVisibility(View.VISIBLE);
                tv_bnav_video.setVisibility(View.GONE);
                tv_bnav_custom.setVisibility(View.GONE);
                tv_bnav_cat.setVisibility(View.GONE);
                tv_bnav_home.setVisibility(View.GONE);
                tv_bnav_profile.setVisibility(View.GONE);

                iv_nav_home.setSelected(false);
                iv_nav_ctgry.setSelected(false);
                iv_nav_cstm.setSelected(false);
                iv_nav_video.setSelected(false);
                iv_nav_mypost.setSelected(true);
                iv_nav_profile.setSelected(false);

                tv_bnav_home.setSelected(false);
                tv_bnav_cat.setSelected(false);
                tv_bnav_custom.setSelected(false);
                tv_bnav_video.setSelected(false);
                tv_bnav_post.setSelected(true);
                tv_bnav_profile.setSelected(false);

            }
        });

        ll_bnav_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_titletoolbar.setText("Profile");

                loadFragment(new ProfileFragment());

                tv_bnav_profile.setVisibility(View.VISIBLE);
                tv_bnav_post.setVisibility(View.GONE);
                tv_bnav_video.setVisibility(View.GONE);
                tv_bnav_custom.setVisibility(View.GONE);
                tv_bnav_cat.setVisibility(View.GONE);
                tv_bnav_home.setVisibility(View.GONE);

                iv_nav_home.setSelected(false);
                iv_nav_ctgry.setSelected(false);
                iv_nav_cstm.setSelected(false);
                iv_nav_video.setSelected(false);
                iv_nav_mypost.setSelected(false);
                iv_nav_profile.setSelected(true);

                tv_bnav_home.setSelected(false);
                tv_bnav_cat.setSelected(false);
                tv_bnav_custom.setSelected(false);
                tv_bnav_video.setSelected(false);
                tv_bnav_post.setSelected(false);
                tv_bnav_profile.setSelected(true);

            }
        });

        ll_bnav_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String plantype = sharedprefconfig.getUser().getPlan_type();

                if (!plantype.equals("Expired")) {

                    tv_titletoolbar.setText("Video");
                    loadFragment(new FragmentVideos());
                    tv_bnav_video.setVisibility(View.VISIBLE);
                    tv_bnav_post.setVisibility(View.GONE);
                    tv_bnav_custom.setVisibility(View.GONE);
                    tv_bnav_cat.setVisibility(View.GONE);
                    tv_bnav_home.setVisibility(View.GONE);
                    tv_bnav_profile.setVisibility(View.GONE);

                    iv_nav_home.setSelected(false);
                    iv_nav_ctgry.setSelected(false);
                    iv_nav_cstm.setSelected(false);
                    iv_nav_video.setSelected(true);
                    iv_nav_mypost.setSelected(false);
                    iv_nav_profile.setSelected(false);

                    tv_bnav_home.setSelected(false);
                    tv_bnav_cat.setSelected(false);
                    tv_bnav_custom.setSelected(false);
                    tv_bnav_video.setSelected(true);
                    tv_bnav_post.setSelected(false);
                    tv_bnav_profile.setSelected(false);

                } else {
                    new AlertDialog.Builder(context)
                            .setCancelable(false)
                            .setMessage("Your plan is expire so can you buy again for next 1 years.")
                            .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sharedprefconfig.logout();
                                    sharedprefconfig.clear();
                                    finishAffinity();
                                }
                            }).setNegativeButton("Buy premium plan", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context, PremiumActivity.class);
                                    context.startActivity(intent);
                                    finishAffinity();
                                }
                            }).show();

                }
            }
        });

        if (!Admin.tinyDB.getBoolean("business")) {
            Admin.Handle_activity_opener(context, BusinessSelection.class);
        }

        ll_bnav_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plantype = sharedprefconfig.getUser().getPlan_type();
                if (!plantype.equals("Expired")) {

                    tv_titletoolbar.setText("Custom");
                    // Constance.fragmentName="custom";
                    loadFragment(new CreateCustomImageFragment());
//                    loadFragment(new AllCustomFragment());

                    tv_bnav_custom.setVisibility(View.VISIBLE);
                    tv_bnav_post.setVisibility(View.GONE);
                    tv_bnav_video.setVisibility(View.GONE);
                    tv_bnav_cat.setVisibility(View.GONE);
                    tv_bnav_home.setVisibility(View.GONE);
                    tv_bnav_profile.setVisibility(View.GONE);

                    iv_nav_home.setSelected(false);
                    iv_nav_ctgry.setSelected(false);
                    iv_nav_cstm.setSelected(true);
                    iv_nav_video.setSelected(false);
                    iv_nav_mypost.setSelected(false);
                    iv_nav_profile.setSelected(false);

                    tv_bnav_home.setSelected(false);
                    tv_bnav_cat.setSelected(false);
                    tv_bnav_custom.setSelected(true);
                    tv_bnav_video.setSelected(false);
                    tv_bnav_post.setSelected(false);
                    tv_bnav_profile.setSelected(false);

                } else {

                    new AlertDialog.Builder(context)
                            .setCancelable(false)
                            .setMessage("Your plan is expire so can you buy again for next 1 years.")
                            .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sharedprefconfig.logout();
                                    sharedprefconfig.clear();
                                    finishAffinity();
                                }
                            }).setNegativeButton("Buy premium plan", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context, PremiumActivity.class);
                                    context.startActivity(intent);
                                    finishAffinity();
                                }
                            }).show();
                }
            }
        });

        ll_bnav_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_titletoolbar.setText("Select Your Business Category");
                tv_titletoolbar.setBackgroundColor(Color.parseColor("#000000"));
                loadFragment(new CategoryFragment());

                tv_bnav_cat.setVisibility(View.VISIBLE);
                tv_bnav_post.setVisibility(View.GONE);
                tv_bnav_home.setVisibility(View.GONE);
                tv_bnav_video.setVisibility(View.GONE);
                tv_bnav_custom.setVisibility(View.GONE);
                tv_bnav_profile.setVisibility(View.GONE);

                iv_nav_home.setSelected(false);
                iv_nav_ctgry.setSelected(true);
                iv_nav_cstm.setSelected(false);
                iv_nav_video.setSelected(false);
                iv_nav_mypost.setSelected(false);
                iv_nav_profile.setSelected(false);

                tv_bnav_home.setSelected(false);
                tv_bnav_cat.setSelected(true);
                tv_bnav_custom.setSelected(false);
                tv_bnav_video.setSelected(false);
                tv_bnav_post.setSelected(false);
                tv_bnav_profile.setSelected(false);

            }
        });

        ll_bnav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_titletoolbar.setText(getResources().getString(R.string.app_name));
//                loadFragment(new FragmentHome());

                startActivity(new Intent(context, ActivityHome.class));

                tv_bnav_home.setVisibility(View.VISIBLE);
                tv_bnav_post.setVisibility(View.GONE);
                tv_bnav_video.setVisibility(View.GONE);
                tv_bnav_cat.setVisibility(View.GONE);
                tv_bnav_custom.setVisibility(View.GONE);
                tv_bnav_profile.setVisibility(View.GONE);

                iv_nav_home.setSelected(true);
                iv_nav_ctgry.setSelected(false);
                iv_nav_cstm.setSelected(false);
                iv_nav_video.setSelected(false);
                iv_nav_mypost.setSelected(false);
                iv_nav_profile.setSelected(false);

                tv_bnav_home.setSelected(true);
                tv_bnav_cat.setSelected(false);
                tv_bnav_custom.setSelected(false);
                tv_bnav_video.setSelected(false);
                tv_bnav_post.setSelected(false);
                tv_bnav_profile.setSelected(false);

            }
        });

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        iv_language.setVisibility(View.GONE);
        iv_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupview_down = LayoutInflater.from(context).inflate(R.layout.category_dropdown_raw, null);
                popupWindowHelper = new PopupWindowHelper(popupview_down);
                popupWindowHelper.showAsDropDown(v, 0, 0);
                RecyclerView rv_cat_dd_raw = popupview_down.findViewById(R.id.rv_cat_dd_raw);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
                rv_cat_dd_raw.setLayoutManager(gridLayoutManager);
                rv_cat_dd_raw.setHasFixedSize(true);

                AdapterCategoryList adapterCategory = new AdapterCategoryList(context, frameName, "home");
                rv_cat_dd_raw.setAdapter(adapterCategory);
            }
        });

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

        Constance.childDataList.clear();
//        if (getuser.getPlan_type() == null || !getuser.getPlan_type().equals("Premium")) {
        if (!Constance.isPremium) {
            for_mInterstitialAd();
        } else {
            mAdView.setVisibility(View.GONE);
            facbook_ad_banner.setVisibility(View.GONE);
        }
        loadFragment(new FragmentHome());
        tv_titletoolbar.setText(R.string.app_name);
        //  getUserImage();
        iv_userimg.setVisibility(View.GONE);
        check_fragmentname = String.valueOf(getIntent().getStringExtra("check_fragmentname"));

        if (check_fragmentname.equals("fragment_mypost")) {
            tv_titletoolbar.setText("My Post");
            loadFragment(new FragmentMyPost());

            tv_bnav_home.setVisibility(View.GONE);
            tv_bnav_post.setVisibility(View.VISIBLE);
            tv_bnav_video.setVisibility(View.GONE);
            tv_bnav_cat.setVisibility(View.GONE);
            tv_bnav_custom.setVisibility(View.GONE);
            tv_bnav_profile.setVisibility(View.GONE);

            iv_nav_home.setSelected(false);
            iv_nav_ctgry.setSelected(false);
            iv_nav_cstm.setSelected(false);
            iv_nav_video.setSelected(false);
            iv_nav_mypost.setSelected(true);
            iv_nav_profile.setSelected(false);

            tv_bnav_home.setSelected(false);
            tv_bnav_cat.setSelected(false);
            tv_bnav_custom.setSelected(false);
            tv_bnav_video.setSelected(false);
            tv_bnav_post.setSelected(true);
            tv_bnav_profile.setSelected(false);
        }

        iv_menuhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });

        tv_buy_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, PremiumActivity.class);
                startActivity(i);
            }
        });

    }

    public void getprofiledetails() {
        Api api = Base_Url.getClient().create(Api.class);
        String token = sharedprefconfig.getapitoken().getApi_token();
        if (token != null && !token.equals("")) {
            Call<ResponseLogin> call = api.getProfileDetails(token);
            call.enqueue(new Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                    if (response != null && response.body() != null) {
                        ResponseLogin getprofile = response.body();
                        if (getprofile.getResult() != null && getprofile.getResult().equals("1")) {
                            if (getprofile.getRecord() != null) {
                                sharedprefconfig.saveUser(getprofile.getRecord());
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.placeholder(R.drawable.userholder);
                                requestOptions.error(R.drawable.userholder);
                                String login = "Login";
                                if (response.body().getRecord().getPlan_type().equals("Premium")) {
                                    Constance.isPremium = true;
                                    Constance.isPremium = false;
                                } else {
                                    Constance.isPremium = false;
                                }
                                sharedprefconfig.getdata();
                                if (Admin.tinyDB.getBoolean("login")) {
                                    login = ("Logout");
                                    if (sharedprefconfig.getUser().getPlan_type().equals("Expired")) {
                                        new AlertDialog.Builder(context)
                                                .setCancelable(false)
                                                .setMessage("your plan is expire so can you buy again for next 1 years.")
                                                .setPositiveButton(login, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        sharedprefconfig.logout();
                                                        sharedprefconfig.clear();
                                                        Intent intent = new Intent(context, ActivitySignIn.class);
                                                        startActivity(intent);
                                                        finishAffinity();
                                                    }
                                                }).setNegativeButton("Buy premium plan", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(context, PremiumActivity.class);
                                                        startActivity(intent);
                                                        finishAffinity();
                                                    }
                                                }).show();
                                    }
                                } else {
                                    login = ("Log In");
                                }


                            }
                        } else {
                            if (response.body().getResult().equals("0")) {
                                if (response.body().getIs_expired() != null && response.body().getIs_expired().equals("1")) {
                                    new AlertDialog.Builder(context)
                                            .setCancelable(false)
                                            .setMessage(response.body().getMessage())
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    sharedprefconfig = new SharedPrefrenceConfig(context);
                                                    sharedprefconfig.logout();
                                                    sharedprefconfig.clear();

                                                    if (Admin.tinyDB.getBoolean("login")) {
                                                        tv_nav_logout.setText("Logout");
                                                    } else {
                                                        tv_nav_logout.setText("Log In");
                                                    }

                                                    Intent intent = new Intent(context, ActivitySignIn.class);
                                                    startActivity(intent);
                                                    finishAffinity();
                                                }
                                            }).setNegativeButton("Buy premium plan", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    sharedprefconfig = new SharedPrefrenceConfig(context);
                                                    sharedprefconfig.logout();
                                                    sharedprefconfig.clear();
                                                }
                                            }).show();
                                }
                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseLogin> call, Throwable t) {
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + "Google Analytics Testing");
        mTracker.setScreenName("Image~" + "Google Analytics Testing");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        if (Admin.tinyDB.getBoolean("login")) {
            tv_nav_logout.setText("Logout");
            getprofiledetails();
        } else {
            tv_nav_logout.setText("Log In");
        }
    }

    public void bindView() {

        mAdView = findViewById(R.id.adview);
        ivBack = findViewById(R.id.ivBack);
        facbook_ad_banner = findViewById(R.id.facbook_ad_banner);
        drawerLayout = findViewById(R.id.drawerLayout);
        iv_menuhome = findViewById(R.id.iv_menuhome);
        iv_userimg = findViewById(R.id.iv_userimg);
        tv_titletoolbar = findViewById(R.id.tv_titletoolbar);
        iv_language = findViewById(R.id.iv_language);
        tv_nav_logout = findViewById(R.id.tv_nav_logout);
        tv_buy_pre = findViewById(R.id.tv_buy_pre);
        ll_bnav_home = findViewById(R.id.ll_bnav_home);
        tv_bnav_home = findViewById(R.id.tv_bnav_home);
        ll_bnav_category = findViewById(R.id.ll_bnav_category);
        tv_bnav_cat = findViewById(R.id.tv_bnav_cat);
        ll_bnav_custom = findViewById(R.id.ll_bnav_custom);
        tv_bnav_custom = findViewById(R.id.tv_bnav_custom);
        ll_bnav_video = findViewById(R.id.ll_bnav_video);
        tv_bnav_video = findViewById(R.id.tv_bnav_video);
        ll_bnav_post = findViewById(R.id.ll_bnav_post);
        tv_bnav_post = findViewById(R.id.tv_bnav_post);
        ll_bnav_profile = findViewById(R.id.ll_bnav_profile);
        tv_bnav_profile = findViewById(R.id.tv_bnav_profile);
        ll_next = findViewById(R.id.ll_next);

        iv_nav_home = findViewById(R.id.iv_nav_home);
        iv_nav_ctgry = findViewById(R.id.iv_nav_ctgry);
        iv_nav_cstm = findViewById(R.id.iv_nav_cstm);
        iv_nav_video = findViewById(R.id.iv_nav_video);
        iv_nav_mypost = findViewById(R.id.iv_nav_mypost);
        iv_nav_profile = findViewById(R.id.iv_nav_profile);
        mAdView.setVisibility(View.GONE);

    }

    public void loadFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_framelayout, fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

    }

    public void getUserImage() {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.userholder);
        requestOptions.error(R.drawable.userholder);
        // Log.d("checkimage","getUserImage"+sharedprefconfig.getUser().getImage());
        Glide.with(context).load(sharedprefconfig.getUser().getImage_url()).apply(requestOptions).into(iv_userimg);
    }

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

    public void onClickHomeActivity(View view) {
        SharedPrefrenceConfig pref = new SharedPrefrenceConfig(context);
        RecordRegister getuser = pref.getUser();
        switch (view.getId()) {

            case R.id.rl_nav_home:
                tv_titletoolbar.setText(R.string.app_name);

                startActivity(new Intent(context, ActivityHome.class));
                if (!Constance.isPremium) {
                    if (Constance.AllowToOpenAdvertise) {
                        if (Constance.adType.equals("Ad Mob")) {
                            interstitialAdMobAd();
                            Log.d("ADssss", "Ad Mob");
                        } else {
                            interstitialFacbookAd();
                            Log.d("ADssss", "Facebook");
                        }
                    }
                }
                drawerLayout.closeDrawers();
                break;
            case R.id.rl_nav_video:
                tv_titletoolbar.setText("Video");
                if (!Constance.isPremium) {
                    if (Constance.adType.equals("Ad Mob")) {
                        interstitialAdMobAd();
                        Log.d("ADssss", "Ad Mob");
                    } else {
                        interstitialFacbookAd();
                        Log.d("ADssss", "Facebook");
                    }
                }

                Intent i = new Intent(context, ActivityViewAllList.class);
                i.putExtra("parentname", "Video");
                startActivity(i);
                drawerLayout.closeDrawers();
                break;
            case R.id.rl_nav_privacypolicy:
//                loadPrivacy();
                drawerLayout.closeDrawers();
                tv_titletoolbar.setText("Privacy Policy");
                loadFragment(new FragmentPolicy());
                break;
            case R.id.rl_nav_myprofile:
                tv_titletoolbar.setText(getResources().getString(R.string.app_name));
                drawerLayout.closeDrawers();

                if (!Constance.isPremium) {
                    if (Constance.AllowToOpenAdvertise) {
                        if (Constance.adType.equals("Ad Mob")) {
                            interstitialAdMobAd();
                            Log.d("ADssss", "Ad Mob");
                        } else {
                            interstitialFacbookAd();
                            Log.d("ADssss", "Facebook");
                        }
                    }
                }
                loadFragment(new ProfileFragment());

                break;
            case R.id.rl_nav_logout:
                if (Admin.tinyDB.getBoolean("login")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are You sure you Want To Logout ? ")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SharedPrefrenceConfig.savebooleanPreferance(context, "checkLogin", false);
                                    sharedprefconfig.saveStringPreferance(context, Constance.CToken, "");
                                    sharedprefconfig.logout();
                                    sharedprefconfig.clear();
                                    Admin.tinyDB.putBoolean("login", false);
                                    Admin.tinyDB.clear();
                                    finishAffinity();
                                    Intent i = new Intent(context, ActivitySplashScreen.class);
                                    startActivity(i);
                                    tv_nav_logout.setText("Logout");
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    drawerLayout.closeDrawers();
                                }
                            });
                    //Creating dialog box
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

                } else {
                    tv_nav_logout.setText("Login");
                    Intent intent = new Intent(context, ActivitySignIn.class);
                    startActivity(intent);
                    finish();
                }
                drawerLayout.closeDrawers();
                break;
            case R.id.rl_nav_contactUs:
                tv_titletoolbar.setText("Contact Us");
                loadFragment(new FragmentContactUs());

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
                drawerLayout.closeDrawers();
                break;
            case R.id.rl_nav_mygallery:
                tv_titletoolbar.setText("My Post");
//                loadFragment(new FragmentMyPost());
                Intent ipost = new Intent(context, ActivityHome.class);
                ipost.putExtra("check_fragmentname", "fragment_mypost");
                startActivity(ipost);

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
                drawerLayout.closeDrawers();
                break;
            case R.id.rl_addbusiness:
                tv_titletoolbar.setText("Add Business");
//        if (getuser.getPlan_type() == null || !getuser.getPlan_type().equals("Premium")) {
                if (!Constance.isPremium) {
                    if (Constance.AllowToOpenAdvertise) {
                        if (Constance.adType.equals("Ad Mob")) {
                            interstitialAdMobAd();
                            Log.d("ADssss", "Ad Mob");
                        } else {
                            interstitialFacbookAd();
                            Log.d("ADssss", "Facebook");
                        }
                    }
                }

                if (Admin.tinyDB.getBoolean("login")) {
                    loadFragment(new FragmentAddBusiness());
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("you need a account to continue ... ")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent i = new Intent(context, ActivitySignIn.class);
                                    startActivity(i);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    drawerLayout.closeDrawers();
                                }
                            });
                    //Creating dialog box
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

                drawerLayout.closeDrawers();
                break;
              /*  case R.id.rl_editbusiness:
                    tv_titletoolbar.setText("Edit Business");
                    loadFragment(new FragmentEditBusiness());

                        if (Constance.adType.equals("Ad Mob")) {
                            interstitialAdMobAd();
                            Log.d("ADssss", "Ad Mob");
                        } else {
                            interstitialFacbookAd();
                            Log.d("ADssss", "Facebook");
                        }

                    drawerLayout.closeDrawers();
                break;*/
            case R.id.ll_shareapp:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, Constance.shareapp_url + context.getPackageName());
                intent.setType("text/plain");
                context.startActivity(intent);
                drawerLayout.closeDrawers();
                break;
            case R.id.ll_rateapp:
                try {
                    Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                    Intent intentrate = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intentrate);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(Constance.rateapp)));
                }
                drawerLayout.closeDrawers();
                break;
            case R.id.ll_moreapp:
                if (Constance.isConnected(context)) {
                    String urlStr = Constance.moreAppurl + context.getPackageName();
                    Intent inMoreapp = new Intent(Intent.ACTION_VIEW, Uri.parse(urlStr));
                    if (urlStr != null && Constance.isAvailable(inMoreapp, context)) {
                        startActivity(inMoreapp);
                    } else {
                        Toast.makeText(context, "There is no app availalbe for this task", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawers();
                break;
            case R.id.rl_nav_aboutus:
                tv_titletoolbar.setText("About Us");
                loadFragment(new FragmentAboutUs());
//        if (getuser.getPlan_type() == null || !getuser.getPlan_type().equals("Premium")) {
                if (!Constance.isPremium) {
                    if (Constance.AllowToOpenAdvertise) {
                        if (Constance.adType.equals("Ad Mob")) {
                            interstitialAdMobAd();
                            Log.d("ADssss", "Ad Mob");
                        } else {
                            interstitialFacbookAd();
                            Log.d("ADssss", "Facebook");
                        }
                    }
                }
                drawerLayout.closeDrawers();
                break;
            case R.id.rl_nav_feedback:
                tv_titletoolbar.setText("FeedBack");
//        if (getuser.getPlan_type() == null || !getuser.getPlan_type().equals("Premium")) {
                if (!Constance.isPremium) {
                    if (Constance.AllowToOpenAdvertise) {
                        if (Constance.adType.equals("Ad Mob")) {
                            interstitialAdMobAd();
                            Log.d("ADssss", "Ad Mob");
                        } else {
                            interstitialFacbookAd();
                            Log.d("ADssss", "Facebook");
                        }
                    }
                }
                if (Admin.tinyDB.getBoolean("login")) {
                    loadFragment(new FeedbackFragment());


                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("you need a account to continue ... ")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent i = new Intent(context, ActivitySignIn.class);
                                    startActivity(i);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    drawerLayout.closeDrawers();
                                }
                            });
                    //Creating dialog box
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

                drawerLayout.closeDrawers();

                break;


        }
    }

    public void loadPrivacy() {
        if (Constance.isConnected(ActivityHome.this)) {
            Constance.privacyPolicy(ActivityHome.this);
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void for_mInterstitialAd() {
        //// AdMob Ad-------------------------


//        mInterstitialAd = new InterstitialAd(context);
//        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_full_screen));
//        mInterstitialAd.loadAd(new AdRequest.Builder()
//                .addTestDevice("6A2D2B68A7166B0DE00868C6F74E8DB9")
//                .addTestDevice("88045C0A4BBC3C24FABBF3D543FC7C8C")
//                .addTestDevice("3BCC9944F0D7A19C3D3BEFCD7D8B3EDE")
//                .addTestDevice("D3662558A58B055494404223B20E0CA8")
//                .build());
//
//        if (mInterstitialAd != null) {
//            if (mInterstitialAd.isLoaded()) {
//                mInterstitialAd.show();
//            }
//        }
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                /*if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                }*/
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//
//                Constance.AllowToOpenAdvertise = false;
//
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//                Constance.AllowToOpenAdvertise = false;
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//                Constance.AllowToOpenAdvertise = false;
//
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when when the interstitial ad is closed.
//                Constance.AllowToOpenAdvertise = false;
//
//
//                mInterstitialAd.loadAd(new AdRequest.Builder()
//                        .addTestDevice("6A2D2B68A7166B0DE00868C6F74E8DB9")
//                        .addTestDevice("88045C0A4BBC3C24FABBF3D543FC7C8C")
//                        .addTestDevice("3BCC9944F0D7A19C3D3BEFCD7D8B3EDE")
//                        .addTestDevice("D3662558A58B055494404223B20E0CA8")
//                        .build());
//            }
//        });


        AudienceNetworkAds.initialize(this);


        //AdSettings.setIntegrationErrorMode(INTEGRATION_ERROR_CRASH_DEBUG_MODE);
        // Toast.makeText(ActivityNewsPapersCategory.this,"id"+getResources().getString(R.string.facebook_interstitial_Ad),Toast.LENGTH_LONG).show();
        interstitialFacbookAd = new com.facebook.ads.InterstitialAd(this, getResources().getString(R.string.facebook_interstitial_Ad));
        AdSettings.setDebugBuild(true);
        // AdSettings.addTestDevice("HASHED ID");
        interstitialFacbookAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialFacbookAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });
    }

    public void interstitialAdMobAd() {
//        if (!Constance.isPremium) {
//            if (ActivityHome.getInstance().mInterstitialAd != null) {
//                if (ActivityHome.getInstance().mInterstitialAd.isLoaded()) {
//                    Log.d("shsjks", "sdhsjkhd");
//                    ActivityHome.getInstance().mInterstitialAd.show();
//                } else {
//                }
//            } else {
//
//            }
//        }
    }

    public void interstitialFacbookAd() {
        if (!Constance.isPremium) {
            if (ActivityHome.getInstance().interstitialFacbookAd != null) {
                if (!ActivityHome.getInstance().interstitialFacbookAd.isAdLoaded()) {

                    AdSettings.setDebugBuild(true);
                    ActivityHome.getInstance().interstitialFacbookAd.loadAd();
                } else {

                }
            } else {

            }
        }
    }

    public void StartTimer() {
        timer = new Timer();
        hourlyTask = new TimerTask() {
            @Override
            public void run() {
                if (!Constance.isFirstTimeOpen) {
                    Constance.AllowToOpenAdvertise = true;
                    stopTask();
                } else {
                    Constance.isFirstTimeOpen = false;
                }
            }
        };

        Constance.isFirstTimeOpen = true;
        if (timer != null) {
            timer.schedule(hourlyTask, 0, 1000 * 60);
        }
    }

    public void stopTask() {
        if (hourlyTask != null) {

            Log.d("TIMER", "timer canceled");
            hourlyTask.cancel();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("abc", "createquote");
        if (Constance.checkFragment.equals("My_Profile")) {
            FragmentMyProfile.getInstance().onActivityResult(requestCode, resultCode, data);
        } else if (Constance.checkFragment.equals("Add_Business")) {
            FragmentAddBusiness.getInstance().onActivityResult(requestCode, resultCode, data);
        } else if (Constance.checkFragment.equals("CreateCustom")) {
            CreateCustomImageFragment.getInstance().onActivityResult(requestCode, resultCode, data);
        }


     /*   if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
               FragmentAddBusiness.getInstance().iv_selectedlogo.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }*/


    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            String title = "Close App";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorTheame_text));
            // Initialize a new spannable string builder instance
            SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);
            // Apply the text color span
            ssBuilder.setSpan(
                    foregroundColorSpan,
                    0,
                    title.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            builder.setTitle(ssBuilder);
            builder.setMessage("Are you sure you want to close this App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }

            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create();
            AlertDialog dialog = builder.create();
//        alert.setTitle( Html.fromHtml("<font color='#FF7F27'>Hello World</font>"));
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {

              /*  int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                TextView tv = dialog.findViewById(textViewId); // It always returns null
                if (tv != null) {
                    tv.setTextColor(getResources().getColor(R.color.colorTheame_text));
                    tv.setBackgroundColor(getResources().getColor(R.color.colorTheame_gray2));
                }*/
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                    //dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black));
                }
            });
            dialog.show();
        }

    }


}