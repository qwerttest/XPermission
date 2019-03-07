package com.wj.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;


/**
 * Created by chunyang on 2018/3/14.
 */

public class XPermission {

//    private static HashMap<String, PermissionListener> listenerMap = new HashMap();


    public static void requestPermission(Context context, PermissionListener listener, String... permissions) {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//        String key = String.valueOf(System.currentTimeMillis());
//        listenerMap.put(key, listener);
        Intent intent = PermissionsActivity.getStartIntent(context, permissions, listener);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
//        } else {
//            listener.permissionGranted(permissions);
//        }
    }


    /**
     * 判断权限是否授权
     *
     * @param activity
     * @param permissions
     * @return 已经获取权限 true 反之 false
     */
    protected static boolean hasPermission(@NonNull Activity activity, @NonNull String... permissions) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (permissions.length == 0) {
            return true;
        }

        for (String permission : permissions) {
            if (checkPermissionDenied(activity, permission)) {
                return false;//DENIED
            }
        }
        return true;
    }

    /**
     * 检测是否授权
     *
     * @param context
     * @param permission
     * @return true : 没有授权 false : 已经授权
     */
    private static boolean checkPermissionDenied(Context context, String permission) {//没有权限
        return PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED;
    }
//
//    public static boolean shouldShowPermission(Activity activity, String... permissions) {
//        for (String permission : permissions) {
//            if (shouldShowPermission(activity, permission)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    private static boolean shouldShowPermission(Activity activity, String permission) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return activity.shouldShowRequestPermissionRationale(permission);
//        } else {
//            return false;
//        }
//    }


    /**
     * 判断一组授权结果是否为授权通过
     *
     * @param grantResult
     * @return
     */
    protected static boolean isGrantedResult(@NonNull int... grantResult) {

        if (grantResult.length == 0) {
            return false;
        }

        for (int result : grantResult) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 跳转到当前应用对应的设置页面
     *
     * @param context
     */
    protected static boolean gotoSetting(@NonNull Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//    /**
//     * @param key
//     * @return
//     */
//    protected static PermissionListener fetchListener(String key) {
//        return listenerMap.remove(key);
//    }

}
