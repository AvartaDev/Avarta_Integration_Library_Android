package com.avarta.integrationlibrary.data.api.response;

import android.text.TextUtils;

import com.avarta.integrationlibrary.enums.WorkflowType;
import com.avarta.integrationlibrary.utils.CrashLogging;
import com.avarta.integrationlibrary.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationResponse extends BaseResponse {

    private String mApplicationCode;
    private String mName;
    private boolean mIsDeviceBased;
    private ApiWorkflow[] mApiWorkflows;
    private ApiLicense[] mApiLicenses;
    private DeviceConfiguration mDeviceConfiguration;

    public ConfigurationResponse() {
        super();
    }

    @Override
    public BaseResponse parse(JSONObject mainObj) throws JSONException {
        if (mainObj == null) {
            return this;
        }

        try {
            String codeField = "Code";
            String nameField = "Name";

            if (jsonHasField(mainObj, codeField)) {
                mApplicationCode = mainObj.getString(codeField);
            }

            if (jsonHasField(mainObj, nameField)) {
                mName = mainObj.getString(nameField);
            }

            String isDeviceBasedField = "IsDeviceBased";
            if (jsonHasField(mainObj, isDeviceBasedField)) {
                mIsDeviceBased = mainObj.getBoolean(isDeviceBasedField);
            }

            String workflowsField = "Workflows";
            if (jsonHasField(mainObj, workflowsField)) {
                JSONArray workflowsArray = mainObj.getJSONArray(workflowsField);
                mApiWorkflows = new ApiWorkflow[workflowsArray.length()];

                for (int i = 0; i < workflowsArray.length(); i++) {
                    JSONObject workflowObj = workflowsArray.getJSONObject(i);
                    ApiWorkflow apiWorkflow = new ApiWorkflow();

                    if (jsonHasField(workflowObj, codeField)) {
                        apiWorkflow.setCode(workflowObj.getString(codeField));
                    }

                    String keyField = "Key";
                    if (jsonHasField(workflowObj, keyField)) {
                        apiWorkflow.setKey(workflowObj.getString(keyField));
                    }

                    mApiWorkflows[i] = apiWorkflow;
                }
            }

            mDeviceConfiguration = new DeviceConfiguration();
            JSONObject configObj = mainObj.optJSONObject("DeviceConfiguration");
            if (configObj != null) {
                mDeviceConfiguration.setApkVersion(configObj.optInt("Version", 0));
                mDeviceConfiguration.setApkUrl(configObj.optString("Location", ""));
                mDeviceConfiguration.setPinLength(configObj.optInt("PINLength", 0));
            }

            String licencesField = "Licences";
            if (jsonHasField(mainObj, licencesField)) {
                JSONArray licencesArray = mainObj.getJSONArray(licencesField);
                mApiLicenses = new ApiLicense[licencesArray.length()];

                for (int i = 0; i < licencesArray.length(); i++) {
                    JSONObject licenseObj = licencesArray.getJSONObject(i);
                    ApiLicense apiLicense = new ApiLicense();

                    apiLicense.setKey(licenseObj.optString("Key", ""));
                    apiLicense.setStartDate(licenseObj.optString("StartDate", ""));
                    apiLicense.setEndDate(licenseObj.optString("EndDate", ""));

                    String providerField = "Provider";
                    if (jsonHasField(licenseObj, providerField)) {
                        JSONObject providerObj = licenseObj.getJSONObject(providerField);
                        ApiAttribute providerAttribute = new ApiAttribute();

                        if (jsonHasField(providerObj, codeField)) {
                            providerAttribute.setCode(providerObj.getString(codeField));
                        }

                        if (jsonHasField(providerObj, nameField)) {
                            providerAttribute.setName(providerObj.getString(nameField));
                        }

                        apiLicense.setProvider(providerAttribute);
                    }

                    String osField = "OperatingSystem";
                    if (jsonHasField(licenseObj, osField)) {
                        JSONObject osObj = licenseObj.getJSONObject(osField);
                        ApiAttribute osAttribute = new ApiAttribute();

                        if (jsonHasField(osObj, codeField)) {
                            osAttribute.setCode(osObj.getString(codeField));
                        }

                        if (jsonHasField(osObj, nameField)) {
                            osAttribute.setName(osObj.getString(nameField));
                        }

                        apiLicense.setOperationSystem(osAttribute);
                    }

                    mApiLicenses[i] = apiLicense;
                }
            }

            Logger.i(this, toString());
        } catch (JSONException e) {
            CrashLogging.LogCrashException(e,"Exception while parsing response from ConfigurationResponse : "+mainObj!=null?mainObj.toString():"");
            e.printStackTrace();
        }

        return this;
    }

    private boolean jsonHasField(JSONObject jsonObject, String fieldName) {
        return jsonObject.has(fieldName) && !jsonObject.isNull(fieldName);
    }

    public Map<WorkflowType, String> parseWorkflowKeys() {
        Map<WorkflowType, String> result = new HashMap<>();

        if (getApiWorkflows() != null) {
            for (ApiWorkflow worflow : getApiWorkflows()) {
                WorkflowType workflowType = WorkflowType.fromKey(worflow.getCode());

                if (workflowType != null) {
                    result.put(workflowType, worflow.getKey());
                }
            }
        }

        return result;
    }

    public DeviceConfiguration getDeviceConfiguration() {
        return mDeviceConfiguration;
    }

    public String getWorkflowKey(String workflowType) {
        if (workflowType == null) {
            throw new IllegalArgumentException();
        }

        if (mApiWorkflows == null) {
            return null;
        }

        for (ApiWorkflow apiWorkflowObj : mApiWorkflows) {
            if (!TextUtils.isEmpty(apiWorkflowObj.getKey())
                    && apiWorkflowObj.getCode().equalsIgnoreCase(workflowType)) {
                return apiWorkflowObj.getKey();
            }
        }

        return null;
    }

    public String getApplicationCode() {
        return mApplicationCode;
    }

    public String getName() {
        return mName;
    }

    public boolean getIsDeviceBased() {
        return mIsDeviceBased;
    }

    public ApiWorkflow[] getApiWorkflows() {
        return mApiWorkflows;
    }

    public ApiLicense[] getApiLicenses() {
        return mApiLicenses;
    }

    public String getEyeVerifyLicense() {
        if (mApiLicenses == null) {
            return null;
        }

        for (ApiLicense apiLicence : mApiLicenses) {
            if (apiLicence.getProvider() != null
                    && !TextUtils.isEmpty(apiLicence.getProvider().getCode())
                    && apiLicence.getProvider().getCode().equalsIgnoreCase("EYEVERIFY")) {
                return apiLicence.getKey();
            }
        }

        return null;
    }

    @Override
    public String toString() {
        String log = "app code - " + getApplicationCode() + "\n" +
                "name - " + mName + "\n" +
                "deviceBased - " + mIsDeviceBased + "\n";

        log += "workflows: ";
        if (mApiWorkflows != null) {
            for (ApiWorkflow item : mApiWorkflows) {
                log += "workflow:\n" + item.toString() + "\n";
            }
        }

        log += "apiLicenses: ";
        if (mApiLicenses != null) {
            for (ApiLicense item : mApiLicenses) {
                log += "apiLicenses:\n" + item.toString() + "\n";
            }
        }

        return log;
    }

    public static class ApiLicense {

        private ApiAttribute mProvider;
        private ApiAttribute mOperationSystem;
        private String mKey;
        private String mStartDate;
        private String mEndDate;

        public ApiLicense() {
            super();
        }

        public ApiAttribute getProvider() {
            return mProvider;
        }

        public ApiAttribute getOperationSystem() {
            return mOperationSystem;
        }

        public String getKey() {
            return mKey;
        }

        public String getStartDate() {
            return mStartDate;
        }

        public String getEndDate() {
            return mEndDate;
        }

        public void setProvider(ApiAttribute provider) {
            mProvider = provider;
        }

        public void setOperationSystem(ApiAttribute operationSystem) {
            mOperationSystem = operationSystem;
        }

        public void setKey(String key) {
            mKey = key;
        }

        public void setStartDate(String startDate) {
            mStartDate = startDate;
        }

        public void setEndDate(String endDate) {
            mEndDate = endDate;
        }

        @Override
        public String toString() {
            return "provider - " + mProvider.toString() + "\n"
                    + "operation system - " + mOperationSystem.toString() + "\n"
                    + "key - " + mKey;
        }
    }

    public static class ApiWorkflow {

        private String mCode;
        private String mKey;

        public ApiWorkflow() {
            super();
        }

        public String getCode() {
            return mCode;
        }

        public String getKey() {
            return mKey;
        }

        public void setCode(String code) {
            mCode = code;
        }

        public void setKey(String key) {
            mKey = key;
        }

        @Override
        public String toString() {
            return "key - " + mKey + "\n" +
                    "code - " + mCode + "\n";
        }
    }

    public static class ApiAttribute {

        private String mCode;
        private String mName;

        public ApiAttribute() {
        }

        public void setCode(String code) {
            mCode = code;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getCode() {
            return mCode;
        }

        public String getName() {
            return mName;
        }

        @Override
        public String toString() {
            return "name - " + mName + "\n"
                    + "code - " + mCode;
        }
    }

    public static class DeviceConfiguration {

        private int mApkVersion;
        private String mApkUrl;
        private int mPinLength;

        public DeviceConfiguration() {
        }

        public int getApkVersion() {
            return mApkVersion;
        }

        public void setApkVersion(int apkVersion) {
            mApkVersion = apkVersion;
        }

        public String getApkUrl() {
            return mApkUrl;
        }

        public void setApkUrl(String apkUrl) {
            mApkUrl = apkUrl;
        }

        public int getPinLength() {
            return mPinLength;
        }

        public void setPinLength(int pinLength) {
            mPinLength = pinLength;
        }
    }
}
