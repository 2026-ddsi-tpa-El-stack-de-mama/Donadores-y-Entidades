package ar.edu.utn.dds.k3003.eventos;

public record DonadorEvento(
        String id,
        String nombre,
        String apellido,
        Integer edad,
        String email,
        String nroDocumento,
        String domicilio,
        String estado,
        String categoria
) {
}