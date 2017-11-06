package biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.TreeSet;

import biz.markgo.senior_project.tracksharelocations.API.URL_API;
import biz.markgo.senior_project.tracksharelocations.ChangePage.BackButtonHandlerInterface;
import biz.markgo.senior_project.tracksharelocations.ChangePage.OnBackClickListener;
import biz.markgo.senior_project.tracksharelocations.Config.Profile.ConfigProfileActivity;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus.Time.DurationFragment;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus.Time.TimeDataset;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus.Time.TimeFragment;
import biz.markgo.senior_project.tracksharelocations.R;


public class SttingNewStatusFragment extends Fragment implements OnBackClickListener {

    private View myFragmentView;
    private String TAG = SttingNewStatusFragment.class.getSimpleName();
    private BackButtonHandlerInterface backButtonHandler;


    private String place_id,sharing_status,place_name;
    private LinearLayout Layout_Secert;
    private Button bt_ok_Newsetting,bt_back_place_setting;
    private EditText et_nameNewsetting,et_code_Newsetting;
    private Spinner spinner_status_Newsetting,spinner_on,spinner_off;

    private AlertDialog alertDialog1;
    private AlertDialog alertDialog_day;
    private AlertDialog alertDialog_timeout;


    private LinearLayout layout_day;
    private TextView tv_dayall,tv_Timeall;

    private CheckBox cb_realplace;
    private String realplace;

    private RadioGroup radioTimeGroup;
    private String offline_mode;
    private String out_place_online;
    private CheckBox cb_out_place_online;
    private LinearLayout layout_time_out,layout_timeend;
    private TextView tv_time_out,tv_timeend;
    private TextView tv_time_out2,tv_timeend2;

    private String type_online = "DT",type_offline;
    private CardView CV_radius,CV_date_Newsetting;
    private GoogleMap mMap;
    private Circle mapCircle;
    private Marker mMarker;

    private TextView tv_radius2;
    private int radius = 5;


    private String[] option_on;
    private String[] option_off;

    private  int mHour1,mHour2;
    private  int mMinute1,mMinute2;
    private  String time1 = "เริ่มต้น";
    private  String time2 = "สิ้นสุด";
    Button bt_time_Newsetting1 , bt_time_Newsetting2;


    private String timer = "15 นาที";
    private String 	to_time;



    private String[] values_item_day = {"อาทิตย์", "จันทร์","อังคาร","พุธ","พฤหัสบดี","ศุกร์","เสาร์"};
    private TreeSet<Integer> seletedItems_day=new TreeSet<>();

    private AlertDialog alertDialog;
    private String name_add,mLatitude,mLongitude;



    public SttingNewStatusFragment(String place_id,String place_name,String name_add,String mLatitude,String mLongitude) {
        this.place_id=place_id;
        this.place_name=place_name;
        this.name_add=name_add;
        this.mLatitude=mLatitude;
        this.mLongitude=mLongitude;
    }
    public SttingNewStatusFragment(String place_id,String place_name) {
        this.place_id=place_id;
        this.place_name=place_name;

    }
    public SttingNewStatusFragment(String place_id, String placename, String s, String toString) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_stting_new_status, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("เพิ่มการตั้งค่า");

//########################################################################################
        et_nameNewsetting = (EditText) myFragmentView.findViewById(R.id.et_nameNewsetting);

        CV_radius = (CardView) myFragmentView.findViewById(R.id.CV_radius);
        tv_radius2 = (TextView) myFragmentView.findViewById(R.id.tv_radius2);

        bt_back_place_setting = (Button) myFragmentView.findViewById(R.id.bt_back_place_setting);
        bt_ok_Newsetting = (Button) myFragmentView.findViewById(R.id.bt_ok_Newsetting);
        et_code_Newsetting = (EditText) myFragmentView.findViewById(R.id.et_code_Newsetting);


        //radioTimeGroup = (RadioGroup) myFragmentView.findViewById(R.id.radioGroup);

        spinner_status_Newsetting = (Spinner) myFragmentView.findViewById(R.id.spinner_status_Newsetting);


