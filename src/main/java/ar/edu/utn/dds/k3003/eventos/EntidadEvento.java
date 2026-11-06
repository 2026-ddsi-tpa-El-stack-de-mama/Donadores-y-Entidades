package ar.edu.utn.dds.k3003.eventos;

public record EntidadEvento(
        String id,
        String razonSocial,
        String domicilio,
        String telefono,
        String correo
) {
}