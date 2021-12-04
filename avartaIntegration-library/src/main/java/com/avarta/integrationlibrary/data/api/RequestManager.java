package com.avarta.integrationlibrary.data.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.avarta.integrationlibrary.data.api.response.BaseResponse;
import com.avarta.integrationlibrary.data.api.response.ConfigurationResponse;
import com.avarta.integrationlibrary.data.api.response.PendingTask;
import com.avarta.integrationlibrary.data.api.response.ScoreResponse;
import com.avarta.integrationlibrary.data.api.response.WorkflowProcessResponse;
import com.avarta.integrationlibrary.data.helpers.ApiCryptManager;
import com.avarta.integrationlibrary.data.helpers.EllipticCurveKeys;
import com.avarta.integrationlibrary.data.models.AvartaConfig;
import com.avarta.integrationlibrary.data.models.UserInfoResponse;
import com.avarta.integrationlibrary.enums.WorkflowType;
import com.avarta.integrationlibrary.exceptions.ApiErrorException;
import com.avarta.integrationlibrary.exceptions.JsonParserException;
import com.avarta.integrationlibrary.exceptions.WorkflowAbortedException;
import com.avarta.integrationlibrary.exceptions.WorkflowFailedException;
import com.avarta.integrationlibrary.interfaces.ApiErrorCode;
import com.avarta.integrationlibrary.interfaces.IBaseApiListener;
import com.avarta.integrationlibrary.interfaces.WorkflowStatus;
import com.avarta.integrationlibrary.utils.CrashLogging;
import com.avarta.integrationlibrary.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.frank_durr.ecdh_curve25519.ECDHCurve25519;

class RequestManager extends BaseRequestManager {
    private String serverPublicKey;
    private String mWorkflowKey;
    private String mSecretSalt;
    private String mUserName;
    private String mSessionKey;
    private String mWorkflowType;
    private boolean mIsDeviceBased;

    RequestManager(String baseUrl, String organizationKey) {
        super(baseUrl, organizationKey);
    }

//    RequestManager(String baseUrl, String organizationKey, boolean flag) {
//        super(baseUrl, organizationKey);
//        compressionFlag = flag;
//    }

    void changePassword(final Context context, String userName, String oldPassword, String newPassword, IBaseApiListener<BaseResponse> listener) {
        CrashLogging.setUsernameToCrashLog(context, userName);
        String url = getServerUrl().concat(Const.URI_CHANGE_PASSWORD);

        final Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(RequestParams.USERNAME.toLowerCase(), userName);
        paramsMap.put(RequestParams.CURRENT_PASSWORD, oldPassword);
        paramsMap.put(RequestParams.NEW_PASSWORD, newPassword);

        BaseApi<?> postRequest = new BasePostRequest<>(getOrganizationKey())
                .send(paramsMap, url, BaseResponse.class, getRequestQueue(context))
                .addListener(listener);

        addRequest(postRequest);
    }

    void loadConfiguration(final String applicationCode, final Context context, final String workflowType,
                           final String userName, final String workflowKey, final boolean checkPendingActivities) {
        CrashLogging.setUsernameToCrashLog(context, userName);
        final Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(RequestParams.APPLICATION_CODE, applicationCode);
        paramsMap.put(RequestParams.OPERATION_SYSTEM_CODE, "ANDROID");

        final String url = getServerUrl().concat(Const.URI_CONFIGURATION);

        final BaseApi<ConfigurationResponse> configRequest = new BasePostRequest<ConfigurationResponse>(getOrganizationKey())
                .send(paramsMap, url, ConfigurationResponse.class, getRequestQueue(context))
                .addListener(new IBaseApiListener<ConfigurationResponse>() {
                    @Override
                    public void onSuccess(ConfigurationResponse data) {
                        if (data == null) {
                            CrashLogging.LogCrashException(new JsonParserException("Can`t parse config response"), " getting null response from loadConfiguration() => " + paramsMap != null ? paramsMap.toString() : "parameter null");
                            onHandleError(new JsonParserException("Can`t parse config response"));
                            return;
                        }

                        AvartaConfig avartaConfig = new AvartaConfig();
                        avartaConfig.setEyeVerifyLicense(data.getEyeVerifyLicense());
                        avartaConfig.setDeviceBased(data.getIsDeviceBased());

                        if (getProccessListener() != null) {
                            getProccessListener().onConfigLoaded(avartaConfig);
                        }

                        if (!TextUtils.isEmpty(workflowKey)) {
                            mWorkflowKey = workflowKey;
                        } else {
                            mWorkflowKey = data.getWorkflowKey(workflowType);
                        }

                        if (TextUtils.isEmpty(mWorkflowKey)) {
                            CrashLogging.LogCrashException(new IllegalStateException("Workflow key doesn`t found in API response from loadConfiguration() : " + data));
                            onHandleError(new IllegalStateException("Workflow key doesn`t found in API response from loadConfiguration() : " + data));
                            return;
                        }

                        if (checkPendingActivities) {
                            checkStatus(context, workflowType, userName, applicationCode);
                        } else {
                            mWorkflowType = workflowType;
                            mUserName = userName;
                            startWorkflow(context, workflowType, userName, applicationCode);
                        }
                    }

                    @Override
                    public void onError(Exception exception) {
                        CrashLogging.LogCrashException(exception, "WorkFlowType = " + workflowType + "\n" + url + "\n" + paramsMap.toString());
                        onHandleError(exception);
                    }
                });

        addRequest(configRequest);
    }