        cb_realplace = (CheckBox) myFragmentView.findViewById(R.id.cb_realplace);
        spinner_on = (Spinner) myFragmentView.findViewById(R.id.spinner_on);


        radioTimeGroup = (RadioGroup) myFragmentView.findViewById(R.id.radioGroup);
        spinner_off = (Spinner) myFragmentView.findViewById(R.id.spinner_off);
        cb_out_place_online = (CheckBox) myFragmentView.findViewById(R.id.cb_out_place_online);
        layout_time_out = (LinearLayout) myFragmentView.findViewById(R.id.layout_time_out);
        layout_timeend = (LinearLayout) myFragmentView.findViewById(R.id.layout_timeend);
        tv_time_out = (TextView) myFragmentView.findViewById(R.id.tv_time_out);
        tv_time_out2 = (TextView) myFragmentView.findViewById(R.id.tv_time_out2);
        tv_timeend = (TextView) myFragmentView.findViewById(R.id.tv_timeend);
        tv_timeend2 = (TextView) myFragmentView.findViewById(R.id.tv_timeend2);

        layout_day = (LinearLayout) myFragmentView.findViewById(R.id.layout_day);
        tv_dayall =(TextView) myFragmentView.findViewById(R.id.tv_dayall);

        CV_date_Newsetting = (CardView) myFragmentView.findViewById(R.id.CV_date_Newsetting);
        tv_Timeall = (TextView) myFragmentView.findViewById(R.id.tv_Timeall);


        Layout_Secert =(LinearLayout) myFragmentView.findViewById(R.id.Layout_Secert);
        Layout_Secert.setVisibility(View.INVISIBLE);

//########################################################################################

        tv_radius2.setText(radius+" เมตร");
        CV_radius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                /////make map clear //FLAG_DIM_BEHIND //FLAG_KEEP_SCREEN_ON
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialogmap);////your// custom content

                MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);
                MapsInitializer.initialize(getActivity());

                mMapView.onCreate(dialog.onSaveInstanceState());
                mMapView.onResume();


                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(final GoogleMap googleMap) {
                        mMap = googleMap;
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;

                        }

                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        //mMap.setMyLocationEnabled(true);
                        mMap.setTrafficEnabled(true);
                        mMap.setIndoorEnabled(true);
                        mMap.setBuildingsEnabled(true);
                        googleMap.getUiSettings().setZoomControlsEnabled(true);


                        MarkerOptions options = new MarkerOptions()
                                .title("สถานที่ : ")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .position(new LatLng(Double.parseDouble(mLatitude),Double.parseDouble(mLongitude)))
                                .snippet(place_name);

                        mMarker = mMap.addMarker(options);

                        int Zoom = 0;
                        if(radius-5 <= 50){
                            Zoom = 19;
                        }else if(radius-5 > 50 && radius-5 <=100){
                            Zoom = 5;
                        }else if(radius-5 > 100 && radius-5 <=150){
                            Zoom = 17;
                        }else if(radius-5 > 150 && radius-5 <=200){
                            Zoom = 16;
                        }

                        LatLng coordinate = new LatLng(Double.parseDouble(mLatitude),Double.parseDouble(mLongitude));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, Zoom));

                        mapCircle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(Double.parseDouble(mLatitude),Double.parseDouble(mLongitude)))
                                .radius(radius)
                                .strokeColor(Color.RED)
                                .strokeWidth(5)
                                .fillColor(Color.GREEN));


                    }
                });


                SeekBar seekBar_radius = (SeekBar) dialog.findViewById(R.id.seekBar_radius);
                final TextView tv_shows_radius = (TextView) dialog.findViewById(R.id.tv_shows_radius);
                //seekBar_radius.getMax()
                seekBar_radius.setProgress(radius-5);
                tv_shows_radius.setText("รัศมี: " +radius+ " เมตร");

                seekBar_radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int progress = 0;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                        progress = progresValue+5;
                        radius = progress;
                       // Toast.makeText(getContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        //Toast.makeText(getContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        radius = progress;
                        tv_shows_radius.setText("รัศมี: " + progress+ " เมตร");

                        int Zoom = 0;
                        if(progress <= 50){
                            Zoom = 19;
                        }else if(progress > 50 && progress <=100){
                            Zoom = 17;
                        }else if(progress > 100 && progress <=150){
                            Zoom = 17;
                        }else if(progress > 150 && progress <=200){
                            Zoom = 16;
                        }

                        if (mapCircle != null) {
                            mapCircle.remove();
                        }
                        LatLng coordinate = new LatLng(Double.parseDouble(mLatitude),Double.parseDouble(mLongitude));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, Zoom));

                            mapCircle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(Double.parseDouble(mLatitude),Double.parseDouble(mLongitude)))
                            .radius(progress)
                            .strokeColor(Color.RED)
                            .strokeWidth(5)
                            .fillColor(Color.GREEN));

                        tv_radius2.setText(progress+" เมตร");

                        //Toast.makeText(getContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
                    }
                });



                Button bt_ok = (Button) dialog.findViewById(R.id.bt_ok);
                // if button is clicked, close the custom dialog
                bt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

