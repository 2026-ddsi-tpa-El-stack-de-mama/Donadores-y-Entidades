package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "necesidades")
@Getter
@Setter
@NoArgsConstructor
public class NecesidadMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String entidadID;
    private Integer nivelDeUrgencia;
    private String descripcion;
    private Integer cantidadObjetivo;
    private Integer cantidadActual;
    private String productoSolicitadoID;
    @Enumerated(EnumType.STRING)
    private TipoNecesidadMaterialEnum tipo;

}