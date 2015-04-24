LOCAL_PATH := $(call my-dir)


include $(CLEAR_VARS)

LOCAL_MODULE    := testndk
LOCAL_SRC_FILES := 
LOCAL_SRC_FILES += com_example_testndk_Good.cpp
LOCAL_SRC_FILES += t/student.cpp

include $(BUILD_SHARED_LIBRARY)
