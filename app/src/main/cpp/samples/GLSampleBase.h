//
// Created by ByteFlow on 2019/7/9.
//

#ifndef NDK_OPENGLES_3_0_GLSAMPLEBASE_H
#define NDK_OPENGLES_3_0_GLSAMPLEBASE_H

#include <GLES3/gl3.h>
#include "../util/ImageDef.h"
#include "../util/ByteFlowLock.h"

//For PI define
#define MATH_PI 3.1415926535897932384626433832802

class GLSampleBase {
public:

    virtual void LoadImage(NativeImage *pImage) {};

    virtual void LoadMultiImageWithIndex(int index, NativeImage *pImage) {};


    virtual void UpdateTransformMatrix(float rotateX, float rotateY, float scaleX, float scaleY) {}

    virtual void Init() = 0;

    virtual void Draw(int screenW, int screenH) = 0;

    virtual void Destroy() = 0;
};


#endif //NDK_OPENGLES_3_0_GLSAMPLEBASE_H
