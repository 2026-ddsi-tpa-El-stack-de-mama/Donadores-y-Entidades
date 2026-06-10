package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entidades")
@Getter
@Setter
@NoArgsConstructor
public class EntidadBenefica {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String razonSocial;
    private String domicilio;
    private String telefono;
    private String correo;
    @OneToMany(
            mappedBy = "entidad",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<NecesidadMaterial> necesidades = new ArrayList<>();



    public void agregarNecesidad(NecesidadMaterial necesidad) {
        this.necesidades.add(necesidad);
    }

}