package biz.markgo.senior_project.tracksharelocations.PlaceStatus;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URL;

import biz.markgo.senior_project.tracksharelocations.ChangePage.BackButtonHandlerInterface;
import biz.markgo.senior_project.tracksharelocations.ChangePage.OnBackClickListener;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.NewPlaceFragment;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;


public class HomePlaceStatusFragment extends Fragment implements OnMapReadyCallback,OnBackClickListener {

    private View myFragmentView;
    private BackButtonHandlerInterface backButtonHandler;

    private MapView gMapView;
    private MapFragment gMapFragment;
    private GoogleMap mMap =null;
    private LocationManager locationManager;
    private LocationListener listener;
    private Marker mMarker;
    private double lat, lng;
    //private Circle mapCircle;
    private Button bt_home_sha;
    String myphoto;
    URL url ;
    Bitmap bmp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_home_place_status, container, false);
        bt_home_sha =(Button) myFragmentView.findViewById(R.id.bt_home_sha);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("หน้าแรก");

//#####################################################################################

//        gMapView = (MapView) myFragmentView.findViewById(R.id.map_home);
//        gMapView.getMapAsync(this);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
                lat = location.getLatitude();
                lng = location.getLongitude();

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));
                // mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
                if (mMarker != null) {
                    mMarker.remove();
                }
//                if (mapCircle != null) {
//                    mapCircle.remove();
//                }



                HomePlaceStatusFragment.TheTask_marker theTask_marker=new HomePlaceStatusFragment.TheTask_marker();
                theTask_marker.execute();

//                mapCircle = mMap.addCircle(new CircleOptions()
//                        .center(new LatLng(lat, lng))
//                        .radius(50)
//                        .strokeColor(Color.RED)
//                        .strokeWidth(5)
//                        .fillColor(Color.GREEN));

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

//#####################################################################################



//        mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_home))
//                .getMap();
//        setUpMap();

//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync((OnMapReadyCallback) this);

//        gMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_home);
//        gMapFragment.getMapAsync(this);

//        gMapView = (MapView) myFragmentView.findViewById(R.id.map_home);
//        gMapView.getMapAsync(this);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_home);
//        mapFragment.getMapAsync(this);

//        gMapView = (MapView) myFragmentView.findViewById(R.id.map_home);
//        gMapView.getMapAsync(this);

        final SupportMapFragment myMAPF = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map_home);
        myMAPF.getMapAsync(this);

        config();
//#####################################################################################
        bt_home_sha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new NewPlaceFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
//#####################################################################################


        return myFragmentView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        setUpMap();

    }


    public void setUpMap() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 20);
            }
            return;
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (isNetwork) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER
                    ,5000, 50, listener);
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
            return;
        }
        locationManager.removeUpdates(listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                config();
                break;
            case 20:
                setUpMap();
                break;
            default:
                break;
        }
    }
    public void config() {
        // first check for permissions
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
                    ,5000, 10, listener);
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


    class TheTask_marker extends AsyncTask<Void,Void,Void> {
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();

    }
    @Override
    protected Void doInBackground(Void... params) {
        URL url ;
        try {
            // URL img to bitmap
            myphoto = MemberInformation.getPicture_name().toString();
            url = new URL(myphoto);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        View marker = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView im_user = (ImageView) marker.findViewById(R.id.im_user);

        //แลงขนาดภาพ
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp2 = Bitmap.createBitmap(400, 400, conf);
        Canvas canvas1 = new Canvas(bmp2);
        Paint color = new Paint();
        //color.setTextSize(30);
        color.setColor(Color.WHITE);
        Bitmap resized = Bitmap.createScaledBitmap(bmp, 490, 440, true);
        canvas1.drawBitmap(resized, 0, 0, color);

        //setรูปภาพ
        im_user.setImageBitmap(bmp2);

        MarkerOptions options = new MarkerOptions()
                .title("ตำแน่งปัจจุบันของ : ")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), marker)))
                .anchor(0.5f, 1)
                .position(new LatLng(lat, lng))
                .snippet(MemberInformation.getFirstName());
        mMarker = mMap.addMarker(options);


    }
}
    //method แปลง layout to Bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}
