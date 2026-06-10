package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Queja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuejasRepository
        extends JpaRepository<Queja, String> {
    List<Queja> findByDonadorID(String donadorID);
}
