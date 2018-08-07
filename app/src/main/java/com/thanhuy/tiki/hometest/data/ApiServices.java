package com.thanhuy.tiki.hometest.data;

import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.os.SystemClock;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.google.gson.Gson;
import com.thanhuy.tiki.hometest.application.AppConfig;
import com.thanhuy.tiki.hometest.data.response.Deal;
import com.thanhuy.tiki.hometest.util.GsonUtil;
import com.thanhuy.tiki.hometest.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ApiServices {
    private static final String TAG = ApiServices.class.getName();
    private static int INCREASING_SECONDS = 15;
    private static int DURATION = 60; // 1 minute
//    private static int DURATION = 10; // 1 minute
    private static String ASSERT_FILE_PATTERN = "file:///android_asset";

    private ApiServices(){

    }

    private Gson gson = GsonUtil.getInstance();

    private static class Loader {
        private static ApiServices INSTANCE = new ApiServices();
    }

    public static ApiServices getInstance() {
        return Loader.INSTANCE;
    }

    @WorkerThread
    public List<Deal> getDeals(){
        // Verify this is called on Worker Thread
        assertWorkerThread();

        // mock a delay
        SystemClock.sleep(500);
        String jsonListDeal = Util.readFileFromAsset(
                "data/deals.json", AppConfig.getContext());
        List<Deal> listDeal = new ArrayList<>();
        try{
            JSONArray jsonArrayDeal = new JSONArray(jsonListDeal);
            for(int index = 0; index < jsonArrayDeal.length(); index++) {

                JSONObject jsonObjectDeal = jsonArrayDeal.getJSONObject(index);
                String name = jsonObjectDeal.getString("name");
                Double price = jsonObjectDeal.getDouble("price");
                String thumbnail = String.format("%s/data/images/%s",
                        ASSERT_FILE_PATTERN, jsonObjectDeal.getString("thumbnail"));
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, index * INCREASING_SECONDS);
                Date startedDate = calendar.getTime();
                calendar.add(Calendar.SECOND, DURATION);
                Date endDate = calendar.getTime();
                Deal deal = new Deal(
                        name,
                        thumbnail,
                        price,
                        startedDate,
                        endDate
                );
                listDeal.add(deal);
            }
        }catch (JSONException e){
            Log.e(TAG, e.toString());
        }

        return listDeal;
    }

    private void assertWorkerThread(){
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new NetworkOnMainThreadException();
        }
    }
}
