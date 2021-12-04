package com.avarta.integrationlibrary.data.api;

import android.text.TextUtils;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class BasePostRequest<T extends BaseResponse> extends BaseApi<T> {

    private static String TAG = BasePostRequest.class.getSimpleName();

    BasePostRequest(String organizationKey) {
        super(organizationKey);
    }

    public BasePostRequest<T> send(final String rawJson, String endpoint, Class<T> responseClass, RequestQueue queue) {
        send(null, rawJson, endpoint, responseClass, queue);
        return this;
    }

    public BasePostRequest<T> send(final Map<String, ?> params, String endpoint, Class<T> responseClass, RequestQueue queue) {
        send(params, null, endpoint, responseClass, queue);
        return this;
    }

//    public BasePostRequest<T> sendForBehav(final Map<String, ?> params, String endpoint, Class<T> responseClass, RequestQueue queue, boolean doCompression) {
//        if (doCompression) {
//            sendBehavSec(params, null, endpoint, responseClass, queue);
//        } else
//            send(params, null, endpoint, responseClass, queue);
//        return this;
//    }

    /**
     * method for compress and decompress data for behavsec
     *
     * @param params
     * @param rawJson
     * @param endpoint
     * @param responseClass
     * @param queue
     * @return
     */
//    private BasePostRequest<T> sendBehavSec(final Map<String, ?> params, final String rawJson, final String endpoint, final Class<T> responseClass, RequestQueue queue) {
//        Logger.i(this, TAG + " <onStart> " + "URL = " + endpoint + "\n" +
//                "params: " + (params != null ? params.toString() : "empty params"));
//        GZipRequest requestGZIP = new GZipRequest(Request.Method.POST, endpoint, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject responseObj = new JSONObject(response);
//                    Logger.i(this, " <onSuccess> code: " + "\n obj: " + responseObj.toString());
//                    if (mListener != null) {
//                        try {
//                            T respObj = createResponseInstance(responseClass);
//
//                            Logger.i(this, "response is null? - " + String.valueOf(respObj == null));
//
//                            if (respObj != null) {
//                                respObj.parse(responseObj);
//                            }
//
//                            mListener.onSuccess(respObj);
//                        } catch (JSONException e) {
//                            CrashLogging.LogCrashException(e);
//                            mListener.onError(new JsonParserException(Const.EXCEPTION_FAILED_JSON));
//                        }
//                    }
//                } catch (JSONException e) {
//                    CrashLogging.LogCrashException(e,"GZIP response json exception");
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//             //   handlerResponseError(error, mListener, TAG);
//                handlerResponseError(error,mListener,TAG,endpoint);
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//                return getContentType();
//            }
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                if (!TextUtils.isEmpty(rawJson)) {
//                    return rawJson.getBytes();
//                }
//
//                if (params != null) {
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    GZIPOutputStream gzos = null;
//
//                    try {
//                        gzos = new GZIPOutputStream(baos);
//                        gzos.write(new JSONObject(params).toString().getBytes());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } finally {
//                        if (gzos != null) try {
//                            gzos.close();
//                        } catch (IOException ignore) {
//                            ignore.printStackTrace();
//                        }
//                    }
//
//                    byte[] fooGzippedBytes = baos.toByteArray();
//                    return fooGzippedBytes;
//                    //   return new JSONObject(params).toString().getBytes();
//                }
//
//                return null;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return getBaseHeadersGZIP(getOrganizationKey());
//            }
//        };
//
//        requestGZIP.setTag(Const.GLOBAL_REQUEST_TAG);
//        requestGZIP.setRetryPolicy(new DefaultRetryPolicy(Const.REQUEST_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(requestGZIP);
//
//        return this;
//    }

    private BasePostRequest<T> send(final Map<String, ?> params, final String rawJson, final String endpoint, final Class<T> responseClass, RequestQueue queue) {
        Logger.i(this, TAG + " <onStart> " + "URL = " + endpoint + "\n" +
                "params: " + (params != null ? params.toString() : "empty params"));

        NetRequest request = new NetRequest(Request.Method.POST, endpoint,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            int code = ApiSuccessHelper.getCode(response);
                            JSONObject responseObj = ApiSuccessHelper.getJsonObject(response);

                            Logger.i(this, " <onSuccess> code: " + code + "\n obj: " + responseObj.toString());

                            if (mListener != null) {
                                try {
                                    T respObj = createResponseInstance(responseClass);

                                    Logger.i(this, "response is null? - " + String.valueOf(respObj == null));

                                    if (respObj != null) {
                                        respObj.parse(responseObj);
                                    }

                                    mListener.onSuccess(respObj);
                                } catch (JSONException e) {
                                    CrashLogging.LogCrashException(e);
                                    mListener.onError(new JsonParserException(Const.EXCEPTION_FAILED_JSON));
                                }
                            }
                        }catch (Exception e){
                            CrashLogging.LogCrashException(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  handlerResponseError(error, mListener, TAG);
                        handlerResponseError(error,mListener,TAG,endpoint);
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return getContentType();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                if (!TextUtils.isEmpty(rawJson)) {
                    return rawJson.getBytes();
                }

                if (params != null) {
                    return new JSONObject(params).toString().getBytes();
                }

                return null;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getBaseHeaders(getOrganizationKey());
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
            throw new RuntimeException(e);
        }
    }
}
