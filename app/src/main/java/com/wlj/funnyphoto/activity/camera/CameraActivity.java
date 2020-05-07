package com.wlj.funnyphoto.activity.camera;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wcl.notchfit.args.NotchProperty;
import com.wlj.funnyphoto.Function;
import com.wlj.funnyphoto.GlideEngine;
import com.wlj.funnyphoto.R;
import com.wlj.funnyphoto.activity.BaseActivity;
import com.wlj.funnyphoto.adpter.FunctionAdapter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * 拍照界面,相机的选择图片一般是选择背景图
 */
public class CameraActivity extends BaseActivity implements FunctionAdapter.OnFunctionClickListener {
    private static List<Function> functions;

    static {
        functions = new ArrayList<>();
        functions.add(new Function("圆环", R.mipmap.ic_launcher));
    }

    private RecyclerView function_list;
    private ImageView iv_select_bg;
    private Function selectFunction = null;//当前选中的功能
    private List<LocalMedia> selectList = new ArrayList<>();//当前选中的图片列表

    private FunctionAdapter functionAdapter;

    @Override
    protected void initView() {
        super.initView();
        function_list = findViewById(R.id.function_list);
        iv_select_bg = findViewById(R.id.iv_select_bg);
        functionAdapter = new FunctionAdapter(functions);
        functionAdapter.setOnFunctionClickListener(this);
        function_list.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        function_list.setAdapter(functionAdapter);
        selectFunction = functions.get(0);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    public void onFunctionClick(Function function) {
        if(selectFunction!=function){
            selectFunction = function;
        }
    }

    @Override
    protected void onAppNotchReady(NotchProperty notchProperty) {
        super.onAppNotchReady(notchProperty);
        if (notchProperty.isNotchEnable()) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) iv_select_bg.getLayoutParams();
            marginLayoutParams.topMargin = notchProperty.getNotchHeight();
            iv_select_bg.requestLayout();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList.clear();
                    selectList.addAll(PictureSelector.obtainMultipleResult(data));
                    if (selectList.size() > 0) {
                    }
                    break;
            }
        }
    }

    public void selectPicture(View view) {
        selectPic();
    }

    public void selectPic() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
}
