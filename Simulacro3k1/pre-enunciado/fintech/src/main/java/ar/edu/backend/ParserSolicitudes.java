package ar.edu.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserSolicitudes {
    private static final String CABECERA = "idCliente,tipoCliente,ingresosMensuales,totalDeudasActuales,estadoSolicitud";

    public ResultadoProceso procesar(Path archivo) throws IOException {

        ArrayList<SolicitudCredito> creditos = new ArrayList<>();
        ArrayList<String> errores = new ArrayList<>();
        ArrayList<String> descartes = new ArrayList<>();
        int leidas = 0;

        try (BufferedReader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            if (!CABECERA.equals(lector.readLine())) {
                throw new IllegalArgumentException("encabezado incorrecto");
            }
            String linea;
            while ((linea = lector.readLine()) != null) {
                leidas++;
                int numero = leidas + 1;
                try {
                    String[] tokens = Arrays.stream(linea.split(",", -1)).map(String::strip).toArray(String[]::new);
                    if (tokens.length != 5)
                        throw new IllegalArgumentException("Cantidad de columnas incorrectas");
                    if (tokens[1].equalsIgnoreCase("black")) {
                        descartes.add("Linea %d: BLACK".formatted(numero));
                        continue;
                    }

                    if (!List.of("REGULAR", "PREMIUM").contains(tokens[1]))
                        throw new IllegalArgumentException("estado desconocido: " + tokens[1]);

                    creditos.add(SolicitudCredito.desdeCampos(tokens));

                } catch (IllegalArgumentException ex) {
                    errores.add("Linea %d: %s".formatted(numero, ex.getMessage()));
                }
            }
        }
        return new ResultadoProceso(creditos, leidas, descartes.size(), errores, descartes);

    }
}
