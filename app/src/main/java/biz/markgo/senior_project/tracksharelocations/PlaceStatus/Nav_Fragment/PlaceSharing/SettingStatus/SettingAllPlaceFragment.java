package biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import biz.markgo.senior_project.tracksharelocations.API.URL_API;
import biz.markgo.senior_project.tracksharelocations.ChangePage.BackButtonHandlerInterface;
import biz.markgo.senior_project.tracksharelocations.ChangePage.OnBackClickListener;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.PlaceEditFragment;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.PlaceFragment;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.adpter.GetSettingPlace;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.adpter.getSettingPlaceAdpter;
import biz.markgo.senior_project.tracksharelocations.R;

public class SettingAllPlaceFragment extends Fragment implements OnBackClickListener {
    private View myFragmentView;
    private String TAG= SettingAllPlaceFragment.class.getSimpleName();
    private BackButtonHandlerInterface backButtonHandler;

    private String mLatitude,mLongitude;
    private String placename,sharing_status,name_add;
    private Double latitude,longitude,Radius;
    private Button bt_back_place,bt_settingNewStatus,bt_edit_placeAll;
    private TextView tv_name_placeAll,tv_addra;


    private JSONArray PlaceSetting_Information;
    ListView listView_settingplace;
    getSettingPlaceAdpter adpter;
    ArrayList<GetSettingPlace> getSettingPlacesList;
    GetSettingPlace  itemValue;

    String Token_key,Member_id,place_id,place_name;
    private ProgressDialog loading;

    private AlertDialog alertDialog;


    public SettingAllPlaceFragment(String place_id,String place_name,String name_add,String mLatitude,String mLongitude){
        this.place_id=place_id;
        this.place_name=place_name;
        this.name_add=name_add;
        this.mLatitude=mLatitude;
        this.mLongitude=mLongitude;
    }

    public SettingAllPlaceFragment(String place_id,String place_name,String name_add){
        this.place_id=place_id;
        this.place_name=place_name;
        this.name_add=name_add;
    }
    public SettingAllPlaceFragment(String place_id,String place_name){
        this.place_id=place_id;
        this.place_name=place_name;
    }
    public SettingAllPlaceFragment(String place_id){
        this.place_id=place_id;
    }

    public SettingAllPlaceFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_setting_all_place, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ตั้งค่าสถานที่");

        //bt_back_place =(Button) myFragmentView.findViewById(R.id.bt_back_place);
        bt_edit_placeAll = (Button) myFragmentView.findViewById(R.id.bt_edit_placeAll);
        bt_settingNewStatus = (Button) myFragmentView.findViewById(R.id.bt_settingNewStatus);
        tv_name_placeAll =(TextView) myFragmentView.findViewById(R.id.tv_name_placeAll);
        tv_addra = (TextView) myFragmentView.findViewById(R.id.tv_addra);

        listView_settingplace=(ListView) myFragmentView.findViewById(R.id.listView_settingplace);
        getSettingPlacesList = new ArrayList<GetSettingPlace>();

//########################################################################################
        tv_name_placeAll.setText(place_name);
        tv_addra.setText(name_add);
//########################################################################################
        loading = new ProgressDialog(getActivity());
        loading.setTitle("กำลังโหลดข้อมูล....");
        loading.setMessage("กรุณารอสักครู...");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
//########################################################################################
        Token_key = MemberInformation.getToken_key();
        Member_id = MemberInformation.getMember_id();

        getSettingPlace getPlace=new getSettingPlace();
        getPlace.execute(Token_key,Member_id,place_id);
//########################################################################################


        bt_edit_placeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PlaceEditFragment(place_id,place_name,name_add,mLatitude,mLongitude);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
//########################################################################################

        bt_settingNewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SttingNewStatusFragment(place_id,place_name,name_add,mLatitude,mLongitude);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

//########################################################################################

        listView_settingplace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewID = view.getId();
                itemValue  = (GetSettingPlace) parent.getItemAtPosition(position);

                if(viewID == R.id.CV_allplase){
                    seting_Button();


                }else if(viewID == R.id.bt_setting){
                    seting_Button();
                }
            }
        });
//########################################################################################

