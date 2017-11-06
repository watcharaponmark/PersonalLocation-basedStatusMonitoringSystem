package biz.markgo.senior_project.tracksharelocations.PlaceStatus;

import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import biz.markgo.senior_project.tracksharelocations.API.URL_API;
import biz.markgo.senior_project.tracksharelocations.ChangePage.BackButtonHandlerInterface;
import biz.markgo.senior_project.tracksharelocations.ChangePage.OnBackClickListener;
import biz.markgo.senior_project.tracksharelocations.ChangePage.Page;
import biz.markgo.senior_project.tracksharelocations.Config.Profile.ConfigProfileActivity;
import biz.markgo.senior_project.tracksharelocations.Login.ChooseLoginActivity;
import biz.markgo.senior_project.tracksharelocations.Config.ConfigFragment;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.PlaceFragment;
import biz.markgo.senior_project.tracksharelocations.QA.QAFragment;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.ShareHistoryFragment;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.Begin.SelectModeActivity;
import biz.markgo.senior_project.tracksharelocations.ServiceBackground.LocationMonitorService;
import biz.markgo.senior_project.tracksharelocations.Traking.TrakingActivity;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.Data.User_Data;


public class PlaceStatusActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,
        GoogleApiClient.OnConnectionFailedListener,BackButtonHandlerInterface {

    private static final String TAG = PlaceStatusActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient2;
    private ImageView im_profile_nav;
    private TextView tv_name_nav, tv_email_nav;
    String statusLogin, FirstName, LastName, personPhotoUrl, email, account_id;
    private JSONArray Memebr_Information;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_status);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        im_profile_nav = (ImageView) header.findViewById(R.id.im_profile_nav);
        tv_name_nav = (TextView) header.findViewById(R.id.tv_name_nav);
        tv_email_nav = (TextView) header.findViewById(R.id.tv_email_nav);

//#####################################################################################
        PlaceFragment homeFragment = new PlaceFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.place_status_for_fragment,
                homeFragment,
                homeFragment.getTag()
        ).commit();

//#####################################################################################
        im_profile_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlaceStatusActivity.this,ConfigProfileActivity.class);
                startActivity(intent);
            }
        });

        tv_name_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlaceStatusActivity.this,ConfigProfileActivity.class);
                startActivity(intent);
            }
        });
        tv_email_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlaceStatusActivity.this,ConfigProfileActivity.class);
                startActivity(intent);
            }
        });
//#####################################################################################


//        final Intent intent = getIntent();
//        statusLogin = intent.getStringExtra("statusLogin");
//        account_id = intent.getStringExtra("account_id");

         //BackGround_Member
//        BackGround_getMember backGround_getmember = new BackGround_getMember();
//        backGround_getmember.execute(account_id, statusLogin);


//
//        Member_Controler member_controler = new Member_Controler(this);
//        Member member1 = member_controler.getMemberById("M000001");
//
//        ArrayList<HashMap<String, String>> memberList =  member_controler.getmemberList();
//        String mmm = memberList.get(0).get("member_id").toString();
//        Log.d(TAG,member1.member_id);

//#####################################################################################
        if (MemberInformation.getType_account().equals("google")) {

            Glide.with(getApplicationContext())
                    .load(MemberInformation.getPicture_name().toString())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(im_profile_nav);

            tv_name_nav.setText(MemberInformation.getFirstName() + " " + MemberInformation.getLastName());
            tv_email_nav.setText(MemberInformation.getEmail());

        } else if (MemberInformation.getType_account().equals("facebook")) {
            Glide.with(getApplicationContext())
                    .load(MemberInformation.getPicture_name().toString())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(im_profile_nav);

            tv_name_nav.setText(MemberInformation.getFirstName() + " " + MemberInformation.getLastName());
            tv_email_nav.setText(MemberInformation.getEmail());

        } else if (MemberInformation.getType_account().equals("Nologin")) {
            im_profile_nav.setImageResource(R.mipmap.ic_launcher);
            tv_name_nav.setText("ผู้ใช้ทั่วไป");
            tv_email_nav.setText("");
        }
//#####################################################################################
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient2 = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }
//#####################################################################################

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.place_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_place) {
            Intent intent =new Intent(PlaceStatusActivity.this, TrakingActivity.class);
            startActivity(intent);


        }else if (id == R.id.action_settings_main) {
            Intent intent =new Intent(PlaceStatusActivity.this,SelectModeActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       /* if (id == R.id.nav_home) {
            HomePlaceStatusFragment homeFragment = new HomePlaceStatusFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.place_status_for_fragment,
                    homeFragment,
                    homeFragment.getTag()
            ).commit();

        } else*/ if (id == R.id.nav_sharering) {

            PlaceFragment placeFragment = new PlaceFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.place_status_for_fragment,
                    placeFragment,
                    placeFragment.getTag()
            ).commit();


        } else if (id == R.id.nav_PlaceToTacking) {
            Intent intent_TrakingActivity = new Intent(PlaceStatusActivity.this, TrakingActivity.class);
            startActivity(intent_TrakingActivity);

        } else if (id == R.id.nav_share_history) {

            ShareHistoryFragment shareNotiFragment = new ShareHistoryFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.place_status_for_fragment,
                    shareNotiFragment,
                    shareNotiFragment.getTag()
            ).commit();

        } else if (id == R.id.nav_config) {

            ConfigFragment configFragment = new ConfigFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.place_status_for_fragment,
                    configFragment,
                    configFragment.getTag()
            ).commit();

        } else if (id == R.id.nav_QA) {

            QAFragment qaFragment = new QAFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.place_status_for_fragment,
                    qaFragment,
                    qaFragment.getTag()
            ).commit();

        } else if (id == R.id.nav_logout) {
            stopService(new Intent(this, LocationMonitorService.class));
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

                                                Intent intent_RevokeAccess = new Intent(PlaceStatusActivity.this, ChooseLoginActivity.class);
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

                Intent intent_RevokeAccess = new Intent(PlaceStatusActivity.this, ChooseLoginActivity.class);
                startActivity(intent_RevokeAccess);

            } else if (MemberInformation.getType_account().equals("Nologin")) {

                Intent intent_RevokeAccess = new Intent(PlaceStatusActivity.this, ChooseLoginActivity.class);
                startActivity(intent_RevokeAccess);
            }else{

                Intent intent_RevokeAccess = new Intent(PlaceStatusActivity.this, ChooseLoginActivity.class);
                startActivity(intent_RevokeAccess);
            }
            //#####################################################################################
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

//กดปุ่มย้อนกลับ
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


    // เมื่อกดปุ่ม back ให้ออกจากโปรแกรม
    @Override
    public void onBackPressed() {
        if(!fragmentsBackKeyIntercept()){
            super.onBackPressed();
        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//
//
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

                        FirstName = MemberInformation.getFirstName();
                        LastName = MemberInformation.getLastName();
                        personPhotoUrl = MemberInformation.getPicture_name();
                        email = MemberInformation.getEmail();

                        User_Data.setStatusLogin(statusLogin);
                        User_Data.setFirstName(FirstName);
                        User_Data.setLastName(LastName);
                        User_Data.setPersonPhotoUrl(personPhotoUrl);
                        User_Data.setEmail(email);
                        User_Data.setAccount_id(account_id);

                   // s = "บันทึกข้อมูลสำเร็จ";
                    //Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
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
