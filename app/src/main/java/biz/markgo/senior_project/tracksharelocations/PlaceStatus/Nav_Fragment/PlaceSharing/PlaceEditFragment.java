package biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import biz.markgo.senior_project.tracksharelocations.API.URL_API;
import biz.markgo.senior_project.tracksharelocations.ChangePage.BackButtonHandlerInterface;
import biz.markgo.senior_project.tracksharelocations.ChangePage.OnBackClickListener;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus.SettingAllPlaceFragment;
import biz.markgo.senior_project.tracksharelocations.R;

public class PlaceEditFragment extends Fragment implements OnMapReadyCallback,OnBackClickListener {

    private static final String TAG = PlaceEditFragment.class.getSimpleName();
    private BackButtonHandlerInterface backButtonHandler;
    private View myFragmentView;

    private AlertDialog alertDialog1;

    private String place_id,place_name,name_add;
    private String mLatitude,mLongitude;


    private Button bt_re, bt_EditPlace, bt_cancel,bt_search_add;
    private TextView tv_address;
    private EditText et_PlaceName;

    private Spinner spinner_radius;

    private GoogleMap mMap;
    private Circle mapCircle;
    private Marker mMarker;

    private double lat, lng;

    private final int REQUEST_CODE_PLACEPICKER = 1;

    private LocationManager locationManager;
    private LocationListener listener;

    private String placename;

    int position_spinner_Radius;

    public PlaceEditFragment(String place_id,String place_name,String name_add,String mLatitude,String mLongitude) {
        this.place_id=place_id;
        this.place_name=place_name;
        this.name_add=name_add;
        this.mLatitude=mLatitude;
        this.mLongitude=mLongitude;
    }
    public PlaceEditFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_place_edit, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ตั้งค่าสถานที่");

//################################################################################################################

        tv_address = (TextView) myFragmentView.findViewById(R.id.tv_address);
        et_PlaceName = (EditText) myFragmentView.findViewById(R.id.et_PlaceName);
        bt_re = (Button) myFragmentView.findViewById(R.id.bt_re);
        bt_cancel = (Button) myFragmentView.findViewById(R.id.bt_cancel_fnp);
        bt_EditPlace = (Button) myFragmentView.findViewById(R.id.bt_EditPlace_fnp);
        bt_search_add =(Button) myFragmentView.findViewById(R.id.bt_search_add);

//################################################################################################################

        et_PlaceName.setText(place_name);
        tv_address.setText(name_add);


//################################################################################################################

//        spinner_radius = (Spinner) myFragmentView.findViewById(R.id.spinner_radius);
//        final String[] option_radius = {"50 เมตร", "80 เมตร", "120 เมตร", "150 เมตร"};
//        int flags[] = {R.drawable.cir001, R.drawable.cir001, R.drawable.cir001, R.drawable.cir001};
//
//        CustomAdapter_spinner customAdapter = new CustomAdapter_spinner(getContext(), flags, option_radius);
//        spinner_radius.setAdapter(customAdapter);


        lat=Double.parseDouble(mLatitude);
        lng=Double.parseDouble(mLongitude);

//################################################################################################################

//        if(mRadius.equals("50.0")){
//            position_spinner_Radius = 0;
//        }else if(mRadius.equals("80.0")) {
//            position_spinner_Radius = 1;
//        }else if(mRadius.equals("120.0")) {
//            position_spinner_Radius = 2;
//        }else if(mRadius.equals("150.0")) {
//            position_spinner_Radius = 3;
//        }
 //       spinner_radius.setSelection(position_spinner_Radius);

//################################################################################################################

//################################################################################################################
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

//################################################################################################################

//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

//        mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
//        setUpMap();