    private void checkStatus(final Context context, final String workflowType, final String userName, final String applicationCode) {
        CrashLogging.setUsernameToCrashLog(context, userName);

        this.mUserName = userName;
        this.mWorkflowType = workflowType;

        final String url = getServerUrl().concat(Const.URI_GET_STATUS);

        final Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(RequestParams.USERNAME, userName);
        paramsMap.put(RequestParams.WORKFLOW_TYPE_CODE, workflowType);
        paramsMap.put(RequestParams.CLIENT_PUBLIC_KEY, EllipticCurveKeys.publicBase64);

        BaseApi<WorkflowProcessResponse> statusRequest = new BasePostRequest<WorkflowProcessResponse>(getOrganizationKey())
                .send(paramsMap, url, WorkflowProcessResponse.class, getRequestQueue(context))
                .addListener(new IBaseApiListener<WorkflowProcessResponse>() {
                    @Override
                    public void onSuccess(WorkflowProcessResponse response) {
                        switch (response.getStatus()) {
                            case WorkflowStatus.STATUS_DONE:
                                updateWorkflowData(response);

                                if (response.getIsDeviceBased()) {
                                    sendComplete(context, workflowType, false);
                                } else {
                                    if (getProccessListener() != null) {
                                        getProccessListener().onWorkflowCompleted(workflowType, mUserName, mIsDeviceBased);
                                    }
                                }
                                break;

                            default:
                                handleWorkflowProcessResponse(response, context, workflowType);
                                break;
                        }
                    }

                    @Override
                    public void onError(Exception exception) {
                        if (exception instanceof ApiErrorException) {
                            CrashLogging.LogCrashException(exception, "WorkFlowType = " + workflowType + "\n" + url + "\n" + paramsMap.toString() + "\n" + "from checkStatus() api onerror method");
                            ApiErrorException apiErrorException = (ApiErrorException) exception;

                            switch (apiErrorException.getErrorCode()) {
                                case ApiErrorCode.WORKFLOW_NOT_FOUND:
                                    startWorkflow(context, workflowType, userName, applicationCode);
                                    return;
                            }
                        }

                        onHandleError(exception);
                    }
                });
        addRequest(statusRequest);
    }

    private void startWorkflow(final Context context, final String workflowType, String userName, String applicationCode) {
        CrashLogging.setUsernameToCrashLog(context, userName);
        startWorkflow(context, workflowType, userName, mWorkflowKey, applicationCode);
    }

