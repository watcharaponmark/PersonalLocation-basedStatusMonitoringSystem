package biz.markgo.senior_project.tracksharelocations.Begin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cuboid.cuboidcirclebutton.CuboidButton;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import biz.markgo.senior_project.tracksharelocations.Login.ChooseLoginActivity;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.PlaceStatusActivity;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.ServiceBackground.LocationMonitorService;
import biz.markgo.senior_project.tracksharelocations.ServiceBackground.MonitorService;
import biz.markgo.senior_project.tracksharelocations.Traking.TrakingActivity;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.Data.User_Data;


public class SelectModeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private CuboidButton bt_SettingStatus_farg_home, bt_traking_farg_home;
    private GoogleApiClient mGoogleApiClient2;
    private static final String TAG = SelectModeActivity.class.getSimpleName();
    Context context= SelectModeActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_select_mode);
        setSupportActionBar(toolbar);


        bt_SettingStatus_farg_home = (CuboidButton) findViewById(R.id.bt_SettingStatus_farg_home);
        bt_traking_farg_home = (CuboidButton) findViewById(R.id.bt_traking_farg_home);



if(!MemberInformation.getType_account().equals("Nologin")) {
//#####################################################################################
    startService(new Intent(this, LocationMonitorService.class));
    // stopService(new Intent(this, MyService.class));
    startService(new Intent(this, MonitorService.class));
//#####################################################################################
}
        bt_SettingStatus_farg_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MemberInformation.getType_account().equals("Nologin")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("ไม่สามารถเข้าใช้งานในโหมดนี้ได้"); //ไปที่เเวรูเเล้วก็สตริง
                    dialog.setIcon(R.drawable.logo);
                    dialog.setMessage("กรุณาเข้าสู่ระบบเพื่อใช้งาน");
                    dialog.setCancelable(true);

                    dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SelectModeActivity.this, ChooseLoginActivity.class);
                            startActivity(intent);
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
                    Intent intent = new Intent(SelectModeActivity.this, PlaceStatusActivity.class);
                    startActivity(intent);
                }
            }
        });
//#####################################################################################
        bt_traking_farg_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectModeActivity.this, TrakingActivity.class);
                startActivity(intent);
            }
        });

//#####################################################################################
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient2 = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
//#####################################################################################

    }

    // เมื่อกดปุ่ม back ให้ออกจากโปรแกรม
    @Override
    public void onBackPressed() {
        // เมื่อกดปุ่ม back ให้ออกจากโปรแกรม

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("ออกจากระบบ"); //ไปที่เเวรูเเล้วก็สตริง
            dialog.setIcon(R.drawable.logo);
            dialog.setMessage("คุณต้องการออกจากระบบ ?");
            dialog.setCancelable(true);

            dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_HOME);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);

                    stopService(new Intent(SelectModeActivity.this, LocationMonitorService.class));
                    //#####################################################################################
                    if (MemberInformation.getType_account().equals("google")) {
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient2).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient2).setResultCallback(
                                                new ResultCallback<Status>() {
                                                    @Override
                                                    public void onResult(Status status) {
                                                        User_Data.setEmail(null);
                                                        User_Data.setStatusLogin(null);
                                                        User_Data.setFirstName(null);
                                                        User_Data.setLastName(null);
                                                        User_Data.setPersonPhotoUrl(null);
                                                        User_Data.setEmail(null);
                                                        User_Data.setAccount_id(null);

                                                        Intent intent_RevokeAccess = new Intent(SelectModeActivity.this, ChooseLoginActivity.class);
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

//                Member_Controler member_controler = new Member_Controler(this);
//                member_controler.delete(MemberInformation.getMember_id());

                        Intent intent_RevokeAccess = new Intent(SelectModeActivity.this, ChooseLoginActivity.class);
                        startActivity(intent_RevokeAccess);

                    } else if (MemberInformation.getType_account().equals("Nologin")) {
                        Intent intent_RevokeAccess = new Intent(SelectModeActivity.this, ChooseLoginActivity.class);
                        startActivity(intent_RevokeAccess);
                    }else{
                        Intent intent_RevokeAccess = new Intent(SelectModeActivity.this, ChooseLoginActivity.class);
                        startActivity(intent_RevokeAccess);
                    }
                    //#####################################################################################
                }
            });
            dialog.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_mode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_select_mode) {
            stopService(new Intent(this, LocationMonitorService.class));
            stopService(new Intent(this, MonitorService.class));
            //#####################################################################################
            if (MemberInformation.getType_account().equals("google")) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient2).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient2).setResultCallback(
                                        new ResultCallback<Status>() {
                                            @Override
                                            public void onResult(Status status) {
                                                User_Data.setEmail(null);
                                                User_Data.setStatusLogin(null);
                                                User_Data.setFirstName(null);
                                                User_Data.setLastName(null);
                                                User_Data.setPersonPhotoUrl(null);
                                                User_Data.setEmail(null);
                                                User_Data.setAccount_id(null);

                                                Intent intent_RevokeAccess = new Intent(SelectModeActivity.this, ChooseLoginActivity.class);
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

//                Member_Controler member_controler = new Member_Controler(this);
//                member_controler.delete(MemberInformation.getMember_id());

                Intent intent_RevokeAccess = new Intent(SelectModeActivity.this, ChooseLoginActivity.class);
                startActivity(intent_RevokeAccess);

            } else if (MemberInformation.getType_account().equals("Nologin")) {
                Intent intent_RevokeAccess = new Intent(SelectModeActivity.this, ChooseLoginActivity.class);
                startActivity(intent_RevokeAccess);
            }else{
                Intent intent_RevokeAccess = new Intent(SelectModeActivity.this, ChooseLoginActivity.class);
                startActivity(intent_RevokeAccess);
            }
            //#####################################################################################
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }




}
