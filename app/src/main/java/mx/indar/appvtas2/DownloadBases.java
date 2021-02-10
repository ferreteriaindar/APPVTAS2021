package mx.indar.appvtas2;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;

import mx.indar.appvtas2.dbClases.MenuClientes.Cobro;
import mx.indar.appvtas2.dbClases.MenuClientes.CobroD;
import mx.indar.appvtas2.dbClases.MenuClientes.visitasHistorico;
import mx.indar.appvtas2.dbClases.art;
import mx.indar.appvtas2.dbClases.cliente;
import mx.indar.appvtas2.dbClases.cxcCliente;
import mx.indar.appvtas2.dbClases.especificos;

import static android.content.Context.MODE_PRIVATE;


public class DownloadBases  extends AsyncTask<Void,String,Boolean> {
    public ProgressDialog p;
    private String TAG = "DownloadBase";
    private NavigationIndar ni;
    SharedPreferences prefs;
    String zona;
    Context context;



    public DownloadBases(NavigationIndar ni, String zona) {
        Log.v(TAG, "Creacion objeto");

        this.p = new ProgressDialog(ni);
        this.ni = ni;
        this.zona = zona;


    }


    @Override
    protected Boolean doInBackground(Void... voids) {


        boolean resultado2= descargaClientes();
       // publishProgress("Descarga Articulos");
     //   boolean resultado =  descargaArticulos();
        publishProgress("Descarga Cobranza");
        descargaCXCZona(prefs.getString("usuario",""));
        publishProgress("Descarga Especificos");
        descargaEspecificos(prefs.getString("usuario",""));
        publishProgress("Descarga Cobros");
        descargaCobros(prefs.getString("usuario",""),"cabecera");
        publishProgress("Descarga CobrosDetalle");
        descargaCobros(prefs.getString("usuario",""),"detalle");
        publishProgress("Descarga Visitas");
        descargaVisitasHistorico(prefs.getString("usuario",""));
        //publishProgress("Descarga Promos");
      //  descargaPDF();
        //publishProgress("Descarga Articulos");
       // descargaART();

        p.dismiss();
        return true;
    }


