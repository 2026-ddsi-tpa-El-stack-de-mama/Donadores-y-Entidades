package ar.edu.utn.dds.k3003.config;

import ar.edu.utn.dds.k3003.observabilidad.RestTemplateTraceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getInterceptors().add(
                new RestTemplateTraceInterceptor()
        );

        return restTemplate;
    }
}