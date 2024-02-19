package com.androhub.networkmodule;

import android.content.Context;


public class AppManager {
    private static AppManager appManager;
    private Context context;
    private PrefManager prefManager;


    public AppManager(Context mContext) {
        this.context = mContext;
    }

    public static AppManager getInstance(Context mContext) {
        if (appManager == null) {
            appManager = new AppManager(mContext);
        }
        return appManager;
    }

    public Context getApplicationContext() {
        return this.context;
    }

    public void setPrefManager(PrefManager prefManager) {
        this.prefManager = prefManager;
    }

    public PrefManager getPrefManager() {
        return this.prefManager;
    }


}
