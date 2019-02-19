package mx.indar.appvtas2.fragmentos.clientes.cxc;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import mx.indar.appvtas2.R;

public class DialogFormasDPagos extends DialogFragment {
    public  View view;
    public Spinner spinnerForma,spinnerBanco;
    public EditText importe,referencia;
    public TextView lblbanco,lblref,txtfechacheque;
    public Button btnAceptar,btnCancelar;
    private  DatePickerDialog.OnDateSetListener mDateSetListener;
  public   int year,month,day,años,mes,dia;
  String mesString;
    Calendar calendar;

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
        txtfechacheque= view.findViewById(R.id.txtfechaCheque);
        txtfechacheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                 year=cal.get(Calendar.YEAR);
                 month=cal.get(Calendar.MONTH);
                 day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener,year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                txtfechacheque.setText(day+"/"+month+1+"/"+year);
                años=year;
                mes=month+1;
                dia=day;
                mesString=month+1+"";
                Log.i("cheque",year+"/"+month+1+"/"+day+"datepicker");
            }
        };

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
                Log.i("pago","pago"+importe.getText().toString()+"pago");
                if(!TextUtils.isEmpty( importe.getText().toString()))
                checarDatos();
                else
                    importe.setError("Ingresa Importe");
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
        else
        {
            bundle.putString("banco","");
            bundle.putString("referencia","N/A");
        }
        bundle.putInt("año",años);
        bundle.putInt("mes",mes);
        bundle.putInt("dia",dia);


        Intent intent = new Intent().putExtras(bundle);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

        dismiss();



    }
}
