package biz.markgo.senior_project.tracksharelocations.Config.Profile;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import biz.markgo.senior_project.tracksharelocations.Begin.SelectModeActivity;
import biz.markgo.senior_project.tracksharelocations.Data.MemberInformation;
import biz.markgo.senior_project.tracksharelocations.Login.ChooseLoginActivity;
import biz.markgo.senior_project.tracksharelocations.Manifest;
import biz.markgo.senior_project.tracksharelocations.R;
import biz.markgo.senior_project.tracksharelocations.Traking.Nav_Fagment.PlaceTracking.ScanQRActivity;

public class QRCodeActivity extends AppCompatActivity {

    private  String TAG= QRCodeActivity.class.getSimpleName();
    public static int white = 0xFFFFFFFF;
    public static int black = 0xFF000000;
    public static int magenta = 0xffff00ff;
    public static int color_qr = 0xff284563;
    public static int color_qr2 = 0xffD36C28;


    Bitmap bmp, merge;
    private ImageView im_qrc;
    private Button bt_save_QR;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_config_QRCodet);
        toolbar.setTitle("คิวอาร์โค้ด");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        im_qrc = (ImageView) findViewById(R.id.im_qrc);
        bt_save_QR = (Button) findViewById(R.id.bt_save_QR);
//#####################################################################################
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//#####################################################################################
        //String url_QR_Code = "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=" + MemberInformation.getFollow_id();
        // String url_QR_Code = "https://chart.googleapis.com/chart?cht=qr&chl=+MemberInformation.getFollow_id()+&chs=250x250&chld=L|0";
//          String url_QR_Code = "http://chart.apis.google.com/chart?chs=250x250&cht=qr&chld=L|0&chl="+MemberInformation.getFollow_id();
//
//        Glide.with(this)
//                .load(url_QR_Code)
//                .thumbnail(0.5f)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(im_qrc);


        try {
            // bmp = mergeBitmaps(encodeAsBitmap(MemberInformation.getFollow_id()),R.drawable.logo);
            bmp = encodeAsBitmap(MemberInformation.getFollow_id());
//            im_qrc.setImageBitmap(bmp);
//            Bitmap myLogo = BitmapFactory.decodeResource(getResources(), R.drawable.cir001);
//            bitMerged = mergeBitmaps(bmp,myLogo);
//            im_qrc.setImageBitmap(bitMerged);

            Bitmap yourLogo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
            merge = mergeBitmaps(yourLogo, bmp);
            im_qrc.setImageBitmap(merge);

        } catch (WriterException e) {
            e.printStackTrace();
        }


        bt_save_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(QRCodeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 250);
                    }
                    return;
                }

                registerForContextMenu(bt_save_QR);
                openContextMenu(bt_save_QR);

            }

        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("คิวอาร์โค้ด");
        menu.add(0, v.getId(), 0, "แชร์");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "ส่งผ่านอีเมล");
        menu.add(0, v.getId(), 0, "บันทึก");

    }


    @Override
    public boolean onContextItemSelected(MenuItem item){

        String imgBitmapPath= MediaStore.Images.Media.insertImage(getContentResolver(),merge,"title",null);
        Uri imgBitmapUri=Uri.parse(imgBitmapPath);
        String text = "ดาวโหลดแอปได้ที่นี่ : https://play.google.com/store/apps/details?id=biz.markgo.senior_project.tracksharelocations";

        if(item.getTitle()=="แชร์"){

            //Toast.makeText(getApplicationContext(),"แชร์",Toast.LENGTH_LONG).show();
            Intent sharingIntent = new Intent();
            sharingIntent.setAction(Intent.ACTION_SEND);
            sharingIntent.setType("image/*");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "คิวอาร์โค้ดสำหรับติดตามสถานะ");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(sharingIntent, "แชร์"));

        }else if(item.getTitle()=="ส่งผ่านอีเมล"){
            //Toast.makeText(getApplicationContext(),"ส่งผ่านอีเมล",Toast.LENGTH_LONG).show();

                Intent sharingIntent = new Intent();
                sharingIntent.setAction(Intent.ACTION_SEND);
                sharingIntent.setType("message/rfc822");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "คิวอาร์โค้ดสำหรับติดตามสถานะ");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(sharingIntent, "ส่งทางอีเมล"));

        }else if(item.getTitle()=="บันทึก"){
            //Toast.makeText(getApplicationContext(),"บันทึก",Toast.LENGTH_LONG).show();

            Boolean saveImage = saveImage(merge,"QR-CodeMonitering");

                if (saveImage) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(QRCodeActivity.this);
                    dialog.setTitle("บันทึกคิวอาร์โค้ด"); //ไปที่เเวรูเเล้วก็สตริง
                    dialog.setIcon(R.drawable.logo);
                    dialog.setMessage("บันทึกคิวอาร์โค้ดเรียบร้อยแล้ว");
                    dialog.setCancelable(true);

                    dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    dialog.show();

                }
        }else{
            return false;
        }
        return true;
    }

    //#######################################################################################################
    //วาดภาพ QR
    public Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        Bitmap bitmap = null;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 500, 500, null);

            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? color_qr : white;
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        } catch (Exception iae) {
            iae.printStackTrace();
            return null;
        }
        return bitmap;
    }
    //#######################################################################################################
    //รวมภาพ QR กับ logo
    public Bitmap mergeBitmaps(Bitmap logo, Bitmap qrcode) {

        Bitmap combined = Bitmap.createBitmap(qrcode.getWidth(), qrcode.getHeight(), qrcode.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        canvas.drawBitmap(qrcode, new Matrix(), null);

        Bitmap resizeLogo = Bitmap.createScaledBitmap(logo, canvasWidth / 8, canvasHeight / 8, true);
        int centreX = (canvasWidth - resizeLogo.getWidth()) / 2;
        int centreY = (canvasHeight - resizeLogo.getHeight()) / 2;
        canvas.drawBitmap(resizeLogo, centreX, centreY, null);
        return combined;
    }

    //#######################################################################################################


    // บันทึกรูปภาพ
    private boolean saveImage(Bitmap finalBitmap, String image_name) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root+ "/Pictures/Location_Monotoring");
        myDir.mkdirs();
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        //updateImage(root+ "/Pictures/Location_Monotoring");
        String fname = "Image-" + image_name+"-"+currentDateTimeString+"-.png";
        File file = new File(myDir, fname);
        updateImage(file.getPath());
        if (file.exists())
            file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //ฟังงก์ชันที่เอาไว้อัปเดตไฟล์ให้กับ Media Scanner เพียงแค่กำหนด Path ของไฟล์ที่ต้องการอัปเดต
    public void updateImage(String path) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        intent.setData(Uri.fromFile(f));
        sendBroadcast(intent);
    }
//#######################################################################################################
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch (requestCode) {
        case 250:
            registerForContextMenu(bt_save_QR);
            openContextMenu(bt_save_QR);
            break;
        default:
            break;
    }
}

}










