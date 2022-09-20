package com.festivalbanner.digitalposterhub.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.festivalbanner.digitalposterhub.ModelRetrofit.ResponseLogin;
import com.festivalbanner.digitalposterhub.ModelRetrofit.UserRegister;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
import com.festivalbanner.digitalposterhub.Activities.ActivityHome;
import com.festivalbanner.digitalposterhub.Activities.ActivitySignIn;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Utills.Constance;
import com.festivalbanner.digitalposterhub.Utills.PathUtills;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
import com.google.android.gms.analytics.Tracker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.net.URISyntaxException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class FragmentMyProfile extends Fragment {

    Context context;
    View view;
    EditText et_username_myprofile, et_email_myprofile, et_password_myprofile, et_city_myprofile, et_contactno;
    LinearLayout ll_Update_btn;
    String imagepath;
    SharedPrefrenceConfig sharedprefconfig;
    ImageView iv_profile_person_myprofile;
    String[] permissionsRequired = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private static final int PERMISSION_CALLBACK_CONSTANT = 200;
    Tracker mTracker;
    MultipartBody.Part image_multipart1;
    ProgressBar pb_profile;
    TextView tv_usernm,tv_contct;
    ImageView iv_prof_edit;

    public static FragmentMyProfile instance = null;

    private final String TAG = FragmentMyProfile.class.getSimpleName();

    public FragmentMyProfile() {
        instance = FragmentMyProfile.this;
    }

    public static synchronized FragmentMyProfile getInstance() {
        if (instance == null) {
            instance = new FragmentMyProfile();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_myprofile, container, false);
        context = getContext();
        Constance.checkFragment = "My_Profile";
        sharedprefconfig = new SharedPrefrenceConfig(context);
        bindView();
        pb_profile.setVisibility(View.VISIBLE);
        ActivityHome.getInstance().ivBack.setVisibility(View.VISIBLE);
        if (sharedprefconfig.getPrefBoolean(context, "checkLogin", true)) {

            Log.d("dsjfjfjjfdsj", "djsfajj");
            getprofiledetails();
        } else {
            Intent i = new Intent(context, ActivitySignIn.class);
            startActivity(i);
        }
        ll_Update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
        iv_profile_person_myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                } else {
                    PickImageFromMobileGallery();
                }
            }
        });
        return view;
    }

    public void bindView() {
        iv_profile_person_myprofile = view.findViewById(R.id.iv_profile_person_myprofile);
        et_username_myprofile = view.findViewById(R.id.et_username_myprofile);
        et_email_myprofile = view.findViewById(R.id.et_email_myprofile);
        et_password_myprofile = view.findViewById(R.id.et_password_myprofile);
        et_city_myprofile = view.findViewById(R.id.et_city_myprofile);
        et_contactno = view.findViewById(R.id.et_contactno);
        ll_Update_btn = view.findViewById(R.id.ll_Update_btn);
        pb_profile = view.findViewById(R.id.pb_profile);
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
                            sharedprefconfig.saveUser(getprofile.getRecord());
                            if (getprofile.getRecord() != null) {
                                pb_profile.setVisibility(View.GONE);
                                et_email_myprofile.setText("" + response.body().getRecord().getEmail());
                                et_username_myprofile.setText("" + response.body().getRecord().getName());
                                et_password_myprofile.setText("");
                                et_city_myprofile.setText("" + response.body().getRecord().getCity());
                                et_contactno.setText("" + response.body().getRecord().getContact());
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.placeholder(R.drawable.userholder);
                                requestOptions.error(R.drawable.userholder);
                                Glide.with(context).load(getprofile.getRecord().getImage_url()).apply(requestOptions).into(iv_profile_person_myprofile);

                            } else {
                                pb_profile.setVisibility(View.GONE);
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("record null : " + response.body().getMessage())
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                et_city_myprofile.setText("");
                                                et_password_myprofile.setText("");
                                                et_email_myprofile.setText("");
                                                et_username_myprofile.setText("");
                                                Glide.with(context).load(R.drawable.userholder).into(iv_profile_person_myprofile);

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

                        } else {
                            pb_profile.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(response.body().getMessage())
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            et_city_myprofile.setText("");
                                            et_password_myprofile.setText("");
                                            et_email_myprofile.setText("");
                                            et_username_myprofile.setText("");
                                            Glide.with(context).load(R.drawable.userholder).into(iv_profile_person_myprofile);

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

                }

                @Override
                public void onFailure(Call<ResponseLogin> call, Throwable t) {
                    pb_profile.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "check Network Connection", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void updateProfile() {
        MultipartBody.Part multipart_updateuser;
      /*if (imagepath != null && !imagepath.equals("")) {
          File file = new File(imagepath);
          // Create a request body with file and image media type
          RequestBody rbImage = RequestBody.create(MediaType.parse("multipart/form-data"), file);
          image_multipart1 = MultipartBody.Part.createFormData("image", file.getName(), rbImage);
          Log.d("checkimage","image_multipart :"+String.valueOf(image_multipart1));

          //Log.e("file_name", String.valueOf(image_multipart));
      }
      else {
          Toast.makeText(context, "Image Not Update", Toast.LENGTH_SHORT).show();
          // Create a request body with file and image media type
          RequestBody rbImage = RequestBody.create(MultipartBody.FORM, "");
          // Create MultipartBody.Part using file request-body,file name and part name
          image_multipart1 = MultipartBody.Part.createFormData("image", "", rbImage);
      }*/
        if (imagepath != null && !imagepath.equals("")) {
            File file = new File(imagepath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            multipart_updateuser =
                    MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        } else {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), "");
            multipart_updateuser =
                    MultipartBody.Part.createFormData("image", "", requestFile);
        }

        RequestBody rbToken = RequestBody.create(MediaType.parse("text/plain"), sharedprefconfig.getapitoken().getApi_token());
        RequestBody rbname = RequestBody.create(MediaType.parse("text/plain"), et_username_myprofile.getText().toString());
        RequestBody rbcity = RequestBody.create(MediaType.parse("text/plain"), et_city_myprofile.getText().toString());
        RequestBody rbpassword = RequestBody.create(MediaType.parse("text/plain"), et_password_myprofile.getText().toString());
        RequestBody rbemail = RequestBody.create(MediaType.parse("text/plain"), et_email_myprofile.getText().toString());

        Api api = Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Call<UserRegister> call = api.updateProfile(rbToken, rbname, rbemail, multipart_updateuser, rbcity);
        //Call<UserRegister> call=api.updateProfile(sharedprefconfig.getapitoken().getApi_token(),et_username_myprofile.getText().toString(),et_email_myprofile.getText().toString(),et_city_myprofile.getText().toString(),imagepath) ;

        call.enqueue(new Callback<UserRegister>() {
            @Override
            public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
                if (response != null && response.body() != null) {
                    UserRegister userResponse = response.body();
                    Log.d("userresponse", "" + userResponse);
                    if (userResponse.getResult() != null && userResponse.getResult().equals("1")) {
                        sharedprefconfig.saveUser(userResponse.getRecord());
                        if (userResponse.getRecord() != null) {
                            // sharedprefconfig.saveUser(userResponse.getRecord());

                            progressDialog.dismiss();
                            Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();

                            Intent i = new Intent(context, ActivityHome.class);
                            startActivity(i);
                        } else {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(response.body().getMessage())
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
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

                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserRegister> call, Throwable t) {
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                Log.d("error_Throwable", "" + t);
                builder.setMessage("No Internet Connection")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
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
        });
    }

    public void checkPermission() {

        if (ActivityCompat.checkSelfPermission(context, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[2])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        getActivity().onBackPressed();
                    }
                });
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
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
        }
    }

    public void PickImageFromMobileGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
//                .setAspectRatio(1, 1)
                .start(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("abc", "createquote");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    imagepath = PathUtills.getPath(context, resultUri);
                    Log.d("dfjdfjj", "djdfis" + imagepath);
                    Log.d("dfjdfjj", "djdfis" + resultUri);
                    Glide.with(context).load(imagepath).into(iv_profile_person_myprofile);

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
      /*  if(sharedprefconfig.getPrefBoolean(context, "checkLogin", true)) {
            getprofiledetails();
        } else {
            Intent i = new Intent(context, ActivitySignIn.class);
            startActivity(i);
        }*/


    }


}
