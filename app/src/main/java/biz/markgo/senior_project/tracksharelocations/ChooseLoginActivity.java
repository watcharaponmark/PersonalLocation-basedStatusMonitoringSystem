package biz.markgo.senior_project.tracksharelocations;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChooseLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = ChooseLoginActivity.class.getSimpleName();


    //ข้อมูลผู้ใช้
    private String statusLogin = "";
    private String personName = "";
    private String personPhotoUrl = "";
    private String email = "";
    private String account_id = "";
    private String email_facebook="";
    User user=new User();

    //facebook
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    // google
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    GraphRequest graphRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_choose_login);


        Button bt_loginGoogle = (Button) findViewById(R.id.bt_loginGoogle);
        TextView tv_loginNo = (TextView) findViewById(R.id.tv_loginNo);
        LoginButton bt_loginFacebook = (LoginButton) findViewById(R.id.bt_loginFacebook);


        // facebook
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //Json Object Data facebook เพื่อเอา email ผู้ใช้
                AccessToken accessToken = loginResult.getAccessToken();
                 graphRequest=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                if (response.getError() != null) {
                                    Log.e(TAG, "Error in Response " + response);
                                } else {

                                    email_facebook = object.optString("email");
                                    user.email_facebook = object.optString("email");
                                    Log.e(TAG, "Json Object Data " + object + " Email id " +  email_facebook);
                                }

                            }
                    });
                Bundle bundle=new Bundle();
                bundle.putString("fields","id,email,name");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();

                //Log.i(TAG, user.email_facebook );
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
                Toast.makeText(getApplicationContext(), "เข้าสู่ระบบ...", Toast.LENGTH_SHORT).show();
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




        // google+
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //เมื่อคลิ๊กปุ่มเข้าสู่ระบบผ่าน google
        bt_loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


        //เมื่อคลิ๊กปุ่มข้าม (ไม่ login)
        tv_loginNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseLoginActivity.this, HomeActivity.class);
                intent.putExtra("statusLogin", "Nologin");
                startActivity(intent);
            }
        });


    }


    //facebook
    //ส่วนโปรแกรม facebook login
    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login

        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void nextActivity(Profile profile) {
        if (profile != null) {
            statusLogin = "facebook";
            account_id = profile.getId();
            personName = profile.getName();
            personPhotoUrl = profile.getProfilePictureUri(200, 200).toString();

            //ส่งค่าไปคลาส BackGround เพื่อเช็คว่ามีสมาชิกใน database server ยัง
            BackGround b = new BackGround();
            b.execute(account_id, email_facebook );
        }
    }

    //


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


    //google
    //ส่วนโปรแกรม google login
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            //Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "display name: " + acct.getDisplayName());
            Log.i(TAG,acct.toString());
            statusLogin = "google";
            account_id = acct.getId().toString();
            email = acct.getEmail().toString();
            personName = acct.getDisplayName().toString();
            personPhotoUrl = acct.getPhotoUrl().toString();

            //ส่งค่าไปคลาส BackGround เพื่อเช็คว่ามีสมาชิกใน database server ยัง
            BackGround b = new BackGround();
            b.execute(account_id, email);

        } else {
        }
    }

    @Override
    public void onStart() {
        super.onStart();

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
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public GoogleApiClient getmGoogleApiClient() {
        return this.mGoogleApiClient;
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loding));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    //


    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String account_id = params[0];
            String email_check = params[1];
            String data = "";
            int tmp;

            try {

                URL url = new URL("http://senior-project.markgo.biz/member/member_status_check.php");
                String urlParams = "account_id=" + account_id + "&email=" + email_check;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
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
            String statusID = "";
            try {
                JSONObject root = new JSONObject(s);
                JSONObject check_member = root.getJSONObject("check_member");
                statusID = check_member.getString("statusID");
                Log.i(TAG, statusID);
                Log.i(TAG, account_id);
                Log.i(TAG,"email_facebook : "+ email_facebook );
                Log.i(TAG, " user.email_facebook :"+user.email_facebook );
                Log.i(TAG, email);
                if (statusID.equals("No")) {
                    Intent intenDetailRegisterActivity = new Intent(ChooseLoginActivity.this, DetailRegisterActivity.class);

                    if(statusLogin.equals("facebook")){
                        intenDetailRegisterActivity.putExtra("statusLogin", statusLogin);
                        intenDetailRegisterActivity.putExtra("personName", personName);
                        intenDetailRegisterActivity.putExtra("personPhotoUrl", personPhotoUrl);
                        intenDetailRegisterActivity.putExtra("email", email_facebook );
                        intenDetailRegisterActivity.putExtra("account_id", account_id);
                    }else if(statusLogin.equals("google")){
                        intenDetailRegisterActivity.putExtra("statusLogin", statusLogin);
                        intenDetailRegisterActivity.putExtra("personName", personName);
                        intenDetailRegisterActivity.putExtra("personPhotoUrl", personPhotoUrl);
                        intenDetailRegisterActivity.putExtra("email", email);
                        intenDetailRegisterActivity.putExtra("account_id", account_id);
                    }

                    startActivity(intenDetailRegisterActivity);

                } else if (statusID.equals("Yes")) {
                    Intent intenHomeActivity = new Intent(ChooseLoginActivity.this, HomeActivity.class);
                    if(statusLogin.equals("facebook")){
                        intenHomeActivity.putExtra("statusLogin", statusLogin);
                        intenHomeActivity.putExtra("personName", personName);
                        intenHomeActivity.putExtra("personPhotoUrl", personPhotoUrl);
                        intenHomeActivity.putExtra("email",email_facebook );
                        intenHomeActivity.putExtra("account_id", account_id);
                    }else if(statusLogin.equals("google")){
                        intenHomeActivity.putExtra("statusLogin", statusLogin);
                        intenHomeActivity.putExtra("personName", personName);
                        intenHomeActivity.putExtra("personPhotoUrl", personPhotoUrl);
                        intenHomeActivity.putExtra("email", email);
                        intenHomeActivity.putExtra("account_id", account_id);
                    }

                    startActivity(intenHomeActivity);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }

        }
    }

}
