package com.thanhuy.tiki.hometest.application;

import android.app.Application;
import android.content.Context;
import android.os.HandlerThread;
import android.os.Handler;
import android.os.HandlerThread;


public class AppConfig extends Application {
    private static Context sContext;
    private static Handler applicationHandler = new Handler();

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static void runInApplicationThread(Runnable r) {
        applicationHandler.post(r);
    }
}
