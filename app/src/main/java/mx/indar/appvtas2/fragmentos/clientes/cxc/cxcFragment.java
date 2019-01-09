package mx.indar.appvtas2.fragmentos.clientes.cxc;


import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.indar.appvtas2.Bluetooth.Impresora;
import mx.indar.appvtas2.GPStracker;
import mx.indar.appvtas2.NavigationIndar;
import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.cxcCliente;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link cxcFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cxcFragment extends Fragment implements NavigationIndar.IOnBackPressed{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public  View view;
    private RecyclerView recyclerView;
    private AdaptadorCXC adaptadorCXC;
    private List<cxcCliente> listacliente;
    private  String cliente="";
    public  float totalImporte=0;
    public TextView txtImporte;
    public  long idVisita;
    public boolean isResultadoSalir() {
        return resultadoSalir;
    }

    public void setResultadoSalir(boolean resultadoSalir) {
        this.resultadoSalir = resultadoSalir;
    }

    private boolean resultadoSalir=false;
    Toolbar toolbar;


    public cxcFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cxcFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static cxcFragment newInstance(String param1, String param2) {
        cxcFragment fragment = new cxcFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.menucxc,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menucxcprint:
                Toast.makeText(getActivity(), "Imprimiendo", Toast.LENGTH_SHORT).show();
                imprimirTodo();
                return  true;


        }
        return super.onOptionsItemSelected(item);
    }


    public boolean imprimirTodo()
    {
        Impresora imp = new Impresora(getActivity(),this);
        if(imp.FindBluetoothDevice())
        {
            try {
                imp.openBluetoothPrinter();
                int width = 47;
                char fill = '\u00A0';
                String linea;
                String padded;
                //imp.printPhoto(R.drawable.ic_cobrar);
                imp.ImprimirCabezeraTicket();
                imp.printDATA("");

                imp.printDATA("----------------------------------------------");
                for(int i=0;i<listacliente.size();i++) {

                    linea=listacliente.get(i).getMov()+" "+listacliente.get(i).getMovID();
                   // padded = new String( linea+new char[width - linea.length()]).replace('\0', fill) ;
                    padded=String.format("%-10s",linea).replace(' ',' ');
                    imp.printDATA(padded);
                    linea=listacliente.get(i).getReferencia();
                    padded=String.format("%-10s",linea).replace(' ',' ');
                    imp.printDATA(" EMISION       VENCIMIENTO            DIAS\t    ");
                    String vencimiento = "", fechaemision = "";
                    try {


                        String pattern = "dd/MM/yyyy";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(pattern);
                        Date newDatE = null, newdate2;

                        newDatE = simpleDateFormat.parse(listacliente.get(i).getVencimiento());
                        newdate2 = simpleDateFormat2.parse(listacliente.get(i).getFechaEmision());

                        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
                        vencimiento = simpleDateFormat.format(newDatE);
                        fechaemision = simpleDateFormat2.format(newdate2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    linea=fechaemision+"         "+vencimiento+"            "+listacliente.get(i).getDiasMoratorios();
                    padded=String.format("%-10s",linea).replace(' ',' ');
                    imp.printDATA(padded);

                    linea="IMPORTE ----------->>       $"+listacliente.get(i).getSaldo();
                    padded=String.format("%-10s",linea).replace(' ',' ');
                    imp.printDATA("$"+padded);
                    imp.printDATA("----------------------------------------------");



                }
                imp.printDATA("");
                imp.printDATA("");
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
        else Toast.makeText(getActivity(), "Problemas con la Impresora", Toast.LENGTH_SHORT).show();

        try {
            imp.disconnectBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
            cliente = bundle.getString("cliente", "");
            idVisita=bundle.getLong("idVisita",0);
        }
        setHasOptionsMenu(true); //ESTO ES PARA  EL MENU
        this.setRetainInstance(true);
    }

    public interface CustomItemClickListener {
        public void onItemClick(View v, int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_cxc, container, false);
        toolbar = ((NavigationIndar) getActivity()).getToolbar();






        recyclerView=(RecyclerView)view.findViewById(R.id.rvCXC);
        txtImporte=view.findViewById(R.id.txtLayoutImportetotalCxc);
        FloatingActionButton floatingActionButton = ((NavigationIndar) getActivity()).getFab();
        if(floatingActionButton!=null)
        floatingActionButton.hide();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        dbAdapter db = new dbAdapter(getActivity());
        try {
            db.open(true);
            listacliente=db.regresaCXCCliente(cliente);
            adaptadorCXC = new AdaptadorCXC(listacliente, new CustomItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
/*                    Toast.makeText(getActivity(), listacliente.get(position).getSaldo()+"", Toast.LENGTH_SHORT).show();


                    if(listacliente.get(position).isCheck()==false)
                    {  totalImporte+=totalImporte+Float.parseFloat(listacliente.get(position).getSaldo()+"");
                    txtImporte.setText(totalImporte+"");
                        //
                        }
                       */
                    ArrayList<String> selectedStrings = new ArrayList<String>();
                    selectedStrings=adaptadorCXC.selectedStrings;
                    totalImporte=0;
                    for(int i=0;i<selectedStrings.size();i++) {

                        totalImporte+=Float.parseFloat(selectedStrings.get(i).toString());
                    }
                    txtImporte.setText("$"+String.format("%.2f",totalImporte));
                    if(totalImporte==0)
                        txtImporte.setTextColor(Color.parseColor("#1b5e20"));
                    if(totalImporte<0)
                        txtImporte.setTextColor(Color.parseColor("#b71c1c"));
                    if(totalImporte>0)
                        txtImporte.setTextColor(Color.parseColor("#808080"));
                }
            });
            db.close(true);

            recyclerView.setAdapter(adaptadorCXC);


    } catch (SQLException e) {
            e.printStackTrace();
        }


        Button btnCxc = view.findViewById(R.id.btnCXCcobrar);
        btnCxc.setBackgroundColor(Color.parseColor("#f57c00"));
        btnCxc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(adaptadorCXC.docsSelecionados.size()>=0)
                {

                    Bundle b = new Bundle();
                  cobrarFragment cf = new cobrarFragment();
                  b.putParcelableArrayList("docsSeleccionado",(ArrayList)adaptadorCXC.docsSelecionados );
                  b.putString("cliente",cliente);
                  cf.setArguments(b);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contenidoNav, cf, "cobrarfagment")
                            .addToBackStack( null)
                            .commit();
                }
                else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content),"Selecciona al menos un documento",Snackbar.LENGTH_LONG).show();
                }
            }
        });

        return  view;
    }



    public  void actualizaBaseAvance(long idVisita,String modulo,float latitud,float longitud)
    {
        dbAdapter db = new dbAdapter(getActivity().getApplicationContext());
        try {
            db.open(true);
            db.actualizaAvanceVisita((int)idVisita,modulo,latitud,longitud);
            db.close(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }



    public  boolean permiteSalir()
    {

        //  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(cxcFragment.this.getActivity());
        builder.setTitle("¿Terminaste  con la Cobranza?");

        builder.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(!revisaDuplicadoAvance(idVisita,"cobranza")) {
                    GPStracker g = new GPStracker(getActivity());
                    Location l = g.getLocation();

                    actualizaBaseAvance(idVisita, "cobranza", (float) l.getLatitude(), (float) l.getLongitude());
                }
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


    public  boolean revisaDuplicadoAvance(long idVisita,String modulo)
    {
        String campo="";
        switch (modulo)
        {
            case "cobranza":  campo="fechaCobranza";
                break;
            case "promo":  campo="fechaPromociones";
                break;
            case "Venta": campo="fechaVenta";
                break;
            case "PostVenta": campo="fechaPostVenta";
                break;

        }


        dbAdapter db = new dbAdapter(getActivity().getApplicationContext());
        try {
            db.open(true);
            Cursor cursor = db.rawQuery("SELECT  CASE WHEN "+campo+" IS NULL  THEN 1  ELSE 0 END as avance   FROM visitas where idVisitas="+idVisita);

            if (cursor.getCount() <= 0 || cursor==null || !cursor.moveToFirst()) {
                cursor.close();
                db.close(true);

            } else {
                int resultado=cursor.getInt(cursor.getColumnIndex("avance"));
                cursor.close();
                db.close(true);
                if(resultado==1) // no es repetido se puede hacer el insert
                    return  false;
                else return true;



            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onBackPressed() {

        boolean resultado=permiteSalir();

        if(resultado)
            return  true;
        else return  false;
    }




}
