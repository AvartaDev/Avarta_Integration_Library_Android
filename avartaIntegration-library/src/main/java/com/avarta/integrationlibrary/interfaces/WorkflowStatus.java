package com.avarta.integrationlibrary.interfaces;

/**
 * During workflow process, server returns the status of the workflow,
 * which determines the future handling of the API response inside the integration library
 */
public interface WorkflowStatus {
    int STATUS_PROCESS = 0;
    int STATUS_DONE = 1;
    int STATUS_FAIL = 2;
    int STATUS_ABORT = 3;
}
