package ar.edu.utnfrc.backend.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {

    private final String titulo;
    private final List<ItemMenu> opciones = new ArrayList<>();

    public Menu(String titulo) {
        this.titulo = titulo;
    }

    // API simple: índice, mensaje y Runnable (lambda / method ref)
    public void addOpcion(int indice, String mensaje, Runnable accion) {
        opciones.add(new ItemMenu(indice, mensaje, accion));
    }

    private int leerEntero(Scanner in) {
        System.out.print("> Opción: ");
        while (!in.hasNextInt()) {
            in.next(); // descarta
            System.out.print("> Opción: ");
        }
        int n = in.nextInt();
        in.nextLine(); // limpia salto de línea
        return n;
    }

    public void ejecutar() {
        try (Scanner lector = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n" + titulo);
                System.out.println("======================================");
                opciones.forEach(o -> System.out.printf("%2d) %s%n", o.indice(), o.mensaje()));
                System.out.println(" 0) Salir");

                int opcion = leerEntero(lector);
                if (opcion == 0) break;

                Optional<ItemMenu> elegido = opciones.stream()
                        .filter(o -> o.indice() == opcion)
                        .findFirst();

                if (elegido.isPresent()) {
                    try {
                        elegido.get().accion().run();
                    } catch (Exception e) {
                        System.out.println("⚠️ Error al ejecutar la opción: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("❌ Opción inválida");
                }
            }
        }
        System.out.println("Aplicación terminada.");
    }

    // Record privado y simple
    private record ItemMenu(int indice, String mensaje, Runnable accion) { }
}
