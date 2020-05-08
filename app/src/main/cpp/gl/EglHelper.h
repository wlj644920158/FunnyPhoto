//
// Created by Administrator on 2020/5/6.
//

#ifndef FUNNYPHOTO_EGLHELPER_H
#define FUNNYPHOTO_EGLHELPER_H


#define FLAG_RECORDABLE 0x01

#define FLAG_TRY_GLES3 002

#include "EGL/egl.h"
#include <EGL/eglext.h>
#include <EGL/eglplatform.h>
#include "android/native_window.h"
#include "android/native_window_jni.h"


class EglHelper {
public:
    EGLDisplay mEglDisplay;
    EGLContext mEglContext;
    EGLSurface mEglSurface;
    EGLConfig  mEglConfig;
    int mGlVersion;
public:
    EglHelper();

    ~EglHelper();

    int initEgl(EGLNativeWindowType surface,int flags);
    int swapBuffers();
    void destroyEgl();

    EGLConfig getConfig(int flags, int version);
    // 检查是否出错
    void checkEglError(const char *msg);
};


#endif //FUNNYPHOTO_EGLHELPER_H
