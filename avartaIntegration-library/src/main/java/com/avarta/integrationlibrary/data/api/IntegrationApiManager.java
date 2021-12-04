package com.avarta.integrationlibrary.data.api;

import android.content.Context;
import android.text.TextUtils;

//import com.crashlytics.android.Crashlytics;
import com.avarta.integrationlibrary.data.api.response.BaseResponse;
//import com.avarta.integrationlibrary.data.api.response.BehaviorSecResponse;
import com.avarta.integrationlibrary.data.api.response.PendingTask;
import com.avarta.integrationlibrary.data.api.response.ScoreResponse;
import com.avarta.integrationlibrary.interfaces.ApplicationCode;
import com.avarta.integrationlibrary.interfaces.IBaseApiListener;
import com.avarta.integrationlibrary.interfaces.IWorkflowProccessListener;
import com.avarta.integrationlibrary.interfaces.PendingActivity;
import com.avarta.integrationlibrary.utils.CrashLogging;

import org.json.JSONObject;

import java.util.Map;

/**
 * The main class of the integration library sdk, which controls the whole worflow process
 */
public class IntegrationApiManager {

    private RequestManager mRequestManager;

    /**
     * Constructor of the class, which create the instance of the library manager with special base domain url
     * and organization key
     *
     * @param baseUrl         indicate the base url of the api, where all requests will be sent.
     *                        For example - https://google.com/
     * @param organizationKey your organization key, which will be used to authorize your requests on the server
     */
    public IntegrationApiManager(String baseUrl, String organizationKey,Context context) {
        CrashLogging.initFabric(context);
        this.mRequestManager = new RequestManager(baseUrl, organizationKey);
    }

    /**
     * Constructor of the class, which create the instance of the library manager with special base domain urlr
     * and organization key
     *
     * @param baseUrl            indicate the base url of the api, where all requests will be sent.
     *                           For example - https://google.com/
     * @param organizationKey    your organization key, which will be used to authorize your requests on the server
     * @param flagForCompression true/false for behavsec compression
     */
//    public IntegrationApiManager(String baseUrl, String organizationKey, boolean flagForCompression,Context context) {
//        CrashLogging.initFabric(context);
//        this.mRequestManager = new RequestManager(baseUrl, organizationKey, flagForCompression);
//    }


    /**
     * Method will change replace the current organization key
     *
     * @param organizationKey update the current organization key
     */
    public void switchOrganizationKey(String organizationKey) {
        if (TextUtils.isEmpty(organizationKey)) {
            throw new IllegalArgumentException("invalid organization key");
        }

        this.mRequestManager.setOrganizationKey(organizationKey);
    }

    /**
     * Method will change replace the current base url with another one
     *
     * @param baseUrl update the base url of the api, where all requests will be sent.
     *                For example - https://google.com/
     */
    public void switchBaseUrl(String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new IllegalArgumentException("invalid server url");
        }

        this.mRequestManager.setServerUrl(baseUrl);
    }

    /**
     * Set the listener for the whole workflow process and manage the process on the mobile side
     *
     * @param proccessListener instance of the interface to manage the workflow process actions
     */
    public void setProcessListener(IWorkflowProccessListener proccessListener) {
        mRequestManager.setProccessListener(proccessListener);
    }

    /**
     * Method will executer request for changing user password
     *
     * @param context     pass the application context into the method
     * @param userName    username, for which password will be reset
     * @param oldPassword current user password
     * @param newPassword new user password
     * @param listener    if password was changed successfully {@link IBaseApiListener#onSuccess(Object)} will be called,
     *                    otherwise {@link IBaseApiListener#onError(Exception)}}
     */
    public void changeUserPassword(Context context, String userName, String oldPassword, String newPassword, IBaseApiListener<BaseResponse> listener) {
        mRequestManager.changePassword(context, userName, oldPassword, newPassword, listener);
    }

    /**
     * Start workflow process for resetting password
     *
     * @param applicationCode the code of the application {@link ApplicationCode}
     * @param context         pass the application context into the method
     * @param userName        username, for which workflow will be started
     * @param workflowKey     workflow key for resetting password
     */
    public void resetPassword( String applicationCode, Context context, String workflowType, String userName, String workflowKey) {
        mRequestManager.startWorkflow(context, workflowType, userName, workflowKey, applicationCode);
    }

    /**
     * Method will start the workflow process with checking the active pending activities
     *
     * @param applicationCode the code of the application {@link ApplicationCode}
     * @param context         pass the application context into the method
     * @param workflowType    the type of the workflow, which will be executed
     * @param userName        username, for which workflow will be started
     */
    public void startWorkflow( String applicationCode, Context context,
                              String workflowType, String userName) {
        startWorkflow(applicationCode, context, workflowType, userName, null, true);
    }

    /**
     * Method will start the workflow process, you can specify to check the pending activities or not
     *
     * @param applicationCode        the code of the application {@link ApplicationCode}
     * @param context                pass the application context into the method
     * @param workflowType           the type of the workflow, which will be executed
     * @param userName               username, for which workflow will be started
     * @param checkPendingActivities boolean flag determines should the integration library check
     *                               on existing workflows before it will start the new one
     */
    public void startWorkflow( String applicationCode, Context context,
                              String workflowType, String userName, boolean checkPendingActivities) {
        startWorkflow(applicationCode, context, workflowType, userName, null, checkPendingActivities);
    }

    /**
     * Method will start the worklow process with a specific key
     * If @param workflowKey is null or empty, workflow key will be pick from the server response
     * according to the workflow type
     *
     * @param applicationCode        the code of the application {@link ApplicationCode}
     * @param context                pass the application context into the method
     * @param workflowType           the type of the workflow, which will be executed
     * @param userName               username, for which workflow will be started
     * @param workflowKey            pass null, if you want to use key from the server. In other cases
     *                               your param key will be used instead of the server key
     * @param checkPendingActivities boolean flag determines should the integration library check
     *                               on existing workflows before it will start the new one
     */
    public void startWorkflow( String applicationCode, Context context, String workflowType,
                              String userName, String workflowKey, boolean checkPendingActivities) {
        mRequestManager.loadConfiguration(applicationCode, context, workflowType, userName, workflowKey, checkPendingActivities);
    }

    /**
     * Worklflow process constist of the few stages - process activities. After starting workflow, users of this SDK get the task {@link PendingActivity}
     * from the integration library in the callback {@link IWorkflowProccessListener#onContinueEnroll(String, PendingTask)}}
     * and after handling the corresponding {@link PendingActivity} developer should pass
     * the data back into the integration library through the method {@link IntegrationApiManager#processActivityData}
     *
     * @param context      pass the application context into the method
     * @param activityCode code of the pending activity which developer will get in the {@link IWorkflowProccessListener#onContinueEnroll}
     * @param activityData string data which developer will be get after the handling corresponding pending activity {@link PendingActivity}
     */
    public void processActivityData(Context context, final String activityCode, String activityData) {
        mRequestManager.sendEnrollData(context, activityCode, activityData);
    }

    /**
     * If you use behaviosek SDK <a href="https://www.behaviosec.com/">BehavioSec Service</a>, and your API has
     * configured route to handle the behavioSec data, you can use this method to send request and handle the API response
     * in the listener {@link IBaseApiListener}
     *
     * @param context        pass the application context into the method
     * @param behavioSecData this data will be generated by the behavioSec SDK BehavioTimings.getTimingsAsJSONString()
     * @param workflowType   the type of the workflow, which will be executed
     * @param userName       username, for which workflow will be started
     * @param listener       callback to handle the API response
     */
