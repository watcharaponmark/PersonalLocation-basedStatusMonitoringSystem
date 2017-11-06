package biz.markgo.senior_project.tracksharelocations.FirebaseNoti;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import biz.markgo.senior_project.tracksharelocations.API.URL_API;
import biz.markgo.senior_project.tracksharelocations.Config.Profile.ConfigProfileActivity;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.R;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    Context ctx=this;
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param newToken The new token.
     */
    private void sendRegistrationToServer(String newToken) {

        // TODO: Implement this method to send token to your app server.
        Log.e(TAG, "newToken : " + newToken);
        BackGround_Token_FCM backGround_token_fcm= new BackGround_Token_FCM();
        backGround_token_fcm.execute(MemberInformation.getMember_id(),newToken);

    }

    class BackGround_Token_FCM extends AsyncTask<String, String,String> {

        @Override
        protected String doInBackground(String... params) {
            String member_id = params[0];
            String Token_FCM = params[1];

            String data="";
            String Api_Key = "Token_FCMUpdate1234";
            String Token_key = MemberInformation.getToken_key();
            int tmp;


            try {
                URL url = new URL( URL_API.getAPI_UpdateToken_FCM() );
                String urlParams = "API_Key="+Api_Key
                        +"&Token_key=" + Token_key
                        +"&Member_id="+member_id
                        +"&Token_FCM="+Token_FCM;

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
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;
            String status = "";

            try {
                JSONObject root = new JSONObject(s);
                JSONObject response = root.getJSONObject("UpdateToken_FCM_response");
                status = response.getString("status");
                if(status.equals("0")) {
//
//                    s="แก้ไขข้อมูลสำเร็จ";
//                    Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
                    Log.e(TAG,"แก้ไขข้อมูลสำเร็จ");

                }else if(status.equals("1")){

                    Log.e(TAG,"แก้ไขข้อมูไม่ลสำเร็จ");
//
//                    s="แก้ไขข้อมูไม่สำเร็จ";
//                    Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();

                }else{

                    Log.e(TAG,"ERROR : "+s);
                    //  Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }
        }


    }
}