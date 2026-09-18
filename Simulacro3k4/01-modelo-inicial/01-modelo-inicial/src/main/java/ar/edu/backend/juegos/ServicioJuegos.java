package ar.edu.backend.juegos;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServicioJuegos {
    private final List<JuegoMesa> juegos;

    public ServicioJuegos(List<JuegoMesa> juegos) {
        this.juegos = List.copyOf(juegos);
    }

    public double calcularPromedioRating() {
        return juegos.stream()
                .mapToDouble(JuegoMesa::getRating)
                .average()
                .orElse(0.0);
    }

    public Optional<JuegoMesa> obtenerMasComplejo() {
        return juegos.stream()
                .max(Comparator.comparingDouble(JuegoMesa::getPeso));
    }

    public Map<String, Long> contarJuegosPorCategoria() {
        return juegos.stream()
                .collect(Collectors.groupingBy(JuegoMesa::getCategoria, Collectors.counting()));
    }

    public List<JuegoMesa> filtrarPorJugadores(int cantidad) {
        return juegos.stream()
                .filter(j -> cantidad >= j.getMinJugadores() && cantidad <= j.getMaxJugadores())
                .collect(Collectors.toList());
    }

    public String panorama() {
        return String.format("Panorama: %d juegos | Categorías: %s", 
                juegos.size(), new java.util.TreeMap<>(contarJuegosPorCategoria()));
    }
}
