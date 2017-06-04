package biz.markgo.senior_project.tracksharelocations;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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

import biz.markgo.senior_project.tracksharelocations.Nav_Fragment.NewPlaceFragment;
import biz.markgo.senior_project.tracksharelocations.Nav_Fragment.NewTrackingFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient2;
    private Button btnSignOut, btnRevokeAccess;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    String Namesnippet="";

    private User user;
    private ImageView profileImage;
    Bitmap bitmap;

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
        String statusLogin = intent.getStringExtra("statusLogin");

        //login google
        final String personName = intent.getStringExtra("personName");
        final String personPhotoUrl = intent.getStringExtra("personPhotoUrl");
        String email = intent.getStringExtra("email");
        String account_id = intent.getStringExtra("account_id");

        Log.i(TAG,email+" "+account_id+" "+personName+" "+personPhotoUrl);

        if(statusLogin.equals("google")){

       Glide.with(getApplicationContext())
                .load(personPhotoUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(im_profile_nav);

        tv_name_nav.setText(personName);
        tv_email_nav.setText(email);

        }else if(statusLogin.equals("facebook")) {

            Glide.with(getApplicationContext())
                    .load("http://graph.facebook.com/"+account_id+"/picture?type=large")
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(im_profile_nav);
            tv_name_nav.setText(personName);
            tv_email_nav.setText(email);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        if (id == R.id.nav_NewPlace) {

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

        } else if (id == R.id.nav_share_noti) {

        } else if (id == R.id.nav_follow_noti) {

        } else if (id == R.id.nav_config) {

        } else if (id == R.id.nav_QA) {

        } else if (id == R.id.nav_logout) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient2).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient2).setResultCallback(
                                    new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(Status status) {
                                            Intent intent_RevokeAccess = new Intent(HomeActivity.this, ChooseLoginActivity.class);
                                            //facbook logout
                                            LoginManager.getInstance().logOut();

                                            startActivity(intent_RevokeAccess);
                                        }
                                    });
                        }
                    });
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
