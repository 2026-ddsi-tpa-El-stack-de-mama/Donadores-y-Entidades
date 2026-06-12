package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.List;

@RestController
@RequestMapping("/necesidades")
public class NecesidadController {

    private final Fachada fachada;
    private final Counter necesidadesConsultas;
    private final Counter necesidadesCreadas;

    public NecesidadController(Fachada fachada, MeterRegistry meterRegistry) {
        this.fachada = fachada;

        this.necesidadesConsultas =
                meterRegistry.counter("necesidades.consultas");

        this.necesidadesCreadas =
                meterRegistry.counter("necesidades.creadas");
    }

    @PostMapping
    public ResponseEntity<NecesidadMaterialDTO> registrarNecesidad(
            @RequestBody NecesidadMaterialDTO necesidadDTO
    ) {

        necesidadesCreadas.increment();

        return ResponseEntity.ok(
                fachada.registrarNecesidad(necesidadDTO)
        );
    }

    @GetMapping
    public ResponseEntity<List<NecesidadMaterialDTO>> obtenerNecesidades() {

        necesidadesConsultas.increment();

        return ResponseEntity.ok(
                fachada.buscarNecesidades()
        );
    }

    @GetMapping("/insatisfechas")
    public ResponseEntity<List<NecesidadMaterialDTO>> obtenerNecesidadesInsatisfechas(
            @RequestParam String productoId
    ) {
        return ResponseEntity.ok(
                fachada.obtenerNecesidadesInsatisfechas(productoId)
        );
    }

    @PostMapping("/{necesidadID}/satisfaccion")
    public ResponseEntity<NecesidadMaterialDTO> satisfacerNecesidad(
            @PathVariable String necesidadID,
            @RequestParam Integer cantidad
    ) {

        return ResponseEntity.ok(
                fachada.satisfacerNecesidad(
                        necesidadID,
                        cantidad
                )
        );
    }
}