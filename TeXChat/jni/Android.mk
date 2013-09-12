LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := mimetex
LOCAL_SRC_FILES := mimetex.cpp

include $(BUILD_SHARED_LIBRARY)
