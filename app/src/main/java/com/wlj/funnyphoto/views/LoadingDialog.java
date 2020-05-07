package com.wlj.funnyphoto.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wlj.funnyphoto.R;

/**
 * created by tao_bp
 */
public class LoadingDialog {
    private Dialog mLoadingDialog;

    // 默认不能关闭
    private boolean mInterceptBack = true;

    private LinearLayout mLoadingLayout;
    private TextView mLoadingText;

    public LoadingDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);

        mLoadingLayout = view.findViewById(R.id.loading_layout);
        mLoadingText   = view.findViewById(R.id.loading_text);

        // 创建自定义样式的Dialog
        mLoadingDialog = new Dialog(context, R.style.loading_dialog) {
            @Override
            public void onBackPressed() {
                if (mInterceptBack) {
                    return;
                }
                LoadingDialog.this.dismiss();
            }
        };
        // 设置Dialog遮盖层透明度
        WindowManager.LayoutParams params = mLoadingDialog.getWindow().getAttributes();
        params.dimAmount = 0.3f;
        mLoadingDialog.getWindow().setAttributes(params);

        mLoadingDialog.setCancelable(!mInterceptBack);
        mLoadingDialog.setContentView(mLoadingLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        mLoadingDialog.setOnDismissListener(dialog -> {

        });
    }

    public LoadingDialog setInterceptBack(boolean interceptBack) {
        this.mInterceptBack = interceptBack;
        mLoadingDialog.setCancelable(!interceptBack);
        return this;
    }

    public LoadingDialog setLoadingText(String msg) {
        if (msg.isEmpty()) {
            mLoadingText.setVisibility(View.GONE);
        } else {
            mLoadingText.setVisibility(View.VISIBLE);
            mLoadingText.setText(msg);
        }

        return this;
    }

    public void show() {
        try {
            if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
                mLoadingDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLoadingDialog = null;
    }
}
