package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.*;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaIncentivos;
import ar.edu.utn.dds.k3003.exceptions.DonadorNoEncontradoException;
import ar.edu.utn.dds.k3003.exceptions.DonadorYaExistenteException;
import ar.edu.utn.dds.k3003.model.*;
import ar.edu.utn.dds.k3003.repositories.*;

import java.util.*;

import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum.RECURRENTE;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.MisionResponseDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.InsigniasResponseDTO;
import ar.edu.utn.dds.k3003.model.HistorialEstadoDonador;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.HistorialEstadoDonadorDTO;
import org.springframework.web.server.ResponseStatusException;

@Service
public class Fachada implements FachadaDonadoresYEntidades {


  private DonadoresYEntidadesDataMapper donadoresYEntidadesDataMapper =
      new DonadoresYEntidadesDataMapper();
  private DonadoresRepository donadoresRepository;
  private EntidadesRepository entidadesRepository;
  private NecesidadesRepository necesidadesRepository;
  private QuejasRepository quejasRepository;

  private FachadaIncentivos fachadaIncentivos;
  private HistorialEstadoDonadorRepository historialEstadoDonadorRepository;

  public Fachada(DonadoresRepository donadoresRepository,
                 EntidadesRepository entidadesRepository,
                 NecesidadesRepository necesidadesRepository,
                 QuejasRepository quejasRepository,
                 HistorialEstadoDonadorRepository historialEstadoDonadorRepository) {
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
    this.historialEstadoDonadorRepository = historialEstadoDonadorRepository;
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

  /* //IMPLEMENTADO
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
  } */
  @Override
  public DonadorDTO modificarEstado(String donadorID, EstadoDonadorEnum estado) {

    Donador donador = donadoresRepository.findById(donadorID).orElse(null);

    if (donador == null) {
      throw new RuntimeException();
    }

    if (estado == null) {
      throw new RuntimeException();
    }

    EstadoDonadorEnum estadoAnterior = donador.getEstado();

    donador.setEstado(estado);

    Donador donadorGuardado = donadoresRepository.save(donador);

    HistorialEstadoDonador historial = new HistorialEstadoDonador(
            donadorGuardado,
            estadoAnterior,
            estado
    );

    historialEstadoDonadorRepository.save(historial);

    return donadoresYEntidadesDataMapper.toDonadorDTO(donadorGuardado);
  }

  public List<HistorialEstadoDonadorDTO> obtenerHistorialEstado(String donadorID) {

    Donador donador = donadoresRepository.findById(donadorID).orElse(null);

    if (donador == null) {
      throw new RuntimeException();
    }

    List<HistorialEstadoDonador> historial =
            historialEstadoDonadorRepository.findByDonadorId(donadorID);

    return historial.stream()
            .map(h -> new HistorialEstadoDonadorDTO(
                    h.getEstadoAnterior(),
                    h.getEstadoNuevo(),
                    h.getFechaCambio()
            ))
            .toList();
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

  // NUEVO - TP3 corregido por cambio de respuesta de Incentivos
  @Override
  public DonadorStatsDTO estadisticasDonador(String donadorID) {

    Donador donador = donadoresRepository
            .findById(donadorID)
            .orElseThrow(() ->
                    new NoSuchElementException("Donador no encontrado"));

    RestTemplate restTemplate = new RestTemplate();

    String baseUrl =
            "https://entrega-2-cesartomasg.onrender.com";

    String misionActualID = null;
    List<String> insigniasID = new ArrayList<>();

    // =========================
    // Obtener misión actual
    // =========================
    try {

      MisionResponseDTO response =
              restTemplate.getForObject(
                      baseUrl
                              + "/misiones/donadores/"
                              + donadorID
                              + "/mision",
                      MisionResponseDTO.class
              );

      if (response != null && response.data() != null) {
        misionActualID = response.data().id();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }


    // =========================
    // Obtener insignias
    // =========================
    try {

      Map<String, Object> response =
              restTemplate.getForObject(
                      baseUrl
                              + "/insignias/donadores/"
                              + donadorID,
                      Map.class
              );

      System.out.println("RESPUESTA DE INSIGNIAS:");
      System.out.println(response);

      if (response != null && response.get("data") != null) {

        List<Map<String, Object>> data =
                (List<Map<String, Object>>) response.get("data");

        for (Map<String, Object> insignia : data) {

          Object id = insignia.get("id");

          if (id != null) {
            insigniasID.add(id.toString());
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }


    // =========================
    // Armar estadísticas
    // =========================
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

  /* //IMPLEMENTADO
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
  } */

  @Override
  public NecesidadMaterialDTO registrarNecesidad(
          NecesidadMaterialDTO necesidadMaterialDTO) {

    if (necesidadMaterialDTO == null) {
      throw new RuntimeException();
    }

    if (necesidadMaterialDTO.id() != null &&
            necesidadesRepository.findById(necesidadMaterialDTO.id()).isPresent()) {
      throw new RuntimeException();
    }

    String productoID = necesidadMaterialDTO.productoSolicitadoID();

    Integer cantidadObjetivo = necesidadMaterialDTO.cantidadObjetivo();

    if (productoID == null || cantidadObjetivo == null || cantidadObjetivo <= 0) {
      throw new RuntimeException("Producto o cantidad inválida");
    }

    RestTemplate restTemplate = new RestTemplate();

    // =====================================================
    // 1. VALIDAR QUE EL PRODUCTO EXISTA EN DONACIONES
    // =====================================================

    String urlProducto =
            "https://donaciones-ctwj.onrender.com/productos/"
                    + productoID;

    try {
      restTemplate.getForObject(urlProducto, String.class);
    } catch (Exception e) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              "El producto solicitado no existe en Donaciones"
      );
    }

    // =====================================================
    // 2. CONSULTAR STOCK EN LOGÍSTICA
    // =====================================================

    String urlStock =
            "https://logistica-jc94.onrender.com/stock/"
                    + productoID;

    Integer stockDisponible;

    try {
      stockDisponible =
              restTemplate.getForObject(urlStock, Integer.class);

    } catch (Exception e) {
      throw new RuntimeException(
              "No se pudo consultar el stock en Logística"
      );
    }

    if (stockDisponible == null) {
      stockDisponible = 0;
    }

    // =====================================================
    // 3. CREAR LA NECESIDAD
    // =====================================================

    NecesidadMaterial necesidad =
            donadoresYEntidadesDataMapper.toNecesidad(necesidadMaterialDTO);

    EntidadBenefica entidad = entidadesRepository
            .findById(necesidadMaterialDTO.entidadID())
            .orElseThrow();

    necesidad.setEntidad(entidad);
    entidad.agregarNecesidad(necesidad);

    // =====================================================
    // 4. CALCULAR CUÁNTO SE PUEDE ASIGNAR
    // =====================================================

    int cantidadAsignada =
            Math.min(cantidadObjetivo, stockDisponible);

    // La cantidad actual empieza con lo que había disponible
    // en Logística.
    necesidad.setCantidadActual(cantidadAsignada);

    // =====================================================
    // 5. RESTAR DEL STOCK DE LOGÍSTICA
    // =====================================================

    if (cantidadAsignada > 0) {

      try {

        restTemplate.postForObject(
                urlStock,
                cantidadAsignada,
                Integer.class
        );

      } catch (Exception e) {

        throw new RuntimeException(
                "No se pudo descontar el stock en Logística"
        );
      }
    }

    // =====================================================
    // 6. GUARDAR LA NECESIDAD
    // =====================================================

    NecesidadMaterial necesidadGuardada =
            necesidadesRepository.save(necesidad);

    return donadoresYEntidadesDataMapper
            .toNecesidadDTO(necesidadGuardada);
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

  public EntidadBeneficaDTO modificarEntidad(
          String entidadID,
          EntidadBeneficaDTO entidadDTO) {

    if (entidadDTO == null) {
      throw new IllegalArgumentException("La entidad no puede ser null");
    }

    EntidadBenefica entidad = entidadesRepository
            .findById(entidadID)
            .orElseThrow(() ->
                    new NoSuchElementException("Entidad no encontrada"));

    entidad.setRazonSocial(entidadDTO.razonSocial());
    entidad.setDomicilio(entidadDTO.domicilio());
    entidad.setTelefono(entidadDTO.telefono());
    entidad.setCorreo(entidadDTO.correo());

    EntidadBenefica entidadActualizada =
            entidadesRepository.save(entidad);

    return donadoresYEntidadesDataMapper
            .toEntidadDTO(entidadActualizada);
  }

  //TP 4
  public NecesidadMaterialDTO buscarNecesidadPorID(String necesidadID)
          throws NoSuchElementException {

    NecesidadMaterial necesidad = necesidadesRepository
            .findById(necesidadID)
            .orElseThrow(() ->
                    new NoSuchElementException("Necesidad no encontrada"));

    return donadoresYEntidadesDataMapper
            .toNecesidadDTO(necesidad);
  }

  public NecesidadMaterialDTO modificarNecesidad(
          String necesidadID,
          NecesidadMaterialDTO necesidadDTO) {

    if (necesidadDTO == null) {
      throw new IllegalArgumentException(
              "La necesidad no puede ser null"
      );
    }

    NecesidadMaterial necesidad = necesidadesRepository
            .findById(necesidadID)
            .orElseThrow(() ->
                    new NoSuchElementException(
                            "Necesidad no encontrada"
                    ));

    necesidad.setProductoSolicitadoID(
            necesidadDTO.productoSolicitadoID()
    );

    necesidad.setCantidadObjetivo(
            necesidadDTO.cantidadObjetivo()
    );

    necesidad.setCantidadActual(
            necesidadDTO.cantidadActual()
    );

    necesidad.setTipo(
            necesidadDTO.tipo()
    );

    NecesidadMaterial necesidadActualizada =
            necesidadesRepository.save(necesidad);

    return donadoresYEntidadesDataMapper
            .toNecesidadDTO(necesidadActualizada);
  }

  public void eliminarNecesidad(String necesidadID) {

    NecesidadMaterial necesidad = necesidadesRepository
            .findById(necesidadID)
            .orElseThrow(() ->
                    new NoSuchElementException(
                            "Necesidad no encontrada"
                    ));

    necesidadesRepository.delete(necesidad);
  }
}


