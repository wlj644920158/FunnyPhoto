package com.wlj.funnyphoto;

import android.view.MotionEvent;
import android.view.Surface;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

public class FunnyEngine {

    static {
        System.loadLibrary("gl_engine");
    }

    public interface OnLoadListener {
        void onLoad();

        void onFinish();
    }

    OnLoadListener onLoadListener;

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }


    public FunnyEngine() {
        native_init();
    }

    /**
     * 开始加载场景
     */
    public void prepareScene(int sceneType, int sceneId) {
        native_prepare(sceneType, sceneId);
    }

    /**
     * Surface创建
     */
    public void setSurfaceCreate(Surface surface) {
        native_surface_create(surface);
    }

    /**
     * Surface尺寸改变
     */
    public void setSurfaceSizeChange(int width, int height) {
        native_surface_size_change(width, height);
    }

    public void onTouchEvent(float rotateX, float rotateY, float scaleX, float scaleY) {
        native_touch_event(rotateX, rotateY, scaleX, scaleY);
    }

    /**
     * 选择了图片,之后给底层设置texture
     *
     * @param imgs
     */
    public void setBitmap(List<LocalMedia> imgs) {

    }

    public void release() {
        native_release();
    }

    /**
     * 底层调用
     */
    public void onPrepare() {
        if (onLoadListener != null) {
            onLoadListener.onLoad();
        }
    }

    /**
     * 底层调用
     */
    public void onFinish() {
        if (onLoadListener != null) {
            onLoadListener.onFinish();
        }
    }

    private native void native_init();

    private native void native_prepare(int type, int id);

    private native void native_surface_create(Surface surface);

    private native void native_surface_size_change(int width, int height);

    private native void native_touch_event(float rotateX, float rotateY, float scaleX, float scaleY);

    private native void native_release();

}
