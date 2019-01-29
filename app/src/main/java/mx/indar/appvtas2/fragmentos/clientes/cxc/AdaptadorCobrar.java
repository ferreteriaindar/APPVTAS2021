package mx.indar.appvtas2.fragmentos.clientes.cxc;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbClases.documentoCXC;

public class AdaptadorCobrar  extends BaseAdapter {

    private Context context;
    private List<documentoCXC> listadocumento;
    String formaPago;
    int diasparaCheque;

    public AdaptadorCobrar(Context context, List<documentoCXC> listadocumento) {
        this.context = context;
        this.listadocumento = listadocumento;
        formaPago="nada";
    }
    public AdaptadorCobrar(Context context, List<documentoCXC> listadocumento,String formaPago,int  diasparaCheque) {
        this.context = context;
        this.listadocumento = listadocumento;
        this.formaPago=formaPago;
        this.diasparaCheque=diasparaCheque;
    }
    @Override
    public int getCount() {
        return listadocumento.size();
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
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vista = inflater.inflate(R.layout.rowcobrar,viewGroup,false);
        TextView movid= vista.findViewById(R.id.txtrowcobrarMovid);
        TextView importe= vista.findViewById(R.id.txtrowcobrarImporte);
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
        movid.setText(listadocumento.get(i).getMov()+" "+ listadocumento.get(i).getMovid());
        importe.setText(format.format(listadocumento.get(i).getImporte()));
        TextView descuento= vista.findViewById(R.id.txtCobrarDescuento);
       // if(listadocumento.get(i).getDias()<=0)
        descuento.setText(listadocumento.get(i).getDescuento()+"");

            if (formaPago.equals("Cheque")) {
                if (listadocumento.get(i).getDias() <= 0 && listadocumento.get(i).getDescuento() != 0 && (listadocumento.get(i).getDias() + diasparaCheque) <= 0) {
                    descuento.setTextColor(Color.GREEN);
                } else {
                    descuento.setTextColor(Color.RED);
                    descuento.setPaintFlags(descuento.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
            else {
                if (listadocumento.get(i).getDias() <= 0 && listadocumento.get(i).getDescuento() != 0) {
                    descuento.setTextColor(Color.GREEN);
                } else {
                    descuento.setTextColor(Color.RED);
                    descuento.setPaintFlags(descuento.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }

            }






        return vista;

    }
}
