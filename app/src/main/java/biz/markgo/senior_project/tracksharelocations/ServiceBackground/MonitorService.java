package biz.markgo.senior_project.tracksharelocations.ServiceBackground;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class MonitorService extends Service {

    private static final String TAG = MonitorService.class.getSimpleName();
    private MonitorThread monitorThread;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");

        monitorThread =new MonitorThread();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //super.onStartCommand(intent, flags, startId);
        Log.e(TAG, "onStartCommand");

        if(!monitorThread.isAlive()){
            monitorThread.start();
        }

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");

        monitorThread.finish = true;


    }



}
