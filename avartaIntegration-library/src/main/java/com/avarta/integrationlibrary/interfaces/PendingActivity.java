package com.avarta.integrationlibrary.interfaces;

import com.avarta.integrationlibrary.enums.WorkflowType;

/**
 * Each workflow process consist of few stages - pending activities, which returns from the server
 * and should be processed on the mobile side and returns the data back to the server through the method:
 *
 * @see IWorkflowProccessListener#onContinueEnroll)
 */
public interface PendingActivity {

    String CODE_DEVICE = "DEVICE";
    String CODE_PASSWORD = "PASSWORD";
    String CODE_EYEVERIFY = "EYEVERIFY";
    String CODE_DYNAMICPIN = "DYNAMICPIN";
    String CODE_NOTIFICATIONPUSHID = "NOTIFICATIONPUSHID";
    String CODE_LOCATION = "GEOLOCATION";
    String CODE_DELETEEVUSER = "DELETEEVUSER";
    String CODE_VALIDATEENROLCODE = "VALIDATEENROLCODE";
    String CODE_DEVICE_CAP = "DEVICECAP";
    String CODE_APPVERSION = "APPVERSION";
    String CODE_REGISTER_INAUTH_DEVICE = "REGISTERINAUTHDEVICE";
    String CODE_CAPTURE_INAUTH_LOGS = "CAPTUREINAUTHLOGS";
    String CODE_ENROLL_ZOOM_SECRET = "ENROLZOOMSECRET";
    String CODE_VALIDATE_ZOOM_SECRET = "VALIDATEZOOMSECRET";
}
