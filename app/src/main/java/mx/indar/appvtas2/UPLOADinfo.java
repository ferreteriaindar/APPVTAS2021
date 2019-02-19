package mx.indar.appvtas2;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.text.LoginFilter;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.indar.appvtas2.dbClases.subirCobro;
import mx.indar.appvtas2.dbClases.subirCobroD;
import mx.indar.appvtas2.dbClases.visita;
import mx.indar.appvtas2.fragmentos.clientes.agenda.AdaptadorAgenda;

import static android.content.Context.MODE_PRIVATE;

public class UPLOADinfo  extends AsyncTask<Void,String,Boolean> {

    public ProgressDialog p;
    private  String TAG="UPLOADinfo";
    private NavigationIndar ni;
    SharedPreferences prefs;

    public UPLOADinfo(NavigationIndar ni,String algo)
    {
        this.p=new ProgressDialog(ni);
        this.ni=ni;

    }




    @Override
    protected Boolean doInBackground(Void... voids) {
         uploadVisitasV2();
         Log.i("cobro"," si entra");
        uploadCobros();
        p.dismiss();

        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        prefs = ni.getSharedPreferences(ni.getResources().getString(R.string.action_settings), MODE_PRIVATE);
        p.setMessage("Subiendo Visitas");
        p.setIndeterminate(false);
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        p.setCancelable(false);
        p.show();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        Snackbar.make(ni.findViewById(R.id.contenidoNav), "Datos Entregados", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();


        p.dismiss();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }





    public void uploadVisitasV2()
    {


        List<visita> v =new ArrayList<>();
        dbAdapter db = new dbAdapter(ni.getApplicationContext());

            try {
                db.open(true);

                v = db.obtenerVisitas();
                db.close(true);
                Log.i("VISITAS", v.size() + "");
                for (int i = 0; i < v.size(); i++) {
                    JSONArray jsonArray = new JSONArray();
                    JSONObject obj = new JSONObject();

                    obj.put("cliente", v.get(i).getCliente());
                    obj.put("fechaInicio", v.get(i).getFechaInicio());
                    obj.put("latitud", v.get(i).getLatitud());
                    obj.put("longitud", v.get(i).getLongitud());
                    if(v.get(i).getFechaPromociones()==null)
                        obj.put("fechaPromociones",JSONObject.NULL);
                    else obj.put("fechaPromociones",v.get(i).getFechaPromociones());
                    Log.i("jsonfechaPromociones",v.get(i).getFechaPromociones()+"***");
                    obj.put("latitudPromociones",v.get(i).getLatitudPromociones());
                    obj.put("longitudPromociones",v.get(i).getLongitudPromociones());
                    obj.put("latitudCobranza",v.get(i).getLatitudCobranza());
                    obj.put("longitudCobranza",v.get(i).getLongitudCobranza());
                    if(v.get(i).getFechaCobranza()==null)
                        obj.put("fechaCobranza",JSONObject.NULL);
                    else
                    obj.put("fechaCobranza",v.get(i).getFechaCobranza());
                    if(v.get(i).getFechaFin()==null)
                        obj.put("fechaFin",JSONObject.NULL);

                    else  obj.put("fechaFin",v.get(i).getFechaFin());
                    obj.put("latitudFin",v.get(i).getLatitudFin());
                    obj.put("longitudFin",v.get(i).getLongitudFin());
                    jsonArray.put(obj);
                    JSONObject visistasJson = new JSONObject();
                    visistasJson.put("visitas",jsonArray);
                    Log.i("cxc",visistasJson.toString());
                    if(insertaEnWebServices(visistasJson))
                    {
                        //Borrar de la base de datos SQLITE la visita
                        db.open(true);
                        db.borrarVisitas(v.get(i).getIdvisita());
                        Log.i("BorrarVisita",v.get(i).getIdvisita()+"");
                        db.close(true);
                    }
                }
            }  catch (SQLException e) {
                    e.printStackTrace();
                Log.i("visitas","error UPLOAD"+e.getMessage());
                }catch (JSONException e) {
                e.printStackTrace();
                Log.i("visitas","error UPLOAD"+e.getMessage());
            }

    }

    public  boolean insertaEnWebServices(JSONObject visistasJson)
    {
        Log.i("visistasJson",visistasJson.toString());
        //AHORA SE INSERTA EN EL WEB SERVICES   EL JSONSTRING
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "json=" + visistasJson.toString());
        Request request = new Request.Builder()
                .url(prefs.getString("server","")+ni.getResources().getString(R.string.WEBSubirVisitas))
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

            JSONArray visJson = reader.getJSONArray("Table1");
        JSONObject c = visJson.getJSONObject(0);
        Log.i("visita",c.getString("resultado"));
        if(c.getString("resultado").equals("OK"))
            return  true;
        else  return false;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }catch (JSONException e) {
            e.printStackTrace();
            return false;
        }


    }


    public  boolean uploadVisitas()
    {   JSONArray jsonArray = new JSONArray();
        JSONObject visistasJson = new JSONObject();
        List<visita> v =new ArrayList<>();
        dbAdapter db = new dbAdapter(ni.getApplicationContext());
        try {
            db.open(true);
            v=db.obtenerVisitas();
            db.close(true);
            Log.i("VISITAS",v.size()+"");
            for (int i=0; i < v.size(); i++) {
                JSONObject obj = new JSONObject();

                    obj.put("cliente",v.get(i).getCliente());
                    obj.put("fechaInicio",v.get(i).getFechaInicio());
                    obj.put("latitud",v.get(i).getLatitud());
                    obj.put("longitud",v.get(i).getLongitud());


                jsonArray.put(obj);
            }

            visistasJson.put("visitas",jsonArray);
            Log.i("VISITA",visistasJson.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //AHORA SE INSERTA EN EL WEB SERVICES   EL JSONSTRING
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "json=" + visistasJson.toString());
        Request request = new Request.Builder()
                .url(prefs.getString("server","")+ni.getResources().getString(R.string.WEBSubirVisitas))
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                // .addHeader("Cache-Control", "no-cache")
                //   .addHeader("Postman-Token", "3016ad7d-a309-449a-81cd-e1d5b037530f")
                .build();


        try {

            response = client.newCall(request).execute();
       //     Log.i("visita",response.body().string());

            JSONObject reader = new JSONObject(response.body().string());
            JSONArray visJson = reader.getJSONArray("Table1");
            JSONObject c = visJson.getJSONObject(0);
            Log.i("visita",c.getString("resultado"));
            if(c.getString("resultado").equals("OK"))
                return  true;
            else  return false;


        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        return false;

    }


    public  boolean uploadCobros()
    {
        List<subirCobro> listsCobro = new ArrayList<>();
        dbAdapter db =new dbAdapter(ni.getApplicationContext());
        try {
            db.open(true);
            listsCobro=db.obtenersubirCobro();
            for (int i=0;i<listsCobro.size();i++)
            {
                List<subirCobroD> listaCobroD = new ArrayList<>();
                listaCobroD=db.obtenersubirCobroD(listsCobro.get(i).getId());
                JSONArray jsonArray = new JSONArray();
                JSONObject obj = new JSONObject();

                obj.put("id", listsCobro.get(i).getId());
                obj.put("cliente",listsCobro.get(i).getCliente());
                obj.put("zona",listsCobro.get(i).getZona());
                obj.put("importe",listsCobro.get(i).getImporte());
                obj.put("formaPago",listsCobro.get(i).getFormaPago());
                obj.put("referencia",listsCobro.get(i).getReferencia());
                obj.put("fechapago",listsCobro.get(i).getFechapago());
               // obj.put("mov",listaCobroD.get(0).getMov());

                JSONArray jsonArrayDetalle = new JSONArray();
                for(int j=0;j<listaCobroD.size();j++)
                {
                JSONObject objDetalle = new JSONObject();
                objDetalle.put("mov",listaCobroD.get(j).getMov());
                objDetalle.put("movid",listaCobroD.get(j).getMovid());
                objDetalle.put("importeMov",listaCobroD.get(j).getImporte());
                objDetalle.put("descuento",listaCobroD.get(j).getDescuento());
                objDetalle.put("aplicaDescto",listaCobroD.get(j).getAplicaDescto());
                jsonArrayDetalle.put(objDetalle);
                }
                obj.put("MOV",jsonArrayDetalle);
                jsonArray.put(obj);
                JSONObject cobroJson = new JSONObject();
                cobroJson.put("cobro",jsonArray);

                Log.i("cobro",cobroJson.toString());

               if(insertaWebservicesCobro(cobroJson))
                {
                    db.BorrarReciboCobroID(listsCobro.get(i).getId());

                }
            }




                db.close(true);


        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }catch (JSONException e)
        {
            e.printStackTrace();
            return false;
        }


        return true;
    }


    public  boolean insertaWebservicesCobro(JSONObject json)
    {
        Log.i("cobroJSON",json.toString());
        //AHORA SE INSERTA EN EL WEB SERVICES   EL JSONSTRING
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "json=" + json.toString());
        Request request = new Request.Builder()
                .url(prefs.getString("server","")+ni.getResources().getString(R.string.WEBSubirCobros))
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

            JSONArray visJson = reader.getJSONArray("Table1");
            JSONObject c = visJson.getJSONObject(0);
            Log.i("visita",c.getString("resultado"));
            if(c.getString("resultado").equals("OK"))
                return  true;
            else  return false;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }
}
