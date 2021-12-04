package com.avarta.integrationlibrary.enums;

import android.text.TextUtils;

public enum WorkflowType {
    ENROL("ENROL"),
    AUTH("AUTH"),
    REMOVE("DE-ENROL"),
    STEPUP("STEP-UP"),
    STEPUP_ELEVATED("STEP-UP-ELEVATED");

    private String mApiParamKey;

    WorkflowType(String paramKey) {
        this.mApiParamKey = paramKey;
    }

    public String getApiParamKey() {
        return mApiParamKey;
    }

    @Override
    public String toString() {
        return mApiParamKey;
    }

    public static WorkflowType fromKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        for (WorkflowType type : WorkflowType.values()) {
            if (type.getApiParamKey().equalsIgnoreCase(key)) {
                return type;
            }
        }

        return null;
    }
}
