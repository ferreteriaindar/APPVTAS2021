package mx.indar.appvtas2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mx.indar.appvtas2.R.string;
import mx.indar.appvtas2.dbClases.MenuClientes.Cobro;
import mx.indar.appvtas2.dbClases.MenuClientes.CobroD;
import mx.indar.appvtas2.dbClases.MenuClientes.visitasHistorico;
import mx.indar.appvtas2.dbClases.art;
import mx.indar.appvtas2.dbClases.cliente;
import mx.indar.appvtas2.dbClases.cxcCliente;
import mx.indar.appvtas2.dbClases.especificos;
import mx.indar.appvtas2.dbClases.subirCobro;
import mx.indar.appvtas2.dbClases.subirCobroD;
import mx.indar.appvtas2.dbClases.visita;

public class dbAdapter {

    private SQLiteDatabase database;
    private db dbIndar;
    private  Context context;




    public dbAdapter(Context context) {
// Through this constructor we are creating/upgrading the database

        dbIndar = new db(context);
        this.context=context;
    }


    public void open(boolean begin) throws SQLException {
// Here database is opened with write permissions and will be assigned to the variable of this class

        database = dbIndar.getWritableDatabase();
       // if(begin==true)
            database.beginTransaction();

    }

    public void close(boolean end) {
// Closing the connection to the database
       // if(end ==true)
        database.setTransactionSuccessful();
       database.endTransaction();
       database.close();
        dbIndar.close();

    }

    public long addContact(cliente cte) {
// All the values used to create a record in table are collected in an ContectValues object
        ContentValues values = new ContentValues();
        values.put("cliente",cte.getCliente());
        values.put("nombreCliente", cte.getNombreCliente());
        values.put("rfc",cte.getRfc());
        values.put("zona",cte.getZona());

// Then ContentValues object will be passed to the insert() method of database which in turn return
// the ID value of the record inserted to the table
        return database.insert("clientes", null, values);
    }


    public boolean tienePendientes()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
       Log.i("update", dateFormat.format(cal.getTime())); //your formatted date here
        long rows =DatabaseUtils.queryNumEntries(database, "visitas",
                "fechainicio<=? ", new String[] {dateFormat.format(cal.getTime()).toString() });
        Log.i("update",rows+"REGISTROS");
        if(rows>=1)
            return  true;
        return false;
    }
