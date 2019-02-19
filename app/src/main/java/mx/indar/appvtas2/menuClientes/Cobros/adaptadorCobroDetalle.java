package mx.indar.appvtas2.menuClientes.Cobros;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbClases.MenuClientes.CobroD;



public class adaptadorCobroDetalle extends BaseAdapter {

    public  List<CobroD> listacobroD;
    public  Context context;
    public adaptadorCobroDetalle(Context context, List<CobroD> listaCobroD) {
        this.context=context;
        this.listacobroD=listaCobroD;
    }

    @Override
    public int getCount() {
        return listacobroD.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vista= inflater.inflate(R.layout.row_cobrodetalle,viewGroup,false);
        TextView mov=vista.findViewById(R.id.txtrowcobrarMovid2);
        TextView descuento= vista.findViewById(R.id.txtCobrarDescuento2);
        TextView importe= vista.findViewById(R.id.txtrowcobrarImporte2);
        mov.setText(listacobroD.get(i).getMov()+" "+listacobroD.get(i).getMovid());
        descuento.setText(listacobroD.get(i).getDescuento()+"%");
        Log.i("cobro","TEXTO"+mov.getText().toString());
        if(listacobroD.get(i).getAplicaDescto().equals("NO")) {
            descuento.setTextColor(Color.RED);
            descuento.setPaintFlags(descuento.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
        importe.setText(format.format( listacobroD.get(i).getImporte()));


        return vista;
    }
}
