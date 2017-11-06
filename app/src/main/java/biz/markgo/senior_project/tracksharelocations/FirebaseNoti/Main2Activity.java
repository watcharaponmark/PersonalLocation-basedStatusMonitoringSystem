package biz.markgo.senior_project.tracksharelocations.FirebaseNoti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import biz.markgo.senior_project.tracksharelocations.R;

public class Main2Activity extends AppCompatActivity /*implements View.OnClickListener */{

    private String TAG = Main2Activity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        findViewById(R.id.btn_subscribe).setOnClickListener(this);
//        findViewById(R.id.btn_un_subscribe).setOnClickListener(this);

        Button btnGetToken = (Button) findViewById(R.id.bt_gettoken);
        btnGetToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastToken();

            }
        });

    }

    public void toastToken(){
        String token = FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(Main2Activity.this, "TOKEN = "+token, Toast.LENGTH_LONG).show();
        Log.d(TAG,""+token);
    }

//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == R.id.btn_subscribe) {
//            FirebaseMessaging.getInstance().subscribeToTopic("promotion");
//            toast("subscribed");
//        } else if (id == R.id.btn_un_subscribe) {
//            FirebaseMessaging.getInstance().unsubscribeFromTopic("promotion");
//            toast("un subscribed");
//        }
//    }

//    private void toast(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//    }
}
