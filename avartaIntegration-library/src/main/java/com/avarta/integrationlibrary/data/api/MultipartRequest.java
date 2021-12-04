package com.avarta.integrationlibrary.data.api;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyMultipartRequest;

import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends BaseApi<byte[]> {

    public MultipartRequest(String organizationKey) {
        super(organizationKey);
    }

    public MultipartRequest send(String url, final byte[] data, RequestQueue queue) {
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                if (mListener != null) {
                    mListener.onSuccess(response.data);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mListener != null) {
                    mListener.onError(error);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getBaseHeaders(getOrganizationKey());
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                params.put("data", new DataPart("data", data));
                return params;
            }
        };

        request.setTag(Const.GLOBAL_REQUEST_TAG);
        request.setRetryPolicy(new DefaultRetryPolicy(Const.REQUEST_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

        return this;
    }
}
