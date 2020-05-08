#include <jni.h>
#include <string>

#include "gl/EglThread.h"
#include "util/JavaCall.h"

EglThread *rglThread;
JavaCall *wlJavaCall = NULL;
_JavaVM *javaVM = NULL;

extern "C" {


JNIEXPORT jint

JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    jint result = -1;
    javaVM = vm;
    JNIEnv *env;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return result;
    }
    return JNI_VERSION_1_4;
}

JNIEXPORT void JNICALL
Java_com_wlj_funnyphoto_FunnyEngine_native_1init(JNIEnv *env, jobject thiz) {

    if (wlJavaCall == NULL) {
        wlJavaCall = new JavaCall(javaVM, env, &thiz);
    }
    if (rglThread == NULL) {
        rglThread = new EglThread();
        rglThread->setJavaCall(wlJavaCall);
    }
}

JNIEXPORT void JNICALL
Java_com_wlj_funnyphoto_FunnyEngine_native_1prepare(JNIEnv *env, jobject thiz, jint type, jint id) {
    if (rglThread) {
        Message *msg = new Message();
        msg->what = MESSAGE_TYPE_PREPARE;
        msg->arg1 = type;
        msg->arg2 = id;
        rglThread->pushMessage(msg);
    }
}
JNIEXPORT void JNICALL
Java_com_wlj_funnyphoto_FunnyEngine_native_1surface_1create(JNIEnv *env, jobject thiz,
                                                            jobject surface, jint width,
                                                            jint height) {
    if (rglThread) {
        ANativeWindow *nativeWindow = ANativeWindow_fromSurface(env, surface);
        rglThread->onSurfaceCreate(nativeWindow, width, height);
    }
}

JNIEXPORT void JNICALL
Java_com_wlj_funnyphoto_FunnyEngine_native_1release(JNIEnv *env, jobject thiz) {
    if (rglThread) {
        Message *msg = new Message();
        msg->what = MESSAGE_TYPE_EXIT;
        rglThread->pushMessage(msg);
    }

    rglThread = NULL;
}
JNIEXPORT void JNICALL
Java_com_wlj_funnyphoto_FunnyEngine_native_1touch_1event(JNIEnv *env, jobject thiz, jfloat rotate_x,
                                                         jfloat rotate_y, jfloat scale_x,
                                                         jfloat scale_y) {
    if (rglThread) {
        rglThread->UpdateTransformMatrix(rotate_x, rotate_y, scale_x, scale_y);
    }
}
}

