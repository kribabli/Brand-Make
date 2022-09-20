package com.festivalbanner.digitalposterhub.Utills;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.festivalbanner.digitalposterhub.BuildConfig;
import com.festivalbanner.digitalposterhub.ModelRetrofit.CompanyRecord;
import com.festivalbanner.digitalposterhub.ModelRetrofit.RecordRegister;
import com.festivalbanner.digitalposterhub.ModelRetrofit.ResponseLogin;
import com.festivalbanner.digitalposterhub.R;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SharedPrefrenceConfig {

    private static final String SHARED_PREF_NAME = "My_shared_pref";
    private Context context;

    public SharedPrefrenceConfig(Context context) {
        this.context = context;
    }

    public static void savebooleanPreferance(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
        editor.commit();
    }

    public static boolean getPrefBoolean(Context context, String key, boolean defvalue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(key, defvalue);
        return value;
    }


    public static void saveStringPreferance(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }

    public static String getPrefString(Context context, String key, String defvalue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, defvalue);
        return value;
    }
    public void logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //return sharedPreferences.getInt("id", -1) != -1;
        return sharedPreferences.getString("id", null) != null;

    }

    /*public boolean isLoggedout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //return sharedPreferences.getInt("id", -1) != -1;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
*/

    public RecordRegister getUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new RecordRegister(
                sharedPreferences.getString("id", null),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("city", null),
                sharedPreferences.getString("imageurl", null),
                sharedPreferences.getString("imagepath", null),
                sharedPreferences.getString("contact", null),
                sharedPreferences.getString("plan_type", ""));
    }
    public void getdata() {
        // progressBar.setVisibility(View.VISIBLE);
        String url = "http://clone.securemywebs.com/api/clone_digital_poster_app";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //    Toast.makeText(getApplicationContext(),"HELLO"+response,Toast.LENGTH_LONG).show();
                try {
                    JSONObject mainobj = new JSONObject(response);
                    if (mainobj.getString("result").equals("1")) {
                        SharedPrefrenceConfig.saveStringPreferance(context, Constance.userStatus, mainobj.getString("status"));
                    }
                } catch (JSONException e) {

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("token",Prefs.getPrefString(context,Constance.auth_token,""));
                params.put("package_name", BuildConfig.class.getPackage().getName());
                params.put("application_id", context.getApplicationContext().getPackageName());
                params.put("name", context.getResources().getString(R.string.app_name));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        //progressBar.setVisibility(View.GONE);
        /*progressDialog.dismiss();*/
    }

    public void setdata() {
        if (SharedPrefrenceConfig.getPrefString(context, Constance.userStatus, "").equals("Block")) {
            File dir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES) + "/" + Constance.FolderName);
            if (dir.isDirectory()) {
//                               file.delete();
                File[] files = dir.listFiles();
                String[] myFiles;
                myFiles = dir.list();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        File myFile = new File(dir, myFiles[i]);
                        myFile.delete();
//                                      files[i].delete();
                    }
                }
                boolean isdelete = dir.delete();
                try {
                    FileUtils.forceDelete(dir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (dir.exists()) {
                }
            }
        }
    }
    public ResponseLogin getapitoken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new ResponseLogin(sharedPreferences.getString("api_token", null));
    }

    public void saveUser(RecordRegister user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", user.getId());
        editor.putString("email", user.getEmail());
        editor.putString("name", user.getName());
        editor.putString("city", user.getCity());
        editor.putString("imageurl", user.getImage_url());
        editor.putString("imagepath", user.getImage());
        editor.putString("contact", user.getContact());
        editor.putString("plan_type", user.getPlan_type());
        editor.apply();
    }
    public void saveapitoken(ResponseLogin responseuser) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("api_token", responseuser.getApi_token());
        editor.apply();
    }
    public void saveCompanyDetails(CompanyRecord companyRecord) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", companyRecord.getId());
        editor.putString("email", companyRecord.getEmail());
        editor.putString("name", companyRecord.getName());
        editor.putString("contact", companyRecord.getContact());
        editor.putString("address", companyRecord.getAddress());
        editor.putString("logo", companyRecord.getLogo());
        editor.apply();
    }
    public CompanyRecord getCompanyDetails() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new CompanyRecord(
                sharedPreferences.getString("id", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("contact", null),
                sharedPreferences.getString("address", null),
                sharedPreferences.getString("logo", null));
    }
    //for logout
    public void clear() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
