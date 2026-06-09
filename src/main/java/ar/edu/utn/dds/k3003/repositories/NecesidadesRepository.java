package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NecesidadesRepository
        extends JpaRepository<NecesidadMaterial, String> {

 /*   NecesidadMaterial save(NecesidadMaterial necesidad);

    Optional<NecesidadMaterial> findById(String id);

    List<NecesidadMaterial> findAll(); */
}