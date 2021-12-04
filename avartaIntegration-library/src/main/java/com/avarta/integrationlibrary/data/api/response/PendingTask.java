package com.avarta.integrationlibrary.data.api.response;

public class PendingTask {

    private String mCode;
    private String mWorkflowType;

    public PendingTask(String code, String workflowType) {
        mCode = code;
        mWorkflowType = workflowType;
    }

    public PendingTask() {
        super();
    }

    public String getCode() {
        return mCode;
    }

    public String getWorkflowType() {
        return mWorkflowType;
    }
}
