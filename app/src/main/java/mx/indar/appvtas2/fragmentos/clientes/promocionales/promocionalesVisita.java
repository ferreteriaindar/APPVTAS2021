package mx.indar.appvtas2.fragmentos.clientes.promocionales;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.indar.appvtas2.Especificos;
import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.especificos;
import mx.indar.appvtas2.dbClases.visitaPromo;

public class promocionalesVisita extends AppCompatActivity {

    private LinearLayout llButtons;
    private  Integer avance;
    public  Switch switchPromocionales1,switchPromocionales2,switchPromocionales3,switchPromocionales4,switchPromocionales5,switchPromocionales6,ferreImpulsos,muestras;
   Button btnFinalizar;
    public  long idvisita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promocionales_visita);
        btnFinalizar=findViewById(R.id.btnPromoFinalizar);
        idvisita=getIntent().getLongExtra("idvisita",0);
        inicializarChecks();
        avance=0;




    }

    public  void terminaPromo(View view)
    {
        visitaPromo vp = new visitaPromo();
        vp.setIdvisitas((int)idvisita);
        vp.setFerreImpulsos(switchPromocionales1.isChecked());
        vp.setMuestras(switchPromocionales2.isChecked());
        vp.setEspecificoNombre1(switchPromocionales3.getText()+"");
        vp.setEspecifico1(switchPromocionales3.isChecked());
        vp.setEspecificoNombre2(switchPromocionales4.getText()+"");
        vp.setEspecifico2(switchPromocionales5.isChecked());
        vp.setEspecificoNombre3(switchPromocionales5.getText()+"");
        vp.setEspecifico3(switchPromocionales5.isChecked());
        vp.setEspecificoNombre4(switchPromocionales4.getText()+"");
        vp.setEspecifico4(switchPromocionales6.isChecked());

        dbAdapter db = new dbAdapter(getApplicationContext());
        try {
            db.open(true);
            db.insertarVisitaPromo(vp);
            db.close(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finish();
    }

    public  void inicializarChecks()
    {
        List<especificos> lista = new ArrayList<>();

        switchPromocionales1= findViewById(R.id.switchPromocionales1);
        switchPromocionales1.setOnClickListener(btnclick);
        switchPromocionales2= findViewById(R.id.switchPromocionales2);
        switchPromocionales2.setOnClickListener(btnclick);
        switchPromocionales3= findViewById(R.id.switchPromocionales3);
        switchPromocionales3.setOnClickListener(btnclick);
        switchPromocionales4= findViewById(R.id.switchPromocionales4);
        switchPromocionales4.setOnClickListener(btnclick);
        switchPromocionales5= findViewById(R.id.switchPromocionales5);
        switchPromocionales5.setOnClickListener(btnclick);
        switchPromocionales6=  findViewById(R.id.switchPromocionales6);
        switchPromocionales6.setOnClickListener(btnclick);
        dbAdapter db = new dbAdapter(getApplicationContext());
        try {
            db.open(true);
            lista=db.obtenerEspecifico();
            db.close(true);
            switchPromocionales3.setText(lista.get(0).getEspecificoNombre1());
            switchPromocionales4.setText(lista.get(0).getEspecificoNombre2());
            switchPromocionales5.setText(lista.get(0).getEspecificoNombre3());
            switchPromocionales6.setText(lista.get(0).getEspecificoNombre4());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) (findViewById(android.R.id.content))).getChildAt(0);

    }

    View.OnClickListener btnclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean on = ((Switch) view).isChecked();
            if(on)
                avance++;
            else avance--;
            habilitaBotonFirma();
        }
    };

    public void habilitaBotonFirma()
    {
        if(avance>=6)
          btnFinalizar.setVisibility(View.VISIBLE);
        else btnFinalizar.setVisibility(View.INVISIBLE);

    }
}
