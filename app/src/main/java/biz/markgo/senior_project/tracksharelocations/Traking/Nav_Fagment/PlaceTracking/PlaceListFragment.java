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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking.adpter.GetPlaceTracking;
import biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking.adpter.getPlaceTrackingAdpter;
import biz.markgo.senior_project.tracksharelocations.R;

public class PlaceListFragment extends Fragment  implements OnBackClickListener {

    private String TAG= PlaceListFragment.class.getSimpleName();
    private View myFragmentView;
    private BackButtonHandlerInterface backButtonHandler;

    private String member_id_tracking;
    ListView listView_place_tracking;
    getPlaceTrackingAdpter adpter;
    ArrayList<GetPlaceTracking> getplacetrackingList;
    private JSONArray Place_Information;
    GetPlaceTracking  itemValue;
    private ProgressDialog loading;
    AlertDialog alertDialog1;

    String Token_key,Member_id;

    public PlaceListFragment() {

    }

    public PlaceListFragment(String member_id_tracking) {
        this.member_id_tracking = member_id_tracking;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_place_list, container, false);
        listView_place_tracking=(ListView) myFragmentView.findViewById(R.id.listView_place_tracking);

        MemberInformation memberInformation = new MemberInformation();
        Token_key = memberInformation.getToken_key();
        Member_id = memberInformation.getMember_id();


//########################################################################################
        loading = new ProgressDialog(getActivity());
        loading.setTitle("กำลังโหลดข้อมูล....");
        loading.setMessage("กรุณารอสักครู...");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
//########################################################################################


        getPlaceTracking getPlaceTracking=new getPlaceTracking();
        getPlaceTracking.execute(Token_key,Member_id,member_id_tracking);

        listView_place_tracking =(ListView) myFragmentView.findViewById(R.id.listView_place_tracking);
        getplacetrackingList = new ArrayList<GetPlaceTracking>();


//########################################################################################
        listView_place_tracking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final long viewID = view.getId();
                itemValue  = (GetPlaceTracking) parent.getItemAtPosition(position);

                if(viewID == R.id.bt_traking) {

                    if(itemValue.getSharing_status().equals("Public")) {

                        //Toast.makeText(getActivity(), "Click Trking Public:" + position + "  Item : " + itemValue.getPlace_status_id(), Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setTitle("ติดตามสถานที่ที่ ?");
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

                                TrackingPlace trackingPlace = new TrackingPlace();
                                trackingPlace.execute(Token_key, Member_id, itemValue.getPlace_status_id(),"allow");

                            }
                        });
                        alertDialog1 = dialog.create();
                        alertDialog1.show();

                    }else if(itemValue.getSharing_status().equals("Private")){

                        //Toast.makeText(getActivity(), "Click Trking Private:" + position + "  Item : " + itemValue.getPlace_status_id(), Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setTitle("ร้องขอติตามสถานที่ที่ ?");
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

                                TrackingPlace trackingPlace = new TrackingPlace();
                                trackingPlace.execute(Token_key, Member_id, itemValue.getPlace_status_id(),"Disallow");


                            }
                        });
                        alertDialog1 = dialog.create();
                        alertDialog1.show();

                    }

                }
            }
        });

//########################################################################################


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


    class getPlaceTracking extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            String Token_key = params[0];
            String Member_id = params[1];
            String Member_id_tracking = params[2];
            String Api_Key = "getplace1234";

            String data = "";
            int tmp;

            try {

                URL url = new URL(URL_API.getAPI_get_place_tracking());
                String urlParams ="API_Key="+Api_Key
                        +"&Token_key="+Token_key
                        +"&Member_id="+Member_id
                        +"&Member_id_tracking="+Member_id_tracking;

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
                JSONObject getPlace = root.getJSONObject("getPlaceTracking_response");
                status = getPlace.getString("status");

                if(status.equals("0")) {
                    Place_Information = getPlace.getJSONArray("Place_Information");
                    Log.d(TAG,"llllllllllllllllllllllllllll"+Place_Information.length());
                    for (int i=0;i<Place_Information.length();++i){

                        GetPlaceTracking place =new GetPlaceTracking();
                        JSONObject jRedobject= Place_Information.getJSONObject(i);

                        place.setMember_id(jRedobject.getString("member_id"));
                        place.setPlace_id(jRedobject.getString("place_id"));
                        place.setPlace_name(jRedobject.getString("place_name"));
                        place.setSub_district(jRedobject.getString("sub_district"));
                        place.setDistrict(jRedobject.getString("district"));
                        place.setProvince(jRedobject.getString("province"));
                        place.setZip_code(jRedobject.getString("zip_code"));
                        place.setCountry_code(jRedobject.getString("country_code"));
                        place.setLatitude(jRedobject.getString("latitude"));
                        place.setLongitude(jRedobject.getString("longitude"));
//                        place.setRadius(jRedobject.getString("radius"));
                        place.setSharing_status(jRedobject.getString("sharing_status"));
                        place.setPlace_status_id(jRedobject.getString("place_status_id"));

                        getplacetrackingList.add(place);
                        adpter=new getPlaceTrackingAdpter(getContext(),R.layout.row_place_tracking,getplacetrackingList);
                        listView_place_tracking.setAdapter(adpter);
                        loading.dismiss();

                    }
//                    s = "select Place";
//                    Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                }else if(status.equals("1")){
                    loading.dismiss();
                    s = "ยังไม่มีข้อมูลการแบ่งปันสถานที่ของผู้ใช้";
                //    Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();

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


    class TrackingPlace extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            String Token_key = params[0];
            String Member_id = params[1];
            String place_status_id =  params[2];
            String tracking_status =  params[3];
            String Api_Key = "TrackingPlace1234";
            String data = "";
            int tmp;
            try {

                URL url = new URL(URL_API.getAPI_tracking_place());
                String urlParams ="API_Key="+Api_Key
                        +"&Token_key="+Token_key
                        +"&Member_id="+Member_id
                        +"&place_status_id="+place_status_id
                        +"&tracking_status="+tracking_status;

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
                JSONObject getPlace = root.getJSONObject("TrackingPlace_response");
                status = getPlace.getString("status");

                if(status.equals("0")) {

                    getplacetrackingList.clear();
                    getPlaceTracking getPlaceTracking=new getPlaceTracking();
                    getPlaceTracking.execute(Token_key,Member_id,member_id_tracking);

                    s = "เพิ่มการติดตามเรียบร้อย";
                    Toast toast = Toast.makeText(getContext(),s, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

//                    Fragment fragment = new PlaceFragment();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();

                }else if(status.equals("1")){

                    s = "เพิ่มการติดตามี่ไม่สำเร็จ";


                    Toast toast = Toast.makeText(getContext(),s, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();


                }else if(status.equals("7")){

                    getplacetrackingList.clear();
                    getPlaceTracking getPlaceTracking=new getPlaceTracking();
                    getPlaceTracking.execute(Token_key,Member_id,member_id_tracking);

                    s = "เคยร้องขอหรือติดตามแล้ว";
                    Toast toast = Toast.makeText(getContext(),s, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }


            } catch (JSONException e) {

                e.printStackTrace();
                err = "Exception: " + e.getMessage();

            }

        }


    }



}
