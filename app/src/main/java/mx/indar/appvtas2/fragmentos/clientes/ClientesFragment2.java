package mx.indar.appvtas2.fragmentos.clientes;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.List;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.cliente;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientesFragment2 extends Fragment {


    public ClientesFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView lv = (ListView) getView().findViewById(R.id.lvClientes2);
        View v =inflater.inflate(R.layout.fragment_clientes_fragment2, container, false);
        lv = (ListView)v.findViewById(R.id.lvClientes2);
        List<cliente> listaclientes = null;
        dbAdapter db = new dbAdapter(getActivity());
        try {
            db.open(false);
            listaclientes=db.obtenerClientes(0);
            db.close(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

       // lv.setAdapter(new AdaptadorClientes(container.getContext(),listaclientes,));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("ClickListView","si");
            }
        });


        // Inflate the layout for this fragment
        return v;
    }

}