//########################################################################################

        layout_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                                //Toast.makeText(getContext(), "กำหนดเอง", Toast.LENGTH_LONG).show();

                                seletedItems_day.clear();
                                AlertDialog.Builder dialog2 = new AlertDialog.Builder(getContext());
                                dialog2.setTitle("กรุณาเลือกวัน");
                                dialog2.setIcon(R.drawable.logo);
                                dialog2.setMultiChoiceItems(values_item_day, null, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                        if (isChecked) {
                                            // If the user checked the item, add it to the selected items
                                            seletedItems_day.add(indexSelected);
                                        } else if (seletedItems_day.contains(indexSelected)) {
                                            // Else, if the item is already in the array, remove it
                                            seletedItems_day.remove(Integer.valueOf(indexSelected));
                                        }
                                    }
                                });

                                dialog2.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                        String str="";

                                        if(seletedItems_day.isEmpty()){
                                            str= "ทุกวัน  ";
                                        }

                                        Iterator<Integer> iterator=seletedItems_day.iterator();
                                        while(iterator.hasNext()){
                                            Integer value= (Integer)iterator.next();
                                            if(value==0){
                                                str+="อา,";
                                            }else if(value==1){
                                                str+="จ,";
                                            }else if(value==2){
                                                str+="อ,";
                                            }else if(value==3){
                                                str+="พ,";
                                            }else if(value==4){
                                                str+="พฤ,";
                                            }else if(value==5){
                                                str+="ศ,";
                                            }else if(value==6){
                                                str+="ส,";
                                            }
                                        }

                                        str = str.substring(0,str.length()-1);
                                        tv_dayall.setText(str);

                                    }
                                });

                                dialog2.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                        tv_dayall.setText("ทุกวัน");

                                    }
                                });

                                alertDialog_day = dialog2.create();
                                alertDialog_day.show();


            }
        });

//########################################################################################

        CV_date_Newsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog_time = new Dialog(getContext());
                dialog_time.setContentView(R.layout.dialog_time);

                 bt_time_Newsetting1 = (Button) dialog_time.findViewById(R.id.bt_time_Newsetting1);
                 bt_time_Newsetting2 = (Button) dialog_time.findViewById(R.id.bt_time_Newsetting2);


                bt_time_Newsetting1.setText(time1);
                bt_time_Newsetting2.setText(time2);

                TimeDataset.setStart_time(bt_time_Newsetting1.getText().toString());
                TimeDataset.setEnd_time(bt_time_Newsetting2.getText().toString());

                final Calendar cal = Calendar.getInstance(TimeZone.getDefault());

                bt_time_Newsetting1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                R.style.MyDialogTheme_Time, mOnTimeSetListener1,
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                DateFormat.is24HourFormat(getActivity()));
                        timePickerDialog.setCancelable(false);
                        timePickerDialog.setTitle("เลือกเวลาที่่ต้องการ");
                        timePickerDialog.show();

                    }
                });

                bt_time_Newsetting2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                R.style.MyDialogTheme_Time, mOnTimeSetListener2,
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                DateFormat.is24HourFormat(getActivity()));
                        timePickerDialog.setCancelable(false);
                        timePickerDialog.setTitle("เลือกเวลาที่่ต้องการ");
                        timePickerDialog.show();

                    }
                });

                Button bt_exit =(Button) dialog_time.findViewById(R.id.bt_exit);
                bt_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_time.dismiss();
                    }
                });

                Button bt_ok =(Button) dialog_time.findViewById(R.id.bt_ok);
                bt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(TimeDataset.getStart_time().equals("เริ่มต้น")){

                            tv_Timeall.setText("ตลอดเวลา");

                        }else if(TimeDataset.getEnd_time().equals("สิ้นสุด")) {

                            tv_Timeall.setText("ตลอดเวลา");

                        }else{
                            tv_Timeall.setText(TimeDataset.getStart_time()+" - "+TimeDataset.getEnd_time()+" น.");
                        }


                        dialog_time.dismiss();
                    }
                });

                dialog_time.show();

            }
        });

