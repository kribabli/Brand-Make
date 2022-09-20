package com.festivalbanner.digitalposterhub.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.festivalbanner.digitalposterhub.Activities.ActivityHome;
import com.festivalbanner.digitalposterhub.Activities.ActivitySignIn;
import com.festivalbanner.digitalposterhub.Activities.ActivitySplashScreen;
import com.festivalbanner.digitalposterhub.Activities.AfterBusinessSelection;
import com.festivalbanner.digitalposterhub.Activities.BusinessSelection;
import com.festivalbanner.digitalposterhub.Activities.PremiumActivity;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Utills.Admin;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;

public class ProfileFragment extends Fragment {
    Context context;

    Button tv_buy_pre;

    TextView tv_nav_logout;
    SharedPrefrenceConfig sharedprefconfig;
    RelativeLayout rl_addbusiness, rl_nav_myprofile, rl_nav_mygallery, rl_nav_privacypolicy, rl_nav_aboutus, rl_nav_contactUs,
            rl_nav_feedback, rl_nav_logout, ll_shareapp, ll_rateapp, ll_moreapp;
    LinearLayout sharedCombinationLinearLayout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = getActivity();

        sharedprefconfig = new SharedPrefrenceConfig(context);
        ActivityHome.getInstance().ivBack.setVisibility(View.VISIBLE);
        ActivityHome.getInstance().ll_next.setVisibility(View.GONE);
        ActivityHome.getInstance().iv_menuhome.setVisibility(View.GONE);

        tv_buy_pre = view.findViewById(R.id.tv_buy_pre);
        rl_addbusiness = view.findViewById(R.id.rl_addbusiness);
        rl_nav_mygallery = view.findViewById(R.id.rl_nav_mygallery);
        rl_nav_aboutus = view.findViewById(R.id.rl_nav_aboutus);
        rl_nav_feedback = view.findViewById(R.id.rl_nav_feedback);
        rl_nav_privacypolicy = view.findViewById(R.id.rl_nav_privacypolicy);
        rl_nav_myprofile = view.findViewById(R.id.rl_nav_myprofile);
        rl_nav_logout = view.findViewById(R.id.rl_nav_logout);
        rl_nav_contactUs = view.findViewById(R.id.rl_nav_contactUs);
        ll_shareapp = view.findViewById(R.id.ll_shareapp);
        ll_rateapp = view.findViewById(R.id.ll_rateapp);
        ll_moreapp = view.findViewById(R.id.ll_moreapp);
        tv_nav_logout = view.findViewById(R.id.tv_nav_logout);
//        sharedCombinationLinearLayout=view.findViewById(R.id.sharedCombinationLinearLayout);


        tv_buy_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, PremiumActivity.class);
                startActivity(i);
            }
        });

        rl_addbusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHome.getInstance().tv_titletoolbar.setText("Add Business");
                if (Constance.AllowToOpenAdvertise) {
                    if (Constance.adType.equals("Ad Mob")) {
                        ActivityHome.getInstance().interstitialAdMobAd();
                        Log.d("ADssss", "Ad Mob");
                    } else {
                        ActivityHome.getInstance().interstitialFacbookAd();
                        Log.d("ADssss", "Facebook");
                    }
                }
                if (Admin.tinyDB.getBoolean("login")) {
                    if (Admin.tinyDB.getBoolean("business")) {
                        Admin.Handle_activity_opener(context, AfterBusinessSelection.class);
                    } else {
                        Admin.Handle_activity_opener(context, BusinessSelection.class);
                    }

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

                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.show();

                }

            }
        });

        rl_nav_mygallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHome.getInstance().tv_titletoolbar.setText("My Post");
