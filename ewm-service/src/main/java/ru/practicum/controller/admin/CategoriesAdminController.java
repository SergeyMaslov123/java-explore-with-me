package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.categoriesDto.CategoriesDto;
import ru.practicum.dto.categoriesDto.NewCategoryDto;
import ru.practicum.service.categoriesService.CategoriesService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoriesAdminController {
    private final CategoriesService categoriesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoriesDto addCategories(@RequestBody @Valid NewCategoryDto categoryDto) {
        return categoriesService.addNewCategoryAdmin(categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable Integer catId) {
        categoriesService.deleteCategoryAdmin(catId);
    }

    @PatchMapping("/{catId}")
    CategoriesDto updateCategory(@PathVariable Integer catId,
                                 @RequestBody @Valid CategoriesDto categoriesDto) {
        return categoriesService.updateCategoryAdmin(catId, categoriesDto);
    }

}
