package ar.edu.utn.dds.k3003.eventos;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;

import java.util.List;

public record DonadorEvento(
        String id,
        String nombre,
        EstadoDonadorEnum estado,
        String categoria,
        List<QuejaDTO> quejasRecibidas
) {
}