        final  SupportMapFragment myMAPF = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        myMAPF.getMapAsync(this);

//################################################################################################################
//        LatLng coordinate = new LatLng(lat,lng);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16));
//
//        MarkerOptions options = new MarkerOptions()
//                .title("ตำแน่งปัจจุบันของ : ")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//                .position(new LatLng(lat,lng))
//                .snippet(MemberInformation.getFirstName());
//
//        mMarker = mMap.addMarker(options);
//
//        mapCircle = mMap.addCircle(new CircleOptions()
//                .center(new LatLng(lat,lng))
//                .radius(Double.parseDouble(mRadius))
//                .strokeColor(Color.RED)
//                .strokeWidth(5)
//                .fillColor(Color.GREEN));

//################################################################################################################
//        spinner_radius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //Object item = parent.getItemAtPosition(position);
//
//                if (option_radius[position].equals("50 เมตร")) {
////                    Toast.makeText(getActivity(), "Selected"+option_radius[position],
////                            Toast.LENGTH_SHORT).show();
//                    if (mapCircle != null) {
//                        mapCircle.remove();
//                    }
//                    mapCircle = mMap.addCircle(new CircleOptions()
//                            .center(new LatLng(lat, lng))
//                            .radius(50)
//                            .strokeColor(Color.RED)
//                            .strokeWidth(5)
//                            .fillColor(Color.GREEN));
//                } else if (option_radius[position].equals("80 เมตร")) {
////                    Toast.makeText(getActivity(), "Selected"+option_radius[position],
////                            Toast.LENGTH_SHORT).show();
//                    if (mapCircle != null) {
//                        mapCircle.remove();
//                    }
//                    mapCircle = mMap.addCircle(new CircleOptions()
//                            .center(new LatLng(lat, lng))
//                            .radius(80)
//                            .strokeColor(Color.RED)
//                            .strokeWidth(5)
//                            .fillColor(Color.GREEN));
//                } else if (option_radius[position].equals("120 เมตร")) {
////                    Toast.makeText(getActivity(), "Selected"+option_radius[position],
////                            Toast.LENGTH_SHORT).show();
//                    if (mapCircle != null) {
//                        mapCircle.remove();
//                    }
//                    mapCircle = mMap.addCircle(new CircleOptions()
//                            .center(new LatLng(lat, lng))
//                            .radius(120)
//                            .strokeColor(Color.RED)
//                            .strokeWidth(5)
//                            .fillColor(Color.GREEN));
//                } else if (option_radius[position].equals("150 เมตร")) {
////                    Toast.makeText(getActivity(), "Selected"+option_radius[position],
////                            Toast.LENGTH_SHORT).show();
//                    if (mapCircle != null) {
//                        mapCircle.remove();
//                    }
//                    mapCircle = mMap.addCircle(new CircleOptions()
//                            .center(new LatLng(lat, lng))
//                            .radius(150)
//                            .strokeColor(Color.RED)
//                            .strokeWidth(5)
//                            .fillColor(Color.GREEN));
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//################################################################################################################
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            public void onMapClick(LatLng arg0) {
//
//                if (mMarker != null) {
//                    mMarker.remove();
//                }
//                if (mapCircle != null) {
//                    mapCircle.remove();
//                }
//                mMap.addMarker(new MarkerOptions().position(arg0)
//                        .title(String.valueOf(arg0.latitude)
//                                + ", " + String.valueOf(arg0.longitude)));
//                mapCircle = mMap.addCircle(new CircleOptions()
//                        .center(arg0)
//                        .radius(50)
//                        .strokeColor(Color.RED)
//                        .strokeWidth(5)
//                        .fillColor(Color.GREEN));
//
//            }
//
//        });
//################################################################################################################
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            public boolean onMarkerClick(Marker arg0) {
//                arg0.remove();
//                mapCircle.remove();
//                Toast.makeText(getActivity().getApplicationContext()
//                        , "Remove Marker " + String.valueOf(arg0.getId())
//                        , Toast.LENGTH_SHORT).show();
//                return true;
//            }
//
//
//        });
//################################################################################################################
        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), AddressSearchActivity.class));

                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                // this would only work if you have your Google Places API working
                try {
                    Intent intent = intentBuilder.build(getActivity());
                    startActivityForResult(intent, REQUEST_CODE_PLACEPICKER);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
//################################################################################################################

        bt_search_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(getActivity());
                    startActivityForResult(intent, REQUEST_CODE_PLACEPICKER);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//################################################################################################################
        //ยกเลิกเพิมสถานที
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SettingAllPlaceFragment(place_id,place_name,getSub_district(lat, lng),mLatitude,mLongitude);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
//################################################################################################################

        bt_EditPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placename = et_PlaceName.getText().toString();
                mLatitude =Double.toString(lat);
                mLongitude =Double.toString(lng);
               // mRadius =Double.toString(mapCircle.getRadius());


                Log.e(TAG,placename+"\n"+mLatitude+"\n"+mLongitude+"\n"+"\n"+place_id);

                if (placename.equals("")) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("กรุณากรอกชื่อสถานที่");
                    dialog.setIcon(R.drawable.logo);
                    dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog1 = dialog.create();
                    alertDialog1.show();

                }else{

                    EditPlace editPlace = new EditPlace();
                    editPlace.execute(placename,
                            getSub_district(lat, lng),
                            getDistrict(lat, lng),
                            getProvince(lat, lng),
                            getZip_code(lat, lng),
                            getCountry_code(lat, lng),
                            Double.toString(lat),
                            Double.toString(lng),
                            place_id

                    );
                }
            }
        });

