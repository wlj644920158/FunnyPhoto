//
// Created by Administrator on 2020/5/6.
//

#ifndef FUNNYPHOTO_MESSAGE_H
#define FUNNYPHOTO_MESSAGE_H

enum MessageType {
    MESSAGE_TYPE_PREPARE,//准备创建资源,携带资源id
    MESSAGE_TYPE_TOUCH_EVENT,//准备创建资源,携带资源id
    MESSAGE_TYPE_EXIT,//请求退出
};


class Message {
public:
    int what;

    int arg1;
    int arg2;
    int arg3;
    int arg4;

    float argf1;
    float argf2;
    float argf3;
    float argf4;

    void *data;

    Message() {}

    ~Message() {
        data = NULL;
    }

};


#endif //FUNNYPHOTO_MESSAGE_H
