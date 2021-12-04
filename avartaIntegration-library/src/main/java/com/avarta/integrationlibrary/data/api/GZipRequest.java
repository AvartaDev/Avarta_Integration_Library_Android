package com.avarta.integrationlibrary.data.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.avarta.integrationlibrary.utils.CrashLogging;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class GZipRequest extends StringRequest {
    private final Response.Listener<String> mListener;

    /**
     * Creates a new request with the given method.
     *
     * @param method        the request {@link Method} to use
     * @param url           URL to fetch the string at
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public GZipRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        mListener = listener;
    }

    /**
     * Creates a new GET request.
     *
     * @param url           URL to fetch the string at
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public GZipRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        mListener = null;
    }

    @Override
    protected void deliverResponse(String response) {
        if (mListener != null)
            mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String output = "";
        if (response.data.length > 0) {
            try {
                GZIPInputStream gStream = new GZIPInputStream(new ByteArrayInputStream(response.data));
                InputStreamReader reader = new InputStreamReader(gStream);
                BufferedReader in = new BufferedReader(reader);
                String read;
                while ((read = in.readLine()) != null) {
                    output += read;
                }
                reader.close();
                in.close();
                gStream.close();
            } catch (IOException e) {
                CrashLogging.LogCrashException(new ParseError(), "GZipRequest.java parseNetworkResponse() error");
                return Response.error(new ParseError());
            }
            return Response.success(output, HttpHeaderParser.parseCacheHeaders(response));
        } else {
            return Response.success("{}", HttpHeaderParser.parseCacheHeaders(response));
        }
    }
}