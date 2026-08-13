package ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HistorialEstadoDonadorDTO {

    private EstadoDonadorEnum estadoAnterior;
    private EstadoDonadorEnum estadoNuevo;
    private LocalDateTime fechaCambio;

    public HistorialEstadoDonadorDTO(
            EstadoDonadorEnum estadoAnterior,
            EstadoDonadorEnum estadoNuevo,
            LocalDateTime fechaCambio) {

        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = fechaCambio;
    }
}