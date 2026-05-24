package ru.netology.mvc.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.mvc.model.Post;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Модульные тесты in-memory репозитория постов.
 * <p>
 * Проверяют потокобезопасное хранение, генерацию id и CRUD-операции.
 */
class PostRepositoryTest {

    private PostRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PostRepository();
    }

    @Test
    void saveCreatesPostWithIncrementedId() {
        Post created = repository.save(new Post(0, "Первый"));

        assertEquals(1, created.getId());
        assertEquals("Первый", created.getContent());
    }

    @Test
    void saveUpdatesExistingPost() {
        Post created = repository.save(new Post(0, "Исходный"));
        Post updated = repository.save(new Post(created.getId(), "Новый"));

        assertEquals(created.getId(), updated.getId());
        assertEquals("Новый", updated.getContent());
    }

    @Test
    void findByIdReturnsPostWhenExists() {
        Post created = repository.save(new Post(0, "Пост"));

        Optional<Post> found = repository.findById(created.getId());

        assertTrue(found.isPresent());
        assertEquals("Пост", found.get().getContent());
    }

    @Test
    void findAllReturnsAllSavedPosts() {
        repository.save(new Post(0, "A"));
        repository.save(new Post(0, "B"));

        List<Post> all = repository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void removeByIdDeletesPost() {
        Post created = repository.save(new Post(0, "Удаляемый"));

        assertTrue(repository.removeById(created.getId()));
        assertFalse(repository.findById(created.getId()).isPresent());
    }
}
