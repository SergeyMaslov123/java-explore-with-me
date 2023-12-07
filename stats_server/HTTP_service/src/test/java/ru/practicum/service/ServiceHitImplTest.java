package ru.practicum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.HitDto;
import ru.practicum.ViewStat;
import ru.practicum.hit.Hit;
import ru.practicum.hit.HitMapper;
import ru.practicum.rep.RepositoryHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceHitImplTest {
    @Mock
    private RepositoryHit repositoryHit;
    @InjectMocks
    private ServiceHitImpl serviceHit;

    HitDto hitDto = new HitDto(
            "yandex.ru",
            "www.yandex.ru",
            "192.167.23.12",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    );
    Hit hit = HitMapper.toHitFromDto(hitDto);
    String startString = LocalDateTime.now().minusSeconds(1000).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    String endString = LocalDateTime.now().plusSeconds(100).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    List<String> uris = List.of("123.34.4", "233.44.55");

    LocalDateTime start = LocalDateTime.parse(startString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    LocalDateTime end = LocalDateTime.parse(endString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


    @Test
    void addHit() {
        when(repositoryHit.save(hit)).thenReturn(hit);
        HitDto actualHitDto = serviceHit.addHit(hitDto);

        assertEquals(hitDto.getIp(), actualHitDto.getIp());

    }

    @Test
    void getHitWithUrisNullUnique() {
        List<ViewStat> stats = List.of(new ViewStat("www", "qqq", 2L));

        when(repositoryHit.getUniqueStat(start, end)).thenReturn(stats);

        List<ViewStat> actualStats = serviceHit.getHit(startString, endString, null, true);

        assertEquals(stats.size(), actualStats.size());

    }

    @Test
    void getHitWithUrisNullUniqueFalse() {
        List<ViewStat> stats = List.of(new ViewStat("www", "qqq", 2L));

        when(repositoryHit.getStats(start, end)).thenReturn(stats);

        List<ViewStat> actualStats = serviceHit.getHit(startString, endString, null, false);

        assertEquals(stats.size(), actualStats.size());

    }

    @Test
    void getHitWithUrisUnique() {
        Set<String> urisSet = Set.of("123.34.4", "233.44.55");
        List<ViewStat> stats = List.of(new ViewStat("www", "qqq", 2L));

        when(repositoryHit.getUniqueStatByUri(start, end, urisSet)).thenReturn(stats);

        List<ViewStat> actualStats = serviceHit.getHit(startString, endString, uris, true);

        assertEquals(stats.size(), actualStats.size());

    }

    @Test
    void getHitWithUrisUniqueFalse() {
        Set<String> urisSet = Set.of("123.34.4", "233.44.55");
        List<ViewStat> stats = List.of(new ViewStat("www", "qqq", 2L));

        when(repositoryHit.getStatByUri(start, end, urisSet)).thenReturn(stats);

        List<ViewStat> actualStats = serviceHit.getHit(startString, endString, uris, false);

        assertEquals(stats.size(), actualStats.size());

    }
}