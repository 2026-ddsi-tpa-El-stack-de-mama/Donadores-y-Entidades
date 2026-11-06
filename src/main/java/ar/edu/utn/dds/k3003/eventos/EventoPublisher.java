package ar.edu.utn.dds.k3003.eventos;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.model.Donador;
import ar.edu.utn.dds.k3003.model.EntidadBenefica;
import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import ar.edu.utn.dds.k3003.model.Queja;
import ar.edu.utn.dds.k3003.repositories.DonadoresRepository;
import ar.edu.utn.dds.k3003.repositories.DonadoresYEntidadesDataMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class EventoPublisher {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DonadoresYEntidadesDataMapper donadoresYEntidadesDataMapper;

    public EventoPublisher() {
        donadoresYEntidadesDataMapper = new DonadoresYEntidadesDataMapper();
    }

    public void publicar(String tipoEvento, Object descripcion) {

        Map<String, String> env = System.getenv();

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(env.get("E_QUEUE_HOST"));
        factory.setUsername(env.get("E_QUEUE_USERNAME"));
        factory.setPassword(env.get("E_QUEUE_PASSWORD"));
        factory.setVirtualHost(env.get("E_QUEUE_USERNAME"));

        String queueName = env.get("E_QUEUE_NAME");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(
                    queueName,
                    true,
                    false,
                    false,
                    null
            );

            EventoQueue evento = new EventoQueue(
                    UUID.randomUUID().toString(),
                    tipoEvento,
                    descripcion
            );

            byte[] body = objectMapper.writeValueAsBytes(evento);

            AMQP.BasicProperties props =
                    new AMQP.BasicProperties.Builder()
                            .headers(new HashMap<>())
                            .deliveryMode(2)
                            .build();

            channel.basicPublish(
                    "",
                    queueName,
                    props,
                    body
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "No se pudo publicar el evento en RabbitMQ",
                    e
            );
        }
    }
    public void publicarDonador(Donador donador) {

        DonadorEvento evento = new DonadorEvento(
                donador.getId(),
                donador.getNombre(),
                donador.getEstado(),
                donador.getCategoria(),
                Collections.singletonList(donadoresYEntidadesDataMapper.toQuejaDTO((Queja) donador.getQuejas()))
        );

        publicar("DonadorEvento", evento);
    }

    public void publicarEntidad(EntidadBenefica entidad) {

        EntidadBeneficaDTO evento = donadoresYEntidadesDataMapper.toEntidadDTO(entidad);

        publicar("EntidadEvento", evento);
    }
    public void publicarNecesidad(NecesidadMaterial necesidad) {
        NecesidadEvento evento = new NecesidadEvento(
                necesidad.getId(),
                necesidad.getNivelDeUrgencia(),
                necesidad.getTipo(),
                donadoresYEntidadesDataMapper.toEntidadDTO(necesidad.getEntidad()),
                (necesidad.getCantidadObjetivo()) - necesidad.getCantidadActual());

        publicar("NecesidadEvento", evento);
    }

    public void publicarQueja(Queja queja) {

        DonadorDTO evento = donadoresYEntidadesDataMapper.toDonadorDTO(queja.getDonador());

        publicar("QuejaEvento", evento);
    }
}