package com.avarta.integrationlibrary.data.api;

class Const {

    /*GLOBAL REQUEST TAG FOR VOLLEY*/
    static final String GLOBAL_REQUEST_TAG = EncryptionConstants.getString(EncryptionConstants.GLOBAL_REQUEST_TAG);

    /*API URL*/
    static final String URI_GET_STATUS = EncryptionConstants.getString(EncryptionConstants.API) + EncryptionConstants.getString(EncryptionConstants.URI_GET_STATUS);
    static final String URI_PROCESS = EncryptionConstants.getString(EncryptionConstants.API) + EncryptionConstants.getString(EncryptionConstants.URI_PROCESS);
    static final String URI_START_WORKFLOW = EncryptionConstants.getString(EncryptionConstants.API) + EncryptionConstants.getString(EncryptionConstants.URI_START_WORKFLOW);
    static final String URI_COMPLETE = EncryptionConstants.getString(EncryptionConstants.API) + EncryptionConstants.getString(EncryptionConstants.URI_COMPLETE);
    static final String URI_CONFIGURATION = EncryptionConstants.getString(EncryptionConstants.API) + EncryptionConstants.getString(EncryptionConstants.URI_CONFIGURATION);
    static final String URI_SESSION = EncryptionConstants.getString(EncryptionConstants.API) + EncryptionConstants.getString(EncryptionConstants.URI_SESSION);
    static final String URI_ABORT = EncryptionConstants.getString(EncryptionConstants.API) + EncryptionConstants.getString(EncryptionConstants.URI_ABORT);
    static final String URI_CHECK_APP_VERSION = EncryptionConstants.getString(EncryptionConstants.API) + EncryptionConstants.getString(EncryptionConstants.URI_CHECK_APP_VERSION);
    static final String URI_CHANGE_PASSWORD = EncryptionConstants.getString(EncryptionConstants.API) + EncryptionConstants.getString(EncryptionConstants.URI_CHANGE_PASSWORD);
    static final String URI_BEHAVIOR_SEC_REPORT = EncryptionConstants.getString(EncryptionConstants.URI_BEHAVIOSEC);
    static final String URI_CALCULATE_USER_SCORES = EncryptionConstants.getString(EncryptionConstants.URI_CALCULATE_USER_SCORES);

    /*API CODES*/
    static final int UNKNOWN_SUCCESS_CODE = EncryptionConstants.getInt(EncryptionConstants.UNKNOWN_SUCCESS_CODE);
    static final int UNKNOWN_ERROR_CODE = EncryptionConstants.getInt(EncryptionConstants.UNKNOWN_ERROR_CODE);
    static final int ON_TIMEOUT_CODE = EncryptionConstants.getInt(EncryptionConstants.ON_TIMEOUT_CODE);

    /*API TIMEOUT*/
    static final int REQUEST_TIMEOUT_MS = EncryptionConstants.getInt(EncryptionConstants.REQUEST_TIMEOUT_MS);


    static final String AUTH_USER_DEF = EncryptionConstants.getString(EncryptionConstants.AUTH_USER_DEF);
    static final String AUTH_PASSWD_DEF = EncryptionConstants.getString(EncryptionConstants.AUTH_PASSWD_DEF);

    static final String SSL_ERROR = EncryptionConstants.getString(EncryptionConstants.SSL_ERROR);

    /*EXCEPTIONS MESSAGE ERRORS*/
    static final String EXCEPTION_FAILED_JSON = "Invalid JSON";
    static final String EXCEPTION_FAILED_WORKFLOW_PROCCESS = "Workflow proccess failed";
    static final String EXCEPTION_TIMEOUT = "Timeout error";

    static final String CONTENT_ENCODING_TYPE = "gzip";

}
