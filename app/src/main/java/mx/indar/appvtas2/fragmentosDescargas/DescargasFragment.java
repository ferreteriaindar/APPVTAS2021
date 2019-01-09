package mx.indar.appvtas2.fragmentosDescargas;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import mx.indar.appvtas2.NavigationIndar;
import mx.indar.appvtas2.R;
import mx.indar.appvtas2.dbAdapter;
import mx.indar.appvtas2.dbClases.art;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DescargasFragment extends Fragment {

    private ProgressDialog progressDialog;
    public Context context;
    public  View view;
    SharedPreferences prefs;
    public DescargasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            context=container.getContext();

        view= inflater.inflate(R.layout.fragment_descargas, container, false);
        progressDialog= new ProgressDialog(getActivity());
        prefs = getActivity().getSharedPreferences(getActivity().getResources().getString(R.string.action_settings), MODE_PRIVATE);
        Button btn = (Button) view.findViewById(R.id.btnDescargasFragment);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Descargando Clientes");
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                new Hilodescargas().execute();
            }
        });
        return view;
    }

    public boolean descargaArticulos() {
        String respuesta = null, json_url =prefs.getString("server","")+context.getResources().getString(R.string.WEBArticulos);// "http://indarweb.dyndns.org:82/WebService1.asmx/art";
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
            dbAdapter db = new dbAdapter(context.getApplicationContext());

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
            progressDialog.dismiss();
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


    public  class  Hilodescargas extends AsyncTask<Void,String,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            descargaArticulos();
            return null;
        }
    }
}
