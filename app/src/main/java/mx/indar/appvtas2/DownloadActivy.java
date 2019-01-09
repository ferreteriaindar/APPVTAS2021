package mx.indar.appvtas2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLClientInfoException;
import java.util.concurrent.ExecutionException;

import mx.indar.appvtas2.dbClases.art;
import mx.indar.appvtas2.dbClases.cliente;

public class DownloadActivy extends AppCompatActivity {
    SharedPreferences prefs;
    TextView txtAvance;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_activy);
        prefs = getSharedPreferences(getResources().getString(R.string.action_settings), MODE_PRIVATE);
        txtAvance = findViewById(R.id.txtDownloadAvance);
        pb=findViewById(R.id.pbDownloadBases);




    }


    @Override
    protected void onStart() {
        super.onStart();
        DescargaClientes dc = new DescargaClientes();
        //DescargaArt da = new DescargaArt();
        String resultado= null,resultadoArt=null;
        try {
            resultado = dc.execute("","").get();
           // resultado= da.execute("","").get();

            if(resultado.equals("SI"))
            {

                Intent intent = new Intent(DownloadActivy.this,NavigationIndar.class);
                startActivity(intent);

                //resultadoArt = dc.execute("","").get();

            }else finish();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();



    }


    public  void ONDOWNLOAD(View view)
    {



    }



    public  String descargaArticulos() {
        String respuesta = null, json_url = "http://indarweb.dyndns.org:82/WebService1.asmx/art";
        String JSON_STRING;
        try {
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            Log.i("art", "INICIA");
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");

            }
            Log.i("art", "TERMINA");
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            //return  stringBuilder.toString().trim();
            respuesta = stringBuilder.toString().trim();


            JSONObject reader = new JSONObject(respuesta);
            JSONArray ArtJson = reader.getJSONArray("Table");
            dbAdapter db = new dbAdapter(DownloadActivy.this);

            db.open(true);
            db.borrarArt();

            for (int i = 0; i < ArtJson.length(); i++) {
                Log.i("art", "CREA OBJETO" + i + "");
                JSONObject c = ArtJson.getJSONObject(i);
                String disponible =c.getString("disponible");
                disponible=disponible.substring(0,disponible.length()-2);
                art a = new art(c.getString("Articulo"), c.getString("Descripcion1"), c.getString("Categoria"), Float.parseFloat(c.getString("PrecioLista")), Float.parseFloat(c.getString("Precio7")), Float.parseFloat(c.getString("Precio8")), Float.parseFloat(c.getString("multiplo")), Float.parseFloat(c.getString("CantidadMinimaVenta")), Float.parseFloat(c.getString("CantidadMaximaVenta")), Integer.parseInt(disponible));
                db.llenaArticulos(a);

            }
            db.close(true);
            return "SI";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException EX) {
            EX.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return "NO";
    }

/*
    public  class DescargaArt extends  AsyncTask<String,String,String>
    {
            String cadena1,cadena2;

        @Override
        protected String doInBackground(String... strings) {

            String respuesta=null,json_url="http://indarweb.dyndns.org:82/WebService1.asmx/art";
            String JSON_STRING;
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                InputStream inputStream =httpURLConnection.getInputStream();
                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder= new StringBuilder();
                Log.i("art","INICIA");
                while ((JSON_STRING=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(JSON_STRING+"\n");

                }
                Log.i("art","TERMINA");
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                //return  stringBuilder.toString().trim();
                respuesta=JSON_STRING.toString().trim();





                JSONObject reader = new JSONObject(respuesta);
                JSONArray ArtJson = reader.getJSONArray("Table");
                dbAdapter db = new dbAdapter(DownloadActivy.this);

                db.open(false);
               db.borrarArt();
                for (int i=0;i<ArtJson.length();i++)
                {
                    Log.i("art","CREA OBJETO"+i+"");
                    JSONObject c = ArtJson.getJSONObject(i);
                    art a = new art(c.getString("Articulo"),c.getString("Descripcion1"),c.getString("Categoria"),Float.parseFloat(c.getString("PrecioLista")),Float.parseFloat(c.getString("Precio7")),Float.parseFloat(c.getString("Precio8")),Integer.parseInt(c.getString("multiplo")),Float.parseFloat(c.getString("CantidadMinimaVenta")),Float.parseFloat(c.getString("CantidadMaximaVenta")),Integer.parseInt(c.getString("disponible")));
                    db.llenaArticulos(a);
                    publishProgress(a.getArticulo());
                }
                db.close(false);

                return  "SI";//descargaArticulos();


            } catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException EX)
            {
                EX.printStackTrace();
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

            return "NO";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.i("makemachine", "onProgressUpdate(): " + String.valueOf(values[0]));
            txtAvance.setText("Articulo :"+values[0]+"");
        }
    }
*/



    public  class DescargaClientes extends AsyncTask<String,String,String>
    {
        String user,pass;


        @Override
        protected String doInBackground(String... strings) {
            user=strings[0];pass=strings[1];
            OkHttpClient client = new OkHttpClient();
            Response response=null;
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "zona="+prefs.getString("usuario","").toString());
            Request request = new Request.Builder()
                    .url("http://indarweb.dyndns.org:82/WebService1.asmx/clientesPorZona")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    // .addHeader("Cache-Control", "no-cache")
                    //   .addHeader("Postman-Token", "3016ad7d-a309-449a-81cd-e1d5b037530f")
                    .build();
            Log.i("body",body.toString());
            try {
                response   = client.newCall(request).execute();


                String resultado=response.body().string();
                Log.i("resultadoHilo",resultado);
                try {
                    JSONObject reader = new JSONObject(resultado);
                    JSONArray clientesJson = reader.getJSONArray("Table");
                    dbAdapter db = new dbAdapter(DownloadActivy.this);

                    db.open(false);

                    db.borrarClientes();
                    for (int i = 0; i < clientesJson.length(); i++) {
                            Log.i("for",i+"");
                        JSONObject c = clientesJson.getJSONObject(i);
                        float coordenadx=0,coordenady;
                        if(!c.getString("coordenaday").equals("null")) {
                            coordenady = Float.parseFloat(c.getString("coordenaday"));
                            coordenadx = Float.parseFloat(c.getString("coordenadax"));
                        }
                        else {
                            coordenady = 0;
                            coordenadx=0;
                        }


                        cliente cte = new cliente(c.getString("Cliente"), c.getString("Nombre"), c.getString("rfc"), c.getString("zona"), c.getString("direccion"), coordenadx, coordenady, c.getString("calle"), c.getString("Poblacion"), Integer.parseInt(c.getString("dia")));

                        db.llenaClientesPorZona(cte);
                        publishProgress(cte.getCliente());


                    }
                    db.close(false);
                    descargaArticulos();
                }catch (JSONException EX)
                {

                }catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }







                return "SI";//descargaArticulos();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "NO";

        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.i("makemachine", "onProgressUpdate(): " + String.valueOf(values[0]));
            txtAvance.setText("CLIENTE "+values[0]+"");
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
}
