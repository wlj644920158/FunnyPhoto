//
// Created by Administrator on 2020/5/6.
//
#ifndef FUNNYPHOTO_EGLTHREAD_H
#define FUNNYPHOTO_EGLTHREAD_H

#include <EGL/eglplatform.h>
#include "pthread.h"
#include "android/native_window.h"
#include "android/native_window_jni.h"
#include "EglHelper.h"
#include <unistd.h>
#include "GLES2/gl2.h"

#include "../message/Message.h"
#include <vector>
#include <JavaCall.h>
#include "../samples/GLSampleBase.h"

using namespace std;

class EglThread {
public:
    pthread_t mEglThread = -1;
    ANativeWindow *mANativeWindow = NULL;
    vector<Message *> message;
    int surfaceWidth = 0;
    int surfaceHeight = 0;

    pthread_mutex_t pthread_mutex;
    pthread_cond_t pthread_cond;
    GLSampleBase *base;
    JavaCall *wlJavaCall;


public:
    EglThread();

    ~EglThread();

    void setJavaCall(JavaCall *wlJavaCall);

    //生命周期对应响应
    void onSurfaceCreate(EGLNativeWindowType window,int width, int height);


    void pushMessage(Message *msg);

    void getMessage(Message *);

    void UpdateTransformMatrix(float rotateX, float rotateY, float scaleX, float scaleY);

};


#endif //FUNNYPHOTO_EGLTHREAD_H
