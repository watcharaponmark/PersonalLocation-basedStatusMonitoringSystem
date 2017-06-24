package biz.markgo.senior_project.tracksharelocations.Nav_Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import biz.markgo.senior_project.tracksharelocations.R;


public class TrackingFragment extends Fragment {
    private View myFragmentView;
    private Button bt_newtracking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_tracking, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ติดตามสถานที่");


        bt_newtracking=(Button) myFragmentView.findViewById(R.id.bt_newtracking);

        bt_newtracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new NewTrackingFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_for_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });




        return myFragmentView;
    }


}
