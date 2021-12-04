package com.avarta.integrationlibrary.data.api;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.avarta.integrationlibrary.data.api.response.BaseResponse;
import com.avarta.integrationlibrary.exceptions.JsonParserException;
import com.avarta.integrationlibrary.utils.CrashLogging;
import com.avarta.integrationlibrary.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BaseGetRequest<T extends BaseResponse> extends BaseApi<T> {

    private static String TAG = BaseGetRequest.class.getSimpleName();

    BaseGetRequest() {
        super(null);
    }

    public BaseGetRequest<T> send(String endpoint, Class<T> responseClass, RequestQueue queue) {
        send(null, endpoint, responseClass, queue);
        return this;
    }

    private BaseGetRequest<T> send(final Map<String, String> headers, final String endpoint, final Class<T> responseClass, RequestQueue queue) {
        Logger.i(this, TAG + " <onStart> " + "URL = " + endpoint + "\n" +
                "params: " + (headers != null ? headers.toString() : "empty headers"));

        NetRequest request = new NetRequest(Request.Method.GET, endpoint,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        int code = ApiSuccessHelper.getCode(response);
                        JSONObject responseObj = ApiSuccessHelper.getJsonObject(response);

                        Logger.i(this, " <onSucforceWorkflowRestartcess> code: " + code + "\n obj: " + responseObj.toString());

                        if (mListener != null) {
                            try {
                                T respObj = createResponseInstance(responseClass);


                                Logger.i(this, "response is null? - " + String.valueOf(respObj == null));

                                if (respObj != null) {
                                    respObj.parse(responseObj);
                                }

                                mListener.onSuccess(respObj);
                            } catch (JSONException e) {
                                CrashLogging.LogCrashException(new JsonParserException(Const.EXCEPTION_FAILED_JSON),"From BaseGetRequest class");
                                mListener.onError(new JsonParserException(Const.EXCEPTION_FAILED_JSON));
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // handlerResponseError(error, mListener, TAG);
                        handlerResponseError(error,mListener,TAG,endpoint);
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return getContentType();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> localHeaders = headers;

                if (localHeaders == null) {
                    localHeaders = new HashMap<>();
                }

                return localHeaders;
            }
        };

        request.setTag(Const.GLOBAL_REQUEST_TAG);
        request.setRetryPolicy(new DefaultRetryPolicy(Const.REQUEST_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

        return this;
    }

    private T createResponseInstance(Class<T> responseClass) {
        try {
            return responseClass.newInstance();
        } catch (Exception e) {
            CrashLogging.LogCrashException(e);
            throw new RuntimeException(e);
        }
    }
}
