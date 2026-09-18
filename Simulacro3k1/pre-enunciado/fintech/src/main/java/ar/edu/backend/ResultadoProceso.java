package ar.edu.backend;

import java.util.List;

public class ResultadoProceso {
    private final List<SolicitudCredito> creditos;
    private final int leidas;
    private final int descartadas;
    private final List<String> errores;
    private final List<String> descartes;

    public ResultadoProceso(List<SolicitudCredito> creditos, int leidas, int descartadas, List<String> errores,
            List<String> descartes) {
        this.creditos = List.copyOf(creditos);
        this.leidas = leidas;
        this.descartadas = descartadas;
        this.errores = List.copyOf(errores);
        this.descartes = List.copyOf(descartes);
    }

    public List<SolicitudCredito> getCreditos() {
        return creditos;
    }

    public int getLeidas() {
        return leidas;
    }

    public int getDescartadas() {
        return descartadas;
    }

    public List<String> getErrores() {
        return errores;
    }

    public List<String> getDescartes() {
        return descartes;
    }

    public int getProcesadas() {
        return creditos.size();
    }

    public int getInvalidas() {
        return errores.size();
    }

    public String informe() {
        return "Leídas: %d | procesadas: %d | descartadas: %d | inválidas: %d | objetos: %d"
                .formatted(leidas, getProcesadas(), descartadas, getInvalidas(), creditos.size());
    }

}
