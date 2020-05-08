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
    jmid_on_load_start = jniEnv->GetMethodID(jlz, "onLoadStart", "()V");
    jmid_on_load_finish = jniEnv->GetMethodID(jlz, "onLoadFinish", "()V");
    jmid_on_egl_init_finish = jniEnv->GetMethodID(jlz, "onEglInitFinish", "()V");
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


void JavaCall::onLoadStart() {
    JNIEnv *jniEnv;
    if (javaVM->AttachCurrentThread(&jniEnv, 0) != JNI_OK) {
        return;
    }
    jniEnv->CallVoidMethod(jobj, jmid_on_load_start);
    javaVM->DetachCurrentThread();
}

void JavaCall::onLoadFinish() {
    JNIEnv *jniEnv;
    if (javaVM->AttachCurrentThread(&jniEnv, 0) != JNI_OK) {
        return;
    }
    jniEnv->CallVoidMethod(jobj, jmid_on_load_finish);
    javaVM->DetachCurrentThread();
}

void JavaCall::onEglInitFinish() {
    JNIEnv *jniEnv;
    if (javaVM->AttachCurrentThread(&jniEnv, 0) != JNI_OK) {
        return;
    }
    jniEnv->CallVoidMethod(jobj, jmid_on_egl_init_finish);
    javaVM->DetachCurrentThread();
}





