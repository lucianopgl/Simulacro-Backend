package ar.edu.utnfrc.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "PELICULA")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(name ="fecha_estreno")
    private LocalDate fechaEstreno;

    @Column(name ="precio_base_alquiler")
    private double precioBaseAlquiler;

    @Enumerated(EnumType.STRING)
    private Clasificacion clasificacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genero_id")
    private Genero genero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "director_id")
    private Director director;

    public double calcularPrecioAlquiler(){
        double precioFinal = this.precioBaseAlquiler;
        LocalDate fechaActual = LocalDate.now();
        long diasDesdeEstreno = java.time.temporal.ChronoUnit.DAYS.between(this.fechaEstreno, fechaActual);

        if (diasDesdeEstreno <= 365) {
            precioFinal *= 1.25; // Aumenta un 25% si es reciente
        }

        if (this.clasificacion == Clasificacion.ADULTOS_18 || this.clasificacion == Clasificacion.ADOLESCENTES_13) {
            precioFinal *= 1.05; // Aumenta un 5% adicional para estas clasificaciones
        }


        return precioFinal;
    }
}
