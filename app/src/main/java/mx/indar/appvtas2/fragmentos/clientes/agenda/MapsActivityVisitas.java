package mx.indar.appvtas2.fragmentos.clientes.agenda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.VectorDrawable;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.indar.appvtas2.GPStracker;
import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.visita;

public class MapsActivityVisitas extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public  List<visita> listaVisita;
    public  Polyline line;
    public  List<Marker> marcadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_visitas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public List<visita> regresaVisitasVendedor()
    {
        dbAdapter db =new dbAdapter(this);
        List<visita> lista = new ArrayList<>();
        try {
            db.open(true);
            lista=db.visitasMapa();
            db.close(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        marcadores =new ArrayList<>();
        listaVisita=regresaVisitasVendedor();

        GPStracker g = new GPStracker(this);
        Location l = g.getLocation();
        LatLng Actual= new LatLng(l.getLatitude(),l.getLongitude());

        Marker marker1=mMap.addMarker(new MarkerOptions().position(Actual).title("Aqui Estoy").snippet("HOLA"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Actual,12));







        if(listaVisita.size()>0)
        {

            for(int i=0;i<listaVisita.size();i++) {
                    Log.i("map",listaVisita.get(i).getCliente()+""+listaVisita.get(i).getLatitud()+"*"+listaVisita.get(i).getLongitud());
                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
                Canvas canvas1 = new Canvas(bmp);

// paint defines the text color, stroke width and size
                Paint color = new Paint();
                color.setTextSize(35);
                color.setFakeBoldText(true);

                color.setColor(Color.WHITE);

// modify canvas
                canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.pin), 0,0, color);
                canvas1.drawText(i+"", 25, 35, color);
                LatLng posicion= new LatLng(listaVisita.get(i).getLatitud(),listaVisita.get(i).getLongitud());
                Marker marker =mMap.addMarker(new MarkerOptions().position(posicion).title(listaVisita.get(i).getCliente()).snippet(listaVisita.get(i).getFechaInicio()).icon(BitmapDescriptorFactory.fromBitmap(bmp)));
                marcadores.add(marker);
                if(i>=1)
                    mMap.addPolyline(new PolylineOptions().add(marcadores.get(i-1).getPosition(),marcadores.get(i).getPosition()).width(5).color(Color.RED));
            }
        }
        else // Snackbar.make(findViewById(android.R.id.content),"No tienes visitas",10);
            Toast.makeText(this, "No tienes visitas", Toast.LENGTH_SHORT).show();




    }
}
