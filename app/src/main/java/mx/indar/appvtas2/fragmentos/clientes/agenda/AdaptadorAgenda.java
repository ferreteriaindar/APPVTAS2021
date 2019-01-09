package mx.indar.appvtas2.fragmentos.clientes.agenda;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbClases.cliente;
import mx.indar.appvtas2.fragmentos.clientes.ClienteDetalle;

public class AdaptadorAgenda  extends BaseAdapter {

    private  Context context;
    private  List<cliente> listaclientes;

    public AdaptadorAgenda(Context context,List<cliente> listaclientes)
    {
        this.context=context;
        this.listaclientes=listaclientes;

    }

    @Override
    public int getCount() {
        return listaclientes.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater1 =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vista = inflater1.inflate(R.layout.rows_clientes,viewGroup,false);
        TextView txtCliente= vista.findViewById(R.id.txtLayoutCteCliente);
        TextView txtNombreCTe= vista.findViewById(R.id.txtLayoutCteNombre);
        txtCliente.setText(listaclientes.get(i).getCliente());
        txtNombreCTe.setText(listaclientes.get(i).getNombreCliente());
        Button btnCte = vista.findViewById(R.id.btnLayoutCTeOpcion1);
        btnCte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClienteDetalle nextFrag= new ClienteDetalle();

                Log.i("button","si");
            }
        });


        return vista;

        //return null;
    }
}
