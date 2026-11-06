package ar.edu.utn.dds.k3003.eventos;

import ar.edu.utn.dds.k3003.model.Donador;
import ar.edu.utn.dds.k3003.model.EntidadBenefica;
import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import ar.edu.utn.dds.k3003.model.Queja;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class EventoPublisher {

    private final ObjectMapper objectMapper = new ObjectMapper();

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
                donador.getApellido(),
                donador.getEdad(),
                donador.getEmail(),
                donador.getNroDocumento(),
                donador.getDomicilio(),
                donador.getEstado().name(),
                donador.getCategoria()
        );

        publicar("DonadorEvento", evento);
    }
    public void publicarEntidad(EntidadBenefica entidad) {

        EntidadEvento evento = new EntidadEvento(
                entidad.getId(),
                entidad.getRazonSocial(),
                entidad.getDomicilio(),
                entidad.getTelefono(),
                entidad.getCorreo()
        );

        publicar("EntidadEvento", evento);
    }
    public void publicarNecesidad(NecesidadMaterial necesidad) {

        NecesidadEvento evento = new NecesidadEvento(
                necesidad.getId(),
                necesidad.getEntidad() != null
                        ? necesidad.getEntidad().getId()
                        : null,
                necesidad.getNivelDeUrgencia(),
                necesidad.getDescripcion(),
                necesidad.getCantidadObjetivo(),
                necesidad.getCantidadActual(),
                necesidad.getProductoSolicitadoID(),
                necesidad.getTipo() != null
                        ? necesidad.getTipo().name()
                        : null
        );

        publicar("NecesidadEvento", evento);
    }
    public void publicarQueja(Queja queja) {

        QuejaEvento evento = new QuejaEvento(
                queja.getId(),
                queja.getDonacionID(),
                queja.getDonador() != null
                        ? queja.getDonador().getId()
                        : null,
                queja.getFecha(),
                queja.getDescripcion()
        );

        publicar("QuejaEvento", evento);
    }
}