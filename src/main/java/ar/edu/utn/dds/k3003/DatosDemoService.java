package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.repositories.DonadoresRepository;
import ar.edu.utn.dds.k3003.repositories.EntidadesRepository;
import ar.edu.utn.dds.k3003.repositories.NecesidadesRepository;
import ar.edu.utn.dds.k3003.repositories.QuejasRepository;
import org.springframework.stereotype.Service;

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
    public void resetearBase() { }
}