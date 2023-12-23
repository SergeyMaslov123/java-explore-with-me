package ru.practicum.dto.compilationDto;

import ru.practicum.model.Compilation;

public class CompilationMapper {
    public static Compilation toCompilationFromNewCompDto(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getPinned() == null) {
            return new Compilation(
                    null,
                    null,
                    false,
                    newCompilationDto.getTitle()
            );
        } else {
            return new Compilation(
                    null,
                    null,
                    newCompilationDto.getPinned(),
                    newCompilationDto.getTitle()
            );
        }
    }

    public static Compilation toCompilationFromUpdateComp(UpdateCompilationRequest updateCompilationRequest) {
        return new Compilation(
                null,
                null,
                updateCompilationRequest.getPinned(),
                updateCompilationRequest.getTitle());
    }

    public static CompilationDto toCompilationDtoFromComp(Compilation compilation) {
        return new CompilationDto(
                null,
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle());
    }
}
