//
// Created by Administrator on 2020/5/8.
//

#ifndef FUNNYPHOTO_GLMESSAGEQUEUE_H
#define FUNNYPHOTO_GLMESSAGEQUEUE_H

#include <vector>
#include "Message.h"
#include "pthread.h"
#include "../util/LogUtil.h"


using namespace std;

class GlMessageQueue {
public:

    vector<Message *> messages;
    pthread_mutex_t pthread_mutex;
    pthread_cond_t pthread_cond;

    GlMessageQueue();
    ~GlMessageQueue();

    void pushMessage(Message *msg);

    void getMessage(Message *);
    void clear();

};


#endif //FUNNYPHOTO_GLMESSAGEQUEUE_H
