package mx.indar.appvtas2.fragmentos.clientes.cxc;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.TimeUnit;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.PublicKey;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mx.indar.appvtas2.Bluetooth.Impresora;
import mx.indar.appvtas2.NavigationIndar;
import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.documentoCXC;
import mx.indar.appvtas2.dbClases.formaPago;
import mx.indar.appvtas2.dbClases.subirCobro;
import mx.indar.appvtas2.dbClases.subirCobroD;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link cobrarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cobrarFragment extends Fragment implements NavigationIndar.IOnBackPressed{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public List<documentoCXC> listadocumentos;
    public ListView lv;
    public RecyclerView recyclerView;
    public  View view;
    public ImageView img;
    public  List<formaPago> listaformaPago;
    public  float importeformaPago=0,importeFinal=0, importe=0;
    TextView txtformaPagoImportefinal;
    TextView txtImporte,txtFormaPago,TxtReferencia,txtFechaPago;
    public String cliente,formaPago,referencia;
    public  int  año,dia,mes, diasparaCheque;
    Long id;


    public boolean isResultadoSalir() {
        return ResultadoSalir;
    }

    public void setResultadoSalir(boolean resultadoSalir) {
        ResultadoSalir = resultadoSalir;
    }

    public boolean ResultadoSalir;
    public cobrarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {

                if (data.getExtras().containsKey("formapago")) {
                    formaPago=data.getExtras().getString("formapago");
                    importeFinal= Float.parseFloat( data.getExtras().getString("importe"));
                    Log.i("cobrar","importe final:"+importeFinal+"");
                    if(!formaPago.equals("Efectivo"))
                    {
                        Log.i("cobrar",data.getExtras().getString("banco"));
                        Log.i("cobrar",data.getExtras().getString("referencia"));
                        referencia=data.getExtras().getString("banco")+data.getExtras().getString("referencia");
                        año=data.getExtras().getInt("año");
                        mes=data.getExtras().getInt("mes");
                        dia=data.getExtras().getInt("dia");
                        Log.i("cobrar",referencia+"referencia");

                        importe=0;
                        for(int i=0;i<listadocumentos.size();i++)
                        {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                            try {
                                Date fechacheque =formatter.parse(año+"/"+mes+"/"+dia);
                                Calendar chequefecha=Calendar.getInstance();
                                chequefecha.setTime(fechacheque);

                                Calendar hoy = Calendar.getInstance();
                                Log.i("cheque",chequefecha.get(Calendar.DAY_OF_YEAR)+"cheque");
                                Log.i("cheque", hoy.get(Calendar.DAY_OF_YEAR)+"hoy");
                                diasparaCheque=0;
                                diasparaCheque=chequefecha.get(Calendar.DAY_OF_YEAR)-hoy.get(Calendar.DAY_OF_YEAR);
                                Log.i("cheque",chequefecha.get(Calendar.DAY_OF_YEAR)-hoy.get(Calendar.DAY_OF_YEAR)+"diasparacheque");


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Log.i("cheque",(listadocumentos.get(i).getDias()+"diasdoc"+diasparaCheque+"diferencia dias"));
                                Log.i("cheque",(listadocumentos.get(i).getDias()+diasparaCheque)+"diferencia dias");
                                if(formaPago.equals("Cheque")) {
                                    if (listadocumentos.get(i).getDias() <= 0 && listadocumentos.get(i).getDescuento() > 0 && (listadocumentos.get(i).getDias()+diasparaCheque)<=0)
                                        importe += listadocumentos.get(i).getImporte() * (1 - (listadocumentos.get(i).getDescuento() / 100));
                                    else importe += listadocumentos.get(i).getImporte();
                                }
                                else {
                                    if (listadocumentos.get(i).getDias() <= 0 && listadocumentos.get(i).getDescuento() > 0)
                                        importe += listadocumentos.get(i).getImporte() * (1 - (listadocumentos.get(i).getDescuento() / 100));
                                    else importe += listadocumentos.get(i).getImporte();

                                }

                        }
                    }
                    else referencia="N/A";
                }
                TxtReferencia.setText(referencia);


                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
                txtImporte.setText(format.format(importe));
                txtformaPagoImportefinal.setText(format.format(importeFinal));
                txtFormaPago.setText(formaPago);
                AdaptadorCobrar ac = new AdaptadorCobrar(getActivity(),listadocumentos,formaPago,diasparaCheque);
                lv.setAdapter(ac);



                /*
                if (data.getExtras().containsKey("formapago")) {

                    Log.i("cxc","formade pago"+ data.getExtras().getString("formapago"));
                    formaPago fp= new formaPago();
                    if(!data.getExtras().getString("formapago").equals("Efectivo"))
                    {
                        fp.setBanco(data.getExtras().getString("banco"));
                        fp.setReferencia(data.getExtras().getString("referencia"));
                    }
                    fp.setImporte(Float.parseFloat( data.getExtras().getString("importe")));
                    fp.setTipoPago(data.getExtras().getString("formapago"));
                    importeformaPago+=fp.getImporte();
                    listaformaPago.add(fp);
                    NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
                    txtformaPagoImportefinal.setText(format.format(importeformaPago));
                    // Use the returned value
                    Log.i("cxc",listaformaPago.size()+"");
                    if(importeformaPago>=importe)
                    {
                        txtformaPagoImportefinal.setTextColor(Color.parseColor("#2e7d32"));
                    }

                }*/
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menuformapago:
                insertaCobro();
                setResultadoSalir(true);
                Toast.makeText(getActivity(), "Cobro Guardado", Toast.LENGTH_SHORT).show();
                //getActivity().getSupportFragmentManager().popBackStack();


                Log.i("cxc","boton registracobro");

                return  true;


        }

        return super.onOptionsItemSelected(item);
    }



    public  void ImprimeCobro()
    {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
        Impresora imp = new Impresora(getActivity());
        if(imp.FindBluetoothDevice())
        {
            try {
                imp.openBluetoothPrinter();
                int width = 47;
                char fill = '\u00A0';
               // String linea;
                String padded;
                //imp.printPhoto(R.drawable.ic_cobrar);
                if (imp.puedoImprimir) {
                    imp.ImprimirCabezeraTicket();
                    imp.printDATA("");

                    imp.printDATA("----------------------------------------------");
                    imp.printDATA("RECIBO DE COBRO: "+id+"");
                    imp.printDATA("CLIENTE: "+cliente);
                    SharedPreferences prefs=getActivity().getSharedPreferences(getResources().getString(R.string.action_settings), MODE_PRIVATE);
                    imp.printDATA("VENDEDOR: "+prefs.getString("nombre",""));
                    imp.printDATA("----------------------------------------------");
                    imp.printDATA("");
                    for(int i=0;i<listadocumentos.size();i++)
                    {
                        String linea="";
                        linea=listadocumentos.get(i).getMov()+" "+listadocumentos.get(i).getMovid()+"  ";//+"  $"+listadocumentos.get(i).getImporte()+"    ";
                        String  descto;
                        if(formaPago.equals("Cheque")) {
                            if (listadocumentos.get(i).getDias() <= 0 && listadocumentos.get(i).getDescuento() > 0 && (listadocumentos.get(i).getDias()+diasparaCheque)<=0) {
                                descto = listadocumentos.get(i).getDescuento() + "%";
                                linea+=descto+"  ";
                                linea+=String.format("%1$"+ 15 + "s",format.format(listadocumentos.get(i).getImporte() * (1 - (listadocumentos.get(i).getDescuento() / 100))) );
                                //listadocumentos.get(i).getImporte() * (1 - (listadocumentos.get(i).getDescuento() / 100));
                                imp.printDATA(linea);
                                imp.printDATA("     antes: "+format.format(listadocumentos.get(i).getImporte()));

                            }
                            else {
                                        descto="0%";
                                        linea+=descto+"  "+String.format("%1$"+ 15 + "s",format.format(listadocumentos.get(i).getImporte()) );;
                                        imp.printDATA(linea);
                            }
                        }
                        else {
                            if (listadocumentos.get(i).getDias() <= 0 && listadocumentos.get(i).getDescuento() > 0) {
                                descto = listadocumentos.get(i).getDescuento() + "%";
                                linea+=descto+"  ";
                                linea+=String.format("%1$"+ 15 + "s",format.format(listadocumentos.get(i).getImporte() * (1 - (listadocumentos.get(i).getDescuento() / 100))) );
                                //listadocumentos.get(i).getImporte() * (1 - (listadocumentos.get(i).getDescuento() / 100));
                                imp.printDATA(linea);
                                imp.printDATA("     antes: "+format.format(listadocumentos.get(i).getImporte()));
                            }
                            else {
                                descto="0%";
                                linea+=descto+"  "+String.format("%1$"+ 15 + "s",format.format(listadocumentos.get(i).getImporte()) );;
                                imp.printDATA(linea);
                            }

                        }
                      /*  linea+=descto+"  ";
                         format = NumberFormat.getCurrencyInstance(Locale.CANADA);
                        padded =  String.format("%1$"+ 15 + "s",format.format(listadocumentos.get(i).getImporte()) );
                        linea+=padded;
                        imp.printDATA(linea); */




                    }
                    imp.printDATA("----------------------------------------------");
                    imp.printDATA("IMPORTE DOCS:                    "+txtImporte.getText().toString());
                    imp.printDATA("IMPORTE PAGADO                   "+txtformaPagoImportefinal.getText().toString());
                    imp.printDATA("");
                    imp.printDATA("METODO:"+formaPago);
                    imp.printDATA("FECHAPAGO: "+txtFechaPago.getText().toString());
                    imp.printDATA("REFERENCIA: "+TxtReferencia.getText().toString());
                    imp.printDATA(" ");
                    imp.printDATA(" ");
                    imp.printDATA(" ");


                }
                imp.disconnectBT();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    imp.disconnectBT();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }



    }

    public  void insertaCobro()
    {
        SharedPreferences prefs= getActivity().getSharedPreferences("Settings",MODE_PRIVATE);
        dbAdapter db= new dbAdapter(getActivity().getApplicationContext());
        try {
            //INSERTA   LA CABECERA DEL COBRO
            db.open(true);
            subirCobro sd = new subirCobro();
            sd.setCliente(cliente);
            sd.setFormaPago(formaPago);
            sd.setImporte(importeFinal);
            sd.setZona(prefs.getString("usuario",""));
             id=db.insertarsubirCobro(sd);
            for(int j=0;j<listadocumentos.size();j++) {
                subirCobroD scd = new subirCobroD();
                scd.setIdSubirCobro(id.intValue());
                scd.setImporte(listadocumentos.get(j).getImporte());
                scd.setMov(listadocumentos.get(j).getMov());
                scd.setMovid(listadocumentos.get(j).getMovid());

                db.insertarsubirCobroD(scd);
            }
            db.close(true);
            ImprimeCobro();

            AlertDialog.Builder builder1 = new AlertDialog.Builder(cobrarFragment.this.getActivity());
            builder1.setMessage("¿Reimprimir  TICKET?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "SI",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                          ImprimeCobro();
                        }
                    });

            builder1.setNegativeButton(
                    "NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

/*
    public  void insertaCobro()
    {
        SharedPreferences prefs =getActivity().getSharedPreferences(getResources().getString(R.string.action_settings), MODE_PRIVATE);
        dbAdapter db = new dbAdapter(getActivity().getApplicationContext());
        try {
            db.open(true);


        for(int i=0;i<listaformaPago.size();i++) {
            subirCobro sd = new subirCobro();
            sd.setCliente(cliente);
            sd.setFormaPago(listaformaPago.get(i).getTipoPago());
            Log.i("cobro",listaformaPago.get(i).getTipoPago()+"tipopago");
            sd.setImporte(listaformaPago.get(i).getImporte());
            sd.setZona(prefs.getString("usuario",""));
            Long id=db.insertarsubirCobro(sd);

            for(int j=0;j<listadocumentos.size();j++) {
                subirCobroD scd = new subirCobroD();
                scd.setIdSubirCobro(id.intValue());
                scd.setImporte(listadocumentos.get(j).getImporte());
                scd.setMov(listadocumentos.get(j).getMov());
                scd.setMovid(listadocumentos.get(j).getMovid());

                db.insertarsubirCobroD(scd);
            }

        }
        db.close(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cobrarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static cobrarFragment newInstance(String param1, String param2) {
        cobrarFragment fragment = new cobrarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            listadocumentos = (ArrayList)bundle.getParcelableArrayList("docsSeleccionado");
            cliente=bundle.getString("cliente");
          //  formaPago=bundle.getString("formapago");
         //   referencia=bundle.getString("referencia");
           // importeFinal=bundle.getFloat("importe");

        }
        setHasOptionsMenu(true); //ESTO ES PARA  EL MENU
        setRetainInstance(true);
        if(savedInstanceState==null)
        {
            listaformaPago= new ArrayList<>();
        }

    }

    public interface CustomItemClickListener {
        public void onItemClick(View v, int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_cobrar, container, false);
        txtImporte =  view.findViewById(R.id.txtcobrarImporte);
        TextView txtSeleccionados= view.findViewById(R.id.txtcobrarSeleccionados);
       txtformaPagoImportefinal= view.findViewById(R.id.txtformaPagoImportefinal);
        lv= view.findViewById(R.id.lvcobrar);
       //recyclerView=view.findViewById(R.id.lvformasPago);
     // recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
/*
       AdaptadorCobrar ac = new AdaptadorCobrar(getActivity(),listadocumentos);
       lv.setAdapter(ac); */
        importe=0;
     /*  AdaptadorFormaPago afp = new AdaptadorFormaPago(listaformaPago, new CustomItemClickListener() {
           @Override
           public void onItemClick(View v, int position) {

           }
       });

       recyclerView.setAdapter(afp);
*/

       for(int i=0;i<listadocumentos.size();i++)
       {
           Log.i("cobrar",listadocumentos.get(i).getImporte()+"");
           if(listadocumentos.get(i).getDias()<=0 && listadocumentos.get(i).getDescuento()>0)
               importe+=listadocumentos.get(i).getImporte()*(1-(listadocumentos.get(i).getDescuento()/100));
         else  importe+=listadocumentos.get(i).getImporte();

       }
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
        txtImporte.setText(format.format(importe));
        txtSeleccionados.append(" "+listadocumentos.size());


        FloatingActionButton floatingActionButton = ((NavigationIndar) getActivity()).getFab();

        if(floatingActionButton!=null)
            floatingActionButton.hide();
     //   txtformaPagoImportefinal.setText(importeFinal+"");
        txtFormaPago = view.findViewById(R.id.txtCobrarFormaPago);
        txtFormaPago.setText(formaPago);
        txtFechaPago= view.findViewById(R.id.txtCobrarFechaPago);
        Date date = new Date();

        DateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
        txtFechaPago.setText(fmt.format(date));
        TxtReferencia= view.findViewById(R.id.txtCobrarReferencia);
        TxtReferencia.setText(referencia);


        ImageView btnagregar = view.findViewById(R.id.imgBTNCobrarAgregar);
        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFormasDPagos dfp = new DialogFormasDPagos();
                dfp.setTargetFragment(cobrarFragment.this,1);
                dfp.show(getActivity().getSupportFragmentManager(),"formapago");
            }
        });
        return view;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menuformapago,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onBackPressed() {

        boolean resultado=permiteSalir();

        if(resultado)
            return  true;
        else return  false;
    }
    public  boolean permiteSalir()
    {

        //  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(cobrarFragment.this.getActivity());
        builder.setTitle("¿Seguro de Salir? \n Si no grabaste tu cobro\n se perderá");

        builder.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setResultadoSalir(true);
                getActivity().getSupportFragmentManager().popBackStack();
                //  dialogInterface.dismiss();


            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setResultadoSalir(false);
                //   dialogInterface.dismiss();
            }
        });
        builder.show();
        Log.i("BACK","si lo detecta2");
        Log.i("BACK","valor:"+isResultadoSalir());
        if(isResultadoSalir())
            return true;
        else  return  false;
        // return  false;//isResultadoSalir();

    }

}
