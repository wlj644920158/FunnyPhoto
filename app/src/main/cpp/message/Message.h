//
// Created by Administrator on 2020/5/6.
//

#ifndef FUNNYPHOTO_MESSAGE_H
#define FUNNYPHOTO_MESSAGE_H

enum MessageType {
    MESSAGE_TYPE_SURFACE_CREATED,
    MESSAGE_TYPE_SURFACE_SIZE_CHANGED,//surface尺寸改变
    MESSAGE_TYPE_PREPARE,//准备创建资源,携带资源id
    MESSAGE_TYPE_TOUCH_EVENT,//准备创建资源,携带资源id
    MESSAGE_TYPE_EXIT,//请求退出
};


class Message {
public:
    int what;
    int arg1;
    int arg2;

};


#endif //FUNNYPHOTO_MESSAGE_H
