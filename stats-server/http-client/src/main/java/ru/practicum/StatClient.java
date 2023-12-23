package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.Map;

@Component
public class StatClient {
    private final RestTemplate restTemplate;

    @Autowired
    public StatClient(@Value("${stat-service.url}") String statServiceUrl, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(statServiceUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> addHit(HitDto hitDto) {
        try {
            return restTemplate.postForEntity("/hit", hitDto, Object.class);
        } catch (HttpStatusCodeException ex) {
            throw new RuntimeException("Can`t add Hit" + ex.getMessage());
        }
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        try {
            return restTemplate.getForEntity("/stats", Object.class, params);
        } catch (HttpStatusCodeException ex) {
            throw new RuntimeException("Can`t get stats" + ex.getMessage());
        }
    }
}
