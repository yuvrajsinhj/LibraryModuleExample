package com.androhub.networkmodule;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.androhub.networkmodule.local.LocaleManager;
import com.androhub.networkmodule.local.Utility;
import com.androhub.networkmodule.utils.Utils;
import com.androhub.networkmodule.utils.broadcast.ConnectivityReceiver;

import org.jetbrains.annotations.NotNull;


public class MyApplication extends Application {

    private static AppManager appManager;
    private static Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private PrefManager prefManager;
//    public static  RudderClient rudderClient;

    public static LocaleManager localeManager;
    public static final String TAG = "MyApplication";

    public MyApplication(@NotNull Context mainActivity) {
        context= mainActivity;
    }
//    public static String sDefSystemLanguage;

    public static AppManager getAppManager() {
        if (appManager == null) {
            appManager = AppManager.getInstance(context);
        }
        return appManager;
    }

//    public static RudderClient getRudderClient() {
//        return rudderClient;
//    }



    public static Context getAppContext() {
        return context;
    }

    public static void setAppManager(AppManager am) {
        appManager = am;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (context == null) {
            context = getApplicationContext();
        }

        initBasic();
//        sDefSystemLanguage = Locale.getDefault().getLanguage();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Utility.bypassHiddenApiRestrictions();


    }

    private void initBasic() {
        if (appManager == null) {
            appManager = AppManager.getInstance(context);
        }
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PrefConst.PREF_FILE, MODE_PRIVATE);
        }
        if (editor == null) {
            editor = sharedPreferences.edit();
        }
        if (prefManager == null) {
            prefManager = new PrefManager(sharedPreferences, editor);
        }
        appManager.setPrefManager(prefManager);



    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    @Override
    protected void attachBaseContext(Context base) {
//        localeManager = new LocaleManager(base);
//        super.attachBaseContext(localeManager.setLocale(base));
        Utils.print(TAG, "attachBaseContext");
        MultiDex.install(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        sDefSystemLanguage = newConfig.locale.getLanguage();
//        localeManager.setLocale(this);
        Utils.print(TAG, "onConfigurationChanged: " + newConfig.locale.getLanguage());
    }


    public static void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


    public static void setApplicationlanguage(Context context) {

    }

}
