package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.*;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaIncentivos;
import ar.edu.utn.dds.k3003.exceptions.DonadorNoEncontradoException;
import ar.edu.utn.dds.k3003.exceptions.DonadorYaExistenteException;
import ar.edu.utn.dds.k3003.model.Donador;
import ar.edu.utn.dds.k3003.model.EntidadBenefica;
import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import ar.edu.utn.dds.k3003.model.Queja;
import ar.edu.utn.dds.k3003.repositories.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum.RECURRENTE;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.MisionResponseDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.InsigniasResponseDTO;

@Service
public class Fachada implements FachadaDonadoresYEntidades {


  private DonadoresYEntidadesDataMapper donadoresYEntidadesDataMapper =
      new DonadoresYEntidadesDataMapper();
  private DonadoresRepository donadoresRepository;
  private EntidadesRepository entidadesRepository;
  private NecesidadesRepository necesidadesRepository;
  private QuejasRepository quejasRepository;

  private FachadaIncentivos fachadaIncentivos;

  public Fachada(DonadoresRepository donadoresRepository,
                 EntidadesRepository entidadesRepository,
                 NecesidadesRepository necesidadesRepository,
                 QuejasRepository quejasRepository) {
    /*
    Para que se ejecuten correctamente los tests, se necesita tener un constructor vacio
    Es decir, que no reciba parametros.
    Si necesitan un constructor con parametros
    Java permite tener varios constructores conviviendo sin conflictos.
    */

    this.donadoresRepository = donadoresRepository;
    this.entidadesRepository = entidadesRepository;
    this.necesidadesRepository = necesidadesRepository;
    this.quejasRepository = quejasRepository;
  }



  @Override
  public DonadorDTO agregarDonador(DonadorDTO donadorDTO) {

    if (donadorDTO == null) {
      throw new RuntimeException();
    }
/*
    if (donadorDTO.id() != null && this.donadoresRepository.findById(donadorDTO.id()).isPresent()) {
      throw new RuntimeException();
    } */

    Donador donador = donadoresYEntidadesDataMapper.toDonador(donadorDTO);

    Donador donadorGuardado = this.donadoresRepository.save(donador);

    return donadoresYEntidadesDataMapper.toDonadorDTO(donadorGuardado);
  }

  @Override
  public DonadorDTO buscarDonadorPorID(String donadorID) throws NoSuchElementException {
    val donadorOptional = this.donadoresRepository.findById(donadorID);

    if (donadorOptional.isEmpty()) {
      throw new RuntimeException();
    }
    val donadorFinal = donadorOptional.get();

    return donadoresYEntidadesDataMapper.toDonadorDTO(donadorFinal);
  }

  //IMPLEMENTADO
  @Override
  public DonadorDTO modificarEstado(String donadorID, EstadoDonadorEnum estado) {

    Donador donador = donadoresRepository.findById(donadorID).orElse(null);

    if (donador == null) {
      throw new RuntimeException();
    }
    if (estado == null) {
      throw new RuntimeException();
    }

    donador.setEstado(estado);

    Donador donadorGuardado = donadoresRepository.save(donador);

    return donadoresYEntidadesDataMapper.toDonadorDTO(donadorGuardado);
  }

  @Override
  public DonadorDTO modificarCategoria(String donadorID, String categoria){

    Donador donador = donadoresRepository.findById(donadorID).orElse(null);

    if (donador == null) {
      throw new RuntimeException();
    }
    if (categoria == null) {
      throw new RuntimeException();
    }

    donador.setCategoria(categoria);

    Donador donadorGuardado = donadoresRepository.save(donador);

    return donadoresYEntidadesDataMapper.toDonadorDTO(donadorGuardado);
  }

/*
  @Override
  public List<NecesidadMaterialDTO> obtenerNecesidadesInsatisfechasDe(String productoSolicitadoID) {
    return List.of();
  }
*/

  @Override
  public void setFachadaIncentivos(FachadaIncentivos fachadaIncentivos) {
    //agregado
    this.fachadaIncentivos = fachadaIncentivos;
  }

  //IMPLEMENTADO
  @Override
  public Boolean puedeDonar(String donadorID) throws NoSuchElementException {

    Donador donador = donadoresRepository.findById(donadorID).orElse(null);

    if (donador == null) {
      throw new RuntimeException(/*"Donador no encontrado"*/);
    }

    switch (donador.getEstado()) {

      case VERIFICADO:
        return true;

      case SOSPECHOSO:
        return false;

      case BANEADO:
        return false;

      default:
        return false;
    }
  }

