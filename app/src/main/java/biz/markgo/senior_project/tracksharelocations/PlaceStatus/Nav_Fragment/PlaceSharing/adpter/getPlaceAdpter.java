package biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.adpter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import biz.markgo.senior_project.tracksharelocations.R;


public class getPlaceAdpter extends ArrayAdapter<GetPlace>{

    ArrayList<GetPlace> placeArrayList;
    int Resoure;
    Context context;
    LayoutInflater vi;

    public getPlaceAdpter(Context context, int resource,ArrayList<GetPlace> object) {

        super(context, resource,object);
        placeArrayList=object;
        Resoure =resource;
        this.context=context;
        vi=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            convertView =  vi.inflate(Resoure,null);
            holder =new ViewHolder();

           // holder.im_place =(ImageView) convertView.findViewById(R.id.im_litplace);
            holder.tv_place_name=(TextView)convertView.findViewById(R.id.tv_name_place);
//            holder.tv_sub_district=(TextView)convertView.findViewById(R.id.tv_subdis);
//            holder.tv_district=(TextView)convertView.findViewById(R.id.tv_district);
        //   holder.tv_province=(TextView)convertView.findViewById(R.id.tv_province);
//            holder.tv_zip_code=(TextView)convertView.findViewById(R.id.tv_zip_code);
       //     holder.sharing_status=(TextView)convertView.findViewById(R.id.tv_sharing_status);
            holder.bt_setting = (Button) convertView.findViewById(R.id.bt_setting);
//            holder.bt_delete = (Button) convertView.findViewById(R.id.bt_delete);

            holder.CV_place= (CardView) convertView.findViewById(R.id.CV_place);
            holder.Layout_place =  (LinearLayout) convertView.findViewById(R.id.Layout_place);

        convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }

//        Double lat= Double.parseDouble(placeArrayList.get(position).getLatitude());
//        Double lng= Double.parseDouble(placeArrayList.get(position).getLongitude());
//
//        LatLng coordinate = new LatLng(lat,lng);
//        holder.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 17));
//        MarkerOptions options = new MarkerOptions()
//                .title("ตำแหน่งของคุณ")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//                .position(new LatLng(lat,lng));
//        holder.mMarker = holder.mMap.addMarker(options);

        //holder.im_place.setImageResource(R.drawable.logo);
        holder.tv_place_name.setText(placeArrayList.get(position).getPlace_name());
//        holder.tv_sub_district.setText(placeArrayList.get(position).getSub_district());
  //      holder.tv_district.setText(placeArrayList.get(position).getDistrict());
     //  holder.tv_province.setText(placeArrayList.get(position).getProvince());
//        holder.tv_zip_code.setText(placeArrayList.get(position).getZip_code());
   //     holder.sharing_status.setText(placeArrayList.get(position).getSharing_status());

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


//        holder.Layout_place.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((ListView) parent).performItemClick(v,position,0);
//            }
//        });

        holder.CV_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v,position,0);
            }
        });

//        holder.CV_place.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                ((ListView) parent).performItemClick(v,position,0);
//                return false;
//            }
//        });


        return convertView;

    }

    static  class ViewHolder{

       // public ImageView im_place;
        public TextView tv_place_name;
        public TextView tv_sub_district;
//        public TextView tv_district;
//        public TextView tv_province;
        public TextView tv_zip_code;
  //      public TextView sharing_status;
        public Button  bt_setting;
//        public Button  bt_delete;
        public CardView CV_place;
        public LinearLayout Layout_place;

    }
}
