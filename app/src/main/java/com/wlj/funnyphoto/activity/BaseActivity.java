package com.wlj.funnyphoto.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wcl.notchfit.NotchFit;
import com.wcl.notchfit.args.NotchProperty;
import com.wcl.notchfit.args.NotchScreenType;
import com.wcl.notchfit.core.OnNotchCallBack;
import com.wlj.funnyphoto.views.LoadingDialog;

public abstract class BaseActivity extends AppCompatActivity {

    private LoadingDialog mLoading = null;

    public void showLoading(String tips) {
        if (mLoading == null) {
            mLoading = new LoadingDialog(this);
        }
        mLoading.setInterceptBack(true).setLoadingText(tips).show();
    }

    public void dismissLoading() {
        if (mLoading != null) mLoading.dismiss();
        mLoading = null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        initView();

        NotchFit.fit(this, NotchScreenType.FULL_SCREEN, new OnNotchCallBack() {
            @Override
            public void onNotchReady(NotchProperty notchProperty) {
                onAppNotchReady(notchProperty);
            }
        });
    }

    protected void initView() {
    }

    protected abstract int getLayoutId();

    protected void onAppNotchReady(NotchProperty notchProperty) {
        //TODO 获取到刘海屏或者滴水屏的高度之后,子类可以重写该方法进行适配
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
