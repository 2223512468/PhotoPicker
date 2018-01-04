package com.jaja.photopicker;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DisplayImageOptions userOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).
                        showImageForEmptyUri(R.drawable.image_not_exist).
                        showImageOnLoading(R.drawable.image_not_exist).
                        showImageOnFail(R.drawable.image_not_exist).
                        imageScaleType(ImageScaleType.EXACTLY).
                        bitmapConfig(Bitmap.Config.RGB_565).
                        considerExifParams(true)
                .build();


        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration
                        .Builder(getApplicationContext())
                        .diskCacheFileCount(200)
                        .threadPoolSize(Thread.NORM_PRIORITY)
//                        .denyCacheImageMultipleSizesInMemory()
//                        .memoryCacheSize(10 * 1024 * 1024)
                        .defaultDisplayImageOptions(userOptions)
                        .build();

        ImageLoader.getInstance().init(config);
    }
}
