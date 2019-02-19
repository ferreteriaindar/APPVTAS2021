package mx.indar.appvtas2;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import io.fabric.sdk.android.Fabric;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import mx.indar.appvtas2.dbClases.art;
import mx.indar.appvtas2.dbClases.cliente;

public class MainActivity extends AppCompatActivity {

    String usuario;
    EditText txtUserName,txtPass;
    Button btn,btnUpdate;
    SharedPreferences prefs;
    ProgressBar pb;
    LocationManager locationManager ;
    boolean GpsStatus ;
    Context context;
    Intent intent1;
    DownloadManager downloadManager;
    private static final double APP_VERSION = 1.2; //CORRIGE   EL FORMATO DE 24 HRS EN LAS VISITAS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        txtUserName = findViewById(R.id.txtLoginUSer);
        txtPass = findViewById(R.id.txtLoginPass);
        btn=findViewById(R.id.btnLogin);
        btnUpdate=findViewById(R.id.btnUpdateAPP);
        btnUpdate.setVisibility(View.INVISIBLE);
        prefs = getSharedPreferences(getResources().getString(R.string.action_settings), MODE_PRIVATE);
        FirebaseMessaging.getInstance().subscribeToTopic("TODOS");
        String restoredText = prefs.getString("FirstRun", null);
        TextView txtVersion = findViewById(R.id.txtMainVersion);
        txtVersion.setText(APP_VERSION+"");

        btn.setVisibility(View.VISIBLE);
        context = getApplicationContext();
        validaInicio();
        GPSStatus();
        if(GpsStatus == true)
        {

        }else
        {

            intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
        }





