package mx.indar.appvtas2.dbClases;

public class cxcCliente {

    public String getMov() {
        return Mov;
    }

    public void setMov(String mov) {
        Mov = mov;
    }

    public String getMovID() {
        return MovID;
    }

    public void setMovID(String movID) {
        MovID = movID;
    }

    public String getFechaEmision() {
        return FechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        FechaEmision = fechaEmision;
    }

    public String getVencimiento() {
        return Vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        Vencimiento = vencimiento;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String referencia) {
        Referencia = referencia;
    }

    public Integer getDiasMoratorios() {
        return DiasMoratorios;
    }

    public void setDiasMoratorios(Integer diasMoratorios) {
        DiasMoratorios = diasMoratorios;
    }

    public float getSaldo() {
        return Saldo;
    }

    public void setSaldo(float saldo) {
        Saldo = saldo;
    }

    String Mov;
    String MovID;
    String FechaEmision;
    String Vencimiento;
    String Referencia;
    String cliente;
    Integer DiasMoratorios;
    float Saldo;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    boolean check;

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }


}
