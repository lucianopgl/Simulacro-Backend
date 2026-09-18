package ar.edu.backend.juegos;

import java.util.Objects;

// Clase base de la jerarquía. Define el comportamiento por defecto de un juego normal.
public class JuegoMesa {
    // Principio de Ocultamiento: Los atributos se declaran privados para que no sean accesibles de forma directa desde el exterior, protegiendo la integridad del objeto[cite: 7].
    // Al ser "final", garantizamos inmutabilidad tras la construcción.
    private final String id;
    private final String nombre;
    private final int anio;
    private final int minJugadores;
    private final int maxJugadores;
    private final int duracion;
    private final double rating;
    private final double peso;
    private final String categoria;

    // Constructor: centraliza la validación de las invariantes de la clase.
    // Si alguna regla de negocio se rompe, se lanza una excepción (unchecked) impidiendo que se cree un objeto inconsistente[cite: 5].
    public JuegoMesa(String id, String nombre, int anio, int minJugadores, int maxJugadores, int duracion, double rating, double peso, String categoria) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID no puede ser vacío");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre no puede ser vacío");
        if (anio < 1900 || anio > 2026) throw new IllegalArgumentException("Año inválido");
        if (minJugadores < 1) throw new IllegalArgumentException("Mínimo de jugadores debe ser al menos 1");
        if (maxJugadores < minJugadores) throw new IllegalArgumentException("Máximo de jugadores no puede ser menor al mínimo");
        if (rating < 0 || rating > 10) throw new IllegalArgumentException("Rating debe estar entre 0 y 10");
        if (peso < 1 || peso > 5) throw new IllegalArgumentException("Peso debe estar entre 1 y 5");

        this.id = id;
        this.nombre = nombre;
        this.anio = anio;
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.duracion = duracion;
        this.rating = rating;
        this.peso = peso;
        this.categoria = categoria;
    }

    // Factory Method: Encapsula la lógica de creación.
    // Lee un arreglo de Strings y decide dinámicamente qué instancia crear (JuegoMesa o JuegoCooperativo).
    public static JuegoMesa desdeCampos(String[] campos) {
        if (campos.length < 9 || campos.length > 10) throw new IllegalArgumentException("Faltan campos");

        String id = campos[0].trim();
        String nombre = campos[1].trim();
        int anio = Integer.parseInt(campos[2].trim());
        int minJug = Integer.parseInt(campos[3].trim());
        int maxJug = Integer.parseInt(campos[4].trim());
        int duracion = Integer.parseInt(campos[5].trim());
        double rating = Double.parseDouble(campos[6].trim());
        double peso = Double.parseDouble(campos[7].trim());
        String categoria = campos[8].trim();

        // Si existen 10 campos y el último tiene contenido, sabemos que es la variante cooperativa.
        if (campos.length == 10 && !campos[9].trim().isBlank()){
            int dificultadCoop = Integer.parseInt(campos[9].trim());
            // Retorna una subclase. Esto es válido porque JuegoCooperativo "es un" JuegoMesa (Polimorfismo)[cite: 3].
            return new JuegoCooperativo(id, nombre, anio, minJug, maxJug, duracion, rating, peso, categoria, dificultadCoop);
        }

        // Si no cumple la condición anterior, se instancia la clase base estándar.
        return new JuegoMesa(id, nombre, anio, minJug, maxJug, duracion, rating, peso, categoria);
    }

    // Método que será invocado polimórficamente. La JVM decidirá en tiempo de ejecución si llama a esta versión o a la de la subclase[cite: 3].
    public double getPuntuacionAjustada() {
        return rating * peso;
    }

    // Métodos de acceso (Getters) marcados como public para permitir lectura respetando el encapsulamiento[cite: 7].
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public int getAnio() { return anio; }
    public int getMinJugadores() { return minJugadores; }
    public int getMaxJugadores() { return maxJugadores; }
    public int getDuracion() { return duracion; }
    public double getRating() { return rating; }
    public double getPeso() { return peso; }
    public String getCategoria() { return categoria; }

    // equals() y hashCode() heredados de Object[cite: 3].
    // Se redefinen para que la identidad del objeto dependa de su 'id' y no de su espacio en memoria.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JuegoMesa juegoMesa = (JuegoMesa) o;
        return Objects.equals(id, juegoMesa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        // Al invocar getPuntuacionAjustada() aquí dentro, actuará dinámicamente según la clase real del objeto instanciado.
        return String.format("%s (%d) - Rating: %.2f, Peso: %.2f, Ajustada: %.2f",
                nombre, anio, rating, peso, getPuntuacionAjustada());
    }
}