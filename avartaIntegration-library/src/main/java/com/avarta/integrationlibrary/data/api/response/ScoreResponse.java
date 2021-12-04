package com.avarta.integrationlibrary.data.api.response;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.avarta.integrationlibrary.utils.CrashLogging;
import com.avarta.integrationlibrary.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class ScoreResponse extends BaseResponse implements Parcelable {

    private double mBehavioSecScore;
    private double mBehavioSecConfidence;
    private double mBehavioSecAverageScore;
    private String mBehavioSecProfile;
    private double mBehavioSecScoreAge;
    private double mInauthScore;
    private double mInauthScoreAge;
    private double mWorkflowScore;
    private double mCombinedScore;
    // 2017-10-09T16:34:56.0183665Z
    private String mDateCaptured;

    public ScoreResponse() {
        super();
    }

    @Override
    public BaseResponse parse(JSONObject mainObj) throws JSONException {
        if (mainObj == null) {
            return this;
        }

        try {
            String behaviosecScoreField = "BehaviosecScore";
            if (jsonHasField(mainObj, behaviosecScoreField)) {
                mBehavioSecScore = mainObj.getDouble(behaviosecScoreField);
            }

            String behaviosecConfidenceField = "BehaviosecConfidence";
            if (jsonHasField(mainObj, behaviosecConfidenceField)) {
                mBehavioSecConfidence = mainObj.getDouble(behaviosecConfidenceField);
            }

            String behaviosecAverageField = "BehaviosecAverageScore";
            if (jsonHasField(mainObj, behaviosecAverageField)) {
                mBehavioSecAverageScore = mainObj.getDouble(behaviosecAverageField);
            }

            String behaviosecProfileField = "BehaviosecProfile";
            if (jsonHasField(mainObj, behaviosecProfileField)) {
                mBehavioSecProfile = mainObj.getString(behaviosecProfileField);
            }

            String behaviosecScoreAgeField = "BehaviosecScoreAge";
            if (jsonHasField(mainObj, behaviosecScoreAgeField)) {
                mBehavioSecScoreAge = mainObj.getDouble(behaviosecScoreAgeField);
            }

            String inauthScoreField = "InAuthScore";
            if (jsonHasField(mainObj, inauthScoreField)) {
                mInauthScore = mainObj.getDouble(inauthScoreField);
            }

            String inauthScoreAgeField = "InAuthScoreAge";
            if (jsonHasField(mainObj, inauthScoreAgeField)) {
                mInauthScoreAge = mainObj.getDouble(inauthScoreAgeField);
            }

            String workflowScoreField = "WorkflowScore";
            if (jsonHasField(mainObj, workflowScoreField)) {
                mWorkflowScore = mainObj.getDouble(workflowScoreField);
            }

            String combinedField = "CombinedScore";
            if (jsonHasField(mainObj, combinedField)) {
                mCombinedScore = mainObj.getDouble(combinedField);
            }

            String dateCapturedField = "DateCaptured";
            if (jsonHasField(mainObj, dateCapturedField)) {
                mDateCaptured = mainObj.getString(dateCapturedField);
            }

            Logger.i(this, toString());

        } catch (JSONException e) {
            CrashLogging.LogCrashException(e,"From ScoreResponse.java class : "+mainObj!=null?mainObj.toString():" null response");
            e.printStackTrace();
        }

        return this;
    }

    private boolean jsonHasField(JSONObject jsonObject, String fieldName) {
        return jsonObject.has(fieldName) && !jsonObject.isNull(fieldName);
    }

    public double getBehavioSecScore() {
        return mBehavioSecScore;
    }

    public double getBehavioSecConfidence() {
        return mBehavioSecConfidence;
    }

    public double getBehavioSecAverageScore() {
        return mBehavioSecAverageScore;
    }

    public String getBehavioSecProfile() {
        return mBehavioSecProfile;
    }

    public double getBehavioSecScoreAge() {
        return mBehavioSecScoreAge;
    }

    public double getInauthScore() {
        return mInauthScore;
    }

    public double getInauthScoreAge() {
        return mInauthScoreAge;
    }

    public double getWorkflowScore() {
        return mWorkflowScore;
    }

    public double getCombinedScore() {
        return mCombinedScore;
    }

    public String getDateCaptured() {
        return mDateCaptured;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.mBehavioSecScore);
        dest.writeDouble(this.mBehavioSecConfidence);
        dest.writeDouble(this.mBehavioSecAverageScore);
        dest.writeString(this.mBehavioSecProfile);
        dest.writeDouble(this.mBehavioSecScoreAge);
        dest.writeDouble(this.mInauthScore);
        dest.writeDouble(this.mInauthScoreAge);
        dest.writeDouble(this.mWorkflowScore);
        dest.writeDouble(this.mCombinedScore);
        dest.writeString(this.mDateCaptured);
    }

    protected ScoreResponse(Parcel in) {
        this.mBehavioSecScore = in.readDouble();
        this.mBehavioSecConfidence = in.readDouble();
        this.mBehavioSecAverageScore = in.readDouble();
        this.mBehavioSecProfile = in.readString();
        this.mBehavioSecScoreAge = in.readDouble();
        this.mInauthScore = in.readDouble();
        this.mInauthScoreAge = in.readDouble();
        this.mWorkflowScore = in.readDouble();
        this.mCombinedScore = in.readDouble();
        this.mDateCaptured = in.readString();
    }

    public static final Parcelable.Creator<ScoreResponse> CREATOR = new Parcelable.Creator<ScoreResponse>() {
        @Override
        public ScoreResponse createFromParcel(Parcel source) {
            return new ScoreResponse(source);
        }

        @Override
        public ScoreResponse[] newArray(int size) {
            return new ScoreResponse[size];
        }
    };

    @Override
    public String toString() {
        StringBuffer logsBuilder = new StringBuffer("");

        logsBuilder
                .append("mBehavioSecScore - ").append(String.valueOf(mBehavioSecScore))
                .append("mBehavioSecConfidence - ").append(String.valueOf(mBehavioSecConfidence))
                .append("mBehavioSecAverageScore - ").append(String.valueOf(mBehavioSecAverageScore))
                .append("mBehavioSecProfile - ").append(String.valueOf(mBehavioSecProfile))
                .append("mBehavioSecScoreAge - ").append(String.valueOf(mBehavioSecScoreAge))
                .append("mInauthScore - ").append(String.valueOf(mInauthScore))
                .append("mInauthScoreAge - ").append(String.valueOf(mInauthScoreAge))
                .append("mWorkflowScore - ").append(String.valueOf(mWorkflowScore))
                .append("mCombinedScore - ").append(String.valueOf(mCombinedScore))
                .append("mDateCaptured - ").append(String.valueOf(mDateCaptured));

        return logsBuilder.toString();
    }
}
