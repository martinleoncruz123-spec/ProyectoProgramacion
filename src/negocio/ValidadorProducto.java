package negocio;

import java.time.LocalDate;

public class ValidadorProducto {

    public static void validarTextoObligatorio(String valor, String nombreCampo) throws ProductoInvalidoException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ProductoInvalidoException("Error: el campo '" + nombreCampo + "' es obligatorio.");
        }
    }

    public static void validarCantidad(int cantidad) throws ProductoInvalidoException {
        if (cantidad < 0) {
            throw new ProductoInvalidoException("Error: la cantidad debe ser un numero entero mayor o igual a 0.");
        }
    }

    public static void validarStockMinimo(int stockMinimo) throws ProductoInvalidoException {
        if (stockMinimo < 0) {
            throw new ProductoInvalidoException("Error: el stock minimo debe ser un numero entero mayor o igual a 0.");
        }
    }

    public static void validarPrecio(double precio) throws ProductoInvalidoException {
        if (precio <= 0) {
            throw new ProductoInvalidoException("Error: el precio debe ser un numero mayor que 0.");
        }
    }

    public static void validarFechaCaducidad(LocalDate fechaCaducidad) throws ProductoInvalidoException {
        if (fechaCaducidad == null) {
            throw new ProductoInvalidoException("Error: la fecha debe tener el formato yyyy-MM-dd.");
        }
        if (fechaCaducidad.isBefore(LocalDate.now())) {
            throw new ProductoInvalidoException("Error: la fecha de caducidad no puede ser anterior a hoy.");
        }
    }

    public static void validarUnidadesMovimiento(int unidades) throws ProductoInvalidoException {
        if (unidades <= 0) {
            throw new ProductoInvalidoException("Error: la cantidad debe ser un numero entero mayor o igual a 0.");
        }
    }
}