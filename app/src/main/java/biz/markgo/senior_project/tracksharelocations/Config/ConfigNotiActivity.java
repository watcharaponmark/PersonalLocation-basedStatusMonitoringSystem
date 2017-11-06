package biz.markgo.senior_project.tracksharelocations.Config;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import biz.markgo.senior_project.tracksharelocations.R;

public class ConfigNotiActivity extends AppCompatActivity {
    private Switch sw_noti;
    private CardView CV_ringtone,CV_suond,CV_shake,CV_LED;
    private TextView tv_ringtone,tv_select_sound,tv_suond,tv_shake,tv_LED;
    private CheckBox cb_suond,cb_shake,cb_LED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_noti);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_config_noti);
        toolbar.setTitle("ตั้งค่า การแจ้งเตือน");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sw_noti = (Switch) findViewById(R.id.sw_noti);
        CV_ringtone = (CardView) findViewById(R.id.CV_ringtone);
        CV_suond  = (CardView) findViewById(R.id.CV_suond);
        CV_shake = (CardView) findViewById(R.id.CV_shake);
        CV_LED = (CardView) findViewById(R.id.CV_LED);
        tv_ringtone = (TextView) findViewById(R.id.tv_ringtone);
        tv_select_sound = (TextView) findViewById(R.id.tv_select_sound);
        tv_suond = (TextView) findViewById(R.id.tv_suond);
        tv_shake = (TextView) findViewById(R.id.tv_shake);
        tv_LED = (TextView) findViewById(R.id.tv_LED);
        cb_suond = (CheckBox) findViewById(R.id.cb_suond);
        cb_shake = (CheckBox) findViewById(R.id.cb_shake);
        cb_LED = (CheckBox) findViewById(R.id.cb_LED);

        sw_noti.setChecked(false);
        //########################################################################################
            CV_ringtone.setCardBackgroundColor(Color.TRANSPARENT);
            tv_ringtone.setFocusable(false);
            tv_ringtone.setEnabled(false);
            tv_ringtone.setCursorVisible(false);
            tv_ringtone.setTextColor(Color.GRAY);
            tv_ringtone.setBackgroundColor(Color.TRANSPARENT);
            tv_select_sound.setFocusable(false);
            tv_select_sound.setEnabled(false);
            tv_select_sound.setCursorVisible(false);
            tv_select_sound.setBackgroundColor(Color.TRANSPARENT);
            //########################################################################################
            CV_suond.setCardBackgroundColor(Color.TRANSPARENT);
            tv_suond.setFocusable(false);
            tv_suond.setEnabled(false);
            tv_suond.setCursorVisible(false);
            tv_suond.setTextColor(Color.GRAY);
            tv_suond.setBackgroundColor(Color.TRANSPARENT);
            cb_suond.setFocusable(false);
            cb_suond.setEnabled(false);
            cb_suond.setCursorVisible(false);
            cb_suond.setBackgroundColor(Color.TRANSPARENT);
            //########################################################################################
            CV_shake.setCardBackgroundColor(Color.TRANSPARENT);
            tv_shake.setFocusable(false);
            tv_shake.setEnabled(false);
            tv_shake.setCursorVisible(false);
            tv_shake.setTextColor(Color.GRAY);
            tv_shake.setBackgroundColor(Color.TRANSPARENT);
            cb_shake.setFocusable(false);
            cb_shake.setEnabled(false);
            cb_shake.setCursorVisible(false);
            cb_shake.setBackgroundColor(Color.TRANSPARENT);
            //########################################################################################
            CV_LED.setCardBackgroundColor(Color.TRANSPARENT);
            tv_LED.setFocusable(false);
            tv_LED.setEnabled(false);
            tv_LED.setCursorVisible(false);
            tv_LED.setTextColor(Color.GRAY);
            tv_LED.setBackgroundColor(Color.TRANSPARENT);
            cb_LED.setFocusable(false);
            cb_LED.setEnabled(false);
            cb_LED.setCursorVisible(false);
            cb_LED.setBackgroundColor(Color.TRANSPARENT);
            //########################################################################################


        if (sw_noti != null) {
            sw_noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Toast.makeText(ConfigNotiActivity.this, "The Switch is " + (isChecked ? "on" : "off"),
                            Toast.LENGTH_SHORT).show();

                    if (isChecked) {
                        //########################################################################################
                        CV_ringtone.setCardBackgroundColor(Color.WHITE);
                        tv_ringtone.setFocusable(true);
                        tv_ringtone.setEnabled(true);
                        tv_ringtone.setCursorVisible(true);
                        tv_ringtone.setTextColor(Color.BLACK);
                        tv_ringtone.setBackgroundColor(Color.WHITE);
                        tv_select_sound.setFocusable(true);
                        tv_select_sound.setEnabled(true);
                        tv_select_sound.setCursorVisible(true);
                        tv_select_sound.setBackgroundColor(Color.WHITE);
                        //########################################################################################
                        CV_suond.setCardBackgroundColor(Color.WHITE);
                        tv_suond.setFocusable(true);
                        tv_suond.setEnabled(true);
                        tv_suond.setCursorVisible(true);
                        tv_suond.setTextColor(Color.BLACK);
                        tv_suond.setBackgroundColor(Color.WHITE);
                        cb_suond.setFocusable(true);
                        cb_suond.setEnabled(true);
                        cb_suond.setCursorVisible(true);
                        cb_suond.setBackgroundColor(Color.WHITE);
                        //########################################################################################
                        CV_shake.setCardBackgroundColor(Color.WHITE);
                        tv_shake.setFocusable(true);
                        tv_shake.setEnabled(true);
                        tv_shake.setCursorVisible(true);
                        tv_shake.setTextColor(Color.BLACK);
                        tv_shake.setBackgroundColor(Color.WHITE);
                        cb_shake.setFocusable(true);
                        cb_shake.setEnabled(true);
                        cb_shake.setCursorVisible(true);
                        cb_shake.setBackgroundColor(Color.WHITE);
                        //########################################################################################
                        CV_LED.setCardBackgroundColor(Color.WHITE);
                        tv_LED.setFocusable(true);
                        tv_LED.setEnabled(true);
                        tv_LED.setCursorVisible(true);
                        tv_LED.setTextColor(Color.BLACK);
                        tv_LED.setBackgroundColor(Color.WHITE);
                        cb_LED.setFocusable(true);
                        cb_LED.setEnabled(true);
                        cb_LED.setCursorVisible(true);
                        cb_LED.setBackgroundColor(Color.WHITE);
                        //########################################################################################

                    } else {
                        //########################################################################################
                        CV_ringtone.setCardBackgroundColor(Color.TRANSPARENT);
                        tv_ringtone.setFocusable(false);
                        tv_ringtone.setEnabled(false);
                        tv_ringtone.setCursorVisible(false);
                        tv_ringtone.setTextColor(Color.GRAY);
                        tv_ringtone.setBackgroundColor(Color.TRANSPARENT);
                        tv_select_sound.setFocusable(false);
                        tv_select_sound.setEnabled(false);
                        tv_select_sound.setCursorVisible(false);
                        tv_select_sound.setBackgroundColor(Color.TRANSPARENT);
                        //########################################################################################
                        CV_suond.setCardBackgroundColor(Color.TRANSPARENT);
                        tv_suond.setFocusable(false);
                        tv_suond.setEnabled(false);
                        tv_suond.setCursorVisible(false);
                        tv_suond.setTextColor(Color.GRAY);
                        tv_suond.setBackgroundColor(Color.TRANSPARENT);
                        cb_suond.setFocusable(false);
                        cb_suond.setEnabled(false);
                        cb_suond.setCursorVisible(false);
                        cb_suond.setBackgroundColor(Color.TRANSPARENT);
                        //########################################################################################
                        CV_shake.setCardBackgroundColor(Color.TRANSPARENT);
                        tv_shake.setFocusable(false);
                        tv_shake.setEnabled(false);
                        tv_shake.setCursorVisible(false);
                        tv_shake.setTextColor(Color.GRAY);
                        tv_shake.setBackgroundColor(Color.TRANSPARENT);
                        cb_shake.setFocusable(false);
                        cb_shake.setEnabled(false);
                        cb_shake.setCursorVisible(false);
                        cb_shake.setBackgroundColor(Color.TRANSPARENT);
                        //########################################################################################
                        CV_LED.setCardBackgroundColor(Color.TRANSPARENT);
                        tv_LED.setFocusable(false);
                        tv_LED.setEnabled(false);
                        tv_LED.setCursorVisible(false);
                        tv_LED.setTextColor(Color.GRAY);
                        tv_LED.setBackgroundColor(Color.TRANSPARENT);
                        cb_LED.setFocusable(false);
                        cb_LED.setEnabled(false);
                        cb_LED.setCursorVisible(false);
                        cb_LED.setBackgroundColor(Color.TRANSPARENT);
                        //########################################################################################
                    }
                }
            });
        }




    }
}