    void startWorkflow(final Context context, final String workflowType, String userName, String workflowKey, String applicationCode) {
        CrashLogging.setUsernameToCrashLog(context, userName);
        this.mWorkflowType = workflowType;
        this.mWorkflowKey = workflowKey;

        final HashMap<String, String> paramsMap = new HashMap<>();

        paramsMap.put(RequestParams.SOURCE, String.format("%s Android", applicationCode));
        paramsMap.put(RequestParams.WORKFLOW_KEY, workflowKey);
        paramsMap.put(RequestParams.USERNAME, userName);
        paramsMap.put(RequestParams.FORCE_WORKFLOW_RESTART, "true");
        paramsMap.put(RequestParams.CLIENT_PUBLIC_KEY, EllipticCurveKeys.publicBase64);

        final String url = getServerUrl().concat(Const.URI_START_WORKFLOW);

        BaseApi<WorkflowProcessResponse> startWorkflowRequest = new BasePostRequest<WorkflowProcessResponse>(getOrganizationKey())
                .send(paramsMap, url, WorkflowProcessResponse.class, getRequestQueue(context))
                .addListener(new IBaseApiListener<WorkflowProcessResponse>() {
                    @Override
                    public void onSuccess(WorkflowProcessResponse response) {
                        handleWorkflowProcessResponse(response, context, workflowType);
                    }

                    @Override
                    public void onError(Exception exception) {
                        CrashLogging.LogCrashException(exception, "WorkFlowType = " + workflowType + "\n" + url + "\n" + paramsMap.toString());
                        onHandleError(exception);
                    }
                });
        addRequest(startWorkflowRequest);
    }

    private void sendComplete(final Context context, final String workflowType, final boolean completeAfterFailed) {
        this.mWorkflowType = workflowType;

        final HashMap<String, Object> paramsMap = new HashMap<>();
        boolean isStepUpWorkflow = isStepUpWorkflow(mWorkflowType);
        boolean createSession = !isStepUpWorkflow && !completeAfterFailed;

        paramsMap.put(RequestParams.WORKFLOW_INSTANCE_KEY, mWorkflowKey);
        paramsMap.put(RequestParams.CREATE_SESSION, createSession);

        if (isStepUpWorkflow) {
            paramsMap.put(RequestParams.UPDATE_SESSION_SCORE, true);
        }

        final String url = getServerUrl().concat(Const.URI_COMPLETE);

        BaseApi<UserInfoResponse> completeWorkflowRequest = new BasePostRequest<UserInfoResponse>(getOrganizationKey())
                .send(paramsMap, url, UserInfoResponse.class, getRequestQueue(context))
                .addListener(new IBaseApiListener<UserInfoResponse>() {
                    @Override
                    public void onSuccess(UserInfoResponse userInfo) {
                        mSessionKey = userInfo.getSessionKey();

                        if (completeAfterFailed) {
                            getProccessListener().onWorkflowError(new WorkflowFailedException(Const.EXCEPTION_FAILED_WORKFLOW_PROCCESS), workflowType, mUserName);
                        } else if (getProccessListener() != null) {
                            getProccessListener().onWorkflowCompleted(workflowType, mUserName, mIsDeviceBased);
                        }
                    }

                    @Override
                    public void onError(Exception exception) {
                        CrashLogging.LogCrashException(exception, "WorkFlowType = " + workflowType + "\n" + url + "\n" + paramsMap.toString());
                        onHandleError(exception);
                    }
                });
        addRequest(completeWorkflowRequest);
    }

    private boolean isStepUpWorkflow(String workflowType) {
        return !TextUtils.isEmpty(workflowType) && workflowType.toLowerCase().startsWith("step");
    }

