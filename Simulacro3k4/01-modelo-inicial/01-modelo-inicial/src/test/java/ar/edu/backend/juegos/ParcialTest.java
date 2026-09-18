package ar.edu.backend.juegos;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParcialTest {

    @Test
    public void testCreacionJuegoValido() {
        JuegoMesa juego = new JuegoMesa("J001", "Gloomhaven", 2017, 1, 4, 120, 8.7, 3.8, "Estrategia");
        assertEquals("Gloomhaven", juego.getNombre());
        assertEquals(33.06, juego.getPuntuacionAjustada(), 0.01);
    }

    @Test
    public void testInvarianteRatingInvalido() {
        assertThrows(IllegalArgumentException.class, () -> 
            new JuegoMesa("J002", "Test", 2020, 1, 4, 60, 11.0, 2.0, "Familiar")
        );
    }

    @Test
    public void testServicioCalculos() {
        JuegoMesa j1 = new JuegoMesa("J001", "A", 2020, 1, 4, 60, 8.0, 2.0, "Cat1");
        JuegoMesa j2 = new JuegoMesa("J002", "B", 2021, 2, 5, 90, 6.0, 4.0, "Cat1");
        
        ServicioJuegos servicio = new ServicioJuegos(List.of(j1, j2));
        
        assertEquals(7.0, servicio.calcularPromedioRating());
        assertEquals("B", servicio.obtenerMasComplejo().get().getNombre());
    }
}
