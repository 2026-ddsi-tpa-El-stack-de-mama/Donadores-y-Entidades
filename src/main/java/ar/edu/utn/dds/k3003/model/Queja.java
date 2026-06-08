package ar.edu.utn.dds.k3003.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Queja {

    private String id;
    private String donacionID;
    private String donadorID;
    private LocalDate fecha;
    private String descripcion;

}
