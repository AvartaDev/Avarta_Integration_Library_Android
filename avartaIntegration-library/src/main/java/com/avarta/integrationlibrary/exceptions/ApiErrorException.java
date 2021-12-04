package com.avarta.integrationlibrary.exceptions;

import android.content.Context;

import com.avarta.integrationlibrary.utils.MainHelper;
import com.avarta.integrationlibrary.R;

public class ApiErrorException extends Exception {

    private int mErrorCode;

    public ApiErrorException(int errorCode) {
        super("API error");

        this.mErrorCode = errorCode;
    }

    public String getErrorDescription(Context context, int errorCode) {
        if (context == null) {
            throw new IllegalArgumentException("Context should not be null");
        }

        String[] apiErrorMessagesArray = MainHelper.getStringArray(context, R.array.api_errors);

        if (errorCode < apiErrorMessagesArray.length) {
            return apiErrorMessagesArray[errorCode];
        }

        return MainHelper.getStringRes(context, R.string.error_server_unknown);
    }

    public int getErrorCode() {
        return mErrorCode;
    }
}