    void abortWorkflow(Context context, final boolean cancelByUser) {
        final HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put(RequestParams.WORKFLOW_INSTANCE_KEY, mWorkflowKey);

        String url = getServerUrl() + Const.URI_ABORT;

        BaseApi<BaseResponse> aborWorkflowRequest = new BasePostRequest<>(getOrganizationKey())
                .send(paramsMap, url, BaseResponse.class, getRequestQueue(context))
                .addListener(new IBaseApiListener<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        if (getProccessListener() != null) {
                            if (cancelByUser) {
                                getProccessListener().onWorkflowAbort(mWorkflowType, mUserName);
                            } else {
                                getProccessListener().onWorkflowError(new WorkflowAbortedException(Const.EXCEPTION_FAILED_WORKFLOW_PROCCESS),
                                        mWorkflowType, mUserName);
                            }
                        }
                    }

                    @Override
                    public void onError(Exception exception) {
                        if (getProccessListener() != null) {
                            getProccessListener().onWorkflowError(exception, mWorkflowType, mUserName);
                        }
                    }
                });
        addRequest(aborWorkflowRequest);
    }

    byte[] getSharedSecretKey() {
        return ECDHCurve25519.generate_shared_secret(EllipticCurveKeys.SECRET_KEY, Base64.decode(serverPublicKey, Base64.DEFAULT));
    }

    void sendEnrollData(final Context context, final String activityCode, String activityData) {
        final String url = getServerUrl().concat(Const.URI_PROCESS);

        final Map<String, Object> params = new HashMap<>();

        byte[] key = getSharedSecretKey();
        String decryptedSalt = ApiCryptManager.decryptSalt(mSecretSalt, key);
        String encryptedItemData = ApiCryptManager.encryptItemData(activityData, decryptedSalt, key);

        params.put(RequestParams.WORKFLOW_INSTANCE_KEY, mWorkflowKey);
        params.put(RequestParams.ITEM_CODE, activityCode);
        params.put(RequestParams.ITEM_DATA, encryptedItemData);
        params.put(RequestParams.CLIENT_PUBLIC_KEY, EllipticCurveKeys.publicBase64);

        BaseApi<WorkflowProcessResponse> workflowRequest = new BasePostRequest<WorkflowProcessResponse>(getOrganizationKey())
                .send(params, url, WorkflowProcessResponse.class, getRequestQueue(context))
                .addListener(new IBaseApiListener<WorkflowProcessResponse>() {
                    @Override
                    public void onSuccess(WorkflowProcessResponse response) {
                        handleWorkflowProcessResponse(response, context, mWorkflowType);
                    }

                    @Override
                    public void onError(Exception exception) {
                        CrashLogging.LogCrashException(exception, url + "\n" + params.toString() + "\n" + "sendEnrollData() onError method");
                        onHandleError(exception);
                    }
                });
        addRequest(workflowRequest);
    }

    void calculateUserScore(Context context, IBaseApiListener<ScoreResponse> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Please, attach listener");
        }

        if (TextUtils.isEmpty(mSessionKey)) {
            listener.onError(new IllegalStateException("Failed to calulate scores. Session key is empty. At first, you need to complete successfully workflow process"));
            return;
        }

        final String url = getServerUrl().concat(Const.URI_CALCULATE_USER_SCORES);

        Map<String, String> params = new HashMap<>();
        params.put(RequestParams.SESSION_KEY, ApiCryptManager.encryptItemData(mSessionKey, "", getSharedSecretKey()));
        params.put(RequestParams.CLIENT_PUBLIC_KEY, EllipticCurveKeys.publicBase64);

        BaseApi<ScoreResponse> scoreRequest = new BasePostRequest<ScoreResponse>(getOrganizationKey())
                .send(params, url, ScoreResponse.class, getRequestQueue(context))
                .addListener(listener);

        addRequest(scoreRequest);
    }

