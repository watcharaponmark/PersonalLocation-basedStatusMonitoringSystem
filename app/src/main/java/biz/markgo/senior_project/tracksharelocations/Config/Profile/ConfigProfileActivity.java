package biz.markgo.senior_project.tracksharelocations.Config.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import biz.markgo.senior_project.tracksharelocations.API.URL_API;
import biz.markgo.senior_project.tracksharelocations.Begin.StartActivity;
import biz.markgo.senior_project.tracksharelocations.Config.ConfigFragment;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.DatabaseSqlLite.Member;
import biz.markgo.senior_project.tracksharelocations.Login.DetailRegisterActivity;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.ShareHistoryFragment;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking.TrackingFragment;

public class ConfigProfileActivity extends AppCompatActivity {

    Context ctx=this;
    private static final String TAG = ConfigProfileActivity.class.getSimpleName();
    private ImageView img_change;
    private Button bt_img_change,bt_QR,bt_svae_profile,bt_send;
    private EditText et_name_profile,et_sername_profile,et_email_profile,et_id_profile;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String UPLOAD_URL ="http://api-location-monitoring.markgo.biz/member/upload.php";
    private String KEY_IMAGE = "image";
    private String KEY_type_account = "type_account";
    private String KEY_account_id = "account_id";
    private String Email,Name,Sername,follow_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_config_profile);
        toolbar.setTitle("ตั้งค่า โปรไฟล์");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
//#####################################################################################
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//#####################################################################################
        img_change = (ImageView) findViewById(R.id.img_change);

        et_name_profile = (EditText) findViewById(R.id.et_name_profile);
        et_sername_profile = (EditText) findViewById(R.id.et_sername_profile);
        et_email_profile = (EditText) findViewById(R.id.et_email_profile);
        et_id_profile = (EditText) findViewById(R.id.et_id_profile);

        bt_img_change =(Button) findViewById(R.id.bt_img_change);
        bt_QR = (Button) findViewById(R.id.bt_QR);
        bt_svae_profile= (Button) findViewById(R.id.bt_svae_profile);


        bt_send = (Button) findViewById(R.id.bt_send);
//#####################################################################################


        if (MemberInformation.getType_account().equals("google")) {

            Glide.with(this)
                    .load(MemberInformation.getPicture_name().toString())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_change);

            et_name_profile.setText(MemberInformation.getFirstName());
            et_sername_profile.setText(MemberInformation.getLastName());
            et_email_profile.setText(MemberInformation.getEmail());
            et_id_profile.setText(MemberInformation.getFollow_id());

        } else if (MemberInformation.getType_account().equals("facebook")) {
            Glide.with(this)
                    .load(MemberInformation.getPicture_name().toString())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_change);

            et_name_profile.setText(MemberInformation.getFirstName());
            et_sername_profile.setText(MemberInformation.getLastName());
            et_email_profile.setText(MemberInformation.getEmail());
            et_id_profile.setText(MemberInformation.getFollow_id());

        } else if (MemberInformation.getType_account().equals("Nologin")) {
            img_change.setImageResource(R.mipmap.ic_launcher);

            Glide.with(this)
                    .load(MemberInformation.getPicture_name().toString())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_change);

//            et_name_profile.setText(MemberInformation.getFirstName());
//            et_sername_profile.setText(MemberInformation.getLastName());
//            et_email_profile.setText(MemberInformation.getEmail());
//            et_id_profile.setText(MemberInformation.getFollow_id());
        }
//#####################################################################################
        bt_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfigProfileActivity.this,QRCodeActivity.class);
                startActivity(intent);
            }
        });
//#####################################################################################
        bt_img_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
//#####################################################################################
        bt_svae_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = et_email_profile.getText().toString();
                Name =  et_name_profile.getText().toString();
                Sername = et_sername_profile.getText().toString();
                follow_id = et_id_profile.getText().toString();

               // onBackPressed();

                if(bitmap != null) {
                    uploadImage();
                }else {
                    BackGround_ProfileUpdate backGround_profileUpdate = new BackGround_ProfileUpdate();
                    backGround_profileUpdate.execute(MemberInformation.getMember_id(),
                            Email,
                            Name,
                            Sername,
                            follow_id);
                }
            }
        });