    private void lockScreenOrientation() {
        int currentOrientation =  ni.getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
           ni.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            ni.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void unlockScreenOrientation() {
        ni.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        lockScreenOrientation();
        prefs = ni.getSharedPreferences(ni.getResources().getString(R.string.action_settings), MODE_PRIVATE);
        p.setMessage("Descargando Clientes");
        p.setIndeterminate(false);
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        p.setCancelable(false);
        p.show();
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
        Snackbar.make(ni.findViewById(R.id.contenidoNav), "Datos actualizados", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        unlockScreenOrientation();
        p.dismiss();

     /*   if(s)
        {
            // Do something awesome here
            Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(context,"Download failed, network issue",Toast.LENGTH_SHORT).show();
        }*/

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        p.setMessage(values[0]);
    }


    public boolean descargaArticulos() {
        String respuesta = null, json_url =prefs.getString("server","")+ni.getResources().getString(R.string.WEBArticulos);// "http://indarweb.dyndns.org:82/WebService1.asmx/art";
        String JSON_STRING;
        try {
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");

            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            //return  stringBuilder.toString().trim();
            respuesta = stringBuilder.toString().trim();


            JSONObject reader = new JSONObject(respuesta);
            JSONArray ArtJson = reader.getJSONArray("Table");
            dbAdapter db = new dbAdapter(ni.getApplicationContext());

            //  Log.i("Articulos","TERMINA DESCARGAR ARTICULOS");
            db.open(true);
            db.borrarArt();
            db.close(true);
            db.open(true);

            for (int i = 0; i < ArtJson.length(); i++) {

                JSONObject c = ArtJson.getJSONObject(i);
                String disponible = c.getString("disponible");
                disponible = disponible.substring(0, disponible.length() - 2);
                //    Log.i("articulo",c.getString("Articulo"));
                art a = new art(c.getString("Articulo"), c.getString("Descripcion1"), c.getString("Categoria"), Float.parseFloat(c.getString("PrecioLista")), Float.parseFloat(c.getString("Precio7")), Float.parseFloat(c.getString("Precio8")), Float.parseFloat(c.getString("multiplo")), Float.parseFloat(c.getString("CantidadMinimaVenta")), Float.parseFloat(c.getString("CantidadMaximaVenta")), Integer.parseInt(disponible));
                //    Log.i("llenarArticulos","si empieza");
                db.llenaArticulos(a);


            }
            db.close(true);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException EX) {
            EX.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean descargaClientes() {
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "zona=" + zona);
        Request request = new Request.Builder()
                .url(prefs.getString("server","")+ni.getResources().getString(R.string.WEBclientesPorZona))
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                // .addHeader("Cache-Control", "no-cache")
                //   .addHeader("Postman-Token", "3016ad7d-a309-449a-81cd-e1d5b037530f")
                .build();

        try {
            response = client.newCall(request).execute();

            String resultado = response.body().string();


            JSONObject reader = new JSONObject(resultado);
            JSONArray clientesJson = reader.getJSONArray("Table");
            dbAdapter db = new dbAdapter(ni.getApplicationContext());

            db.open(true);

            db.borrarClientes();
            for (int i = 0; i < clientesJson.length(); i++) {

                JSONObject c = clientesJson.getJSONObject(i);
                float coordenadx = 0, coordenady;
                if (!c.getString("coordenaday").equals("null")) {
                    coordenady = Float.parseFloat(c.getString("coordenaday"));
                    coordenadx = Float.parseFloat(c.getString("coordenadax"));
                } else {
                    coordenady = 0;
                    coordenadx = 0;
                }


                cliente cte = new cliente(c.getString("Cliente"), c.getString("Nombre"), c.getString("rfc"), c.getString("zona"), c.getString("direccion"), coordenadx, coordenady, c.getString("calle"), c.getString("Poblacion"), Integer.parseInt(c.getString("dia")));

                db.llenaClientesPorZona(cte);
                //  publishProgress(cte.getCliente());


            }


            db.close(true);
            return  true;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException EX) {

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }







    public  boolean descargaPDF()
    {
        try {

            File ruta= new File(Environment.getExternalStorageDirectory(), "/IndarApp/PDF/FI.PDF");
            FileOutputStream file = new FileOutputStream(ruta);
            URL url = new URL("https://www.indar.com.mx/Imagenes/CATALOGOS/FERREIMPULSOS/PDF/FI.PDF");
            HttpURLConnection connection = (HttpURLConnection) url .openConnection();
            connection .setRequestMethod("GET");
            connection .setDoOutput(true);
            connection .connect();
            InputStream input = connection .getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = input .read(buffer)) > 0) {
                file .write(buffer, 0, len );
            }

            file .close();
            p.dismiss();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public  boolean descargaART()
    {
        File ruta= new File(Environment.getExternalStorageDirectory(), "/IndarApp/ART/art.zip");

        try {
            FileOutputStream file = new FileOutputStream(ruta);
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "zona="+zona+"&undefined=");
            Request request = new Request.Builder()
                    .url(prefs.getString("server","")+ni.getResources().getString(R.string.WEBARTDOWNLOAD))
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "4c22a0c4-ecfc-4090-a6ab-e38f9c0372c7")
                    .build();

            Response response = client.newCall(request).execute();
            InputStream input =  new ByteArrayInputStream(response.body().bytes());
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = input .read(buffer)) > 0) {
                file .write(buffer, 0, len );
            }

            file .close();
            p.dismiss();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



          public  boolean descargaCXCZona(String zona) {
              OkHttpClient client = new OkHttpClient();
              Response response = null;
              MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
              RequestBody body = RequestBody.create(mediaType, "zona=" + zona);
              Request request = new Request.Builder()
                      .url(prefs.getString("server", "") + ni.getResources().getString(R.string.WEBcxcZona))
                      .post(body)
                      .addHeader("Content-Type", "application/x-www-form-urlencoded")
                      // .addHeader("Cache-Control", "no-cache")
                      //   .addHeader("Postman-Token", "3016ad7d-a309-449a-81cd-e1d5b037530f")
                      .build();


              try {
                  response = client.newCall(request).execute();
                  String resultado = response.body().string();

                  JSONObject reader = new JSONObject(resultado);
                  JSONArray cxcJson = reader.getJSONArray("Table");


                  dbAdapter db =new dbAdapter(ni.getApplicationContext());
                  db.open(true);
                  db.borrarCXC();
                  Log.i("cxc",cxcJson.length()+"registros");
                  for (int i = 0; i < cxcJson.length(); i++) {

                      JSONObject c = cxcJson.getJSONObject(i);
                      cxcCliente cxcCliente = new cxcCliente();
                      cxcCliente.setCliente(c.getString("Cliente"));
                      cxcCliente.setMov(c.getString("Mov"));
                      cxcCliente.setMovID(c.getString("MovID"));
                      cxcCliente.setFechaEmision(c.getString("FechaEmision"));
                      cxcCliente.setVencimiento(c.getString("Vencimiento"));
                      cxcCliente.setDiasMoratorios(c.getInt("DiasMoratorios"));
                      cxcCliente.setSaldo(Float.parseFloat(c.getString("Saldo")));
                      cxcCliente.setReferencia(c.getString("Referencia"));

                      cxcCliente.setDescuento(Float.parseFloat(c.getString("descuento")));
                      db.obtenerCXCZona(cxcCliente);

                  }
                  db.close(true);




              } catch (IOException e) {
                  e.printStackTrace();
              }catch (JSONException EX) {
                  EX.printStackTrace();
              }catch (java.sql.SQLException e) {
                  e.printStackTrace();
              }



                return  false;
          }




          public  boolean descargaCobros(String zona,String parte)
          {

              Log.i("cobros",zona+parte);
              OkHttpClient client = new OkHttpClient();
              Response response = null;
              MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
              RequestBody body = RequestBody.create(mediaType, "zona="+zona+"&parte="+parte+"&undefined=");
              Request request = new Request.Builder()
                      .url(prefs.getString("server","")+ni.getResources().getString(R.string.WEBCobros))
                      .post(body)
                      .addHeader("Content-Type", "application/x-www-form-urlencoded")
                     // .addHeader("cache-control", "no-cache")
                    //  .addHeader("Postman-Token", "35a6a5bb-ab81-4702-a40d-57f69555a54e")
                      .build();


              try {
                  response = client.newCall(request).execute();
                  String resultado = response.body().string();
                  Log.i("cobros",resultado);
                  JSONObject reader = new JSONObject(resultado);
                  JSONArray cobroJson = reader.getJSONArray("Table");


                  dbAdapter db =new dbAdapter(ni.getApplicationContext());
                  db.open(true);

                  if(parte.equals("cabecera")) {
                      db.borrarCobro();
                      for (int i = 0; i < cobroJson.length(); i++) {
                          JSONObject c = cobroJson.getJSONObject(i);
                          Cobro cobro = new Cobro();
                          cobro.setId(c.getInt("IdCobro"));
                          cobro.setCliente(c.getString("Cliente"));
                          cobro.setFormaPago(c.getString("FormaPago"));
                          cobro.setReferencia(c.getString("Referencia"));
                          cobro.setImporte(Float.parseFloat(c.getString("importe")));
                          cobro.setFechaPago(c.getString("fechaPago"));
                          cobro.setFechaRegistro(c.getString("fechaRegistro"));
                          cobro.setUsuario(c.getString("usuario"));
                          cobro.setNumCobro(Integer.parseInt(c.getString("numCobro")));
                          db.insertaCobro(cobro);
                      }
                  }
                  else
                  {
                      db.borrarCobroD();
                      for (int i = 0; i < cobroJson.length(); i++) {
                          JSONObject c= cobroJson.getJSONObject(i);
                          CobroD cd =new CobroD();
                          cd.setIdCobro(c.getInt("IdCobro"));
                          cd.setMov(c.getString("Mov"));
                          cd.setMovid(c.getString("MovId"));
                          cd.setImporte(Float.parseFloat( c.getString("importe")));
                          cd.setDescuento(Float.parseFloat(c.getString("descuento")));
                          cd.setAplicaDescto(c.getString("aplicaDescto"));
                           db.insertaCobroD(cd);
                      }
                  }
                db.close(true);

              } catch (IOException e) {
                  e.printStackTrace();

              }catch (JSONException EX) {
                  EX.printStackTrace();
              }catch (java.sql.SQLException e) {
                  e.printStackTrace();
              }


            return false;
          }


    public  boolean descargaEspecificos(String zona) {

        String  ejercicio,periodo;
        String dateFormatMES = "MM";
        String dateFormatANIO = "yyyy";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatMES);
        SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormatANIO);
        periodo = sdf.format(date);
        ejercicio=sdf2.format(date);
        Log.i("especificos",ejercicio+periodo);

        OkHttpClient client = new OkHttpClient();
        Response response = null;
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "zona="+zona+"&ejercicio="+ejercicio+"&periodo="+periodo);
        Request request = new Request.Builder()
                .url(prefs.getString("server", "") + ni.getResources().getString(R.string.WEBespecificos))
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                // .addHeader("Cache-Control", "no-cache")
                //   .addHeader("Postman-Token", "3016ad7d-a309-449a-81cd-e1d5b037530f")
                .build();

