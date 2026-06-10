package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "quejas")
@Getter
@Setter
@NoArgsConstructor
public class Queja {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String donacionID;
    @ManyToOne
    @JoinColumn(name = "donador_id")
    private Donador donador;
    private LocalDate fecha;
    private String descripcion;

}
