package com.example.receiptholder.springootandroid.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by roxanap on 19.02.2016.
 */
public class ReceiptService extends Service {
    private static final String TAG = ReceiptService.class.getName();

    private IBinder binder = new ServiceBinder();
    int startMode;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras!=null){
            Log.d(TAG, "onBind with extras");
        }
        return binder;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"service created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "service destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public class ServiceBinder extends Binder {
        public ReceiptService getService(){
            return ReceiptService.this;
        }
    }
}
