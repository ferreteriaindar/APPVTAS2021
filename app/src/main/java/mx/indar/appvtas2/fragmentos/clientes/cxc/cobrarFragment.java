package mx.indar.appvtas2.fragmentos.clientes.cxc;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.security.PublicKey;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    TextView txtImporte;
    public String cliente;

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

                }
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
                getActivity().getSupportFragmentManager().popBackStack();


                Log.i("cxc","boton registracobro");

                return  true;


        }

        return super.onOptionsItemSelected(item);
    }



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
       recyclerView=view.findViewById(R.id.lvformasPago);
      recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       AdaptadorCobrar ac = new AdaptadorCobrar(getActivity(),listadocumentos);
       lv.setAdapter(ac);
        importe=0;
       AdaptadorFormaPago afp = new AdaptadorFormaPago(listaformaPago, new CustomItemClickListener() {
           @Override
           public void onItemClick(View v, int position) {

           }
       });

       recyclerView.setAdapter(afp);


       for(int i=0;i<listadocumentos.size();i++)
       {
           Log.i("cobrar",listadocumentos.get(i).getImporte()+"");
           importe+=listadocumentos.get(i).getImporte();

       }
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
        txtImporte.setText(format.format(importe));
        txtSeleccionados.append(" "+listadocumentos.size());
        img = view.findViewById(R.id.imgcobrarbtn);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFormasDPagos dfp = new DialogFormasDPagos();
                dfp.setTargetFragment(cobrarFragment.this,1);
                dfp.show(getActivity().getSupportFragmentManager(),"formapago");
            }
        });

        FloatingActionButton floatingActionButton = ((NavigationIndar) getActivity()).getFab();

        if(floatingActionButton!=null)
            floatingActionButton.hide();
        txtformaPagoImportefinal.setText(importeFinal+"");
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
