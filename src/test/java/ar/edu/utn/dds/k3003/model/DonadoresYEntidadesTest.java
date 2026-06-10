/* package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum.EXTRAORDINARIA;
import static ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum.RECURRENTE;
import static org.junit.jupiter.api.Assertions.*;

public class DonadoresYEntidadesTest {

  private Fachada fachada;

  @BeforeEach
  void init() {
    fachada = new Fachada();
  }

  @Test
  void noPermiteAgregarDonadorDuplicado() {
    DonadorDTO donador = new DonadorDTO(
            "1",
            "Juan",
            "Perez",
            30,
            "juan@mail.com",
            "12345678",
            "Calle 123",
            EstadoDonadorEnum.VERIFICADO,
            "Bronce"
    );

    fachada.agregarDonador(donador);

    assertThrows(RuntimeException.class, () -> {
      fachada.agregarDonador(donador);
    });
  }

  @Test
  void modificarEstadoActualizaCorrectamente() {
    DonadorDTO donador = new DonadorDTO(
            "2",
            "Ana",
            "Lopez",
            28,
            "ana@mail.com",
            "87654321",
            "Av Siempre Viva",
            EstadoDonadorEnum.VERIFICADO,
            "Plata"
    );

    DonadorDTO guardado = fachada.agregarDonador(donador);

    DonadorDTO actualizado =
            fachada.modificarEstado(
                    guardado.id(),
                    EstadoDonadorEnum.BANEADO
            );

    assertEquals(
            EstadoDonadorEnum.BANEADO,
            actualizado.estado()
    );
  }

  @Test
  void donadorBaneadoNoPuedeDonar() {
    DonadorDTO donador = new DonadorDTO(
            "3",
            "Luis",
            "Gomez",
            40,
            "luis@mail.com",
            "11111111",
            "Mitre 456",
            EstadoDonadorEnum.BANEADO,
            "Oro"
    );

    DonadorDTO guardado = fachada.agregarDonador(donador);

    Boolean resultado =
            fachada.puedeDonar(guardado.id());

    assertFalse(resultado);
  }

  @Test
  void agregarEntidadGuardaCorrectamente() {
    EntidadBeneficaDTO entidad = new EntidadBeneficaDTO(
            "E1",
            "Comedor Central",
            "Av Siempre Viva 123",
            "11223344",
            "comedor@mail.com"
    );

    EntidadBeneficaDTO resultado =
            fachada.agregarEntidad(entidad);

    assertEquals("E1", resultado.id());
    assertEquals("Comedor Central", resultado.razonSocial());
  }

  @Test
  void agregarNecesidadSeGuardaCorrectamente() {
    NecesidadMaterialDTO necesidad = new NecesidadMaterialDTO(
            "N1",
            "E1",
            5,
            "Faltan alimentos",
            100,
            "P1",
            EXTRAORDINARIA
    );

    NecesidadMaterialDTO resultado =
            fachada.registrarNecesidad(necesidad);

    assertEquals("N1", resultado.id());
    assertEquals(100, resultado.cantidadObjetivo());
  }

  @Test
  void obtenerQuejasSinQuejasDevuelveListaVacia() {
    DonadorDTO donador = new DonadorDTO(
            "11",
            "Mariana",
            "Fernandez",
            29,
            "mariana@mail.com",
            "55667788",
            "San Martin 456",
            EstadoDonadorEnum.VERIFICADO,
            "Bronce"
    );

    DonadorDTO guardado = fachada.agregarDonador(donador);

    assertTrue(
            fachada.obtenerQuejasDe(guardado.id()).isEmpty()
    );
  }

  @Test
  void satisfacerNecesidadExactaHastaObjetivoFunciona() {
    NecesidadMaterialDTO necesidad = new NecesidadMaterialDTO(
            "N20",
            "E20",
            90,
            "Faltan útiles",
            100,
            "P20",
            EXTRAORDINARIA
    );

    NecesidadMaterialDTO registrada =
            fachada.registrarNecesidad(necesidad);

    NecesidadMaterialDTO resultado =
            fachada.satisfacerNecesidad(
                    registrada.id(),
                    10
            );

    assertNotNull(resultado);
    assertEquals("N20", resultado.id());
  }

  @Test
  void cambiarEstadoABaneadoImpideDonar() {
    DonadorDTO donador = new DonadorDTO(
            "50",
            "Sofia",
            "Martinez",
            31,
            "sofia@mail.com",
            "44556611",
            "Moreno 123",
            EstadoDonadorEnum.VERIFICADO,
            "Oro"
    );

    DonadorDTO guardado = fachada.agregarDonador(donador);

    assertTrue(
            fachada.puedeDonar(guardado.id())
    );

    fachada.modificarEstado(
            guardado.id(),
            EstadoDonadorEnum.BANEADO
    );

    assertFalse(
            fachada.puedeDonar(guardado.id())
    );
  }

  @Test
  void satisfacerNecesidadDejaDeAparecerComoInsatisfecha() {
    NecesidadMaterialDTO necesidad = new NecesidadMaterialDTO(
            "N50",
            "E50",
            90,
            "Faltan alimentos",
            100,
            "P50",
            EXTRAORDINARIA
    );

    fachada.registrarNecesidad(necesidad);

    assertFalse(
            fachada.obtenerNecesidadesInsatisfechasDe("P50").isEmpty()
    );

    fachada.satisfacerNecesidad("N50", 100);

    assertTrue(
            fachada.obtenerNecesidadesInsatisfechasDe("P50").isEmpty()
    );
  }

  //test TP2
  @Test
  void necesidadRecurrenteNoAceptaSatisfaccionParcial() {

    NecesidadMaterialDTO necesidad =
            new NecesidadMaterialDTO(
                    "R1",
                    "E1",
                    10,
                    "Fideos semanales",
                    100,
                    "P1",
                    RECURRENTE
            );

    fachada.registrarNecesidad(necesidad);

    assertThrows(RuntimeException.class, () -> {
      fachada.satisfacerNecesidad("R1", 50);
    });
  }

  //test TP2
  @Test
  void necesidadExtraordinariaAceptaSatisfaccionParcial() {
    NecesidadMaterialDTO necesidad =
            new NecesidadMaterialDTO(
                    "E1",
                    "Entidad1",
                    5,
                    "Alimentos",
                    100,
                    "P1",
                    EXTRAORDINARIA
            );

    NecesidadMaterialDTO registrada =
            fachada.registrarNecesidad(necesidad);

    assertDoesNotThrow(() -> {
      fachada.satisfacerNecesidad(
              registrada.id(),
              50
      );
    });
  }

} */
