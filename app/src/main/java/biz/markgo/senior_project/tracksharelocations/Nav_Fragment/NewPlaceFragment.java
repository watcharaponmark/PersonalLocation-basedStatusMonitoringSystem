package biz.markgo.senior_project.tracksharelocations.Nav_Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import biz.markgo.senior_project.tracksharelocations.AddressSearchActivity;
import biz.markgo.senior_project.tracksharelocations.R;


public class NewPlaceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = NewTrackingFragment.class.getSimpleName();
    private FragmentActivity myContext;
    private Button b;
    private TextView tv_address;
    private Toolbar myToolbar,toolbar;
    private LocationManager locationManager;
    private LocationListener listener;
    GoogleMap mMap;
    Marker mMarker;
    double lat, lng;
    String Namesnippet="";
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
                             Bundle savedInstanceState) {

        myFragmentView = inflater.inflate(R.layout.fragment_new_place, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("เพิ่มสถานที่");
       /* myToolbar = (Toolbar) myFragmentView.findViewById(R.id.toolbar);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("เพิ่มสถานที่");
       // toolbar =(Toolbar) myFragmentView.findViewById(R.id.newplace_toolbar);
        toolbar = (Toolbar) getActivity().findViewById(R.id.newplace_toolbar);
        ((ActionBarActivity)getActivity()).getSupportActionBar(toolbar);
        */

        tv_address =(TextView) myFragmentView.findViewById(R.id.tv_address);
        b = (Button) myFragmentView.findViewById(R.id.button);


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
                lat = location.getLatitude();
                lng = location.getLongitude();
                tv_address.setText(getAddress(lat,lng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16));
                // mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
                if (mMarker != null) {
                    mMarker.remove();
                }
                    MarkerOptions options = new MarkerOptions()
                            .title("ตำแน่งปัจจุบันของ : ")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .position(new LatLng(lat, lng))
                            .snippet(Namesnippet);
                    mMarker = mMap.addMarker(options);

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


        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getActivity(),AddressSearchActivity.class));
            }
        });
         configure_button();
//
//        mMap = ((SupportMapFragment) myContext.getSupportFragmentManager()
//        .findFragmentById(R.id.map)).getMap();
        mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                .getMap();



       // return inflater.inflate(R.layout.fragment_new_place, container, false);
        return myFragmentView;
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
    }
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

    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET},10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
            }
        });
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



}
