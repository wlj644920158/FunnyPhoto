//
// Created by Administrator on 2020/5/6.
//
#include <LogUtil.h>
#include <GLSampleBase.h>
#include <Model3DSample.h>
#include "EglThread.h"


void *eglThreadImpl(void *context) {
    LOGCATE("egl thread start");
    EglThread *eglThread = static_cast<EglThread *>(context);
    if (!eglThread) {
        LOGCATE("eglThreadImpl eglThread is null");
        return 0;
    }
    EglHelper *eglHelper = new EglHelper();
    if (eglHelper->initEgl(eglThread->mANativeWindow, FLAG_TRY_GLES3) != 0) {
        LOGCATE("eglHelper initEgl error");
        return 0;
    }
    glEnable(GL_DEPTH_TEST);
    glClearColor(0.5f,0.5f,0.5f, 1.0f);
    glViewport(0, 0, eglThread->surfaceWidth, eglThread->surfaceHeight);
    eglHelper->swapBuffers();
    eglThread->wlJavaCall->onEglInitFinish();

    while (true) {
        Message *message = new Message();
        eglThread->getMessage(message);
       if (message->what == MESSAGE_TYPE_PREPARE) {
            LOGCATE("MESSAGE_TYPE_PREPARE");
            eglThread->wlJavaCall->onLoadStart();
            eglThread->base = new Model3DSample();
            eglThread->base->Init();
            eglThread->wlJavaCall->onLoadFinish();
            eglThread->base->Draw(eglThread->surfaceWidth, eglThread->surfaceHeight);
            eglHelper->swapBuffers();
        } else if (message->what == MESSAGE_TYPE_TOUCH_EVENT) {
            LOGCATE("MESSAGE_TYPE_TOUCH_EVENT");
            eglThread->base->Draw(eglThread->surfaceWidth, eglThread->surfaceHeight);
            eglHelper->swapBuffers();
        } else if (message->what == MESSAGE_TYPE_EXIT) {
            LOGCATE("MESSAGE_TYPE_EXIT");
            eglThread->base->Destroy();
            delete eglThread->base;
            //退出循环
            break;
        } else {
            //do nothing
        }
    }
    eglHelper->destroyEgl();
    delete eglHelper;
    eglHelper = NULL;
    delete eglThread;
    eglThread = NULL;
    return 0;
}

EglThread::EglThread() {
    pthread_mutex_init(&pthread_mutex, NULL);
    pthread_cond_init(&pthread_cond, NULL);
    LOGCATE("EglThread");
}

EglThread::~EglThread() {
    message.clear();
    pthread_mutex_destroy(&pthread_mutex);
    pthread_cond_destroy(&pthread_cond);
    LOGCATE("~EglThread");
}


void EglThread::onSurfaceCreate(EGLNativeWindowType window, int width, int height) {
    mANativeWindow = window;
    surfaceWidth = width;
    surfaceHeight = height;
    if (mEglThread == -1) {
        pthread_create(&mEglThread, NULL, eglThreadImpl, this);
    }
}

void EglThread::pushMessage(Message *msg) {
    pthread_mutex_lock(&pthread_mutex);
    this->message.push_back(msg);
    LOGCATE("push a message, what:%d, size:%d", msg->what, this->message.size());
    pthread_cond_signal(&pthread_cond);
    pthread_mutex_unlock(&pthread_mutex);
}

void EglThread::getMessage(Message *msg) {
    pthread_mutex_lock(&pthread_mutex);
    if (this->message.size() == 0) {
        pthread_cond_wait(&pthread_cond, &pthread_mutex);
    }
    Message *m = this->message.back();
    msg->what = m->what;
    msg->arg1 = m->arg1;
    msg->arg2 = m->arg2;
    this->message.pop_back();
    LOGCATE("pop a message, what:%d, size:%d", msg->what, this->message.size());
    pthread_mutex_unlock(&pthread_mutex);
}

void EglThread::UpdateTransformMatrix(float rotateX, float rotateY, float scaleX, float scaleY) {
    if (base) {
        base->UpdateTransformMatrix(rotateX, rotateY, scaleX, scaleY);
        Message *msg = new Message();
        msg->what = MESSAGE_TYPE_TOUCH_EVENT;
        pushMessage(msg);
    }
}

void EglThread::setJavaCall(JavaCall *wlJavaCall) {
    this->wlJavaCall = wlJavaCall;
}





