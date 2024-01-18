package ru.practicum.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.StatClient;
import ru.practicum.model.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LikeRepositoryTest {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;

    User user1 = new User(1, "Jon@ya.ru", "Jon Snow");
    Categories categories = new Categories(1, "cat1");
    Location location = new Location(1, 23.45, 35.67);
    Event event = new Event(1, "ddttt", categories, 2, LocalDateTime.now(), "desc", LocalDateTime.now().plusDays(3), user1, location, true, 3, LocalDateTime.now(), true, State.PUBLISHED, "title", 0, null);

    @BeforeEach
    private void addAll() {
        locationRepository.save(location);
        userRepository.save(user1);
        categoriesRepository.save(categories);
        eventRepository.save(event);
    }

    @AfterEach
    private void deleteAll() {
        likeRepository.deleteAll();
        eventRepository.deleteAll();
        categoriesRepository.deleteAll();
        userRepository.deleteAll();
        locationRepository.deleteAll();
    }

    @Test
    void countByLikePk_event_id() {
        Like like = new Like(new LikePk(1, 1), true);
        likeRepository.save(like);
        Double count = likeRepository.countByLikePk_event_id(1);

        assertEquals(1, count);
    }

    @Test
    void countByLikePk_event_idAndLikeEvent() {
    }

    @Test
    void findLikeByUser() {
    }
}