        dbAdapter db =new dbAdapter(ni.getApplicationContext());
        try {
            response = client.newCall(request).execute();
            String resultado = response.body().string();

            JSONObject reader = new JSONObject(resultado);
            JSONArray cxcJson = reader.getJSONArray("Table");



            db.open(true);
            db.borrarEspecificos();
            Log.i("especificos",cxcJson.length()+"especificos");
            for (int i = 0; i < cxcJson.length(); i++) {

                JSONObject c = cxcJson.getJSONObject(i);
                especificos e = new especificos();
                e.setCuota(Float.parseFloat(c.getString("Cuota")));
                e.setVenta(Float.parseFloat(c.getString("Venta")));
                e.setPorcentaje(Float.parseFloat(c.getString("porcentaje")));
                Log.i("especificos",c.getString("porcentaje"));
                Log.i("especificos",c.getString("EspecificoNombre1"));
                e.setEspecificoNombre1(c.getString("EspecificoNombre1"));
                e.setEspecificoCuota1(Float.parseFloat(c.getString("EspecificoCuota1")));
                e.setEspecificoVenta1(Float.parseFloat(c.getString("EspecificoVenta1")));
                e.setEspecificoNombre2(c.getString("EspecificoNombre2"));
                e.setEspecificoCuota2(Float.parseFloat(c.getString("EspecificoCuota2")));
                e.setEspecificoVenta2(Float.parseFloat(c.getString("EspecificoVenta2")));
                e.setEspecificoNombre3(c.getString("EspecificoNombre3"));
                e.setEspecificoCuota3(Float.parseFloat(c.getString("EspecificoCuota3")));
                e.setEspecificoVenta3(Float.parseFloat(c.getString("EspecificoVenta3")));
                e.setEspecificoNombre4(c.getString("EspecificoNombre4"));
                e.setEspecificoCuota4(Float.parseFloat(c.getString("EspecificoCuota4")));
                e.setEspecificoVenta4(Float.parseFloat(c.getString("EspecificoVenta4")));
                db.llenarEspecifico(e);

            }





        } catch (IOException e) {
            e.printStackTrace();
            Log.i("especificos1",e.getMessage().toString()+"");

        }catch (JSONException EX) {
            Log.i("especificos2",EX.getMessage().toString()+""+EX.getLocalizedMessage());
        }catch (java.sql.SQLException eX2) {
            eX2.printStackTrace();
            Log.i("especificos3",eX2.getMessage().toString()+"");
        }


