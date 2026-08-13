package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.HistorialEstadoDonador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialEstadoDonadorRepository
        extends JpaRepository<HistorialEstadoDonador, String> {

    List<HistorialEstadoDonador> findByDonadorId(String donadorId);
}