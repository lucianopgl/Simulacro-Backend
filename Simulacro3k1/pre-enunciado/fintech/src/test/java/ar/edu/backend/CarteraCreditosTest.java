package ar.edu.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class CarteraCreditosTest {
    @Test
    void totalesYFiltro() {
        var lista = List.of(new SolicitudCredito("A", "PR", 12.40, "A"),
                new SolicitudCredito("B", "R", 25.20, "A"),
                new SolicitudCredito("C", "PR", 75.8, "A"));

        CarteraCreditos c = new CarteraCreditos(lista);
        assertEquals(89.76, redondear(c.tasaInteresPromedio()));
        assertTrue(c.filtrar(sc -> sc.getTipoCliente().equals("B")).isEmpty());

        var mapeo = c.maximaTasaInteresPorTipo().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> redondear(e.getValue()),
                        (newKey, newValue) -> newKey,
                        LinkedHashMap::new));

        assertEquals(mapeo, Map.of("R", 99.84, "PR", 110.08));

    }

    @Test
    void vacio() {
        var c = new CarteraCreditos(List.of());
        assertEquals(0, c.tasaInteresPromedio());
        assertTrue(c.maximaTasaInteresPorTipo().isEmpty());
        assertTrue(c.filtrar(e -> true).isEmpty());
    }

    @Test
    void copiaDefensiva() {
        var lista = new ArrayList<SolicitudCredito>();
        lista.add(new SolicitudCredito("A", "PR", 10, "A"));
        var c = new CarteraCreditos(lista);
        lista.clear();
        assertEquals(112.0, c.tasaInteresPromedio());
        assertThrows(UnsupportedOperationException.class, () -> c.filtrar(e -> true).clear());
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
