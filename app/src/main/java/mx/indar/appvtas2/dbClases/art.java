package mx.indar.appvtas2.dbClases;

public class art {
    public String getArticulo() {
        return Articulo;
    }

    public void setArticulo(String articulo) {
        Articulo = articulo;
    }

    public String getDescripcion1() {
        return Descripcion1;
    }

    public void setDescripcion1(String descripcion1) {
        Descripcion1 = descripcion1;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public float getPrecioLista() {
        return PrecioLista;
    }

    public void setPrecioLista(float precioLista) {
        PrecioLista = precioLista;
    }

    public float getPrecio7() {
        return Precio7;
    }

    public void setPrecio7(float precio7) {
        Precio7 = precio7;
    }

    public float getPrecio8() {
        return Precio8;
    }

    public void setPrecio8(float precio8) {
        Precio8 = precio8;
    }

    public float getCantidadMinimaVenta() {
        return CantidadMinimaVenta;
    }

    public void setCantidadMinimaVenta(float cantidadMinimaVenta) {
        CantidadMinimaVenta = cantidadMinimaVenta;
    }

    public float getCantidadMaximaVenta() {
        return CantidadMaximaVenta;
    }

    public void setCantidadMaximaVenta(float cantidadMaximaVenta) {
        CantidadMaximaVenta = cantidadMaximaVenta;
    }

    public float getDisponible() {
        return disponible;
    }

    public void setDisponible(float disponible) {
        this.disponible = disponible;
    }

    public Integer getMultiplo() {
        return multiplo;
    }

    public void setMultiplo(Integer multiplo) {
        this.multiplo = multiplo;
    }

    String Articulo;

    public art(String articulo, String descripcion1, String categoria, float precioLista, float precio7, float precio8, float cantidadMinimaVenta, float cantidadMaximaVenta, float disponible, Integer multiplo) {
        Articulo = articulo;
        Descripcion1 = descripcion1;
        Categoria = categoria;
        PrecioLista = precioLista;
        Precio7 = precio7;
        Precio8 = precio8;
        CantidadMinimaVenta = cantidadMinimaVenta;
        CantidadMaximaVenta = cantidadMaximaVenta;
        this.disponible = disponible;
        this.multiplo = multiplo;
    }

    String Descripcion1;
    String Categoria;
    float PrecioLista,Precio7,Precio8,CantidadMinimaVenta,CantidadMaximaVenta,disponible;
    Integer multiplo;
}
