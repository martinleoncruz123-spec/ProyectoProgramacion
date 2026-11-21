package modelo;

import java.time.LocalDate;

public class ProductoNoPerecedero extends Producto {

    private String ubicacionBodega;

    public ProductoNoPerecedero(String codigo, String nombre, String categoria, int cantidad,
                                int stockMinimo, double precioCosto, double precioVenta,
                                LocalDate fechaIngreso, String proveedor, String ubicacionBodega) {
        super(codigo, nombre, categoria, cantidad, stockMinimo, precioCosto, precioVenta, fechaIngreso, proveedor);
        this.ubicacionBodega = ubicacionBodega;
    }

    public String getUbicacionBodega() {
        return ubicacionBodega;
    }

    @Override
    public String generarAlerta() {
        if (tieneStockBajo()) {
            return "[" + getCodigo() + "] " + getNombre() + " - STOCK BAJO ("
                    + getCantidad() + " unidades, minimo " + getStockMinimo() + ")";
        }
        return "";
    }

    @Override
    public String getTipo() {
        return "No perecedero";
    }

    @Override
    public String toString() {
        return super.toString() + " | Bodega: " + ubicacionBodega;
    }
}