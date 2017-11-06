package biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus.Time;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import biz.markgo.senior_project.tracksharelocations.R;


public class DurationFragment extends Fragment {
    private View myFragmentView;

    private LinearLayout layout_duration;
    private TextView tv_time;
    private String timer = "15 นาที";


    public DurationFragment(String timer) {
        this.timer = timer;
    }

    public DurationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_duration, container, false);

        layout_duration = (LinearLayout) myFragmentView.findViewById(R.id.layout_duration);
        tv_time = (TextView)  myFragmentView.findViewById(R.id.tv_time);

        if(timer.indexOf(":") == -1) {

            tv_time.setText(timer);

        }else{

            String H, M = "";
            H = timer.substring(0, timer.indexOf(":"));
            M = timer.substring(timer.indexOf(":") + 1);

            if(H.equals("0")){
                tv_time.setText(M+" นาที");
            }else{
                tv_time.setText(H +" ชัวโมง  "+ M+" นาที");
            }
            
        }
        TimeDataset.setDuration_time(timer);

        layout_duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_duration);

                final NumberPicker numberPicker_hr = (NumberPicker) dialog.findViewById(R.id.numberPicker_hr);
                final NumberPicker numberPicker_mi = (NumberPicker) dialog.findViewById(R.id.numberPicker_mi);

                numberPicker_hr.setMinValue(0);   // min value 0
                numberPicker_hr.setMaxValue(24); // max value 100
                numberPicker_hr.setWrapSelectorWheel(false);

                numberPicker_mi.setMinValue(0);   // min value 0
                numberPicker_mi.setMaxValue(60); // max value 60
                numberPicker_mi.setWrapSelectorWheel(false);

                String H1 = "",M1 = "";

                if(tv_time.getText().toString().indexOf(" ชัวโมง") == -1){
                    
                    M1  = tv_time.getText().toString().substring(0,tv_time.getText().toString().indexOf(" นาที"));

                    Log.e("HHHH",H1+"T"+M1);

                    numberPicker_hr.setValue(0);
                    numberPicker_mi.setValue(Integer.parseInt(M1));
               
                }else{
                    H1  = tv_time.getText().toString().substring(0,tv_time.getText().toString().indexOf(" ชัวโมง"));
                    M1  = tv_time.getText().toString().substring(tv_time.getText().toString().indexOf(" ชัวโมง")+9,tv_time.getText().toString().indexOf(" นาที"));
                    Log.e("HHHH",H1+"T"+M1);

                    numberPicker_hr.setValue(Integer.parseInt(H1));
                    numberPicker_mi.setValue(Integer.parseInt(M1));
                }


                numberPicker_hr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        if(newVal==0) {
                            tv_time.setText(String.valueOf(numberPicker_mi.getValue()) + " นาที"); //set the value to textview
                            TimeDataset.setDuration_time(newVal + ":" + String.valueOf(numberPicker_mi.getValue()));
                        }else{
                            tv_time.setText(newVal + " ชัวโมง  " + String.valueOf(numberPicker_mi.getValue()) + " นาที"); //set the value to textview
                            TimeDataset.setDuration_time(newVal + ":" + String.valueOf(numberPicker_mi.getValue()));
                        }

                    }
                });

                numberPicker_mi.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                        if(numberPicker_mi.getValue()==0) {
                            tv_time.setText(newVal+" นาที"); //set the value to textview
                            TimeDataset.setDuration_time(String.valueOf(numberPicker_hr.getValue())+":"+newVal);
                        }else{
                            tv_time.setText(String.valueOf(numberPicker_hr.getValue())+" ชัวโมง  "+ newVal+" นาที"); //set the value to textview
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
                            tv_time.setText(String.valueOf(numberPicker_mi.getValue())+" นาที");
                            TimeDataset.setDuration_time(String.valueOf(numberPicker_hr.getValue())+":"+ String.valueOf(numberPicker_mi.getValue()));
                        }else{
                            tv_time.setText(String.valueOf(numberPicker_hr.getValue())+" ชัวโมง  "+ String.valueOf(numberPicker_mi.getValue())+" นาที");
                            TimeDataset.setDuration_time(String.valueOf(numberPicker_hr.getValue())+":"+ String.valueOf(numberPicker_mi.getValue()));
                        }

                        dialog.dismiss(); // dismiss the dialog
                    }
                });

                dialog.show();

            }
        });



        return myFragmentView;
    }

}