/*
    public cliente getContact(int id) throws SQLException {
// Queries performed on database table return cursors
// query() method of database will be used to build and execute the query.
        Cursor cursor = database.query("clientes", new String[]{"cliente"}, "idclientes" + "=?", new String[] {String.valueOf(id)}, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();

// Values of the Cursor object are fed into Contacts object which will be returned by this method
        cliente contact = new cliente(cursor.getString(0), "ccccc", "xxxxxx","zsss");
        cursor.close();
        return contact;
    }
*/

    public  void borrarClientes()
    {
     database.execSQL("delete from  clientes");
    }
    public  void borrarArt(){database.execSQL("delete from art");}
    public  void borrarEspecificos(){database.execSQL("delete from especificos");}
    public  void borrarCobro(){database.execSQL("delete from Cobro");}
    public  void borrarCobroD(){database.execSQL("delete from CobroD");}
    public  void borrarvisitasHistorico(){database.execSQL("delete from visitasHistorico");}

    public  long llenaArticulos(art a)
    {
        ContentValues values = new ContentValues();
        values.put("Articulo",a.getArticulo());
        values.put("Descripcion1",a.getDescripcion1());
        values.put("Categoria",a.getCategoria());
        values.put("PrecioLista",a.getPrecioLista());
        values.put("Precio7",a.getPrecio7());
        values.put("Precio8",a.getPrecio8());
        values.put("multiplo",a.getMultiplo());
        values.put("CantidadMinimaVenta",a.getCantidadMinimaVenta());
        values.put("CantidadMaximaVenta",a.getCantidadMaximaVenta());
        values.put("disponible",a.getDisponible());

             return   database.insert("art",null,values);




    }


    public  long llenaClientesPorZona(cliente cte)
    {

        ContentValues values = new ContentValues();
        values.put("cliente",cte.getCliente());
        values.put("nombre", cte.getNombreCliente());
        values.put("rfc",cte.getRfc());
        values.put("zona",cte.getZona());
        values.put("direccion",cte.getDireccion());
        values.put("coordenadax",cte.getCoordenadax());
        values.put("coordenaday",cte.getCoordenaday());
        values.put("calle",cte.getCalle());
        values.put("poblacion",cte.getPoblacion());
        values.put("dia",cte.getDia());
        return database.insert("clientes", null, values);
    }



    public List<cliente> obtenerClientes(int dia)
    {

        List<cliente> listaClientes= new ArrayList<cliente>();

        String query="";
                if(dia==0)
                query=context.getString(string.SQLObtenerClientes);
                else query=context.getString(string.SQLObtenerClientes)+ " where dia="+dia+"";
        Log.i("query",query);
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do{
              cliente cte = new cliente();
              cte.setCliente(cursor.getString(0));
              cte.setNombreCliente(cursor.getString(1));
              cte.setDireccion(cursor.getString(7));
              listaClientes.add(cte);
            }while (cursor.moveToNext());


        }
        cursor.close();
        return  listaClientes;

    }

    public  long registraVisitaCte(visita v)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd kk:mm:ss",Locale.CANADA);
        Date date = new Date();
        Log.i("fecha",dateFormat.format(date));
        ContentValues values = new ContentValues();
        values.put("cliente",v.getCliente());
        values.put("latitud",v.getLatitud());
        values.put("longitud",v.getLongitud());
        values.put("fechaInicio",dateFormat.format(date));
        return database.insert("visitas", null, values);

    }

    public  List<visita> obtenerVisitas()
    {
        List<visita> v = new ArrayList<>();
        String query=context.getString(string.SQLObtenerVisitas);
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do{
                visita vi = new visita();
               vi.setCliente(cursor.getString(1));
               vi.setFechaInicio(cursor.getString(2));
                vi.setLatitud(cursor.getFloat(6));
                vi.setLongitud(cursor.getFloat(7));
                vi.setIdvisita(cursor.getInt(0));
                vi.setFechaPromociones(cursor.getString(4));
                vi.setLatitudPromociones(cursor.getFloat(13));
                vi.setLongitudPromociones(cursor.getFloat(14));
                vi.setLatitudCobranza(cursor.getFloat(15));
                vi.setLongitudCobranza(cursor.getFloat(16));
                vi.setFechaCobranza(cursor.getString(3));
                vi.setFechaFin(cursor.getString(17));
                vi.setLatitudFin(cursor.getFloat(18));
                vi.setLongitudFin(cursor.getFloat(19));
                v.add(vi);
            }while (cursor.moveToNext());


        }
        cursor.close();
        return  v;





    }

    public  long insertVisitasHistorico(visitasHistorico vh)
    {
        ContentValues cv = new ContentValues();
        cv.put("cliente",vh.getCliente());
        cv.put("fechaInicio",vh.getFechaInicio());
        cv.put("fechaCobranza",vh.getFechaCobranza());
        cv.put("fechaPromociones",vh.getFechaPromociones());
        cv.put("latitud",vh.getLatitud());
        cv.put("longitud",vh.getLongitud());
        cv.put("latitudPedido",vh.getLatitudPedido());
        cv.put("longitudPedido",vh.getLongitudPedido());
        cv.put("latitudPromociones",vh.getLatitudPromociones());
        cv.put("longitudPromociones",vh.getLatitudPromociones());
        cv.put("latitudCobranza",vh.getLatitudCobranza());
        cv.put("longitudCobranza",vh.getLongitudCobranza());
        return database.insert("visitasHistorico",null,cv);

    }

    public  List<visitasHistorico>  selectVisitasHistorico(String fecha,String tipo,String cliente)
    {
        List<visitasHistorico> vh = new ArrayList<>();
        String query="select * from visitasHistorico where   date(fechainicio)='"+fecha+"'";
        if(tipo.equals("cliente"))
            query="select * from  visitasHistorico where cliente='"+fecha+"'";
        if(tipo.equals("detalle"))
            query="select * from  visitasHistorico where fechaInicio='"+fecha+"' and cliente='"+cliente+"'";
        Log.i("map",query);
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do{
                visitasHistorico histo = new visitasHistorico();
                histo.setCliente(cursor.getString(0));
                histo.setFechaInicio(cursor.getString(1));
                histo.setFechaCobranza(cursor.getString(2));
                histo.setFechaPromociones(cursor.getString(3));
                histo.setFechaVenta(cursor.getString(4));
                histo.setLatitud(Float.parseFloat(cursor.getString(5)));
                histo.setLongitud(Float.parseFloat(cursor.getString(6)));
                histo.setLatitudPedido(Float.parseFloat(cursor.getString(7)));
                histo.setLongitudPedido(Float.parseFloat(cursor.getString(8)));
                histo.setLatitudPromociones(Float.parseFloat(cursor.getString(12)));
                histo.setLongitudPromociones(Float.parseFloat(cursor.getString(13)));
                histo.setLatitudCobranza(Float.parseFloat(cursor.getString(14)));
                histo.setLongitudCobranza(Float.parseFloat(cursor.getString(15)));
                vh.add(histo);
            }while (cursor.moveToNext());


        }
        cursor.close();
        return vh;
    }


    public  void borrarCXC(){database.execSQL("delete from cxcCliente");}

    public long obtenerCXCZona(cxcCliente cxcCliente)
    {
        ContentValues values = new ContentValues();
        values.put("Cliente",cxcCliente.getCliente());
        values.put("Mov",cxcCliente.getMov());
        values.put("MovID",cxcCliente.getMovID());
        values.put("FechaEmision",cxcCliente.getFechaEmision());
        values.put("Vencimiento",cxcCliente.getVencimiento());
        values.put("DiasMoratorios",cxcCliente.getDiasMoratorios());
        values.put("Saldo",cxcCliente.getSaldo());
        values.put("Referencia",cxcCliente.getReferencia());
        values.put("descuento", cxcCliente.getDescuento());

        return database.insert("cxcCliente", null, values);


    }


    public  List<especificos>  obtenerEspecifico()
    {
        List<especificos> Listaespecificos= new ArrayList<>();
        String query=context.getString(string.SQLespecificos);
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do{
                especificos e = new especificos();
                e.setCuota(Float.parseFloat( cursor.getString(0)));
                e.setVenta(Float.parseFloat(cursor.getString(1)));
                e.setPorcentaje(Float.parseFloat(cursor.getString(2)));
                e.setEspecificoNombre1(cursor.getString(3));
                e.setEspecificoCuota1(Float.parseFloat(cursor.getString(4)));
                e.setEspecificoVenta1(Float.parseFloat(cursor.getString(5)));
                e.setEspecificoNombre2(cursor.getString(6));
                e.setEspecificoCuota2(Float.parseFloat(cursor.getString(7)));
                e.setEspecificoVenta2(Float.parseFloat(cursor.getString(8)));
                e.setEspecificoNombre3(cursor.getString(9));
                e.setEspecificoCuota3(Float.parseFloat(cursor.getString(10)));
                e.setEspecificoVenta3(Float.parseFloat(cursor.getString(11)));
                e.setEspecificoNombre4(cursor.getString(12));
                e.setEspecificoCuota4(Float.parseFloat(cursor.getString(13)));
                e.setEspecificoVenta4(Float.parseFloat(cursor.getString(14)));
                Listaespecificos.add(e);

            }while (cursor.moveToNext());


        }
        cursor.close();
        Log.i(("especificos"),Listaespecificos.size()+"tamaño");
        return  Listaespecificos;



    }

    public  long  llenarEspecifico(especificos e)
    {
        ContentValues values = new ContentValues();
        values.put("Cuota",e.getCuota());
        values.put("Venta",e.getVenta());
        values.put("porcentaje",e.getPorcentaje());
        values.put("EspecificoNombre1",e.getEspecificoNombre1());
        values.put("EspecificoCuota1",e.getEspecificoCuota1());
        values.put("EspecificoVenta1",e.getEspecificoVenta1());
        values.put("EspecificoNombre2",e.getEspecificoNombre2());
        values.put("EspecificoCuota2",e.getEspecificoCuota2());
        values.put("EspecificoVenta2",e.getEspecificoVenta2());
        values.put("EspecificoNombre3",e.getEspecificoNombre3());
        values.put("EspecificoCuota3",e.getEspecificoCuota3());
        values.put("EspecificoVenta3",e.getEspecificoVenta3());
        values.put("EspecificoNombre4",e.getEspecificoNombre4());
        values.put("EspecificoCuota4",e.getEspecificoCuota4());
        values.put("EspecificoVenta4",e.getEspecificoVenta4());
        return database.insert("especificos", null, values);

    }

    public  List<cxcCliente> regresaCXCCliente(String cliente)
    {
        List<cxcCliente> listacxc= new ArrayList<cxcCliente>();

        String query="";

            query=context.getString(string.SQLObtenerCXCZONA)+"  and  cliente='"+cliente+"' ORDER BY DiasMoratorios DESC";

        Log.i("query",query);
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do{
                cxcCliente cxc = new cxcCliente();
                cxc.setMov(cursor.getString(1));
                cxc.setMovID(cursor.getString(2));
                cxc.setFechaEmision(cursor.getString(3));
                cxc.setVencimiento(cursor.getString(4));
                cxc.setDiasMoratorios(cursor.getInt(5));
                cxc.setSaldo(cursor.getFloat(6));
                cxc.setReferencia(cursor.getString(7));
                cxc.setDescuento(cursor.getFloat(8));
                listacxc.add(cxc);
            }while (cursor.moveToNext());


        }
        cursor.close();
        return  listacxc;


    }




    public  int actualizaAvanceVisita(int idvisitas,String avance,float latitud,float longitud)
    {
        Log.i("actualizaAvanceVisita","latitud"+latitud+"longitud"+longitud);

        ContentValues cv = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd kk:mm:ss",Locale.CANADA);
        Date date = new Date();
        switch (avance)
        {
            case "cobranza": cv.put("fechaCobranza",dateFormat.format(date));
                             cv.put("latitudCobranza",latitud);
                             cv.put("longitudCobranza",longitud);

            break;
            case "promo": cv.put("fechaPromociones",dateFormat.format(date));
                          cv.put("latitudPromociones",latitud);
                          cv.put("longitudPromociones",longitud);
            break;
            case "Venta": cv.put("fechaVenta",dateFormat.format(date));
                          cv.put("latitudPedido",latitud);
                          cv.put("longitudPedido",longitud);
            break;
            case "PostVenta": cv.put("fechaPostVenta",dateFormat.format(date));
                              cv.put("latitudPostVenta",latitud);
                              cv.put("longitudPostVenta",longitud);
            break;
            case "fin":  cv.put("fechaFin",dateFormat.format(date));
                        cv.put("latitudFin",latitud);
                        cv.put("longitudFin",longitud);
            break;
        }
        return  database.update("visitas",cv,"idVisitas=?",new String[]{""+idvisitas});


    }


    public  List<visita> visitasMapa()
    {
        List<visita> v = new ArrayList<>();
        String query=context.getString(string.SQLvisitasMapa);
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do{
                visita vi = new visita();
                vi.setCliente(cursor.getString(0));
                vi.setFechaInicio(cursor.getString(1));
                vi.setLatitud(cursor.getFloat(2));
                vi.setLongitud(cursor.getFloat(3));
                v.add(vi);
            }while (cursor.moveToNext());


        }
        cursor.close();
        return  v;

    }


    public  boolean borrarVisitas(int id)
    {
      return  database.delete("visitas","idvisitas="+id,null)>0;

    }

    public Cursor rawQuery(String query)
    {

        Cursor cursor = database.rawQuery(query,null);
        return  cursor;
    }


    ///////////////////////////INICIO//////////////////////////////////////////////////
    ////////////////////////RECIBOS DE COBRO app///////////////////////////////////////////////

    public  void insertaCobro(Cobro c)
    {
        ContentValues cv = new ContentValues();
        cv.put("IdCobro",c.getId());
        cv.put("Cliente",c.getCliente());
        cv.put("FormaPago",c.getFormaPago());
        cv.put("Referencia",c.getReferencia());
        cv.put("importe",c.getImporte());
        cv.put("fechaPago",c.getFechaPago());
        cv.put("fechaRegistro",c.getFechaRegistro());
        cv.put("usuario",c.getUsuario());
        cv.put("numCobro",c.getNumCobro());

        database.insert("Cobro",null,cv);
    }


    public  void insertaCobroD(CobroD cd)
    {
        ContentValues cv = new ContentValues();
        cv.put("IdCobro",cd.getIdCobro());
        cv.put("Mov",cd.getMov());
        cv.put("MovId",cd.getMovid());
        cv.put("importe",cd.getImporte());
        cv.put("descuento",cd.getDescuento());
        cv.put("aplicaDescto",cd.getAplicaDescto());

        database.insert("CobroD",null,cv);

    }

    public List<Cobro> SelectCobro(String cliente,int id)
    {
        List<Cobro> c = new ArrayList<>();

        String query="select * from Cobro where cliente='"+cliente+"'";
        if(id!=0)
            query ="select * from  Cobro where cliente='"+cliente+"' and IdCobro="+id;
        Cursor cursor = database.rawQuery(query,null);
        {
            if(cursor.moveToFirst()) {
                do {
                    Cobro cobro = new Cobro();
                    cobro.setId(cursor.getInt(0));
                    cobro.setCliente(cursor.getString(1));
                    cobro.setFormaPago(cursor.getString(2));
                    cobro.setReferencia(cursor.getString(3));
                    cobro.setImporte(cursor.getFloat(4));
                    cobro.setFechaPago(cursor.getString(5));
                    Log.i("cobro","select"+cursor.getString(6));
                    cobro.setFechaRegistro(cursor.getString(6));
                    cobro.setUsuario(cursor.getString(7));
                    cobro.setNumCobro(cursor.getInt(8));
                    c.add(cobro);
                } while (cursor.moveToNext());
            }

        }
        cursor.close();
        return  c;
    }


    public List<CobroD> selectCobroD (int IdCobro)
    {
        List<CobroD> c = new ArrayList<>();
        String query="select * from CobroD where IdCobro="+IdCobro;
        Cursor cursor = database.rawQuery(query,null);
        {
            if(cursor.moveToFirst()) {
                do {
                    CobroD cd = new CobroD();
                    cd.setIdCobro(cursor.getInt(0));
                    cd.setMov(cursor.getString(1));
                    cd.setMovid(cursor.getString(2));
                    cd.setImporte(cursor.getFloat(3));
                    cd.setDescuento(cursor.getFloat(4));
                    cd.setAplicaDescto(cursor.getString(5));
                    c.add(cd);

                } while (cursor.moveToNext());
            }

        }
        cursor.close();
        return  c;

    }



    ///////////////////////////FIN//////////////////////////////////////////////////



    ///////////////////////////INICIO//////////////////////////////////////////////////
    ////////////////////////RECIBOS DE COBRO///////////////////////////////////////////////

    public  void  borrarRecibosDeCobro()
    {

          database.execSQL("delete from subirCobroD");
          database.execSQL("delete from subirCobro");
    }



    public  long insertarsubirCobro(subirCobro sc)
    {
        ContentValues cv = new ContentValues();
        cv.put("cliente",sc.getCliente());
        cv.put("zona",sc.getZona());
        cv.put("formaPago",sc.getFormaPago());
        cv.put("importe",sc.getImporte());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd kk:mm:ss",Locale.CANADA);
        Date date = new Date();
        cv.put("fechapago",dateFormat.format(date));
        cv.put("referencia",sc.getReferencia());

        return  database.insert("subirCobro",null,cv);
    }


    public  void insertarsubirCobroD(subirCobroD scd)
    {

        ContentValues cv = new ContentValues();
        cv.put("idsubirCobro",scd.getIdSubirCobro());
        cv.put("mov",scd.getMov());
        cv.put("movid",scd.getMovid());
        cv.put("importe",scd.getImporte());
        cv.put("descuento",scd.getDescuento());
        cv.put("aplicadescto",scd.getAplicaDescto());

        database.insert("subirCobroD",null,cv);

    }

    public List<subirCobro> obtenersubirCobro()
    {
        List<subirCobro> ListaCobro = new ArrayList<>();
        String query="select * from  subirCobro";
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do{
                subirCobro  sc = new subirCobro();
                sc.setId(cursor.getInt(0));
                sc.setCliente(cursor.getString(1));
                sc.setZona(cursor.getString(2));
                sc.setFormaPago(cursor.getString(3));
                sc.setImporte(cursor.getFloat(4));
                sc.setReferencia(cursor.getString(5));
                sc.setFechapago(cursor.getString(6));
                ListaCobro.add(sc);
            }while (cursor.moveToNext());


        }
        cursor.close();
        return  ListaCobro;
    }

    public List<subirCobroD> obtenersubirCobroD(int id)
    {
        List<subirCobroD> listasubirCobroD = new ArrayList<>();
        String query= "select * from subirCobroD where idsubirCobro="+id+"";
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do{
               subirCobroD scd = new subirCobroD();
               scd.setIdSubirCobro(cursor.getInt(0));
               scd.setMov(cursor.getString(1));
               scd.setMovid(cursor.getString(2));
               scd.setImporte(cursor.getFloat(3));
               scd.setDescuento(cursor.getFloat(4));
               scd.setAplicaDescto(cursor.getString(5));
               listasubirCobroD.add(scd);
            }while (cursor.moveToNext());


        }
        cursor.close();
        return listasubirCobroD;


    }


    public  void BorrarReciboCobroID(Integer id)
        {
            database.delete("subirCobroD","idsubirCobro="+id+"",null);
            database.delete("subirCobro","id="+id+"",null);
        }


       public  void borrarTodos()
       {
           database.execSQL("delete from clientes");
           database.execSQL("delete from art");
           database.execSQL("delete from visitas");
           database.execSQL("delete from cxcCliente");
           database.execSQL("delete from especificos");
           database.execSQL("delete from subirCobro");
           database.execSQL("delete from subirCobroD");
           database.execSQL("delete from Cobro");
           database.execSQL("delete from CobroD");
           database.execSQL("delete from visitasHistorico");




       }

    ///////////////////////////FIN//////////////////////////////////////////////////
    ////////////////////////RECIBOS DE COBRO///////////////////////////////////////////////

}
