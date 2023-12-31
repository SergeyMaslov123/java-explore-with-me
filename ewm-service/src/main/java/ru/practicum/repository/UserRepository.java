package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findAllByIdIn(List<Integer> ids, Pageable pageable);

    long countByName(String name);
}
