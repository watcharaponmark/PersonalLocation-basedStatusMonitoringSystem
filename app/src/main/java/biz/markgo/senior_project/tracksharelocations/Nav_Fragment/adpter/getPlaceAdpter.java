package biz.markgo.senior_project.tracksharelocations.Nav_Fragment.adpter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            convertView =  vi.inflate(Resoure,null);
            holder =new ViewHolder();

           // holder.im_place =(ImageView) convertView.findViewById(R.id.im_litplace);
            holder.tv_place_name=(TextView)convertView.findViewById(R.id.tv_name_place);
            holder.tv_sub_district=(TextView)convertView.findViewById(R.id.tv_subdis);
            holder.tv_district=(TextView)convertView.findViewById(R.id.tv_district);
            holder.tv_province=(TextView)convertView.findViewById(R.id.tv_province);
            holder.tv_zip_code=(TextView)convertView.findViewById(R.id.tv_zip_code);
            holder.sharing_status=(TextView)convertView.findViewById(R.id.tv_sharing_status);


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
        holder.tv_sub_district.setText(placeArrayList.get(position).getSub_district());
        holder.tv_district.setText(placeArrayList.get(position).getDistrict());
        holder.tv_province.setText(placeArrayList.get(position).getProvince());
        holder.tv_zip_code.setText(placeArrayList.get(position).getZip_code());
        holder.sharing_status.setText(placeArrayList.get(position).getSharing_status());

        return convertView;
    }



    static  class ViewHolder{

       // public ImageView im_place;
        public TextView tv_place_name;
        public TextView tv_sub_district;
        public TextView tv_district;
        public TextView tv_province;
        public TextView tv_zip_code;
        public TextView sharing_status;
    }
}
