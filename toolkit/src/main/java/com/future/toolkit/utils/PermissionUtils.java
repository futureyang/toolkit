package com.future.toolkit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by yangqc on 2019/9/23
 * 权限
 * @Author yangqc
 */
public class PermissionUtils {

    public static final int REQUEST_CODE_SD_CARD = 2001;

    /**
     * 检测权限
     * @param permission
     * @return
     */
    public static boolean checkPermission(@NonNull Context context, @NonNull String permission) {
        boolean isGranted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
        }
        return isGranted;
    }

    /**
     * 检测权限与尝试request
     */
    public static boolean checkAndRequestPermission(@NonNull Activity activity, int requestCode,
                                                    String... permissions) {
        ArrayList<String> notAllowPermissions = null;
        for (String permission : permissions) {
            if (!checkPermission(activity, permission)){
                if (notAllowPermissions == null){
                    notAllowPermissions = new ArrayList<>(permissions.length);
                }
                notAllowPermissions.add(permission);
            }
        }
        if (notAllowPermissions != null){
            ActivityCompat.requestPermissions(activity,
                    notAllowPermissions.toArray(new String[notAllowPermissions.size()]), requestCode);

            return false;
        } else {
            return true;
        }
    }

    public static boolean checkAllRequestGranted(@NonNull int[] grantResults){
        boolean granted = true;
        for (int result : grantResults) {
            granted &= result == PackageManager.PERMISSION_GRANTED;
        }
        return granted;
    }

    //系统授权设置的弹框
    //@info getResources().getString(R.string.app_name) + "需要访问 \"外部存储器读写权限\",否则会影响视频下载的功能使用, 请到 \"应用信息 -> 权限\" 中设置！"
    private void showPermissionDialog(@NonNull Activity activity, String info) {
        AlertDialog openAppDetDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(info);
        builder.setPositiveButton("去设置", (dialog, which) -> {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            activity.startActivity(intent);
        });
        builder.setCancelable(false);
        builder.setNegativeButton("暂不设置", (dialog, which) -> {
            //finish();
        });
        openAppDetDialog = builder.create();
        if (!openAppDetDialog.isShowing()) {
            openAppDetDialog.show();
        }
    }
}
