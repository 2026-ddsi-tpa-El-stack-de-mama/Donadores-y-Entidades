package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "donadores")
@Getter
@Setter
@NoArgsConstructor
public class Donador {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String nombre;
  private String apellido;
  private Integer edad;
  private String email;
  private String nroDocumento;
  private String domicilio;

  @Enumerated(EnumType.STRING)
  private EstadoDonadorEnum estado;

  private String categoria;

  /*@OneToMany(
          cascade = CascadeType.ALL,
          orphanRemoval = true
  )*/
  @Transient
  private List<Queja> quejas = new ArrayList<>();

  //public Donador() {}

  public Donador(
          String nombre,
          String apellido,
          Integer edad,
          String email,
          String nroDocumento,
          String domicilio) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
    this.email = email;
    this.nroDocumento = nroDocumento;
    this.domicilio = domicilio;
    this.estado = EstadoDonadorEnum.VERIFICADO;
    this.categoria = "Ocasional";
  }

  public void agregarQueja(Queja queja) {
    this.quejas.add(queja);
  }
}

