package com.avarta.integrationlibrary.data.models;

import com.avarta.integrationlibrary.data.api.response.BaseResponse;
import com.avarta.integrationlibrary.utils.CrashLogging;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfoResponse extends BaseResponse {

    private String mSessionKey;
    private String mUserName;

    public UserInfoResponse() {
        super();
    }

    @Override
    public BaseResponse parse(JSONObject obj) throws JSONException {
        if (obj == null) {
            return this;
        }

        String keyUser = "User";
        String keyFullName = "FullName";
        String key = "Key";

        try {
            mSessionKey = obj.optString(key);
            if (obj.has(keyUser)) {
                mUserName = obj.getJSONObject(keyUser).optString(keyFullName);
            }
        } catch (Exception e) {
            CrashLogging.LogCrashException(e,"From UserInfoResponse.java class");
            mSessionKey = null;
            mUserName = null;
        }

        return this;
    }

    public String getSessionKey() {
        return mSessionKey;
    }

    public String getUserName() {
        return mUserName;
    }
}
