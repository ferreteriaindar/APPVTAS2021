package mx.indar.appvtas2.menuClientes.Cobros;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbClases.MenuClientes.Cobro;

public class cobrosCte extends AppCompatActivity {

    public RecyclerView recyclerView;
    public  RecyclerCobro adapter;
    public  RecyclerView.LayoutManager layoutManager;

    public List<Cobro> listCobro = new ArrayList<Cobro>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cobros_cte);
    }
}