        ImageView btnSettings = findViewById(R.id.btnInicioSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Contraseña Sistema");

// Set up the input
                final EditText input = new EditText(MainActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if( input.getText().toString().equals("indar2018"))
                        {
                            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


        if (restoredText != null) {
            if (checaVersion()) {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(200);
                //vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                btnUpdate.setVisibility(View.VISIBLE);
                btn.setVisibility(View.INVISIBLE);
            }
        }
    }


    public void descargaArchivo() {

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse("https://www.indar.mx/Descargas/indarAPP/indarAPP.apk");
        final DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        final Long reference = downloadManager.enqueue(request);


        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                try {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    install.setDataAndType(downloadManager.getUriForDownloadedFile(reference), downloadManager.getMimeTypeForDownloadedFile(reference));
                    startActivity(install);

                    unregisterReceiver(this);
                    finish();
                }catch (Exception e)
                {
                    Log.e("INSTALAR",e.getMessage());
                }
            }

        };

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    public  void actualizaAPP(View v)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Vas a instalar una nueva version \n se borrará toda la info que no hayas subido\n ¿CONTINUAR?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("update","si detecta click ok button");
                        dbAdapter db = new dbAdapter(getApplicationContext());
                        try {
                            db.open(true);
                            db.borrarTodos();
                            db.close(true);
                            SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.action_settings), MODE_PRIVATE).edit();
                            editor.putString("FirstRun","0");
                            editor.commit();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                        descargaArchivo();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();


    }


    public void GPSStatus(){
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public  void validaInicio()
    {

        String restoredText = prefs.getString("FirstRun", null);
        if (restoredText == null) {
            txtPass.setFocusable(false);
            txtUserName.setFocusable(false);
            btn.setEnabled(false);


        }
    }


    public  boolean checaVersion()
    {
        if(!HayInternet())
        {
            Log.i("update","No hay internet");
            return false;
        }
        else
        {
           habilitaUpdate hu = new habilitaUpdate(MainActivity.this);
            try {
             boolean  resultado= hu.execute().get();
             return resultado;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        return false;
    }




    public boolean HayInternet() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }



    public  void onLogin(View view)
    {
        String  usuario=txtUserName.getText().toString();
        String  pass=txtPass.getText().toString();



        if(usuario.equals(prefs.getString("usuario","")) && pass.equals("123"))
        {
            Intent intent = new Intent(MainActivity.this,NavigationIndar.class);
            startActivity(intent);


/*
            DescargaClientes dc = new DescargaClientes();
            //DescargaArt da = new DescargaArt();
            String resultado= null,resultadoArt=null;
            try {
                /*resultado = dc.execute("","").get();
                // resultado= da.execute("","").get();

               //if(resultado.equals("SI"))
                if(true)
                {

                    Intent intent = new Intent(MainActivity.this,NavigationIndar.class);
                    startActivity(intent);

                    //resultadoArt = dc.execute("","").get();

                }else finish();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
                */


        }
        else  {
            txtUserName.setText("");
            txtPass.setText("");
            txtUserName.requestFocus();
            txtUserName.setError("Incorrecto");
            txtPass.setError("");
        }

        ///ESTE PARTE ES PRUEBA PARA HACER EL INSERT A LA BASE DATOS Y HACE UNA CONSULTA
        /*
        dbAdapter db = new dbAdapter(this);
        try {
            db.open();
            cliente cte = new cliente("roberto","velasco","xxx000","z3558");
            long id=db.addContact(cte);
            cliente cte2 = db.getContact(1);
            Toast.makeText(this, "BASE :"+cte2.getCliente().toString(), Toast.LENGTH_SHORT).show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
*/

        /*
        String user=txtUserName.getText().toString(),pass=txtPass.getText().toString();

        LoginTask lt = new LoginTask();
        try {
            String resultado=   lt.execute(user,pass).get();

            JSONObject reader = new JSONObject(resultado);

                JSONArray contacts = reader.getJSONArray("Table");
                if(contacts.length()>0) {
                    JSONObject vendedor = contacts.getJSONObject(0);
                  // Toast.makeText(this, vendedor.getString("AgenteNombre"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,NavigationIndar.class);
                    intent.putExtra("zona",vendedor.getString("zona"));
                    intent.putExtra("AgenteNombre",vendedor.getString("AgenteNombre"));
                    intent.putExtra("agenteEmail",vendedor.getString("agenteEmail"));
                    startActivity(intent);
                }else {
                    Toast.makeText(this, "Usuario y/o Contraseñas Incorrecta", Toast.LENGTH_SHORT).show();
                    txtUserName.setError("Incorrecto");
                    txtPass.setError("Incorrecto");
                    txtUserName.setText("");
                    txtPass.setText("");
                    txtUserName.requestFocus();
                }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
*/





    }
    public  class  habilitaUpdate extends AsyncTask<Void,Void,Boolean>
    {

        String user,pass;
        MainActivity mainActivity;
       // ProgressDialog pb;
    //   private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        public habilitaUpdate(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }


        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            Response response = null;
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "version=" + APP_VERSION);
            Request request = new Request.Builder()
                    .url(prefs.getString("server","")+mainActivity.getResources().getString(R.string.WEBchecaVersion))
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    // .addHeader("Cache-Control", "no-cache")
                    //   .addHeader("Postman-Token", "3016ad7d-a309-449a-81cd-e1d5b037530f")
                    .build();


            try {
                response = client.newCall(request).execute();

                //     Log.i("visita",response.body().string());

                JSONObject reader = null;

                reader = new JSONObject(response.body().string());

                JSONArray visJson = reader.getJSONArray("Table");
                JSONObject c = visJson.getJSONObject(0);
                Log.i("update",c.getString("accion"));
                if(c.getString("accion").equals("UPDATE")) {
                  //  Toast.makeText(mainActivity, "", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else  return false;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }catch (JSONException e) {
                e.printStackTrace();
                return false;
            }




        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Toast.makeText(mainActivity.getApplicationContext(), "Actualiza tu APP ", Toast.LENGTH_SHORT).show();
          //  txtAvance.setText("CLIENTE "+values[0]+"");
        }



        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            //this.dialog.dismiss();

        }






    }


}
