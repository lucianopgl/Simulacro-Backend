package ar.edu.backend.juegos;


import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String ruta = "datos/datos-parcial.csv";
        if (args.length > 0) {
            ruta = args[0];
        }

        System.out.println("Iniciando procesamiento de juegos desde: " + ruta);

        ParserJuegos parser = new ParserJuegos();
        ResultadoParseo resultado = parser.parsear(ruta);

        System.out.println(resultado);

        ServicioJuegos servicio = new ServicioJuegos(resultado.getJuegos());

        System.out.println("Líneas leídas: " + resultado.getLeidas());
        System.out.println("Juegos procesados exitosamente: " + resultado.getProcesadas());
        System.out.println("Juegos descartados (rating < 1.0): " + resultado.getDescartadas());
        System.out.println("Filas inválidas (error de formato/reglas): " + resultado.getInvalidas());
        System.out.println("--------------------------------------------------");

        System.out.println(servicio.panorama());
    }
}
