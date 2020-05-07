//
// Created by ywl on 2017-12-1.
//

#include "JavaCall.h"

JavaCall::JavaCall(_JavaVM *vm, JNIEnv *env, jobject *obj) {
    javaVM = vm;
    jniEnv = env;
    jobj = *obj;
    jobj = env->NewGlobalRef(jobj);
    jclass jlz = jniEnv->GetObjectClass(jobj);
    if (!jlz) {
        return;
    }
    jmid_on_prepare = jniEnv->GetMethodID(jlz, "onPrepare", "()V");
    jmid_on_finish = jniEnv->GetMethodID(jlz, "onFinish", "()V");
}

JavaCall::~JavaCall() {
}


void JavaCall::release() {
    if (javaVM != NULL) {
        javaVM = NULL;
    }
    if (jniEnv != NULL) {
        jniEnv = NULL;
    }
}


void JavaCall::onPrepare() {
    JNIEnv *jniEnv;
    if (javaVM->AttachCurrentThread(&jniEnv, 0) != JNI_OK) {
        return;
    }
    jniEnv->CallVoidMethod(jobj, jmid_on_prepare);
    javaVM->DetachCurrentThread();
}

void JavaCall::onFinish() {
    JNIEnv *jniEnv;
    if (javaVM->AttachCurrentThread(&jniEnv, 0) != JNI_OK) {
        return;
    }
    jniEnv->CallVoidMethod(jobj, jmid_on_finish);
    javaVM->DetachCurrentThread();
}





