LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
                
LOCAL_CFLAGS := -DANDROID_NDK \
                -DDISABLE_IMPORTGL

LOCAL_MODULE    := skm
LOCAL_SRC_FILES := skm.cpp / aes256.cpp

LOCAL_ALLOW_UNDEFINED_SYMBOLS:=true

LOCAL_CPPFLAGS += -fexceptions

LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/include
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include

LOCAL_LDLIBS := -L$(NDK_PLATFORMS_ROOT)/$(TARGET_PLATFORM)/arch-arm/usr/lib -L$(LOCAL_PATH) -llog -lz -lm -ljnigraphics
LOCAL_EXPORT_LDLIBS := $(call host-path,$(LOCAL_PATH)/libs/$(TARGET_ARCH_ABI)/libsupc++.a)
include $(BUILD_SHARED_LIBRARY)
include $(call all-subdir-makefiles)


include $(subdirs)

include $(CLEAR_VARS)

LOCAL_MODULE := ecdhcurve25519

LOCAL_SRC_FILES := bigint.c curve25519.c ecdh_curve25519.c fe25519.c de_frank_durr_ecdh_curve25519_ECDHCurve25519.cc

include $(BUILD_SHARED_LIBRARY)

LOCAL_C_INCLUDES := $(LOCAL_PATH)

LOCAL_CFLAGS += -std=c99
