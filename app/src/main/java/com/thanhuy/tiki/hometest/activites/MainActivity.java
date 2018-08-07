package com.thanhuy.tiki.hometest.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.thanhuy.tiki.hometest.R;
import com.thanhuy.tiki.hometest.adapter.DealRecyclerViewAdapter;
import com.thanhuy.tiki.hometest.data.response.Deal;
import com.thanhuy.tiki.hometest.interfaces.MainAsyncResponse;
import com.thanhuy.tiki.hometest.util.CountDownTimerManager;
import com.thanhuy.tiki.hometest.util.SpacesItemDecoration;
import com.thanhuy.tiki.hometest.util.async_task.ApiCallAsyncTask;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * author:uy.daonguyen@gmail.com
 */
public class MainActivity extends AppCompatActivity implements MainAsyncResponse{
    private static final String TAG = MainActivity.class.getName();
    private AVLoadingIndicatorView loadingView;
    private RecyclerView recyclerViewDeal;
    private DealRecyclerViewAdapter dealRecyclerViewAdapter;
    private List<Deal> listDataFromAPi;
    private ScheduledExecutorService scheduleRealTimeExecutor;
    private ScheduledFuture<?> scheduledFutureJobHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewId();
        initData();
        new ApiCallAsyncTask(this).execute();
    }

    private void setupViewId(){
        loadingView = (AVLoadingIndicatorView) findViewById(R.id.loadingView);
        recyclerViewDeal = (RecyclerView) findViewById(R.id.recyclerViewDeal);
    }

    private void initData(){
        dealRecyclerViewAdapter = new DealRecyclerViewAdapter(null, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,
                2);
        layoutManager.setItemPrefetchEnabled(false); // Fix java.lang.IllegalArgumentException: Pixel distance must be non-negative
        recyclerViewDeal.setLayoutManager(layoutManager);
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(
                2,
                8,
                true);
        recyclerViewDeal.addItemDecoration(spacesItemDecoration);
        recyclerViewDeal.invalidateItemDecorations();
        recyclerViewDeal.setAdapter(dealRecyclerViewAdapter);
    }

    private void startScheduleGetDealProduct(){
        if(scheduleRealTimeExecutor == null) {
            scheduleRealTimeExecutor = Executors.newScheduledThreadPool(3);
        }
        if (scheduledFutureJobHandle == null ||
                scheduledFutureJobHandle != null && scheduledFutureJobHandle.isCancelled()) {
            scheduledFutureJobHandle = scheduleRealTimeExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    getDealProductFromApiData();
                }
            },0, 1, TimeUnit.SECONDS);
        }
    }

    private void stopScheduleGetDealProductExecutor(){
        if(scheduleRealTimeExecutor != null &&
                !scheduleRealTimeExecutor.isShutdown()){
            if(scheduledFutureJobHandle != null){
                scheduledFutureJobHandle.cancel(false);
            }
            scheduleRealTimeExecutor.shutdown();
            try {
                scheduleRealTimeExecutor.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e){
                Log.e(TAG, e.toString());
            }

            scheduledFutureJobHandle = null;
            scheduleRealTimeExecutor = null;

        }
    }

    private void getDealProductFromApiData(){
        if(listDataFromAPi != null){
            long currentTime = System.currentTimeMillis();
            Map<String, Boolean> mapUuidDealExpired = CountDownTimerManager.getInstance().getMapUuidDealExpired();
            for(Deal deal : listDataFromAPi){
                if(mapUuidDealExpired.get(deal.getUuid()) == null || !mapUuidDealExpired.get(deal.getUuid())){
                    if(deal.getStartedDate().getTime() <= currentTime){
                        CountDownTimerManager.getInstance().putDataCacheTime(dealRecyclerViewAdapter, deal.getUuid(), deal);
                    }
                }
            }

        }
    }

    @Override
    public void processStart() {
        loadingView.show();
    }

    @Override
    public void processFinish(List<Deal> listDeal) {
        listDataFromAPi = listDeal;
        loadingView.hide();
//        dealRecyclerViewAdapter.notifyData(listDeal);
    }

    @Override
    public void onPause() {
        super.onPause();
        CountDownTimerManager.getInstance().stop();
        stopScheduleGetDealProductExecutor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CountDownTimerManager.getInstance().start();
        startScheduleGetDealProduct();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
