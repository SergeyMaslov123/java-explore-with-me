package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.HitDto;
import ru.practicum.ViewStat;
import ru.practicum.service.ServiceHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ControllerHitTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ServiceHit serviceHit;

    @SneakyThrows
    @Test
    void addHit() {
        HitDto hitDto = new HitDto(
                "yandex",
                "www",
                "192:23:23",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        when(serviceHit.addHit(hitDto)).thenReturn(hitDto);
        String result = mockMvc.perform(post("/hit")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(hitDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(hitDto), result);
    }

    @SneakyThrows
    @Test
    void getHitWithUri() {
        String start = LocalDateTime.now().toString();
        String end = LocalDateTime.now().plusHours(1).toString();
        String uris = "www,rtr";
        List<String> urisList = List.of("www", "rtr");
        Boolean unique = true;
        List<ViewStat> stats = List.of(new ViewStat("www", "wewe", 2L));

        when(serviceHit.getHit(start, end, urisList, unique)).thenReturn(stats);

        mockMvc.perform(get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", start, end, uris, unique))
                .andExpect(status().isOk());
        verify(serviceHit).getHit(start, end, urisList, unique);

    }

    @Test
    @SneakyThrows
    void getHitWithUriNot() {
        String start = LocalDateTime.now().toString();
        String end = LocalDateTime.now().plusHours(1).toString();
        List<String> uris = List.of();

        Boolean unique = true;
        List<ViewStat> stats = List.of(new ViewStat("www", "wewe", 2L));

        when(serviceHit.getHit(start, end, uris, unique)).thenReturn(stats);

        mockMvc.perform(get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", start, end, null, unique))
                .andExpect(status().isOk());
        verify(serviceHit).getHit(start, end, uris, unique);

    }
}