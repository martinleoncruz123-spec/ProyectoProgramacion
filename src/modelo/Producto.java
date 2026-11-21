package modelo;

import java.time.LocalDate;
import java.util.Locale;

public abstract class Producto {

    private String codigo;
    private String nombre;
    private String categoria;
    private int cantidad;
    private int stockMinimo;
    private double precioCosto;
    private double precioVenta;
    private LocalDate fechaIngreso;
    private String proveedor;

    public Producto(String codigo, String nombre, String categoria, int cantidad,
                    int stockMinimo, double precioCosto, double precioVenta,
                    LocalDate fechaIngreso, String proveedor) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.stockMinimo = stockMinimo;
        this.precioCosto = precioCosto;
        this.precioVenta = precioVenta;
        if (fechaIngreso == null) {
            this.fechaIngreso = LocalDate.now();
        } else {
            this.fechaIngreso = fechaIngreso;
        }
        this.proveedor = proveedor;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad >= 0) {
            this.cantidad = cantidad;
        }
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        if (stockMinimo >= 0) {
            this.stockMinimo = stockMinimo;
        }
    }

    public double getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(double precioCosto) {
        if (precioCosto > 0) {
            this.precioCosto = precioCosto;
        }
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        if (precioVenta > 0) {
            this.precioVenta = precioVenta;
        }
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public String getProveedor() {
        return proveedor;
    }

    public double calcularValorInventario() {
        return cantidad * precioCosto;
    }

    public double calcularMargen() {
        return precioVenta - precioCosto;
    }

    public boolean tieneStockBajo() {
        return cantidad <= stockMinimo;
    }

    public abstract String generarAlerta();

    public abstract String getTipo();

    @Override
    public String toString() {
        return String.format(
                Locale.US,
                "[%s] %s (%s) | Tipo: %s | Stock: %d (Min: %d) | Costo: USD %.2f | Venta: USD %.2f | Ingreso: %s | Prov: %s",
                codigo, nombre, categoria, getTipo(),
