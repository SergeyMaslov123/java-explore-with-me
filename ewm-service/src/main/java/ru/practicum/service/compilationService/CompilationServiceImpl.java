package ru.practicum.service.compilationService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilationDto.CompilationDto;
import ru.practicum.dto.compilationDto.CompilationMapper;
import ru.practicum.dto.compilationDto.NewCompilationDto;
import ru.practicum.dto.compilationDto.UpdateCompilationRequest;
import ru.practicum.dto.eventDto.EventMapper;
import ru.practicum.dto.eventDto.EventShotDto;
import ru.practicum.exception.EntityNotFoundEx;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto addCompilationAdmin(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilationFromNewCompDto(newCompilationDto);
        if (newCompilationDto.getEvents() != null) {
            Set<Event> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        Compilation savedCompilation = compilationRepository.save(compilation);
        CompilationDto compilationDto = CompilationMapper.toCompilationDtoFromComp(savedCompilation);
        if (savedCompilation.getEvents() != null) {
            Set<EventShotDto> eventShots = savedCompilation.getEvents().stream()
                    .map(EventMapper::eventShotDtoFromEvent)
                    .collect(Collectors.toSet());
            compilationDto.setEvents(eventShots);
        }
        return compilationDto;
    }

    @Override
    public void deleteCompilationAdmin(Integer compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new EntityNotFoundEx("compilation not found"));
        compilationRepository.delete(compilation);


    }

    @Override
    public CompilationDto updateCompilationAdmin(Integer compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundEx("compilation not found"));
        if (updateCompilationRequest.getEvents() != null) {
            Set<Event> events = eventRepository.findAllByIdIn(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        Compilation savedCompilation = compilationRepository.save(compilation);
        CompilationDto compilationDto = CompilationMapper.toCompilationDtoFromComp(savedCompilation);
        Set<EventShotDto> eventShots = savedCompilation.getEvents().stream()
                .map(EventMapper::eventShotDtoFromEvent)
                .collect(Collectors.toSet());
        compilationDto.setEvents(eventShots);
        return compilationDto;
    }

    @Override
    public List<CompilationDto> getCompilationsPublic(Boolean pinned, Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new RuntimeException("некорректный запрос from, size");
        }
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        return compilationRepository.findAllByPinned(pinned, pageable).stream()
                .map(compilation -> {
                    CompilationDto compilationDto = CompilationMapper.toCompilationDtoFromComp(compilation);
                    Set<EventShotDto> eventShots = compilation.getEvents().stream()
                            .map(EventMapper::eventShotDtoFromEvent)
                            .collect(Collectors.toSet());
                    compilationDto.setEvents(eventShots);
                    return compilationDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationByIdPublic(Integer compId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundEx("comp not found"));
        CompilationDto compilationDto = CompilationMapper.toCompilationDtoFromComp(compilation);
        Set<EventShotDto> eventShots = compilation.getEvents()
                .stream()
                .map(EventMapper::eventShotDtoFromEvent)
                .collect(Collectors.toSet());
        compilationDto.setEvents(eventShots);
        return compilationDto;
    }
}
