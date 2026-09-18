package ar.edu.backend.juegos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Estos tests verifican la evolución solicitada en el parcial:
 * 1. Incorporación de Juegos Cooperativos.
 * 2. Cálculo de puntuación ajustada polimórfica.
 * 3. Parser actualizado para manejar la columna opcional de dificultad.
 * 4. Servicio con panorama y conteos por tipo.
 */
public class EvolucionParcialTest {

    @TempDir
    Path tempDir;

    private ResultadoParseo leer(String contenido, String cabecera) throws IOException {
        Path csvFile = tempDir.resolve("test.csv");
        Files.writeString(csvFile, cabecera + "\n" + contenido);
        return new ParserJuegos().parsear(csvFile.toString());
    }

    private ResultadoParseo leer(String contenido) throws IOException {
        return leer(contenido, "id;nombre;anio;min_jugadores;max_jugadores;duracion;rating;peso;categoria;dificultad_cooperativa");
    }

    @Test
    public void testJuegoCooperativoPuntuacion() {
        // Un juego normal: rating 8.0, peso 2.5 -> puntuación 20.0
        JuegoMesa normal = new JuegoMesa("J001", "Normal", 2020, 1, 4, 60, 8.0, 2.5, "Estrategia");
        assertEquals(20.0, normal.getPuntuacionAjustada(), 0.01);

        // Un juego cooperativo: rating 8.0, peso 2.5, dificultad 6 -> puntuación 20.0 + (6/2) = 23.0
        JuegoCooperativo coop = new JuegoCooperativo("J002", "Coop", 2021, 2, 5, 45, 8.0, 2.5, "Cooperativo", 6);
        assertEquals(23.0, coop.getPuntuacionAjustada(), 0.01);
    }

    @Test
    public void testPolimorfismoEnServicio() {
        JuegoMesa j1 = new JuegoMesa("J001", "A", 2020, 1, 4, 60, 8.0, 2.0, "Cat1"); // 16.0
        JuegoCooperativo j2 = new JuegoCooperativo("J002", "B", 2021, 2, 5, 90, 6.0, 4.0, "Cat1", 4); // 24.0 + 2.0 = 26.0
        
        ServicioJuegos servicio = new ServicioJuegos(List.of(j1, j2));
        
        // Suma total: 16.0 + 26.0 = 42.0
        assertEquals(42.0, servicio.calcularSumaPuntuacionAjustada(), 0.01);
        assertEquals(Map.of("Normal", 1L, "Cooperativo", 1L), servicio.contarJuegosPorTipo());
        assertTrue(servicio.panorama().contains("Total Puntos"));
    }

    @Test
    public void testParserEvolucionado() throws IOException {
        String contenido = 
            "J001;Gloomhaven;2017;1;4;120;8.7;3.8;Estrategia;\n" +
            "J002;Spirit Island;2017;1;4;90;8.3;4.0;Cooperativo;7\n";
        
        ResultadoParseo resultado = leer(contenido);
        
        assertEquals(2, resultado.getProcesadas());
        List<JuegoMesa> juegos = resultado.getJuegos();
        
        // El primero es normal
        assertFalse(juegos.get(0) instanceof JuegoCooperativo);
        assertEquals(33.06, juegos.get(0).getPuntuacionAjustada(), 0.01);
        
        // El segundo es cooperativo
        assertTrue(juegos.get(1) instanceof JuegoCooperativo);
        JuegoCooperativo coop = (JuegoCooperativo) juegos.get(1);
        assertEquals(7, coop.getDificultadCooperativa());
        assertEquals(36.7, coop.getPuntuacionAjustada(), 0.01);
    }

    @Test
    public void testFormatoAnteriorSigueVigente() throws IOException {
        String cabeceraVieja = "id;nombre;anio;min_jugadores;max_jugadores;duracion;rating;peso;categoria";
        String contenido = "J001;Gloomhaven;2017;1;4;120;8.7;3.8;Estrategia\n";
        
        ResultadoParseo resultado = leer(contenido, cabeceraVieja);
        assertEquals(1, resultado.getProcesadas());
        assertEquals(0, resultado.getInvalidas());
    }

    @Test
    public void testParserEstrictoColumnas() throws IOException {
        // Demasiadas columnas
        String contenido = "J001;Nombre;2020;1;4;60;8.0;2.5;Cat;Extra\n"; 
        ResultadoParseo resultado = leer(contenido, "id;nombre;anio;min_jugadores;max_jugadores;duracion;rating;peso;categoria");
        assertEquals(1, resultado.getInvalidas());
        
        // Muy pocas columnas
        contenido = "J001;Nombre;2020\n";
        resultado = leer(contenido, "id;nombre;anio;min_jugadores;max_jugadores;duracion;rating;peso;categoria");
        assertEquals(1, resultado.getInvalidas());
    }

    @Test
    public void testParserInvarianteDificultad() throws IOException {
        // Dificultad fuera de rango (1-10)
        String contenido = "J003;Error Dificultad;2020;1;4;30;7.0;2.0;Cooperativo;15\n";
        ResultadoParseo resultado = leer(contenido);
        
        assertEquals(1, resultado.getInvalidas());
        assertEquals(0, resultado.getProcesadas());
    }
}
