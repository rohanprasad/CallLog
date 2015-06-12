package com.rohanprasad.calllog.holder;

import android.net.Uri;

import java.util.Date;

/**
 * Created by Rohan on 11-06-2015.
 */
public class CallLogItem {

    private String userId;
    private String name;
    private Uri userImageId;
    private String time;
    private long duration;
    private String phoneNumber;
    private int callStatus;
    private Date date;
    private String durationString;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUserImageId() {
        return userImageId;
    }

    public void setUserImageId(Uri userImageId) {
        this.userImageId = userImageId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(int callStatus) {
        this.callStatus = callStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDurationString() {
        return durationString;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
