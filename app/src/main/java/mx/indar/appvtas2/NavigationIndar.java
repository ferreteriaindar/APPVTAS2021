package mx.indar.appvtas2;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import mx.indar.appvtas2.Bluetooth.BlueToothController;
import mx.indar.appvtas2.dbClases.visita;
import mx.indar.appvtas2.fragmentos.clientes.ClientesFragment;
import mx.indar.appvtas2.fragmentos.clientes.agenda.AgendasFragment;
import mx.indar.appvtas2.fragmentos.clientes.agenda.MapsActivityVisitas;
import mx.indar.appvtas2.fragmentos.clientes.agenda.VisitaAgendas;

public class NavigationIndar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences prefs;
    TextView txtEnviar;

  public   DrawerLayout drawer;
  public  boolean permiteNavegar;


    public FloatingActionButton getFab() {
        return fab;
    }



    FloatingActionButton fab;

    public Toolbar getToolbar() {
        return toolbar;
    }



    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_indar);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences(getResources().getString(R.string.action_settings), MODE_PRIVATE);
        permiteNavegar=true;



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        TextView navUsername= headerView.findViewById(R.id.txtNavNombreVendedor);
        TextView navEmail = headerView.findViewById(R.id.txtNavEmailVendedor);
        navUsername.setText(prefs.getString("nombre",""));
        navEmail.setText(prefs.getString("email",""));
        txtEnviar =(TextView)  MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_Enviar));

        this.getSupportActionBar().setTitle(prefs.getString("usuario",""));
        buscaNotifiaciones();


        String filePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "IndarApp/PERFIL/perfil.jpg";
        Bitmap bmp = BitmapFactory.decodeFile(filePath);
        Log.i("perfil", filePath);

        ImageView perfilfoto=headerView.findViewById(R.id.imagePerfil2);

            perfilfoto.setImageBitmap(bmp);
         ImageView btnHome = headerView.findViewById(R.id.btnHome);
         btnHome.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Fragment fragment = new Especificos();
                 FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                 tx.replace(R.id.contenidoNav, fragment);
                 tx.commit();
                 drawer.closeDrawers();
             }
         });





        if(savedInstanceState==null) {
            Fragment fragment = new Especificos();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.contenidoNav, fragment);
            tx.commit();
        }
        if(prefs.getString("FirstRun","").equals("0"))
        {
            DownloadBases down = new DownloadBases(this,prefs.getString("usuario",""));
            try {
                if(down.execute().get()){
                SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.action_settings), MODE_PRIVATE).edit();
                editor.putString("FirstRun","1");
                editor.commit();
                Fragment fragment = new Especificos();
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.contenidoNav, fragment);
                tx.commit();}
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }



    private void buscaNotifiaciones() {

         txtEnviar.setTextColor(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_orange_dark));
        List<visita> v =new ArrayList<>();
        dbAdapter db = new dbAdapter(this.getApplicationContext());
        try {

            db.open(true);
            v=db.obtenerVisitas();
            db.close(true);
            txtEnviar.setText(v.size()+"+");
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }




    public interface IOnBackPressed {
        /**
         * If you return true the back press will not be taken into account, otherwise the activity will act naturally
         * @return true if your processing has priority if not false
         */
     //   boolean permiteSalir();
        public  boolean fragmentSalir=false;

        boolean onBackPressed();





    }




    @Override
    public void onBackPressed() {
        drawer= (DrawerLayout) findViewById(R.id.drawer_layout);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contenidoNav);

            if (drawer.isDrawerOpen(GravityCompat.START)) {

                drawer.closeDrawer(GravityCompat.START);

             if(((IOnBackPressed)fragment).onBackPressed())
                {
                    Log.i("Back", "si salio interface");
                    super.onBackPressed();
                }

            } else {
                Log.i("Back", "si salio normal else ");
                   // super.onBackPressed();
                if(((IOnBackPressed)fragment).onBackPressed())
                {
                    Log.i("Back", "si salio interface dentro else ");
                    super.onBackPressed();
                }
            }

        buscaNotifiaciones();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.navigation_indar, menu);

        buscaNotifiaciones();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        buscaNotifiaciones();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

                int id = item.getItemId();

                Fragment miFragment = null;
                boolean fragmentSeleccionado = false;

                if (id == R.id.nav_Agenda) {
                    miFragment = new AgendasFragment();
                    fragmentSeleccionado = true;
                } else if (id == R.id.nav_Clientes) {
                    miFragment = new ClientesFragment();

                    fragmentSeleccionado = true;

                } else if (id == R.id.nav_Inventario) {
                    Intent intent = new Intent(this,MapsActivityVisitas.class);
                    startActivity(intent);

                } else if (id == R.id.nav_Mensajes) {
                  /*  miFragment = new BlueToothController();
                    fragmentSeleccionado = true;
*/
                    dbAdapter db = new dbAdapter(getApplicationContext());
                    try {
                        db.open(true);
                      if(  db.tienePendientes()) {
                          UPLOADinfo up = new UPLOADinfo(NavigationIndar.this, "");
                          up.execute();
                      }
                        DownloadBases downb = new DownloadBases(NavigationIndar.this, prefs.getString("usuario", ""));

                        downb.execute();

                        db.close(true);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else if (id == R.id.nav_share) {  //// DESCARGAR INFO

                    dbAdapter db = new dbAdapter(getApplicationContext());
                    try {
                        db.open(true);
                        if(  db.tienePendientes()) {
                            db.close(true);
                            //  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(NavigationIndar.this);
                            builder.setTitle("Tienes Pendientes por entregar \n Primero envia tu INFO");

                            builder.setPositiveButton("ENTERADO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    }
                            });


                            builder.show();
                        }
                        else {
                            db.close(true);
                            DownloadBases downb = new DownloadBases(NavigationIndar.this, prefs.getString("usuario", ""));
                            downb.execute();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }



                    //downb.p.dismiss();

                } else if (id == R.id.nav_Enviar) {
                    UPLOADinfo up = new UPLOADinfo(NavigationIndar.this, "");
                    up.execute();
                }


                if (fragmentSeleccionado == true) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// getSupportFragmentManager().beginTransaction().replace(R.id.content_main, miFragment).commit();

                    ft.replace(R.id.contenidoNav, miFragment);
                    ft.commit();
                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

                drawer.closeDrawer(GravityCompat.START);
               // buscaNotifiaciones();

        return true;
    }



}
