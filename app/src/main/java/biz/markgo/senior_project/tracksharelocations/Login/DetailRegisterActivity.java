package biz.markgo.senior_project.tracksharelocations.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
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
import biz.markgo.senior_project.tracksharelocations.Begin.SelectModeActivity;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.PlaceStatusActivity;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.Data.User_Data;

public class DetailRegisterActivity extends AppCompatActivity {
    Context ctx=this;
    private String statusLogin,FirstName,LastName,personPhotoUrl,email,account_id;
    private EditText et_FirstName,et_LastName,et_followID,et_Email;
    private Button bt_next_detail,selectImage;
    public ImageView im_profile_detail;
    private int PICK_IMAGE_REQUEST = 1;
    private String UPLOAD_URL ="http://api-location-monitoring.markgo.biz/member/upload.php";
    private String KEY_IMAGE = "image";
    private String KEY_type_account = "type_account";
    private String KEY_account_id = "account_id";
    private Bitmap bitmap;
    private int count_bitmap=0;
    private JSONArray Memebr_Information;
    private static final String TAG = DetailRegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_register);


        im_profile_detail = (ImageView) findViewById(R.id.im_profile_detail);
        et_Email =(EditText) findViewById(R.id.et_Email);
        et_FirstName =(EditText) findViewById(R.id.et_FirstName);
        et_LastName =(EditText) findViewById(R.id.et_LastName);
        et_followID =(EditText) findViewById(R.id.et_followID);
        bt_next_detail =(Button) findViewById(R.id.bt_next_detail);
        selectImage = (Button) findViewById(R.id.selectImage);
//#####################################################################################
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
//#####################################################################################
        statusLogin = User_Data.getStatusLogin();
        FirstName = User_Data.getFirstName();
        LastName = User_Data.getLastName();
        personPhotoUrl = User_Data.getPersonPhotoUrl();
        email = User_Data.getEmail();
        account_id = User_Data.getAccount_id();
//#####################################################################################

        et_Email.setText(email);
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
                        et_Email.getText().toString(),
                        et_FirstName.getText().toString(),
                        et_LastName.getText().toString(),
                        personPhotoUrl,
                        statusLogin,
                        et_followID.getText().toString());

               }
     });
//#####################################################################################

    }

    private void showFileChooser() {
        count_bitmap = 1;
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
                im_profile_detail.setImageBitmap(bitmap);
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
                                Toast.makeText(DetailRegisterActivity.this, s , Toast.LENGTH_LONG).show();
                                BackGround_getMember backGround_getMember = new BackGround_getMember();
                                backGround_getMember.execute(account_id, statusLogin);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                //Dismissing the progress dialog
                                loading.dismiss();

                                //Showing toast
                                Toast.makeText(DetailRegisterActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //Converting Bitmap to String
                        String image = getStringImage(bitmap);
                        //Getting Image Name
                        String name = et_Email.getText().toString().trim();
                        //Creating parameters
                        Map<String,String> params = new Hashtable<String, String>();
                        //Adding parameters
                        params.put(KEY_IMAGE, image);
                        params.put(KEY_account_id, account_id);
                        params.put(KEY_type_account, statusLogin);

                        //returning parameters
                        return params;
                    }
                };

                //Creating a Request Queue
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                //Adding request to the queue
                requestQueue.add(stringRequest);
            }




    class BackGround extends AsyncTask<String, String,String> {
//            private Bitmap picture_name;
//            public BackGround(Bitmap picture_name){
//            this.picture_name = picture_name;
//         }

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
                URL url = new URL(URL_API.getAPI_register());

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
                JSONObject response = root.getJSONObject("register_response");
                status = response.getString("status");
                if(status.equals("0")) {
                    if(bitmap != null) {
                        uploadImage();

                    }else {
                        BackGround_getMember backGround_getMember = new BackGround_getMember();
                        backGround_getMember.execute(account_id, statusLogin);
                    }

//                    Intent intentHomeActivity=new Intent(DetailRegisterActivity.this,HomeActivity.class);
//                    intentHomeActivity.putExtra("statusLogin",statusLogin);
//                    intentHomeActivity.putExtra("account_id",account_id);
//                    startActivity(intentHomeActivity);
//                    s="บันทึกข้อมูลสำเร็จ";
//                    Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
                }else if(status.equals("5")){

                    AlertDialog.Builder dialog = new AlertDialog.Builder(DetailRegisterActivity.this);
                    dialog.setTitle("ไอดีนี้มีผู้ใช้งานแล้ว!"); //ไปที่เเวรูเเล้วก็สตริง
                    dialog.setIcon(R.drawable.logo);
                    dialog.setMessage("กรุณากรอกไอดีใหมอีกครั้ง");
                    dialog.setCancelable(true);

                    dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            et_Email.setText(email);
                            et_FirstName.setText(FirstName);
                            et_LastName.setText(LastName);

                            et_followID.setText("");
                        }
                    });

//                    dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });

                    dialog.show();


                }else{
                    Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }
        }


    }
    class BackGround_getMember extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String account_id = params[0];
            String statusLogin = params[1];
            String data = "";
            String Api_Key = "getMember1234";
            int tmp;

            try {
                URL url = new URL(URL_API.getAPI_get_member());
                String urlParams = "API_Key=" + Api_Key
                        + "&account_id=" + account_id
                        + "&statusLogin=" + statusLogin;

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

                JSONObject root = new JSONObject(s);
                JSONObject getMember = root.getJSONObject("getMember_response");
                status = getMember.getString("status");



                if (status.equals("0")) {
                    Memebr_Information = getMember.getJSONArray("member_Information");
                    Log.d(TAG, "length : " + Memebr_Information.length());
                    //  for (int i = 0; i < Memebr_Information.length(); ++i) {

                    // MemberInformation memberInformation = new MemberInformation();
                    JSONObject jRedobject = Memebr_Information.getJSONObject(0);

                    MemberInformation.setMember_id(jRedobject.getString("member_id"));
                    MemberInformation.setAccount_id(jRedobject.getString("account_id"));
                    MemberInformation.setEmail(jRedobject.getString("email"));
                    MemberInformation.setFirstName(jRedobject.getString("FirstName"));
                    MemberInformation.setLastName(jRedobject.getString("LastName"));
                    MemberInformation.setPicture_name(jRedobject.getString("picture_name"));
                    MemberInformation.setType_account(jRedobject.getString("type_account"));
                    MemberInformation.setFollow_id(jRedobject.getString("follow_id"));
                    MemberInformation.setToken_key(jRedobject.getString("Token_key"));

                    s="บันทึกข้อมูลสำเร็จ";
                    Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
                    Intent intentHomeActivity=new Intent(DetailRegisterActivity.this,SelectModeActivity.class);
//                    intentHomeActivity.putExtra("statusLogin",statusLogin);
//                    intentHomeActivity.putExtra("account_id",account_id);
                    startActivity(intentHomeActivity);

                } else {

                    // Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }

        }


    }
}
