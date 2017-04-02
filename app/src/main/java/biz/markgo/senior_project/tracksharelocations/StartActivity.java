package biz.markgo.senior_project.tracksharelocations;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StartActivity extends AppCompatActivity {
    private  static  int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(StartActivity.this,ChooseLoginActivity.class);
                StartActivity.this.startActivity(intent);
                StartActivity.this.finish();
            }
        },SPLASH_TIME_OUT);
    }
}
