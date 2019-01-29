package mx.indar.appvtas2.Bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Set;
import java.util.UUID;
import mx.indar.appvtas2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlueToothController#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlueToothController extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;

    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile  boolean stopWorker;
    View V;

    public BlueToothController() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlueToothController.
     */
    // TODO: Rename and change types and number of parameters
    public static BlueToothController newInstance(String param1, String param2) {
        BlueToothController fragment = new BlueToothController();
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

            V=inflater.inflate(R.layout.fragment_bluetoothcontroller, container, false);

        Button btnAbrir =  (Button) V.findViewById(R.id.btnBluetoothAbrir);
        Button btnBluetoothImprimir = (Button) V.findViewById(R.id.btnBluetoothImprimir);
        Button btnBluetoohDesconectar =(Button) V.findViewById(R.id.btnBluetoohDesconectar);
        btnAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                FindBluetoothDevice();
                openBluetoothPrinter();
                    Snackbar.make(view, "Impresora Conectada", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        });

        btnBluetoothImprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try{
              /*  printDATA("EL BETO \n");
                printDATA("");
                printDATA("SE la COME....");*/

                printDATA("***FERRETERIA INDAR****");
                printDATA("***********************");
                printDATA("************************************************");
                printDATA("\n");
                printDATA("IMPRESION DE TICKET PRUEBA 1 \n");
                printDATA("\n");
                printDATA("|Articulo|     |Cantidad|    |IMPORTE|");
                printDATA("DISCO PARA CORTE DE ACERO INOXIDABLE 4.1/2\"X1MMX7/8\" SUPER DELGADO (PREMIUM)");
                printDATA("|A4 750|       |15      |      |$454.8|");
                printDATA("PILA ALCALINA AA ENERGIZER MAX C/4 PZAS BLISTER");
                printDATA("|E1 E91BP-4|    |10      |      |$397.4|");




               } catch (IOException e) {
                   e.printStackTrace();
               }
            }
        });
        btnBluetoohDesconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //printPhoto();
                try {
                    disconnectBT();
                    Snackbar.make(view, "Impresora Desconectada", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return V;
    }


    public  void onAbrirBT(View v)
    {
                 try {
                FindBluetoothDevice();
                openBluetoothPrinter();

                // disconnectBT();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }


    public void printPhoto() {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    R.drawable.pollo);
            if(bmp!=null){
                UtilsPrinter up = new UtilsPrinter();

                byte[] command = up.decodeBitmap(bmp);

              printDATAIMG(command);
            }else{
                Log.e("bluetooth", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bluetooth", "the file isn't exists");
        }
    }




    void FindBluetoothDevice()
    {
        try
        {
             bluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
             if(bluetoothAdapter==null)
             {
                 Log.i("bluetooth","No puerto bluetooth");
             }
             if(!bluetoothAdapter.isEnabled())
             {
                 Log.i("bluetooth","si encontró");
                 Intent enableBt =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                 startActivityForResult(enableBt,0);
             }
             else
             {
                 Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                 startActivityForResult(enableBtIntent, 0);
                 Log.i("bluetooth","si encontró");
                 Intent enableBt =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                 startActivityForResult(enableBt,0);
             }
            Set<BluetoothDevice> pairedDevice= bluetoothAdapter.getBondedDevices();

             if(pairedDevice.size()>0)
             {
                 for(BluetoothDevice pairedDev:pairedDevice)
                 {
                     //Nombre de Impresora :Mobile Printer
                    Log.i("bluetooth","Dispositivo: "+pairedDev.getName().toString());
                    if(pairedDev.getName().equals("Mobile Printer"))
                    {
                        bluetoothDevice=pairedDev;
                        Log.i("bluetooth","IMPRESORA CONECTADA "+bluetoothDevice.getName());
                        break;
                    }
                 }
             }


        }catch (Exception ex){

        }
    }


    //Open Bluetooth Printer
    public  void  openBluetoothPrinter() throws IOException{
        try {

            UUID uuidString = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(uuidString);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();

            beginListenDATA();

        }catch (Exception ex)
        {
            bluetoothSocket.close();
        }
    }

    public  void beginListenDATA()
    {
        try
        {
            final Handler handler =new Handler();
            final byte delimiter = 10;
            stopWorker= false;
            readBufferPosition =0;
            readBuffer= new byte[1024];

            thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
                            int byteAvailable = inputStream.available();
                            if(byteAvailable>0)
                            {
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);
                                for(int i =0;i<byteAvailable;i++)
                                {
                                    byte b = packetByte[i];
                                    if(b==delimiter){
                                        byte[] encodedByte= new byte[readBufferPosition];
                                        System.arraycopy(

                                                readBuffer,0,encodedByte,0,encodedByte.length
                                        );
                                        final String data= new String(encodedByte,"US-ASCII");
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.i("bluetooth","PrinterNAME:"+data);
                                            }
                                        });
                                    }else
                                    {
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }
                        }catch (Exception ex)
                        {
                            Log.i("BT","ERROR AL IMPRIMIR");
                            stopWorker=true;

                        }
                    }
                }
            });


            thread.start();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    //PRINTING TEXT TO BLUETOOTH
        public  void  printDATA(String msg) throws  IOException{
           try {

               //String  msg= "INDAR  IMPRESORA";
               msg+="\n";
               Log.i("bluetooth","IMPRIMIENDO");
               outputStream.write(msg.getBytes());
           }catch (Exception ex)
           {

           }
        }
    public  void  printDATAIMG(byte[] msg) throws  IOException{
        try {

            //String  msg= "INDAR  IMPRESORA";

            Log.i("bluetooth","IMPRIMIENDO IMAGEN");
            outputStream.write(msg);
        }catch (Exception ex)
        {

        }
    }
        public  void disconnectBT() throws IOException{
        try
        {
            stopWorker=true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            Log.i("bluetooth","DESCONTADO");
        }catch (Exception ex)
        {

        }
    }

}