//################################################################################################################

       return myFragmentView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        this.mMap = googleMap;
        setUpMap();

        //mMap.setOnMapClickListener(this);
    }

    public void setUpMap() {

//################################################################################################################
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

//################################################################################################################

        LatLng coordinate = new LatLng(lat,lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16));

        MarkerOptions options = new MarkerOptions()
                .title("ตำแน่งปัจจุบันของ : ")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(new LatLng(lat,lng))
                .snippet(MemberInformation.getFirstName());

        mMarker = mMap.addMarker(options);

        mapCircle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(lat,lng))
                .radius(50)
                .strokeColor(Color.RED)
                .strokeWidth(5)
                .fillColor(Color.GREEN));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(getActivity());
                    startActivityForResult(intent, REQUEST_CODE_PLACEPICKER);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//################################################################################################################

    }

//    @Override
//    public void onMapClick(LatLng latLng) {
//
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PLACEPICKER && resultCode == Activity.RESULT_OK) {
            displaySelectedPlaceFromPlacePicker(data);
        }
    }

    //################################################################################################################

    private void displaySelectedPlaceFromPlacePicker(Intent data) {
        Place placeSelected = PlacePicker.getPlace(data, getActivity());
        LatLng latLng = placeSelected.getLatLng();
        lat = latLng.latitude;
        lng = latLng.longitude;

        Log.e(TAG,lat+"\n"+lng);

        String name = placeSelected.getName().toString();
        String address = placeSelected.getAddress().toString();

        et_PlaceName.setText(name);
        tv_address.setText(address);
        LatLng coordinate1 = new LatLng(lat , lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate1, 16));

        if (mMarker != null) {
            mMarker.remove();
        }
        if (mapCircle != null) {
            mapCircle.remove();
        }

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 10);
            }
            return;
        }

        MarkerOptions options = new MarkerOptions()
                .title("ตำแน่งปัจจุบันของ : ")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(new LatLng(lat, lng))
                .snippet(MemberInformation.getFirstName());

        mMarker = mMap.addMarker(options);

//        mapCircle = mMap.addCircle(new CircleOptions()
//                .center(new LatLng(lat, lng))
//                .radius(Double.parseDouble(mRadius))
//                .strokeColor(Color.RED)
//                .strokeWidth(5)
//                .fillColor(Color.GREEN));

    }

    //################################################################################################################
    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                android.location.Address address = addresses.get(0);
                //ชื่อซอย หรือชื่อตำบล
                result.append(address.getAddressLine(0));
                //อำเภอ
                // result.append(address.getLocality());
                //จังหวัด
                //result.append(address.getAdminArea()).append(",");
                //ชื่อประเทศ
                //result.append(address.getCountryName()).append(",");
                //รหัสประเทศ
                //result.append(address.getCountryCode()).append(",");
                //รหัสไปรษณีย์
                //result.append(address.getPostalCode()).append(",");
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    private String getSub_district(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                android.location.Address address = addresses.get(0);
                //ชื่อซอย หรือชื่อตำบล
                result.append(address.getAddressLine(0));
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    private String getDistrict(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                android.location.Address address = addresses.get(0);
                //อำเภอ
                result.append(address.getLocality());

            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    private String getProvince(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                android.location.Address address = addresses.get(0);
                //จังหวัด
                result.append(address.getAdminArea());

            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    private String getZip_code(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                android.location.Address address = addresses.get(0);
                //รหัสไปรษณีย์
                result.append(address.getPostalCode());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    private String getCountry_code(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                android.location.Address address = addresses.get(0);
                //รหัสประเทศ
                result.append(address.getCountryCode());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
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


    //################################################################################################################

    class EditPlace extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String placename = params[0];
            String getSub_district = params[1];
            String getDistrict = params[2];
            String getProvince = params[3];
            String getZip_code = params[4];
            String getCountry_code = params[5];
            String latitude = params[6];
            String longitude = params[7];
            String Place_id = params[8];


            String Api_Key = "UpdatePlace1234";
            MemberInformation memberInformation = new MemberInformation();
            String Token_key = memberInformation.getToken_key();
            String Member_id = memberInformation.getMember_id();

            String data = "";
            int tmp;

            try {

                URL url = new URL(URL_API.getAPI_update_place());
                String urlParams = "API_Key=" + Api_Key
                        + "&Token_key=" + Token_key
                        + "&Member_id=" + Member_id
                        + "&place_name=" + placename
                        + "&sub_district=" + getSub_district
                        + "&district=" + getDistrict
                        + "&province=" + getProvince
                        + "&zip_code=" + getZip_code
                        + "&country_code=" + getCountry_code
                        + "&latitude=" + latitude
                        + "&longitude=" + longitude
                        + "&place_id=" + Place_id;

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
                JSONObject response = root.getJSONObject("UpdatePlace_response");
                status = response.getString("status");
                if (status.equals("0")) {

                        Fragment fragment = new SettingAllPlaceFragment(place_id,et_PlaceName.getText().toString(),tv_address.getText().toString(),Double.toString(lat),Double.toString(lng));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();


                    s = "แก้ข้อมูลสถานที่เรียบร้อย";
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }

        }
    }

}
