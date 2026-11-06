package ar.edu.utn.dds.k3003.eventos;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum;

public record NecesidadEvento(
        String id,
        Integer nivelDeUrgencia,
        TipoNecesidadMaterialEnum tipoNecesidadMaterial,
        EntidadBeneficaDTO entidad,
        Integer cantidadFaltante
) {
}