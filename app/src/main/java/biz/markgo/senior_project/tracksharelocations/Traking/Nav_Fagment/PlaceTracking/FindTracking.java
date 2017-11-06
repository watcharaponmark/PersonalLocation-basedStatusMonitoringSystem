package biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import biz.markgo.senior_project.tracksharelocations.ChangePage.BackButtonHandlerInterface;
import biz.markgo.senior_project.tracksharelocations.ChangePage.OnBackClickListener;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.Traking.PlaceStatusFragment;

public class FindTracking extends Fragment implements OnBackClickListener {

    private View myFragmentView;
    private BackButtonHandlerInterface backButtonHandler;

    private String picture_name,name,member_id_tracking,Tab;
    private ImageView img_new_tracking;
    private TextView tv_name_tracking;
    private Button  bt_trackingfande;
    private Button bt_srecert;
    private AlertDialog alertDialog;


    public FindTracking(String Tab ,String picture_name,String name,String member_id_tracking) {
        this.Tab=Tab;
        this.picture_name=picture_name;
        this.name=name;
        this.member_id_tracking = member_id_tracking;
    }

    public FindTracking() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      myFragmentView = inflater.inflate(R.layout.fragment_find_tracking, container, false);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("เพิ่มการติดตาม");


        img_new_tracking = (ImageView) myFragmentView.findViewById(R.id.img_new_tracking);
        tv_name_tracking=(TextView) myFragmentView.findViewById(R.id.tv_name_tracking);
        bt_trackingfande=(Button) myFragmentView.findViewById(R.id.bt_trackingfande);
        bt_srecert = (Button) myFragmentView.findViewById(R.id.bt_srecert);

        Glide.with(getContext())
                .load(picture_name)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_new_tracking);
        tv_name_tracking.setText(name);

        bt_trackingfande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Tab.equals("Tab1")) {

                    Fragment fragment = new PlaceListFragment(member_id_tracking);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.new_tracking, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }else if(Tab.equals("Tab2")){

                    Fragment fragment = new PlaceListFragment(member_id_tracking);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.search_qr, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            }
        });

        bt_srecert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Tab.equals("Tab1")) {

                    show_secret_id();

                }else if(Tab.equals("Tab2")){

                    show_secret_QR();

                }
            }
        });



        return myFragmentView;
    }

    //กดปุ่มย้อนกลับ
//################################################################################################################
    public void show_secret_id(){

        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_secret_searching);

        final EditText et_secret = (EditText) dialog.findViewById(R.id.et_secret);

        Button bt_ok = (Button) dialog.findViewById(R.id.bt_ok);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!et_secret.getText().toString().equals("")){

                    Fragment fragment = new PlaceSecretFragment(member_id_tracking,et_secret.getText().toString());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.new_tracking, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    dialog.dismiss();

                }else{

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("กรุณากรอกรหัสลับเฉพาะ");
                    dialog.setIcon(R.drawable.logo);
                    dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });
                    alertDialog = dialog.create();
                    alertDialog.show();

                }

            }
        });

        Button bt_exit = (Button) dialog.findViewById(R.id.bt_exit);

        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void show_secret_QR(){

        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_secret_searching);

        final EditText et_secret = (EditText) dialog.findViewById(R.id.et_secret);

        Button bt_ok = (Button) dialog.findViewById(R.id.bt_ok);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!et_secret.getText().toString().equals("")){

                    Fragment fragment = new PlaceSecretFragment(member_id_tracking,et_secret.getText().toString());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.search_qr, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    dialog.dismiss();

                }else{

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("กรุณากรอกรหัสลับเฉพาะ");
                    dialog.setIcon(R.drawable.logo);
                    dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });
                    alertDialog = dialog.create();
                    alertDialog.show();

                }

            }
        });

        Button bt_exit = (Button) dialog.findViewById(R.id.bt_exit);

        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

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
    //################################################################################################################



}
