package mx.indar.appvtas2.menuClientes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import mx.indar.appvtas2.R;

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

    }
}
