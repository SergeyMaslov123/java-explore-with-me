package ru.practicum.rep;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ViewStat;
import ru.practicum.hit.Hit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RepositoryHitTest {
    @Autowired
    private RepositoryHit repositoryHit;
    Hit hit1 = new Hit(
            null,
            "yandex",
            "www",
            "192.168",
            LocalDateTime.now()
    );
    Hit hit2 = new Hit(
            null,
            "yandex",
            "www6",
            "192.168",
            LocalDateTime.now().minusSeconds(700)
    );
    Hit hit3 = new Hit(
            null,
            "yandex",
            "www6",
            "192.168",
            LocalDateTime.now().minusSeconds(600)
    );
    Hit hit4 = new Hit(
            null,
            "yandex5",
            "www8",
            "192.169",
            LocalDateTime.now().minusSeconds(500)
    );
    Hit hit5 = new Hit(
            null,
            "yandex9",
            "www9",
            "192.1699",
            LocalDateTime.now().minusSeconds(400)
    );
    Set<String> uris = Set.of("www", "www9");
    LocalDateTime start = LocalDateTime.now().minusHours(5);
    LocalDateTime end = LocalDateTime.now().minusSeconds(3);

    @BeforeEach
    public void addHit() {
        repositoryHit.save(hit2);
        repositoryHit.save(hit3);
        repositoryHit.save(hit4);
        repositoryHit.save(hit5);

    }

    @AfterEach
    public void deleteAll() {
        repositoryHit.deleteAll();
    }

    @Test
    void saveHit() {
        Hit actualHit = repositoryHit.save(hit1);

        assertEquals(5, actualHit.getId());
        assertEquals("yandex", actualHit.getApp());
    }

    @Test
    void getStats() {
        List<ViewStat> actualList = repositoryHit.getStats(start, end);

        assertEquals(3, actualList.size());
    }

    @Test
    void getUniqueStat() {
        List<ViewStat> actualList = repositoryHit.getUniqueStat(start, end);

        assertEquals(3, actualList.size());
    }

    @Test
    void getUniqueStatByUri() {
        List<ViewStat> actualList = repositoryHit.getUniqueStatByUri(start, end, uris);

        assertEquals(1, actualList.size());
    }

    @Test
    void getStatByUri() {
        List<ViewStat> actualList = repositoryHit.getStatByUri(start, end, uris);

        assertEquals(1, actualList.size());
    }

}