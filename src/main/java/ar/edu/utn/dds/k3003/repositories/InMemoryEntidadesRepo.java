/* package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.EntidadBenefica;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryEntidadesRepo implements EntidadesRepository {

    private Map<String, EntidadBenefica> entidades = new HashMap<>();

    @Override
    public EntidadBenefica save(EntidadBenefica entidad) {
        entidades.put(entidad.getId(), entidad);
        return entidad;
    }

    @Override
    public Optional<EntidadBenefica> findById(String id) {
        return Optional.ofNullable(entidades.get(id));
    }

    @Override
    public List<EntidadBenefica> findAll() {
        return entidades.values()
                .stream()
                .toList();
    }
} */
