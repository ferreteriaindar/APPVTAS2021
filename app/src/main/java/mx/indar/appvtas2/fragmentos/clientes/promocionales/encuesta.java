package mx.indar.appvtas2.fragmentos.clientes.promocionales;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import mx.indar.appvtas2.R;

public class encuesta extends AppCompatActivity {

    ImageView  malo,regular,bueno;
    Boolean  bMalo,bRegular,bBueno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        malo=findViewById(R.id.btnPromoMalo);
        regular=findViewById(R.id.btnPromoRegular);
        bueno=findViewById(R.id.btnPromoBueno);
        bMalo=false;
        bRegular=false;
        bBueno=false;
        malo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(bMalo) {
                   malo.setImageResource(R.drawable.ic_face_anger2);
                   regular.setImageResource(R.drawable.ic_face_sad);
                   bueno.setImageResource(R.drawable.ic_face_happy);
                   bMalo=false;
               }
               else {malo.setImageResource(R.drawable.ic_face_anger);
               bMalo=true;
               }
            }
        });
        regular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bRegular)
                {
                    regular.setImageResource(R.drawable.ic_face_sad2);
                    bueno.setImageResource(R.drawable.ic_face_happy);
                    malo.setImageResource(R.drawable.ic_face_anger);
                    bRegular=false;
                }
                else
                    {
                        regular.setImageResource(R.drawable.ic_face_sad);
                        bRegular=true;
                    }
            }
        });
        bueno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bBueno)
                {
                    bueno.setImageResource(R.drawable.ic_face_happy2);
                    malo.setImageResource(R.drawable.ic_face_anger);
                    regular.setImageResource(R.drawable.ic_face_sad);
                    bBueno=false;
                }
                else  {bueno.setImageResource(R.drawable.ic_face_happy);
                        bBueno=true;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_encuesta,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menuencuestaFin:
                //imprimirCobro();



                return  true;


        }
        return super.onOptionsItemSelected(item);
    }
}
