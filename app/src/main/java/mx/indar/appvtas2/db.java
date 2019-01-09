package mx.indar.appvtas2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class db extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 13;
    //VERSION 1  pruebas
    //VERSION 2  solo alta tabla de clientes
    //VERSION 3  se crea la tabla de art
    //VERSION 4  Me equivoque de nombre de columna jeje
    //VERSION 6  Se hace publica la baase // no funcionó es para acceder el .db
    //VERSION 7  Se Crea la tabla Visitas
    //VERSION 8 Se quita  el auto getdate de la columna fecha
    //VERSION 9 Se agrega campo fechaPostVenta en la tabla de Visitas y sus Ubicaciones
    //VERSION 10 Se crea la Tabla de cxcClientes para ver  documentos pendientes del cliente
    //VERSION 11 SE Crea la Tabla Especificos del vendedor
    //VERSION 12 Se crea ta Table subirCobro y subirCobroD  para los recibos de cobro CXC
    //VERSION 13 SE Agregan campor en la tabla de VISITAS   latitud y longitud cobranza
    public static final String DATABASE_NAME = "indarAPP";




    public db(Context context) {


        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TABLA DE CLIENTES
        db.execSQL("CREATE TABLE clientes (\n" +
                "    Cliente     TEXT    PRIMARY KEY\n" +
                "                        UNIQUE\n" +
                "                        NOT NULL,\n" +
                "    Nombre      TEXT    NOT NULL,\n" +
                "    zona        TEXT    NOT NULL,\n" +
                "    direccion   TEXT    NOT NULL,\n" +
                "    rfc         TEXT    NOT NULL,\n" +
                "    coordenadax REAL,\n" +
                "    coordenaday REAL,\n" +
                "    calle       TEXT,\n" +
                "    Poblacion   TEXT,\n" +
                "    dia         INTEGER\n" +
                ");");
        db.execSQL("CREATE TABLE art (\n" +
                "    Articulo            TEXT    PRIMARY KEY\n" +
                "                                NOT NULL\n" +
                "                                UNIQUE,\n" +
                "    Descripcion1        TEXT    NOT NULL,\n" +
                "    Categoria           TEXT    NOT NULL,\n" +
                "    PrecioLista         REAL    NOT NULL,\n" +
                "    Precio7             REAL    NOT NULL,\n" +
                "    Precio8             REAL    NOT NULL,\n" +
                "    multiplo            INTEGER NOT NULL,\n" +
                "    CantidadMinimaVenta  REAL    NOT NULL,\n" +
                "    CantidadMaximaVenta REAL    NOT NULL,\n" +
                "    disponible          REAL    NOT NULL\n" +
                ");");
        db.execSQL("CREATE TABLE visitas (\n" +
                "    idVisitas        INTEGER PRIMARY KEY AUTOINCREMENT,\n" +  //0
                "    cliente          TEXT    NOT NULL,\n" +
                "    fechaInicio      TEXT    NOT NULL,\n" +
                "    fechaCobranza    TEXT,\n" +
                "    fechaPromociones TEXT,\n" +
                "    fechaVenta       TEXT,\n" +
                "    latitud          REAL,\n" +
                "    longitud         REAL,\n" +
                "    latitudPedido    REAL,\n" +
                "    longitudPedido   REAL,\n" +
                "    fechaPostVenta   TEXT,\n" +
                "    latitudPostVenta REAL,\n" +
                "    longitudPostVenta REAL,\n" +
                "    latitudPromociones REAL,\n" +
                "    longitudPromociones REAL,\n" +//14
                "    latitudCobranza REAL,\n"  +
                "    longitudCobranza REAL \n"  +
                ");");

        db.execSQL("CREATE TABLE cxcCliente (\n" +
                "    Cliente        TEXT    NOT NULL,\n" +
                "    Mov            TEXT    NOT NULL,\n" +
                "    MovID          TEXT    NOT NULL,\n" +
                "    FechaEmision   TEXT    NOT NULL,\n" +
                "    Vencimiento    TEXT    NOT NULL,\n" +
                "    DiasMoratorios INTEGER,\n" +
                "    Saldo          REAL,\n" +
                "    Referencia\n" +
                ");");
        db.execSQL("CREATE TABLE especificos (\n" +
                "    Cuota             REAL,\n" +
                "    Venta             REAL,\n" +
                "    porcentaje        REAL,\n" +
                "    EspecificoNombre1 TEXT,\n" +
                "    EspecificoCuota1  REAL,\n" +
                "    EspecificoVenta1  REAL,\n" +
                "    EspecificoNombre2 TEXT,\n" +
                "    EspecificoCuota2  REAL,\n" +
                "    EspecificoVenta2  REAL,\n" +
                "    EspecificoNombre3 TEXT,\n" +
                "    EspecificoCuota3  REAL,\n" +
                "    EspecificoVenta3  REAL,\n" +
                "    EspecificoNombre4 TEXT,\n" +
                "    EspecificoCuota4  REAL,\n" +
                "    EspecificoVenta4  REAL\n" +
                ");\n");

        db.execSQL("CREATE TABLE subirCobro (\n" +
                "    id        INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                      NOT NULL,\n" +
                "    cliente   TEXT    NOT NULL,\n" +
                "    zona      TEXT    NOT NULL,\n" +
                "    formaPago TEXT,\n" +
                "    importe   REAL\n" +
                ");\n");

        db.execSQL("CREATE TABLE subirCobroD (\n" +
                "    idsubirCobro INTEGER NOT NULL,\n" +
                "    mov          TEXT,\n" +
                "    movid        TEXT,\n" +
                "    importe      REAL\n" +
                ");\n");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Contact App", "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        Log.i("database","si actualiza");
      db.execSQL("drop table if exists " + "clientes");
        db.execSQL("drop table if exists " + "art");
        db.execSQL("drop table if exists "+"visitas");
        db.execSQL("drop table if exists "+"cxcCliente");
        db.execSQL("drop table if exists "+"especificos ");
        db.execSQL("drop table if exists "+"subirCobroD");
        db.execSQL("drop table if exists "+"subirCobro");

        onCreate(db);
    }
}
