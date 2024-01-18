package ru.practicum.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.StatClient;

@Configuration
public class ApplicationConfig {
    @Bean
    public StatClient statClient() {
        return new StatClient("http://stats-server:9090", new RestTemplateBuilder());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().uriTemplateHandler(new DefaultUriBuilderFactory("http://stats-server:9090"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    @Bean
    public RestTemplateBuilder builder() {
        return new RestTemplateBuilder();
    }


}
