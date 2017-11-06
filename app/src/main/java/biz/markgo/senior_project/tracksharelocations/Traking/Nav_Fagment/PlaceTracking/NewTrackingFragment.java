package biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

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
import biz.markgo.senior_project.tracksharelocations.ChangePage.BackButtonHandlerInterface;
import biz.markgo.senior_project.tracksharelocations.ChangePage.OnBackClickListener;
import biz.markgo.senior_project.tracksharelocations.Config.Profile.QRCodeActivity;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.R;


public class NewTrackingFragment extends Fragment implements OnBackClickListener {


    private static final String TAG = NewTrackingFragment.class.getSimpleName();
    private View myFragmentView;
    private BackButtonHandlerInterface backButtonHandler;


    private EditText et_tracking_searching;
    private Button bt_tarcking_search;
    private AlertDialog alertDialog1;
    private JSONArray FindTracking_Information;

    private Button bt_scan_QR;
    private TextView result;
    public static  final  int REQUEST_CODE =100;

    private ProgressDialog loading;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_new_tracking, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("เพิ่มการติดตาม");

         et_tracking_searching = (EditText) myFragmentView.findViewById(R.id.et_tracking_searching);
         bt_tarcking_search=(Button) myFragmentView.findViewById(R.id.bt_tarcking_search);

        bt_scan_QR=(Button) myFragmentView.findViewById(R.id.bt_scan_QR);
        result = (TextView) myFragmentView.findViewById(R.id.result);



//########################################################################################

        TabHost mTabHost = (TabHost) myFragmentView.findViewById(R.id.tabhost);
        mTabHost.setup();
        //Lets add the first Tab
        TabHost.TabSpec mSpec = mTabHost.newTabSpec("Tab1");
        mSpec.setContent(R.id.tab1);
        mSpec.setIndicator("ค้นหาจาก ID");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("Tab2");
        mSpec.setContent(R.id.tab2);
        mSpec.setIndicator("ค้นหาจาก คิวอาร์โค้ด");
        mTabHost.addTab(mSpec);

//########################################################################################

        bt_tarcking_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_tracking_searching.getText().toString().equals("")){

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("กรุณากรอก ID ของบุคคลที่ต้องการติดตาม");
                    dialog.setIcon(R.drawable.logo);
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog1 = dialog.create();
                    alertDialog1.show();

                }else {
                    MemberInformation memberInformation = new MemberInformation();
                    String Token_key = memberInformation.getToken_key();
                    String  Member_id = memberInformation.getMember_id();

//########################################################################################
                    loading = new ProgressDialog(getActivity());
                    loading.setTitle("กำลังโหลดข้อมูล....");
                    loading.setMessage("กรุณารอสักครู...");
                    loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    loading.show();
//########################################################################################

                    FindFollowers findFollowers = new FindFollowers("ID");
                    findFollowers.execute(Token_key,Member_id,et_tracking_searching.getText().toString());
                }
            }
        });


        bt_scan_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 200);
                    }
                    return;
                }


                Intent intent = new Intent(getActivity(), ScanQRActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        return myFragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if(data != null){

                final Barcode barcode = data.getParcelableExtra("barcode");
                String id_bra= barcode.displayValue.toString();



//########################################################################################
                loading = new ProgressDialog(getActivity());
                loading.setTitle("กำลังโหลดข้อมูล....");
                loading.setMessage("กรุณารอสักครู...");
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loading.show();
//########################################################################################

                FindFollowers findFollowers = new FindFollowers("QR");
                findFollowers.execute(MemberInformation.getToken_key(),
                                        MemberInformation.getMember_id(),
                                        id_bra.trim());

//                result.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        result.setText(barcode.displayValue);
//
//                        SearchQRFragment.FindFollowers findFollowers = new SearchQRFragment.FindFollowers();
//                        findFollowers.execute(MemberInformation.getToken_key(),MemberInformation.getMember_id(),result.getText().toString());
//                    }
//                });
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200:
                Intent intent = new Intent(getActivity(), ScanQRActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            default:
                break;
        }
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



    class FindFollowers extends AsyncTask<String,String,String> {
        String Type_search ;
        public FindFollowers(String Type_search){
            this.Type_search=Type_search;
        }
        @Override
        protected String doInBackground(String... params) {
            String Token_key = params[0];
            String Member_id = params[1];
            String Follow_id = params[2];

            String Api_Key = "FindTracking1234";

            String data = "";
            int tmp;

            try {

                URL url = new URL(URL_API.getAPI_find_tracking());
                String urlParams ="API_Key="+Api_Key
                        +"&Token_key="+Token_key
                        +"&Member_id="+Member_id
                        +"&Follow_id="+Follow_id;

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
                JSONObject getPlace = root.getJSONObject("FindTracking_response");
                status = getPlace.getString("status");


                if(status.equals("0")) {
                    FindTracking_Information = getPlace.getJSONArray("FindTracking_Information");
                    Log.d(TAG,"llllllllllllllllllllllllllll"+FindTracking_Information.length());

                    JSONObject jRedobject= FindTracking_Information.getJSONObject(0);

                    if(Type_search.equals("ID")) {

                        Fragment fragment = new FindTracking("Tab1",jRedobject.getString("picture_name"),
                                jRedobject.getString("FirstName")+" "+jRedobject.getString("LastName"),
                                jRedobject.getString("member_id"));

                        loading.dismiss();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.new_tracking, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }else if(Type_search.equals("QR")){

                        Fragment fragment = new FindTracking("Tab2",jRedobject.getString("picture_name"),
                                jRedobject.getString("FirstName")+" "+jRedobject.getString("LastName"),
                                jRedobject.getString("member_id"));

                        loading.dismiss();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.search_qr, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }


                }else if(status.equals("1")){
                    loading.dismiss();

                    if(Type_search.equals("ID")) {

                        Fragment fragment = new NoFindTracking();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.new_tracking, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }else if(Type_search.equals("QR")){

                        Fragment fragment = new NoFindTracking();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.search_qr, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }


                }else{
                    loading.dismiss();

                    if(Type_search.equals("ID")) {

                        Fragment fragment = new NoFindTracking();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.new_tracking, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }else if(Type_search.equals("QR")){

                        Fragment fragment = new NoFindTracking();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.search_qr, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                }

            } catch (JSONException e) {
                loading.dismiss();

                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }

        }


    }

}