//########################################################################################


        realplace = "Yes";
        cb_realplace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()) {
                    realplace = "No";
                } else {
                    realplace = "Yes";
                }
            }
        });


//########################################################################################

        option_on = new String[]{"อัตโนมัติ", "ถาม", "เฉยๆ"};
        ArrayAdapter<String> adapter_on=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,option_on);
        adapter_on.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_on.setAdapter(adapter_on);

        spinner_on.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (option_on[position].equals("อัตโนมัติ")) {

//                    Toast.makeText(getActivity(), "Selected"+option_on[position],
//                            Toast.LENGTH_SHORT).show();

                    type_online = "Auto";

                }else if(option_on[position].equals("ถาม")){

//                    Toast.makeText(getActivity(), "Selected"+option_on[position],
//                            Toast.LENGTH_SHORT).show();
                    type_online = "Ask";

                }else if(option_on[position].equals("เฉยๆ")){

//                    Toast.makeText(getActivity(), "Selected"+option_on[position],
//                            Toast.LENGTH_SHORT).show();

                    type_online = "Inactive";

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//########################################################################################

        offline_mode = "1";
        to_time = "null";
        layout_time_out.setVisibility(View.INVISIBLE);
        layout_timeend.setVisibility(View.INVISIBLE);


//        if(timer.indexOf(":") == -1) {
//
//            tv_time_out.setText(timer);
//
//        }else{
//
//            String H, M = "";
//            H = timer.substring(0, timer.indexOf(":"));
//            M = timer.substring(timer.indexOf(":") + 1);
//
//            if(H.equals("0")){
//                tv_time_out.setText(M+" นาที");
//            }else{
//                tv_time_out.setText(H +" ชัวโมง  "+ M+" นาที");
//            }
//
//        }
        tv_time_out.setText(timer);
        TimeDataset.setDuration_time(timer);


        layout_time_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogViewTime_out();
            }
        });



        layout_timeend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogViewTimeend();
            }
        });

        radioTimeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.rb_time_range:
                        offline_mode = "1";
                        layout_time_out.setVisibility(View.INVISIBLE);
                        layout_timeend.setVisibility(View.INVISIBLE);
                        TimeDataset.setDuration_time("null");
                        to_time = "null";
                        break;
                    case R.id.rb_time_out:
                        offline_mode = "2";
                        layout_time_out.setVisibility(View.VISIBLE);
                        layout_timeend.setVisibility(View.INVISIBLE);
                        AlertDialogViewTime_out();
                        to_time = "null";
                        break;
                    case R.id.rb_time_end:
                        offline_mode = "3";
                        layout_time_out.setVisibility(View.INVISIBLE);
                        layout_timeend.setVisibility(View.VISIBLE);
                        AlertDialogViewTimeend();
                        TimeDataset.setDuration_time("null");
                        break;
                }
            }
        });

//########################################################################################

        out_place_online = "No";
        cb_out_place_online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()) {
                    out_place_online = "Yes";
                } else {
                    out_place_online = "No";
                }
            }
        });

