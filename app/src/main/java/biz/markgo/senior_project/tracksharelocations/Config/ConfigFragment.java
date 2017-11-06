package biz.markgo.senior_project.tracksharelocations.Config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import biz.markgo.senior_project.tracksharelocations.ChangePage.BackButtonHandlerInterface;
import biz.markgo.senior_project.tracksharelocations.ChangePage.OnBackClickListener;
import biz.markgo.senior_project.tracksharelocations.Config.Profile.ConfigProfileActivity;
import biz.markgo.senior_project.tracksharelocations.Login.ChooseLoginActivity;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.Data.User_Data;
import biz.markgo.senior_project.tracksharelocations.ServiceBackground.LocationMonitorService;

public class ConfigFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener , OnBackClickListener {

    private View myFragmentView;
    private BackButtonHandlerInterface backButtonHandler;


    private LinearLayout layout1_seting,layout2_seting;
    private TextView tv_name_setting,tv_email_setting,tv_status_setting,tv_noti,tv_acount,tv_exit;
    private ImageView im_profile_setting;
    private  String str;
    private GoogleApiClient mGoogleApiClient2;
    private  GoogleSignInOptions gso;
    private static final String TAG = ConfigFragment.class.getSimpleName();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_config, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("การตั้งค่า");

        layout1_seting = (LinearLayout) myFragmentView.findViewById(R.id.layout1_seting);
        layout2_seting = (LinearLayout) myFragmentView.findViewById(R.id.layout2_seting);
        tv_status_setting = (TextView) myFragmentView.findViewById(R.id.tv_status_setting) ;
        tv_name_setting = (TextView) myFragmentView.findViewById(R.id.tv_name_setting) ;
        tv_email_setting = (TextView) myFragmentView.findViewById(R.id.tv_email_setting) ;
        im_profile_setting=(ImageView) myFragmentView.findViewById(R.id.im_profile_setting);

        tv_noti = (TextView) myFragmentView.findViewById(R.id.tv_noti) ;
        tv_acount = (TextView) myFragmentView.findViewById(R.id.tv_acount) ;
        tv_exit = (TextView) myFragmentView.findViewById(R.id.tv_exit);

//#####################################################################################

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient2 = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(),1,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

//#####################################################################################

        str = "เข้าสู่ระบบด้วย : "+MemberInformation.getType_account();

        if (MemberInformation.getType_account().equals("google")) {

            Glide.with(getContext())
                    .load(MemberInformation.getPicture_name().toString())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(im_profile_setting);

            tv_status_setting.setText(str);
            tv_name_setting.setText(MemberInformation.getFirstName() + " " + MemberInformation.getLastName());
            tv_email_setting.setText(MemberInformation.getEmail());

        } else if (MemberInformation.getType_account().equals("facebook")) {
            Glide.with(getContext())
                    .load(MemberInformation.getPicture_name().toString())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(im_profile_setting);
            tv_status_setting.setText(str);
            tv_name_setting.setText(MemberInformation.getFirstName() + " " + MemberInformation.getLastName());
            tv_email_setting.setText(MemberInformation.getEmail());

        } else if (MemberInformation.getType_account().equals("Nologin")) {
            im_profile_setting.setImageResource(R.mipmap.ic_launcher);

            str = "เข้าสู่ระบบด้วย : ผู้ใช้ทั่วไป";
            tv_status_setting.setText(str);
            tv_name_setting.setText("ผู้ใช้ทั่วไป");
            tv_email_setting.setText("");
        }
//#####################################################################################

        im_profile_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(getActivity(),ConfigProfileActivity.class);
               startActivity(intent);
            }
        });

        tv_name_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ConfigProfileActivity.class);
                startActivity(intent);
            }
        });
        tv_email_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ConfigProfileActivity.class);
                startActivity(intent);
            }
        });
        tv_status_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ConfigProfileActivity.class);
                startActivity(intent);
            }
        });
//#####################################################################################

        tv_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ConfigNotiActivity.class);
                startActivity(intent);
            }
        });

//#####################################################################################
        tv_acount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ConfigAccountActivity.class);
                startActivity(intent);
            }
        });


//#####################################################################################

        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getApplicationContext().stopService(new Intent(getContext(), LocationMonitorService.class));

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

                                                    Intent intent_RevokeAccess = new Intent(getActivity(), ChooseLoginActivity.class);
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

                    Intent intent_RevokeAccess = new Intent(getActivity(), ChooseLoginActivity.class);
                    startActivity(intent_RevokeAccess);

                } else if (MemberInformation.getType_account().equals("Nologin")) {
                    Intent intent_RevokeAccess = new Intent(getActivity(), ChooseLoginActivity.class);
                    startActivity(intent_RevokeAccess);
                }else{
                    Intent intent_RevokeAccess = new Intent(getActivity(), ChooseLoginActivity.class);
                    startActivity(intent_RevokeAccess);
                }
            }
        });
//#####################################################################################


        return myFragmentView;
    }

    //กดปุ่มย้อนกลับ
//################################################################################################################

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        backButtonHandler = (BackButtonHandlerInterface) activity;
        backButtonHandler.addBackClickListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        backButtonHandler.removeBackClickListener(this);
        backButtonHandler = null;
    }

    @Override
    public boolean onBackClick() {
        return false;
    }
    //################################################################################################################


    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient2.stopAutoManage(getActivity());
        mGoogleApiClient2.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


}
