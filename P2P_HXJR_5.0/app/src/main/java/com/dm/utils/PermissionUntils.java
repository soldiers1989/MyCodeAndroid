package com.dm.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.dm.widgets.utils.AlertDialogUtil;


/**
 * Created by TodayFu Lee on 2017/6/16.
 */

public class PermissionUntils
{
    public PermissionUntils(final Activity activity){
        this.activity=activity;
    }
    // 要申请的权限
    private Activity activity;
//    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//    private AlertDialog dialog;
    // 开始提交请求权限
    private void startRequestPermission(String[] permissions){
        ActivityCompat.requestPermissions(activity,permissions,210);
    }
    // 提示用户该请求权限的弹出框
    public void showDialogTipUserRequestPermission(String message,final String[] permissions) {
        AlertDialog alert =
                AlertDialogUtil.confirm(activity, message, new AlertDialogUtil.ConfirmListener(){
                    @Override
                    public void onOkClick() {
                        startRequestPermission(permissions);
                    }

                    @Override
                    public void onCancelClick() {
                    }
                });
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
   }
    // 提示用户去应用设置界面手动开启权限
    public  void showDialogTipUserGoToAppSettting(String message) {
        AlertDialog alert =
                AlertDialogUtil.alert(activity, message, new AlertDialogUtil.AlertListener() {
                    @Override
                    public void doConfirm() {
                        goToAppSetting();
//                        startRequestPermission(permissions);
                    }
                });
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
     }
    // 跳转到当前应用的设置界面
    private  void goToAppSetting() {
         Intent intent = new Intent();
         intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
         Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
         intent.setData(uri);
         activity.startActivityForResult(intent, 123);
     }


//
//    @Override
//     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//             super.onActivityResult(requestCode, resultCode, data);
//             if (requestCode == 123) {
//
//                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                             // 检查该权限是否已经获取
//                             int i = ContextCompat.checkSelfPermission(this, permissions[0]);
//                             // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
//                             if (i != PackageManager.PERMISSION_GRANTED) {
//                                     // 提示用户应该去应用设置界面手动开启权限
//                                     showDialogTipUserGoToAppSettting();
//                                 } else {
//                                    if (dialog != null && dialog.isShowing()) {
//                                            dialog.dismiss();
//                                         }
//                                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
//                                }
//                         }
//                 }
//         }



     // 用户权限 申请 的回调方法
//      @Override
//      public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//           if (requestCode == 321) {
//               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                   if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                     // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
//                     boolean b = shouldShowRequestPermissionRationale(permissions[0]);
//                     if (!b) {
//                        // 用户还是想用我的 APP 的
//                        // 提示用户去应用设置界面手动开启权限
//                        showDialogTipUserGoToAppSettting();
//                    } else
//                          finish();
//                      } else {
//                           Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
//                         }
//                    }
//             }
//       }
}
