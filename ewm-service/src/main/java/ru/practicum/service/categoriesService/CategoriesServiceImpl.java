package ru.practicum.service.categoriesService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.categoriesDto.CategoriesDto;
import ru.practicum.dto.categoriesDto.CategoriesMapper;
import ru.practicum.dto.categoriesDto.NewCategoryDto;
import ru.practicum.exception.ConflictEx;
import ru.practicum.exception.EntityNotFoundEx;
import ru.practicum.model.Categories;
import ru.practicum.repository.CategoriesRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CategoriesDto> getCategories(Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new RuntimeException("некорректный запрос from, size");
        }
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        return categoriesRepository.findAll(pageable).stream()
                .map(CategoriesMapper::toCategoriesDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoriesDto getCategoriesById(Integer id) {
        return CategoriesMapper.toCategoriesDto(categoriesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundEx("categories not found")));
    }

    @Override
    public CategoriesDto addNewCategoryAdmin(NewCategoryDto categoryDto) {
        validateName(categoryDto.getName());
        return CategoriesMapper.toCategoriesDto(categoriesRepository.save(CategoriesMapper.toCategoryFromNewCategoryDto(categoryDto)));
    }

    @Override
    public void deleteCategoryAdmin(Integer catId) {
        Categories categories = categoriesRepository.findById(catId).orElseThrow(() -> new EntityNotFoundEx("The required object was not found."));
        if (eventRepository.existsByCategory_Id(catId)) {
            throw new ConflictEx("event have category ");
        }
        categoriesRepository.deleteById(catId);
    }

    @Override
    public CategoriesDto updateCategoryAdmin(Integer catId, CategoriesDto categoriesDto) {
        Categories categories = categoriesRepository.findById(catId).orElseThrow(() -> new EntityNotFoundEx("The required object was not found."));
        if (!categories.getName().equals(categoriesDto.getName())) {
            validateName(categoriesDto.getName());
        }
        categories.setName(categoriesDto.getName());
        return CategoriesMapper.toCategoriesDto(categoriesRepository.save(categories));
    }

    private void validateName(String name) {
        if (categoriesRepository.existsByName(name)) {
            throw new ConflictEx("name is busy");
        }
    }

}
