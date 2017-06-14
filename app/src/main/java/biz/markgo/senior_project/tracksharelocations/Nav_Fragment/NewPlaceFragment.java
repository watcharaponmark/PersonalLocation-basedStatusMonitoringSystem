package biz.markgo.senior_project.tracksharelocations.Nav_Fragment;

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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

import biz.markgo.senior_project.tracksharelocations.HomeActivity;
import biz.markgo.senior_project.tracksharelocations.Login.DetailRegisterActivity;
import biz.markgo.senior_project.tracksharelocations.Nav_Fragment.adpter.CustomAdapter_spinner;
import biz.markgo.senior_project.tracksharelocations.Search_Address.AddressSearchActivity;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.User_Data;


public class NewPlaceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = NewTrackingFragment.class.getSimpleName();
    private FragmentActivity myContext;
    private Button bt_re, bt_addplace, bt_cancel;
    private TextView tv_address;
    private Toolbar myToolbar, toolbar;
    private LocationManager locationManager;
    private LocationListener listener;
    private Spinner spinner_radius;
    private GoogleMap mMap;
    private Circle mapCircle;
    private Marker mMarker;
    private double lat, lng;
    private EditText tv_PersonName;
    private String placename;
    private  AlertDialog alertDialog1;
    private  CharSequence[] values = {"สาธารณะ","ส่วนตัว"};
    String Namesnippet = "";
    String latitude , longitude , Radius , Account_id ,StatusLogin,sharing_status ;


    private View myFragmentView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public NewPlaceFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewPlaceFragment newInstance(String param1, String param2) {
        NewPlaceFragment fragment = new NewPlaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        myFragmentView = inflater.inflate(R.layout.fragment_new_place, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("เพิ่มสถานที่");
       /* myToolbar = (Toolbar) myFragmentView.findViewById(R.id.toolbar);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("เพิ่มสถานที่");
       // toolbar =(Toolbar) myFragmentView.findViewById(R.id.newplace_toolbar);
        toolbar = (Toolbar) getActivity().findViewById(R.id.newplace_toolbar);
        ((ActionBarActivity)getActivity()).getSupportActionBar(toolbar);
        */

        tv_address = (TextView) myFragmentView.findViewById(R.id.tv_address);
        tv_PersonName=(EditText)myFragmentView.findViewById(R.id.et_PlaceName);
        bt_re = (Button) myFragmentView.findViewById(R.id.bt_re);
        bt_cancel = (Button) myFragmentView.findViewById(R.id.bt_cancel_fnp);
        bt_addplace = (Button) myFragmentView.findViewById(R.id.bt_addplace_fnp);

        spinner_radius = (Spinner) myFragmentView.findViewById(R.id.spinner_radius);
        final String[] option_radius = {"50 เมตร", "80 เมตร", "120 เมตร", "150 เมตร"};
        int flags[] = {R.drawable.cir001, R.drawable.cir001, R.drawable.cir001, R.drawable.cir001};

        CustomAdapter_spinner customAdapter = new CustomAdapter_spinner(getContext(), flags, option_radius);
        spinner_radius.setAdapter(customAdapter);


//         ArrayAdapter<String> adapter_radius=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,option_radius);
//        spinner_radius.setAdapter(adapter_radius);

        spinner_radius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Object item = parent.getItemAtPosition(position);

                if (option_radius[position].equals("50 เมตร")) {
//                    Toast.makeText(getActivity(), "Selected"+option_radius[position],
//                            Toast.LENGTH_SHORT).show();
                    if (mapCircle != null) {
                        mapCircle.remove();
                    }
                    mapCircle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(lat, lng))
                            .radius(50)
                            .strokeColor(Color.RED)
                            .strokeWidth(5)
                            .fillColor(Color.GREEN));
                } else if (option_radius[position].equals("80 เมตร")) {
//                    Toast.makeText(getActivity(), "Selected"+option_radius[position],
//                            Toast.LENGTH_SHORT).show();
                    if (mapCircle != null) {
                        mapCircle.remove();
                    }
                    mapCircle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(lat, lng))
                            .radius(80)
                            .strokeColor(Color.RED)
                            .strokeWidth(5)
                            .fillColor(Color.GREEN));
                } else if (option_radius[position].equals("120 เมตร")) {
//                    Toast.makeText(getActivity(), "Selected"+option_radius[position],
//                            Toast.LENGTH_SHORT).show();
                    if (mapCircle != null) {
                        mapCircle.remove();
                    }
                    mapCircle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(lat, lng))
                            .radius(120)
                            .strokeColor(Color.RED)
                            .strokeWidth(5)
                            .fillColor(Color.GREEN));
                } else if (option_radius[position].equals("150 เมตร")) {
//                    Toast.makeText(getActivity(), "Selected"+option_radius[position],
//                            Toast.LENGTH_SHORT).show();
                    if (mapCircle != null) {
                        mapCircle.remove();
                    }
                    mapCircle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(lat, lng))
                            .radius(150)
                            .strokeColor(Color.RED)
                            .strokeWidth(5)
                            .fillColor(Color.GREEN));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
                lat = location.getLatitude();
                lng = location.getLongitude();
                tv_address.setText(getAddress(lat, lng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16));
                // mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
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


        configure_button();


