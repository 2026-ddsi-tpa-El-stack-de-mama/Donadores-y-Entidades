package ar.edu.utn.dds.k3003.eventos;

public record NecesidadEvento(
        String id,
        String entidadId,
        Integer nivelDeUrgencia,
        String descripcion,
        Integer cantidadObjetivo,
        Integer cantidadActual,
        String productoSolicitadoID,
        String tipo
) {
}