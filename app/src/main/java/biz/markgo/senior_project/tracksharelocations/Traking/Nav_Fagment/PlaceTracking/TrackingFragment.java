package biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.ArrayList;

import biz.markgo.senior_project.tracksharelocations.API.URL_API;
import biz.markgo.senior_project.tracksharelocations.ChangePage.BackButtonHandlerInterface;
import biz.markgo.senior_project.tracksharelocations.ChangePage.OnBackClickListener;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;

import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.PlaceFragment;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus.SettingAllPlaceFragment;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.adpter.GetPlace;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking.adpter.GetPlaceTrackingAll;
import biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking.adpter.getPlaceTrakingAllAdpter;


public class TrackingFragment extends Fragment implements OnBackClickListener {
    private View myFragmentView;
    private Button bt_newtracking,bt_back;
    private BackButtonHandlerInterface backButtonHandler;

    private String TAG= TrackingFragment.class.getSimpleName();

    String Token_key,Member_id;
    private AlertDialog alertDialog1;
    private ProgressDialog loading;

    ListView listView_tracking;
    getPlaceTrakingAllAdpter adpter;
    ArrayList<GetPlaceTrackingAll> getplaceTrakingList;

    GetPlaceTrackingAll  itemValue;
    private JSONArray Place_Information;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_tracking, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("โหมดติดตามบุคคล");


        bt_newtracking=(Button) myFragmentView.findViewById(R.id.bt_newtracking);
        //bt_back=(Button)  myFragmentView.findViewById(R.id.bt_back_tracking);

        listView_tracking=(ListView) myFragmentView.findViewById(R.id.listView_tracking);
        getplaceTrakingList=new ArrayList<GetPlaceTrackingAll>();


        MemberInformation memberInformation = new MemberInformation();
        Token_key = memberInformation.getToken_key();
        Member_id = memberInformation.getMember_id();
        //loading = ProgressDialog.show(getContext(),"loading...","Please wait...",false,false);
        // loading = ProgressDialog.show(getContext(), "", "Loading. Please wait...", true);
//########################################################################################
        loading = new ProgressDialog(getActivity());
        loading.setTitle("กำลังโหลดข้อมูล....");
        loading.setMessage("กรุณารอสักครู...");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
//########################################################################################

        getPlaceTraking getPlaceTrak=new getPlaceTraking();
        getPlaceTrak.execute(Token_key,Member_id);
//########################################################################################

        bt_newtracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new NewTrackingFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.traking_for_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        listView_tracking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewID = view.getId();
                itemValue  = (GetPlaceTrackingAll) parent.getItemAtPosition(position);

               if(viewID == R.id.bt_delete){
                    //Toast.makeText(getActivity(), "Click DeledtButton :"+position+"  Item : " +itemValue.getPlace_id() , Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("ยกเลิกการติดตาม ?");
                    dialog.setMessage(itemValue.getPlace_name());
                    dialog.setIcon(R.drawable.logo);

                    dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

                    dialog.setPositiveButton("ตกลง" ,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DeleteTrakingPlace deleteTrakingPlace=new DeleteTrakingPlace();
                            deleteTrakingPlace.execute(Token_key,Member_id,itemValue.getPlace_tracking_id());

                        }
                    });
                    alertDialog1 = dialog.create();
                    alertDialog1.show();
                }


            }
        });

//        bt_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // getActivity().finish();
//                Fragment fragment = new PlaceStatusFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.traking_for_fragment, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });




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


    class getPlaceTraking extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            String Token_key = params[0];
            String Member_id = params[1];
            String Api_Key = "getplace1234";


            String data = "";
            int tmp;
            try {

                URL url = new URL(URL_API.getAPI_get_place_tracking_All()
                );
                String urlParams = "API_Key=" + Api_Key
                        + "&Token_key=" + Token_key
                        + "&Member_id=" + Member_id;

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
                JSONObject getPlace = root.getJSONObject("getPlaceTracking_response");
                status = getPlace.getString("status");

                if (status.equals("0")) {
                    Place_Information = getPlace.getJSONArray("PlaceTracking_Information");
                    Log.d(TAG, "Trakingggggggg" + Place_Information.length());
                    for (int i = 0; i < Place_Information.length(); ++i) {

                        GetPlaceTrackingAll getPlaceTrackingAll = new GetPlaceTrackingAll();
                        JSONObject jRedobject = Place_Information.getJSONObject(i);

                        getPlaceTrackingAll.setMember_id(jRedobject.getString("member_id"));
                        getPlaceTrackingAll.setPlace_id(jRedobject.getString("place_id"));
                        getPlaceTrackingAll.setPlace_name(jRedobject.getString("place_name"));
                        getPlaceTrackingAll.setSub_district(jRedobject.getString("sub_district"));
                        getPlaceTrackingAll.setDistrict(jRedobject.getString("district"));
                        getPlaceTrackingAll.setProvince(jRedobject.getString("province"));
                        getPlaceTrackingAll.setZip_code(jRedobject.getString("zip_code"));
                        getPlaceTrackingAll.setCountry_code(jRedobject.getString("country_code"));
                        getPlaceTrackingAll.setLatitude(jRedobject.getString("latitude"));
                        getPlaceTrackingAll.setLongitude(jRedobject.getString("longitude"));
//                        getPlaceTrackingAll.setRadius(jRedobject.getString("radius"));
                        getPlaceTrackingAll.setSharing_status(jRedobject.getString("sharing_status"));
                        getPlaceTrackingAll.setPlace_tracking_id(jRedobject.getString("place_tracking_id"));
                        getPlaceTrackingAll.setTracking_status(jRedobject.getString("tracking_status"));

                        getplaceTrakingList.add(getPlaceTrackingAll);
                        adpter = new getPlaceTrakingAllAdpter(getContext(), R.layout.row_trackingall, getplaceTrakingList);
                        listView_tracking.setAdapter(adpter);
                        loading.dismiss();


                    }
//                    s = "select Place";
//                    Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                } else if (status.equals("1")) {
                    loading.dismiss();
                    s = "คุณยังไม่มีข้อมูลการติดตาม";
                    Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
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


    class DeleteTrakingPlace extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            String Token_key = params[0];
            String Member_id = params[1];
            String place_tracking_id =  params[2];
            String Api_Key = "DeleteTrakingPlace1234";
            String data = "";
            int tmp;
            try {

                URL url = new URL(URL_API.getAPI_delete_tracking_place());
                String urlParams ="API_Key="+Api_Key
                        +"&Token_key="+Token_key
                        +"&Member_id="+Member_id
                        +"&place_tracking_id="+place_tracking_id;

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
                JSONObject getPlace = root.getJSONObject("DeletePlaceTracking_response");
                status = getPlace.getString("status");

                if(status.equals("0")) {

                    s = "ลบการติดตามเรียบร้อย";
                    Toast toast = Toast.makeText(getContext(),s, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                    Fragment fragment = new TrackingFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.traking_for_fragment, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }else if(status.equals("1")){

                    s = "ลบการติดตามไม่สำเร็จ";
//                    Toast toast = Toast.makeText(getContext(),s, Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
//                    toast.show();

                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

                }
                //Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {

                e.printStackTrace();
                err = "Exception: " + e.getMessage();

            }

        }


    }


}
