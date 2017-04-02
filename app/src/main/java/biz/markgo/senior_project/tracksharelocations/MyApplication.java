package biz.markgo.senior_project.tracksharelocations;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        PackageInfo info;
        try {
             info = getPackageManager().getPackageInfo(
                    "biz.markgo.senior_project.tracksharelocations", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String smoething =new String(Base64.encode(md.digest(),0));
                Log.e("KeyHash:", smoething);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found",e1.toString());

        } catch (NoSuchAlgorithmException e) {
             Log.e("no such an algoritrihm",e.toString());
        } catch (Exception e){
            Log.e("Exception",e.toString());
        }
    }
}
