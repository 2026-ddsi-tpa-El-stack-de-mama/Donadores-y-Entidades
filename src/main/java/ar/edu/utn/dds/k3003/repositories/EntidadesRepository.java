package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.EntidadBenefica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntidadesRepository
        extends JpaRepository<EntidadBenefica, String> {

/*    EntidadBenefica save(EntidadBenefica entidad);

    Optional<EntidadBenefica> findById(String id);

    List<EntidadBenefica> findAll(); */

}