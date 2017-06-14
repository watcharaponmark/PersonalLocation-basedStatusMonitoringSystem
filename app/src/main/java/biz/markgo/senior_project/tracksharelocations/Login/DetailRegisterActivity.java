package biz.markgo.senior_project.tracksharelocations.Login;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import biz.markgo.senior_project.tracksharelocations.HomeActivity;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.User_Data;

public class DetailRegisterActivity extends AppCompatActivity {
    Context ctx=this;
    private String statusLogin,FirstName,LastName,personPhotoUrl,email,account_id;
    private EditText et_FirstName,et_LastName,et_followID;
    private Button bt_next_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_register);


        ImageView im_profile_detail = (ImageView) findViewById(R.id.im_profile_detail);
        TextView tv_Email =(TextView) findViewById(R.id.tv_Email);
        et_FirstName =(EditText) findViewById(R.id.et_FirstName);
        et_LastName =(EditText) findViewById(R.id.et_LastName);
        et_followID =(EditText) findViewById(R.id.et_followID);
        bt_next_detail =(Button) findViewById(R.id.bt_next_detail);

        final Intent intent = getIntent();
        statusLogin = intent.getStringExtra("statusLogin");
        FirstName = intent.getStringExtra("FirstName");
        LastName = intent.getStringExtra("LastName");
        personPhotoUrl = intent.getStringExtra("personPhotoUrl");
        email = intent.getStringExtra("email");
        account_id = intent.getStringExtra("account_id");

        tv_Email.setText(email);
        et_FirstName.setText(FirstName);
        et_LastName.setText(LastName);
        Glide.with(getApplicationContext()).load(personPhotoUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(im_profile_detail);

        bt_next_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BackGround b = new BackGround();
                b.execute(account_id,
                        email,
                        et_FirstName.getText().toString(),
                        et_LastName.getText().toString(),
                        personPhotoUrl,
                        statusLogin,
                        et_followID.getText().toString());

            }
        });
    }
    class BackGround extends AsyncTask<String, String,String> {

        @Override
        protected String doInBackground(String... params) {
            String account_id = params[0];
            String email = params[1];
            String FirstName = params[2];
            String LastName = params[3];
            String picture_name = params[4];
            String type_account = params[5];
            String follow_id = params[6];
            String data="";
            String Api_Key = "register1234";
            int tmp;

            try {
                URL url = new URL("http://senior-project.markgo.biz/member/register.php");
                String urlParams = "API_Key="+Api_Key
                        +"&account_id="+account_id
                        +"&email="+email
                        +"&FirstName="+FirstName
                        +"&LastName="+LastName
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
            String err = null;
            String status = "";

            try {
                JSONObject root = new JSONObject(s);
                JSONObject response = root.getJSONObject("responsejson");
                status = response.getString("status");

                if(status.equals("TRUE")) {
                    Intent intentHomeActivity=new Intent(DetailRegisterActivity.this,HomeActivity.class);
                    intentHomeActivity.putExtra("statusLogin",statusLogin);
                    intentHomeActivity.putExtra("FirstName",et_FirstName.getText().toString());
                    intentHomeActivity.putExtra("LastName",et_LastName.getText().toString());
                    intentHomeActivity.putExtra("personPhotoUrl",personPhotoUrl);
                    intentHomeActivity.putExtra("email",email);
                    intentHomeActivity.putExtra("account_id",account_id);
                    startActivity(intentHomeActivity);
                    s="บันทึกข้อมูลสำเร็จ";
                    Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }
        }


    }
}
