package com.wlj.funnyphoto.activity;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wcl.notchfit.args.NotchProperty;
import com.wlj.funnyphoto.MenuView;
import com.wlj.funnyphoto.R;
import com.wlj.funnyphoto.activity.camera.CameraActivity;
import com.wlj.funnyphoto.activity.texture.TextureActivity;
import com.wlj.funnyphoto.util.CommonUtils;

public class MainActivity extends BaseActivity {
    private ImageView iv_setting;
    private MenuView menuView;

    @Override
    protected void initView() {
        super.initView();
        iv_setting = findViewById(R.id.iv_setting);
        menuView = findViewById(R.id.menuView);
        menuView.setOnMenuClickListener(position -> {
            if (position == 0) {
                startTexture();
            } else if (position == 1) {
                startCamera();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onAppNotchReady(NotchProperty notchProperty) {
        super.onAppNotchReady(notchProperty);
        if (notchProperty.isNotchEnable()) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) iv_setting.getLayoutParams();
            marginLayoutParams.topMargin = notchProperty.getNotchHeight();
            iv_setting.requestLayout();
        }
    }


    public void startTexture() {
        Intent intent = new Intent(this, TextureActivity.class);
        startActivity(intent);
    }

    public void startCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.copyAssetsDirToSDCard(MainActivity.this, "poly", "/sdcard/model");
    }

}
