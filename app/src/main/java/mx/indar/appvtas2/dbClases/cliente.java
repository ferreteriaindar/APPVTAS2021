package mx.indar.appvtas2.dbClases;

public class cliente {


    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public  cliente(String cte,String nombreCliente,String rfc,String zona,String direccion,float coordenadax,float coordenaday,String calle,String poblacion,int dia)
    {
        cliente=cte;
        this.nombreCliente=nombreCliente;
        this.rfc=rfc;
        this.zona=zona;
        this.direccion=direccion;
        this.coordenadax=coordenadax;
        this.coordenaday=coordenaday;
        this.calle=calle;
        this.poblacion=poblacion;
        this.dia=dia;
    }

    public  cliente()
    {

    }

    int idCliente;
    String cliente;
    String nombreCliente;
    String rfc;
    String zona;
    String direccion;
    float coordenadax,coordenaday;
    String calle,poblacion;
    int dia;

    public int getMetrosTolerancia() {
        return metrosTolerancia;
    }

    public void setMetrosTolerancia(int metrosTolerancia) {
        this.metrosTolerancia = metrosTolerancia;
    }

    int metrosTolerancia;

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public float getCoordenadax() {
        return coordenadax;
    }

    public void setCoordenadax(float coordenadax) {
        this.coordenadax = coordenadax;
    }

    public float getCoordenaday() {
        return coordenaday;
    }

    public void setCoordenaday(float coordenaday) {
        this.coordenaday = coordenaday;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }




}
