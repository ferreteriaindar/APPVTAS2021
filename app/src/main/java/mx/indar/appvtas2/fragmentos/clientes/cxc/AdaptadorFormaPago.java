package mx.indar.appvtas2.fragmentos.clientes.cxc;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbClases.formaPago;

public class AdaptadorFormaPago extends RecyclerView.Adapter<AdaptadorFormaPago.ViewHolder> {

    public List<formaPago> listaformaPago;
    cobrarFragment.CustomItemClickListener listener;

    public AdaptadorFormaPago(List<formaPago> listaformaPago, cobrarFragment.CustomItemClickListener listener) {
        this.listaformaPago = listaformaPago;
        this.listener = listener;
    }

    @Override
    public AdaptadorFormaPago.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.rows_formapago,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view,viewHolder.getPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdaptadorFormaPago.ViewHolder holder, int position) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
        if(!listaformaPago.get(position).getTipoPago().equals("Efectivo"))
        {
            holder.banco.setText(listaformaPago.get(position).getBanco());
            holder.referencia.setText(listaformaPago.get(position).getReferencia());

        }
        holder.formapago.setText(listaformaPago.get(position).getTipoPago());
        holder.importe.setText(format.format(listaformaPago.get(position).getImporte()));
        if(listaformaPago.get(position).getTipoPago().equals("Efectivo"))
        { holder.img.setImageResource(R.drawable.ic_money);
        //    holder.cardView.setBackgroundColor(Color.parseColor("#66BB6A"));
          //  holder.itemView.setBackgroundColor(Color.parseColor("#66BB6A"));
            holder.tableRow.setBackgroundColor(Color.parseColor("#66BB6A"));

        }
        else {
            holder.img.setImageResource(R.drawable.ic_cheque);
          //  holder.itemView.setBackgroundColor(Color.parseColor("4FC3F7"));
            holder.tableRow.setBackgroundColor(Color.parseColor("#4FC3F7"));
        }
    }

    @Override
    public int getItemCount() {
        return listaformaPago.size();
    }



    public  static  class ViewHolder extends RecyclerView.ViewHolder
    {

        ImageView img;
        TextView banco,formapago,importe,referencia;
        CardView cardView;
        TableRow tableRow;

        public ViewHolder(View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imgFormaPago);
            banco=itemView.findViewById(R.id.txtformapagobanco);
            formapago=itemView.findViewById(R.id.txtformapagopago);
            importe=itemView.findViewById(R.id.txtformapagoImporte);
            referencia=itemView.findViewById(R.id.txtformapagoRefeerencia);
            //cardView=itemView.findViewById(R.id.cardviewformaPago);
            tableRow=itemView.findViewById(R.id.fondoformapago);


        }
    }
}
