//
// Created by ywl on 2017-12-1.
//

#ifndef WLPLAYER_WLLISTENER_H
#define WLPLAYER_WLLISTENER_H

#include <jni.h>
#include <stdint.h>


class JavaCall {

public:
    _JavaVM *javaVM = NULL;
    JNIEnv *jniEnv = NULL;
    jmethodID jmid_on_prepare;
    jmethodID jmid_on_finish;

    jobject jobj;

public:
    JavaCall(_JavaVM *javaVM, JNIEnv *env, jobject *jobj);

    ~JavaCall();


    void onPrepare();

    void onFinish();

    void release();
};


#endif //WLPLAYER_WLLISTENER_H
