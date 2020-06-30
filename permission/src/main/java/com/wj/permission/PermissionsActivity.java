package com.wj.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wj.permission.compat.RomUtil;
import com.wj.permission.compat.SettingsCompat;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by chunyang on 2018/3/14.
 */

public class PermissionsActivity extends AppCompatActivity {


    static final String TAG = "PermissionsActivity";

    private static final int PERMISSION_REQUEST_CODE = 999;
    private static final String EXTRA_PERMISSION = "permissions";
    //    private static final String EXTRA_KEY = "key";
    //    private static final String EXTRA_SHOW_TIP = "showTip";
    //    private static final String EXTRA_TIP = "tip";
    private String[] permissions;
    //    private String key;
    //    private boolean isRequireCheck;
    private boolean isAlertWindow;
    //    private boolean isWriteSetting;
    private final String defaultTitle = "帮助";
    private final String defaultContent = "当前应用缺少必要权限。\n \n 请点击 \"设置\"-\"权限\"-打开所需权限。";
    private final String defaultCancel = "取消";
    private final String defaultEnsure = "设置";
    private boolean isGotoSetting;

    private static PermissionListener sRequestListener;

    static Intent getStartIntent(Context context, String[] permission, PermissionListener requestListener) {
        Intent intent = new Intent(context, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSION, permission);
        PermissionsActivity.sRequestListener = requestListener;
//        intent.putExtra(EXTRA_KEY, key);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Permission_NoTitleTranslucentTheme);
        super.onCreate(savedInstanceState);
        invasionStatusBar(this);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSION)) {
            finish();
            return;
        }
//        isRequireCheck = true;
        String[] permissions = getIntent().getStringArrayExtra(EXTRA_PERMISSION);
        List<String> permissionList = new ArrayList<>(Arrays.asList(permissions));
        isAlertWindow = permissionList.contains(Manifest.permission.SYSTEM_ALERT_WINDOW);
        if (isAlertWindow) {
            permissionList.remove(Manifest.permission.SYSTEM_ALERT_WINDOW);
            if (SettingsCompat.canDrawOverlays(this)) {
                isAlertWindow = false;
            }
        }
//        isWriteSetting = permissionList.contains(Manifest.permission.WRITE_SETTINGS);
//        if (isWriteSetting) {
//            permissionList.remove(Manifest.permission.WRITE_SETTINGS);
//            if (SettingsCompat.canWriteSettings(this)) {
//                isWriteSetting = false;
//            }
//        }
        this.permissions = permissionList.toArray(new String[permissionList.size()]);
//        key = getIntent().getStringExtra(EXTRA_KEY);

        if (isAlertWindow) {
            overlayPermission(new Action<Void>() {
                @Override
                public void onAction(Void data) {
                    permissionsDenied();
                }
            }, new Action<Void>() {
                @Override
                public void onAction(Void data) {
                    requestPermissions();
                }
            });
        } else {
            requestPermissions();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGotoSetting) {
            if (isAlertWindow) {
                if (SettingsCompat.canDrawOverlays(this)) {
                    requestPermissions();
                } else {
                    overlayPermission(new Action<Void>() {
                        @Override
                        public void onAction(Void data) {
                            permissionsDenied();
                        }
                    }, new Action<Void>() {
                        @Override
                        public void onAction(Void data) {
                            requestPermissions();
                        }
                    });
                }
            } else {
                requestPermissions();
            }
            isGotoSetting = false;
        }

    }

    /**
     * @param denied  没有获取到
     * @param granted 获取到
     */
    private void overlayPermission(final Action<Void> denied, Action<Void> granted) {
        if (RomUtil.isFlyme()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);

            builder.setTitle(defaultTitle);
            builder.setMessage(defaultContent);

            builder.setNegativeButton(defaultCancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    denied.onAction(null);
                }
            });

            builder.setPositiveButton(defaultEnsure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    isGotoSetting = true;
                    SettingsCompat.manageDrawOverlays(PermissionsActivity.this);
                }
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            AndPermission.with(this).overlay().rationale(new Rationale<Void>() {
                @Override
                public void showRationale(Context context, Void data, final RequestExecutor executor) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);

                    builder.setTitle(defaultTitle);
                    builder.setMessage(defaultContent);

                    builder.setNegativeButton(defaultCancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            executor.cancel();
                        }
                    });

                    builder.setPositiveButton(defaultEnsure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            executor.execute();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            }).onDenied(denied).onGranted(granted).start();
        }
    }

    private void setting() {
        AndPermission.with(this).runtime().setting().start(1);
    }

    private void requestPermissions() {
        if (permissions.length <= 0) {
            permissionsGranted();
        } else {
            AndPermission.with(this).runtime().permission(permissions).onGranted(new Action<List<String>>() {
                @Override
                public void onAction(List<String> data) {
                    permissionsGranted();
                }
            }).onDenied(new Action<List<String>>() {
                @Override
                public void onAction(List<String> data) {
                    showMissingPermissionDialog();
                }
            }).start();
        }
    }

    // 显示缺失权限提示s
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);

        builder.setTitle(defaultTitle);
        builder.setMessage(defaultContent);

        builder.setNegativeButton(defaultCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
                permissionsDenied();
            }
        });

        builder.setPositiveButton(defaultEnsure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
                gotoSetting();
            }
        });
        builder.setCancelable(false);
        builder.show();
        Log.d(TAG, "Dialog Show ");
    }


    private void gotoSetting() {
        isGotoSetting = true;
        if (RomUtil.isFlyme()) {
            SettingsCompat.manageDrawOverlaysForRom(this);
        } else
            setting();
    }

//    // 请求权限兼容低版本
//    private void requestPermissions(String[] permissions) {
////        if (XPermission.shouldShowPermission(this, permissions)) {
////            showMissingPermissionDialog();
////        } else {
//        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
////        }
//    }

//    /**
//     * 用户权限处理,
//     * 如果全部获取, 则直接过.
//     * 如果权限缺失, 则提示Dialog.
//     *
//     * @param requestCode  请求码
//     * @param permissions  权限
//     * @param grantResults 结果
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        //部分厂商手机系统返回授权成功时，厂商可以拒绝权限，所以要用PermissionChecker二次判断
//        Log.d("PermissionsActivity", "onRequestPermissionsResult");
//        if (requestCode == PERMISSION_REQUEST_CODE &&
//                XPermission.isGrantedResult(grantResults) &&
//                XPermission.hasPermission(this, permissions)) {
//            permissionsGranted();
//        } else {
//
//        }
//    }


    private void permissionsDenied() {
        if (sRequestListener != null) {
            sRequestListener.permissionDenied(permissions);
        }
//        PermissionListener listener = XPermission.fetchListener(key);
//        if (listener != null) {
//            listener.permissionDenied(permissions);
//        }
        Log.d("PermissionsActivity", "permissionsDenied " + sRequestListener);
        finish();
    }

    // 全部权限均已获取
    private void /**/permissionsGranted() {
        if (sRequestListener != null) {
            sRequestListener.permissionGranted(permissions);
        }
//        PermissionListener listener = XPermission.fetchListener(key);
//        if (listener != null) {
//            listener.permissionGranted(permissions);
//        }
        Log.d("PermissionsActivity", "permissionsGranted");
        finish();
    }

    private static void invasionStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void finish() {
        sRequestListener = null;
        super.finish();
    }
}
