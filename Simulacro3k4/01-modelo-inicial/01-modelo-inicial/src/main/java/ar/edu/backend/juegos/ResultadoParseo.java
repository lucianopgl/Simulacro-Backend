package ar.edu.backend.juegos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultadoParseo {
    private final List<JuegoMesa> juegos;
    private final int leidas;
    private final int procesadas;
    private final int descartadas;
    private final int invalidas;

    public ResultadoParseo(List<JuegoMesa> juegos, int leidas, int procesadas, int descartadas, int invalidas) {
        this.juegos = new ArrayList<>(juegos);
        this.leidas = leidas;
        this.procesadas = procesadas;
        this.descartadas = descartadas;
        this.invalidas = invalidas;
    }

    public List<JuegoMesa> getJuegos() {
        return Collections.unmodifiableList(juegos);
    }

    public int getLeidas() { return leidas; }
    public int getProcesadas() { return procesadas; }
    public int getDescartadas() { return descartadas; }
    public int getInvalidas() { return invalidas; }

    @Override
    public String toString() {
        return String.format("Resumen: Leídas: %d, Procesadas: %d, Descartadas: %d, Inválidas: %d, Total Juegos: %d",
                leidas, procesadas, descartadas, invalidas, juegos.size());
    }
}
