package biz.markgo.senior_project.tracksharelocations;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailRegisterActivity extends AppCompatActivity {
    Context ctx=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_register);


        ImageView im_profile_detail = (ImageView) findViewById(R.id.im_profile_detail);
        TextView tv_Email =(TextView) findViewById(R.id.tv_Email);
        EditText et_name =(EditText) findViewById(R.id.et_name);
        final EditText et_followID =(EditText) findViewById(R.id.et_followID);
        Button bt_next_detail =(Button) findViewById(R.id.bt_next_detail);

        final Intent intent = getIntent();
        final String statusLogin = intent.getStringExtra("statusLogin");
        final String personName = intent.getStringExtra("personName");
        final String personPhotoUrl = intent.getStringExtra("personPhotoUrl");
        final String email = intent.getStringExtra("email");
        final String user_id = intent.getStringExtra("user_id");

        tv_Email.setText(email);
        et_name.setText(personName);
        Glide.with(getApplicationContext()).load(personPhotoUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(im_profile_detail);

        bt_next_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BackGround b = new BackGround();
                b.execute(user_id, email,personName,personPhotoUrl,statusLogin,et_followID.getText().toString());

            }
        });
    }
    class BackGround extends AsyncTask<String, String,String> {

        @Override
        protected String doInBackground(String... params) {
            String user_id = params[0];
            String email = params[1];
            String name = params[2];
            String picture_name = params[3];
            String type_account = params[4];
            String follow_id = params[5];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://senior-project.markgo.biz/member/register.php");
                String urlParams = "user_id="+user_id
                        +"&email="+email
                        +"&name="+name
                        +"&picture_name="+picture_name
                        +"&type_account="+type_account
                        +"&follow_id="+follow_id;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
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
            if(s.equals("")){
                s="บันทึกข้อมูลสำเร็จ";
                Intent intentHomeActivity=new Intent(DetailRegisterActivity.this,HomeActivity.class);
                Intent intent = getIntent();
                String statusLogin = intent.getStringExtra("statusLogin");
                String personName = intent.getStringExtra("personName");
                String personPhotoUrl = intent.getStringExtra("personPhotoUrl");
                String email = intent.getStringExtra("email");
                String user_id = intent.getStringExtra("user_id");
                intentHomeActivity.putExtra("statusLogin","google");
                intentHomeActivity.putExtra("personName",personName);
                intentHomeActivity.putExtra("personPhotoUrl",personPhotoUrl);
                intentHomeActivity.putExtra("email",email);
                intentHomeActivity.putExtra("user_id",user_id);
                startActivity(intentHomeActivity);
            }
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
        }


    }
}
