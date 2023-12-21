package ru.practicum.dto.categoriesDto;

import ru.practicum.model.Categories;

public class CategoriesMapper {
    public static CategoriesDto toCategoriesDto(Categories categories) {
        return new CategoriesDto(categories.getId(), categories.getName());
    }

    public static Categories toCategories(CategoriesDto categoriesDto) {
        return new Categories(categoriesDto.getId(), categoriesDto.getName());
    }

    public static Categories toCategoryFromNewCategoryDto(NewCategoryDto categoryDto) {
        return new Categories(null, categoryDto.getName());
    }
}
