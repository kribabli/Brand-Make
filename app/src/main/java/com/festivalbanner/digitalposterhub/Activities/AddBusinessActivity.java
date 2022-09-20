package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.festivalbanner.digitalposterhub.ModelRetrofit.CompanyDetails;
import com.festivalbanner.digitalposterhub.ModelRetrofit.ResponseLogin;
import com.festivalbanner.digitalposterhub.Fragments.FragmentAddBusiness;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Retrofit.Api;
import com.festivalbanner.digitalposterhub.Retrofit.Base_Url;
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

public class AddBusinessActivity extends AppCompatActivity {


    Context context;
    View view;
    Tracker mTracker;
    LinearLayout ll_logo, ll_finish_btn, ll_update_btn;
    public ImageView iv_selectedlogo;
    EditText et_businessaddress, et_businesswebsite, et_businessemail, et_businessmobile, et_businessname;
    String[] permissionsRequired = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    SharedPrefrenceConfig sharedprefconfig;

    MultipartBody.Part multipart_image, multipart_updateimage;
    String imagepath;
    String companyid;
    private static final int PERMISSION_CALLBACK_CONSTANT = 200;
    public static FragmentAddBusiness instance = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);

        context = AddBusinessActivity.this;
        bindView();
        sharedprefconfig = new SharedPrefrenceConfig(context);
        Constance.checkFragment = "Add_Business";
        hideKeyboard(AddBusinessActivity.this);
        ll_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                } else {
                    pickFromGallery();
                }
            }
        });
        ll_finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBusiness();
            }
        });
        ll_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateBusiness();
            }
        });
        // getbusinessdetail();
    }


    public void bindView() {
        ll_logo = findViewById(R.id.ll_logo);
        iv_selectedlogo = findViewById(R.id.iv_selectedlogo);
        ll_finish_btn = findViewById(R.id.ll_finish_btn);
        et_businessaddress = findViewById(R.id.et_businessaddress);
        et_businesswebsite = findViewById(R.id.et_businesswebsite);
        et_businessemail = findViewById(R.id.et_businessemail);
        et_businessmobile = findViewById(R.id.et_businessmobile);
        et_businessname = findViewById(R.id.et_businessname);
        ll_update_btn = findViewById(R.id.ll_update_btn);

    }

    public void UpdateBusiness() {
        MultipartBody.Part multipart_updateimage;
        if (imagepath != null && !imagepath.equals("")) {
            //pass it like this
            File file = new File(imagepath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            multipart_updateimage =
                    MultipartBody.Part.createFormData("logo", file.getName(), requestFile);

        } else {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), "");

            multipart_updateimage =
                    MultipartBody.Part.createFormData("logo", "", requestFile);

        }

        RequestBody rbToken = RequestBody.create(MediaType.parse("text/plain"), sharedprefconfig.getapitoken().getApi_token());

        RequestBody rbname = RequestBody.create(MediaType.parse("text/plain"), et_businessname.getText().toString());
        RequestBody rbaddress = RequestBody.create(MediaType.parse("text/plain"), et_businessaddress.getText().toString());
        RequestBody rbmobile = RequestBody.create(MediaType.parse("text/plain"), et_businessmobile.getText().toString());
        RequestBody rbemail = RequestBody.create(MediaType.parse("text/plain"), et_businessemail.getText().toString());
        RequestBody rbwebsite = RequestBody.create(MediaType.parse("text/plain"), et_businesswebsite.getText().toString());
        RequestBody rbcompanyid = RequestBody.create(MediaType.parse("text/plain"), companyid);

        Api api = Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Log.d("companyid", "" + companyid);
        Call<CompanyDetails> call = api.updateBusinessDetails(rbToken, rbname, rbemail, multipart_updateimage, rbaddress, rbmobile, rbcompanyid, rbwebsite);

        //Call<CompanyDetails> call=api.updateBusinessDetails(sharedprefconfig.getapitoken().getApi_token(),et_businessname.getText().toString(),et_businessemail.getText().toString(),et_businessaddress.getText().toString(),et_businessmobile.getText().toString(),imagepath,companyid) ;
        Log.d("getLogo", "UpdateBusiness imagepath: " + imagepath);

        // Log.d("checkimagepath","UpdateBusiness : "+imagepath);
        call.enqueue(new Callback<CompanyDetails>() {
            @Override
            public void onResponse(Call<CompanyDetails> call, Response<CompanyDetails> response) {
                if (response !=null && response.body()!=null) {
                    CompanyDetails companyDetails = response.body();
                    Log.d("userresponse", "" + companyDetails);
                    if (companyDetails.getResult().equals("1")) {
                        if (companyDetails.getRecord() != null) {
                            Log.d("getLogo", "UpdateBusiness : " + companyDetails.getRecord().getLogo());
                            Log.d("getLogo", "UpdateBusiness url : " + companyDetails.getRecord().getLogo_url());
                            Log.d("getLogo", "UpdateBusiness Name  : " + companyDetails.getRecord().getName());

                            Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            getbusinessdetail();

                        } else {
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
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
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
            public void onFailure(Call<CompanyDetails> call, Throwable t) {
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Check Your Internet Connection" + t)
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
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                    }
                });
                dialog.show();
            }
        });
    }

    public void getbusinessdetail() {
        Api api = Base_Url.getClient().create(Api.class);

        Call<ResponseLogin> call = api.getBusinessDetails(sharedprefconfig.getapitoken().getApi_token());
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if (response !=null && response.body()!=null) {
                    ResponseLogin getprofile = response.body();
                    if (getprofile.getResult() != null && getprofile.getResult().equals("1")) {
                        if (getprofile.getRecord() != null) {
                            if (getprofile.getRecord().getCompany() != null) {
                                et_businessemail.setText("" + getprofile.getRecord().getCompany().getEmail());
                                et_businessname.setText("" + getprofile.getRecord().getCompany().getName());
                                et_businessmobile.setText("" + getprofile.getRecord().getCompany().getContact());
                                et_businessaddress.setText("" + getprofile.getRecord().getCompany().getAddress());
                                et_businesswebsite.setText("" + getprofile.getRecord().getCompany().getWebsite());
                                companyid = getprofile.getRecord().getCompany().getId();
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.placeholder(R.drawable.userholder);
                                requestOptions.error(R.drawable.userholder);
                                Glide.with(context).load(getprofile.getRecord().getCompany().getLogo_url()).apply(requestOptions).into(iv_selectedlogo);
                                Log.d("getLogo", "getbusinessdetail" + getprofile.getRecord().getCompany().getLogo());
                                Log.d("getLogo", "getbusinessdetail url" + getprofile.getRecord().getCompany().getLogo_url());
                                ll_update_btn.setVisibility(View.VISIBLE);

                                onBackPressed();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("Business Details Not Added")
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                ll_update_btn.setVisibility(View.GONE);
                                                ll_finish_btn.setVisibility(View.VISIBLE);

                                                et_businessemail.setText("");
                                                et_businessname.setText("");
                                                et_businessmobile.setText("");
                                                et_businessaddress.setText("");
                                                Glide.with(context).load(R.drawable.userholder).into(iv_selectedlogo);

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
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("record null : " + response.body().getMessage())
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            et_businessemail.setText("");
                                            et_businessname.setText("");
                                            et_businessmobile.setText("");
                                            et_businessaddress.setText("");
                                            Glide.with(context).load(R.drawable.userholder).into(iv_selectedlogo);

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

                    } else {
                        Intent i = new Intent(context, ActivitySignIn.class);
                        startActivity(i);
                        ActivityHome.getInstance().finish();

                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Toast.makeText(context, "check Network Connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void PickImageFromMobileGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)

                .start(AddBusinessActivity.this);

    }
    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK)
                .setType("image/*");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(intent, 10);
    }
    public void addBusiness() {
        MultipartBody.Part multipart_addimage;
        if (imagepath != null && !imagepath.equals("")) {
            File file = new File(imagepath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            multipart_addimage =
                    MultipartBody.Part.createFormData("logo", file.getName(), requestFile);

        } else {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), "");
            multipart_addimage =
                    MultipartBody.Part.createFormData("logo", "", requestFile);

        }

        RequestBody rbToken = RequestBody.create(MediaType.parse("text/plain"), sharedprefconfig.getapitoken().getApi_token());

        RequestBody rbname = RequestBody.create(MediaType.parse("text/plain"), et_businessname.getText().toString());
        RequestBody rbaddress = RequestBody.create(MediaType.parse("text/plain"), et_businessaddress.getText().toString());
        RequestBody rbmobile = RequestBody.create(MediaType.parse("text/plain"), et_businessmobile.getText().toString());
        RequestBody rbemail = RequestBody.create(MediaType.parse("text/plain"), et_businessemail.getText().toString());
        RequestBody rbwebsite = RequestBody.create(MediaType.parse("text/plain"), et_businesswebsite.getText().toString());

        Api api = Base_Url.getClient().create(Api.class);
        ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Call<CompanyDetails> call = api.addBusinessDetails(rbToken, rbname, rbemail, multipart_addimage, rbaddress, rbmobile, rbwebsite);
        //Call<CompanyDetails> call=api.addBusinessDetails(sharedprefconfig.getapitoken().getApi_token(),et_businessname.getText().toString(),et_businessemail.getText().toString(),et_businessaddress.getText().toString(),et_businessmobile.getText().toString(),imagepath) ;

        call.enqueue(new Callback<CompanyDetails>() {
            @Override
            public void onResponse(Call<CompanyDetails> call, Response<CompanyDetails> response) {
                if (response !=null && response.body()!=null) {
                    CompanyDetails companyDetails = response.body();
                    if (companyDetails.getResult().equals("1")) {
                        if (companyDetails.getRecord() != null) {

                            Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();


                            progressDialog.dismiss();
                            ll_update_btn.setVisibility(View.VISIBLE);
                            ll_finish_btn.setVisibility(View.GONE);
                            getbusinessdetail();




                        } else {
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
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
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
            public void onFailure(Call<CompanyDetails> call, Throwable t) {
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Check Your Internet Connection" + t)
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
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTheame_text));
                    }
                });
                dialog.show();
            }
        });
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
                    Glide.with(context).load(imagepath).into(iv_selectedlogo);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }if (requestCode == 10 && resultCode == RESULT_OK) {
//            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
            if (data != null && data.getData() != null) {
                try {
//                    final Uri selectedUri =Uri.parse(PathUtills.getPath(context, data.getData()));
                    final Uri selectedUri = data.getData();
                    imagepath= PathUtills.getPath(context,selectedUri);
//                    imagepath= PathUtills.getPath(context,selectedUri);
                    Glide.with(context).load(imagepath).into(iv_selectedlogo);
                    if (selectedUri != null) {
//                        CropImage.activity(selectedUri)
//                            .setGuidelines(CropImageView.Guidelines.ON)
//                            .setAspectRatio(1, 1)
//                                .start((Activity) context);
                    } else {
                        Toast.makeText(context, "Image not selected !", Toast.LENGTH_SHORT).show();
                    }
                    //doCrop(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void checkPermission() {

        if (ActivityCompat.checkSelfPermission(context, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddBusinessActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddBusinessActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddBusinessActivity.this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(AddBusinessActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        onBackPressed();
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
                //just request the permission
                ActivityCompat.requestPermissions(AddBusinessActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}