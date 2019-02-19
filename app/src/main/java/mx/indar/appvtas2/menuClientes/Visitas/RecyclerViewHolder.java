package mx.indar.appvtas2.menuClientes.Visitas;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbClases.MenuClientes.visitasHistorico;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView txtFecha;
    ImageView btnenter;

    public RecyclerViewHolder(View itemView) {


        super(itemView);
        txtFecha=itemView.findViewById(R.id.txtLayoutVisitasHistorialFecha);
        btnenter=itemView.findViewById(R.id.btnLayoutVisitasEnter);


    }


}
class RecyclerVisitasHistorico extends RecyclerView.Adapter<RecyclerViewHolder> {

    List<visitasHistorico> listaVisita = new ArrayList<>();

    public RecyclerVisitasHistorico(List<visitasHistorico> listaVisita) {
        this.listaVisita = listaVisita;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_visitashistorial,parent,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.txtFecha.setText(listaVisita.get(position).getFechaInicio());

        holder.btnenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view.getContext(),visitasHistoriaDetalle.class);
                intent.putExtra("cliente",listaVisita.get(position).getCliente());
                intent.putExtra("fechaInicio",listaVisita.get(position).getFechaInicio());
                view.getContext().startActivity(intent);


            }
        });
    }







    @Override
    public int getItemCount() {
        return listaVisita.size();
    }
}