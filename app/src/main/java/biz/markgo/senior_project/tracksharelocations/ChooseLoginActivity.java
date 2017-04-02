package biz.markgo.senior_project.tracksharelocations;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

public class ChooseLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = ChooseLoginActivity.class.getSimpleName();
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView btnLogin;
    private ProgressDialog progressDialog;
    User user;

    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    //ข้อมูลผู้ใช้ google
    String personName="";
    String personPhotoUrl="";
    String email = "";
    String user_id="";

    Context ctx=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login);

        Button bt_loginFacebook =(Button) findViewById(R.id.bt_loginFacebook);
        Button bt_loginGoogle =(Button) findViewById(R.id.bt_loginGoogle);
        TextView tv_loginNo =(TextView) findViewById(R.id.tv_loginNo);

        //เมื่อคลิ๊กปุ่มเข้าสู่ระบบผ่าน facebook
        bt_loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(PrefUtils.getCurrentUser(ChooseLoginActivity.this) != null){
                    Intent homeIntent = new Intent(ChooseLoginActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    finish();
                }*/
            }
        });

        // google+
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
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
                Intent intent=new Intent(ChooseLoginActivity.this,HomeActivity.class);
                intent.putExtra("name_nologin","Noname");
                intent.putExtra("statusLogin","Nologin");
                startActivity(intent);
            }
        });
    }

//ส่วนโปรแกรม google login
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
           //Log.e(TAG, "display name: " + acct.getDisplayName());
           // Log.i(TAG,acct.toString());
            user_id=acct.getId().toString();
            email = acct.getEmail().toString();
            personName = acct.getDisplayName().toString();
            personPhotoUrl = acct.getPhotoUrl().toString();

           //ส่งค่าไปคลาส BackGround เพื่อเช็คว่ามีสมาชิกใน database server ยัง
            BackGround b = new BackGround();
            b.execute(user_id, email);

        } else {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        //callbackManager.onActivityResult(requestCode, resultCode, data);
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

    public GoogleApiClient getmGoogleApiClient(){
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


  /*  //ส่วนโปรแกรม facebook login
    @Override
    protected void onResume() {
        super.onResume();


        callbackManager=CallbackManager.Factory.create();

       loginButton= (LoginButton)findViewById(R.id.login_button);

       loginButton.setReadPermissions("public_profile", "email","user_friends");

        btnLogin= (TextView) findViewById(R.id.bt_loginFacebook);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(ChooseLoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                loginButton.registerCallback(callbackManager, mCallBack);

                loginButton.setPressed(false);

                loginButton.invalidate();

            }
        });
    }


    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            try {
                                user = new User();
                                user.facebookID = object.getString("id").toString();
                                user.email = object.getString("email").toString();
                                user.name = object.getString("name").toString();
                                user.gender = object.getString("gender").toString();
                                PrefUtils.setCurrentUser(user,ChooseLoginActivity.this);

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Toast.makeText(ChooseLoginActivity.this,"welcome "+user.name,Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(ChooseLoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();

                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
        }
    };*/
  class BackGround extends AsyncTask<String, String, String> {

      @Override
      protected String doInBackground(String... params) {
          String user_id = params[0];
          String email = params[1];
          String data="";
          int tmp;

          try {
              URL url = new URL("http://senior-project.markgo.biz/member/member_status_check.php");
              String urlParams = "user_id="+user_id+"&email="+email;

              HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
              httpURLConnection.setDoOutput(true);
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
          String err=null;
          String statusID="";
          try {
              JSONObject root = new JSONObject(s);
              JSONObject check_member = root.getJSONObject("check_member");
              statusID = check_member.getString("statusID");
              Log.i(TAG,statusID);
              Log.i(TAG,user_id);
              Log.i(TAG,email);
              if(statusID.equals("No")){
                  Intent intenDetailRegisterActivity=new Intent(ChooseLoginActivity.this,DetailRegisterActivity.class);
                  intenDetailRegisterActivity.putExtra("statusLogin","google");
                  intenDetailRegisterActivity.putExtra("personName",personName);
                  intenDetailRegisterActivity.putExtra("personPhotoUrl",personPhotoUrl);
                  intenDetailRegisterActivity.putExtra("email",email);
                  intenDetailRegisterActivity.putExtra("user_id",user_id);
                  startActivity(intenDetailRegisterActivity);

              }else if(statusID.equals("Yes")){
                  Intent intenHomeActivity=new Intent(ChooseLoginActivity.this,HomeActivity.class);
                  intenHomeActivity.putExtra("statusLogin","google");
                  intenHomeActivity.putExtra("personName",personName);
                  intenHomeActivity.putExtra("personPhotoUrl",personPhotoUrl);
                  intenHomeActivity.putExtra("email",email);
                  intenHomeActivity.putExtra("user_id",user_id);
                  startActivity(intenHomeActivity);
              }

          } catch (JSONException e) {
              e.printStackTrace();
              err = "Exception: "+e.getMessage();
          }

      }
  }

}
