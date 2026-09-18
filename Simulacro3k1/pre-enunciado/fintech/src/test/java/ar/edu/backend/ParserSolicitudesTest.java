package ar.edu.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ParserSolicitudesTest {
    @TempDir
    Path dir;

    @Test
    void archivoProvisto() throws IOException {
        var lector = new ParserSolicitudes().procesar(Path.of("datos/datos.csv"));
        assertEquals(60, lector.getLeidas());
        assertEquals(45, lector.getProcesadas());
        assertEquals(3, lector.getDescartadas());
        assertEquals(12, lector.getInvalidas());
        assertEquals(45, lector.getCreditos().size());
        assertEquals("Leídas: 60 | procesadas: 45 | descartadas: 3 | inválidas: 12 | objetos: 45", lector.informe());
        assertTrue(lector.getErrores().get(0).startsWith("Linea 9"));
        assertEquals(3, lector.getDescartes().size());
        assertEquals(lector.getLeidas(), lector.getProcesadas() + lector.getDescartadas() + lector.getInvalidas());
    }

    @Test
    void agregadosYTestigos() throws IOException {
        var l = new ParserSolicitudes().procesar(Path.of("datos/datos.csv"));
        var c = new CarteraCreditos(l.getCreditos());
        assertTrue(l.getDescartes().contains("Linea 19: BLACK"));
        assertTrue(l.getErrores().stream().anyMatch(e -> e.startsWith("Linea 44:")));
        assertTrue(c.filtrar(sc -> sc.getIdCliente().equals("CL123")).isEmpty());

        double sf = c.filtrar(sc -> sc.getIdCliente().equals("CL306")).get(0).getScoreFinanciero();
        sf = Math.round(sf * 100.0) / 100.0;
        assertEquals(68.17, sf);
    }

    @Test
    void resultadoConservaCopiasDefensivas() {
        var envios = new java.util.ArrayList<SolicitudCredito>();
        var errores = new java.util.ArrayList<String>();
        var descartes = new java.util.ArrayList<String>();
        envios.add(new SolicitudCredito("A", "N", 1.5, "P"));
        errores.add("error");
        descartes.add("descarte");
        var r = new ResultadoProceso(envios, 3, 1, errores, descartes);
        envios.clear();
        errores.clear();
        descartes.clear();
        assertEquals(1, r.getProcesadas());
        assertEquals(1, r.getInvalidas());
        assertEquals(java.util.List.of("descarte"), r.getDescartes());
        assertThrows(UnsupportedOperationException.class, () -> r.getCreditos().clear());
        assertThrows(UnsupportedOperationException.class, () -> r.getErrores().clear());
        assertThrows(UnsupportedOperationException.class, () -> r.getDescartes().clear());
    }

    @Test
    void estructuraYContinuacion() throws IOException {
        Path p = dir.resolve("x.csv");
        Files.writeString(p,
                "idCliente,tipoCliente,ingresosMensuales,totalDeudasActuales,estadoSolicitud\n\nE,BLACK,100,10,A,extra\nE,PREMIUM,10000,20,A\n");
        var r = new ParserSolicitudes().procesar(p);
        assertEquals(3, r.getLeidas());
        assertEquals(2, r.getInvalidas());
        assertEquals(1, r.getProcesadas());
    }

    @Test
    void soloCabeceraYErroresDeArchivo() throws IOException {
        Path p = dir.resolve("x.csv");
        Files.writeString(p, "idCliente,tipoCliente,ingresosMensuales,totalDeudasActuales,estadoSolicitud\n");
        assertEquals(0, new ParserSolicitudes().procesar(p).getLeidas());
        Files.writeString(p, "mal\n");
        assertThrows(IllegalArgumentException.class, () -> new ParserSolicitudes().procesar(p));
        assertThrows(IOException.class, () -> new ParserSolicitudes().procesar(dir.resolve("ausente")));
    }
}
