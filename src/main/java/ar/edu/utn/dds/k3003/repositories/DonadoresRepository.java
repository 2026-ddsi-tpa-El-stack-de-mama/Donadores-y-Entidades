package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Donador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonadoresRepository
        extends JpaRepository<Donador, String> {
/*  Optional<Donador> findById(String id);

  Donador save(Donador donador);

  Donador deleteById(String id);

  List<Donador> findAll();

 */
}
