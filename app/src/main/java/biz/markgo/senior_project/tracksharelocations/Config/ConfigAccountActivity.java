package biz.markgo.senior_project.tracksharelocations.Config;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

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
import biz.markgo.senior_project.tracksharelocations.Config.Profile.QRCodeActivity;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.Data.User_Data;
import biz.markgo.senior_project.tracksharelocations.Login.ChooseLoginActivity;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus.SettingAllPlaceFragment;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.ServiceBackground.LocationMonitorService;

public class ConfigAccountActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private String TAG=ConfigAccountActivity.class.getSimpleName();
    private TextView tv_id,tv_email;
    private CardView CV_QR,CV_Delete,CV_email,CV_id;
    private AlertDialog alertDialog;

    private GoogleApiClient mGoogleApiClient2;
    private GoogleSignInOptions gso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_config_account);
        toolbar.setTitle("ตั้งค่า บัญชี");
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
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_id = (TextView) findViewById(R.id.tv_id);

        CV_QR = (CardView) findViewById(R.id.CV_QR);
        CV_Delete = (CardView) findViewById(R.id.CV_Delete);
        CV_email = (CardView) findViewById(R.id.CV_email);
        CV_id = (CardView) findViewById(R.id.CV_id);

        tv_email.setText(MemberInformation.getEmail());
        tv_id.setText(MemberInformation.getFollow_id());

        CV_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfigAccountActivity.this, QRCodeActivity.class);
                startActivity(intent);
            }
        });

        CV_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(ConfigAccountActivity.this);
                dialog.setTitle("ลบบัญชีผู้ใช้ ?");
                dialog.setMessage("นี่จะเป็นการลบบัญชีทั้งหมดของคุณ และนำสถานที่ของคุณออกทั้งหมด");
                dialog.setIcon(R.drawable.alert);

                dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

                dialog.setPositiveButton("ดำเนินการต่อ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteAccount deleteSettingPlace = new DeleteAccount();
                        deleteSettingPlace.execute(MemberInformation.getToken_key(), MemberInformation.getMember_id());
                    }
                });
                alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        CV_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigAccountActivity.this,ConfigProfileActivity.class));
            }
        });

        CV_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigAccountActivity.this,ConfigProfileActivity.class));
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient2 = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,1,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient2.stopAutoManage(this);
        mGoogleApiClient2.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }



        class DeleteAccount extends AsyncTask<String,String,String> {

            @Override
            protected String doInBackground(String... params) {
                String Token_key = params[0];
                String Member_id = params[1];

                String Api_Key = "DeleteAccount1234";
                String data = "";
                int tmp;
                try {

                    URL url = new URL(URL_API.getAPI_delete_account());
                    String urlParams = "API_Key=" + Api_Key
                            + "&Token_key=" + Token_key
                            + "&Member_id=" + Member_id;


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
                    JSONObject getPlace = root.getJSONObject("DeleteAccount_response");
                    status = getPlace.getString("status");

                    if (status.equals("0")) {

                        s = "ลบบัญชีผู้ใช้เรียบร้อย";
                        Toast toast = Toast.makeText(getApplication(), s, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();

                        stopService(new Intent(ConfigAccountActivity.this, LocationMonitorService.class));

                        if (MemberInformation.getType_account().equals("google")) {
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient2).setResultCallback(
                                    new ResultCallback<com.google.android.gms.common.api.Status>() {
                                        @Override
                                        public void onResult(com.google.android.gms.common.api.Status status) {
                                            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient2).setResultCallback(
                                                    new ResultCallback<com.google.android.gms.common.api.Status>() {
                                                        @Override
                                                        public void onResult(com.google.android.gms.common.api.Status status) {
                                                            User_Data.setEmail(null);
                                                            User_Data.setStatusLogin(null);
                                                            User_Data.setFirstName(null);
                                                            User_Data.setLastName(null);
                                                            User_Data.setPersonPhotoUrl(null);
                                                            User_Data.setEmail(null);
                                                            User_Data.setAccount_id(null);

                                                            Intent intent_RevokeAccess = new Intent(ConfigAccountActivity.this, ChooseLoginActivity.class);
                                                            startActivity(intent_RevokeAccess);
                                                        }
                                                    });
                                        }
                                    });
                        } else if (MemberInformation.getType_account().equals("facebook")) {
                            //facbook logout
                            User_Data.setEmail(null);
                            User_Data.setStatusLogin(null);
                            User_Data.setFirstName(null);
                            User_Data.setLastName(null);
                            User_Data.setPersonPhotoUrl(null);
                            User_Data.setEmail(null);
                            User_Data.setAccount_id(null);
                            LoginManager.getInstance().logOut();

                            Intent intent_RevokeAccess = new Intent(ConfigAccountActivity.this, ChooseLoginActivity.class);
                            startActivity(intent_RevokeAccess);

                        } else if (MemberInformation.getType_account().equals("Nologin")) {
                            Intent intent_RevokeAccess = new Intent(ConfigAccountActivity.this, ChooseLoginActivity.class);
                            startActivity(intent_RevokeAccess);
                        }else{
                            Intent intent_RevokeAccess = new Intent(ConfigAccountActivity.this, ChooseLoginActivity.class);
                            startActivity(intent_RevokeAccess);
                        }


                    } else if (status.equals("1")) {

                        s = "ลบบัญชีผู้ใช้ไม่สำเร็จ";
//                    Toast toast = Toast.makeText(getContext(),s, Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
//                    toast.show();

                        Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                    err = "Exception: " + e.getMessage();

                }

            }
        }


}
