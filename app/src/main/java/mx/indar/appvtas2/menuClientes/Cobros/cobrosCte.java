package mx.indar.appvtas2.menuClientes.Cobros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.MenuClientes.Cobro;

public class cobrosCte extends AppCompatActivity {

    public RecyclerView recyclerView;
    public  RecyclerCobro adapter;
    public  RecyclerView.LayoutManager layoutManager;

    public List<Cobro> listCobro = new ArrayList<Cobro>();
    public  String cliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cobros_cte);
        Intent intent = getIntent();
        cliente=intent.getStringExtra("cliente");
        recyclerView = (RecyclerView) findViewById(R.id.lvLayoutCobros);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        dbAdapter db = new dbAdapter(getApplicationContext());
        try {
            db.open(true);
            listCobro=db.SelectCobro(cliente,0);
            db.close(true);
            adapter= new RecyclerCobro(listCobro,cliente);
            recyclerView.setAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
}
