package com.thanhuy.tiki.hometest.util.async_task;

import android.os.AsyncTask;

import com.thanhuy.tiki.hometest.application.AppConfig;
import com.thanhuy.tiki.hometest.data.ApiServices;
import com.thanhuy.tiki.hometest.data.response.Deal;
import com.thanhuy.tiki.hometest.interfaces.MainAsyncResponse;

import java.lang.ref.WeakReference;
import java.util.List;

public class ApiCallAsyncTask extends AsyncTask<Void, String, Void> {
    private final WeakReference<MainAsyncResponse> delegate;

    /**
     * Constructor to set the delegate
     *
     * @param delegate Called when a port scan has finished
     */
    public ApiCallAsyncTask(MainAsyncResponse delegate) {
        this.delegate = new WeakReference<>(delegate);
    }



    @Override
    protected Void doInBackground(Void... voids) {
        final MainAsyncResponse activity = delegate.get();
        AppConfig.runInApplicationThread(new Runnable() {
            @Override
            public void run() {
                activity.processStart();
            }
        });

        final List<Deal> listDeal = ApiServices.getInstance().getDeals();
        AppConfig.runInApplicationThread(new Runnable() {
            @Override
            public void run() {
                activity.processFinish(listDeal);
            }
        });

        return null;
    }
}
