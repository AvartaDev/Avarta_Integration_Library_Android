package com.avarta.integrationlibrary.data.api;

import android.util.Base64;

import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.avarta.integrationlibrary.interfaces.IBaseApiListener;
import com.avarta.integrationlibrary.exceptions.ApiErrorException;
import com.avarta.integrationlibrary.interfaces.IBaseApiListener;
import com.avarta.integrationlibrary.utils.CrashLogging;
import com.avarta.integrationlibrary.utils.Logger;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseApi<T> {

    protected final String TAG = this.getClass().getSimpleName();
    protected IBaseApiListener<T> mListener;
    private String mOrganizationKey;

    BaseApi(String organizationKey) {
        this.mOrganizationKey = organizationKey;
    }

    String getOrganizationKey() {
        return mOrganizationKey;
    }

    Map<String, String> getBaseHeaders(String organisationKey) {
        Map<String, String> headers = new HashMap<>();

        String credentials = Const.AUTH_USER_DEF + ":" + Const.AUTH_PASSWD_DEF;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        headers.put("Authorization", auth);
        headers.put("organisationKey", organisationKey);

        Logger.i(this, " <Headers>\n" + headers.toString());

        return headers;
    }

    Map<String, String> getBaseHeadersGZIP(String organisationKey) {
        Map<String, String> headers = new HashMap<>();

        String credentials = Const.AUTH_USER_DEF + ":" + Const.AUTH_PASSWD_DEF;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        headers.put("Authorization", auth);
        headers.put("organisationKey", organisationKey);
        headers.put("Content-Encoding", Const.CONTENT_ENCODING_TYPE);
        headers.put("Accept-Encoding", Const.CONTENT_ENCODING_TYPE);

        Logger.i(this, " <Headers>\n" + headers.toString());

        return headers;
    }

    String getContentType() {
        return "application/json";
    }

    void handlerResponseError(VolleyError error, IBaseApiListener<?> apiListener, String logTag) {
        String msg = getErrorResponseMessage(error);
        int code = getErrorResponseCode(error);
        CrashLogging.LogCrashException(error, logTag + " <onWorkflowError> " + "\n msg: " + error.getMessage() + "\n code: " + Integer.toString(code) + "\n Message:  " + msg);
        Logger.i(this, logTag + " <onWorkflowError> " + "\n msg: " + error.getMessage() + "\n code: " + Integer.toString(code) + "\n Message:  " + msg);

        try {
            int errorApiCode = Integer.parseInt(msg);

            if (apiListener != null) {
                apiListener.onError(new ApiErrorException(errorApiCode));
            }
        } catch (Exception e) {
            CrashLogging.LogCrashException(e);
            if (apiListener != null) {
                apiListener.onError(error);
            }
        }
    }

    void handlerResponseError(VolleyError error, IBaseApiListener<?> apiListener, String logTag, String requestEndpoint) {
        String msg = getErrorResponseMessage(error);
        int code = getErrorResponseCode(error);
        CrashLogging.LogCrashException(error, logTag + " <onWorkflowError> " + "\n request URL: " + requestEndpoint + "\n code: " + Integer.toString(code) + "\n Message:  " + msg);
        Logger.i(this, logTag + " <onWorkflowError> " + "\n msg: " + error.getMessage() + "\n code: " + Integer.toString(code) + "\n Message:  " + msg);

        try {
            int errorApiCode = Integer.parseInt(msg);

            if (apiListener != null) {
                apiListener.onError(new ApiErrorException(errorApiCode));
            }
        } catch (Exception e) {
            CrashLogging.LogCrashException(e);
            if (apiListener != null) {
                apiListener.onError(error);
            }
        }
    }

    private int getErrorResponseCode(final VolleyError error) {
        return error instanceof TimeoutError
                ? Const.ON_TIMEOUT_CODE : ApiErrorHelper.getErrorCode(error);
    }

    private String getErrorResponseMessage(final VolleyError error) {
        return error instanceof TimeoutError
                ? Const.EXCEPTION_TIMEOUT : ApiErrorHelper.getErrorMessage(error);
    }

    public BaseApi<T> addListener(IBaseApiListener<T> l) {
        mListener = l;
        return this;
    }

    public BaseApi<T> removeListener() {
        mListener = null;
        return this;
    }
}

