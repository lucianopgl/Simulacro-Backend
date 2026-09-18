package ar.edu.backend;

public class SolicitudCredito {

    private final String idCliente;
    private final String tipoCliente;
    private final double scoreFinanciero;
    private final String estado;

    private final static double TASA_BASE = 40;

    public SolicitudCredito(String idClient, String tipoCliente, double scoreFinanciero, String estado) {

        if (idClient == null || idClient.isBlank() || tipoCliente == null || tipoCliente.isBlank())
            throw new IllegalArgumentException("El Id del Cliente como su Categorizacion (tipo) son requeridos");

        this.idCliente = idClient;
        this.tipoCliente = tipoCliente;
        this.scoreFinanciero = scoreFinanciero;
        this.estado = estado;
    }

    public static SolicitudCredito desdeCampos(String[] tokens) {
        if (tokens.length != 5)
            throw new IllegalArgumentException("Se esperan 5 columnas a procesar");

        double ingresos = Double.parseDouble(tokens[2]);
        double deudas = Double.parseDouble(tokens[3]);

        if (ingresos < 1000 || ingresos > 15000)
            throw new IllegalArgumentException("Los ingresos mensuales no son aptos para solicitar el credito");

        if (deudas < 0 || deudas >= ingresos)
            throw new IllegalArgumentException("La deudas registradas no permiten solicitar el credito");

        double scoreFinanciero = (1 - (deudas / ingresos)) * 100;
        return new SolicitudCredito(tokens[0], tokens[1], scoreFinanciero, tokens[4]);
    }

    public String getIdCliente() {
        return idCliente;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public double getScoreFinanciero() {
        return scoreFinanciero;
    }

    public String getEstado() {
        return estado;
    }

    public double getTasaInteresAnual() {
        return SolicitudCredito.TASA_BASE + (100 - this.scoreFinanciero) * 0.8;
    }

}
