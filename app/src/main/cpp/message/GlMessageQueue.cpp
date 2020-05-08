//
// Created by Administrator on 2020/5/8.
//

#include "GlMessageQueue.h"

void GlMessageQueue::pushMessage(Message *msg) {
    pthread_mutex_lock(&pthread_mutex);
    this->messages.push_back(msg);
    LOGCATE("push a message, what:%d, size:%d", msg->what, this->messages.size());
    pthread_cond_signal(&pthread_cond);
    pthread_mutex_unlock(&pthread_mutex);
}

void GlMessageQueue::getMessage(Message *msg) {
    pthread_mutex_lock(&pthread_mutex);
    if (this->messages.size() == 0) {
        pthread_cond_wait(&pthread_cond, &pthread_mutex);
    }
    Message *m = this->messages.back();
    msg->what = m->what;
    msg->arg1 = m->arg1;
    msg->arg2 = m->arg2;
    msg->arg3 = m->arg3;
    msg->arg4 = m->arg4;
    msg->argf1 = m->argf1;
    msg->argf2 = m->argf2;
    msg->argf3 = m->argf3;
    msg->argf4 = m->argf4;
    msg->data = m->data;
    this->messages.pop_back();
    LOGCATE("pop a message, what:%d, size:%d", msg->what, this->messages.size());
    pthread_mutex_unlock(&pthread_mutex);
}

void GlMessageQueue::clear() {
    this->messages.clear();
}

GlMessageQueue::GlMessageQueue() {
    pthread_mutex_init(&pthread_mutex, NULL);
    pthread_cond_init(&pthread_cond, NULL);
}

GlMessageQueue::~GlMessageQueue() {
    clear();
    pthread_mutex_destroy(&pthread_mutex);
    pthread_cond_destroy(&pthread_cond);
}
