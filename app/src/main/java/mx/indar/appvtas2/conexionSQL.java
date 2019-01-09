package mx.indar.appvtas2;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class conexionSQL extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "SERVIDOR";
    SharedPreferences prefs;
    String ip,pass,sa,base;
    public  conexionSQL()
    {
/*
        prefs=   getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        ip=prefs.getString("IP","192.168.87.100");
        pass=prefs.getString("password","7Ind4r7");
        sa=prefs.getString("SA","sa");
        base=prefs.getString("base","master");
*/

    }


    public  String login(String user,String pass)
    {
        return "";
    }







}
