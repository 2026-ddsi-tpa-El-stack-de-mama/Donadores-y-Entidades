package ar.edu.utn.dds.k3003.eventos;

import java.time.LocalDate;

public record QuejaEvento(
        String id,
        String donacionID,
        String donadorId,
        LocalDate fecha,
        String descripcion
) {
}