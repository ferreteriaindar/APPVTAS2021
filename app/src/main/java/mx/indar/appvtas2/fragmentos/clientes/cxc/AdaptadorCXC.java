package mx.indar.appvtas2.fragmentos.clientes.cxc;


import android.graphics.Color;
import android.icu.util.ValueIterator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbClases.cxcCliente;
import mx.indar.appvtas2.dbClases.documentoCXC;

public class AdaptadorCXC extends RecyclerView.Adapter<AdaptadorCXC.ViewHolder> {


    public List<cxcCliente> listacxc;
    cxcFragment.CustomItemClickListener listener;
   public ArrayList<String> selectedStrings = new ArrayList<String>();
   public  List<documentoCXC> docsSelecionados= new ArrayList<>();




    public  float totalImporte=0;


    public AdaptadorCXC(List<cxcCliente> listacxc, cxcFragment.CustomItemClickListener listener) {
        this.listacxc = listacxc;
        this.listener=listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rows_cxc, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view,viewHolder.getPosition());
                totalImporte=viewHolder.totalcard;
            }
        });
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
    /*    Calendar cal = Calendar.getInstance();
        Date emision=  cal.getTime();
        Date vencimiento=cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
           emision =sdf.parse( listacxc.get(position).getFechaEmision());
           vencimiento=sdf.parse(listacxc.get(position).getVencimiento());

        } catch (ParseException e) {
            e.printStackTrace();
           Log.i("error", e.getMessage());
        }
*/
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(pattern);
        Date newDatE=null,newdate2;
        String vencimiento="",fechaemision="";
        try {
            newDatE = simpleDateFormat.parse(listacxc.get(position).getVencimiento());
            newdate2=simpleDateFormat2.parse(listacxc.get(position).getFechaEmision());

            simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat2 =new SimpleDateFormat("yyyy-MM-dd");
            vencimiento=simpleDateFormat.format(newDatE);
            fechaemision=simpleDateFormat2.format(newdate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.movid.setText(listacxc.get(position).getMov()+" "+listacxc.get(position).getMovID());
     holder.referencia.setText(listacxc.get(position).getReferencia());
     holder.importe.setText("$"+listacxc.get(position).getSaldo()+"");
     holder.emision.setText(fechaemision);
     holder.vencimiento.setText(vencimiento);
     if((Integer)(listacxc.get(position).getDiasMoratorios())>0)
         holder.dias.setTextColor(Color.RED);
     else holder.dias.setTextColor(Color.GREEN);
     holder.dias.setText(listacxc.get(position).getDiasMoratorios()+"");
     if(listacxc.get(position).getMov().startsWith("F"))
         holder.img.setImageResource(R.drawable.ic_letter_f);
        if(listacxc.get(position).getMov().startsWith("C"))
            holder.img.setImageResource(R.drawable.ic_letter_c);
        if(listacxc.get(position).getMov().startsWith("N"))
            holder.img.setImageResource(R.drawable.ic_letter_n);
        if(holder.checkBox.isChecked())
        {
            Log.i("CXC","checado");
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                documentoCXC cxc = new documentoCXC();
                cxc.setMov(listacxc.get(position).getMov());
                cxc.setMovid(   holder.movid.getText().toString().replace(listacxc.get(position).getMov()+ " ",""));

                cxc.setImporte(Float.parseFloat( holder.importe.getText().toString().substring(1)));
                if(checked)
                {
                    selectedStrings.add(holder.importe.getText().toString().substring(1)+"");

                    docsSelecionados.add(cxc);
                }
                else {
                    selectedStrings.remove(holder.importe.getText().toString().substring(1) + "");
                    for(Iterator<documentoCXC> iterator = docsSelecionados.iterator(); iterator.hasNext(); ) {
                        if(iterator.next().getMovid() == holder.movid.getText()+"");
                            iterator.remove();
                            break;
                    }
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return listacxc.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  mov,movid,referencia,emision,vencimiento,dias,importe,total;
        ImageView img;
        public float totalcard=0;

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public   CheckBox checkBox;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            movid= itemView.findViewById(R.id.txtlayoutMovcxc);
            referencia=itemView.findViewById(R.id.txtlayoutreferencaiacxc);
            emision=itemView.findViewById(R.id.txtlayoutemisioncxc);
            vencimiento=itemView.findViewById(R.id.txtlayoutvencimientocxc);
            dias=itemView.findViewById(R.id.txtlayoutdiascxc);
            importe=itemView.findViewById(R.id.txtlayoutImportecxc);
            img = itemView.findViewById(R.id.imgLayoutcxc);
            total= itemView.findViewById(R.id.txtLayoutImportetotalCxc);
            checkBox=itemView.findViewById(R.id.checklayoutcxc);
           /* checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkBox.isChecked())
                    {  //  Toast.makeText(view.getContext(), "check2", Toast.LENGTH_SHORT).show();
                     //   checkBox.setChecked(false);
                     totalcard+=Float.parseFloat( importe.getText().toString().substring(1)+"");
                    }
                   // else checkBox.setChecked(true);

                }
            });*/






        }


    }


}
