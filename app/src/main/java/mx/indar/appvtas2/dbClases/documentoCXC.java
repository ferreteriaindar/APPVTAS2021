package mx.indar.appvtas2.dbClases;

public class documentoCXC {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMov() {
        return mov;
    }

    public void setMov(String mov) {
        this.mov = mov;
    }

    public String getMovid() {
        return movid;
    }

    public void setMovid(String movid) {
        this.movid = movid;
    }

    public String getFechaemision() {
        return fechaemision;
    }

    public void setFechaemision(String fechaemision) {
        this.fechaemision = fechaemision;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    int id;

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    int dias;
    String mov;
    String movid;
    String fechaemision;
    String cliente;
    String usuario;

    public String getAplicaDescto() {
        return aplicaDescto;
    }

    public void setAplicaDescto(String aplicaDescto) {
        this.aplicaDescto = aplicaDescto;
    }

    String aplicaDescto;
    float importe;

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    float descuento;

    public float getImportefacturaFinal() {
        return importefacturaFinal;
    }

    public void setImportefacturaFinal(float importefacturaFinal) {
        this.importefacturaFinal = importefacturaFinal;
    }

    float importefacturaFinal;


}
