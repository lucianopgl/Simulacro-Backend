package ar.edu.backend;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        Path archivo = Path.of(args.length == 0 ? "datos/datos.csv" : args[0]);
        ResultadoProceso resultado = new ParserSolicitudes().procesar(archivo);
        CarteraCreditos cartera = new CarteraCreditos(resultado.getCreditos());

        System.out.println(resultado.informe());
        resultado.getDescartes().forEach(System.out::println);
        resultado.getErrores().forEach(System.out::println);

        System.out.println(cartera.informe());
    }
}
