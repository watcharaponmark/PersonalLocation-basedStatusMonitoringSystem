package biz.markgo.senior_project.tracksharelocations.ServiceBackground;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import biz.markgo.senior_project.tracksharelocations.API.URL_API;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.FirebaseNoti.MyFirebaseInstanceIDService;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.adpter.GetPlace;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.PlaceStatusActivity;
import biz.markgo.senior_project.tracksharelocations.R;


public class LocationMonitorService extends Service {



    private static final String TAG = LocationMonitorService.class.getSimpleName();


    GoogleApiClient mGoogleApiClient;

    private Context mContext=LocationMonitorService.this;
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0;
    private Double lat;
    private Double lng;


    private JSONArray Place_Information;
    ArrayList<GetPlace> placeList = new ArrayList<>();

    private class LocationListener implements  android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

            Log.e(TAG,"LocationListener: "+" Lat : "+mLastLocation.getLatitude()+" lng:"+mLastLocation.getLongitude());
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
            Log.e(TAG,"Lat : "+lat+" lng:"+lng);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

//    LocationListener[] mLocationListeners = new LocationListener[]{
//            new LocationListener(LocationManager.GPS_PROVIDER),
//            new LocationListener(LocationManager.NETWORK_PROVIDER)
//    };

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");

        initializeLocationManager();

        try {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //LOCATION_INTERVAL, LOCATION_DISTANCE,//NETWORK_PROVIDER [1]
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);

        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //getting systems default ringtone
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
        myFirebaseInstanceIDService.onTokenRefresh();

        getPlace getPlace = new getPlace();
        getPlace.execute(MemberInformation.getToken_key(), MemberInformation.getMember_id());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();

        placeList.clear();

        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listener, ignore", ex);
                }
            }
        }


    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }



    class getPlace extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            String Token_key = params[0];
            String Member_id = params[1];
            String Api_Key = "getplace1234";


            String data = "";
            int tmp;
            try {

                URL url = new URL(URL_API.getAPI_get_place());
                String urlParams ="API_Key="+Api_Key
                        +"&Token_key="+Token_key
                        +"&Member_id="+Member_id;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setInstanceFollowRedirects(false);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("charset", "utf-8");
                httpURLConnection.setRequestProperty("Content-Length", "" + Integer.toString(urlParams.getBytes().length));
                httpURLConnection.setUseCaches(false);

                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();
                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;
            String status = "";

            try {
                Log.e(TAG,s);
                JSONObject root = new JSONObject(s);
                JSONObject getPlace = root.getJSONObject("getPlace_response");
                status = getPlace.getString("status");

                if(status.equals("0")) {
                    Place_Information = getPlace.getJSONArray("Place_Information");
                   // Log.d(TAG,"llllllllllllllllllllllllllll"+Place_Information.length());

                    placeList.clear();
                    for (int i=0;i<Place_Information.length();++i){

                        GetPlace place =new GetPlace();
                        JSONObject jRedobject= Place_Information.getJSONObject(i);

                        place.setMember_id(jRedobject.getString("member_id"));
                        place.setPlace_id(jRedobject.getString("place_id"));
                        place.setPlace_name(jRedobject.getString("place_name"));
                        place.setSub_district(jRedobject.getString("sub_district"));
                        place.setDistrict(jRedobject.getString("district"));
                        place.setProvince(jRedobject.getString("province"));
                        place.setZip_code(jRedobject.getString("zip_code"));
                        place.setCountry_code(jRedobject.getString("country_code"));
                        place.setLatitude(jRedobject.getString("latitude"));
                        place.setLongitude(jRedobject.getString("longitude"));
                        placeList.add(place);

                    }
//                    s = "select Place";
//                    Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                }else if(status.equals("1")){

                    s = "คุณยังไม่มีข้อมูลการแบ่งปันสถานที่";
                    Log.e(TAG,s);
//                    Toast toast = Toast.makeText(getApplication(),s, Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
//                    toast.show();

                }


            } catch (JSONException e) {

                e.printStackTrace();
                err = "Exception: " + e.getMessage();

            }

        }


    }


}
