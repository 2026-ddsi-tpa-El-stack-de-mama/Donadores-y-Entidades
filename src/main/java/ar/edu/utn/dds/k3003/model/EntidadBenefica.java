package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}