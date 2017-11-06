package biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking.adpter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.adpter.GetPlace;
import biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.adpter.getPlaceAdpter;
import biz.markgo.senior_project.tracksharelocations.R;

/**
 * Created by macintosh on 9/23/2017 AD.
 */

public class getPlaceTrakingAllAdpter extends ArrayAdapter<GetPlaceTrackingAll> {

    ArrayList<GetPlaceTrackingAll> placeTrakingArrayList;
    int Resoure;
    Context context;
    LayoutInflater vi;

    public getPlaceTrakingAllAdpter(Context context, int resource,ArrayList<GetPlaceTrackingAll> object) {

        super(context, resource,object);
        placeTrakingArrayList=object;
        Resoure =resource;
        this.context=context;
        vi=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        getPlaceTrakingAllAdpter.ViewHolder holder;
        if(convertView == null){
            convertView =  vi.inflate(Resoure,null);
            holder =new getPlaceTrakingAllAdpter.ViewHolder();

            holder.im_litplace =(ImageView) convertView.findViewById(R.id.im_litplace);
            holder.tv_place_name=(TextView)convertView.findViewById(R.id.tv_name_place);
//            holder.tv_sub_district=(TextView)convertView.findViewById(R.id.tv_subdis);
//            holder.tv_district=(TextView)convertView.findViewById(R.id.tv_district);
            holder.tv_province=(TextView)convertView.findViewById(R.id.tv_province);
//            holder.tv_zip_code=(TextView)convertView.findViewById(R.id.tv_zip_code);
//     holder.sharing_status=(TextView)convertView.findViewById(R.id.tv_sharing_status);
// holder.bt_edit = (Button) convertView.findViewById(R.id.bt_edit);
            holder.bt_delete = (Button) convertView.findViewById(R.id.bt_delete);


            convertView.setTag(holder);
        }else{
            holder =(getPlaceTrakingAllAdpter.ViewHolder) convertView.getTag();
        }

        if("allow".equals(placeTrakingArrayList.get(position).getTracking_status())){

            holder.im_litplace.setImageResource(R.drawable.offline_button);

        }else if("Disallow".equals(placeTrakingArrayList.get(position).getTracking_status())){

            holder.im_litplace.setImageResource(R.drawable.icon_cancel_trking);

        }
        //holder.im_place.setImageResource(R.drawable.logo);
        holder.tv_place_name.setText(placeTrakingArrayList.get(position).getPlace_name());
//        holder.tv_sub_district.setText(placeArrayList.get(position).getSub_district());
//      holder.tv_district.setText(placeArrayList.get(position).getDistrict());
        holder.tv_province.setText(placeTrakingArrayList.get(position).getProvince());
//        holder.tv_zip_code.setText(placeArrayList.get(position).getZip_code());
//     holder.sharing_status.setText(placeArrayList.get(position).getSharing_status());

//        holder.bt_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((ListView) parent).performItemClick(v,position,0);
//            }
//        });

        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v,position,0);
            }
        });


        return convertView;
    }



    static  class ViewHolder{

        public ImageView im_litplace;
        public TextView tv_place_name;
        public TextView tv_sub_district;
        //        public TextView tv_district;
        public TextView tv_province;
        public TextView tv_zip_code;
        //      public TextView sharing_status;
        //public Button  bt_edit;
        public Button  bt_delete;
    }
}