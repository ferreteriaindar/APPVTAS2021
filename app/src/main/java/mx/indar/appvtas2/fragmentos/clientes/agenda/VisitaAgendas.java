package mx.indar.appvtas2.fragmentos.clientes.agenda;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.indar.appvtas2.GPStracker;
import mx.indar.appvtas2.NavigationIndar;
import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.fragmentos.clientes.cxc.cxcFragment;
import mx.indar.appvtas2.fragmentos.clientes.promocionales.encuesta;
import mx.indar.appvtas2.fragmentos.clientes.promocionales.promocionalesVisita;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VisitaAgendas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisitaAgendas extends Fragment implements NavigationIndar.IOnBackPressed {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    HorizontalStepView hStepView;
    public  List<StepBean> stepsBeanList = new ArrayList<>();
   public StepBean stepBean0;
    public StepBean stepBean1;
    public StepBean stepBean2;
    public StepBean stepBean3;
    public StepBean stepBean4;
    public  Button btnCxc;
    public  Button btnPromo;



    View view;
    Long idVisita;
    String cliente;
    public boolean isResultadoSalir() {
        return resultadoSalir;
    }

    public void setResultadoSalir(boolean resultadoSalir) {
        this.resultadoSalir = resultadoSalir;
    }

    private boolean resultadoSalir=false;

    public VisitaAgendas() {
        // Required empty public constructor


    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VisitaAgendas.
     */
    // TODO: Rename and change types and number of parameters
    public static VisitaAgendas newInstance(String param1, String param2) {
        VisitaAgendas fragment = new VisitaAgendas();
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
        view= inflater.inflate(R.layout.fragment_visita_agendas, container, false);

        Bundle b =getArguments();
        cliente=b.getString("cliente");
        if(b.getLong("idVisita")==0)
            idVisita=regresaIDVisita(cliente);
        else idVisita=b.getLong("idVisita");

        btnCxc = view.findViewById(R.id.btnVisitaCobrar);
        btnPromo= view.findViewById(R.id.btnFragmentVisita);
        hStepView = (HorizontalStepView) view.findViewById(R.id.horizontalStepView);
         stepBean0 = new StepBean("Inicio",1);
         stepBean1 = new StepBean("Cobranza",0);
         stepBean2 = new StepBean("Promo",-1);
         stepBean3 = new StepBean("Pedido",-1);
         stepBean4 = new StepBean("PostVenta",-1);
        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);
        stepsBeanList.add(stepBean3);
        stepsBeanList.add(stepBean4);
       // generaStepView();
       // btnCxc.setEnabled(false);
      //  btnCxc.setClickable(false);
      //  btnCxc.invalidate();
        btnCxc.setBackgroundColor(Color.GRAY);
        actualizarVisitaFLUJO();


        btnCxc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                cxcFragment cxc =new cxcFragment();
                Bundle b = new Bundle();
                b.putString("cliente",cliente);
                b.putLong("idVisita",idVisita);
                cxc.setArguments(b);


                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenidoNav, cxc, "cxcFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!revisaDuplicadoAvance(idVisita,"promo"))
                {
                    GPStracker g = new GPStracker(getActivity());
                    Location l = g.getLocation();
                    Log.i("gps","gps");
                    Log.i("gps",l.getLongitude()+"");
                        actualizaBaseAvance(idVisita,"promo",(float)l.getLatitude(),(float)l.getLongitude());



                }PdfFragment pdff =new PdfFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenidoNav, pdff, "pdfFragment")
                        .addToBackStack(null)
                        .commit();
                /**/
                Intent intent = new Intent(getContext(),promocionalesVisita.class);
                intent.putExtra("idvisita",idVisita);
                startActivity(intent);

            }
        });

        return  view;
    }



    // automatic turn off the gps
    public void turnGPSOff()
    {
        String provider = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.getContext().sendBroadcast(poke);
        }
    }
    public  long regresaIDVisita(String cliente) {
        dbAdapter db = new dbAdapter(getActivity().getApplicationContext());
        try {
            db.open(true);
            Cursor cursor = db.rawQuery("SELECT idVisitas   \n" +
                    "  FROM visitas\n" +
                    "  where fechaInicio>= date('now') and cliente='"+cliente+"' order by fechainicio desc  limit 1;");

            if (cursor.getCount() <= 0 || cursor==null || !cursor.moveToFirst()) {
                cursor.close();
                db.close(true);
                return 0;
            } else {
                long id=cursor.getLong(cursor.getColumnIndex("idVisitas"));
                Log.i("visita",id+"");
                cursor.close();
                db.close(true);
                return id;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }


    public  void actualizarVisitaFLUJO()
    {
        int avance=0;
        dbAdapter db = new dbAdapter(getActivity().getApplicationContext());
        try {
            db.open(true);
            Cursor cursor = db.rawQuery("SELECT  CASE WHEN FECHACOBRANZA IS NULL  THEN 1 \n" +
                    "            WHEN FECHAPROMOCIONES IS NULL  THEN 2\n" +
                    "            WHEN FECHAVENTA IS NULL THEN 3\n" +
                    "            ELSE 4\n" +
                    "        END as avance \n" +
                    "  FROM visitas where idVisitas="+idVisita);

            if (cursor.getCount() <= 0 || cursor==null || !cursor.moveToFirst()) {
                cursor.close();
                db.close(true);

            } else {
                avance=cursor.getInt(cursor.getColumnIndex("avance"));

                cursor.close();
                db.close(true);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        stepsBeanList.clear();
        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);
        stepsBeanList.add(stepBean3);
        stepsBeanList.add(stepBean4);


        switch (avance)
        {
            case 1:// stepBean1.setState(1);
            break;
            case 2:  stepBean1.setState(1);
                     stepBean2.setState(0);

            break;
            case 3:  stepBean1.setState(1);
                     stepBean2.setState(1);
                     stepBean3.setState(0);
                btnPromo.setBackgroundColor(Color.GRAY);
            break;
            case 4:   stepBean1.setState(1);
                    stepBean2.setState(1);
                    stepBean3.setState(1);
                    stepBean4.setState(1);
                break;
        }


       // hStepView.animate().start();
        hStepView.setStepViewTexts(stepsBeanList)//总步骤
                .setTextSize(12)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_dark))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getActivity(), R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(getActivity(), android.R.color.black))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getActivity(), android.R.color.white))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_ok))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getActivity(), R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getActivity(), R.drawable.attention));//设置StepsViewIndicator AttentionIcon


    }



    public  void generaStepView()
    {
       /* hStepView = (HorizontalStepView) view.findViewById(R.id.horizontalStepView);
        List<StepBean> stepsBeanList = new ArrayList<>();

        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);
        stepsBeanList.add(stepBean3);
        stepsBeanList.add(stepBean4);

*/


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
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VisitaAgendas.this.getActivity());
        builder.setTitle("¿Seguro de salir del Cliente?");

        builder.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                GPStracker g = new GPStracker(getActivity());
                Location l = g.getLocation();
                Log.i("gps","gps");
                Log.i("gps",l.getLongitude()+"");
                actualizaBaseAvance(idVisita,"fin",(float)l.getLatitude(),(float)l.getLongitude());

                setResultadoSalir(true);
                Intent intent = new Intent(getContext(),encuesta.class);
                intent.putExtra("idVisita",idVisita);
                intent.putExtra("cliente",cliente);
                startActivity(intent);

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
