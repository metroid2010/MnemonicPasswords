package com.metroid2010.mnemonicpasswordgenerator;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class Utils {

    private static final String TAG = BuildConfig.APPLICATION_ID;

    public static void showToastAndLog(Context mContext, String text) {
        Handler mHandler = new Handler(Looper.getMainLooper());
        Runnable makeToast = () -> {
            Toast.makeText(mContext.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            Log.d(TAG, text);
        };
        mHandler.post(makeToast);
    }



}
