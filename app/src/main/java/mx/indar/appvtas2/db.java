package mx.indar.appvtas2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class db extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 22;
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
    //VERSION 14 SE agrega el campo  de descuento en la tabla CXCCLIENTE
    //VERSION 15 se agrega campo de  referencia en cobro
    //VERSION 16 Se agrega  campos (descuento y aplicadestco para los cobros
    //VERSION 17  SE agrega  las tablas Cobro y CobroD
    //VERSION 18 Se agrega la tabla de visitasHistorico
    //VERSION 19 Se agrega campo finvisita,y las coordenada de fin visita   en la tabla visitas y visitas historico
    //VERSION 20 Se agrega la tabla  visitasPromo
    //VERSION 21 Se agregan  precio2,3  ,proveedor y linea en la tabla de art
    //VERSION 22 Se agrega  metrostolerancia en table clientes
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
                "    dia         INTEGER,\n" +
                "    metrosTolerancia   INTERGER\n" +
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
                "    disponible          REAL    NOT NULL,\n" +
                "    Precio2             REAL    NOT NULL,\n" +
                "    Precio3            REAL     NOT NULL,\n" +
                "    Proveedor           TEXT    NOT NULL,\n" +
                "    Linea               TEXT    NOT NULL"+
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
                "    longitudCobranza REAL, \n"  +
                "    fechaFin         TEXT,\n"  +
                "    longitudFin       REAL,\n" +
                "    latitudFin        REAL"+
                ");");


        db.execSQL("CREATE TABLE cxcCliente (\n" +
                "    Cliente        TEXT    NOT NULL,\n" +
                "    Mov            TEXT    NOT NULL,\n" +
                "    MovID          TEXT    NOT NULL,\n" +
                "    FechaEmision   TEXT    NOT NULL,\n" +
                "    Vencimiento    TEXT    NOT NULL,\n" +
                "    DiasMoratorios INTEGER,\n" +
                "    Saldo          REAL,\n" +
                "Referencia     TEXT,\n" +
                "descuento      REAL"+

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
                "    importe   REAL,\n" +
                "    referencia TEXT    NOT NULL,\n" +
                "    fechapago  TEXT\n" +
                ");\n");

        db.execSQL("CREATE TABLE subirCobroD (\n" +
                "    idsubirCobro INTEGER NOT NULL,\n" +
                "    mov          TEXT,\n" +
                "    movid        TEXT,\n" +
                "    importe      REAL,\n" +
                "    descuento     REAL,\n"+
                "    aplicadescto  TEXT\n"+
                ");\n");

        db.execSQL("CREATE TABLE Cobro (\n" +
                "    IdCobro       INTEGER NOT NULL\n" +
                "                          PRIMARY KEY,\n" +
                "    Cliente       TEXT    REFERENCES clientes (Cliente),\n" +
                "    FormaPago     TEXT,\n" +
                "    Referencia    TEXT,\n" +
                "    importe       REAL,\n" +
                "    fechaPago     TEXT,\n" +
                "    fechaRegistro TEXT,\n" +
                "    usuario       TEXT,\n" +
                "    numCobro      INT\n" +
                ");");

        db.execSQL("CREATE TABLE CobroD (\n" +
                "    IdCobro      INTEGER REFERENCES Cobro (IdCobro),\n" +
                "    Mov          TEXT,\n" +
                "    MovId        TEXT,\n" +
                "    importe      REAL,\n" +
                "    descuento    REAL,\n" +
                "    aplicaDescto TEXT\n" +
                ");");

        db.execSQL("CREATE TABLE visitasHistorico (\n" +
                "    cliente             TEXT NOT NULL,\n" +
                "    fechaInicio         TEXT NOT NULL,\n" +
                "    fechaCobranza       TEXT,\n" +
                "    fechaPromociones    TEXT,\n" +
                "    fechaVenta          TEXT,\n" +
                "    latitud             REAL,\n" +
                "    longitud            REAL,\n" +
                "    latitudPedido       REAL,\n" +
                "    longitudPedido      REAL,\n" +
                "    fechaPostVenta      TEXT,\n" +
                "    latitudPostVenta    REAL,\n" +
                "    longitudPostVenta   REAL,\n" +
                "    latitudPromociones  REAL,\n" +
                "    longitudPromociones REAL,\n" +
                "    latitudCobranza     REAL,\n" +
                "    longitudCobranza    REAL,\n" +
                "    fechaFin            TEXT,\n" +
                "    latitudFin          REAL,\n" +
                "    longitudFin         REAL\n"  +
                ");\n");

        db.execSQL("CREATE TABLE visitasPromo (\n" +
                "    idVisita          INT  PRIMARY KEY,\n" +
                "    ferreImpulsos     INT,\n" +
                "    muestras          INT,\n" +
                "    especificoNombre1 TEXT,\n" +
                "    especifico1       INT,\n" +
                "    especificoNombre2 TEXT,\n" +
                "    especifico2       INT,\n" +
                "    especificoNombre3 TEXT,\n" +
                "    especifico3       INT,\n" +
                "    especificoNombre4 TEXT,\n" +
                "    especifico4       INT\n" +
                ");");

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
        db.execSQL("drop table if exists "+"Cobro");
        db.execSQL("drop table if exists "+"CobroD");
        db.execSQL("drop table if exists "+" visitasHistorico");
        db.execSQL("drop table if exists "+" visitasPromo");


        onCreate(db);
    }
}
