package biz.markgo.senior_project.tracksharelocations.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import biz.markgo.senior_project.tracksharelocations.API.URL_API;
import biz.markgo.senior_project.tracksharelocations.Begin.SelectModeActivity;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.FirebaseNoti.Main2Activity;
import biz.markgo.senior_project.tracksharelocations.Network.ConnectionDetector;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.Data.User_Data;

public class ChooseLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = ChooseLoginActivity.class.getSimpleName();
    private JSONArray Memebr_Information;
    Context ctx=this;

    private ProgressDialog loading;
    // กำหนดค่าเริ่มต้นของสถานะเป็น false
    Boolean isInternetPresent = false;

    // เรียกใช้งาน  ConnectionDetector
    ConnectionDetector cd;
    //ข้อมูลผู้ใช้
    private String statusLogin = "";
    private String personPhotoUrl = "";
    private String email = "";
    private String account_id = "";
    private String FirstName = "";
    private String LastName = "";
    private String email_facebook = "";


    //facebook
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    // google
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    GraphRequest graphRequest;

    Button bt_loginNo;
    LoginButton bt_loginFacebook;
//    TextView tv_loginNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_choose_login);


        Button bt_loginGoogle = (Button) findViewById(R.id.bt_loginGoogle);
//        tv_loginNo = (TextView) findViewById(R.id.tv_loginNo);
        bt_loginNo = (Button) findViewById(R.id.bt_loginNo);
        bt_loginFacebook = (LoginButton) findViewById(R.id.bt_loginFacebook);


        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            // หากไม่ได้เชื่อมต่ออินเตอร์เน็ต
            showAlertDialog(ChooseLoginActivity.this, "เชื่อต่ออินเตอร์เน็ตล้มเหลว",
                    "คุณไม่ได้เชื่อมต่ออินเตอร์เน็ต.", false);
        }


//#####################################################################################
        // facebook
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };
        accessTokenTracker.startTracking();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
            }
        };

        profileTracker.startTracking();

        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                //Json Object Data facebook เพื่อเอา email ผู้ใช้
                AccessToken accessToken = loginResult.getAccessToken();
                graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        if (response.getError() != null) {
                            Log.e(TAG, "Error in Response " + response);
                        } else {

                            email_facebook = object.optString("email");
                            User_Data.setEmail(email_facebook);
                            Log.e(TAG, "Json Object Data " + object + " Email id " + email_facebook);
                        }

                    }
                });

                Bundle bundle = new Bundle();
                bundle.putString("fields", "id,email,name");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();

                profileTracker = new ProfileTracker() {

                    @Override
                    protected void onCurrentProfileChanged(
                            Profile oldProfile, Profile currentProfile) {
                        Log.e(TAG,"ttttface: onCurrentProfileChanged2");
                        Profile profile = Profile.getCurrentProfile();
                        nextActivity(profile);
                    }
                };
                profileTracker.startTracking();

                //Log.i(TAG, user.email_facebook );
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
                //Toast.makeText(getApplicationContext(), "เข้าสู่ระบบ...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {

            }
        };
        bt_loginFacebook.setReadPermissions("email", "user_likes", "user_friends");
        bt_loginFacebook.registerCallback(callbackManager, callback);

//#####################################################################################
        // google+

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



//#####################################################################################
        //เมื่อคลิ๊กปุ่มเข้าสู่ระบบผ่าน google
        bt_loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectingToInternet();
                // ตรวจสอบสถานะการเชื่อมต่ออินเตอร์เน็ต
                if (isInternetPresent) {
                    //หากเชื่อมต่ออินเตอร์เน็ต
//                    showAlertDialog(ChooseLoginActivity.this, "กำลังเชื่อมต่ออินเตอร์เน็ต",
//                            "คุณกำลังเชื่อมต่ออินเตอร์เน็ต", true)
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);

                } else {
                    // หากไม่ได้เชื่อมต่ออินเตอร์เน็ต
                    showAlertDialog(ChooseLoginActivity.this, "เชื่อต่ออินเตอร์เน็ตล้มเหลว",
                            "คุณไม่ได้เชื่อมต่ออินเตอร์เน็ต.", false);
                }

            }
        });

//#####################################################################################
        //เมื่อคลิ๊กปุ่มข้าม (ไม่ login)
        bt_loginNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {

                    User_Data.setStatusLogin("Nologin");
                    MemberInformation.setType_account("Nologin");

                 //   Toast.makeText(getApplicationContext(), "เข้าสู่ระบบ...", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ChooseLoginActivity.this, SelectModeActivity.class);
                    startActivity(intent);

//                    startActivity(new Intent(ChooseLoginActivity.this, Main2Activity.class));
                } else {
                    // หากไม่ได้เชื่อมต่ออินเตอร์เน็ต
                    showAlertDialog(ChooseLoginActivity.this, "เชื่อต่ออินเตอร์เน็ตล้มเหลว",
                            "คุณไม่ได้เชื่อมต่ออินเตอร์เน็ต.", false);
                }


            }
        });
