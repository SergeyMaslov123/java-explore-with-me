package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Integer> {

    Boolean existsByName(String name);

}