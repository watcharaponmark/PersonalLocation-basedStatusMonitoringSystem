package biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
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

import biz.markgo.senior_project.tracksharelocations.API.URL_API;
import biz.markgo.senior_project.tracksharelocations.ChangePage.BackButtonHandlerInterface;
import biz.markgo.senior_project.tracksharelocations.ChangePage.OnBackClickListener;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus.SettingAllPlaceFragment;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.adpter.GetPlace;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.adpter.getPlaceAdpter;
import biz.markgo.senior_project.tracksharelocations.R;


public class PlaceFragment extends Fragment implements OnBackClickListener {

    private String TAG= PlaceFragment.class.getSimpleName();
    private BackButtonHandlerInterface backButtonHandler;
    private View myFragmentView;
    private Button bt_newPlace,bt_back;
    private ProgressDialog loading;
    private JSONArray Place_Information;

     ListView listView_place;
     getPlaceAdpter adpter;
    ArrayList<GetPlace> getplaceList;

    String Token_key,Member_id;
    getPlace getPlace;
    private AlertDialog alertDialog1;
    GetPlace  itemValue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_place, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("โหมดกำหนดสถานะ");

        bt_newPlace =(Button) myFragmentView.findViewById(R.id.bt_newplace);
        //bt_back=(Button)  myFragmentView.findViewById(R.id.bt_back_place);
        listView_place=(ListView) myFragmentView.findViewById(R.id.listView_place);
        getplaceList=new ArrayList<GetPlace>();

//########################################################################################

        MemberInformation memberInformation = new MemberInformation();
         Token_key = memberInformation.getToken_key();
         Member_id = memberInformation.getMember_id();
        //loading = ProgressDialog.show(getContext(),"loading...","Please wait...",false,false);
       // loading = ProgressDialog.show(getContext(), "", "Loading. Please wait...", true);
//########################################################################################
        loading = new ProgressDialog(getActivity());
        loading.setTitle("กำลังโหลดข้อมูล....");
        loading.setMessage("กรุณารอสักครู...");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
//########################################################################################
        getPlace getPlace=new getPlace();
        getPlace.execute(Token_key,Member_id);
//########################################################################################
//        listView_place.setOnItemClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "test "+ getplaceList.get(position).getCountry_code(), Toast.LENGTH_SHORT).show();
//            }
//        });
//########################################################################################

        listView_place.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewID = view.getId();
                itemValue  = (GetPlace) parent.getItemAtPosition(position);

               if(viewID == R.id.CV_place) {

                   setting_bunton();

               }else if(viewID == R.id.bt_setting){

                   setting_bunton();

                }
            }
        });


//########################################################################################
        bt_newPlace.setOnClickListener(new View.OnClickListener() {
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

//        FloatingActionButton fab_newplace = (FloatingActionButton) myFragmentView.findViewById(R.id.fab_newplace);
//        fab_newplace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Fragment fragment = new NewPlaceFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });



        return myFragmentView;
    }
    //################################################################################################################

    public  void setting_bunton(){
        //Toast.makeText(getContext(), itemValue.getPlace_name(), Toast.LENGTH_SHORT).show();
        //registerForContextMenu(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyAlertDialogStyle);

        builder.setTitle(itemValue.getPlace_name());
        //builder.setTitle( Html.fromHtml("<font color='#FF7F27'>"+itemValue.getPlace_name()+"</font>"));
        builder.setItems(new CharSequence[] {"ตั้งค่า", "ลบ"} , new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){

                    Fragment fragment = new SettingAllPlaceFragment(itemValue.getPlace_id(),itemValue.getPlace_name(),itemValue.getSub_district(),itemValue.getLatitude(),itemValue.getLongitude());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }else if(which == 1){

                    AlertDialog.Builder dialog1 = new AlertDialog.Builder(getContext());
                    dialog1.setTitle("ลบสถานที่ ?");

                    dialog1.setMessage(itemValue.getPlace_name());
                    dialog1.setIcon(R.drawable.logo);

                    dialog1.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

                    dialog1.setPositiveButton("ตกลง" ,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DeletePlace deletePlace=new DeletePlace();
                            deletePlace.execute(Token_key,Member_id,itemValue.getPlace_id());

                        }
                    });
                    alertDialog1 = dialog1.create();
                    alertDialog1.show();

                }

            }
        });

        builder.show();
    }

    //################################################################################################################
//context manu
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
//    {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.setHeaderTitle("Select The Action");
//        menu.add(0, v.getId(), 0, "Call");//groupId, itemId, order, title
//        menu.add(0, v.getId(), 0, "SMS");
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item){
//        if(item.getTitle()=="Call"){
//            Toast.makeText(getContext(),"calling code",Toast.LENGTH_LONG).show();
//        }
//        else if(item.getTitle()=="SMS"){
//            Toast.makeText(getContext(),"sending sms code",Toast.LENGTH_LONG).show();
//        }else{
//            return false;
//        }
//        return true;
//    }

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


    class getPlace extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            String Token_key = params[0];
            String Member_id = params[1];
            String Api_Key = "getplace1234";


            String data = "";
            int tmp;
            try {

                URL url = new URL(URL_API.getAPI_get_place());
                String urlParams ="API_Key="+Api_Key
                        +"&Token_key="+Token_key
                        +"&Member_id="+Member_id;

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
                JSONObject getPlace = root.getJSONObject("getPlace_response");
                status = getPlace.getString("status");

                if(status.equals("0")) {
                    Place_Information = getPlace.getJSONArray("Place_Information");
                    Log.d(TAG,"llllllllllllllllllllllllllll"+Place_Information.length());
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
    //                  place.setRadius(jRedobject.getString("radius"));
                   //     place.setSharing_status(jRedobject.getString("sharing_status"));


                        getplaceList.add(place);
                        adpter=new getPlaceAdpter(getContext(),R.layout.row_place,getplaceList);
                        listView_place.setAdapter(adpter);
                        loading.dismiss();


                    }
//                    s = "select Place";
//                    Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                }else if(status.equals("1")){
                    loading.dismiss();
                    s = "คุณยังไม่มีข้อมูลการแบ่งปันสถานที่";
                    Toast toast = Toast.makeText(getContext(),s, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                }
                loading.dismiss();

            } catch (JSONException e) {
                loading.dismiss();
                e.printStackTrace();
                err = "Exception: " + e.getMessage();

            }

        }


    }

    class DeletePlace extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            String Token_key = params[0];
            String Member_id = params[1];
            String Place_id =  params[2];
            String Api_Key = "DeletePlace1234";
            String data = "";
            int tmp;
            try {

                URL url = new URL(URL_API.getAPI_delete_place());
                String urlParams ="API_Key="+Api_Key
                        +"&Token_key="+Token_key
                        +"&Member_id="+Member_id
                        +"&place_id="+Place_id;

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
                JSONObject getPlace = root.getJSONObject("DeletePlace_response");
                status = getPlace.getString("status");

                if(status.equals("0")) {

                    s = "ลบสถานที่เรียบร้อย";
                    Toast toast = Toast.makeText(getContext(),s, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                    Fragment fragment = new PlaceFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.place_status_for_fragment, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }else if(status.equals("1")){

                    s = "ลบสถานที่ไม่สำเร็จ";
//                    Toast toast = Toast.makeText(getContext(),s, Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
//                    toast.show();

                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

                }


            } catch (JSONException e) {

                e.printStackTrace();
                err = "Exception: " + e.getMessage();

            }

        }


    }




}