//#####################################################################################
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = "สามารถใช้ไอดีผู้ใช้นี้ >> "+MemberInformation.getFollow_id()+" <<\nเพื่อใช้ในการติดตามสถานะของฉัน\nดาวโหลดแอปได้ที่นี่ : https://play.google.com/store/apps/details?id=biz.markgo.senior_project.tracksharelocations";
                Intent sharingIntent = new Intent();
                sharingIntent.setAction(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "รหัสติดตามสถานะ");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(sharingIntent, "แชร์"));
            }
        });
//#####################################################################################
    }
//#####################################################################################

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                img_change.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    //#####################################################################################
    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(ConfigProfileActivity.this, s , Toast.LENGTH_LONG).show();

                        Glide.with(getApplicationContext())
                                .load(MemberInformation.getPicture_name().toString())
                                .thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(img_change);

                        BackGround_ProfileUpdate backGround_profileUpdate = new BackGround_ProfileUpdate();
                        backGround_profileUpdate.execute(MemberInformation.getMember_id(),
                                Email,
                                Name,
                                Sername,
                                follow_id);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(ConfigProfileActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);
                //Getting Image Name
                String name = et_email_profile.getText().toString().trim();
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();
                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_account_id, MemberInformation.getAccount_id());
                params.put(KEY_type_account, MemberInformation.getType_account());

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
     }


    class BackGround_ProfileUpdate extends AsyncTask<String, String,String> {

        @Override
        protected String doInBackground(String... params) {
            String member_id = params[0];
            String email = params[1];
            String FirstName = params[2];
            String LastName = params[3];
            String follow_id = params[4];
            String data="";
            String Api_Key = "ProfileUpdate1234";
            String Token_key = MemberInformation.getToken_key();
            int tmp;
            Log.e(TAG,member_id+" "+email);

            try {
                URL url = new URL( URL_API.getAPI_update_profile() );
                String urlParams = "API_Key="+Api_Key
                        +"&Token_key=" + Token_key
                        +"&Member_id="+member_id
                        +"&email="+email
                        +"&FirstName="+FirstName
                        +"&LastName="+LastName
                        +"&follow_id="+follow_id;

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
                JSONObject response = root.getJSONObject("UpdateProfile_response");
                status = response.getString("status");
                if(status.equals("0")) {

                    MemberInformation.setFirstName(Name);
                    MemberInformation.setLastName(Sername);
                    MemberInformation.setEmail(Email);
                    MemberInformation.setFollow_id(follow_id);

//                    ConfigFragment configFragment = new ConfigFragment();
//                    FragmentManager manager = getSupportFragmentManager();
//                    manager.beginTransaction().replace(
//                            R.id.place_status_for_fragment,
//                            configFragment,
//                            configFragment.getTag()
//                    ).commit();

                    s="แก้ไขข้อมูลสำเร็จ";
                    Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();

                }else if(status.equals("7")){

                    AlertDialog.Builder dialog = new AlertDialog.Builder(ConfigProfileActivity.this);
                    dialog.setTitle("ไอดีนี้มีผู้ใช้งานแล้ว!"); //ไปที่เเวรูเเล้วก็สตริง
                    dialog.setIcon(R.drawable.logo);
                    dialog.setMessage("กรุณากรอกไอดีใหมอีกครั้ง");
                    dialog.setCancelable(true);

                    dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            et_name_profile.setText(MemberInformation.getFirstName());
                            et_sername_profile.setText(MemberInformation.getLastName());
                            et_email_profile.setText(MemberInformation.getEmail());
                            et_id_profile.setText(MemberInformation.getFollow_id());

                        }
                    });

                    dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    dialog.show();


                }else{
                  //  Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }
        }


    }
}
