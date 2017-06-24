package biz.markgo.senior_project.tracksharelocations.Nav_Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biz.markgo.senior_project.tracksharelocations.R;


public class NewTrackingFragment extends Fragment {

    private View myFragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_new_tracking, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("เพิ่มการติดตาม");

        return myFragmentView;
    }

}
