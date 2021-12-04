# The ARMv7 is significanly faster due to the use of the hardware FPU
APP_ABI := arm64-v8a armeabi-v7a
APP_PLATFORM := android-19
APP_PROJECT_PATH := $(shell pwd)
LOCAL_PATH := $(call my-dir)
APP_STL    := stlport_static
