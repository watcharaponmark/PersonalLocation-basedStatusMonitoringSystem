package biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import biz.markgo.senior_project.tracksharelocations.ChangePage.BackButtonHandlerInterface;
import biz.markgo.senior_project.tracksharelocations.ChangePage.OnBackClickListener;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.Login.DetailRegisterActivity;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking.adpter.GetPlaceTracking;
import biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking.adpter.getPlaceTrackingAdpter;

public class PlaceSecretFragment extends Fragment implements OnMapReadyCallback, OnBackClickListener{
    private View myFragmentView;
    private BackButtonHandlerInterface backButtonHandler;

    private static final String TAG = PlaceSecretFragment.class.getSimpleName();
    private JSONArray TrackingSecret_Information;

    private String member_id_tracking;
    private String secret_code;


    private ProgressDialog loading;
    String Token_key,Member_id;

    private GoogleMap mMap;
    private Circle mapCircle;
    private Marker mMarker;
    private final int REQUEST_CODE_PLACEPICKER = 1;


    private TextView tv_name_placeAll,tv_addra;
    private Button bt_traking;

    public PlaceSecretFragment(String member_id_tracking , String secret_code){
        this.member_id_tracking = member_id_tracking;
        this.secret_code = secret_code;
    }

    public PlaceSecretFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_place_secret, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("เพิ่มการติดตาม");

        tv_name_placeAll = (TextView)  myFragmentView.findViewById(R.id.tv_name_placeAll);
        tv_addra = (TextView)  myFragmentView.findViewById(R.id.tv_addra);
        bt_traking = (Button) myFragmentView.findViewById(R.id.bt_traking);



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

        getPlaceTrackingSecret getPlaceTracking = new getPlaceTrackingSecret();
        getPlaceTracking.execute(Token_key,Member_id,member_id_tracking,secret_code);


        final SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        myMAPF.getMapAsync(this);


        bt_traking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        return myFragmentView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        setUpMap();

    }

    public void setUpMap() {

        Log.e("nssssssssssssssssss", "setUpMap");

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);



    }

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

    class getPlaceTrackingSecret extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            String Token_key = params[0];
            String Member_id = params[1];
            String Member_id_tracking = params[2];
            String secret_code = params[3];
            String Api_Key = "getPlaceTrackingSecret1234";

            String data = "";
            int tmp;

            try {

                URL url = new URL(URL_API.getAPI_get_place_tracking_secret());
                String urlParams ="API_Key="+Api_Key
                        +"&Token_key="+Token_key
                        +"&Member_id="+Member_id
                        +"&Member_id_tracking="+Member_id_tracking
                        +"&secret_code="+secret_code;

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
                JSONObject getPlaceTrackingSecret = root.getJSONObject("getPlaceTrackingSecret_response");
                status = getPlaceTrackingSecret.getString("status");

                if(status.equals("0")) {

                    TrackingSecret_Information = getPlaceTrackingSecret.getJSONArray("TrackingSecret_Information");
                    Log.d(TAG, "length : " + TrackingSecret_Information.length());

                    JSONObject jRedobject = TrackingSecret_Information.getJSONObject(0);
                    //GetPlaceTracking place =new GetPlaceTracking();

//                    place.setMember_id(jRedobject.getString("member_id"));
//                    place.setPlace_id(jRedobject.getString("place_id"));
//                    place.setPlace_name(jRedobject.getString("place_name"));
//                    place.setSub_district(jRedobject.getString("sub_district"));
//                    place.setDistrict(jRedobject.getString("district"));
//                    place.setProvince(jRedobject.getString("province"));
//                    place.setZip_code(jRedobject.getString("zip_code"));
//                    place.setCountry_code(jRedobject.getString("country_code"));
//                    place.setLatitude(jRedobject.getString("latitude"));
//                    place.setLongitude(jRedobject.getString("longitude"));
//                    place.setSharing_status(jRedobject.getString("sharing_status"));
//                    place.setPlace_status_id(jRedobject.getString("place_status_id"));

                    LatLng coordinate = new LatLng(Double.parseDouble(jRedobject.getString("latitude")), Double.parseDouble(jRedobject.getString("longitude")));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16));

                    if (mMarker != null) {
                        mMarker.remove();
                    }
                    if (mapCircle != null) {
                        mapCircle.remove();
                    }


                    MarkerOptions options = new MarkerOptions()
                            .title("ตำแน่งปัจจุบันของ : ")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .position(new LatLng(Double.parseDouble(jRedobject.getString("latitude")), Double.parseDouble(jRedobject.getString("longitude"))))
                            .snippet(jRedobject.getString("place_name"));

                    mMarker = mMap.addMarker(options);

                    mapCircle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(Double.parseDouble(jRedobject.getString("latitude")), Double.parseDouble(jRedobject.getString("longitude"))))
                            .radius(50)
                            .strokeColor(Color.RED)
                            .strokeWidth(5)
                            .fillColor(Color.GREEN));

                    tv_name_placeAll.setText(jRedobject.getString("place_name"));
                    tv_addra.setText(jRedobject.getString("sub_district"));


//                    s="บันทึกข้อมูลสำเร็จ";
//                    Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();

                    loading.dismiss();

                }else if(status.equals("1")){
                    loading.dismiss();
                    s = "ไม่พบข้อมูลการแบ่งปันสถานที่ของผู้ใช้";
                    //    Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();

                    Toast toast = Toast.makeText(getContext(),s, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                }else{

                    Toast toast = Toast.makeText(getContext(),s, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    loading.dismiss();
                }

            } catch (JSONException e) {
                loading.dismiss();
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }

        }

    }

}
