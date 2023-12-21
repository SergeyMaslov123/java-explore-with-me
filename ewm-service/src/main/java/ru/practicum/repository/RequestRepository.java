package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByRequesterId(Integer userId);

    Boolean existsByRequester_idAndEvent_id(Integer userId, Integer eventId);

    List<Request> findAllByIdIn(List<Integer> ids);

    List<Request> findAllByEvent_id(Integer eventId);

}