//    public void sendBehavioSecData(Context context, String behavioSecData, String workflowType,
//                                   String userName, IBaseApiListener<BehaviorSecResponse> listener) {
//        mRequestManager.sendBehavioSecData(context, userName, behavioSecData, workflowType, listener);
//    }

    /**
     * Use this method to send multipart request
     *
     * @param context          pass the application context into the method
     * @param url              full address where api will send request
     * @param data             array of bytes
     * @param responseListener callback to handle the API response
     */
    public void sendMultipartRequest(Context context, String url, byte[] data, IBaseApiListener<byte[]> responseListener) {
        mRequestManager.sendMultipartData(context, url, data, responseListener);
    }

    /**
     * Method for sending post request with {@link Map} params
     *
     * @param context       pass the application context into the method
     * @param endpoint      request address
     * @param responseClass response class
     * @param params        map with request params
     * @param listener      callback to handle the API response
     * @param <T>           describes the response object, users can extend the {@link BaseResponse} and override method
     *                      {@link BaseResponse#parse(JSONObject)} to handle the response by themselves
     */
    public <T extends BaseResponse> void sendPostRequest(Context context, String endpoint, Class<T> responseClass, Map<String, String> params, IBaseApiListener<T> listener) {
        mRequestManager.sendPost(context, endpoint, params, responseClass, listener);
    }

    /**
     * Method for sending get request
     */
    public <T extends BaseResponse> void sendGetRequest(Context context, String endpoint, Class<T> responseClass, IBaseApiListener<T> listener) {
        mRequestManager.sendGet(context, endpoint, responseClass, listener);
    }

    /**
     * Method for sending post request with json body as {@link String}
     *
     * @param context       pass the application context into the method
     * @param endpoint      request address
     * @param responseClass response class
     * @param jsonBody      json body as string
     * @param listener      callback to handle the API response
     * @param <T>           describes the response object, users can extend the {@link BaseResponse} and override method
     *                      {@link BaseResponse#parse(JSONObject)} to handle the response by themselves
     */
    public <T extends BaseResponse> void sendPostRequest(Context context, String endpoint, Class<T> responseClass, String jsonBody, IBaseApiListener<T> listener) {
        mRequestManager.sendPost(context, endpoint, jsonBody, responseClass, listener);
    }

    /**
     * This method will calculate the score. Note, that you can execute it only after successful completed workflow, in other
     * case you will get {@link IllegalStateException} in the {@link IWorkflowProccessListener#onWorkflowError(Throwable, String, String)}
     */
    public void calculateUserScore(Context context, IBaseApiListener<ScoreResponse> responseListener) {
        mRequestManager.calculateUserScore(context, responseListener);
    }

    /**
     * Developer can cancel the current workflow process with this method
     *
     * @param context pass the application context into the method
     */
    public void cancelWorkflow(Context context) {
        mRequestManager.abortWorkflow(context, true);
    }

    /**
     * This method will cancel all requests from the queue.
     * You can use it to handle your lifecycle in the application
     *
     * @param context pass the application context into the method
     */
    public void cancelAllRequests(Context context) {
        mRequestManager.removeApiListeners();
        mRequestManager.cancelAllRequests(context);
    }
}