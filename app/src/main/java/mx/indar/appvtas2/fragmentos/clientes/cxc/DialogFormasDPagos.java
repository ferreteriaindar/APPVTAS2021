package mx.indar.appvtas2.fragmentos.clientes.cxc;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import mx.indar.appvtas2.R;

public class DialogFormasDPagos extends DialogFragment {
    public  View view;
    public Spinner spinnerForma,spinnerBanco;
    public EditText importe,referencia;
    public TextView lblbanco,lblref;
    public Button btnAceptar,btnCancelar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        view= inflater.inflate(R.layout.dialogformapago,container,false);
        getDialog().setTitle("FORMAS DE PAGO");
        importe=view.findViewById(R.id.txtdialogcobrarimporte);
        referencia=view.findViewById(R.id.txtdialogcobrarRef);
        lblbanco=view.findViewById(R.id.labeldialogbanco);
        lblref=view.findViewById(R.id.labeldialogref);
        spinnerForma= view.findViewById(R.id.spinnerdialogcobrar);
        spinnerBanco= view.findViewById(R.id.spinnerdialogcobrarbanco);
        spinnerForma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinnerForma.getItemAtPosition(i).toString().equals("Efectivo"))
                {
                    lblbanco.setVisibility(View.INVISIBLE);
                    spinnerBanco.setVisibility(View.INVISIBLE);
                    referencia.setVisibility(View.INVISIBLE);
                    lblref.setVisibility(View.INVISIBLE);
                }else
                {
                    lblbanco.setVisibility(View.VISIBLE);
                    spinnerBanco.setVisibility(View.VISIBLE);
                    referencia.setVisibility(View.VISIBLE);
                    lblref.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCancelar = view.findViewById(R.id.btnDialogpagoCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnAceptar= view.findViewById(R.id.btnDialogpagoAceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checarDatos();
            }
        });



        return view;
    }


    public  void onCerrarDialogformapago(View view)
    {

        dismiss();
    }

    public  void checarDatos()
    {
        Bundle bundle = new Bundle();
        bundle.putString("formapago", spinnerForma.getSelectedItem().toString());
        bundle.putString("importe",importe.getText().toString());
        if(!spinnerForma.getSelectedItem().toString().equals("Efectivo"))
        {
            bundle.putString("banco",spinnerBanco.getSelectedItem().toString());
            bundle.putString("referencia",referencia.getText().toString());
        }


        Intent intent = new Intent().putExtras(bundle);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

        dismiss();



    }
}
