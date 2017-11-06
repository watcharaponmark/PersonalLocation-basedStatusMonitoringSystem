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

import biz.markgo.senior_project.tracksharelocations.R;


public class getPlaceTrackingAdpter extends ArrayAdapter<GetPlaceTracking> {

        ArrayList<GetPlaceTracking> placeArrayList;
        int Resoure;
        Context context;
        LayoutInflater vi;
public getPlaceTrackingAdpter(Context context, int resource,ArrayList<GetPlaceTracking> object) {
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
//        holder.tv_sub_district=(TextView)convertView.findViewById(R.id.tv_subdis);
//        holder.tv_district=(TextView)convertView.findViewById(R.id.tv_district);
        holder.tv_status=(TextView)convertView.findViewById(R.id.tv_status);
//        holder.tv_zip_code=(TextView)convertView.findViewById(R.id.tv_zip_code);
//        holder.sharing_status=(TextView)convertView.findViewById(R.id.tv_sharing_status);
            holder.bt_traking = (Button) convertView.findViewById(R.id.bt_traking);
            holder.ImageView= (ImageView) convertView.findViewById(R.id.im_litplace);


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
//        holder.tv_district.setText(placeArrayList.get(position).getDistrict());
//        holder.tv_zip_code.setText(placeArrayList.get(position).getZip_code());
//        holder.sharing_status.setText(placeArrayList.get(position).getSharing_status());




    if("Public".equals(placeArrayList.get(position).getSharing_status())){
        holder.bt_traking.setText("ติดตาม");
        holder.bt_traking.setTextColor(Color.BLACK);
        holder.bt_traking.setBackgroundColor(Color.GREEN);
        holder.ImageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.icon_public));
        holder.tv_status.setText("สาธารณะ");
    }else if("Private".equals(placeArrayList.get(position).getSharing_status())){
        holder.bt_traking.setText("ร้องขอ");
        holder.bt_traking.setTextColor(Color.WHITE);
        holder.bt_traking.setBackgroundColor(Color.RED);
        holder.ImageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.icon_private));
        holder.tv_status.setText("ส่วนตัว");

    }

    holder.bt_traking.setOnClickListener(new View.OnClickListener() {
         @Override
            public void onClick(View v) {
            ((ListView) parent).performItemClick(v,position,0);
            }
    });


    return convertView;
}



static  class ViewHolder{

    // public ImageView im_place;
    public TextView tv_place_name;
    public TextView tv_sub_district;
    public TextView tv_district;
    public TextView tv_status;
    public TextView tv_zip_code;
    public TextView sharing_status;
    public Button bt_traking;
    public ImageView ImageView;
}
}
