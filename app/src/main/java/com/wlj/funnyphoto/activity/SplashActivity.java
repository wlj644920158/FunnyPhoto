package com.wlj.funnyphoto.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.wlj.funnyphoto.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

public class SplashActivity extends BaseActivity {

    private static final int REQ_CODE_PERMISSION = 0x1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        jumpMain();
                    }
                }).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this);
                alertDialog
                        .setTitle("权限申请").
                        setMessage(getString(R.string.app_name) + "在导入相册图片以及保存图片到相册时需要读写外置存储卡,请授予权限")
                        .setPositiveButton("好的,去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AndPermission.with(SplashActivity.this)
                                        .runtime()
                                        .setting()
                                        .start(REQ_CODE_PERMISSION);
                            }
                        }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showToast("权限被拒绝,应用退出");
                        finish();
                    }
                }).create()
                        .show();

            }
        }).start();

    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        switch (reqCode) {
            case REQ_CODE_PERMISSION: {
                if (AndPermission.hasPermissions(this, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)) {
                    // 有对应的权限
                    jumpMain();
                } else {
                    // 没有对应的权限
                    showToast("权限被拒绝,应用退出");
                    finish();
                }
                break;
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    private void jumpMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
