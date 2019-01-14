package mx.indar.appvtas2.fragmentos.clientes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mx.indar.appvtas2.NavigationIndar;
import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.cliente;
import mx.indar.appvtas2.firebase.UbicacionGPS;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClientesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClientesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientesFragment extends Fragment implements NavigationIndar.IOnBackPressed {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  Context context;
    private List<cliente> listaclientes,listaTemp;
    public  ListView lv;
    public EditText filtrador;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int textlength;

    private OnFragmentInteractionListener mListener;

    public ClientesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientesFragment newInstance(String param1, String param2) {
        ClientesFragment fragment = new ClientesFragment();
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



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context=container.getContext();
        View v = (ConstraintLayout) inflater.inflate(R.layout.fragment_clientes, container, false);
        lv = v.findViewById(R.id.lvFragmentClientes);
        dbAdapter db = new dbAdapter(context);
        try {

            db.open(true);
            listaclientes=db.obtenerClientes(0);  // Se ingresa Valor 0 para que no escoja dia , y sean TODOS los clientes.
            listaTemp=db.obtenerClientes(0);
            db.close(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

       // ArrayAdapter<cliente> adapter = new ArrayAdapter<cliente>(context,android.R.layout.simple_list_item_1,listaclientes);

        lv.setAdapter(new AdaptadorClientes(getActivity().getApplicationContext(),listaclientes,ClientesFragment.this));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("click si","siii");

            }
        });


        FloatingActionButton floatingActionButton = ((NavigationIndar) getActivity()).getFab();

        if(floatingActionButton!=null)
            floatingActionButton.hide();


        filtrador=(EditText) v.findViewById(R.id.txtBuscarClientes);
        filtrador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               textlength=filtrador.getText().length();
                listaTemp.clear();
                for(int j=0;j<listaclientes.size();j++)
                {
                    if (textlength <= listaclientes.get(j).getNombreCliente().length()) {
                        Log.d("filtrador",listaclientes.get(j).getNombreCliente().toLowerCase().trim());
                        if (listaclientes.get(j).getNombreCliente().toLowerCase().trim().contains(
                                filtrador.getText().toString().toLowerCase().trim())) {
                            listaTemp.add(listaclientes.get(j));
                        }
                    }
                }
               AdaptadorClientes adapter =  new AdaptadorClientes(getActivity().getApplication(),listaTemp,ClientesFragment.this);
                lv.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ImageView btnMic = v.findViewById(R.id.btnMicClientes);
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


        return   v;//  inflater.inflate(R.layout.fragment_clientes, container, false);






    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       context=activity.getApplicationContext();
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onBackPressed() {
        Snackbar.make(getActivity().findViewById(R.id.contenidoNav),"Usa el menu principal",Snackbar.LENGTH_SHORT).show();
        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
