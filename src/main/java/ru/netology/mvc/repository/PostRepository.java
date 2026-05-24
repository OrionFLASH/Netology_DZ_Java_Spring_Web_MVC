package ru.netology.mvc.repository;

import org.springframework.stereotype.Repository;
import ru.netology.mvc.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory репозиторий постов с поддержкой конкурентного доступа.
 * <p>
 * Данные хранятся в {@link ConcurrentHashMap}, счётчик идентификаторов —
 * в {@link AtomicLong}. Это обеспечивает потокобезопасность при одновременных
 * запросах к API из разных потоков встроенного Tomcat.
 */
@Repository
public class PostRepository {

    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    /**
     * Возвращает копию списка всех сохранённых постов.
     */
    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }

    /**
     * Ищет пост по идентификатору.
     */
    public Optional<Post> findById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    /**
     * Сохраняет новый пост (id = 0) или обновляет существующий.
     *
     * @return сохранённая копия поста с актуальным id
     */
    public Post save(Post post) {
        if (post.getId() == 0) {
            long newId = idGenerator.incrementAndGet();
            Post created = new Post(newId, post.getContent());
            posts.put(newId, created);
            return created;
        }

        Post existing = posts.get(post.getId());
        if (existing == null) {
            return null;
        }

        existing.setContent(post.getContent());
        return existing;
    }

    /**
     * Помечает пост как удалённый (мягкое удаление).
     *
     * @return {@code true}, если пост найден и помечен
     */
    public boolean markRemoved(long id) {
        Post post = posts.get(id);
        if (post == null) {
            return false;
        }
        post.setRemoved(true);
        return true;
    }

    /**
     * Физически удаляет пост из хранилища.
     *
     * @return {@code true}, если пост был найден и удалён
     */
    public boolean removeById(long id) {
        return posts.remove(id) != null;
    }
}
