package com.mobile.android.permission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.wj.permission.PermissionListener;
import com.wj.permission.XPermission;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//
//        builder.setTitle("Title");
//        builder.setMessage("dddd");
//
//        builder.setNegativeButton("qu", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////                dialog.dismiss();
////                permissionsDenied();
//            }
//        });
//
//        builder.setPositiveButton("qeud", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////                dialog.dismiss();
////                gotoSetting();
//            }
//        });
//        builder.setCancelable(true);
//        builder.show();

//        RxPermissions rxPermissions = new RxPermissions(this);
//        rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
//            @Override
//            public void accept(Boolean aBoolean) throws Exception {
//                XLog.d("权限：" + aBoolean);
//            }
//        });


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
//                XLog.d("权限：shouldShowRequestPermissionRationale true");
//            } else {
//                XLog.d("权限：shouldShowRequestPermissionRationale");
//
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
//            }
//
//        }

//        if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
//        EasyPermissions.requestPermissions(this, "必要的权限", 0, Manifest.permission.CAMERA);
//        }

//        XPermission.requestPermission(this, new PermissionListener() {
//            @Override
//            public void permissionGranted(@NonNull String[] permission) {
//                XLog.d("权限：permissionGranted");
////                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
////                intent.putExtra(CameraActivity.PATH, Environment.getExternalStorageDirectory().getPath());
////                intent.putExtra(CameraActivity.OPEN_PHOTO_PREVIEW, true);
////                intent.putExtra(CameraActivity.USE_FRONT_CAMERA, false);
////                startActivity(intent);
//            }
//
//            @Override
//            public void permissionDenied(@NonNull String[] permission) {
//                XLog.d("权限：permissionDenied");
//            }
//        }, Manifest.permission.READ_CONTACTS);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (XPermissions.hasPermission(this, Manifest.permission.CAMERA)) {
//            XLog.d("含有权限");
//            return;
//        }

    }

    public void onClick(View view) {

//        if (!SettingsCompat.canDrawOverlays(this)) {
//            SettingsCompat.manageDrawOverlays(this);
//        }
//        AndPermission.with(this).overlay().rationale(new Rationale<Void>() {
//            @Override
//            public void showRationale(Context context, Void data, final RequestExecutor executor) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//
//                builder.setTitle("title");
//                builder.setMessage("内容");
//
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        executor.cancel();
//                    }
//                });
//
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        executor.execute();
//                    }
//                });
//                builder.setCancelable(false);
//                builder.show();
//            }
//        }).onGranted(new Action<Void>() {
//            @Override
//            public void onAction(Void data) {
//                Toast.makeText(MainActivity.this, "权限：获取到", Toast.LENGTH_SHORT).show();
//            }
//        }).onDenied(new Action<Void>() {
//            @Override
//            public void onAction(Void data) {
//                Toast.makeText(MainActivity.this, "权限：没有获取到", Toast.LENGTH_SHORT).show();
//            }
//        }).start();

        XPermission.requestPermission(this, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                Toast.makeText(MainActivity.this, "权限：获取到", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {
                Toast.makeText(MainActivity.this, "权限：没有获取到", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.CAMERA);

//        AndPermission.with(this).overlay().onDenied(new Action<Void>() {
//            @Override
//            public void onAction(Void data) {
//                Toast.makeText(MainActivity.this, "权限：没有获取到", Toast.LENGTH_SHORT).show();
//            }
//        }).onGranted(new Action<Void>() {
//            @Override
//            public void onAction(Void data) {
//                Toast.makeText(MainActivity.this, "权限：获取到", Toast.LENGTH_SHORT).show();
//            }
//        }).start();

//        if (!SettingsCompat.canDrawOverlays(this)) {
//            SettingsCompat.manageDrawOverlays(this);
//            Toast.makeText(MainActivity.this, "权限：没有获取到", Toast.LENGTH_SHORT).show();
//            return;
//        } else {
//            Toast.makeText(MainActivity.this, "权限：获取到", Toast.LENGTH_SHORT).show();
//        }


//        AndPermission.with(this).runtime().permission(Manifest.permission.CAMERA).onDenied(new Action<List<String>>() {
//            @Override
//            public void onAction(List<String> data) {
//                AndPermission.with(MainActivity.this).runtime().setting().start();
//                Toast.makeText(MainActivity.this, "权限：没有获取到", Toast.LENGTH_SHORT).show();
//            }
//        }).onGranted(new Action<List<String>>() {
//            @Override
//            public void onAction(List<String> data) {
//                Toast.makeText(MainActivity.this, "权限：获取到", Toast.LENGTH_SHORT).show();
//            }
//        }).start();
//        Log.d("MainActivity", " psermission:" + Build.VERSION.SDK_INT);


//        XPermission.requestPermission(this, new PermissionListener() {
//            @Override
//            public void permissionGranted(@NonNull String[] permission) {
//                Toast.makeText(MainActivity.this, "权限：获取到", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void permissionDenied(@NonNull String[] permission) {
//                Toast.makeText(MainActivity.this, "权限：没有获取到", Toast.LENGTH_SHORT).show();
//            }
//        }, Manifest.permission.CAMERA);

    }
}
