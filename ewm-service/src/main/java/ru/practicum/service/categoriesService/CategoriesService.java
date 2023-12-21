package ru.practicum.service.categoriesService;

import ru.practicum.dto.categoriesDto.CategoriesDto;
import ru.practicum.dto.categoriesDto.NewCategoryDto;

import java.util.List;

public interface CategoriesService {
    List<CategoriesDto> getCategories(Integer from, Integer size);

    CategoriesDto getCategoriesById(Integer id);

    CategoriesDto addNewCategoryAdmin(NewCategoryDto categoryDto);

    void deleteCategoryAdmin(Integer catId);

    CategoriesDto updateCategoryAdmin(Integer catId, CategoriesDto categoriesDto);
}