//########################################################################################

            option_off = new String[]{"อัตโนมัติ", "ถาม", "เฉยๆ"};
            ArrayAdapter<String> adapter_off=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,option_off);
            adapter_off.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_off.setAdapter(adapter_off);

        spinner_off.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (option_off[position].equals("อัตโนมัติ")) {

//                    Toast.makeText(getActivity(), "Selected"+option_off[position],
//                            Toast.LENGTH_SHORT).show();

                    type_offline = "Auto";

                }else if (option_off[position].equals("ถาม")) {

                    //Toast.makeText(getActivity(), "Selected"+option_off[position], Toast.LENGTH_SHORT).show();

                    type_offline = "Ask";


                }else if (option_off[position].equals("เฉยๆ")) {

//                    Toast.makeText(getActivity(), "Selected"+option_off[position],
//                            Toast.LENGTH_SHORT).show();
                    type_offline = "Inactive";

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//########################################################################################
        final String[] option_statusShar = {"ส่วนตัว", "ลับเฉาพะ","สาธารณะ"};
        ArrayAdapter<String> adapter_Status=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,option_statusShar);
        adapter_Status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_status_Newsetting.setAdapter(adapter_Status);

        spinner_status_Newsetting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Object item = parent.getItemAtPosition(position);

                if (option_statusShar[position].equals("สาธารณะ")) {
//                    Toast.makeText(getActivity(), "Selected"+option_radius[position],
//                            Toast.LENGTH_SHORT).show();
                    sharing_status = "Public";
                    Layout_Secert.setVisibility(View.INVISIBLE);


                } else if (option_statusShar[position].equals("ส่วนตัว")) {
//                    Toast.makeText(getActivity(), "Selected"+option_radius[position],
//                            Toast.LENGTH_SHORT).show();
                    sharing_status = "Private";
                    Layout_Secert.setVisibility(View.INVISIBLE);

                } else if (option_statusShar[position].equals("ลับเฉาพะ")) {
//                    Toast.makeText(getActivity(), "Selected"+option_radius[position],
//                            Toast.LENGTH_SHORT).show();
                    sharing_status = "Secret";
                    Layout_Secert.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//########################################################################################

        bt_back_place_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new SettingAllPlaceFragment(place_id,place_name,name_add,mLatitude,mLongitude);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
//########################################################################################
        bt_ok_Newsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharing_status.equals("Secret") && et_code_Newsetting.getText().toString().isEmpty()){

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


                }else if(et_nameNewsetting.getText().toString().isEmpty()){

                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setTitle("กรุณากรอกชื่อการตั้งค่า");
                        dialog.setIcon(R.drawable.logo);
                        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog = dialog.create();
                        alertDialog.show();


                }else {

                    String Radius = tv_radius2.getText().toString();
                    Radius = Radius.replace(" เมตร","");

                    String Str_day = "";
                    Iterator<Integer> iterator=seletedItems_day.iterator();
                    while(iterator.hasNext()) {
                        Integer value = (Integer) iterator.next();
                        Str_day += Integer.toString(value)+",";
                    }

                    if(seletedItems_day.size()==0){
                        Str_day = "0,1,2,3,4,5,6";
                    }else {
                        Str_day = Str_day.substring(0, Str_day.length() - 1);
                    }


                    String Start_time = TimeDataset.getStart_time();
                    if(Start_time.equals("เริ่มต้น")){
                        Start_time = "null";
                    }

                    String End_time = TimeDataset.getEnd_time();
                    if(End_time.equals("ส้ินสุด")){
                        End_time = "null";
                    }

                    if("null".equals(to_time)){

                        to_time = "00:00";

                    }else{

                        to_time = to_time.replace(" น.", "");
                    }


                    Log.e(TAG,"Radius: "+Radius);

                    AddNewstatusPlace addNewstatusPlace = new AddNewstatusPlace();
                    addNewstatusPlace.execute(place_id,
                            et_nameNewsetting.getText().toString(),
                            Radius,
                            Str_day,
                            Start_time,
                            End_time,
                            type_online,
                            realplace,
                            type_offline,
                            offline_mode,
                            TimeDataset.getDuration_time(),
                            to_time,
                            out_place_online,
                            sharing_status,
                            et_code_Newsetting.getText().toString()

                    );

                }
            }
        });
