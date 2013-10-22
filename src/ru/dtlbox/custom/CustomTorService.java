package ru.dtlbox.custom;

import org.torproject.android.service.TorService;

import android.content.Intent;
import android.util.Log;

public class CustomTorService extends TorService {
    
    private final static String LOG_TAG = "CustomTorService";
    
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(LOG_TAG, LOG_TAG + " start");
    }
    
    @Override
    public void logMessage(String msg)
    {
        Log.i(LOG_TAG,msg);
        super.logMessage(msg);
    }
    
    @Override
    public void logException(String msg, Exception e)
    {
        Log.e(LOG_TAG,msg,e);
        super.logException(msg, e);
    }
    
}
