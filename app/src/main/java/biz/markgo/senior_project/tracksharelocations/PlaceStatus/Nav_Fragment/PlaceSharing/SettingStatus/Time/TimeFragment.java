package biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus.Time;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.TimeZone;

import biz.markgo.senior_project.tracksharelocations.R;

public class TimeFragment extends Fragment {

    private View myFragmentView;
    private Button bt_time_Newsetting1,bt_time_Newsetting2;
    private  int mHour1,mHour2;
    private  int mMinute1,mMinute2;
    private  String time1 = "เริ่มต้น";
    private  String time2 = "ส้นสุด";


    public TimeFragment(String time1,String time2) {
        this.time1 =time1;
        this.time2 =time2;
    }

    public TimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_time, container, false);
        bt_time_Newsetting1 = (Button) myFragmentView.findViewById(R.id.bt_time_Newsetting1);
        bt_time_Newsetting2 = (Button) myFragmentView.findViewById(R.id.bt_time_Newsetting2);


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


        return myFragmentView;
    }

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

}
