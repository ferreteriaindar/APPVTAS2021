package mx.indar.appvtas2.dbClases;

public class visita {

    public int getIdvisita() {
        return idvisita;
    }

    public void setIdvisita(int idvisita) {
        this.idvisita = idvisita;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaCobranza() {
        return fechaCobranza;
    }

    public void setFechaCobranza(String fechaCobranza) {
        this.fechaCobranza = fechaCobranza;
    }

    public String getFechaPromociones() {
        return fechaPromociones;
    }

    public void setFechaPromociones(String fechaPromociones) {
        this.fechaPromociones = fechaPromociones;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public float getLatitudPedido() {
        return latitudPedido;
    }

    public void setLatitudPedido(float latitudPedido) {
        this.latitudPedido = latitudPedido;
    }

    public float getLongitudPedido() {
        return longitudPedido;
    }

    public void setLongitudPedido(float longitudPedido) {
        this.longitudPedido = longitudPedido;
    }


    public  visita()
    {

    }

    int idvisita;
    String cliente;
    String fechaInicio;
    String fechaCobranza;
    String fechaPromociones;
    String fechaVenta;
    float latitud;
    float longitud;
    float latitudPedido;
    float longitudPedido;

    public String getFechaPostVenta() {
        return fechaPostVenta;
    }

    public void setFechaPostVenta(String fechaPostVenta) {
        this.fechaPostVenta = fechaPostVenta;
    }

    public float getLatitudPostVenta() {
        return latitudPostVenta;
    }

    public void setLatitudPostVenta(float latitudPostVenta) {
        this.latitudPostVenta = latitudPostVenta;
    }

    public float getLongitudPostVenta() {
        return longitudPostVenta;
    }

    public void setLongitudPostVenta(float longitudPostVenta) {
        this.longitudPostVenta = longitudPostVenta;
    }

    String fechaPostVenta;
    float latitudPostVenta;
    float longitudPostVenta;

    public float getLatitudPromociones() {
        return latitudPromociones;
    }

    public void setLatitudPromociones(float latitudPromociones) {
        this.latitudPromociones = latitudPromociones;
    }

    public float getLongitudPromociones() {
        return longitudPromociones;
    }

    public void setLongitudPromociones(float longitudPromociones) {
        this.longitudPromociones = longitudPromociones;
    }

    float  latitudPromociones;
    float longitudPromociones;

    public float getLatitudCobranza() {
        return latitudCobranza;
    }

    public void setLatitudCobranza(float latitudCobranza) {
        this.latitudCobranza = latitudCobranza;
    }

    public float getLongitudCobranza() {
        return longitudCobranza;
    }

    public void setLongitudCobranza(float longitudCobranza) {
        this.longitudCobranza = longitudCobranza;
    }

    float latitudCobranza;
    float longitudCobranza;

}