  @Override
  public List<QuejaDTO> obtenerQuejasDe(String donadorID) {

    Donador donador = donadoresRepository
            .findById(donadorID)
            .orElseThrow();

    return donador.getQuejas()
            .stream()
            .map(donadoresYEntidadesDataMapper::toQuejaDTO)
            .toList();
  }

  //IMPLEMENTADO
  @Override
  public NecesidadMaterialDTO satisfacerNecesidad(String necesidadID, Integer cantidad) {

    if (cantidad == null || cantidad <= 0) {
      throw new RuntimeException(/*"Cantidad inválida"*/);
    }
    NecesidadMaterial necesidad = necesidadesRepository
            .findById(necesidadID)
            .orElse(null);
    if (necesidad == null) {
      throw new RuntimeException(/*"Necesidad no encontrada"*/);
    }
    int nuevaCantidad = necesidad.getCantidadActual() + cantidad;
    //Nuevo requerimiento TP2
    if (necesidad.getTipo() == RECURRENTE &&
            cantidad < necesidad.getCantidadObjetivo()) {
      throw new RuntimeException();
    }
    if (necesidad.getCantidadActual() >= necesidad.getCantidadObjetivo()) {
      throw new RuntimeException("Necesidad ya satisfecha");
    }
    necesidad.setCantidadActual(nuevaCantidad);

    necesidadesRepository.save(necesidad);

    return donadoresYEntidadesDataMapper.toNecesidadDTO(necesidad);
  }

 /* //IMPLEMENTADO
  @Override
  public DonadorStatsDTO estadisticasDonador(String donadorID) {

    Donador donador = donadoresRepository
            .findById(donadorID)
            .orElseThrow(() -> new NoSuchElementException("Donador no encontrado"));

    List<InsigniaDTO> insignias = fachadaIncentivos.getInsigniasDeDonador(donadorID);

    MisionDTO mision = fachadaIncentivos.getMisionEnCursoDeDonador(donadorID);

    List<String> insigniasID = insignias
            .stream()
            .map(InsigniaDTO::id)
            .toList();

    String misionActualID = mision != null ? mision.id() : null;

    return new DonadorStatsDTO(
            donador.getId(),
            donador.getNombre(),
            donador.getApellido(),
            donador.getEdad(),
            donador.getEstado(),
            donador.getCategoria(),
            misionActualID,
            insigniasID
    );
  } */

