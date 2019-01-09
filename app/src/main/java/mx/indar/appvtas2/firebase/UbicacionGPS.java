package mx.indar.appvtas2.firebase;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class UbicacionGPS extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    public Location Ubicacion;
   public boolean gpsOK=false;
    Context context;

    public UbicacionGPS(Context context) {
      this.context=context;

    }

    @SuppressLint("MissingPermission")
    public  void inicializar()
    {

            //if permission is grated
            buidLocationRequest();
            buildLocationCallBack();
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
            //REVISAR PERMISOS  , YO LO PONGO IGNORAR PERO POSIBLE ERROR SI NO SE AUTORIZAN LOS PERMISOS DE GEOLOCALIZACION
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

    }





    private void buildLocationCallBack() {
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location location:locationResult.getLocations())
                    // txt.setText(String.valueOf(location.getLatitude())+"/"+String.valueOf(location.getLongitude()));
                    Ubicacion=location;
                gpsOK=true;
                Log.i("gps",Ubicacion.getLongitude()+" desde OnlocationResult");

            }
        };

    }

    private void buidLocationRequest() {
        locationRequest= new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setSmallestDisplacement(10);
    }


}
