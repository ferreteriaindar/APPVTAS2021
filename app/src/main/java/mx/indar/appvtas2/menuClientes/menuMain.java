package mx.indar.appvtas2.menuClientes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.menuClientes.Cobros.cobrosCte;
import mx.indar.appvtas2.fragmentos.clientes.agenda.VisitasCte;
import mx.indar.appvtas2.menuClientes.Visitas.visitasHistoria;

public class menuMain extends AppCompatActivity {

    String cliente,nombreCte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        cliente=intent.getStringExtra("codigo");
        nombreCte=intent.getStringExtra("cliente");
        TextView txtTitulo= findViewById(R.id.txtTitulomenu);
        txtTitulo.setText("MENU CLIENTE "+cliente);
        TextView txtNombreCte = findViewById(R.id.txtTitulomenuNombre);
        txtNombreCte.setText(nombreCte);

        ImageView btnCobros = findViewById(R.id.btnMenuCobros);
        ImageView btnVisitas= findViewById(R.id.btnMenuVisitas);
        btnCobros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menuMain.this,cobrosCte.class);
                intent.putExtra("cliente", cliente);
                startActivity(intent);

            }
        });

        btnVisitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menuMain.this,visitasHistoria.class);
                intent.putExtra("cliente",cliente);
                startActivity(intent);
            }
        });




    }
}
