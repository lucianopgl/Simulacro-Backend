package ar.edu.backend.juegos;


import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String ruta = "datos/datos.csv";
        if (args.length > 0) {
            ruta = args[0];
        }

        System.out.println("Iniciando procesamiento de juegos desde: " + ruta);

        ParserJuegos parser = new ParserJuegos();
        ResultadoParseo resultado = parser.parsear(ruta);

        System.out.println(resultado);

        ServicioJuegos servicio = new ServicioJuegos(resultado.getJuegos());

        System.out.println(servicio.panorama());
        System.out.printf("Promedio de Rating: %.2f%n", servicio.calcularPromedioRating());
        servicio.obtenerMasComplejo().ifPresent(j -> 
            System.out.println("Juego más complejo: " + j.getNombre() + " (Peso: " + j.getPeso() + ")")
        );
    }
}
