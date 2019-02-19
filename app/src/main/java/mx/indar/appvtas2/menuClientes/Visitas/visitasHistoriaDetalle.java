package mx.indar.appvtas2.menuClientes.Visitas;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.VerticalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.indar.appvtas2.GPStracker;
import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.MenuClientes.visitasHistorico;
import mx.indar.appvtas2.fragmentos.clientes.agenda.VisitasCte;

public class visitasHistoriaDetalle extends AppCompatActivity implements OnMapReadyCallback {
    VerticalStepView hStepView;
    public List<StepBean> stepsBeanList = new ArrayList<>();
    public StepBean stepBean0;
    public StepBean stepBean1;
    public StepBean stepBean2;
    public StepBean stepBean3;
    public StepBean stepBean4;
    String cliente,fechaInicio;
    List<visitasHistorico> Listavh = new ArrayList<>();
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cliente=getIntent().getStringExtra("cliente");
        fechaInicio=getIntent().getStringExtra("fechaInicio");
        setContentView(R.layout.activity_visitas_historia_detalle);
        mapFragment  = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapvisitaHistoriaDetalle);

        dbAdapter db = new dbAdapter(getApplicationContext());
        try {
            db.open(true);

            Listavh=db.selectVisitasHistorico(fechaInicio,"detalle",cliente);
            db.close(true);
            mapFragment.getMapAsync(visitasHistoriaDetalle.this);

            hStepView = (VerticalStepView) findViewById(R.id.verticalStepView);

            List<String> list0 = new ArrayList<>();
            if(Listavh.get(0).getFechaInicio().isEmpty())
            {list0.add("Inicio");

            }
            else {
                list0.add("Inicio: " + Listavh.get(0).getFechaInicio());

            }
            if(Listavh.get(0).getFechaCobranza().isEmpty()) {
                list0.add("Cobranza");

            }
            else
            {
                list0.add("Cobranza: "+Listavh.get(0).getFechaCobranza());

            }
            if(Listavh.get(0).getFechaPromociones().isEmpty())
            {
                list0.add("Promo");

            }
            else
            {
                list0.add("Promo: "+Listavh.get(0).getFechaPromociones());

            }
            if ( Listavh.get(0).getFechaVenta()==null) {
                list0.add("Pedido");

            }
            else
            {
                list0.add("Pedido "+Listavh.get(0).getFechaVenta());

            }
           // hStepView.setStepsViewIndicatorComplectingPosition(2);
          /*  stepsBeanList.add(stepBean0);
            stepsBeanList.add(stepBean1);
            stepsBeanList.add(stepBean2);
            stepsBeanList.add(stepBean3);
            stepsBeanList.add(stepBean4); */
            hStepView.setStepViewTexts(list0)//总步骤
                    .reverseDraw(false)//default is true
                    .setTextSize(14)
                    .setStepViewTexts(list0)//总步骤
                    .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white))//设置StepsViewIndicator完成线的颜色
                    .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getApplicationContext(), R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                    .setStepViewComplectedTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white))//设置StepsView text完成线的颜色
                    .setStepViewUnComplectedTextColor(ContextCompat.getColor(getApplicationContext(), R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                    .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                    .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                    .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.attention));//设置StepsViewIndicator AttentionIcon
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(isOnline()) {
            GPStracker g = new GPStracker(this);
            Location l = g.getLocation();
            LatLng Actual = new LatLng(Listavh.get(0).getLatitud(), Listavh.get(0).getLongitud());

            Marker marker1 = mMap.addMarker(new MarkerOptions().position(Actual).title("Inicio").snippet(""));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Actual, 12));
        }
        else {
            Log.i("map", "si entra");
            View view =getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar
                .make(view,"El mapa necesita internet",Snackbar.LENGTH_LONG);
            snackbar.show();
            mapFragment.getView().setVisibility(View.INVISIBLE);
        }
    }
}
