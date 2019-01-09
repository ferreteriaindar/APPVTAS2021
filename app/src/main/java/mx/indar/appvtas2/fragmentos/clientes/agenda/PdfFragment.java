package mx.indar.appvtas2.fragmentos.clientes.agenda;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;

import java.io.File;

import mx.indar.appvtas2.NavigationIndar;
import mx.indar.appvtas2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PdfFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PdfFragment extends Fragment  implements NavigationIndar.IOnBackPressed {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public boolean isResultadoSalir() {
        return resultadoSalir;
    }

    public void setResultadoSalir(boolean resultadoSalir) {
        this.resultadoSalir = resultadoSalir;
    }

    private boolean resultadoSalir=false;
    View view;


    public PdfFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PdfFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PdfFragment newInstance(String param1, String param2) {
        PdfFragment fragment = new PdfFragment();
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
        view= inflater.inflate(R.layout.fragment_pdf, container, false);


        PDFView pdfView = view.findViewById(R.id.pdfFragmentView);
        File ruta= new File(Environment.getExternalStorageDirectory(), "/IndarApp/PDF/FI.PDF");
        pdfView.fromFile(ruta).load();
      /*  view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {


                if( i== KeyEvent.KEYCODE_BACK )
                {

                    if(permiteSalir())
                        getActivity().getSupportFragmentManager().popBackStack();





                    return true;
                }
                getActivity().getSupportFragmentManager().popBackStack();
                return false;
            }
        });*/




        return  view;
    }





    public  boolean permiteSalir()
    {

      //  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(PdfFragment.this.getActivity());
        builder.setTitle("¿Terminaste  con las Promociones?");

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


    @Override
    public boolean onBackPressed() {

        Log.i("BACK","si lo detecta1");

        boolean resultado=permiteSalir();

        if(resultado)
            return  true;
        else return  false;

    }




}
