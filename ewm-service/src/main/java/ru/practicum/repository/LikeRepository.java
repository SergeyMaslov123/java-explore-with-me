package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Like;
import ru.practicum.model.LikePk;

public interface LikeRepository extends JpaRepository<Like, LikePk> {
    @Query("select count(l) from Like l where l.id.event_id = :eventId ")
    Double countByLikePk_event_id(Integer eventId);

    @Query("select count(l) from Like l where " +
            "l.id.event_id = :eventId and " +
            "l.likeEvent = :likeEvent")
    Double countByLikePk_event_idAndLikeEvent(Integer eventId, Boolean likeEvent);

@Query("select l from Like l where l.id.user_id = :userId and " +
        "((:likeEvent) IS NULL or l.likeEvent = :likeEvent)")
    Page<Like> findLikeByUser(Integer userId, Boolean likeEvent, Pageable pageable);

}
