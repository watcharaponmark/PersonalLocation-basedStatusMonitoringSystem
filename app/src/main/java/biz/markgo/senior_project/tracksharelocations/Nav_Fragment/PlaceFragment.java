package biz.markgo.senior_project.tracksharelocations.Nav_Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import biz.markgo.senior_project.tracksharelocations.Nav_Fragment.adpter.GetPlace;
import biz.markgo.senior_project.tracksharelocations.Nav_Fragment.adpter.getPlaceAdpter;
import biz.markgo.senior_project.tracksharelocations.Place.DetailPlaceActivity;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.User_Data;



public class PlaceFragment extends Fragment {

    private String TAG= PlaceFragment.class.getSimpleName();
    private View myFragmentView;
    private Button bt_newPlace;
    private JSONArray Place_Information;
     ListView listView_place;
     getPlaceAdpter adpter;
    ArrayList<GetPlace> getplaceList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_place, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("แบ่งปันสถานที่");

        bt_newPlace =(Button) myFragmentView.findViewById(R.id.bt_newplace);
        listView_place=(ListView) myFragmentView.findViewById(R.id.listView_place);
        getplaceList=new ArrayList<GetPlace>();


        getPlace getPlace=new getPlace();
        getPlace.execute(User_Data.getAccount_id(),User_Data.getStatusLogin());


        listView_place.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(getActivity(),DetailPlaceActivity.class);
                //based on item add info to intent
                startActivity(intent);


            }


        });

        bt_newPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new NewPlaceFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_for_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });





        return myFragmentView;
    }

    class getPlace extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            String Account_id = params[0];
            String StatusLogin = params[1];
            String Api_Key = "getplace1234";
            String data = "";
            int tmp;

            try {

                URL url = new URL("http://api-location-monitoring.markgo.biz/place/get_place.php");
                String urlParams ="API_Key="+Api_Key
                        +"&account_id="+Account_id
                        +"&StatusLogin="+StatusLogin;

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
                JSONObject getPlace = root.getJSONObject("getPlace_response");
                status = getPlace.getString("status");
                Place_Information = getPlace.getJSONArray("Place_Information");

                Log.d(TAG,"llllllllllllllllllllllllllll"+Place_Information.length());

                if(status.equals("0")) {
                    for (int i=0;i<Place_Information.length();++i){

                        GetPlace place =new GetPlace();
                        JSONObject jRedobject= Place_Information.getJSONObject(i);

                        place.setMember_id(jRedobject.getString("member_id"));
                        place.setPlace_id(jRedobject.getString("place_id"));
                        place.setPlace_name(jRedobject.getString("place_name"));
                        place.setSub_district(jRedobject.getString("sub_district"));
                        place.setDistrict(jRedobject.getString("district"));
                        place.setProvince(jRedobject.getString("province"));
                        place.setZip_code(jRedobject.getString("zip_code"));
                        place.setCountry_code(jRedobject.getString("country_code"));
                        place.setLatitude(jRedobject.getString("latitude"));
                        place.setLongitude(jRedobject.getString("longitude"));
                        place.setRadius(jRedobject.getString("radius"));
                        place.setSharing_status(jRedobject.getString("sharing_status"));

//                        String member_id =  jRedobject.getString("member_id");
//                        String place_id =  jRedobject.getString("place_id");
//                        String place_name =  jRedobject.getString("place_name");
//                        String sub_district =  jRedobject.getString("sub_district");
//                        String district =  jRedobject.getString("district");
//                        String province =  jRedobject.getString("province");
//                        String zip_code =  jRedobject.getString("zip_code");
//                        String country_code =  jRedobject.getString("country_code");
//                        String latitude =  jRedobject.getString("latitude");
//                        String longitude =  jRedobject.getString("longitude");
//                        String radius =  jRedobject.getString("radius");
//                        String sharing_status =  jRedobject.getString("sharing_status");

                        getplaceList.add(place);

                        adpter=new getPlaceAdpter(getContext(),R.layout.row_place,getplaceList);
                        listView_place.setAdapter(adpter);

                    }
//                    s = "select Place";
//                    Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
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
