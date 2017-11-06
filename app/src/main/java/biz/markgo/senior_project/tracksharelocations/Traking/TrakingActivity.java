package biz.markgo.senior_project.tracksharelocations.Traking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.lang.ref.WeakReference;
import java.util.Iterator;

import biz.markgo.senior_project.tracksharelocations.ChangePage.BackButtonHandlerInterface;
import biz.markgo.senior_project.tracksharelocations.ChangePage.OnBackClickListener;
import biz.markgo.senior_project.tracksharelocations.ChangePage.Page;
import biz.markgo.senior_project.tracksharelocations.Config.Profile.ConfigProfileActivity;
import biz.markgo.senior_project.tracksharelocations.Login.ChooseLoginActivity;
import biz.markgo.senior_project.tracksharelocations.QA.QAFragment;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.PlaceStatusActivity;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.Begin.SelectModeActivity;
import biz.markgo.senior_project.tracksharelocations.ServiceBackground.LocationMonitorService;
import biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.FollowHistoryFragment;
import biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking.TrackingFragment;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.Data.User_Data;
import biz.markgo.senior_project.tracksharelocations.Config.ConfigFragment;

public class TrakingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener,
        BackButtonHandlerInterface {
    private GoogleApiClient mGoogleApiClient3;
    private static final String TAG = TrakingActivity.class.getSimpleName();
    private ImageView im_profile_nav_traker;
    private TextView tv_name_nav_traker, tv_email_nav_traker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        im_profile_nav_traker = (ImageView) header.findViewById(R.id.im_profile_nav_traker);
        tv_name_nav_traker = (TextView) header.findViewById(R.id.tv_name_nav_traker);
        tv_email_nav_traker = (TextView) header.findViewById(R.id.tv_email_nav_traker);
//#####################################################################################
        TrackingFragment homeFragment = new TrackingFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.traking_for_fragment,
                homeFragment,
                homeFragment.getTag()
        ).commit();
//#####################################################################################
        im_profile_nav_traker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TrakingActivity.this,ConfigProfileActivity.class);
                startActivity(intent);
            }
        });

        tv_name_nav_traker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TrakingActivity.this,ConfigProfileActivity.class);
                startActivity(intent);
            }
        });
        tv_email_nav_traker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TrakingActivity.this,ConfigProfileActivity.class);
                startActivity(intent);
            }
        });
//#####################################################################################
        if (MemberInformation.getType_account().equals("google")) {

            Glide.with(getApplicationContext())
                    .load(MemberInformation.getPicture_name().toString())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(im_profile_nav_traker);

            tv_name_nav_traker.setText(MemberInformation.getFirstName() + " " + MemberInformation.getLastName());
            tv_email_nav_traker.setText(MemberInformation.getEmail());

        } else if (MemberInformation.getType_account().equals("facebook")) {
            Glide.with(getApplicationContext())
                    .load(MemberInformation.getPicture_name().toString())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(im_profile_nav_traker);

            tv_name_nav_traker.setText(MemberInformation.getFirstName() + " " + MemberInformation.getLastName());
            tv_email_nav_traker.setText(MemberInformation.getEmail());

        } else if (MemberInformation.getType_account().equals("Nologin")) {
            im_profile_nav_traker.setImageResource(R.mipmap.ic_launcher);
            tv_name_nav_traker.setText("ผู้ใช้ทั่วไป");
            tv_email_nav_traker.setText("");
        }