//########################################################################################
        return myFragmentView;

    }

    //########################################################################################

    private void AlertDialogViewTime_out() {


        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_duration);

        final NumberPicker numberPicker_hr = (NumberPicker) dialog.findViewById(R.id.numberPicker_hr);
        final NumberPicker numberPicker_mi = (NumberPicker) dialog.findViewById(R.id.numberPicker_mi);

        numberPicker_hr.setMinValue(0);   // min value 0
        numberPicker_hr.setMaxValue(23); // max value 100
        numberPicker_hr.setWrapSelectorWheel(false);

        numberPicker_mi.setMinValue(0);   // min value 0
        numberPicker_mi.setMaxValue(59); // max value 60
        numberPicker_mi.setWrapSelectorWheel(false);

        String H1 = "",M1 = "";

        if(tv_time_out.getText().toString().indexOf(" ชัวโมง") == -1){

            M1  = tv_time_out.getText().toString().substring(0,tv_time_out.getText().toString().indexOf(" นาที"));

            Log.e("HHHH",H1+"T"+M1);

            numberPicker_hr.setValue(0);
            numberPicker_mi.setValue(Integer.parseInt(M1));

        }else{

            H1  = tv_time_out.getText().toString().substring(0,tv_time_out.getText().toString().indexOf(" ชัวโมง"));
            M1  = tv_time_out.getText().toString().substring(tv_time_out.getText().toString().indexOf(" ชัวโมง")+9,tv_time_out.getText().toString().indexOf(" นาที"));
            Log.e("HHHH",H1+"T"+M1);

            numberPicker_hr.setValue(Integer.parseInt(H1));
            numberPicker_mi.setValue(Integer.parseInt(M1));
        }


        numberPicker_hr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(newVal==0) {
                    tv_time_out.setText(String.valueOf(numberPicker_mi.getValue()) + " นาที"); //set the value to textview
                    TimeDataset.setDuration_time(newVal + ":" + String.valueOf(numberPicker_mi.getValue()));
                }else{
                    tv_time_out.setText(newVal + " ชัวโมง  " + String.valueOf(numberPicker_mi.getValue()) + " นาที"); //set the value to textview
                    TimeDataset.setDuration_time(newVal + ":" + String.valueOf(numberPicker_mi.getValue()));
                }

            }
        });

        numberPicker_mi.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                if(numberPicker_mi.getValue()==0) {
                    tv_time_out.setText(newVal+" นาที"); //set the value to textview
                    TimeDataset.setDuration_time(String.valueOf(numberPicker_hr.getValue())+":"+newVal);
                }else{
                    tv_time_out.setText(String.valueOf(numberPicker_hr.getValue())+" ชัวโมง  "+ newVal+" นาที"); //set the value to textview
                    TimeDataset.setDuration_time(String.valueOf(numberPicker_hr.getValue())+":"+newVal);
                }

            }
        });

        Button bt_ok =(Button) dialog.findViewById(R.id.bt_ok);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set the value to textview
                if(numberPicker_hr.getValue()==0){
                    tv_time_out.setText(String.valueOf(numberPicker_mi.getValue())+" นาที");
                    TimeDataset.setDuration_time(String.valueOf(numberPicker_hr.getValue())+":"+ String.valueOf(numberPicker_mi.getValue()));
                }else{
                    tv_time_out.setText(String.valueOf(numberPicker_hr.getValue())+" ชัวโมง  "+ String.valueOf(numberPicker_mi.getValue())+" นาที");
                    TimeDataset.setDuration_time(String.valueOf(numberPicker_hr.getValue())+":"+ String.valueOf(numberPicker_mi.getValue()));
                }

                dialog.dismiss(); // dismiss the dialog
            }
        });

        dialog.show();

    }

    //########################################################################################

    private void AlertDialogViewTimeend() {

        Calendar cal1 = Calendar.getInstance(TimeZone.getDefault());
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                R.style.MyDialogTheme_Time, mOnTimeSetListener_Timeend,
                cal1.get(Calendar.HOUR_OF_DAY),
                cal1.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.setCancelable(false);
        timePickerDialog.setTitle("ออนไลน์ถึงเวลา");
        timePickerDialog.show();

    }
