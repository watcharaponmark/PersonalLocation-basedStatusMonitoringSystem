package biz.markgo.senior_project.tracksharelocations;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import biz.markgo.senior_project.tracksharelocations.Login.ChooseLoginActivity;
import biz.markgo.senior_project.tracksharelocations.Nav_Fragment.ConfigFragment;
import biz.markgo.senior_project.tracksharelocations.Nav_Fragment.FollowNotiFragment;
import biz.markgo.senior_project.tracksharelocations.Nav_Fragment.NewPlaceFragment;
import biz.markgo.senior_project.tracksharelocations.Nav_Fragment.NewTrackingFragment;
import biz.markgo.senior_project.tracksharelocations.Nav_Fragment.QAFragment;
import biz.markgo.senior_project.tracksharelocations.Nav_Fragment.ShareNotiFragment;
import biz.markgo.senior_project.tracksharelocations.Nav_Fragment.ShareringFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient2;
    private Button btnSignOut, btnRevokeAccess;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    String Namesnippet="";

    private ImageView profileImage;
    Bitmap bitmap;
    String statusLogin,FirstName,LastName,personPhotoUrl,email,account_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header= navigationView.getHeaderView(0);

        ImageView im_profile_nav=(ImageView) header.findViewById(R.id.im_profile_nav);
        TextView tv_name_nav=(TextView) header.findViewById(R.id.tv_name_nav);
        TextView tv_email_nav=(TextView) header.findViewById(R.id.tv_email_nav);

        //login status

        final Intent intent = getIntent();
        statusLogin = intent.getStringExtra("statusLogin");
        FirstName = intent.getStringExtra("FirstName");
        LastName = intent.getStringExtra("LastName");
        personPhotoUrl = intent.getStringExtra("personPhotoUrl");
        email = intent.getStringExtra("email");
        account_id = intent.getStringExtra("account_id");

        User_Data.setStatusLogin(statusLogin);
        User_Data.setFirstName(FirstName);
        User_Data.setLastName(LastName);
        User_Data.setPersonPhotoUrl(personPhotoUrl);
        User_Data.setEmail(email);
        User_Data.setAccount_id(account_id);

        Log.i(TAG,email+" "+account_id+" "+FirstName+LastName+" "+personPhotoUrl);

        if(statusLogin.equals("google")){

       Glide.with(getApplicationContext())
                .load(User_Data.getPersonPhotoUrl())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(im_profile_nav);

        tv_name_nav.setText(User_Data.getFirstName()+" "+User_Data.getLastName());
        tv_email_nav.setText(User_Data.getEmail());

        }else if(statusLogin.equals("facebook")) {
           //"http://graph.facebook.com/"+account_id+"/picture?type=large" ลิ้งภาพ
            Glide.with(getApplicationContext())
                    .load(User_Data.getPersonPhotoUrl())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(im_profile_nav);
            //new HomeActivity.DownloadImage((ImageView)findViewById(R.id.im_profile_nav)).execute(personPhotoUrl);
            tv_name_nav.setText(User_Data.getFirstName()+" "+User_Data.getLastName());
            tv_email_nav.setText(User_Data.getEmail());

        }else if(statusLogin.equals("Nologin")){
            im_profile_nav.setImageResource(R.mipmap.ic_launcher);
            tv_name_nav.setText("ผู้ใช้ทั่วไป");
            tv_email_nav.setText("");
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient2 = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    // เมื่อกดปุ่ม back ให้ออกจากโปรแกรม
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("ออกจากแอพพลิเคชัน"); //ไปที่เเวรูเเล้วก็สตริง
            dialog.setIcon(R.drawable.logo);
            dialog.setMessage("คุณต้องการออกจากแอพพลิเคชัน ?");
            dialog.setCancelable(true);

            dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            RelativeLayout item2 = (RelativeLayout)findViewById(R.id.home_for_fragment);
            View child = getLayoutInflater().inflate(R.layout.content_home, null);
            getSupportActionBar().setTitle("หน้าแรก");
            item2.addView(child);

        }else if (id == R.id.nav_NewPlace) {

           NewPlaceFragment newPlaceFragment= new NewPlaceFragment();
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.home_for_fragment,
                    newPlaceFragment,
                    newPlaceFragment.getTag()
            ).commit();
        } else if (id == R.id.nav_NewTacking) {

            NewTrackingFragment newTrackingFragment= new NewTrackingFragment();
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.home_for_fragment,
                    newTrackingFragment,
                    newTrackingFragment.getTag()
            ).commit();

        } else if (id == R.id.nav_sharering) {

            ShareringFragment shareringFragment= new ShareringFragment();
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.home_for_fragment,
                    shareringFragment,
                    shareringFragment.getTag()
            ).commit();
        } else if (id == R.id.nav_share_noti) {

            ShareNotiFragment shareNotiFragment= new ShareNotiFragment();
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.home_for_fragment,
                    shareNotiFragment,
                    shareNotiFragment.getTag()
            ).commit();

        } else if (id == R.id.nav_follow_noti) {

            FollowNotiFragment followNotiFragment= new FollowNotiFragment();
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.home_for_fragment,
                    followNotiFragment,
                    followNotiFragment.getTag()
            ).commit();

        } else if (id == R.id.nav_config) {

            ConfigFragment configFragment= new ConfigFragment();
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.home_for_fragment,
                    configFragment,
                    configFragment.getTag()
            ).commit();

        } else if (id == R.id.nav_QA) {

            QAFragment qaFragment = new QAFragment();
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.home_for_fragment,
                    qaFragment,
                    qaFragment.getTag()
            ).commit();

        } else if (id == R.id.nav_logout) {
            if(statusLogin.equals("google")) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient2).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient2).setResultCallback(
                                        new ResultCallback<Status>() {
                                            @Override
                                            public void onResult(Status status) {
                                                Intent intent_RevokeAccess = new Intent(HomeActivity.this, ChooseLoginActivity.class);
                                                startActivity(intent_RevokeAccess);
                                            }
                                        });
                            }
                        });
            }else if (statusLogin.equals("facebook")) {
                //facbook logout
                LoginManager.getInstance().logOut();
                Intent intent_RevokeAccess = new Intent(HomeActivity.this, ChooseLoginActivity.class);
                startActivity(intent_RevokeAccess);

            }else if(statusLogin.equals("Nologin")){
                Intent intent_RevokeAccess = new Intent(HomeActivity.this, ChooseLoginActivity.class);
                startActivity(intent_RevokeAccess);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }




}
