package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NecesidadMaterial {

    private String id;
    private String entidadID;
    private Integer nivelDeUrgencia;
    private String descripcion;
    private Integer cantidadObjetivo;
    private Integer cantidadActual;
    private String productoSolicitadoID;
    private TipoNecesidadMaterialEnum tipo;

}