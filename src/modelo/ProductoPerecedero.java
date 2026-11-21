package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductoPerecedero extends Producto {

    private LocalDate fechaCaducidad;
    private int diasAlerta;

    public ProductoPerecedero(String codigo, String nombre, String categoria, int cantidad,
                              int stockMinimo, double precioCosto, double precioVenta,
                              LocalDate fechaIngreso, String proveedor,
                              LocalDate fechaCaducidad, int diasAlerta) {
        super(codigo, nombre, categoria, cantidad, stockMinimo, precioCosto, precioVenta, fechaIngreso, proveedor);
        this.fechaCaducidad = fechaCaducidad;
        this.diasAlerta = diasAlerta;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public int getDiasAlerta() {
        return diasAlerta;
    }

    public long diasParaCaducar() {
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaCaducidad);
    }

    public boolean estaCaducado() {
        return fechaCaducidad.isBefore(LocalDate.now());
    }

    public boolean estaPorCaducar() {
        long dias = diasParaCaducar();
        return !estaCaducado() && dias <= diasAlerta;
    }

    @Override
    public String generarAlerta() {
        StringBuilder sb = new StringBuilder();

        if (estaCaducado()) {
            sb.append("[").append(getCodigo()).append("] ").append(getNombre())
                    .append(" - CADUCADO (caduco el ").append(fechaCaducidad).append(")");
        } else if (estaPorCaducar()) {
            long dias = diasParaCaducar();
            sb.append("[").append(getCodigo()).append("] ").append(getNombre())
                    .append(" - POR CADUCAR (caduca en ").append(dias).append(" dias)");
        }

        if (tieneStockBajo()) {
            if (sb.length() > 0) {
                sb.append(" | ");
            }
            sb.append("[").append(getCodigo()).append("] ").append(getNombre())
                    .append(" - STOCK BAJO (").append(getCantidad()).append(" unidades, minimo ")
                    .append(getStockMinimo()).append(")");
        }

        return sb.toString();
    }

    @Override
    public String getTipo() {
        return "Perecedero";
    }

    @Override
    public String toString() {
        return super.toString() + " | Caduca: " + fechaCaducidad + " (Alerta: " + diasAlerta + " dias)";
    }
}