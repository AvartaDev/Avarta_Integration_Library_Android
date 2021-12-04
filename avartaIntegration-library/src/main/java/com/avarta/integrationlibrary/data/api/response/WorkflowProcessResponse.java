package com.avarta.integrationlibrary.data.api.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WorkflowProcessResponse extends BaseResponse {

    private String dateCreated;
    private String dateUpdated;
    private ArrayList<PendingTask> mPendingActivities;
    private String key;
    private int mStatus;
    private String publicKey;

    /**
     * true - if the workflow was started from the device
     * false - from the web part
     */
    private boolean mIsDeviceBased;
    private String salt;

    public WorkflowProcessResponse() {
        super();
    }

    @Override
    public BaseResponse parse(JSONObject response) throws JSONException {
        if (response == null) {
            return this;
        }

        setPublicKey(response.getString("PublicKey"));
        setKey(response.getString("Key"));
        setDateCreated(response.getString("DateCreated"));
        setDateUpdated(response.getString("DateUpdated"));
        setStatus(response.getInt("Status"));
        setSalt(response.getString("Salt"));
        mIsDeviceBased = response.optBoolean("DeviceBasedFlag");

        final JSONArray pAct = response.getJSONArray("PendingActivities");
        mPendingActivities = new ArrayList<>();
        if (pAct != null && pAct.length() > 0) {
            for (int i = 0; i < pAct.length(); i++) {
                try {
                    JSONObject mainActObj = pAct.getJSONObject(i);

                    String code = mainActObj.getString("Code");
                    String workflowType = mainActObj.getJSONObject("Type").getString("Code");

                    mPendingActivities.add(new PendingTask(code, workflowType));
                } catch (Exception ignore) {
                }
            }
        }

        return this;
    }

    public boolean getIsDeviceBased() {
        return mIsDeviceBased;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    private void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    private void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public ArrayList<PendingTask> getPendingActivities() {
        return mPendingActivities;
    }

    public String getSalt() {
        return salt;
    }

    private void setSalt(String salt) {
        this.salt = salt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String key) {
        this.publicKey = key;
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
                "dateCreated='" + dateCreated + '\'' +
                ", dateUpdated='" + dateUpdated + '\'' +
                ", mPendingActivities=" + mPendingActivities +
                ", key='" + key + '\'' +
                ", publickey='" + publicKey + '\'' +
                ", mStatus=" + mStatus +
                ", mIsDeviceBased=" + mIsDeviceBased +
                ", salt='" + salt + '\'' +
                '}';
    }
}
