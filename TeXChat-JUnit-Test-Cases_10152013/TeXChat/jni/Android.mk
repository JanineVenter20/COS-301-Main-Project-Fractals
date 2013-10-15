LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := chatTex
LOCAL_SRC_FILES := chatTex.c mimetex.c
LOCAL_CFLAGS += -DTEXFONTS -lm

include $(BUILD_SHARED_LIBRARY)

	