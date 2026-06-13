package ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades;

import java.util.List;

public record InsigniasResponseDTO(
        List<InsigniaExternaDTO> data
) {}