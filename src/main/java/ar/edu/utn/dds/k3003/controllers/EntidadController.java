package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.List;


@RestController
@RequestMapping("/entidades")
public class EntidadController {

    private final Fachada fachada;
    private final Counter entidadesConsultas;
    private final Counter entidadesCreadas;

    public EntidadController(Fachada fachada, MeterRegistry meterRegistry) {
        this.fachada = fachada;
        this.entidadesConsultas =
                meterRegistry.counter("entidades.consultas");

        this.entidadesCreadas =
                meterRegistry.counter("entidades.creadas");
    }

    @PostMapping
    public ResponseEntity<EntidadBeneficaDTO> agregarEntidad(
            @RequestBody EntidadBeneficaDTO entidadDTO
    ) {

        entidadesCreadas.increment();

        return ResponseEntity.ok(
                fachada.agregarEntidad(entidadDTO)
        );
    }

    @GetMapping
    public ResponseEntity<List<EntidadBeneficaDTO>> obtenerEntidades() {

        entidadesConsultas.increment();

        return ResponseEntity.ok(
                fachada.buscarEntidades()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntidadBeneficaDTO> buscarEntidad(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                fachada.buscarEntidadPorID(id)
        );
    }
}