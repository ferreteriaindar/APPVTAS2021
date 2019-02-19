package mx.indar.appvtas2.menuClientes.Cobros;


import android.content.Intent;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbClases.MenuClientes.Cobro;

class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public ImageView img ;
    public TextView  recibo,fecha,importe;
    public ConstraintLayout constraintLayout;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        img= (ImageView) itemView.findViewById(R.id.imglayoutCobroTipoPago);
        recibo=(TextView) itemView.findViewById(R.id.txtLayoutCobrorecibo);
        fecha=(TextView) itemView.findViewById(R.id.txtLayoutCobroFecha);
        importe=(TextView)itemView.findViewById(R.id.txtlayoutCobroImporte);
        constraintLayout= itemView.findViewById(R.id.layoutcobros);


    }


}
public class RecyclerCobro extends RecyclerView.Adapter<RecyclerViewHolder> {

        private List<Cobro>  listaCobro = new ArrayList<>();
        String cliente;

    public RecyclerCobro(List<Cobro> listaCobro,String cliente) {
        this.listaCobro = listaCobro;
        this.cliente=cliente;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.row_cobros_cte2,parent,false);
        return  new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        if(listaCobro.get(position).getFormaPago().toString().equals("Efectivo"))
            holder.img.setImageResource(R.drawable.ic_pagoefectivo);
        else  holder.img.setImageResource(R.drawable.ic_signature);
        holder.recibo.setText("Recibo: "+listaCobro.get(position).getNumCobro());
        SimpleDateFormat formatfecha = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = formatfecha.parse(listaCobro.get(position).getFechaRegistro());
            holder.fecha.setText(formatfecha.format(listaCobro.get(position).getFechaRegistro()));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("cobror","error"+e.getMessage());

        }


         NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
         holder.importe.setText(format.format(listaCobro.get(position).getImporte()));

         holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Log.i("cobro","Recibo:"+listaCobro.get(position).getNumCobro());
                 Cobro c = new Cobro();
                 c=listaCobro.get(position);
                 Intent intent = new Intent(view.getContext(),cobroDetalle.class);
                 intent.putExtra("cobro",listaCobro.get(position).getId());
                 intent.putExtra("cliente",cliente);
                 view.getContext().startActivity(intent);

             }
         });

    }

    @Override
    public int getItemCount() {
        return listaCobro.size();
    }
}
