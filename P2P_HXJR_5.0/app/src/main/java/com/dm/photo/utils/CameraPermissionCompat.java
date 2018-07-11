package com.dm.photo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

/**
 * 摄像头权限检测
 * author  dengyuhan
 * created 2017/3/6 18:08
 */
public class CameraPermissionCompat {

    private static final int REQUEST_CODE_CAMERA = 999;
    private static OnCameraPermissionListener mOnCameraPermissionListener;


    /**
     * 检测摄像头权限 没有就会申请
     * @param context
     * @param listener 申请权限的结果回调
     * @return
     */
    public static boolean checkCameraPermission(Context context, OnCameraPermissionListener listener) {
        mOnCameraPermissionListener = listener;
        boolean granted = true;
        //魅族或者6.0以下
        if (isFlyme() || Build.VERSION.SDK_INT < 23) {
            Camera mCamera = null;
            try {
                mCamera = Camera.open();
                // setParameters 是针对魅族MX5 做的。MX5 通过Camera.open() 拿到的Camera
                // 对象不为null
                Camera.Parameters mParameters = mCamera.getParameters();
                mCamera.setParameters(mParameters);
            } catch (Exception e) {
                granted = false;
            }
            if (mCamera != null) {
                mCamera.release();
            }
        } else {
            granted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PermissionChecker.PERMISSION_GRANTED;
        }
        if (granted) {
            if (mOnCameraPermissionListener != null) {
                mOnCameraPermissionListener.onGrantResult(true);
            }
        } else {
            if (context instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            }
        }
        return granted;
    }

    public static void onRequestPermissionsResult(Context context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (mOnCameraPermissionListener != null) {
                mOnCameraPermissionListener.onGrantResult(checkCameraPermission(context,null));
            }

        }
    }

    private static boolean isFlyme() {
        if (Build.BRAND.contains("Meizu")) {
            return true;
        } else {
            return false;
        }
    }


    public interface OnCameraPermissionListener {
        void onGrantResult(boolean granted);
    }
    /**
     * 通过尝试打开相机的方式判断有无拍照权限（在6.0以下使用拥有root权限的管理软件可以管理权限）
     *
     * @return
     */
    public static boolean cameraIsCanUse() { 
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }
}