//#####################################################################################


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient3 = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
//#####################################################################################
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    // เมื่อกดปุ่ม back ให้ออกจากโปรแกรม
    //#####################################################################################

    @Override
    public void addBackClickListener(OnBackClickListener onBackClickListener) {
        Page.backClickListenersList.add(new WeakReference<>(onBackClickListener));
    }

    @Override
    public void removeBackClickListener(OnBackClickListener onBackClickListener) {
        for (Iterator<WeakReference<OnBackClickListener>> iterator = Page.backClickListenersList.iterator();
             iterator.hasNext();){
            WeakReference<OnBackClickListener> weakRef = iterator.next();
            if (weakRef.get() == onBackClickListener){
                iterator.remove();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(!fragmentsBackKeyIntercept()){
            super.onBackPressed();
        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            //super.onBackPressed();
//            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//            dialog.setTitle("ออกจากแอพพลิเคชัน"); //ไปที่เเวรูเเล้วก็สตริง
//            dialog.setIcon(R.drawable.logo);
//            dialog.setMessage("คุณต้องการออกจากแอพพลิเคชัน ?");
//            dialog.setCancelable(true);
//
//            dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_HOME);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//            });
//            dialog.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            dialog.show();
//        }
   }


    private boolean fragmentsBackKeyIntercept() {
        boolean isIntercept = false;
        for (WeakReference<OnBackClickListener> weakRef : Page.backClickListenersList) {
            OnBackClickListener onBackClickListener = weakRef.get();
            if (onBackClickListener != null) {
                boolean isFragmIntercept = onBackClickListener.onBackClick();
                if (!isIntercept) isIntercept = isFragmIntercept;
            }
        }
        return isIntercept;
    }
    //#####################################################################################
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.traking, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings_tracking) {
                if(MemberInformation.getType_account().equals("Nologin")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("ไม่สามารถเข้าใช้งานในโหมดนี้ได้"); //ไปที่เเวรูเเล้วก็สตริง
                    dialog.setIcon(R.drawable.logo);
                    dialog.setMessage("กรุณาเข้าสู่ระบบเพื่อใช้งาน");
                    dialog.setCancelable(true);

                    dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(TrakingActivity.this, ChooseLoginActivity.class);
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
                    Intent intent = new Intent(TrakingActivity.this, PlaceStatusActivity.class);
                    startActivity(intent);
                }

            }else if (id == R.id.action_settings_main) {
                Intent intent =new Intent(TrakingActivity.this,SelectModeActivity.class);
                startActivity(intent);
            }

            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
            // Handle navigation view item clicks here.
            int id = item.getItemId();

           /* if (id == R.id.nav_home) {
//            RelativeLayout item2 = (RelativeLayout) findViewById(R.id.home_for_fragment);
//            View child = getLayoutInflater().inflate(R.layout.content_place_status, null);
//            getSupportActionBar().setTitle("หน้าแรก");
//            item2.addView(child);

                PlaceStatusFragment homeFragment = new PlaceStatusFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(
                        R.id.traking_for_fragment,
                        homeFragment,
                        homeFragment.getTag()
                ).commit();

            } else*/ if (id == R.id.nav_Tacking) {

                TrackingFragment TrackingFragment = new TrackingFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(
                        R.id.traking_for_fragment,
                        TrackingFragment,
                        TrackingFragment.getTag()
                ).commit();

            } else if (id == R.id.nav_TackingToPlace) {

                if(MemberInformation.getType_account().equals("Nologin")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("ไม่สามารถเข้าใช้งานในโหมดนี้ได้"); //ไปที่เเวรูเเล้วก็สตริง
                    dialog.setIcon(R.drawable.logo);
                    dialog.setMessage("กรุณาเข้าสู่ระบบเพื่อใช้งาน");
                    dialog.setCancelable(true);

                    dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(TrakingActivity.this, ChooseLoginActivity.class);
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
                    Intent intent_PlaceStatusActivity = new Intent(TrakingActivity.this, PlaceStatusActivity.class);
                    startActivity(intent_PlaceStatusActivity);
                }

            } else if (id == R.id.nav_follow_history) {

                FollowHistoryFragment followNotiFragment = new FollowHistoryFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(
                        R.id.traking_for_fragment,
                        followNotiFragment,
                        followNotiFragment.getTag()
                ).commit();

            }else if (id == R.id.nav_config) {

                ConfigFragment configFragment = new ConfigFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(
                        R.id.traking_for_fragment,
                        configFragment,
                        configFragment.getTag()
                ).commit();

            } else if (id == R.id.nav_QA) {

                QAFragment qaFragment = new QAFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(
                        R.id.traking_for_fragment,
                        qaFragment,
                        qaFragment.getTag()
                ).commit();

            } else if (id == R.id.nav_logout) {
                stopService(new Intent(this, LocationMonitorService.class));
                if (MemberInformation.getType_account().equals("google")) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient3).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient3).setResultCallback(
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

                                                    Intent intent_RevokeAccess = new Intent(TrakingActivity.this, ChooseLoginActivity.class);
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

                    Intent intent_RevokeAccess = new Intent(TrakingActivity.this, ChooseLoginActivity.class);
                    startActivity(intent_RevokeAccess);

                } else if (MemberInformation.getType_account().equals("Nologin")) {
                    Intent intent_RevokeAccess = new Intent(TrakingActivity.this, ChooseLoginActivity.class);
                    startActivity(intent_RevokeAccess);
                }else{
                    Intent intent_RevokeAccess = new Intent(TrakingActivity.this, ChooseLoginActivity.class);
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
