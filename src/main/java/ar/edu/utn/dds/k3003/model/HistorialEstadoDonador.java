package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estado_donador")
@Getter
@Setter
@NoArgsConstructor
public class HistorialEstadoDonador {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "donador_id", nullable = false)
    private Donador donador;

    @Enumerated(EnumType.STRING)
    private EstadoDonadorEnum estadoAnterior;

    @Enumerated(EnumType.STRING)
    private EstadoDonadorEnum estadoNuevo;

    private LocalDateTime fechaCambio;

    public HistorialEstadoDonador(
            Donador donador,
            EstadoDonadorEnum estadoAnterior,
            EstadoDonadorEnum estadoNuevo) {

        this.donador = donador;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = LocalDateTime.now();
    }
}