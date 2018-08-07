package com.thanhuy.tiki.hometest.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import android.os.Handler;
import android.os.Message;

import com.thanhuy.tiki.hometest.adapter.DealRecyclerViewAdapter;
import com.thanhuy.tiki.hometest.application.AppConfig;
import com.thanhuy.tiki.hometest.data.response.Deal;
import com.thanhuy.tiki.hometest.interfaces.RenderTimerCallback;

/**
 *
 * author:uy.daonguyen@gmail.com
 */
public class CountDownTimerManager {
    private static final String KEY_CACHE_TIME_OF_COMPONENT = "KEY_CACHE_TIME_OF_COMPONENT";
    private static final String KEY_CACHE_RENDER_TIME = "KEY_CACHE_RENDER_TIME";
    private static final int HANDLE_MESSAGE_RENDER = 0X01;
    private Map<String, RenderTimerCallback> mMapCallbacksRedertime;
    // start test
    private Map<String, Deal> mMapComponentCacheTime;
    // end test
    private static final long UPDATE_MSG_INTERVAL = 1000l;
    private Map<String, Boolean> mMapUuidDealExpired;

    private CountDownTimerManager(){
        mMapCallbacksRedertime = new ConcurrentHashMap<>();
        mMapComponentCacheTime = new ConcurrentHashMap<>();
        mMapUuidDealExpired = new ConcurrentHashMap<>();
    }

    private Handler mCountDownHandle = new Handler(){
        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what) {
                case HANDLE_MESSAGE_RENDER:
//                    if(mIntervalTimer >= Long.MAX_VALUE){
//                        mIntervalTimer = mTimeDefault;
//                    }
//                    mIntervalTimer += mTimeInSecond;
                    Message loopMessage = mCountDownHandle.obtainMessage(HANDLE_MESSAGE_RENDER);
//					  Message loopMessage = new Message();
//					  loopMessage.what = HANDLE_MESSAGE_RENDER;
                    mCountDownHandle.sendMessageDelayed(loopMessage, UPDATE_MSG_INTERVAL);
                    if(!mMapCallbacksRedertime.isEmpty()){
                        mCountDownHandle.post(new Runnable() {
                            @Override
                            public void run() {
                                setCountDownTimeAllData();
                                for(Entry<String, RenderTimerCallback> entry : mMapCallbacksRedertime.entrySet()){
                                    if(entry.getValue() != null){
                                        Deal deal = mMapComponentCacheTime.get(entry.getKey());
                                        if (deal != null){
                                            entry.getValue().renderTimer(deal.getCountDownTime(), deal);
                                        }

                                    }
                                }
                            }
                        });
                    }

                    break;
                default:
                    break;
            }
        }

    };


    private static class Loader {
        private static CountDownTimerManager INSTANCE = new CountDownTimerManager();
    }

    public static CountDownTimerManager getInstance() {
        return CountDownTimerManager.Loader.INSTANCE;
    }


    private void setDefault(){
        if(mMapComponentCacheTime != null){
            mMapComponentCacheTime.clear();
        }

        if(mMapCallbacksRedertime != null){
            mMapCallbacksRedertime.clear();
        }

        if(mMapUuidDealExpired != null){
            mMapUuidDealExpired.clear();
        }
    }

    public Map<String, Boolean> getMapUuidDealExpired(){
        return mMapUuidDealExpired;
    }

    public void putDataCacheTime(final DealRecyclerViewAdapter dealRecyclerViewAdapter,String uuid, Deal deal){
        if(mMapComponentCacheTime != null){
            if(!mMapComponentCacheTime.containsKey(uuid)){
                deal.setCountDownTime(deal.getEndDate().getTime() - deal.getStartedDate().getTime());
                mMapComponentCacheTime.put(uuid, deal);
                AppConfig.runInApplicationThread(new Runnable() {
                    @Override
                    public void run() {
                        dealRecyclerViewAdapter.notifyData(getListDeal());
                    }
                });
            }

        }
    }

    public synchronized void setCountDownTimeAllData(){
        if(mMapComponentCacheTime != null){
            synchronized (mMapComponentCacheTime){
                for(Map.Entry<String, Deal> entry : mMapComponentCacheTime.entrySet()){
                    Deal deal = entry.getValue();
                    if(deal.getCountDownTime() > 0L){
                        deal.setCountDownTime(deal.getCountDownTime() - 1000L);
                        mMapUuidDealExpired.put(deal.getUuid(), false);
                    } else {
                        deal.setCountDownTime(0L);
                        mMapUuidDealExpired.put(deal.getUuid(), true);
                    }

                }
//                for(Map.Entry<String, Boolean> entry : mMapUuidDealExpired.entrySet()){
//                    if(mMapComponentCacheTime.containsKey(entry.getKey()) && entry.getValue()){
//                        mMapComponentCacheTime.remove(entry.getKey());
//                    }
//                }
            }

        }
    }

    public List<Deal> getListDeal(){
        List<Deal> listDeal = new ArrayList<>();
        if(mMapComponentCacheTime != null){
            for(Map.Entry<String, Deal> entry : mMapComponentCacheTime.entrySet()){
                Deal deal = entry.getValue();
                if(deal.getCountDownTime() > 0L){
                    listDeal.add(deal);
                }
            }
        }
        return listDeal;
    }

    public void start(){
        Message message = mCountDownHandle.obtainMessage(HANDLE_MESSAGE_RENDER);
//		Message message = new Message();
//		message.what = HANDLE_MESSAGE_RENDER;
        mCountDownHandle.sendMessageDelayed(message, UPDATE_MSG_INTERVAL);
    }

    public void stop(){
//		mCountDownHandle.removeMessages(HANDLE_MESSAGE_RENDER);
        mCountDownHandle.removeCallbacksAndMessages(null);
    }

    public void restart(){
        setDefault();
        stop();
        start();
    }


    public long getCacheTimerByComponentId(String uuid){
        if(mMapComponentCacheTime != null){
            Deal deal = mMapComponentCacheTime.get(uuid);
            return deal.getCountDownTime();
        }
        return 0l;

    }

    public void registerRenderTime(String uuid, RenderTimerCallback component){
        if(mMapCallbacksRedertime != null){
            mMapCallbacksRedertime.put(uuid, component);
        }
    }

    public void unRegisterRenderTime(Integer componentId){
        if(mMapCallbacksRedertime != null &&
                mMapCallbacksRedertime.containsKey(componentId)){
            mMapCallbacksRedertime.remove(componentId);
        }
    }

    public static String getSecond(long time){
        return String.format("%02d", (time % 60000l) / 1000l);
    }

    public static String getMinute(long time){
        return String.format("%02d", (time / 60000l));
    }

    public static String getHour(long time){
        return String.format("%02d", (time / (3600 * 1000L)));
    }

}