//                loadFragment(new FragmentMyPost());
                Intent i = new Intent(context, ActivityHome.class);
                i.putExtra("check_fragmentname", "fragment_mypost");
                startActivity(i);
                if (Constance.adType.equals("Ad Mob")) {
                    ActivityHome.getInstance().interstitialAdMobAd();
                    Log.d("ADssss", "Ad Mob");
                } else {
                    ActivityHome.getInstance().interstitialFacbookAd();
                    Log.d("ADssss", "Facebook");
                }

            }
        });
        rl_nav_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHome.getInstance().tv_titletoolbar.setText("About Us");
                loadFragment(new FragmentAboutUs());
                if (Constance.AllowToOpenAdvertise) {
                    if (Constance.adType.equals("Ad Mob")) {
                        ActivityHome.getInstance().interstitialAdMobAd();
                        Log.d("ADssss", "Ad Mob");
                    } else {
                        ActivityHome.getInstance().interstitialFacbookAd();
                        Log.d("ADssss", "Facebook");
                    }
                }

            }
        });

        rl_nav_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHome.getInstance().tv_titletoolbar.setText("FeedBack");
                if (Constance.AllowToOpenAdvertise) {
                    if (Constance.adType.equals("Ad Mob")) {
                        ActivityHome.getInstance().interstitialAdMobAd();
                        Log.d("ADssss", "Ad Mob");
                    } else {
                        ActivityHome.getInstance().interstitialFacbookAd();
                        Log.d("ADssss", "Facebook");
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

                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.show();

                }

            }
        });
        rl_nav_privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityHome.getInstance().loadPrivacy();
                ActivityHome.getInstance().tv_titletoolbar.setText("Privacy Policy");
//                tv_titletoolbar.setText("Privacy Policy");
                loadFragment(new FragmentPolicy());
            }
        });
        rl_nav_myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constance.AllowToOpenAdvertise) {

                    if (Constance.adType.equals("Ad Mob")) {
                        ActivityHome.getInstance().interstitialAdMobAd();
                        Log.d("ADssss", "Ad Mob");
                    } else {
                        ActivityHome.getInstance().interstitialFacbookAd();
                        Log.d("ADssss", "Facebook");
                    }
                }
                if (Admin.tinyDB.getBoolean("login")) {
                    ActivityHome.getInstance().tv_titletoolbar.setText("Profile");
                    loadFragment(new FragmentMyProfile());

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

                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        if (Admin.tinyDB.getBoolean("login")) {
            tv_nav_logout.setText("Logout");
        } else {
            tv_nav_logout.setText("Login");
        }
        rl_nav_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Admin.tinyDB.getBoolean("login")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setMessage("Are You Want To Logout ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SharedPrefrenceConfig.savebooleanPreferance(context, "checkLogin", false);
                                    SharedPrefrenceConfig.saveStringPreferance(context, Constance.CToken, "");
                                    sharedprefconfig.logout();
                                    sharedprefconfig.clear();
                                    tv_nav_logout.setText("Login");
                                    Admin.tinyDB.putBoolean("login", false);
                                    Admin.tinyDB.clear();
                                    dialog.cancel();
                                    Intent i = new Intent(context, ActivitySplashScreen.class);
                                    startActivity(i);
                                    ((FragmentActivity) context).finishAffinity();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
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
                }
            }
        });
        rl_nav_contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHome.getInstance().tv_titletoolbar.setText("Contact Us");
                loadFragment(new FragmentContactUs());
                if (Constance.adType.equals("Ad Mob")) {
                    ActivityHome.getInstance().interstitialAdMobAd();
                    Log.d("ADssss", "Ad Mob");
                } else {
                    ActivityHome.getInstance().interstitialFacbookAd();
                    Log.d("ADssss", "Facebook");
                }

            }
        });
        ll_shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, Constance.shareapp_url + context.getPackageName());
                intent.setType("text/plain");
                context.startActivity(intent);

            }
        });
        ll_rateapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                    Intent intentrate = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intentrate);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(Constance.rateapp)));
                }
            }
        });
        ll_moreapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        return view;
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_framelayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null);
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}