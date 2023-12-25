package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Event;
import ru.practicum.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Set<Event> findAllByIdIn(Set<Integer> ids);

    Page<Event> findAllByInitiator_id(Integer id, Pageable pageable);

    @Query("Select e from Event e where ((:text) IS NULL or (upper(e.annotation) like upper(concat('%',:text, '%'))" +
            " or upper(e.description) like upper(concat('%', :text, '%')))) " +
            "and e.state = 'PUBLISHED' " +
            "and ((:paid) IS NULL or e.paid = :paid) " +
            "and ((:categories) IS NULL or e.category.id in :categories) " +
            "and e.eventDate between :start and :end " +
            "and (e.participantLimit = 0 or e.confirmedRequests < e.participantLimit) ")
    Page<Event> findAllEventOnlyAvailableWithDate(String text, Boolean paid, List<Integer> categories, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("Select e from Event e where ((:text) IS NULL or (upper(e.annotation) like upper(concat('%',:text, '%'))" +
            " or upper(e.description) like upper(concat('%', :text, '%')))) " +
            "and e.state = 'PUBLISHED' " +
            "and ((:paid) IS NULL or e.paid = :paid) " +
            "and ((:categories) IS NULL or e.category.id in :categories) " +
            "and e.eventDate > :now  " +
            "and (e.participantLimit = 0 or e.confirmedRequests < e.participantLimit) ")
    Page<Event> findAllEventOnlyAvailableNotDate(String text, Boolean paid, List<Integer> categories, LocalDateTime now, Pageable pageable);

    @Query("Select e from Event e where ((:text) IS NULL or (upper(e.annotation) like upper(concat('%',:text, '%'))" +
            " or upper(e.description) like upper(concat('%', :text, '%')))) " +
            "and e.state = 'PUBLISHED' " +
            "and ((:paid) IS NULL or e.paid = :paid) " +
            "and ((:categories) IS NULL or e.category.id in :categories) " +
            "and e.eventDate between :start and :end ")
    Page<Event> findAllEventWithDate(String text, Boolean paid, List<Integer> categories, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("Select e from Event e where ((:text) IS NULL or (upper(e.annotation) like upper(concat('%',:text, '%'))" +
            " or upper(e.description) like upper(concat('%', :text, '%')))) " +
            "and e.state = 'PUBLISHED' " +
            "and ((:paid) IS NULL or e.paid = :paid) " +
            "and ((:categories) IS NULL or e.category.id in :categories) " +
            "and e.eventDate > :now  ")
    Page<Event> findAllEventNotDate(String text, Boolean paid, List<Integer> categories, LocalDateTime now, Pageable pageable);


    @Query("Select e from Event e where ((:users) IS NULL or e.initiator.id in :users) " +
            "and ((:stats) IS NULL or e.state in :stats) " +
            "and ((:categories) IS NULL or e.category.id in :categories) " +
            "and e.eventDate between :start and :end ")
    Page<Event> findAllByAdmin(List<Integer> users, List<State> stats, List<Integer> categories, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("Select e from Event e where ((:users) IS NULL or e.initiator.id in :users) " +
            "and ((:stats) IS NULL or e.state in :stats) " +
            "and ((:categories) IS NULL or e.category.id in :categories) " +
            "and (e.eventDate >= :now)")
    Page<Event> findAllByAdminNotDate(List<Integer> users, List<State> stats, List<Integer> categories, LocalDateTime now, Pageable pageable);


    Boolean existsByCategory_Id(Integer id);

    List<Event> findAllByIdInOrderByRateDesc(List<Integer> ids);

    @Query("Select e from Event e where ((:text) IS NULL or (upper(e.annotation) like upper(concat('%',:text, '%'))" +
            " or upper(e.description) like upper(concat('%', :text, '%')))) " +
            "and e.state = 'PUBLISHED' " +
            "and ((:paid) IS NULL or e.paid = :paid) " +
            "and ((:categories) IS NULL or e.category.id in :categories) " +
            "and e.eventDate between :start and :end " +
            "and (e.participantLimit = 0 or e.confirmedRequests < e.participantLimit) " +
            "ORDER BY e.rate DESC")
    Page<Event> findAllEventOnlyAvailableWithDateLike(String text, Boolean paid, List<Integer> categories, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("Select e from Event e where ((:text) IS NULL or (upper(e.annotation) like upper(concat('%',:text, '%'))" +
            " or upper(e.description) like upper(concat('%', :text, '%')))) " +
            "and e.state = 'PUBLISHED' " +
            "and ((:paid) IS NULL or e.paid = :paid) " +
            "and ((:categories) IS NULL or e.category.id in :categories) " +
            "and e.eventDate > :now  " +
            "and (e.participantLimit = 0 or e.confirmedRequests < e.participantLimit) " +
            "ORDER BY e.rate DESC")
    Page<Event> findAllEventOnlyAvailableNotDateLike(String text, Boolean paid, List<Integer> categories, LocalDateTime now, Pageable pageable);

    @Query("Select e from Event e where ((:text) IS NULL or (upper(e.annotation) like upper(concat('%',:text, '%'))" +
            " or upper(e.description) like upper(concat('%', :text, '%')))) " +
            "and e.state = 'PUBLISHED' " +
            "and ((:paid) IS NULL or e.paid = :paid) " +
            "and ((:categories) IS NULL or e.category.id in :categories) " +
            "and e.eventDate between :start and :end " +
            "ORDER BY e.rate DESC")
    Page<Event> findAllEventWithDateLike(String text, Boolean paid, List<Integer> categories, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("Select e from Event e where ((:text) IS NULL or (upper(e.annotation) like upper(concat('%',:text, '%'))" +
            " or upper(e.description) like upper(concat('%', :text, '%')))) " +
            "and e.state = 'PUBLISHED' " +
            "and ((:paid) IS NULL or e.paid = :paid) " +
            "and ((:categories) IS NULL or e.category.id in :categories) " +
            "and e.eventDate > :now  " +
            "ORDER BY e.rate DESC")
    Page<Event> findAllEventNotDateLike(String text, Boolean paid, List<Integer> categories, LocalDateTime now, Pageable pageable);


}
