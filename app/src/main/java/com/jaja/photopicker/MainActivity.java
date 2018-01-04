package com.jaja.photopicker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.jaja.photopicker.engine.GlideEngine;
import com.jaja.photopicker.engine.LoadEngine;
import com.jaja.photopicker.utils.PicturePickerUtils;

import java.io.FileNotFoundException;
import java.util.List;


public class MainActivity extends FragmentActivity {
    public static final int REQUEST_CODE_CHOOSE = 1;
    private List<Uri> mSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = PicturePickerUtils.obtainResult(data);
            for (Uri u : mSelected) {
                Log.i("picture", u.getPath());

                try {
                    ParcelFileDescriptor fd = getContentResolver().openFileDescriptor(u, "r");
                    Bitmap bmp = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor());
                    Log.i("picture", "图片 宽：" + bmp.getWidth() + ", 高：" + bmp.getHeight());
                    bmp.recycle();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void onClickButton(View view) {
        Picker.from(this)
                .count(2)
                .enableCamera(true)
                .setEngine(new GlideEngine())
                //.setEngine(new PicassoEngine())
                //.setEngine(new ImageLoaderEngine())
                //.setEngine(new CustomEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    static class CustomEngine implements LoadEngine {
        @Override
        public void displayImage(String path, ImageView imageView) {
            Log.i("picture", path);
        }

        @Override
        public void displayCameraItem(ImageView imageView) {

        }

        @Override
        public void scrolling(GridView view) {

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }

        public CustomEngine() {

        }

        protected CustomEngine(Parcel in) {
        }

        public static final Creator<CustomEngine> CREATOR = new Creator<CustomEngine>() {
            public CustomEngine createFromParcel(Parcel source) {
                return new CustomEngine(source);
            }

            public CustomEngine[] newArray(int size) {
                return new CustomEngine[size];
            }
        };
    }
}
