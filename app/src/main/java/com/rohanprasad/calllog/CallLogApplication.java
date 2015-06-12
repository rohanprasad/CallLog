package com.rohanprasad.calllog;

import android.app.Application;

/**
 * Created by Rohan on 11-06-2015.
 */
public class CallLogApplication extends Application{

    CallLogApplication mCallLogApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        mCallLogApplication = this;
    }

    public CallLogApplication getInstance(){

        if(this.mCallLogApplication == null){
            this.mCallLogApplication = new CallLogApplication();
        }

        return this.mCallLogApplication;
    }
}
