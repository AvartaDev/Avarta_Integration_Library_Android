package com.avarta.integrationlibrary.interfaces;

import com.avarta.integrationlibrary.data.api.response.PendingTask;
import com.avarta.integrationlibrary.data.models.AvartaConfig;
import com.avarta.integrationlibrary.exceptions.*;

/**
 * The main listener of the workflow process, which helps to control the whole process
 * and track the process actions as abort, error or successful complete
 */
public interface IWorkflowProccessListener {

    /**
     * The method 'onWorkflowError()' calls when error was catched during the workflow process
     *
     * @param error        instance of catched exception
     * @param workflowType type of workflow instance, on which the error was catched
     * @param userName     name of user, for which the process was started
     * @see ApiErrorException
     * @see JsonParserException
     * @see WorkflowFailedException
     * @see WorkflowAbortedException
     */
    void onWorkflowError(Throwable error, String workflowType, String userName);

    /**
     * This method will be called, when SDK will get the configuration from the server
     *
     * @param avartaConfig - configuration object, that contains license keys, OS information, etc.
     *                    Notice, that it can be changed in the next versions of the integration library
     */
    void onConfigLoaded(AvartaConfig avartaConfig);

    /**
     * The method 'onWorkflowAbort()' calls when workflow was aborted after the catching error or
     * was run by the user
     *
     * @param workflowType type of workflow instance, which was aborted
     * @param userName     name of user, for which the process was started
     */
    void onWorkflowAbort(String workflowType, String userName);

    /**
     * The method 'onContinueEnroll()' calls when SDK get the new process activity from the server
     *
     * @param workflowType type of workflow instance, which was aborted
     * @param pendingTask  pending task that was came from the API response
     * @see PendingActivity
     */
    void onContinueEnroll(String workflowType, PendingTask pendingTask);

    /**
     * The method 'onWorkflowCompleted()' called when workflow process completes successfully
     *
     * @param workflowType  type of workflow instance, which was aborted
     * @param userName      name of user, for which the process was started
     * @param isDeviceBased true - if the workflow was started from the device, false - from the web application
     */
    void onWorkflowCompleted(String workflowType, String userName, boolean isDeviceBased);
}