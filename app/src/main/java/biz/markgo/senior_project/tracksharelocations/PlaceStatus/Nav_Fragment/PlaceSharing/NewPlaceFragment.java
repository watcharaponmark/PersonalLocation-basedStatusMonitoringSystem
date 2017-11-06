package biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.Toolbar;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
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
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus.SttingNewStatusFragment;
import biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking.NewTrackingFragment;
import biz.markgo.senior_project.tracksharelocations.R;


public class NewPlaceFragment extends Fragment implements OnMapReadyCallback, OnBackClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = NewTrackingFragment.class.getSimpleName();

    private BackButtonHandlerInterface backButtonHandler;

    private FragmentActivity myContext;
    private Button bt_re, bt_addplace, bt_cancel, bt_search_add;
    private TextView tv_address;
    private Toolbar myToolbar, toolbar;
    private LocationManager locationManager;
    private LocationListener listener;
    private Spinner spinner_radius;
    private GoogleMap mMap;
    private Circle mapCircle;
    private Marker mMarker;
    private double lat, lng;
    private EditText et_PlaceName;
    private String placename;
    private AlertDialog alertDialog1;
    private CharSequence[] values = {"สาธารณะ", "ส่วนตัว"};
    String Namesnippet = "";
    String latitude, longitude, /*Radius, */
            Account_id, StatusLogin, sharing_status;
    private final int REQUEST_CODE_PLACEPICKER = 1;
    private View myFragmentView;



    private int chacksettingplace = 0;


    private String place_id;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        myFragmentView = inflater.inflate(R.layout.fragment_new_place, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("เพิ่มสถานที่");

        tv_address = (TextView) myFragmentView.findViewById(R.id.tv_address);
        et_PlaceName = (EditText) myFragmentView.findViewById(R.id.et_PlaceName);
        bt_re = (Button) myFragmentView.findViewById(R.id.bt_re);
        bt_cancel = (Button) myFragmentView.findViewById(R.id.bt_cancel_fnp);
        bt_addplace = (Button) myFragmentView.findViewById(R.id.bt_addplace_fnp);
        bt_search_add = (Button) myFragmentView.findViewById(R.id.bt_search_add);


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
                lat = location.getLatitude();
                lng = location.getLongitude();

                Log.e(TAG, "test " + Double.toString(lat));
                Log.e(TAG, "test " + Double.toString(lng));
                Log.e(TAG, "test " + getDistrict(lat, lng));
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
                        .position(new LatLng(lat, lng))
                        .snippet(Namesnippet);

                mMarker = mMap.addMarker(options);

                mapCircle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(lat, lng))
                        .radius(50)
                        .strokeColor(Color.RED)
                        .strokeWidth(5)
                        .fillColor(Color.GREEN));


                tv_address.setText(getAddress(lat, lng));
                Log.e(TAG,"xxx"+getAddress(lat,lng));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }

        };

//################################################################################################################

//        mMap = ((SupportMapFragment) myContext.getSupportFragmentManager()
//        .findFragmentById(R.id.map)).getMap();

//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);


//        mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
//        setUpMap();


        final SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        myMAPF.getMapAsync(this);

        config();



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
        bt_re.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 10);
                    }
                    return;
                }

                boolean isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (isNetwork) {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER
                            , 5000, 0, listener);
                    Location loc = locationManager.getLastKnownLocation(
                            LocationManager.NETWORK_PROVIDER);
                    if (loc != null) {
                        lat = loc.getLatitude();
                        lng = loc.getLongitude();
                    }
                }

                if (isGPS) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                            , 5000, 0, listener);

                    Location loc = locationManager.getLastKnownLocation(
                            LocationManager.GPS_PROVIDER);

                    if (loc != null) {
                        lat = loc.getLatitude();
                        lng = loc.getLongitude();
                    }
                }
                //  locationManager.requestLocationUpdates("gps", 5000, 0, listener);
            }
        });
