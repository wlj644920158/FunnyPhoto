//
// Created by Administrator on 2020/5/6.
//

#include "../util/LogUtil.h"
#include "EglHelper.h"


EglHelper::EglHelper() {
    mEglDisplay = EGL_NO_DISPLAY;
    mEglSurface = EGL_NO_SURFACE;
    mEglContext = EGL_NO_CONTEXT;
}

EglHelper::~EglHelper() {

}

int EglHelper::initEgl(EGLNativeWindowType window,int flags) {
    mEglDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    if (mEglDisplay == EGL_NO_DISPLAY) {
        LOGCATE("eglGetDisplay error");
        return -1;
    }
    if (!eglInitialize(mEglDisplay, 0, 0)) {
        LOGCATE("eglInitialize error");
        return -1;
    }

    // 判断是否尝试使用GLES3
    if ((flags & FLAG_TRY_GLES3) != 0) {
        EGLConfig config = getConfig(flags, 3);
        if (config != nullptr) {
            int attrib3_list[] = {
                    EGL_CONTEXT_CLIENT_VERSION, 3,
                    EGL_NONE
            };
            EGLContext context = eglCreateContext(mEglDisplay, config, mEglContext, attrib3_list);
            checkEglError("eglCreateContext");
            if (eglGetError() == EGL_SUCCESS) {
                mEglConfig = config;
                mEglContext = context;
                mGlVersion = 3;
            }
        }
    }

    // 判断如果GLES3的EGLContext没有获取到，则尝试使用GLES2
    if (mEglContext == EGL_NO_CONTEXT) {
        EGLConfig config = getConfig(flags, 2);
        int attrib2_list[] = {
                EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL_NONE
        };
        EGLContext context = eglCreateContext(mEglDisplay, config, mEglContext, attrib2_list);
        checkEglError("eglCreateContext");
        if (eglGetError() == EGL_SUCCESS) {
            mEglConfig = config;
            mEglContext = context;
        }
    }
    //5.创建渲染的surface
    mEglSurface = eglCreateWindowSurface(mEglDisplay, mEglConfig, window, NULL);
    if (mEglSurface == EGL_NO_SURFACE) {
        LOGCATE("eglCreateWindowSurface  error");
        return -1;
    }
    //6. 绑定eglContext和surface到display
    if (!eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)) {
        LOGCATE("eglMakeCurrent  error");
        return -1;
    }

    return 0;
}

int EglHelper::swapBuffers() {
    if (mEglDisplay != EGL_NO_DISPLAY && mEglSurface != EGL_NO_SURFACE &&
        eglSwapBuffers(mEglDisplay, mEglSurface)) {
        return 0;
    }
    return -1;
}

void EglHelper::destroyEgl() {
    if (mEglDisplay != EGL_NO_DISPLAY) {
        //解绑display上的eglContext和surface
        eglMakeCurrent(mEglDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);

        //销毁surface 和 eglContext
        if (mEglSurface != EGL_NO_SURFACE) {
            eglDestroySurface(mEglDisplay, mEglSurface);
            mEglSurface = EGL_NO_SURFACE;
        }

        if (mEglContext != EGL_NO_CONTEXT) {
            eglDestroyContext(mEglDisplay, mEglContext);
            mEglContext = EGL_NO_CONTEXT;
        }

        if (mEglDisplay != EGL_NO_DISPLAY) {
            eglTerminate(mEglDisplay);
            mEglDisplay = EGL_NO_DISPLAY;
        }
    }
}

EGLConfig EglHelper::getConfig(int flags, int version) {
    int renderableType = EGL_OPENGL_ES2_BIT;
    if (version >= 3) {
        renderableType |= EGL_OPENGL_ES3_BIT_KHR;
    }
    int attribList[] = {
            EGL_RED_SIZE, 8,
            EGL_GREEN_SIZE, 8,
            EGL_BLUE_SIZE, 8,
            EGL_ALPHA_SIZE, 8,
            EGL_DEPTH_SIZE, 16,
            EGL_STENCIL_SIZE, 8,
            EGL_RENDERABLE_TYPE, renderableType,
            EGL_NONE, 0,      // placeholder for recordable [@-3]
            EGL_NONE
    };
    int length = sizeof(attribList) / sizeof(attribList[0]);
    if ((flags & FLAG_RECORDABLE) != 0) {
        attribList[length - 3] = EGL_RECORDABLE_ANDROID;
        attribList[length - 2] = 1;
    }
    EGLConfig configs = nullptr;
    int numConfigs;
    if (!eglChooseConfig(mEglDisplay, attribList, &configs, 1, &numConfigs)) {
        LOGCATE("unable to find RGB8888 / %d  EGLConfig", version);
        return nullptr;
    }
    return configs;
}
void EglHelper::checkEglError(const char *msg) {
    int error;
    if ((error = eglGetError()) != EGL_SUCCESS) {
        LOGCATE("%s: EGL error: %x", msg, error);
    }
}
