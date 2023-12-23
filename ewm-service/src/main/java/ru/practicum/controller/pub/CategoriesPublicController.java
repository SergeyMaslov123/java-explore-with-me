package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.categoriesDto.CategoriesDto;
import ru.practicum.service.categoriesService.CategoriesService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoriesPublicController {
    private final CategoriesService categoriesService;

    @GetMapping
    List<CategoriesDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return categoriesService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    CategoriesDto getCategoriesById(@PathVariable @Positive Integer catId) {
        return categoriesService.getCategoriesById(catId);
    }
}