        db.close(true);
        return  false;
    }


    public  boolean descargaVisitasHistorico(String zona)
    {
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "zona=" + zona);
        Request request = new Request.Builder()
                .url(prefs.getString("server", "") + ni.getResources().getString(R.string.WEBvisitasHistorico))
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                // .addHeader("Cache-Control", "no-cache")
                //   .addHeader("Postman-Token", "3016ad7d-a309-449a-81cd-e1d5b037530f")
                .build();
        try {
            response = client.newCall(request).execute();
            String resultado = response.body().string();

            JSONObject reader = new JSONObject(resultado);
           JSONArray visitaJson = reader.getJSONArray("Table");
           dbAdapter db = new dbAdapter(ni.getApplicationContext());
            db.open(true);
            db.borrarvisitasHistorico();
            for (int i = 0; i < visitaJson.length(); i++) {

                JSONObject c = visitaJson.getJSONObject(i);
                visitasHistorico vh =new visitasHistorico();
               // e.setCuota(Float.parseFloat(c.getString("Cuota")));
                vh.setCliente(c.getString("cliente"));
                vh.setFechaInicio(c.getString("fechaInicio"));
                vh.setFechaCobranza(c.getString("fechaCobranza"));
                vh.setFechaPromociones(c.getString("fechaPromociones"));
                if(!c.getString("fechaFin").equals("null"))
                    vh.setFechaFin("0");
                else
                vh.setFechaFin(c.getString("fechaFin"));
                vh.setLatitud(Float.parseFloat(c.getString("latitud")));
                vh.setLongitud(Float.parseFloat(c.getString("longitud")));
                if (!c.getString("longitudPedido").equals("null"))
                vh.setLongitudPedido(Float.parseFloat(c.getString("longitudPedido")));
                else vh.setLongitudPedido(0.0f);
                if(!c.getString("latitudPedido").equals("null"))
                vh.setLatitudPedido(Float.parseFloat(c.getString("latitudPedido")));
                else vh.setLatitudPedido(0.0f);
                if(!c.getString("longitudPromociones").equals("null"))
                vh.setLongitudPromociones(Float.parseFloat(c.getString("longitudPromociones")));
                else vh.setLongitudPromociones(0.0f);
                if(!c.getString("latitudPromociones").equals("null"))
                vh.setLatitudPromociones(Float.parseFloat(c.getString("latitudPromociones")));
                else vh.setLatitudPromociones(0.0f);
                if(!c.getString("latitudCobranza").equals("null"))
                vh.setLatitudCobranza(Float.parseFloat(c.getString("latitudCobranza")));
                else vh.setLatitudCobranza(0.0f);
                if(!c.getString("longitudCobranza").equals("null"))
                vh.setLongitudCobranza(Float.parseFloat(c.getString("longitudCobranza")));
                else vh.setLongitudCobranza(0.0f);
                if(!c.getString("latitudFin").equals("null"))
                    vh.setLatitudFin(Float.parseFloat(c.getString("latitudFin")));
                else vh.setLatitudFin(0.0f);
                if(!c.getString("longitudFin").equals("null"))
                    vh.setLongitudFin(Float.parseFloat(c.getString("longitudFin")));
                else vh.setLongitudFin(0.0f);

                db.insertVisitasHistorico(vh);


            }
            db.close(true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}