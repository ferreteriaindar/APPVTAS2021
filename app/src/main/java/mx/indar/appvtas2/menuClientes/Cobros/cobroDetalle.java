package mx.indar.appvtas2.menuClientes.Cobros;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mx.indar.appvtas2.Bluetooth.Impresora;
import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.MenuClientes.Cobro;
import mx.indar.appvtas2.dbClases.MenuClientes.CobroD;

public class cobroDetalle extends AppCompatActivity {


    private  int idCobro;
    private List<Cobro> cobro;
    private List<CobroD> cobroD;
    private  String cliente;
    TextView numrecibo,importe,txtCliente,formapago,fechapago,referencia;
    ListView lv  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cobro_detalle);

        idCobro=getIntent().getIntExtra("cobro",1);
        cliente=getIntent().getStringExtra("cliente");
        dbAdapter db = new dbAdapter(getApplicationContext());
        try {
            db.open(true);
            cobro=db.SelectCobro(cliente,idCobro);
            cobroD=db.selectCobroD(idCobro);
            db.close(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        numrecibo=findViewById(R.id.txtlayoutCobroDetalleNum);
        importe=findViewById(R.id.txtlayoutCobroDetalleImporte);
        txtCliente=findViewById(R.id.txtlayoutCobroDetalleCliente);
        formapago=findViewById(R.id.txtlayoutCobroDetalleFormaPago);
        fechapago=findViewById(R.id.txtlayoutCobroDetalleFechaPago);
        referencia=findViewById(R.id.txtlayoutCobroDetalleReferencia);

        numrecibo.setText(cobro.get(0).getNumCobro()+"");
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
        importe.setText(format.format(cobro.get(0).getImporte())+"");
        txtCliente.setText(cobro.get(0).getCliente());
        formapago.setText(cobro.get(0).getFormaPago());
        lv=findViewById(R.id.lvCobroDetalle);
        Log.i("cobro","tamaño"+cobroD.size());
        adaptadorCobroDetalle adapter = new adaptadorCobroDetalle(getApplicationContext(),cobroD);
        lv.setAdapter(adapter);

        SimpleDateFormat formatfecha = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = formatfecha.parse(cobro.get(0).getFechaPago());
            fechapago.setText(formatfecha.format(cobro.get(0).getFechaPago()));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("cobro","error"+e.getMessage());

        }
        fechapago.setText(cobro.get(0).getFechaPago());
        referencia.setText(cobro.get(0).getReferencia());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menucxc,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menucxcprint:
                imprimirCobro();



                return  true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void imprimirCobro() {
        Log.i("cobro","entra a imprimir");
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
        Impresora imp = new Impresora(cobroDetalle.this);
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
                    imp.printDATA("RECIBO DE COBRO: "+numrecibo.getText().toString()+"");
                    imp.printDATA("CLIENTE: "+cliente);
                    SharedPreferences prefs= getApplicationContext().getSharedPreferences(getResources().getString(R.string.action_settings), MODE_PRIVATE);
                    imp.printDATA("VENDEDOR: "+prefs.getString("nombre",""));
                    imp.printDATA("----------------------------------------------");
                    imp.printDATA("");
                    for(int i=0;i<cobroD.size();i++)
                    {
                        String linea="";
                        linea=cobroD.get(i).getMov()+" "+cobroD.get(i).getMovid()+"  ";//+"  $"+listadocumentos.get(i).getImporte()+"    ";
                        String  descto;


                                descto = cobroD.get(i).getDescuento() + "%";
                                linea+=descto+"  ";
                                linea+=String.format("%1$"+ 15 + "s",format.format(cobroD.get(i).getImporte()) );
                                //listadocumentos.get(i).getImporte() * (1 - (listadocumentos.get(i).getDescuento() / 100));
                                imp.printDATA(linea);

                    }
                    imp.printDATA("----------------------------------------------");
                    imp.printDATA("IMPORTE PAGADO                   "+importe.getText().toString());
                    imp.printDATA("");
                    imp.printDATA("METODO:"+formapago.getText().toString());
                    imp.printDATA("FECHAPAGO: "+fechapago.getText().toString());
                    imp.printDATA("REFERENCIA: "+referencia.getText().toString());
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
}
