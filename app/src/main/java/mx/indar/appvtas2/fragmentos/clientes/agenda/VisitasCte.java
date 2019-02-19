package mx.indar.appvtas2.fragmentos.clientes.agenda;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.List;

import mx.indar.appvtas2.GPStracker;
import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.MenuClientes.visitasHistorico;

public class VisitasCte extends AppCompatActivity implements OnMapReadyCallback {
    private  DatePickerDialog.OnDateSetListener mDateSetListener;
    public   int year,month,day,años,mes,dia;
    TextView  txtfecha;

    private GoogleMap mMap;
    public List<visitasHistorico> listaVisita;
    public Polyline line;
    public  List<Marker> marcadores;
    //private MapView mapView;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitas_cte);
       mapFragment  = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);

          txtfecha = findViewById(R.id.txtVisitasfecha);
        txtfecha .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                year=cal.get(Calendar.YEAR);
                month=cal.get(Calendar.MONTH);
                day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        VisitasCte.this,android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener,year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                txtfecha.setText(day+"/"+(month+1)+"/"+year);
                dbAdapter db = new dbAdapter(getApplicationContext());
                try {
                    db.open(true);
                    listaVisita=db.selectVisitasHistorico(year + "-" +(month+1<10?("0"+(month+1)):(month+1))+ "-" + (day<10?("0"+day):(day)),"fecha","");
                    db.close(true);
                    Log.i("map",listaVisita.size()+"");
                    mapFragment.getMapAsync(VisitasCte.this);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        marcadores =new ArrayList<>();

        GPStracker g = new GPStracker(this);
        Location l = g.getLocation();
     //   LatLng Actual= new LatLng(l.getLatitude(),l.getLongitude());

       // Marker marker1=mMap.addMarker(new MarkerOptions().position(Actual).title("Aqui Estoy").snippet("HOLA"));
    //    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Actual,12));






            mMap.clear();
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marcadores.get(marcadores.size()-1).getPosition(),12));
        }
        else // Snackbar.make(findViewById(android.R.id.content),"No tienes visitas",10);
            Toast.makeText(this, "No tienes visitas", Toast.LENGTH_SHORT).show();


    }
}
