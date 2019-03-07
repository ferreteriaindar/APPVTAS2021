package mx.indar.appvtas2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class GPStracker implements LocationListener {

    Context context;
   public LocationManager lm;
    public  GPStracker (Context context)
    {
        this.context=context;
    }





    public Location getLocation()
    {
        if(ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permision not Granted", Toast.LENGTH_SHORT).show();
            return null;
        }
         lm =(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnable=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnable)
        {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,1,this);
            Location l =lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
          //  Log.i("gps",l.getLongitude()+l.getLatitude()+"");
            return l;
        }else {
            Toast.makeText(context, "Habilita el GPS", Toast.LENGTH_SHORT).show();
        }
        return  null;
    }


    public  void apagarGPS()
    {
        lm.removeUpdates(this);
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.i("GPS",location.getLatitude()+"changed");
        Log.i("GPS",location.getLongitude()+"changed");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
