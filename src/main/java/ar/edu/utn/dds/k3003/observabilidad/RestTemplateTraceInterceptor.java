package ar.edu.utn.dds.k3003.observabilidad;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Solo para modulos que llaman con RestTemplate (ej. Donadores y Entidades).
 * Un {@code new RestTemplate()} creado dentro de un metodo NO lo usa: hay que declarar un bean
 * RestTemplate y registrar este interceptor:
 * <pre>
 *   RestTemplate rt = new RestTemplate();
 *   rt.getInterceptors().add(new RestTemplateTraceInterceptor());
 * </pre>
 */
public class RestTemplateTraceInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        String traceId = TraceContext.traceId();
        if (traceId != null) {
            request.getHeaders().set(TraceContext.TRACE_HEADER, traceId);
        }
        return execution.execute(request, body);
    }
}