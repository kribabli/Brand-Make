package com.festivalbanner.digitalposterhub.Activities;

import static com.bumptech.glide.Glide.with;
import static com.festivalbanner.digitalposterhub.R.drawable.finger;
import static com.festivalbanner.digitalposterhub.R.drawable.image_place_holder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Utills.Admin;
import com.teamup.app_sync.AppSyncImageSelector;
import com.teamup.app_sync.AppSyncPermissions;
import com.teamup.app_sync.AppSyncToast;

public class AfterBusinessSelection extends AppCompatActivity {

    Context context;

    RelativeLayout upload_image_reler;
    ImageView img;
    Button submit_btn;
    EditText name_edt, number_one_edt, number_two_edt, email_edt, website_edt, address_edt, city_edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_business_selection);
        Admin.HandleToolBar(this, "Add Business", findViewById(R.id.go_back_img), findViewById(R.id.title_head_txt));
        context = this;

        Handle_init_views();

        Handle_clickes();

        Handle_permissions();

        Handle_set_details();
    }

    private void Handle_set_details() {
        name_edt.setText("" + Admin.tinyDB.getString("name"));
        number_one_edt.setText("" + Admin.tinyDB.getString("number_one"));
        number_two_edt.setText("" + Admin.tinyDB.getString("number_two"));
        email_edt.setText("" + Admin.tinyDB.getString("email"));
        website_edt.setText("" + Admin.tinyDB.getString("website"));
        address_edt.setText("" + Admin.tinyDB.getString("address"));
        city_edt.setText("" + Admin.tinyDB.getString("city"));

        with(context).load(Admin.tinyDB.getString("path")).placeholder(image_place_holder).into(img);
    }

    private void Handle_permissions() {
        String pers[] = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        AppSyncPermissions.askPermissions(this, pers);

    }

    private void Handle_clickes() {
        upload_image_reler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSyncImageSelector.openGalleryAndSelect(context);
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handle_submission();
            }
        });
    }

    private void Handle_submission() {
        String name = name_edt.getText().toString();
        String number_two = number_two_edt.getText().toString();
        String number_one = number_one_edt.getText().toString();
        String email = email_edt.getText().toString();
        String website = website_edt.getText().toString();
        String address = address_edt.getText().toString();
        String city = city_edt.getText().toString();

        Admin.tinyDB.putString("name", name);
        Admin.tinyDB.putString("number_two", number_two);
        Admin.tinyDB.putString("number_one", number_one);
        Admin.tinyDB.putString("email", email);
        Admin.tinyDB.putString("website", website);
        Admin.tinyDB.putString("address", address);
        Admin.tinyDB.putString("city", city);

        Admin.tinyDB.putBoolean("business", true);

        AppSyncToast.showToast(context, "Business details saved successfully");

        finish();
        Admin.OverrideNow(context);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            String path = AppSyncImageSelector.getPath(context, data);
            Admin.tinyDB.putString("path", path);
            with(context).load(path).placeholder(image_place_holder).into(img);
        }
    }

    private void Handle_init_views() {
        city_edt = findViewById(R.id.city_edt);
        address_edt = findViewById(R.id.address_edt);
        website_edt = findViewById(R.id.website_edt);
        email_edt = findViewById(R.id.email_edt);
        number_two_edt = findViewById(R.id.number_two_edt);
        number_one_edt = findViewById(R.id.number_one_edt);
        name_edt = findViewById(R.id.name_edt);
        submit_btn = findViewById(R.id.submit_btn);
        upload_image_reler = findViewById(R.id.upload_image_reler);
        img = findViewById(R.id.img);
    }
}