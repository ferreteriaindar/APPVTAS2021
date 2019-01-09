package mx.indar.appvtas2.fragmentos.clientes.agenda;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.cliente;

public class AdaptadorAgendas extends BaseAdapter implements Filterable {
    private  AgendasFragment af;
    private  Context context;
    private List<cliente> listaclietes,listaclienteTemp;
    CustomFiltrador customFiltrador;

    public AdaptadorAgendas(Context context, List<cliente> listaClientes) {
       // this.af=af;
        this.context=context;
        this.listaclietes=listaClientes;
        this.listaclienteTemp=listaClientes;
    }

    @Override
    public int getCount() {
        return listaclietes.size();
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
        View vista = inflater1.inflate(R.layout.rows_agendas,viewGroup,false);
        TextView txtNombre = vista.findViewById(R.id.txtLayoutAgendasNombretextView4);
        TextView txtCte = vista.findViewById(R.id.txtLayoutAgendasCte);
        TextView txtDir = vista.findViewById(R.id.txtLayoutAgendasDir);
        ImageView img = vista.findViewById(R.id.imgAgendasIMG);
        txtNombre.setText(listaclietes.get(i).getNombreCliente());
        txtCte.setText(listaclietes.get(i).getCliente());
        txtDir.setText(listaclietes.get(i).getDireccion());

        if(primeraVisita(listaclietes.get(i).getCliente()))
        {
            img.setImageResource(R.drawable.ic_gps_no);

        }
        else {
            img.setImageResource(R.drawable.ic_ok);
            vista.setBackgroundColor(Color.parseColor("#009688"));
            txtCte.setTextColor(Color.parseColor("#ffffff"));
            txtNombre.setTextColor(Color.parseColor("#ffffff"));
            txtDir.setTextColor(Color.parseColor("#ffffff"));
        }




        return vista;
    }




    public  boolean primeraVisita(String cliente) {
        dbAdapter db = new dbAdapter(context.getApplicationContext());
        try {
            db.open(true);
            Cursor cursor = db.rawQuery("SELECT idVisitas   \n" +
                    "  FROM visitas\n" +
                    "  where fechaInicio>= date('now','localtime') and cliente='"+cliente+"';");
            Log.i("primeraVisita","SELECT idVisitas   \n" +
                    "  FROM visitas\n" +
                    "  where fechaInicio>= date('now') and cliente='"+cliente+"';");
            if (cursor.getCount() <= 0 || cursor==null || !cursor.moveToFirst()) {
                cursor.close();
                db.close(true);
                return true;
            } else {
                cursor.close();
                db.close(true);
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public Filter getFilter() {
            if(customFiltrador==null)
            {Log.i("GetFilter","getFIlter()");
                customFiltrador= new CustomFiltrador();
            }
        return customFiltrador;
    }

    class CustomFiltrador extends  Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results =new FilterResults();
            if(charSequence !=null && charSequence.length()>0)
            {
                Log.i("GetFilter","CHASECUENTES"+charSequence);
             charSequence= charSequence.toString().toUpperCase();
                List<cliente> filtrados = new ArrayList<>();
                for (int i=0;i<listaclienteTemp.size();i++)
                {
                    if(listaclienteTemp.get(i).getNombreCliente().contains(charSequence))
                    {
                        cliente cte =new cliente();
                        cte.setCliente(listaclienteTemp.get(i).getCliente());
                        cte.setNombreCliente(listaclienteTemp.get(i).getNombreCliente());
                        cte.setDireccion(listaclienteTemp.get(i).getDireccion());
                        filtrados.add(cte);
                        Log.i("GetFilter","cte"+listaclienteTemp.get(i).getNombreCliente());


                    }
                    results.count=filtrados.size();
                    results.values=filtrados;

                }
            }
            else
            {
               results.count=listaclienteTemp.size();
               results.values=listaclienteTemp;
            }

            return null;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listaclietes=(List<cliente>)filterResults.values;
            notifyDataSetChanged();
        }
    }


}
