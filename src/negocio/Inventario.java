package negocio;

import modelo.Producto;
import modelo.ProductoPerecedero;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Inventario {

    private List<Producto> productos;

    public Inventario() {
        this.productos = new ArrayList<>();
    }

    public void registrarProducto(Producto p) throws ProductoInvalidoException {
        if (p == null) {
            throw new ProductoInvalidoException("Error: el producto no puede ser nulo.");
        }

        ValidadorProducto.validarTextoObligatorio(p.getCodigo(), "codigo");
        ValidadorProducto.validarTextoObligatorio(p.getNombre(), "nombre");
        ValidadorProducto.validarCantidad(p.getCantidad());
        ValidadorProducto.validarStockMinimo(p.getStockMinimo());
        ValidadorProducto.validarPrecio(p.getPrecioCosto());
        ValidadorProducto.validarPrecio(p.getPrecioVenta());

        if (p instanceof ProductoPerecedero) {
            ProductoPerecedero perecedero = (ProductoPerecedero) p;
            ValidadorProducto.validarFechaCaducidad(perecedero.getFechaCaducidad());
        }

        for (Producto existente : productos) {
            if (existente.getCodigo().equalsIgnoreCase(p.getCodigo().trim())) {
                throw new ProductoInvalidoException(
                        "Error: ya existe un producto con el codigo " + p.getCodigo().trim() + "."
                );
            }
        }

        productos.add(p);
    }

    public List<Producto> listarProductos() {
        return new ArrayList<>(productos);
    }

    public Producto buscarPorCodigo(String codigo) throws ProductoNoEncontradoException {
        if (codigo != null) {
            String buscado = codigo.trim();
            for (Producto p : productos) {
                if (p.getCodigo().equalsIgnoreCase(buscado)) {
                    return p;
                }
            }
        }
        throw new ProductoNoEncontradoException("Error: producto no encontrado.");
    }

    public List<Producto> buscarPorNombre(String texto) {
        List<Producto> resultados = new ArrayList<>();
        if (texto == null) {
            return resultados;
        }
        String filtro = texto.trim().toLowerCase(Locale.ROOT);
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase(Locale.ROOT).contains(filtro)) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    public void entradaStock(String codigo, int unidades)
            throws ProductoNoEncontradoException, ProductoInvalidoException {
        ValidadorProducto.validarUnidadesMovimiento(unidades);
        Producto p = buscarPorCodigo(codigo);
        p.setCantidad(p.getCantidad() + unidades);
    }

    public void salidaStock(String codigo, int unidades)
            throws ProductoNoEncontradoException, ProductoInvalidoException, StockInsuficienteException {
        ValidadorProducto.validarUnidadesMovimiento(unidades);
        Producto p = buscarPorCodigo(codigo);
        if (unidades > p.getCantidad()) {
            throw new StockInsuficienteException(
                    "Error: stock insuficiente. Disponible: " + p.getCantidad() + "."
            );
        }
        p.setCantidad(p.getCantidad() - unidades);
    }

    public List<String> obtenerAlertasStockBajo() {
        List<String> alertas = new ArrayList<>();
        for (Producto p : productos) {
            if (p.tieneStockBajo()) {
                String alerta = p.generarAlerta();
                if (!alerta.isEmpty()) {
                    String[] partes = alerta.split(" \\| ");
                    for (String parte : partes) {
                        if (parte.contains("STOCK BAJO")) {
                            alertas.add(parte);
                        }
                    }
                }
            }
        }
        return alertas;
    }

    public List<String> obtenerAlertasCaducidad() {
        List<String> alertas = new ArrayList<>();
        for (Producto p : productos) {
            if (p instanceof ProductoPerecedero) {
                ProductoPerecedero perecedero = (ProductoPerecedero) p;
                if (perecedero.estaCaducado() || perecedero.estaPorCaducar()) {
                    String alerta = p.generarAlerta();
                    if (!alerta.isEmpty()) {
                        String[] partes = alerta.split(" \\| ");
                        for (String parte : partes) {
                            if (parte.contains("CADUCADO") || parte.contains("POR CADUCAR")) {
                                alertas.add(parte);
                            }
                        }
                    }
                }
            }
        }
        return alertas;
    }

    public double calcularValorTotal() {
        double total = 0.0;
        for (Producto p : productos) {
            total += p.calcularValorInventario();
        }
        return total;
    }
}