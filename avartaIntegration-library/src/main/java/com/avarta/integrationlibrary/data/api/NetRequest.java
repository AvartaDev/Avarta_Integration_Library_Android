package com.avarta.integrationlibrary.data.api;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.avarta.integrationlibrary.utils.CrashLogging;

import java.io.UnsupportedEncodingException;

public class NetRequest extends Request<NetworkResponse> {

    private final Response.Listener<NetworkResponse> mListener;

    public NetRequest(int method, String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    public NetRequest(String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        this(Request.Method.GET, url, listener, errorListener);
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    public static String parseToString(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            CrashLogging.LogCrashException(e);
            parsed = new String(response.data);
        }
        return parsed;
    }
}