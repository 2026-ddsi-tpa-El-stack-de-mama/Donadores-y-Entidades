package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.*;
import ar.edu.utn.dds.k3003.model.Donador;
import ar.edu.utn.dds.k3003.model.EntidadBenefica;
import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import ar.edu.utn.dds.k3003.model.Queja;

public class DonadoresYEntidadesDataMapper {

  // MODELO → DTO
  public DonadorDTO toDonadorDTO(Donador donador) {
    return new DonadorDTO(
            donador.getId(),
            donador.getNombre(),
            donador.getApellido(),
            donador.getEdad(),
            donador.getEmail(),
            donador.getNroDocumento(),
            donador.getDomicilio(),
            donador.getEstado(),
            donador.getCategoria()
    );
  }

  // DTO → MODELO
  public Donador toDonador(DonadorDTO dto) {

    Donador donador = new Donador(
            dto.nombre(),
            dto.apellido(),
            dto.edad(),
            dto.email(),
            dto.nroDocumento(),
            dto.domicilio()
    );

    donador.setId(dto.id());
    donador.setEstado(dto.estado());
    donador.setCategoria(dto.categoria());

    return donador;
  }

  // ENTIDAD BENEFICA

  public EntidadBeneficaDTO toEntidadDTO(EntidadBenefica entidad) {
    return new EntidadBeneficaDTO(
            entidad.getId(),
            entidad.getRazonSocial(),
            entidad.getDomicilio(),
            entidad.getTelefono(),
            entidad.getCorreo()
    );
  }

  public EntidadBenefica toEntidad(EntidadBeneficaDTO dto) {

    EntidadBenefica entidad = new EntidadBenefica();

    entidad.setId(dto.id());
    entidad.setRazonSocial(dto.razonSocial());
    entidad.setDomicilio(dto.domicilio());
    entidad.setTelefono(dto.telefono());
    entidad.setCorreo(dto.correo());

    return entidad;
  }

  // NECESIDAD MATERIAL

  public NecesidadMaterialDTO toNecesidadDTO(NecesidadMaterial necesidad) {

    return new NecesidadMaterialDTO(
            necesidad.getId(),
            necesidad.getEntidadID(),
            necesidad.getNivelDeUrgencia(),
            necesidad.getDescripcion(),
            necesidad.getCantidadObjetivo(),
            necesidad.getCantidadActual(),
            necesidad.getProductoSolicitadoID(),
            necesidad.getTipo()
    );
  }

  public NecesidadMaterial toNecesidad(NecesidadMaterialDTO dto) {

    NecesidadMaterial necesidad = new NecesidadMaterial();

    necesidad.setId(dto.id());
    necesidad.setEntidadID(dto.entidadID());
    necesidad.setNivelDeUrgencia(dto.nivelDeUrgencia());
    necesidad.setDescripcion(dto.descripcion());
    necesidad.setProductoSolicitadoID(dto.productoSolicitadoID());
    necesidad.setCantidadObjetivo(dto.cantidadObjetivo());
    necesidad.setCantidadActual(0);
    necesidad.setTipo(dto.tipo());

    return necesidad;
  }

  // QUEJA

  public QuejaDTO toQuejaDTO(Queja queja) {

    return new QuejaDTO(
            queja.getId(),
            queja.getDonacionID(),
            queja.getDonadorID(),
            queja.getFecha(),
            queja.getDescripcion()
    );
  }

  public Queja toQueja(QuejaDTO dto) {

    Queja queja = new Queja();

    queja.setDonacionID(dto.donacionID());
    queja.setDonadorID(dto.donadorID());
    queja.setFecha(dto.fecha());
    queja.setDescripcion(dto.descripcion());

    return queja;
  }
}