//#####################################################################################

    }


    //facebook
    //ส่วนโปรแกรม facebook login
    @Override
    protected void onResume() {
        super.onResume();
       // Facebook login
        Log.e(TAG,"ttttface: onResume");
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            Profile profile = Profile.getCurrentProfile();
            nextActivity(profile);

        }else{
            // หากไม่ได้เชื่อมต่ออินเตอร์เน็ต
            showAlertDialog(ChooseLoginActivity.this, "เชื่อต่ออินเตอร์เน็ตล้มเหลว",
                    "คุณไม่ได้เชื่อมต่ออินเตอร์เน็ต.", false);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"ttttface:  onPause");
//        Profile profile = Profile.getCurrentProfile();
//        nextActivity(profile);

    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void nextActivity(Profile profile) {
        if (profile != null) {
            //########################################################################################
            loading = new ProgressDialog(ChooseLoginActivity.this);
            loading.setTitle("กำลังเข้าสู่ระบบ....");
            loading.setMessage("กรุณารอสักครู...");
            loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading.show();
            //########################################################################################
            statusLogin = "facebook";
            account_id = profile.getId();
            FirstName = profile.getFirstName();
            LastName = profile.getLastName();
            personPhotoUrl = profile.getProfilePictureUri(200, 200).toString();

            User_Data.setStatusLogin(statusLogin);
            User_Data.setAccount_id(account_id);
            User_Data.setFirstName(FirstName);
            User_Data.setLastName(LastName);
            User_Data.setPersonPhotoUrl(personPhotoUrl);

           // bt_loginFacebook.setLoginBehavior();
            //ส่งค่าไปคลาส BackGround เพื่อเช็คว่ามีสมาชิกใน database server ยัง
            BackGround b = new BackGround();
            b.execute(User_Data.getAccount_id(), User_Data.getStatusLogin());
        }
    }

    //

    //########################################################################################
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        //facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    //########################################################################################
    //google
    //ส่วนโปรแกรม google login
    private void handleSignInResult(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            loading = new ProgressDialog(ChooseLoginActivity.this);
            loading.setTitle("กำลังเข้าสู่ระบบ....");
            loading.setMessage("กรุณารอสักครู...");
            loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading.show();

            //Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;

            Log.e(TAG, "display name: " + acct.getDisplayName());
            Log.i(TAG, acct.toString());
            statusLogin = "google";
            account_id = acct.getId().toString();
            email = acct.getEmail().toString();
            FirstName = acct.getGivenName().toString();
            LastName = acct.getFamilyName().toString();
            personPhotoUrl = acct.getPhotoUrl().toString();


            User_Data.setStatusLogin(statusLogin);
            User_Data.setEmail(email);
            User_Data.setAccount_id(account_id);
            User_Data.setFirstName(FirstName);
            User_Data.setLastName(LastName);
            User_Data.setPersonPhotoUrl(personPhotoUrl);

            //ส่งค่าไปคลาส BackGround เพื่อเช็คว่ามีสมาชิกใน database server ยัง
            BackGround b = new BackGround();
            b.execute(User_Data.getAccount_id(), User_Data.getStatusLogin());

        } else {
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.

                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                //########################################################################################
                //########################################################################################
                //showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        //hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }

        }else{
            // หากไม่ได้เชื่อมต่ออินเตอร์เน็ต
            showAlertDialog(ChooseLoginActivity.this, "เชื่อต่ออินเตอร์เน็ตล้มเหลว",
                    "คุณไม่ได้เชื่อมต่ออินเตอร์เน็ต.", false);
        }

    }
    //########################################################################################
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
    //########################################################################################
    public GoogleApiClient getmGoogleApiClient() {
        return this.mGoogleApiClient;
    }

    //########################################################################################

    // เมื่อกดปุ่ม back ให้ออกจากโปรแกรม
    public void onBackPressed() {
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


    //แจ้งเตือนอินเตอเน็ต
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setIcon(R.drawable.logo);
        dialog.setMessage(message);
        dialog.setCancelable(true);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.show();
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String account_id = params[0];
            String statusLogin = params[1];
            String data = "";
            String Api_Key = "MemberStatusCheck1234";
            Log.i(TAG, account_id);
            Log.i(TAG, statusLogin);
            int tmp;

            try {

                URL url = new URL(URL_API.getAPI_member_status_check());
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
                JSONObject check_member = root.getJSONObject("check_member");
                status = check_member.getString("status");
                Log.i(TAG, status);
                Log.i(TAG, account_id);
                Log.i(TAG, "email_facebook : " + email_facebook);
                Log.i(TAG, email);

                if (status.equals("1")) {
                    loading.dismiss();
                    Intent intenDetailRegisterActivity = new Intent(ChooseLoginActivity.this, DetailRegisterActivity.class);
                    startActivity(intenDetailRegisterActivity);

                } else if (status.equals("0")) {

                    BackGround_getMember backGround_getmember = new BackGround_getMember();
                    backGround_getmember.execute(User_Data.getAccount_id(), User_Data.getStatusLogin());
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

//                    s="เข้าสู่ระบบ";
//                    Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();

                    loading.dismiss();
                    Intent intentMode = new Intent(ChooseLoginActivity.this,SelectModeActivity.class);
                    startActivity(intentMode);

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
