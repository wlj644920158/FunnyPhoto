cmake_minimum_required(VERSION 3.4.1)

set(jnilibs "${CMAKE_SOURCE_DIR}/src/main/jniLibs")


# 根据API版本判断使用哪个版本的OpenGLES
if (${ANDROID_PLATFORM_LEVEL} LESS 12)
    message(FATAL_ERROR "OpenGL 2 is not supported before API level 11 (currently using ${ANDROID_PLATFORM_LEVEL}).")
    return()
elseif (${ANDROID_PLATFORM_LEVEL} LESS 18)
    add_definitions("-DDYNAMIC_ES3")
    set(GLES-lib GLESv2)
else ()
    set(GLES-lib GLESv3)
endif (${ANDROID_PLATFORM_LEVEL} LESS 11)


include_directories(
        src/main/cpp/util
        src/main/cpp/gl
        src/main/cpp/samples
        src/main/cpp/message
        src/main/cpp/glm
        src/main/cpp/assimp
        src/main/cpp/model
        src/main/cpp/opencv_3_0_0
)

file(GLOB src-files
        ${CMAKE_SOURCE_DIR}/src/main/cpp/*.cpp
        ${CMAKE_SOURCE_DIR}/src/main/cpp/gl/*.cpp
        ${CMAKE_SOURCE_DIR}/src/main/cpp/util/*.cpp
        ${CMAKE_SOURCE_DIR}/src/main/cpp/samples/*.cpp)

add_library(gl_engine
        SHARED
        ${src-files}
        )

add_library(assimp SHARED IMPORTED )
set_target_properties(assimp PROPERTIES IMPORTED_LOCATION ${jnilibs}/${ANDROID_ABI}/libassimp.so)

add_library(opencv_java3 SHARED IMPORTED )
set_target_properties(opencv_java3 PROPERTIES IMPORTED_LOCATION ${jnilibs}/${ANDROID_ABI}/libopencv_java3.so)


target_link_libraries(gl_engine
        android
        EGL
        ${GLES-lib}
        assimp
        opencv_java3
        log)