//    void sendBehavioSecData(final Context context, String userName, String behavioSecData, String workflowType, IBaseApiListener<BehaviorSecResponse> listener) {
//        CrashLogging.setUsernameToCrashLog(context, userName);
//
//        String currentTime = Long.toString(System.currentTimeMillis());
//        String sessionId = currentTime.substring(currentTime.length() - 9, currentTime.length() - 1);
//        String userAgent = new WebView(context).getSettings().getUserAgentString();
//        String notes = WorkflowType.fromKey(workflowType) == WorkflowType.ENROL ? "enroll" : "auth";
//        String reportFlags = "0";
//        String operatorFlags = "0";
//
//        final Map<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("UserName", userName);
//        paramsMap.put("BehaviourData", behavioSecData);
//        paramsMap.put("UserAgent", userAgent);
//        paramsMap.put("SessionId", sessionId);
//        paramsMap.put("TimeStamp", currentTime);
//        paramsMap.put("Notes", notes);
//        paramsMap.put("ReportFlags", reportFlags);
//        paramsMap.put("OperatorFlags", operatorFlags);
//        paramsMap.put("UserIPAddress", "::1");
//
//        String url = getServerUrl() + Const.URI_BEHAVIOR_SEC_REPORT;
//
//        BaseApi<BehaviorSecResponse> behaviorSecRequest = new BasePostRequest<BehaviorSecResponse>(getOrganizationKey())
//                .sendForBehav(paramsMap, url, BehaviorSecResponse.class, getRequestQueue(context), compressionFlag)
//                .addListener(listener);
//        addRequest(behaviorSecRequest);
//    }

    <T extends BaseResponse> void sendPost(Context context, String endpoint, Map<String, String> params, Class<T> responseClass, IBaseApiListener<T> listener) {
        BaseApi<T> postRequest = new BasePostRequest<T>(getOrganizationKey())
                .send(params, endpoint, responseClass, getRequestQueue(context))
                .addListener(listener);

        addRequest(postRequest);
    }

    <T extends BaseResponse> void sendGet(Context context, String endpoint, Class<T> responseClass, IBaseApiListener<T> listener) {
        BaseApi<T> getRequest = new BaseGetRequest<T>()
                .send(endpoint, responseClass, getRequestQueue(context))
                .addListener(listener);

        addRequest(getRequest);
    }

    <T extends BaseResponse> void sendPost(Context context, String endpoint, String rawJson, Class<T> responseClass, IBaseApiListener<T> listener) {
        BaseApi<T> postRequest = new BasePostRequest<T>(getOrganizationKey())
                .send(rawJson, endpoint, responseClass, getRequestQueue(context))
                .addListener(listener);

        addRequest(postRequest);
    }

    void sendMultipartData(Context context, String url, byte[] data, IBaseApiListener<byte[]> responseListener) {
        BaseApi request = new MultipartRequest(getOrganizationKey())
                .send(url, data, getRequestQueue(context))
                .addListener(responseListener);
        addRequest(request);
    }

    void cancelAllRequests(Context context) {
        getRequestQueue(context).cancelAll(Const.GLOBAL_REQUEST_TAG);
    }

    private void handleWorkflowProcessResponse(WorkflowProcessResponse response, Context context, String workflowType) {
        updateWorkflowData(response);

        // TODO activity type is onlty auth and enroll - what should we do for stepup, etc?
        PendingTask nextPendingTask = null;
        boolean isEmptyActivities = response.getPendingActivities() == null
                || response.getPendingActivities().isEmpty()
                || (nextPendingTask = findNextPendingTask(response.getPendingActivities(), workflowType)) == null;

        if (response.getStatus() == WorkflowStatus.STATUS_ABORT) {
            if (getProccessListener() != null) {
                getProccessListener().onWorkflowAbort(workflowType, mUserName);
                return;
            }
        }

        if (response.getStatus() == WorkflowStatus.STATUS_FAIL) {
            sendComplete(context, workflowType, true);
            return;
        }

        if (response.getStatus() == WorkflowStatus.STATUS_DONE || isEmptyActivities) {
            if (response.getIsDeviceBased()) {
                sendComplete(context, workflowType, false);
            } else {
                if (getProccessListener() != null) {
                    getProccessListener().onWorkflowCompleted(workflowType, mUserName, mIsDeviceBased);
                }
            }
            return;
        }

        if (getProccessListener() != null) {
            getProccessListener().onContinueEnroll(workflowType, nextPendingTask);
        }
    }

    private PendingTask findNextPendingTask(ArrayList<PendingTask> pendingActivities, String workflowType) {
        if (pendingActivities == null) {
            throw new IllegalArgumentException();
        }

        String expectedWorkflow = WorkflowType.fromKey(workflowType) == WorkflowType.ENROL
                ? WorkflowType.ENROL.getApiParamKey() : WorkflowType.AUTH.getApiParamKey();

        for (PendingTask task : pendingActivities) {
            if (!TextUtils.isEmpty(task.getWorkflowType())
                    && expectedWorkflow.equalsIgnoreCase(task.getWorkflowType()))
                return task;
        }

        return null;
    }

    private void updateWorkflowData(WorkflowProcessResponse response) {
        if (response == null) {
            throw new IllegalArgumentException();
        }
        serverPublicKey = response.getPublicKey();
        mWorkflowKey = response.getKey();
        mSecretSalt = response.getSalt();
        mIsDeviceBased = response.getIsDeviceBased();
    }

    private void onHandleError(Exception e) {
        Logger.e(this, e);
        //CrashLogging.LogCrashException(e);
        if (getProccessListener() != null) {
            getProccessListener().onWorkflowError(e, mWorkflowType, mUserName);
        }
    }
}