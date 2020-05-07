package com.wlj.funnyphoto.activity.texture;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wcl.notchfit.args.NotchProperty;
import com.wlj.funnyphoto.Function;
import com.wlj.funnyphoto.FunnyEngine;
import com.wlj.funnyphoto.GlideEngine;
import com.wlj.funnyphoto.R;
import com.wlj.funnyphoto.activity.BaseActivity;
import com.wlj.funnyphoto.adpter.FunctionAdapter;

import java.util.ArrayList;
import java.util.List;

public class TextureActivity extends BaseActivity implements FunctionAdapter.OnFunctionClickListener {


    private final float TOUCH_SCALE_FACTOR = 180.0f / 360;

    private static List<Function> functions;

    static {
        functions = new ArrayList<>();
        functions.add(new Function("垃圾桶", R.mipmap.ic_launcher));
    }

    private SurfaceView surfaceView;
    private RecyclerView function_list;
    private LinearLayout iv_select_bg;
    private Function selectFunction = null;//当前选中的功能
    private List<LocalMedia> selectList = new ArrayList<>();//当前选中的图片列表
    private List<LocalMedia> selectbgList = new ArrayList<>();//当前选中的图片列表

    private FunctionAdapter functionAdapter;
    private FunnyEngine funnyEngine;

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
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                funnyEngine.setSurfaceCreate(holder.getSurface());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                funnyEngine.setSurfaceSizeChange(width, height);
                //在surfaceChanged调用之后立马进行资源加载并且绘制,因为在之前Egl环境已经初始化好
                new Handler().postDelayed(() -> funnyEngine.prepareScene(0, 0), 100);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
        funnyEngine = new FunnyEngine();
        funnyEngine.setOnLoadListener(new FunnyEngine.OnLoadListener() {
            @Override
            public void onLoad() {
                runOnUiThread(() -> showLoading("正在加载资源..."));
            }

            @Override
            public void onFinish() {
                runOnUiThread(() -> dismissLoading());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        funnyEngine.release();
        funnyEngine = null;
    }

    private float mPreviousY; //上次的触控位置 y 坐标
    private float mPreviousX; //上次的触控位置 x 坐标

    private int mXAngle;
    private int mYAngle;
    private float mCurScale = 1.0f;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY(); //获取此次触控的 y 坐标
        float x = event.getX(); //获取此次触控的 x 坐标
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreviousY = y;
                mPreviousX = x; //记录触控点的 x、y 坐标
                break;
            case MotionEvent.ACTION_MOVE: //若为移动动作
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                mYAngle += dx * TOUCH_SCALE_FACTOR;
                mXAngle += dy * TOUCH_SCALE_FACTOR;

                funnyEngine.onTouchEvent(mXAngle, mYAngle,mCurScale,mCurScale);

                mPreviousY = y;
                mPreviousX = x;

                break;
        }
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_texture;
    }

    @Override
    public void onFunctionClick(Function function) {
        if (selectFunction != function) {
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
                case PictureConfig.CHOOSE_REQUEST + 1:
                    // 图片选择结果回调
                    selectbgList.clear();
                    selectbgList.addAll(PictureSelector.obtainMultipleResult(data));
                    if (selectbgList.size() > 0) {

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
                .maxSelectNum(6)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    public void selectBackground(View view) {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(6)
                .forResult(PictureConfig.CHOOSE_REQUEST + 1);
    }
}
