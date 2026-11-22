package interfaz;

import modelo.Producto;
import modelo.ProductoNoPerecedero;
import modelo.ProductoPerecedero;
import negocio.Inventario;
import negocio.ProductoInvalidoException;
import negocio.ProductoNoEncontradoException;
import negocio.StockInsuficienteException;
import negocio.ValidadorProducto;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class MenuConsola {

    private Inventario inventario;
    private Scanner scanner;

    public MenuConsola() {
        this.inventario = new Inventario();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        boolean ejecutando = true;

        while (ejecutando) {
            mostrarMenu();
            String opcion = leerTexto("Seleccione una opcion: ");

            switch (opcion) {
                case "1":
                    registrarProductoMenu();
                    break;
                case "2":
                    listarProductosMenu();
                    break;
                case "3":
                    buscarProductoMenu();
                    break;
                case "4":
                    entradaStockMenu();
                    break;
                case "5":
                    salidaStockMenu();
                    break;
                case "6":
                    verAlertasMenu();
                    break;
                case "7":
                    verValorTotalMenu();
                    break;
                case "8":
                    ejecutando = false;
                    System.out.println("Saliendo del sistema. Hasta pronto.");
                    break;
                default:
                    System.out.println("Error: opcion no valida. Intente nuevamente.");
                    break;
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n=== MINIMARKET \"LA ESQUINA\" - GESTION DE INVENTARIO ===");
        System.out.println("1. Registrar producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Buscar producto");
        System.out.println("4. Registrar entrada de stock");
        System.out.println("5. Registrar salida de stock");
        System.out.println("6. Ver alertas");
        System.out.println("7. Ver valor total del inventario");
        System.out.println("8. Salir");
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private String leerTextoObligatorio(String mensaje, String nombreCampo) throws ProductoInvalidoException {
        String valor = leerTexto(mensaje);
        ValidadorProducto.validarTextoObligatorio(valor, nombreCampo);
        return valor;
    }

    private int leerEntero(String mensaje, String nombreCampo, String mensajeInvalido)
            throws ProductoInvalidoException {
        String entrada = leerTexto(mensaje);
        if (entrada.isEmpty()) {
            throw new ProductoInvalidoException("Error: el campo '" + nombreCampo + "' es obligatorio.");
        }
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            throw new ProductoInvalidoException(mensajeInvalido);
        }
    }

    private double leerPrecio(String mensaje) throws ProductoInvalidoException {
        String entrada = leerTexto(mensaje);
        if (entrada.isEmpty()) {
            throw new ProductoInvalidoException("Error: el precio debe ser un numero mayor que 0.");
        }
        try {
            return Double.parseDouble(entrada);
        } catch (NumberFormatException e) {
            throw new ProductoInvalidoException("Error: el precio debe ser un numero mayor que 0.");
        }
    }

    private LocalDate leerFecha(String mensaje) throws ProductoInvalidoException {
        String entrada = leerTexto(mensaje);
        if (entrada.isEmpty()) {
            throw new ProductoInvalidoException("Error: la fecha debe tener el formato yyyy-MM-dd.");
        }
        try {
            return LocalDate.parse(entrada);
        } catch (DateTimeParseException e) {
            throw new ProductoInvalidoException("Error: la fecha debe tener el formato yyyy-MM-dd.");
        }
    }

    private void registrarProductoMenu() {
        try {
            System.out.println("Tipo de producto: 1 = Perecedero | 2 = No perecedero");
            String tipo = leerTexto("Seleccione el tipo (1 o 2): ");
            if (!tipo.equals("1") && !tipo.equals("2")) {
                System.out.println("Error: seleccione 1 para perecedero o 2 para no perecedero.");
                return;
            }

            String codigo = leerTextoObligatorio("Codigo: ", "codigo");
            String nombre = leerTextoObligatorio("Nombre: ", "nombre");
            String categoria = leerTexto("Categoria: ");
            int cantidad = leerEntero(
                    "Cantidad: ",
                    "cantidad",
                    "Error: la cantidad debe ser un numero entero mayor o igual a 0."
            );
            int stockMinimo = leerEntero(
                    "Stock minimo: ",
                    "stockMinimo",
                    "Error: el stock minimo debe ser un numero entero mayor o igual a 0."
            );
            double precioCosto = leerPrecio("Precio de costo: ");
            double precioVenta = leerPrecio("Precio de venta: ");
            String proveedor = leerTexto("Proveedor: ");

            Producto nuevoProducto;
            if (tipo.equals("1")) {
                LocalDate fechaCaducidad = leerFecha("Fecha de caducidad (yyyy-MM-dd): ");
                int diasAlerta = leerEntero(
                        "Dias de alerta: ",
                        "diasAlerta",
                        "Error: los dias de alerta deben ser un numero entero mayor o igual a 0."
                );
                nuevoProducto = new ProductoPerecedero(
                        codigo, nombre, categoria, cantidad, stockMinimo,
                        precioCosto, precioVenta, LocalDate.now(), proveedor,
                        fechaCaducidad, diasAlerta
                );
            } else {
                String ubicacionBodega = leerTexto("Ubicacion en bodega: ");
                nuevoProducto = new ProductoNoPerecedero(
                        codigo, nombre, categoria, cantidad, stockMinimo,
                        precioCosto, precioVenta, LocalDate.now(), proveedor,
                        ubicacionBodega
                );
            }

            inventario.registrarProducto(nuevoProducto);
            System.out.println("Producto registrado correctamente.");
        } catch (ProductoInvalidoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listarProductosMenu() {
        List<Producto> lista = inventario.listarProductos();
        if (lista.isEmpty()) {
            System.out.println("No hay productos registrados en el inventario.");
            return;
        }
        System.out.println("\n--- LISTADO DE PRODUCTOS ---");
        for (Producto p : lista) {
            System.out.println(p.toString());
        }
    }

    private void buscarProductoMenu() {
        System.out.println("Buscar por: 1 = Codigo | 2 = Nombre");
        String criterio = leerTexto("Seleccione criterio (1 o 2): ");

        if (criterio.equals("1")) {
            String codigo = leerTexto("Ingrese el codigo: ");
            try {
                Producto encontrado = inventario.buscarPorCodigo(codigo);
                System.out.println(encontrado.toString());
            } catch (ProductoNoEncontradoException e) {
                System.out.println(e.getMessage());
            }
        } else if (criterio.equals("2")) {
            String texto = leerTexto("Ingrese el nombre o parte del nombre: ");
            List<Producto> encontrados = inventario.buscarPorNombre(texto);
            if (encontrados.isEmpty()) {
                System.out.println("Error: producto no encontrado.");
            } else {
                for (Producto p : encontrados) {
                    System.out.println(p.toString());
                }
            }
        } else {
            System.out.println("Error: criterio de busqueda no valido.");
        }
    }

    private void entradaStockMenu() {
        try {
            String codigo = leerTexto("Codigo del producto: ");
            int unidades = leerEntero(
                    "Unidades a ingresar: ",
                    "cantidad",
                    "Error: la cantidad debe ser un numero entero mayor o igual a 0."
            );
            inventario.entradaStock(codigo, unidades);
            Producto actualizado = inventario.buscarPorCodigo(codigo);
            System.out.println(
                    "Entrada registrada. Nueva cantidad de " + actualizado.getCodigo()
                            + ": " + actualizado.getCantidad() + "."
            );
        } catch (ProductoNoEncontradoException | ProductoInvalidoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void salidaStockMenu() {
        try {
            String codigo = leerTexto("Codigo del producto: ");
            int unidades = leerEntero(
                    "Unidades a retirar: ",
                    "cantidad",
                    "Error: la cantidad debe ser un numero entero mayor o igual a 0."
            );
            inventario.salidaStock(codigo, unidades);
            Producto actualizado = inventario.buscarPorCodigo(codigo);
            System.out.println(
                    "Salida registrada. Nueva cantidad de " + actualizado.getCodigo()
                            + ": " + actualizado.getCantidad() + "."
            );
        } catch (ProductoNoEncontradoException | ProductoInvalidoException | StockInsuficienteException e) {
            System.out.println(e.getMessage());
        }
    }

    private void verAlertasMenu() {
        List<String> alertasStock = inventario.obtenerAlertasStockBajo();
        List<String> alertasCaducidad = inventario.obtenerAlertasCaducidad();

        if (alertasStock.isEmpty() && alertasCaducidad.isEmpty()) {
            System.out.println("No hay alertas por el momento.");
            return;
        }

        if (!alertasStock.isEmpty()) {
            System.out.println("\n--- ALERTAS DE STOCK BAJO ---");
            for (String alerta : alertasStock) {
                System.out.println(alerta);
            }
        }

        if (!alertasCaducidad.isEmpty()) {
            System.out.println("\n--- ALERTAS DE CADUCIDAD ---");
            for (String alerta : alertasCaducidad) {
                System.out.println(alerta);
            }
        }
    }

    private void verValorTotalMenu() {
        double total = inventario.calcularValorTotal();
        System.out.println(String.format(Locale.US, "Valor total del inventario: USD %.2f", total));
    }
}