  //nuevo TP3
  @Override
  public DonadorStatsDTO estadisticasDonador(String donadorID) {

    Donador donador = donadoresRepository
            .findById(donadorID)
            .orElseThrow();

    RestTemplate restTemplate = new RestTemplate();

    String baseUrl =
            "https://entrega-2-cesartomasg-produccion.onrender.com";

    String misionActualID = null;
    List<String> insigniasID = new ArrayList<>();

    try {

      MisionResponseDTO response =
              restTemplate.getForObject(
                      baseUrl
                              + "/misiones/donadores/"
                              + donadorID
                              + "/mision",
                      MisionResponseDTO.class);

      if (response != null && response.data() != null) {
        misionActualID = response.data().id();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    try {

      String response =
              restTemplate.getForObject(
                      baseUrl
                              + "/insignias/donadores/"
                              + donadorID,
                      String.class);

      System.out.println(response);

    } catch (Exception e) {
      e.printStackTrace();

    }

    return new DonadorStatsDTO(
            donador.getId(),
            donador.getNombre(),
            donador.getApellido(),
            donador.getEdad(),
            donador.getEstado(),
            donador.getCategoria().toString(),
            misionActualID,
            insigniasID
    );
  }

  //IMPLEMENTADO
  @Override
  public EntidadBeneficaDTO agregarEntidad(EntidadBeneficaDTO entidadBeneficaDTO) {

    if (entidadBeneficaDTO == null) {
      throw new RuntimeException();
    }

    if (entidadBeneficaDTO.id() != null &&
            entidadesRepository.findById(entidadBeneficaDTO.id()).isPresent()) {
      throw new RuntimeException();
    }

    EntidadBenefica entidad =
            donadoresYEntidadesDataMapper.toEntidad(entidadBeneficaDTO);

    /* if (entidad.getId() == null) {
      entidad.setId(java.util.UUID.randomUUID().toString());
    } */

    EntidadBenefica entidadGuardada = entidadesRepository.save(entidad);

    return donadoresYEntidadesDataMapper.toEntidadDTO(entidadGuardada);
  }

  //IMPLEMENTADO
  @Override
  public EntidadBeneficaDTO buscarEntidadPorID(String entidadID) throws NoSuchElementException {
    EntidadBenefica entidad = entidadesRepository
            .findById(entidadID)
            .orElseThrow(() -> new NoSuchElementException("Entidad no encontrada"));

    return donadoresYEntidadesDataMapper.toEntidadDTO(entidad);
  }

  //IMPLEMENTADO
  @Override
  public NecesidadMaterialDTO registrarNecesidad(NecesidadMaterialDTO necesidadMaterialDTO) {

    if (necesidadMaterialDTO == null) {
      throw new RuntimeException();
    }

    if (necesidadMaterialDTO.id() != null &&
            necesidadesRepository.findById(necesidadMaterialDTO.id()).isPresent()) {
      throw new RuntimeException();
    }

    NecesidadMaterial necesidad =
            donadoresYEntidadesDataMapper.toNecesidad(necesidadMaterialDTO);

    EntidadBenefica entidad = entidadesRepository
            .findById(necesidadMaterialDTO.entidadID())
            .orElseThrow();

    necesidad.setEntidad(entidad);

    entidad.agregarNecesidad(necesidad);

    NecesidadMaterial necesidadGuardada =
            necesidadesRepository.save(necesidad);

    return donadoresYEntidadesDataMapper.toNecesidadDTO(necesidadGuardada);
  }

  //IMPLEMENTADO
  @Override
  public QuejaDTO agregarQueja(String donadorID,
                               QuejaDTO quejaDTO) {

    if (quejaDTO == null) {
      throw new RuntimeException();
    }
    Donador donador = donadoresRepository
            .findById(donadorID)
            .orElse(null);
    //VALIDAR DUPLICADO SOLO POR ID DEL DTO
    if (quejaDTO.id() != null) {
      throw new RuntimeException();
    }
    Queja queja = donadoresYEntidadesDataMapper.toQueja(quejaDTO);

    if (donador == null) {
      throw new RuntimeException();
    }
    queja.setDonador(donador);

    donador.agregarQueja(queja);

    int cantidadQuejas = donador.getQuejas().size();

    if (cantidadQuejas >= 10) {
      donador.setEstado(EstadoDonadorEnum.BANEADO);
    } else if (cantidadQuejas >= 5) {
      donador.setEstado(EstadoDonadorEnum.SOSPECHOSO);
    }

    donadoresRepository.save(donador);

    Queja quejaGuardada = quejasRepository.save(queja);

    return donadoresYEntidadesDataMapper.toQuejaDTO(quejaGuardada);
  }

  //Nuevo necesidades insatisfechas por productoID
  public List<NecesidadMaterialDTO> obtenerNecesidadesInsatisfechas(
          String productoId
  ) {

    return necesidadesRepository.findAll().stream()
            .filter(n -> n.getProductoSolicitadoID().equals(productoId))
            .filter(n -> n.getCantidadActual() < n.getCantidadObjetivo())
            .map(donadoresYEntidadesDataMapper::toNecesidadDTO)
            .toList();
  }

  //IMPLEMENTADO
  public List<NecesidadMaterialDTO> obtenerNecesidadesInsatisfechasDe(String productoSolicitadoID) {

    return necesidadesRepository.findAll()
            .stream()
            .filter(n -> n.getProductoSolicitadoID().equals(productoSolicitadoID))
            .filter(n -> n.getCantidadActual() < n.getCantidadObjetivo())
            .map(donadoresYEntidadesDataMapper::toNecesidadDTO)
            .toList();
  }

  //Parte 2
  public List<DonadorDTO> buscarDonadores() {

    return donadoresRepository.findAll()
            .stream()
            .map(donadoresYEntidadesDataMapper::toDonadorDTO)
            .toList();
  }
  public List<EntidadBeneficaDTO> buscarEntidades() {

    return entidadesRepository.findAll()
            .stream()
            .map(donadoresYEntidadesDataMapper::toEntidadDTO)
            .toList();
  }

  public List<NecesidadMaterialDTO> buscarNecesidades() {

    return necesidadesRepository.findAll()
            .stream()
            .map(donadoresYEntidadesDataMapper::toNecesidadDTO)
            .toList();
  }
}

// asd
