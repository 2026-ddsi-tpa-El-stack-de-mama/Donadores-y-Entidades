package ar.edu.utn.dds.k3003.eventos;

public record EventoQueue(
        String id,
        String tipoEvento,
        Object descripcion
) {
}
