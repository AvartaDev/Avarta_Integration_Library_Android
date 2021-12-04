package com.avarta.integrationlibrary.data.api;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.avarta.integrationlibrary.interfaces.IWorkflowProccessListener;

import java.util.ArrayList;
import java.util.List;

abstract class BaseRequestManager {

    private RequestQueue mRequestQueue;
    private List<BaseApi> mActiveRequests;
    private IWorkflowProccessListener mProccessListener;
    private String mServerUrl;
    private String mOrganizationKey;

    BaseRequestManager(String serverUrl, String organizationKey) {
        this.mServerUrl = serverUrl;
        this.mOrganizationKey = organizationKey;
    }

    void setServerUrl(String serverUrl) {
        mServerUrl = serverUrl;
    }

    public void setOrganizationKey(String organizationKey) {
        mOrganizationKey = organizationKey;
    }

    String getServerUrl() {
        return mServerUrl;
    }

    String getOrganizationKey() {
        return mOrganizationKey;
    }

    void setProccessListener(IWorkflowProccessListener proccessListener) {
        mProccessListener = proccessListener;
    }

    IWorkflowProccessListener getProccessListener() {
        return mProccessListener;
    }

    void removeApiListeners() {
        for (BaseApi api : getActiveRequests()) {
            api.removeListener();
        }
    }

    void addRequest(BaseApi api) {
        getActiveRequests().add(api);
    }

    private List<BaseApi> getActiveRequests() {
        if (mActiveRequests == null) {
            mActiveRequests = new ArrayList<>();
        }

        return mActiveRequests;
    }

    RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }
}
