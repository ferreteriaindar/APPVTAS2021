package mx.indar.appvtas2.dbClases.MenuClientes;

public class CobroD {


    Integer idSubirCobro;

    public CobroD() {
    }

    public Integer getIdSubirCobro() {
        return idSubirCobro;
    }

    public void setIdSubirCobro(Integer idSubirCobro) {
        this.idSubirCobro = idSubirCobro;
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

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    String mov,movid,aplicaDescto;
    float importe,descuento;

    public String getAplicaDescto() {
        return aplicaDescto;
    }

    public void setAplicaDescto(String aplicaDescto) {
        this.aplicaDescto = aplicaDescto;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public int getIdCobro() {
        return idCobro;
    }

    public void setIdCobro(int idCobro) {
        this.idCobro = idCobro;
    }

    int idCobro;
}
