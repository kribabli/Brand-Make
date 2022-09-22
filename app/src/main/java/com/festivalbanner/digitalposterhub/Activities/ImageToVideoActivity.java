package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.festivalbanner.digitalposterhub.R;
import com.jcmore2.collage.CollageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageToVideoActivity extends AppCompatActivity {
    LinearLayoutCompat select_imgLayout;
    int PICK_IMAGE_MULTIPLE = 1;
    private String imagePath;
    ArrayList<Uri> mArrayUri;
    int position = 0;
    private List<String> imagePathList;
    CollageView imgVideo;
    private List<Bitmap> yourbitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_to_video);
        SetInit();
        setAction();


    }

    private void SetInit() {
        select_imgLayout = findViewById(R.id.select_imgLayout);
        imgVideo = findViewById(R.id.imgVideo);
    }

    private void setAction() {
        select_imgLayout.setOnClickListener(view -> {
            // initialising intent
            Intent intent = new Intent();
            // setting type to select to be image
            intent.setType("image/*");
            // allowing multiple image to be selected
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            imagePathList = new ArrayList<>();
            if (data.getClipData() != null) {
                if(data.getStringExtra("data").split("\\|")!=null){
                    int count = data.getClipData().getItemCount();
                    String[] imagesPath = data.getStringExtra("data").split("\\|");
                    for (int i=0; i<count; i++) {
                        yourbitmap.add(BitmapFactory.decodeFile(imagesPath[i]));
                    }
                }
                int count = data.getClipData().getItemCount();
                for (int i=0; i<count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();

                    Log.d("Amit","Check"+imageUri);
                    getImageFilePath(imageUri);
//                    imgVideo.createCollageBitmaps(yourbitmap);
                }
            }
            else if (data.getData() != null) {
                Uri imgUri = data.getData();
                getImageFilePath(imgUri);
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("Range")
    public void getImageFilePath(Uri uri) {
        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];
        Cursor cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor!=null) {
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            imagePathList.add(imagePath);

            cursor.close();
        }
    }




}