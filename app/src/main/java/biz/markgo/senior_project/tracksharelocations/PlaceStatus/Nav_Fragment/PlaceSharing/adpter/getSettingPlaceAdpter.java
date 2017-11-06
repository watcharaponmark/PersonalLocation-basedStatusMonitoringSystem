package biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.adpter;

        import android.content.Context;
        import android.os.AsyncTask;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.widget.CardView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.CompoundButton;
        import android.widget.ListView;
        import android.widget.Switch;
        import android.widget.TextView;
        import android.widget.Toast;

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
        import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
        import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus.SettingAllPlaceFragment;
        import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus.SettingStatusFragment;
        import biz.markgo.senior_project.tracksharelocations.R;


public class getSettingPlaceAdpter extends ArrayAdapter<GetSettingPlace> {

    ArrayList<GetSettingPlace> placeArrayList;
    int Resoure;
    Context context;
    LayoutInflater vi;

    int StatusOnOff;

    public getSettingPlaceAdpter(Context context, int resource,ArrayList<GetSettingPlace> object) {
        super(context, resource,object);
        placeArrayList=object;
        Resoure =resource;
        this.context=context;
        vi=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder holder;
        if(convertView == null){
            convertView =  vi.inflate(Resoure,null);
            holder =new ViewHolder();

            // holder.im_place =(ImageView) convertView.findViewById(R.id.im_litplace);
       //     holder.tv_No=(TextView)convertView.findViewById(R.id.tv_No);
            holder.tv_name_status=(TextView)convertView.findViewById(R.id.tv_name_status);
            holder.bt_setting = (Button) convertView.findViewById(R.id.bt_setting);
//            holder.bt_delete = (Button) convertView.findViewById(R.id.bt_delete);
            holder.CV_allplase= (CardView) convertView.findViewById(R.id.CV_allplase);
          //  holder.tv_sharing_status=(TextView) convertView.findViewById(R.id.tv_sharing_status);
            holder.switch_OnOff=(Switch) convertView.findViewById(R.id.switch_OnOff);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }

//        String No = Integer.toString(position+1)+"." ;
//
//        holder.tv_No.setText(No);

        holder.tv_name_status.setText(placeArrayList.get(position).getName_status());
//        holder.tv_sharing_status.setText(placeArrayList.get(position).getSharing_status());

        holder.bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v,position,0);
            }
        });
//
//        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((ListView) parent).performItemClick(v,position,0);
//            }
//        });

        holder.CV_allplase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v,position,0);
            }
        });

        if(holder.switch_OnOff != null){

            holder.switch_OnOff.setOnCheckedChangeListener(null);
            if(placeArrayList.get(position).getStatus_display().equals("ON")) {
                holder.switch_OnOff.setChecked(true);
            }else{
                holder.switch_OnOff.setChecked(false);
            }

            holder.switch_OnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //((ListView) parent).performItemClick(buttonView,position,0);
                    if (isChecked) {
                        placeArrayList.get(position).setStatus_display("ON");
                        holder.switch_OnOff.setChecked(isChecked);
                        StatusOnOff = 0;
                        UpdateStatusPlace updateStatusPlace1 = new UpdateStatusPlace();
                        updateStatusPlace1.execute( placeArrayList.get(position).getPlace_status_id(),
                                placeArrayList.get(position).getName_status(),
                                placeArrayList.get(position).getStatus_display(),
                                placeArrayList.get(position).getRadius(),
                                placeArrayList.get(position).getDay(),
                                placeArrayList.get(position).getStart_time(),
                                placeArrayList.get(position).getEnd_time(),
                                placeArrayList.get(position).getOnline(),
                                placeArrayList.get(position).getReal_place(),
                                placeArrayList.get(position).getOffline(),
                                placeArrayList.get(position).getOffline_mode(),
                                placeArrayList.get(position).getDuration_time(),
                                placeArrayList.get(position).getTo_time(),
                                placeArrayList.get(position).getOut_place_online(),
                                placeArrayList.get(position).getSharing_status(),
                                placeArrayList.get(position).getSecret_code());
                    }else{
                        placeArrayList.get(position).setStatus_display("OFF");
                        holder.switch_OnOff.setChecked(isChecked);
                        StatusOnOff = 1;

                        UpdateStatusPlace updateStatusPlace2 = new UpdateStatusPlace();
                        updateStatusPlace2.execute( placeArrayList.get(position).getPlace_status_id(),
                                placeArrayList.get(position).getName_status(),
                                placeArrayList.get(position).getStatus_display(),
                                placeArrayList.get(position).getRadius(),
                                placeArrayList.get(position).getDay(),
                                placeArrayList.get(position).getStart_time(),
                                placeArrayList.get(position).getEnd_time(),
                                placeArrayList.get(position).getOnline(),
                                placeArrayList.get(position).getReal_place(),
                                placeArrayList.get(position).getOffline(),
                                placeArrayList.get(position).getOffline_mode(),
                                placeArrayList.get(position).getDuration_time(),
                                placeArrayList.get(position).getTo_time(),
                                placeArrayList.get(position).getOut_place_online(),
                                placeArrayList.get(position).getSharing_status(),
                                placeArrayList.get(position).getSecret_code());
                    }

                }
            });
        }

        return convertView;
    }



    static  class ViewHolder{

        // public ImageView im_place;
        public TextView tv_name_status;
       // public TextView tv_No;
     //   public TextView tv_sharing_status;
        public Button  bt_setting;
//        public Button  bt_delete;
        public Switch switch_OnOff;
        public CardView CV_allplase;
    }

    class UpdateStatusPlace extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String place_status_id = params[0];
            String Name_status = params[1];
            String status_display = params[2];
            String radius = params[3];
            String day = params[4];
            String start_time = params[5];
            String end_time = params[6];
            String online = params[7];
            String real_place = params[8];
            String offline = params[9];
            String offline_mode = params[10];
            String duration_time = params[11];
            String to_time = params[12];
            String out_place_online = params[13];
            String sharing_status = params[14];
            String secret_code = params[15];



            String Api_Key = "UpdateStatusPlace1234";
            MemberInformation memberInformation = new MemberInformation();
            String Token_key = memberInformation.getToken_key();
            String Member_id = memberInformation.getMember_id();

            String data = "";
            int tmp;

            try {

                URL url = new URL(URL_API.getAPI_update_statusplace());
                String urlParams = "API_Key=" + Api_Key
                        + "&Token_key=" + Token_key
                        + "&Member_id=" + Member_id
                        + "&place_status_id=" + place_status_id
                        + "&name_status=" + Name_status
                        + "&status_display=" + status_display
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
                JSONObject response = root.getJSONObject("UpdateStatusPlace_response");
                status = response.getString("status");
                if (status.equals("0")) {

                    if(StatusOnOff==0) {
                        s = "เปิดการใช้งานเรียบร้อย";
                    }else if(StatusOnOff==1) {
                        s = "ปิดการใช้งานเรียบร้อย";
                    }

                    Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
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
