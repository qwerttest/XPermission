# XPermissions 介绍

XPermission 是动态授权辅助工具，帮助开发者简单几行代码就可实现动态授权。

## 方法介绍

```
# 请求检测权限，可以直接使用，也可以通过会调监听
XPermission.requestPermission(Context context, PermissionListener listener, String... permissions)
```

基本使用以上一个方法即可检测是否已经获取到授权状态。


## PermissionListener介绍

权限检测会调方法，如果已经获取到权限返回 `permissionGranted` 
反之返回 `permissionDenied` 


### 1.0.2版本
添加特殊权限

### 1.0.4版本
更改 FlyMe 版本问题
添加 new Task Flag
### 1.0.6版本
会调问题修复