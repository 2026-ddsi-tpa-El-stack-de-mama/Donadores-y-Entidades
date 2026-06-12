package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum;
import ar.edu.utn.dds.k3003.model.Donador;
import ar.edu.utn.dds.k3003.model.EntidadBenefica;
import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import ar.edu.utn.dds.k3003.model.Queja;
import ar.edu.utn.dds.k3003.repositories.DonadoresRepository;
import ar.edu.utn.dds.k3003.repositories.EntidadesRepository;
import ar.edu.utn.dds.k3003.repositories.NecesidadesRepository;
import ar.edu.utn.dds.k3003.repositories.QuejasRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DatosDemoService {

    private final DonadoresRepository donadoresRepository;
    private final EntidadesRepository entidadesRepository;
    private final NecesidadesRepository necesidadesRepository;
    private final QuejasRepository quejasRepository;

    public DatosDemoService(
            DonadoresRepository donadoresRepository,
            EntidadesRepository entidadesRepository,
            NecesidadesRepository necesidadesRepository,
            QuejasRepository quejasRepository) {

        this.donadoresRepository = donadoresRepository;
        this.entidadesRepository = entidadesRepository;
        this.necesidadesRepository = necesidadesRepository;
        this.quejasRepository = quejasRepository;
    }

    public void resetearBase() {

        limpiarBase();

        cargarEntidadesDemo();
        cargarDonadoresDemo();
        cargarNecesidadesDemo();
        cargarQuejasDemo();
    }

    private void limpiarBase() {

        quejasRepository.deleteAll();
        necesidadesRepository.deleteAll();
        entidadesRepository.deleteAll();
        donadoresRepository.deleteAll();
    }

    private void cargarEntidadesDemo() {

        EntidadBenefica e1 = new EntidadBenefica();
        e1.setRazonSocial("Comedor Los Pibes");
        e1.setDomicilio("Av. Siempre Viva 123");
        e1.setTelefono("1122334455");
        e1.setCorreo("comedor@ejemplo.com");

        EntidadBenefica e2 = new EntidadBenefica();
        e2.setRazonSocial("Hogar San Jose");
        e2.setDomicilio("Mitre 456");
        e2.setTelefono("1144556677");
        e2.setCorreo("hogar@ejemplo.com");

        entidadesRepository.save(e1);
        entidadesRepository.save(e2);

    }

    private void cargarDonadoresDemo() {

        Donador d1 = new Donador(
                "Juan",
                "Perez",
                30,
                "juan@mail.com",
                "12345678",
                "Calle 123");

        Donador d2 = new Donador(
                "Maria",
                "Gomez",
                45,
                "maria@mail.com",
                "23456789",
                "Av Siempre Viva 742");

        Donador d3 = new Donador(
                "Carlos",
                "Lopez",
                38,
                "carlos@mail.com",
                "34567890",
                "Belgrano 500");

        donadoresRepository.save(d1);
        donadoresRepository.save(d2);
        donadoresRepository.save(d3);

    }

    private void cargarNecesidadesDemo() {

        List<EntidadBenefica> entidades = entidadesRepository.findAll();

        if (entidades.size() < 2) {
            return;
        }

        NecesidadMaterial n1 = new NecesidadMaterial();
        n1.setEntidad(entidades.get(0));
        n1.setNivelDeUrgencia(8);
        n1.setDescripcion("Leche para comedor");
        n1.setCantidadObjetivo(100);
        n1.setCantidadActual(0);
        n1.setProductoSolicitadoID("LECHE-001");
        n1.setTipo(TipoNecesidadMaterialEnum.EXTRAORDINARIA);

        NecesidadMaterial n2 = new NecesidadMaterial();
        n2.setEntidad(entidades.get(1));
        n2.setNivelDeUrgencia(5);
        n2.setDescripcion("Fideos secos");
        n2.setCantidadObjetivo(200);
        n2.setCantidadActual(20);
        n2.setProductoSolicitadoID("FIDEO-001");
        n2.setTipo(TipoNecesidadMaterialEnum.RECURRENTE);

        necesidadesRepository.save(n1);
        necesidadesRepository.save(n2);

    }

    private void cargarQuejasDemo() {

        List<Donador> donadores = donadoresRepository.findAll();

        Donador donador4 = donadores.get(1);
        Donador donador9 = donadores.get(2);

        for (int i = 1; i <= 4; i++) {

            Queja q = new Queja();

            q.setDescripcion("Queja demo " + i);
            q.setDonacionID("DON-" + i);
            q.setFecha(LocalDate.now());
            q.setDonador(donador4);

            donador4.agregarQueja(q);

            quejasRepository.save(q);
        }

        for (int i = 1; i <= 9; i++) {

            Queja q = new Queja();

            q.setDescripcion("Queja demo baneado " + i);
            q.setDonacionID("BAN-" + i);
            q.setFecha(LocalDate.now());
            q.setDonador(donador9);

            donador9.agregarQueja(q);

            quejasRepository.save(q);
        }

        donador4.setEstado(EstadoDonadorEnum.VERIFICADO);

        donador9.setEstado(EstadoDonadorEnum.SOSPECHOSO);

        donadoresRepository.save(donador4);
        donadoresRepository.save(donador9);

    }
}