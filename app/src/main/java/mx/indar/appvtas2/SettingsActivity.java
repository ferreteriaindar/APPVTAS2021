package mx.indar.appvtas2;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class SettingsActivity extends AppCompatActivity {
    EditText txtServer,txtUser,txtPass;
    ImageView img;
    SharedPreferences prefs;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        txtServer = findViewById(R.id.txtSettingsServer);
        txtUser=findViewById(R.id.txtSettingsUser);
        txtPass= findViewById(R.id.txtSettingsPass);
        img=findViewById(R.id.imgSettings);
        ;

        validaInicio();
    }



public  void validaInicio()
    {
     prefs = getSharedPreferences(getResources().getString(R.string.action_settings), MODE_PRIVATE);
    String restoredText = prefs.getString("FirstRun", null);
        if (restoredText == null){
                txtServer.setText("http://indarweb.dyndns.org:82/");
                txtPass.setError("escribe");
                txtUser.setError("escribe");
            dbAdapter DA = new dbAdapter(this);   //SE Inicializa la base de datos por se la primera ejecuxcion
            try {
                DA.open(false);
                DA.close(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
            else
        {
            txtServer.setText(prefs.getString("server","http://indarweb.dyndns.org:82/"));
            txtUser.setText(prefs.getString("usuario",""));
            txtPass.setText(prefs.getString("pass",""));
        }


    }


    public  void exportDB(View view)
    {
        db database = new db(this);
        String rutaDB= getApplicationContext().getDatabasePath(database.getDatabaseName()).toString();
        File rootPath = new File(Environment.getExternalStorageDirectory(), "/IndarApp/db");
        String destino =rootPath.getPath();
        File origen= new File(rutaDB);
        File destini = new File(destino);
        FileInputStream is = null;
        try {
            is = new FileInputStream(origen);


        OutputStream os = new FileOutputStream(destino);


        byte[] buffer = new byte[1024];


        while (is.read(buffer) > 0) {


            os.write(buffer);


        }
        os.flush();
        os.close();
        is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (java.io.IOException e2)
        {
            e2.printStackTrace();
        }
        Toast.makeText(this, "Base datos Exportada", Toast.LENGTH_SHORT).show();
    }

    public  void onSalir (View view)
    {
        finish();
        Intent intent = new Intent(this,MainActivity.class);
        this.startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            int grantResultsLength = grantResults.length;
            if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "You grant write external storage permission. Please click original button again to continue.", Toast.LENGTH_LONG).show();
                Snackbar snackbar= Snackbar.make(view,"GPS Autorizado",Snackbar.LENGTH_LONG);
                snackbar.show();
                File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/IndarApp/PDF");

                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d("IndarApp", "failed to create directory");
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show();
            }
        }
    }





    public void creaCarpetas()
    {
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(writeExternalStoragePermission!= PackageManager.PERMISSION_GRANTED)
        {
            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.BLUETOOTH}, 1);
        }else {


            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/IndarApp/PDF");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("IndarApp", "failed to create directory");
                }
            }
        }


    }

    public  void onAceptar(View view)
    {
        if(!txtServer.getText().toString().equals("") && !txtUser.getText().toString().equals(""))
        {
          /*  SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.action_settings), MODE_PRIVATE).edit();
            editor.putString("FirstRun", "0");
            editor.putString("server",txtServer.getText().toString());
            editor.putString("usuario",txtUser.getText().toString());
            editor.putString("pass",txtPass.getText().toString());
            editor.apply(); */
            this.view=view;
            String user=txtUser.getText().toString(),pass=txtPass.getText().toString();

            LoginTask lt = new LoginTask();
            try {
                String resultado=   lt.execute(user,pass).get();
                Log.i("resulado",resultado);
                JSONObject reader = new JSONObject(resultado);

                JSONArray contacts = reader.getJSONArray("Table");
                if(contacts.length()>0) {
                    SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.action_settings), MODE_PRIVATE).edit();
                    editor.putString("FirstRun", "0");
                    editor.putString("server",txtServer.getText().toString());
                    editor.putString("usuario",txtUser.getText().toString());
                    editor.putString("pass",txtPass.getText().toString());
                    JSONObject vendedor = contacts.getJSONObject(0);
                    editor.putString("nombre",vendedor.getString("AgenteNombre"));
                    editor.putString("email",vendedor.getString("agenteEmail"));
                    editor.apply();
                    creaCarpetas();
                  //  permisos();
                    img.setImageResource(R.drawable.ic_ok);
                    FirebaseMessaging.getInstance().subscribeToTopic(txtUser.getText().toString());
                    FirebaseMessaging.getInstance().subscribeToTopic(txtUser.getText().toString().substring(0, Math.min(txtUser.getText().toString().length(), 2)));
                }
                else {
                    img.setImageResource(R.drawable.ic_alert);
                }
                downloadFotoPerfil();
                creaCarpetaSQLITE();

            }catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    private void permisos() {

        if(ContextCompat.checkSelfPermission(SettingsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(SettingsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else
        {
            Snackbar snackbar= Snackbar.make(view,"GPS Autorizado",Snackbar.LENGTH_LONG);
            snackbar.show();


        }
    }

    private void creaCarpetaSQLITE()
    {

        File rootPath = new File(Environment.getExternalStorageDirectory(), "/IndarApp/db");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }
    }

    private void downloadFotoPerfil() {
      FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://indarapp-1540563064458.appspot.com/PersonalInfo");
        StorageReference  islandRef = storageRef.child("foco.PNG");

        File rootPath = new File(Environment.getExternalStorageDirectory(), "/IndarApp/PERFIL");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath,"perfil.jpg");

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ",";local tem file created  created " +localFile.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ",";local tem file not created  created " +exception.toString());
            }
        });
    }


    public  class LoginTask extends AsyncTask<String,Void,String>
    {
        String user,pass;


        @Override
        protected String doInBackground(String... strings) {
            user=strings[0];pass=strings[1];
            OkHttpClient client = new OkHttpClient();
            Response response=null;
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "username="+user+"&pass="+pass);
            Request request = new Request.Builder()
                    .url(txtServer.getText().toString()+getResources().getString(R.string.WEBLOGIN))
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    // .addHeader("Cache-Control", "no-cache")
                    //   .addHeader("Postman-Token", "3016ad7d-a309-449a-81cd-e1d5b037530f")
                    .build();
            Log.i("body",body.toString());
            try {
                response   = client.newCall(request).execute();
                return response.body().string();
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
}