//########################################################################################
    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener1=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            mHour1 =hourOfDay;
            mMinute1 = minute;
            bt_time_Newsetting1.setText(
                    new StringBuilder()
                            .append(mHour1).append(":")
                            .append(mMinute1).append(" "));

            TimeDataset.setStart_time(bt_time_Newsetting1.getText().toString());
//            String hour = String.valueOf(hourOfDay);
//            String min = String.valueOf(minute);
            // bt_date2.setText(day2 + "/" + month2 + "/" + year2);

        }
    };

    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener2=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            mHour2 =hourOfDay;
            mMinute2 = minute;
            bt_time_Newsetting2.setText(
                    new StringBuilder()
                            .append(mHour2).append(":")
                            .append(mMinute2).append(" "));

            TimeDataset.setEnd_time(bt_time_Newsetting2.getText().toString());

//            String hour = String.valueOf(hourOfDay);
//            String min = String.valueOf(minute);
            // bt_date2.setText(day2 + "/" + month2 + "/" + year2);

        }
    };

    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener_Timeend = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            int mHour_timeend = hourOfDay;
            int mMinute1_timeend = minute;
            tv_timeend.setText(new StringBuilder()
                            .append(mHour_timeend).append(":")
                            .append(mMinute1_timeend).append(" ").append(" น."));
            to_time = tv_timeend.getText().toString();

        }
    };



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


    class AddNewstatusPlace extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            String place_id = params[0];
            String Name_status = params[1];
            String radius = params[2];
            String day = params[3];
            String start_time = params[4];
            String end_time = params[5];
            String online = params[6];
            String real_place = params[7];
            String offline = params[8];
            String offline_mode = params[9];
            String duration_time = params[10];
            String to_time = params[11];
            String out_place_online = params[12];
            String sharing_status = params[13];
            String secret_code = params[14];

            String Api_Key = "Mark13942180";
            MemberInformation memberInformation = new MemberInformation();
            String Token_key = memberInformation.getToken_key();
            String Member_id = memberInformation.getMember_id();

            String data = "";
            int tmp;

            try {


                URL url = new URL(URL_API.getAPI_add_statusPlace());
                String urlParams = "API_Key=" + Api_Key
                        + "&Token_key=" + Token_key
                        + "&Member_id=" + Member_id
                        + "&place_id=" + place_id
                        + "&name_status=" + Name_status
                        + "&radius=" + radius
                        + "&day=" + day
                        + "&start_time=" + start_time
                        + "&end_time=" + end_time
                        + "&online=" + online
                        + "&real_place=" + real_place
                        + "&offline=" + offline
                        + "&offline_mode=" + offline_mode
                        + "&duration_time=" + duration_time
                        + "&to_time=" + to_time
                        + "&out_place_online=" + out_place_online
                        + "&sharing_status=" + sharing_status
                        + "&secret_code=" + secret_code;

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
                JSONObject response = root.getJSONObject("AddStatusPlace_response");
                status = response.getString("status");
                if (status.equals("0")) {


                    Fragment fragment = new SettingAllPlaceFragment(place_id,place_name,name_add,mLatitude,mLongitude);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    s = "เพิ่มการตั้งค่าสถานที่เรียบร้อย";
                    Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();

                }if (status.equals("7")) {


                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("ชื่อเงือนไขนี้ถูกใช้งานแล้ว!"); //ไปที่เเวรูเเล้วก็สตริง
                    dialog.setIcon(R.drawable.logo);
                    dialog.setMessage("กรุณากรอกชื่อเงือนไขใหมอีกครั้ง");
                    dialog.setCancelable(true);

                    dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            et_nameNewsetting.setText("");

                        }
                    });

                    dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    dialog.show();

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
