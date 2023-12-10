package ru.practicum.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ViewStat;
import ru.practicum.hit.Hit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface RepositoryHit extends JpaRepository<Hit, Long> {
    @Query("SELECT new ru.practicum.ViewStat(h.app, h.uri, COUNT(h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStat> getStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ViewStat(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ViewStat> getUniqueStat(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ViewStat(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND h.uri IN ?3 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ViewStat> getUniqueStatByUri(LocalDateTime start, LocalDateTime end, Set<String> uris);

    @Query("SELECT new ru.practicum.ViewStat(h.app, h.uri, COUNT(h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND h.uri IN ?3 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStat> getStatByUri(LocalDateTime start, LocalDateTime end, Set<String> uris);
}
