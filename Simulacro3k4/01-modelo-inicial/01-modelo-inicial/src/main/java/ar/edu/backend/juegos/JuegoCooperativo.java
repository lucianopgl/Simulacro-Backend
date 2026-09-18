package ar.edu.backend.juegos;

// Uso de "extends" para indicar que JuegoCooperativo deriva de JuegoMesa, obteniendo todos sus miembros[cite: 3].
public class JuegoCooperativo extends JuegoMesa {

    // Atributo específico de la subclase.
    private final int dificultadCooperativa;

    public int getDificultadCooperativa() {
        return dificultadCooperativa;
    }

    public JuegoCooperativo(String id, String nombre, int anio, int minJugadores, int maxJugadores, int duracion, double rating, double peso, String categoria, int dificultadCooperativa) {
        // Invocación explícita al constructor de la superclase mediante "super()".
        // Es obligatorio para inicializar el estado heredado (id, nombre, etc.) y reusar sus validaciones[cite: 3].
        super(id, nombre ,anio, minJugadores, maxJugadores, duracion, rating, peso, categoria);

        // Validación propia y exclusiva de la subclase.
        if (dificultadCooperativa < 1 || dificultadCooperativa > 10){
            throw new IllegalArgumentException("La dificultad cooperativa debe estar entre 1 y 10 ");
        }

        this.dificultadCooperativa = dificultadCooperativa;

    }

    // Anotación @Override indica la redefinición de un método heredado.
    // Cambia el comportamiento base para adaptarse a los atributos específicos de la derivada[cite: 3].
    @Override
    public double getPuntuacionAjustada() {
        // Se usan los getters (getRating, getPeso) porque los atributos originales son "private" en la superclase y no se heredan directamente de forma visible[cite: 3, 7].
        return (getRating() * getPeso()) + (dificultadCooperativa / 2.0);
    }

    @Override
    public String toString() {
        // super.toString() llama al comportamiento de la clase padre y luego le concatenamos el texto extra.
        return super.toString() + String.format(", Dif. Coop: %d", dificultadCooperativa);
    }

}