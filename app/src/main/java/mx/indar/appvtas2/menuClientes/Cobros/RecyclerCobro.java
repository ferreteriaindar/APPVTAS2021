package mx.indar.appvtas2.menuClientes.Cobros;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbClases.MenuClientes.Cobro;

class RecyclerViewHolder extends RecyclerView.ViewHolder{

    public ImageView img ;
    public TextView  recibo,fecha,importe;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        img= (ImageView) itemView.findViewById(R.id.imglayoutCobroTipoPago);
        recibo=(TextView) itemView.findViewById(R.id.txtLayoutCobrorecibo);
        fecha=(TextView) itemView.findViewById(R.id.txtLayoutCobroFecha);
        importe=(TextView)itemView.findViewById(R.id.txtlayoutCobroImporte);

    }
}
public class RecyclerCobro extends RecyclerView.Adapter<RecyclerViewHolder> {

        private List<Cobro>  listaCobro = new ArrayList<>();

    public RecyclerCobro(List<Cobro> listaCobro) {
        this.listaCobro = listaCobro;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.row_cobros_cte,parent,false);
        return  new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if(listaCobro.get(position).getFormaPago().toString().equals("Efectivo"))
            holder.img.setImageResource(R.drawable.ic_pagoefectivo);
        else  holder.img.setImageResource(R.drawable.ic_signature);
        holder.recibo.setText("Recibo: "+listaCobro.get(position).getId());
        holder.fecha.setText(listaCobro.get(position).getFecha());
         NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
         holder.importe.setText(format.format(listaCobro.get(position).getImporte()));

    }

    @Override
    public int getItemCount() {
        return listaCobro.size();
    }
}
