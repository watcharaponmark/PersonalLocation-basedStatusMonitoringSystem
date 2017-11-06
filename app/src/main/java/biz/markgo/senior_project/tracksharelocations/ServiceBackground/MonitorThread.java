package biz.markgo.senior_project.tracksharelocations.ServiceBackground;

import android.util.Log;

import java.util.Calendar;

public class MonitorThread extends Thread {
    private static final String TAG = MonitorThread.class.getSimpleName();
    private static final int DELAY = 5000;
    public boolean finish = false;

    public void run(){

        while (true){
           Log.e(TAG,"วัน: "+getCurrentDay()+" เวลา: "+getCurrentTime());

            //หยุดพัก 5 วิ
            try {
                sleep(DELAY);
            } catch (InterruptedException e){
                e.printStackTrace();;
            }

            //ถ้าคลิกปุ่ม stop service ให้ออกจากลูป
             if(finish){
                 Log.e(TAG,"stop service");
                 return;
             }

        }
    }

    public String getCurrentDay(){

        //String daysArray[] = {"Sunday","Monday","Tuesday", "Wednesday","Thursday","Friday", "Saturday"};
        String daysArray[] = {"อาทิตย์", "จันทร์","อังคาร","พุธ","พฤหัสบดี","ศุกร์","เสาร์"};

        //Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        //Date currentTime = Calendar.getInstance().getTime()

        return daysArray[day];

    }

    public String getCurrentTime(){


        Calendar cal = Calendar.getInstance();
        int millisecond = cal.get(Calendar.MILLISECOND);
        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
//        //12 hour format
//        int hour = cal.get(Calendar.HOUR);

        //24 hour format
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);
        String TimeCurrent = hourofday+":"+minute+":"+second;

        Log.e("Time",TimeCurrent);

        return TimeCurrent;

    }


}