//################################################################################################################

        //ยกเลิกเพิมสถานที
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new PlaceFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.place_status_for_fragment, fragment,TAG);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
//################################################################################################################
        //เพิมสถานที่
        bt_addplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CreateAlertDialogWithRadioButtonGroup();

                placename = et_PlaceName.getText().toString();
                latitude = Double.toString(lat);
                longitude = Double.toString(lng);
                //Radius = Double.toString(mapCircle.getRadius());

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

                } else {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("ต้องการตั้งค่าสถานะของสถานที่นี้เลยไหม ?");
                    dialog.setIcon(R.drawable.logo);

                    dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            chacksettingplace = 0;
                            AddNewPlace addNewPlace2 = new AddNewPlace();
                            addNewPlace2.execute(placename,
                                    getSub_district(lat, lng),
                                    getDistrict(lat, lng),
                                    getProvince(lat, lng),
                                    getZip_code(lat, lng),
                                    getCountry_code(lat, lng),
                                    latitude,
                                    longitude
                            );

                        }
                    });

                    dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            chacksettingplace = 1;
                            AddNewPlace addNewPlace2 = new AddNewPlace();
                            addNewPlace2.execute(placename,
                                    getSub_district(lat, lng),
                                    getDistrict(lat, lng),
                                    getProvince(lat, lng),
                                    getZip_code(lat, lng),
                                    getCountry_code(lat, lng),
                                    latitude,
                                    longitude

                            );
                        }
                    });
                    alertDialog1 = dialog.create();
                    alertDialog1.show();


                }

            }
        });
//################################################################################################################
        // return inflater.inflate(R.layout.fragment_new_place, container, false);
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


    }


    // ##############################################################################################################
    @Override
    public void onResume() {
        super.onResume();

    }
    //################################################################################################################
    @Override
    public void onPause() {
        super.onPause();
        Log.e("nssssssssssssssssss", "onPause1");
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.e("nssssssssssssssssss", "onPause2");

        locationManager.removeUpdates(listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                config();
                break;
            default:
                break;
        }
    }

    //################################################################################################################
    public void config() {
        Log.e("nssssssssssssssssss", "config1");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 10);
            return;
        }
        Log.e("nssssssssssssssssss", "config2");


        boolean isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isNetwork) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER
                    , 5000, 10, listener);
            Location loc = locationManager.getLastKnownLocation(
                    LocationManager.NETWORK_PROVIDER);
            if (loc != null) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
            }
        }

        if (isGPS) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                    , 5000, 10, listener);
            Location loc = locationManager.getLastKnownLocation(
                    LocationManager.GPS_PROVIDER);
            if (loc != null) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
            }
        }

    }
    //################################################################################################################
    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
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

    //################################################################################################################
    private void displaySelectedPlaceFromPlacePicker(Intent data) {
        Place placeSelected = PlacePicker.getPlace(data, getActivity());
        LatLng latLng = placeSelected.getLatLng();
        lat = latLng.latitude;
        lng = latLng.longitude;

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
        locationManager.removeUpdates(listener);

        MarkerOptions options = new MarkerOptions()
                .title("ตำแน่งปัจจุบันของ : ")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(new LatLng(lat, lng))
                .snippet(Namesnippet);

        mMarker = mMap.addMarker(options);

        mapCircle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(lat, lng))
                .radius(50)
                .strokeColor(Color.RED)
                .strokeWidth(5)
                .fillColor(Color.GREEN));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PLACEPICKER && resultCode == Activity.RESULT_OK) {
            displaySelectedPlaceFromPlacePicker(data);
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

    class AddNewPlace extends AsyncTask<String, String, String> {

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



            String Api_Key = "Mark13942180";
            MemberInformation memberInformation = new MemberInformation();
            String Token_key = memberInformation.getToken_key();
            String Member_id = memberInformation.getMember_id();

            String data = "";
            int tmp;

            try {

                URL url = new URL(URL_API.getAPI_add_newplaces());
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
                        + "&longitude=" + longitude;

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
                JSONObject response = root.getJSONObject("NewPlace_response");
                status = response.getString("status");
                if (status.equals("0")) {

                    place_id =response.getString("place_id");

                    if(chacksettingplace==0){

                        Fragment fragment = new PlaceFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }else if(chacksettingplace==1){

//                           Fragment fragment = new SettingPlaceFragment(placename, lat, lng, mapCircle.getRadius(),place_id);
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
//                            fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();


                        Fragment fragment = new SttingNewStatusFragment(place_id,placename, getSub_district(lat, lng) ,Double.toString(lat), Double.toString(lng));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }

                    s = "เพิ่มสถานที่เรียบร้อย";
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
