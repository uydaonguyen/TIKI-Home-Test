package com.thanhuy.tiki.hometest.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.thanhuy.tiki.hometest.application.AppConfig;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * author:uy.daonguyen@gmail.com
 */
public class Util {
    private static final String TAG = Util.class.getName();

    public static String readFileFromAsset(@NonNull String fileName, @NonNull Context context){
        StringBuilder returnString = new StringBuilder();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = context.getResources().getAssets()
                    .open(fileName, Context.MODE_PRIVATE);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            closeQuietly(inputStream);
            closeQuietly(inputStreamReader);
            closeQuietly(bufferedReader);
        }
        return returnString.toString();
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    public static Drawable getImageFromAssetsFile(String fileName) {
        Drawable drawable = null;
        Bitmap image = null;
        AssetManager assetManager = AppConfig.getContext().getResources().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(fileName);
            drawable = Drawable.createFromStream(inputStream, null);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            closeQuietly(inputStream);
        }
        return drawable;
    }
}
