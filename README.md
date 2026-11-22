# Sistema de Gestión de Inventario — Minimarket "La Esquina"

Aplicación de consola en Java desarrollada como proyecto académico para administrar el inventario de productos perecederos y no perecederos del minimarket **"La Esquina"**.

## Arquitectura del proyecto

El código está organizado en tres paquetes bajo `src/`:

* `modelo`: Contiene la clase abstracta `Producto` y las subclases `ProductoPerecedero` y `ProductoNoPerecedero`.
* `negocio`: Contiene la lógica del inventario (`Inventario`), validaciones (`ValidadorProducto`) y excepciones personalizadas (`ProductoInvalidoException`, `ProductoNoEncontradoException`, `StockInsuficienteException`).
* `interfaz`: Contiene la interacción por consola (`MenuConsola`) y el punto de entrada de la aplicación (`Main`).

## Requisitos y ejecución

* **Java**: JDK 17 o superior (solo librería estándar de Java).
* **IDE recomendado**: IntelliJ IDEA.

Para ejecutar el sistema:

1. Abrir el proyecto en IntelliJ IDEA.
2. Ejecutar `interfaz.Main` (`public static void main(String[] args)`).
3. Utilizar el menú interactivo en consola para registrar productos, listar, buscar, gestionar entradas/salidas de stock, consultar alertas y ver el valor total del inventario.