/*        bt_back_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PlaceFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });*/

        return  myFragmentView;
    }

    //########################################################################################

    public void seting_Button(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyAlertDialogStyle);

        builder.setTitle(itemValue.getName_status());
        //builder.setTitle( Html.fromHtml("<font color='#FF7F27'>"+itemValue.getPlace_name()+"</font>"));
        builder.setItems(new CharSequence[] {"แก้ไข", "ลบ"} , new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){

                    Fragment fragment = new SettingStatusFragment(name_add,itemValue.getPlace_id(),place_name,itemValue.getPlace_status_id(),itemValue.getName_status(),itemValue.getStatus_display(),
                            itemValue.getRadius(),itemValue.getDay(),itemValue.getStart_time(),itemValue.getEnd_time(),itemValue.getOnline(),itemValue.getReal_place(),
                            itemValue.getOffline(),itemValue.getOffline_mode(),itemValue.getDuration_time(),itemValue.getTo_time(),itemValue.getOut_place_online(),
                            itemValue.getSharing_status(),itemValue.getSecret_code(),mLatitude,mLongitude);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }else if(which == 1){

                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(getContext());
                    dialog2.setTitle("ลบการตั้งค่า ?");
                    dialog2.setMessage(itemValue.getName_status());
                    dialog2.setIcon(R.drawable.logo);

                    dialog2.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

                    dialog2.setPositiveButton("ตกลง" ,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DeleteSettingPlace deleteSettingPlace=new DeleteSettingPlace();
                            deleteSettingPlace.execute(Token_key,Member_id,itemValue.getPlace_status_id());
                        }
                    });
                    alertDialog = dialog2.create();
                    alertDialog.show();


                }

            }
        });

        builder.show();
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




    class getSettingPlace extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            String Token_key = params[0];
            String Member_id = params[1];
            String Place_id = params[2];
            String Api_Key = "getSettingplace1234";

            String data = "";
            int tmp;
            try {

                URL url = new URL(URL_API.getAPI_get_settingplace());
                String urlParams ="API_Key="+Api_Key
                        +"&Token_key="+Token_key
                        +"&Member_id="+Member_id
                        +"&Place_id="+Place_id;

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
                JSONObject getSettingPlace = root.getJSONObject("getSettingPlace_response");
                status = getSettingPlace.getString("status");

                if(status.equals("0")) {
                    PlaceSetting_Information = getSettingPlace.getJSONArray("PlaceSetting_Information");
                    Log.d(TAG,"llllllllllllllllllllllllllll"+PlaceSetting_Information.length());
                    for (int i=0;i<PlaceSetting_Information.length();++i){

                        GetSettingPlace SettingPlace=new GetSettingPlace();
                        JSONObject jRedobject= PlaceSetting_Information.getJSONObject(i);

                        SettingPlace.setPlace_status_id(jRedobject.getString("place_status_id"));
                        SettingPlace.setName_status(jRedobject.getString("name_status"));
                        SettingPlace.setPlace_id(jRedobject.getString("place_id"));
                        SettingPlace.setStatus_display(jRedobject.getString("status_display"));
                        SettingPlace.setRadius(jRedobject.getString("radius"));
                        SettingPlace.setDay(jRedobject.getString("day"));
                        SettingPlace.setStart_time(jRedobject.getString("start_time"));
                        SettingPlace.setEnd_time(jRedobject.getString("end_time"));
                        SettingPlace.setOnline(jRedobject.getString("online"));
                        SettingPlace.setReal_place(jRedobject.getString("real_place"));
                        SettingPlace.setOffline(jRedobject.getString("offline"));
                        SettingPlace.setOffline_mode(jRedobject.getString("offline_mode"));
                        SettingPlace.setDuration_time(jRedobject.getString("duration_time"));
                        SettingPlace.setTo_time(jRedobject.getString("to_time"));
                        SettingPlace.setOut_place_online(jRedobject.getString("out_place_online"));
                        SettingPlace.setSharing_status(jRedobject.getString("sharing_status"));
                        String secret_code = jRedobject.getString("secret_code");

                        if(secret_code.equals("null")){
                            SettingPlace.setSecret_code("null");
                        }else {
                            String secret_code_decoded = new String(Base64.decode(secret_code, Base64.DEFAULT));
                            SettingPlace.setSecret_code(secret_code_decoded);
                        }

                        getSettingPlacesList.add(SettingPlace);
                        adpter=new getSettingPlaceAdpter(getContext(),R.layout.row_allplace,getSettingPlacesList);
                        listView_settingplace.setAdapter(adpter);
                        loading.dismiss();


                    }
//                    s = "select Place";
//                    Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                }else if(status.equals("1")){
                    loading.dismiss();
                    s = "คุณยังไม่มีข้อมูลการตั้งค่า";
                    Toast toast = Toast.makeText(getContext(),s, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                }
                loading.dismiss();

            } catch (JSONException e) {
                loading.dismiss();
                e.printStackTrace();
                err = "Exception: " + e.getMessage();

            }

        }


    }

    class DeleteSettingPlace extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            String Token_key = params[0];
            String Member_id = params[1];
            String Place_status_id = params[2];
            String Api_Key = "DeleteSettingPlace1234";
            String data = "";
            int tmp;
            try {

                URL url = new URL(URL_API.getAPI_delete_statusplace());
                String urlParams = "API_Key=" + Api_Key
                        + "&Token_key=" + Token_key
                        + "&Member_id=" + Member_id
                        + "&Place_status_id=" + Place_status_id;

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
            String status = "";

            try {
                JSONObject root = new JSONObject(s);
                JSONObject getPlace = root.getJSONObject("DeleteStatusPlace_response");
                status = getPlace.getString("status");

                if (status.equals("0")) {

                    s = "ลบการตั้งค่าเรียบร้อย";
                    Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                    Fragment fragment = new SettingAllPlaceFragment(place_id,place_name,name_add,mLatitude,mLongitude);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else if (status.equals("1")) {

                    s = "ลบสถานที่ไม่สำเร็จ";
//                    Toast toast = Toast.makeText(getContext(),s, Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
//                    toast.show();

                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

                }


            } catch (JSONException e) {

                e.printStackTrace();
                err = "Exception: " + e.getMessage();

            }

        }
    }


}
