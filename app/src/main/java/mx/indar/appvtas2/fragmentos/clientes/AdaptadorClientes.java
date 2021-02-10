package mx.indar.appvtas2.fragmentos.clientes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.cliente;
import mx.indar.appvtas2.dbClases.visita;
import mx.indar.appvtas2.firebase.UbicacionGPS;
import  mx.indar.appvtas2.fragmentos.clientes.ClienteDetalle;
import mx.indar.appvtas2.fragmentos.clientes.agenda.AgendasFragment;
import mx.indar.appvtas2.fragmentos.clientes.agenda.VisitaAgendas;
import mx.indar.appvtas2.menuClientes.menuMain;

public class AdaptadorClientes extends BaseAdapter {

    private  static LayoutInflater inflater=null;
    Context context;
    List<cliente> listaclientes;
    UbicacionGPS gps;
    ClientesFragment clientesFragment;



    public AdaptadorClientes(Context context,List<cliente> listaclientes,ClientesFragment clientesFragment)
    {
        this.context=context;
        this.listaclientes=listaclientes;
        this.clientesFragment=clientesFragment;


        inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
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

     //   final  View vista =inflater.inflate(R.layout.rows_clientes,null);
        LayoutInflater inflater1 =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vista = inflater1.inflate(R.layout.rows_clientes,viewGroup,false);
        gps= new UbicacionGPS(context);
        gps.inicializar();
       // final FragmentManager manager = ((AppCompatActivity)Activity).getSupportFragmentManager();

        final TextView txtCliente= vista.findViewById(R.id.txtLayoutCteCliente);
        final TextView txtNombreCTe= vista.findViewById(R.id.txtLayoutCteNombre);
        txtCliente.setText(listaclientes.get(i).getCliente());
        txtNombreCTe.setText(listaclientes.get(i).getNombreCliente());
        Button btnOpcion2 =(Button) vista.findViewById(R.id.btnLayoutCTeOpcion2);
        btnOpcion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              if(gps.gpsOK)
              {
                  Log.i("gpsClase",gps.Ubicacion.getLatitude()+"");


                  try {
                      if(EstasDentroDeLaCerca(context,gps.Ubicacion.getLatitude(),gps.Ubicacion.getLongitude(),txtCliente.getText().toString())<50) {
                          dbAdapter db = new dbAdapter(context);
                          try {
                              db.open(true);
                              visita v = new visita();
                              v.setCliente(txtCliente.getText().toString());
                              // v.setLatitud((float) location.getLatitude());
                              //  v.setLongitud((float) location.getLongitude());
                              v.setLatitud((float) gps.Ubicacion.getLatitude());
                              v.setLongitud((float) gps.Ubicacion.getLongitude());
                              long idvisita;
                              idvisita = db.registraVisitaCte(v);
                              Log.i("primeraVisita", "ID" + idvisita);
                              db.close(true);
                              VisitaAgendas va = new VisitaAgendas();
                              Bundle args = new Bundle();
                              args.putLong("idVisita", idvisita);
                              args.putString("cliente", txtCliente.getText().toString());

                              va.setArguments(args);
                              Log.i("visita", "si crea obketo");
                              clientesFragment.getActivity().getSupportFragmentManager().beginTransaction()
                                      .replace(R.id.contenidoNav, va, "findThisFragment")
                                      .addToBackStack("AgendasFragment")
                                      .commit();

                          } catch (SQLException e) {
                              e.printStackTrace();
                              Log.i("primeraVisita", e.getMessage());
                          }
                      }
                      else
                      {
                          //   Toast.makeText(getActivity(), "Estas a "+Float.toString( EstasDentroDeLaCerca(container.getContext(), latitude, longitude, selected))+"metros del cliente,estas lejos", Toast.LENGTH_LONG).show();
                          Snackbar snack = null;
                          snack= Snackbar.make(view,"Estas a "+Float.toString( EstasDentroDeLaCerca(context,gps.Ubicacion.getLatitude(),gps.Ubicacion.getLongitude(),txtCliente.getText().toString()))+" metros del cliente, ACERCATE!!",Snackbar.LENGTH_LONG);
                          View snackView =snack.getView();
                          snackView.setBackgroundColor(context.getResources().getColor(R.color.RojoAviso));
                          snack.show();
                      }
                  } catch (SQLException e) {
                      e.printStackTrace();
                  }
              }
              else Toast.makeText(context, "No hay conexion GPS,espera un momento", Toast.LENGTH_SHORT).show();
            }
        });
        Button btnEntrar = (Button) vista.findViewById(R.id.btnLayoutCTeOpcion1);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,menuMain.class);
                intent.putExtra("cliente",txtNombreCTe.getText().toString());
                intent.putExtra("codigo",txtCliente.getText().toString());
               context.startActivity(intent);
            }
        });

        return vista;
    }

    public Float EstasDentroDeLaCerca(Context context,double latitud,double logitud,String clienteP) throws SQLException {
        try {
            dbAdapter da = new dbAdapter(context);
            da.open(true);
            List<cliente> CTE = da.regresaClienteInfoUnico(clienteP);
            da.close(true);
            if (!CTE.isEmpty() && CTE != null) {
                cliente cte = CTE.get(0);
                Location mylocation = new Location("");
                Location dest_location = new Location("");
                mylocation.setLatitude(cte.getCoordenadax());
                mylocation.setLongitude(cte.getCoordenaday());
                dest_location.setLatitude(latitud);
                dest_location.setLongitude(logitud);
                float distance = mylocation.distanceTo(dest_location);//in meters
                Log.d("Metros", Float.toString(distance));
                //PARA LOS CLIENTES QUE NO TIENEN LA COORDENADA  ,osea =1    los dejo pasar
                return   cte.getCoordenadax()==1?0:distance;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0F;
    }
}
