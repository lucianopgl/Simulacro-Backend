package ar.edu.backend.juegos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParserJuegos {
    private static final String SEPARADOR = ";";
    private static final String CABECERA_V1 = "id;nombre;anio;min_jugadores;max_jugadores;duracion;rating;peso;categoria";
    private static final String CABECERA_V2 = "id;nombre;anio;min_jugadores;max_jugadores;duracion;rating;peso;categoria;dificultad_cooperativa";

    public ResultadoParseo parsear(String rutaArchivo) {
        List<JuegoMesa> juegos = new ArrayList<>();
        int leidas = 0;
        int procesadas = 0;
        int descartadas = 0;
        int invalidas = 0;

        // Try-with-resources: Estructura introducida en Java 7 que asegura el cierre automático del recurso (Scanner), evitando filtraciones de memoria incluso si ocurre una excepción[cite: 5].
        try (Scanner scanner = new Scanner(new File(rutaArchivo))) {
            if (scanner.hasNextLine()) {
                String lineaCabecera = scanner.nextLine().trim();

                // Soporta compatibilidad hacia atrás: valida tanto la estructura vieja como la nueva.
                if (!CABECERA_V1.equals(lineaCabecera) && !CABECERA_V2.equals(lineaCabecera)) {
                    throw new IllegalArgumentException("Cabecera incorrecta");
                }
            }

            while (scanner.hasNextLine()) {
                leidas++;
                String linea = scanner.nextLine();
                if (linea.isBlank()) continue;

                // El bloque try interno captura excepciones específicas por fila, permitiendo que el ciclo while continúe procesando el resto del archivo sin detener la aplicación[cite: 5].
                try {
                    // El -1 es importante para que los delimitadores finales consecutivos formen campos vacíos
                    String[] campos = linea.split(SEPARADOR, -1);
                    if (campos.length != 9 && campos.length != 10) {
                        // Lanzamiento explícito de excepción si se rompe la invariante de formato[cite: 5].
                        throw new IllegalArgumentException("Cantidad de columnas incorrecta");
                    }

                    // Delega la responsabilidad de instanciar al Factory Method.
                    JuegoMesa juego = JuegoMesa.desdeCampos(campos);

                    if (juego.getRating() < 1.0) {
                        descartadas++;
                    } else {
                        juegos.add(juego);
                        procesadas++;
                    }
                    // Captura múltiple: atrapa tanto si falta un índice (OutOfBounds) como si la validación interna del objeto falla (IllegalArgument)[cite: 5].
                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                    invalidas++;
                }
            }
            // Excepción de I/O tratada en un catch superior[cite: 5].
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Archivo no encontrado: " + rutaArchivo);
        }

        return new ResultadoParseo(juegos, leidas, procesadas, descartadas, invalidas);
    }
}