//
//                    mMarker = mMap.addMarker(options);
//        mMap = ((SupportMapFragment) myContext.getSupportFragmentManager()
//        .findFragmentById(R.id.map)).getMap();
        mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                .getMap();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng arg0) {

                if (mMarker != null) {
                    mMarker.remove();
                }
                if (mapCircle != null) {
                    mapCircle.remove();
                }
                mMap.addMarker(new MarkerOptions().position(arg0)
                        .title(String.valueOf(arg0.latitude)
                                + ", " + String.valueOf(arg0.longitude)));
                mapCircle = mMap.addCircle(new CircleOptions()
                        .center(arg0)
                        .radius(50)
                        .strokeColor(Color.RED)
                        .strokeWidth(5)
                        .fillColor(Color.GREEN));

            }

        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker arg0) {
                arg0.remove();
                mapCircle.remove();
                Toast.makeText(getActivity().getApplicationContext()
                        , "Remove Marker " + String.valueOf(arg0.getId())
                        , Toast.LENGTH_SHORT).show();
                return true;
            }


        });


        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddressSearchActivity.class));
            }
        });


        bt_re.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                boolean isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (isNetwork) {
                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 10);
                        }
                        return;
                    }
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

        //ยกเลิกเพิมสถานที
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),HomeActivity.class);
                intent.putExtra("statusLogin",User_Data.getStatusLogin());
                intent.putExtra("FirstName", User_Data.getFirstName());
                intent.putExtra("LastName", User_Data.getLastName());
                intent.putExtra("personPhotoUrl", User_Data.getPersonPhotoUrl());
                intent.putExtra("email",User_Data.getEmail() );
                intent.putExtra("account_id", User_Data.getAccount_id());
                startActivity(intent);
            }
        });

        //เพิมสถานที่
        bt_addplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialogWithRadioButtonGroup() ;
            }
        });

        // return inflater.inflate(R.layout.fragment_new_place, container, false);
        return myFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isNetwork) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 10);
                }
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER
                    , 5000, 50, listener);
            Location loc = locationManager.getLastKnownLocation(
                    LocationManager.NETWORK_PROVIDER);
            if (loc != null) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
            }
        }

        if (isGPS) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                    , 5000, 50, listener);
            Location loc = locationManager.getLastKnownLocation(
                    LocationManager.GPS_PROVIDER);
            if (loc != null) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 10);
            }
            return;
        }
        locationManager.removeUpdates(listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }


    public void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 10);
            }
            return;
        }

    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                android.location.Address address = addresses.get(0);
                //ชื่อซอย หรือชื่อตำบล
                result.append(address.getAddressLine(0)).append(",");
                //อำเภอ
                result.append(address.getLocality());
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


    public void CreateAlertDialogWithRadioButtonGroup(){


        if(tv_PersonName.getText().toString().equals("")){

        }else{
            placename= tv_PersonName.getText().toString();
        }
         latitude = Double.toString(lat);
         longitude = Double.toString(lng);
         Radius =  Double.toString(mapCircle.getRadius());
         Account_id = User_Data.getAccount_id();
        StatusLogin = User_Data.getStatusLogin();


        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("กรุณาเลือกสถานะ การแบ่งปันสถานที่");
        dialog.setIcon(R.drawable.logo);
       // dialog.setCancelable(true);
        dialog.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:

                       // Toast.makeText(getContext(), "สาธารณะ", Toast.LENGTH_LONG).show();
                         sharing_status = "Public";
                        AddNewPlace addNewPlace1 = new AddNewPlace();
                        addNewPlace1.execute(Account_id,
                                StatusLogin,
                                placename,
                                getSub_district(lat,lng),
                                getDistrict(lat,lng),
                                getProvince(lat,lng),
                                getZip_code(lat,lng),
                                getCountry_code(lat,lng),
                                latitude,
                                longitude,
                                Radius,
                                sharing_status
                        );
                        break;
                    case 1:

                        //Toast.makeText(getContext(), "ส่วนตัว", Toast.LENGTH_LONG).show();
                         sharing_status = "Private";
                        AddNewPlace addNewPlace2 = new AddNewPlace();
                        addNewPlace2.execute(Account_id,
                                StatusLogin,
                                placename,
                                getSub_district(lat,lng),
                                getDistrict(lat,lng),
                                getProvince(lat,lng),
                                getZip_code(lat,lng),
                                getCountry_code(lat,lng),
                                latitude,
                                longitude,
                                Radius,
                                sharing_status
                        );
                        break;

                }
                alertDialog1.dismiss();
            }
        });
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog1 = dialog.create();
        alertDialog1.show();

    }


    class AddNewPlace extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String Account_id = params[0];
            String StatusLogin = params[1];
            String placename = params[2];
            String getSub_district = params[3];
            String getDistrict = params[4];
            String getProvince = params[5];
            String getZip_code = params[6];
            String getCountry_code = params[7];
            String latitude = params[8];
            String longitude = params[9];
            String Radius = params[10];
            String sharing_status = params[11];
            String Api_Key = "Mark13942180";
            String data = "";
            int tmp;

            try {

                URL url = new URL("http://senior-project.markgo.biz/place/add_newplaces.php");
                String urlParams ="API_Key="+Api_Key
                        +"&Account_id="+Account_id
                        +"&StatusLogin="+StatusLogin
                        +"&place_name="+placename
                        +"&sub_district="+getSub_district
                        +"&district="+getDistrict
                        +"&province="+getProvince
                        +"&zip_code="+getZip_code
                        +"&country_code="+getCountry_code
                        +"&latitude="+latitude
                        +"&longitude="+longitude
                        +"&radius="+Radius
                        +"&sharing_status="+sharing_status;

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
                    JSONObject response = root.getJSONObject("responsejson");
                    status = response.getString("status");
                if(status.equals("TRUE")) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra("statusLogin", User_Data.getStatusLogin());
                    intent.putExtra("FirstName", User_Data.getFirstName());
                    intent.putExtra("LastName", User_Data.getLastName());
                    intent.putExtra("personPhotoUrl", User_Data.getPersonPhotoUrl());
                    intent.putExtra("email", User_Data.getEmail());
                    intent.putExtra("account_id", User_Data.getAccount_id());
                    startActivity(intent);
                    s = "เพิ่มสถานที่เรียบร้อย";
                    Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }

        }

    }
}
