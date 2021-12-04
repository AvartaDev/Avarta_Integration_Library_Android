package com.avarta.integrationlibrary.data.api.response;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseResponse {

    private JSONObject mJSONObject;

    public BaseResponse() {
        super();
    }

    public BaseResponse parse(JSONObject obj) throws JSONException {
        this.mJSONObject = obj;
        return this;
    }

    public JSONObject getJSONObject() {
        return mJSONObject;
    }
}
