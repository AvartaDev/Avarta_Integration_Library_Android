package com.avarta.integrationlibrary.data.models;

public class AvartaConfig {

    private String mEyeVerifyLicense;
    private boolean mIsDeviceBased;
    private String mApkUrl;
    private int mApkVersion;

    public AvartaConfig() {
        super();
    }

    public boolean isDeviceBased() {
        return mIsDeviceBased;
    }

    public void setDeviceBased(boolean deviceBased) {
        mIsDeviceBased = deviceBased;
    }

    public String getEyeVerifyLicense() {
        return mEyeVerifyLicense;
    }

    public void setEyeVerifyLicense(String eyeVerifyLicense) {
        mEyeVerifyLicense = eyeVerifyLicense;
    }

    public String getApkUrl() {
        return mApkUrl;
    }

    public void setApkUrl(String apkUrl) {
        mApkUrl = apkUrl;
    }

    public int getApkVersion() {
        return mApkVersion;
    }

    public void setApkVersion(int apkVersion) {
        mApkVersion = apkVersion;
    }
}
