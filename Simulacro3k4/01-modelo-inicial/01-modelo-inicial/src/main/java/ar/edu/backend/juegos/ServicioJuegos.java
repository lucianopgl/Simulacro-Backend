package ar.edu.backend.juegos;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServicioJuegos {
    // Almacena la colección de juegos a procesar.
    // Se declara 'final' para que la referencia a la lista no pueda ser reemplazada por otra luego de la instanciación.
    private final List<JuegoMesa> juegos;

    public ServicioJuegos(List<JuegoMesa> juegos) {
        // List.copyOf crea una lista inmutable para asegurar que el servicio no altere la lista original ni sufra alteraciones externas (Defensive Copy).
        this.juegos = List.copyOf(juegos);
    }

    public double calcularPromedioRating() {
        // Uso de Stream API para procesamiento declarativo: mapToDouble evita el overhead del unboxing iterando los valores numéricos rápidamente[cite: 4, 7].
        return juegos.stream()
                .mapToDouble(JuegoMesa::getRating)
                .average()
                .orElse(0.0); // Retorno seguro si la lista está vacía.
    }

    public Optional<JuegoMesa> obtenerMasComplejo() {
        // Operación terminal "max" que recibe un Comparator basado en referencias a métodos (JuegoMesa::getPeso)[cite: 7].
        return juegos.stream()
                .max(Comparator.comparingDouble(JuegoMesa::getPeso));
    }

    public double calcularSumaPuntuacionAjustada(){
        return juegos.stream()
                // Polimorfismo en acción: mapToDouble invocará la versión correcta de getPuntuacionAjustada() dependiendo de si el elemento es JuegoMesa o JuegoCooperativo[cite: 3].
                .mapToDouble(JuegoMesa::getPuntuacionAjustada)
                .sum();
    }

    public Map<String, Long> contarJuegosPorCategoria() {
        // groupingBy actúa como un agrupador SQL, y counting() es un 'downstream collector' que cuenta los elementos en lugar de recolectarlos en listas[cite: 7].
        return juegos.stream()
                .collect(Collectors.groupingBy(
                        JuegoMesa::getCategoria,
                        Collectors.counting()));
    }

    public Map<String, Long> contarJuegosPorTipo(){
        return juegos.stream()
                // El operador instanceof verifica en tiempo de ejecución la clase "real" a la que pertenece el objeto instanciado[cite: 3].
                .collect(Collectors.groupingBy(juego -> juego instanceof JuegoCooperativo ? "Cooperativo": "Normal",
                        Collectors.counting()
                ));
    }

    public List<JuegoMesa> filtrarPorJugadores(int cantidad) {
        // Operación intermedia "filter" que usa una función lambda (Predicate) para retener solo los objetos que cumplan la condición[cite: 7].
        return juegos.stream()
                .filter(j -> cantidad >= j.getMinJugadores() && cantidad <= j.getMaxJugadores())
                .collect(Collectors.toList());
    }

    public String panorama() {
        return String.format(
                "--- Panorama del Catálogo ---\n" +
                        "Total Puntos: %.2f\n" +
                        "Promedio Rating: %.2f\n" +
                        "Juego más complejo: %s\n" +
                        "Conteo por tipo: %s",
                calcularSumaPuntuacionAjustada(),
                calcularPromedioRating(),
                // Se extrae el String interno del Optional con .map() de forma segura[cite: 4].
                obtenerMasComplejo().map(JuegoMesa::getNombre).orElse("Ninguno"),
                contarJuegosPorTipo().toString()
        );
    }
}