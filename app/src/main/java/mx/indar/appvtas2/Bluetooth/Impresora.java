package mx.indar.appvtas2.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import mx.indar.appvtas2.R;
import mx.indar.appvtas2.fragmentos.clientes.cxc.cxcFragment;

import static android.content.Context.MODE_PRIVATE;

public class Impresora {


    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;
    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile  boolean stopWorker;
    Context context;
    cxcFragment cxcFragment;

    public Impresora(Context context,cxcFragment cxc) {

        this.cxcFragment=cxc;
        this.context=context;
    }

   public boolean FindBluetoothDevice()
    {
        try
        {
            bluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter==null)
            {
                Log.i("bluetooth","No puerto bluetooth");
                return  false;
            }
            if(bluetoothAdapter.isEnabled())
            {
                Log.i("bluetooth","si encontró");
                Intent enableBt =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((Activity)context).startActivityForResult(enableBt,0);
            }
            else
            {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((Activity)context).startActivityForResult(enableBtIntent, 0);
                Log.i("bluetooth","si encontró");
                Intent enableBt =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((Activity)context).startActivityForResult(enableBt,0);
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
                        return  true;

                    }
                }
            }
            else return  false;


        }catch (Exception ex){
            Log.i("bluetooth",ex.getMessage());
        }
        return  false;
    }



    public  void  openBluetoothPrinter() throws IOException {
        try {

            UUID uuidString = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(uuidString);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();

            beginListenDATA();

        }catch (Exception ex)
        {
            Log.i("bluetooth",ex.getMessage());
            disconnectBT();
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
                            Log.i("bluetooth",ex.getMessage());
                            stopWorker=true;

                        }
                    }
                }
            });


            thread.start();
        }catch (Exception ex)
        {
            try {
                disconnectBT();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        }

    }


    public void printPhoto(int img) {
        try {
            Bitmap bmp =  BitmapFactory.decodeResource(cxcFragment.getActivity().getResources(),
                    R.drawable.pollo);

            if(bmp!=null){
                UtilsPrinter up = new UtilsPrinter();
                byte[] command =  up.decodeBitmap(bmp);
                 outputStream.write(new byte[] { 0x1b, 'a', 0x00 });
               outputStream.write(command);

            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }


    public  void  printDATA(String msg) throws  IOException{
        try {

            //String  msg= "INDAR  IMPRESORA";
            msg+="\n";
            Log.i("bluetooth","IMPRIMIENDO");
            outputStream.write(msg.getBytes());
        }catch (Exception ex)
        {
            Log.i("bluetooth",ex.getMessage());
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
            Log.i("bluetooth",ex.getMessage());
        }
    }


    public void ImprimirCabezeraTicket()
    {
        String  titulo=  context.getResources().getString(R.string.tituloIndar);
        try {
            printDATA(titulo);
            printDATA(context.getResources().getString(R.string.rfc));
            printDATA(context.getResources().getString(R.string.indarDireccion));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
