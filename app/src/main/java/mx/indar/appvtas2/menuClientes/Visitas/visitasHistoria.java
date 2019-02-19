package mx.indar.appvtas2.menuClientes.Visitas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.MenuClientes.visitasHistorico;

public class visitasHistoria extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerVisitasHistorico adapter;
    List<visitasHistorico> lista = new ArrayList<>();
    String cliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitas_historia);
        recyclerView=findViewById(R.id.lvVisitasHistoria);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        cliente=getIntent().getStringExtra("cliente");
        dbAdapter db = new dbAdapter(getApplicationContext());
        try {
            db.open(true);
           lista=db.selectVisitasHistorico(cliente,"cliente","");
           db.close(true);
           adapter= new RecyclerVisitasHistorico(lista);
           recyclerView.setAdapter(adapter);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
