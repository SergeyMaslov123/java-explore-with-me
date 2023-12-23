package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ViewStat;
import ru.practicum.HitDto;
import ru.practicum.hit.Hit;
import ru.practicum.hit.HitMapper;
import ru.practicum.hit.ValidEx;
import ru.practicum.rep.RepositoryHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ServiceHitImpl implements ServiceHit {
    private final RepositoryHit repositoryHit;

    @Override
    public HitDto addHit(HitDto hitDto) {
        Hit hit = HitMapper.toHitFromDto(hitDto);
        return HitMapper.toHitDtoFromHit(repositoryHit.save(hit));
    }

    @Override
    public List<ViewStat> getHit(String start, String end, List<String> urisList, Boolean unique) {
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (startDate.isAfter(endDate)) {
            throw new ValidEx("start if after end");
        }
        if (urisList == null || urisList.isEmpty()) {
            if (unique) {
                return repositoryHit.getUniqueStat(startDate, endDate);
            } else {
                return repositoryHit.getStats(startDate, endDate);
            }
        } else {
            Set<String> uris = new HashSet<>(urisList);
            if (unique) {
                return repositoryHit.getUniqueStatByUri(startDate, endDate, uris);
            } else {
                return repositoryHit.getStatByUri(startDate, endDate, uris);
            }
        }
    }
}
