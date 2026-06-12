package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorStatsDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.List;

@RestController
@RequestMapping("/donadores")
public class DonadorController {

  private final Fachada fachada;
  private final Counter consultasDonadores;
  private final Counter donadoresCreados;
  private final Counter categoriasModificadas;

  public DonadorController(Fachada fachada, MeterRegistry meterRegistry) {
    this.fachada = fachada;
    this.consultasDonadores =
            meterRegistry.counter("donadores.consultas");
    this.donadoresCreados =
            meterRegistry.counter("donadores.creados");
    this.categoriasModificadas =
            meterRegistry.counter("donadores.categoria_modificada");
  }

  @PostMapping
  public ResponseEntity<DonadorDTO> agregarDonador(
          @RequestBody DonadorDTO donadorDTO
  ) {

    donadoresCreados.increment();

    return ResponseEntity.ok(
            fachada.agregarDonador(donadorDTO)
    );
  }

  @GetMapping
  public ResponseEntity<List<DonadorDTO>> obtenerDonadores() {

    consultasDonadores.increment();

    return ResponseEntity.ok(
            fachada.buscarDonadores()
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<DonadorDTO> buscarDonador(
          @PathVariable String id
  ) {

    return ResponseEntity.ok(
            fachada.buscarDonadorPorID(id)
    );
  }

  @PatchMapping("/{id}/estado")
  public ResponseEntity<DonadorDTO> modificarEstado(
          @PathVariable String id,
          @RequestParam EstadoDonadorEnum estado
  ) {

    return ResponseEntity.ok(
            fachada.modificarEstado(id, estado)
    );
  }

  @PatchMapping("/{id}/categoria")
  public ResponseEntity<DonadorDTO> modificarCategoria(
          @PathVariable String id,
          @RequestParam String categoria
  ) {

    categoriasModificadas.increment();

    return ResponseEntity.ok(
            fachada.modificarCategoria(id, categoria)
    );
  }

  @GetMapping("/{id}/puede-donar")
  public ResponseEntity<Boolean> puedeDonar(
          @PathVariable String id
  ) {

    return ResponseEntity.ok(
            fachada.puedeDonar(id)
    );
  }

  @GetMapping("/{id}/estadisticas")
  public ResponseEntity<DonadorStatsDTO> estadisticas(
          @PathVariable String id
  ) {

    return ResponseEntity.ok(
            fachada.estadisticasDonador(id)
    );
  }
/*
  @PostMapping("/{id}/quejas")
  public ResponseEntity<QuejaDTO> agregarQueja(
          @PathVariable String id,
          @RequestBody QuejaDTO quejaDTO
  ) {

    return ResponseEntity.ok(
            fachada.agregarQueja(quejaDTO)
    );
  } */

  @PostMapping("/{id}/quejas")
  public QuejaDTO agregarQueja(
          @PathVariable String id,
          @RequestBody QuejaDTO quejaDTO) {

    return fachada.agregarQueja(id, quejaDTO);
  }

  @GetMapping("/{id}/quejas")
  public ResponseEntity<List<QuejaDTO>> obtenerQuejas(
          @PathVariable String id
  ) {

    return ResponseEntity.ok(
            fachada.obtenerQuejasDe(id)
    );
  }
}
/*
@RestController
@RequestMapping("/donadores")
public class DonadorController {

  private Fachada fachada;

  public DonadorController(Fachada fachada) {
    this.fachada = fachada;
  }

  // Opcion 1 utilizando @RequestMapping
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<DonadorDTO> postDonador(@RequestBody DonadorDTO donadorDTO) {
    DonadorDTO donadorAgregado = fachada.agregarDonador(donadorDTO);
    return ResponseEntity.ok(donadorAgregado);
  }

  // Opcion 2 utilizando @GetMapping
  @GetMapping
  public ResponseEntity<DonadorDTO> getDonadorByID(@RequestParam String donadorID) {
    return ResponseEntity.status(HttpStatus.OK).body(this.fachada.buscarDonadorPorID(donadorID));
  }
}
*/