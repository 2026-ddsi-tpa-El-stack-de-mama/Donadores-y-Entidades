/* package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.NecesidadMaterial;

import java.util.*;

public class InMemoryNecesidadesRepo implements NecesidadesRepository {

    private Map<String, NecesidadMaterial> necesidades = new HashMap<>();

    @Override
    public NecesidadMaterial save(NecesidadMaterial necesidad) {
        necesidades.put(necesidad.getId(), necesidad);
        return necesidad;
    }

    @Override
    public Optional<NecesidadMaterial> findById(String id) {
        return Optional.ofNullable(necesidades.get(id));
    }

    @Override
    public List<NecesidadMaterial> findAll() {
        return new ArrayList<>(necesidades.values());
    }
} */