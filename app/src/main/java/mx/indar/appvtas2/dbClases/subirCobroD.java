package mx.indar.appvtas2.dbClases;

public class subirCobroD {
    public subirCobroD() {
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

    Integer idSubirCobro;
    String mov,movid;
    float importe;
}
