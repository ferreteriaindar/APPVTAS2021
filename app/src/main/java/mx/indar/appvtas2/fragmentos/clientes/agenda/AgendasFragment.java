package mx.indar.appvtas2.fragmentos.clientes.agenda;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


import mx.indar.appvtas2.NavigationIndar;
import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.cliente;
import mx.indar.appvtas2.dbClases.visita;


import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgendasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgendasFragment extends Fragment implements TextWatcher, LocationListener, NavigationIndar.IOnBackPressed {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AdaptadorAgendas ag;
    private List<cliente> listaTemp;
    List<cliente> listaclientes = new ArrayList<>();
    private int textlength;
    private ListView lv;
    private EditText filtrador;
    private GoogleApiClient googleApiClient;

    public boolean isResultadoSalir() {
        return resultadoSalir;
    }

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;


    public Location location, Ubicacion;
    boolean gpsOK=false;


    public void setResultadoSalir(boolean resultadoSalir) {
        this.resultadoSalir = resultadoSalir;
    }

    private boolean resultadoSalir = false;

    public AgendasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgendasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgendasFragment newInstance(String param1, String param2) {
        AgendasFragment fragment = new AgendasFragment();
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
        setRetainInstance(true);
    }

//PRIMER COMENTARIOS GITHUB
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActivityCompat.requestPermissions(AgendasFragment.this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        View view = inflater.inflate(R.layout.fragment_agendas2, container, false);
        Calendar calendar = Calendar.getInstance();
        setHasOptionsMenu(true); //ESTO ES PARA  EL MENU
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //if permission is grated
            buidLocationRequest();
            buildLocationCallBack();
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        }

        //change state of button
        final int dayNumber=calendar.get(Calendar.DAY_OF_WEEK);
         filtrador = view.findViewById(R.id.txtFragmentAgendasFiltrar);
        ImageButton btnMic = view.findViewById(R.id.btnMICAgebda);
        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"NOMBRE CLIENTE");

                try
                {
                    startActivityForResult(intent,1000);

                }catch (Exception e)
                {

                }
            }
        });





        dbAdapter db = new dbAdapter(container.getContext());
        try {
            db.open(true);

            listaclientes=db.obtenerClientes(dayNumber-1);
            listaTemp=db.obtenerClientes(dayNumber-1);// Se ingresa Valor 0 para que no escoja dia , y sean TODOS los clientes.
            db.close(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ag = new AdaptadorAgendas(getActivity().getApplicationContext(),listaclientes);

         lv = view.findViewById(R.id.lvAgendas);

        lv.setAdapter(ag);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String selected =((TextView)view.findViewById(R.id.txtLayoutAgendasCte)).getText().toString();
                    final boolean primerVisita=primeraVisita(selected);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    if(primerVisita)
                    builder.setTitle("¿Empezar Visita al cliente " + selected + "?");
                    else builder.setTitle("¿Continuar Visar al cliente "+ selected +"?");

// Set up the input
                   //   final EditText input = new EditText(view.getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    //    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    // builder.setView(input);

// Set up the buttons
                    builder.setPositiveButton(" SI ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            long idvisita=0;
                         /*  GPStracker g = new GPStracker(getActivity());
                         location   = g.getLocation();
                           Log.i("gps","gps");
*/
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                                return;
                            }
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                            // Check if GPS enabled
                                if(gpsOK) {
                                    double latitude = Ubicacion.getLatitude();
                                    double longitude = Ubicacion.getLongitude();
                                    Log.i("gps", latitude + "," + longitude);
                                    Toast.makeText(getActivity(), latitude + "/" + longitude, Toast.LENGTH_SHORT).show();


//                                Log.i("gps",location.getLongitude()+"");

                                    dbAdapter db = new dbAdapter(container.getContext());
                                    try {

                                        if (primerVisita) {
                                            Log.i("primeraVisita", "primeravisita del if" + primerVisita + "");
                                            db.open(true);
                                            visita v = new visita();
                                            v.setCliente(selected);
                                            // v.setLatitud((float) location.getLatitude());
                                            //  v.setLongitud((float) location.getLongitude());
                                            v.setLatitud((float) Ubicacion.getLatitude());
                                            v.setLongitud((float) Ubicacion.getLongitude());

                                            idvisita = db.registraVisitaCte(v);
                                            Log.i("primeraVisita", "ID" + idvisita);
                                            db.close(true);
                                        }
                                        VisitaAgendas va = new VisitaAgendas();
                                        Bundle args = new Bundle();
                                        args.putLong("idVisita", idvisita);
                                        args.putString("cliente", selected);

                                        va.setArguments(args);
                                        Log.i("visita", "si crea obketo");
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.contenidoNav, va, "findThisFragment")
                                                .addToBackStack("AgendasFragment")
                                                .commit();

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                        Log.i("primeraVisita", e.getMessage());
                                    }
                                }
                                else
                                    Toast.makeText(getActivity(), "No hay señal GPS espera un Momento", Toast.LENGTH_SHORT).show();

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();

            }
        });


        filtrador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textlength = filtrador.getText().length();
                listaTemp.clear();
                for (int j = 0;j < listaclientes.size(); j++) {
                    if (textlength <= listaclientes.get(j).getNombreCliente().length()) {
                        Log.d("filtrador",listaclientes.get(j).getNombreCliente().toLowerCase().trim());
                        if (listaclientes.get(j).getNombreCliente().toLowerCase().trim().contains(
                                filtrador.getText().toString().toLowerCase().trim())) {
                            listaTemp.add(listaclientes.get(j));
                        }
                    }
                }
                AdaptadorAgendas da2 = new AdaptadorAgendas(getActivity().getApplicationContext(),listaTemp);
              lv.setAdapter(da2);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_agenda,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menuAgendas:
                Intent intent = new Intent(getActivity(),VisitasCte.class);
                getContext().startActivity(intent);


                return  true;


        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("voz",requestCode+"request");
        switch (requestCode)
        {

            case 1000: if(resultCode==RESULT_OK && null!=data) {

                        ArrayList<String> result= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        filtrador.setText(result.get(0));
                        Log.i("voz",result.get(0));
                Toast.makeText(getActivity(), result.get(0), Toast.LENGTH_SHORT).show();
                        }
                    break;



        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        this.ag.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    public  boolean primeraVisita(String cliente) {
        dbAdapter db = new dbAdapter(getActivity().getApplicationContext());
        try {
            db.open(true);
            Cursor cursor = db.rawQuery("SELECT idVisitas   \n" +
                    "  FROM visitas\n" +
                    "  where fechaInicio>= date('now', 'localtime') and cliente='"+cliente+"';");
            Log.i("primeraVisita","SELECT idVisitas   \n" +
                    "  FROM visitas\n" +
                    "  where fechaInicio>= date('now') and cliente='"+cliente+"';");
            if (cursor.getCount() <= 0 || cursor==null || !cursor.moveToFirst()) {
                cursor.close();
                db.close(true);
                return true;
            } else {
                cursor.close();
                db.close(true);
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
            return false;

    }


    @Override
    public void onLocationChanged(Location location) {
        this.location=location;
        Toast.makeText(getActivity(), location.getLatitude()+"*"+location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(getActivity(), "GPS CONECTADO", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(getActivity(), "GPS SIN SEÑAL", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onBackPressed() {

        Snackbar.make(getActivity().findViewById(R.id.contenidoNav),"Usa el menu principal",Snackbar.LENGTH_SHORT).show();
        return false;
    }




    private void buildLocationCallBack() {
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location location:locationResult.getLocations())
                   // txt.setText(String.valueOf(location.getLatitude())+"/"+String.valueOf(location.getLongitude()));
                   Ubicacion=location;
                gpsOK=true;
                Log.i("gps",Ubicacion.getLongitude()+" desde OnlocationResult");

            }
        };

    }

    private void buidLocationRequest() {
        locationRequest= new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setSmallestDisplacement(10);
    }
}
