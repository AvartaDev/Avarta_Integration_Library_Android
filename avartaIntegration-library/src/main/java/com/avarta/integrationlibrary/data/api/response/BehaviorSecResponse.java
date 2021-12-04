package com.avarta.integrationlibrary.data.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class BehaviorSecResponse extends BaseResponse implements Parcelable, Serializable {

    private double mScore;
    private double mConfidence;
    private String mPolicy;

    public BehaviorSecResponse() {
        super();
    }

    @Override
    public BaseResponse parse(JSONObject response) throws JSONException {
        if (response == null) {
            return this;
        }
        if (response.length() > 0) {
            setConfidence(response.getDouble("confidence"));
            setScore(response.getDouble("score"));
            setPolicy(response.getString("policy"));
        }
        return this;
    }

    public void setScore(double score) {
        mScore = score;
    }

    public void setConfidence(double confidence) {
        mConfidence = confidence;
    }

    public void setPolicy(String policy) {
        mPolicy = policy;
    }

    public double getScore() {
        return mScore;
    }

    public double getConfidence() {
        return mConfidence;
    }

    public String getPolicy() {
        return mPolicy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.mScore);
        dest.writeDouble(this.mConfidence);
        dest.writeString(this.mPolicy);
    }

    protected BehaviorSecResponse(Parcel in) {
        this.mScore = in.readDouble();
        this.mConfidence = in.readDouble();
        this.mPolicy = in.readString();
    }

    public static final Parcelable.Creator<BehaviorSecResponse> CREATOR = new Parcelable.Creator<BehaviorSecResponse>() {
        @Override
        public BehaviorSecResponse createFromParcel(Parcel source) {
            return new BehaviorSecResponse(source);
        }

        @Override
        public BehaviorSecResponse[] newArray(int size) {
            return new BehaviorSecResponse[size];
        }
    };
}
