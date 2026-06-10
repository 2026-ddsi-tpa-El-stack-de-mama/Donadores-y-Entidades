package ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record QuejaDTO(
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String id, String donacionID, @Schema(accessMode = Schema.AccessMode.READ_ONLY) String donadorID, LocalDate fecha, String descripcion) {}
