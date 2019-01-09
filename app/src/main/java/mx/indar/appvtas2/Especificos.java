package mx.indar.appvtas2;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.zip.CheckedOutputStream;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.Direction;
import mx.indar.appvtas2.dbClases.especificos;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Especificos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Especificos extends Fragment implements NavigationIndar.IOnBackPressed{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CircleProgressView circle;
    View view;
    List<especificos> listaEspecificos;
    TextView cuota,venta,titulo1,titulo2,titulo3,titulo4,cuota1,cuota2,cuota3,cuota4,venta1,venta2,venta3,venta4,porciento1,porciento2,porciento3,porciento4;

    public Especificos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Especificos.
     */
    // TODO: Rename and change types and number of parameters
    public static Especificos newInstance(String param1, String param2) {
        Especificos fragment = new Especificos();
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
        view= inflater.inflate(R.layout.fragment_especificos, container, false);
        circle= view.findViewById(R.id.circleEspecifico);
        circle.setMaxValue(100);
        //circle.setValue(57);
        circle.setValueAnimated(57);
        circle.setDirection(Direction.CW);
        circle.setText("0");
        circle.setBarColor(Color.parseColor("#f9aa33"),Color.parseColor("#f9aa33"));
        circle.setUnit("%");
        circle.setUnitVisible(true);
        circle.setUnitSize(50);
        circle.setTextSize(200);
        circle.setTextColor(Color.parseColor("#344955"));
        circle.setOuterContourSize(0);
        circle.setInnerContourSize(0);
        circle.setBarStrokeCap(Paint.Cap.ROUND);
        circle.setRimColor(Color.parseColor("#eeeeee"));
        cuota= view.findViewById(R.id.txtespecificosCuota);
        venta=view.findViewById(R.id.txtespecificosVenta);
        titulo1=view.findViewById(R.id.txtespecificostTitulo1);
        titulo2=view.findViewById(R.id.txtespecificostTitulo2);
        titulo3=view.findViewById(R.id.txtespecificostTitulo3);
        titulo4=view.findViewById(R.id.txtespecificostTitulo4);
        cuota1=view.findViewById(R.id.txtespecificostCuota1);
        cuota2=view.findViewById(R.id.txtespecificostCuota2);
        cuota3=view.findViewById(R.id.txtespecificostCuota3);
        cuota4=view.findViewById(R.id.txtespecificostCuota4);
        venta1=view.findViewById(R.id.txtespecificosVenta1);
        venta2=view.findViewById(R.id.txtespecificosVenta2);
        venta3=view.findViewById(R.id.txtespecificosVenta3);
        venta4=view.findViewById(R.id.txtespecificosVenta4);
        porciento1=view.findViewById(R.id.txtespecificosPorciento1);
        porciento2=view.findViewById(R.id.txtespecificosPorciento2);
        porciento3=view.findViewById(R.id.txtespecificosPorciento3);
        porciento4=view.findViewById(R.id.txtespecificosPorciento4);
        if(cargaDatos())
        circle.setValueAnimated(listaEspecificos.get(0).getPorcentaje());



        return view;
    }

    public  boolean cargaDatos()
    {
        dbAdapter db = new dbAdapter(getActivity());
        try {
            db.open(true);
            listaEspecificos =db.obtenerEspecifico();
            db.close(true);
            if(listaEspecificos.size()>0) {
                especificos e = new especificos();

                e.setCuota(listaEspecificos.get(0).getCuota());
                e.setVenta(listaEspecificos.get(0).getVenta());
                e.setPorcentaje(listaEspecificos.get(0).getPorcentaje());
                e.setEspecificoNombre1(listaEspecificos.get(0).getEspecificoNombre1());
                e.setEspecificoCuota1(listaEspecificos.get(0).getEspecificoCuota1());
                e.setEspecificoVenta1(listaEspecificos.get(0).getEspecificoVenta1());
                e.setEspecificoNombre2(listaEspecificos.get(0).getEspecificoNombre2());
                e.setEspecificoCuota2(listaEspecificos.get(0).getEspecificoCuota2());
                e.setEspecificoVenta2(listaEspecificos.get(0).getEspecificoVenta2());
                e.setEspecificoNombre3(listaEspecificos.get(0).getEspecificoNombre3());
                e.setEspecificoCuota3(listaEspecificos.get(0).getEspecificoCuota3());
                e.setEspecificoVenta3(listaEspecificos.get(0).getEspecificoVenta3());
                e.setEspecificoNombre4(listaEspecificos.get(0).getEspecificoNombre4());
                e.setEspecificoCuota4(listaEspecificos.get(0).getEspecificoCuota4());
                e.setEspecificoVenta4(listaEspecificos.get(0).getEspecificoVenta4());
                //PONER DATOS EN LOS TEXTVIEW
                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
                NumberFormat format1 = NumberFormat.getCurrencyInstance(Locale.CANADA);
                            format1.setParseIntegerOnly(true);
                            format1.setRoundingMode(RoundingMode.DOWN);
                            format1.setMaximumFractionDigits(0);
                          //  format1.setMaximumIntegerDigits(0);
                DecimalFormat df = new DecimalFormat("##.0");
                DecimalFormat df1  = new DecimalFormat("#.#");
                cuota.setText(format.format(e.getCuota()));
                venta.setText(format.format(e.getVenta()));
                //circle.setValue(e.getPorcentaje());
                titulo1.setText(e.getEspecificoNombre1());
                titulo2.setText(e.getEspecificoNombre2());
                titulo3.setText(e.getEspecificoNombre3());
                titulo4.setText(e.getEspecificoNombre4());
                cuota1.setText( format1.format(Math.round(e.getEspecificoCuota1()))+"");
                cuota2.setText(format1.format(Math.round( e.getEspecificoCuota2())));
                cuota3.setText(format1.format(Math.round( e.getEspecificoCuota3())));
                cuota4.setText(format1.format(Math.round(e.getEspecificoCuota4())));
                venta1.setText(format1.format(Math.round( e.getEspecificoVenta1())));
                venta2.setText(format1.format(Math.round(e.getEspecificoVenta2())));
                venta3.setText(format1.format(Math.round(e.getEspecificoVenta3())));
                venta4.setText(format1.format(Math.round(e.getEspecificoVenta4())));
                porciento1.setText( df.format( e.getEspecificoVenta1() / e.getEspecificoCuota1()*100.0) +"");
                porciento2.setText(df.format( e.getEspecificoVenta2() / e.getEspecificoCuota2()*100.0) + "");
                porciento3.setText( df.format( e.getEspecificoVenta3() / e.getEspecificoCuota3()*100.0) + "");
                porciento4.setText( df.format( e.getEspecificoVenta4() / e.getEspecificoCuota4()*100.0) + "");
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
            return false;
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }
}
