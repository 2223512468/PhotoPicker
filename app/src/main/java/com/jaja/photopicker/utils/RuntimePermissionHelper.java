package com.jaja.photopicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名：RuntimePermissionHelper <br/>
 * 描述：运行时权限帮助类
 * 创建时间：2016/12/24 13:15
 *
 * @author hanter
 * @version 1.0
 */
public class RuntimePermissionHelper {

    private Activity mActivity;

    private Fragment mFragment;

    private OnGrantPermissionListener mListener;

    public interface OnGrantPermissionListener {
        /** 授权结果 */
        void onGrant(int requestCode, int result, String[] deniedPermissions);
    }

    public RuntimePermissionHelper(Activity activity, OnGrantPermissionListener listener) {
        this.mListener = listener;
        this.mActivity = activity;
    }

    public RuntimePermissionHelper(Fragment fragment, OnGrantPermissionListener listener) {
        this.mListener = listener;
        this.mFragment = fragment;
    }

    public boolean hasPermissions(String[] permissions) {
        boolean granted = true;

        for (String permission : permissions) {

            Context context;

            if (mActivity != null) {
                context = mActivity;
            } else {
                context = mFragment.getContext();
            }

            if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                granted = false;
            }
        }

        return granted;
    }

    public void grantPermissions(int requestCode, String[] permissions) {
        boolean granted = true;

        List<String> deniedPermissionList = new ArrayList<>();
        for (String permission : permissions) {

            Context context;

            if (mActivity != null) {
                context = mActivity;
            } else {
                context = mFragment.getContext();
            }

            if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                deniedPermissionList.add(permission);
            }
        }

        if (granted) {
            if (mListener != null) {
                mListener.onGrant(requestCode, PackageManager.PERMISSION_GRANTED,
                        list2Array(deniedPermissionList));
            }
        } else {
            if (mActivity != null) {
                ActivityCompat.requestPermissions(mActivity, list2Array(deniedPermissionList),
                        requestCode);
            } else if (mFragment != null) {
                mFragment.requestPermissions(list2Array(deniedPermissionList), requestCode);
            }
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean granted = false;
        List<String> deniedPermissionList = new ArrayList<>();
        try {
            int i = 0;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    granted = true;
                } else {
                    deniedPermissionList.add(permissions[i]);
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mListener != null) {

                if (granted)
                    mListener.onGrant(requestCode, PackageManager.PERMISSION_GRANTED,
                            list2Array(deniedPermissionList));
                else
                    mListener.onGrant(requestCode, PackageManager.PERMISSION_DENIED,
                            list2Array(deniedPermissionList));

            }
        }
    }

    private static String[] list2Array(List<String> list) {
        String[] aStr = new String[list.size()];
        return list.toArray(aStr);
    }
}