package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.List;
import java.util.NoSuchElementException;

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

    @GetMapping("/{necesidadID}")
    public ResponseEntity<NecesidadMaterialDTO> obtenerNecesidad(
            @PathVariable String necesidadID
    ) {
        necesidadesConsultas.increment();

        return ResponseEntity.ok(
                fachada.buscarNecesidadPorID(necesidadID)
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

    @Operation(summary = "Crear una necesidad")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Necesidad creada"),
            @ApiResponse(responseCode = "404", description = "El producto solicitado no existe en Donaciones")
    })
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

    @PutMapping("/{necesidadID}")
    public ResponseEntity<NecesidadMaterialDTO> modificarNecesidad(
            @PathVariable String necesidadID,
            @RequestBody NecesidadMaterialDTO necesidadDTO
    ) {
        return ResponseEntity.ok(
                fachada.modificarNecesidad(necesidadID, necesidadDTO)
        );
    }

    @DeleteMapping("/{necesidadID}")
    public ResponseEntity<String> eliminarNecesidad(
            @PathVariable String necesidadID
    ) {
        try {
            fachada.eliminarNecesidad(necesidadID);

            return ResponseEntity.ok(
                    "Necesidad " + necesidadID + " eliminada correctamente"
            );

        } catch (NoSuchElementException e) {

            return ResponseEntity.status(404).body(
                    "No se encontró la necesidad con ID: " + necesidadID
            );
        }
    }
}