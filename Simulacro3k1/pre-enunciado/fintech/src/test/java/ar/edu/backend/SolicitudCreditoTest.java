package ar.edu.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class SolicitudCreditoTest {

    @Test
    void construccionYCalculo() {
        SolicitudCredito sc = SolicitudCredito.desdeCampos(new String[] { "CL1", "PR", "3000", "200", "A" });
        assertEquals("CL1", sc.getIdCliente());
        assertEquals("A", sc.getEstado());
        assertEquals("PR", sc.getTipoCliente());
        assertEquals(93.33, redondear(sc.getScoreFinanciero()));
        assertEquals(45.33, redondear(sc.getTasaInteresAnual()));
    }

    @Test
    void invalidos() {
        assertThrows(IllegalArgumentException.class, () -> new SolicitudCredito("E", null, 10, "A"));
        assertThrows(IllegalArgumentException.class,
                () -> SolicitudCredito.desdeCampos(new String[] { "E", "N", "1500", "1600", "A" }));
        assertThrows(IllegalArgumentException.class,
                () -> SolicitudCredito.desdeCampos(new String[] { "E", "N", "1500", "-1", "A" }));
        assertThrows(IllegalArgumentException.class,
                () -> SolicitudCredito.desdeCampos(new String[] { "E", "N", "100", "1600", "A" }));
        assertThrows(IllegalArgumentException.class,
                () -> SolicitudCredito.desdeCampos(new String[] { "E", "N", "41500", "-1", "A" }));
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

}
