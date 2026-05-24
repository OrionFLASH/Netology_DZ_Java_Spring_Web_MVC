package ru.netology.mvc.service;

import org.springframework.stereotype.Service;
import ru.netology.mvc.exception.NotFoundException;
import ru.netology.mvc.model.Post;
import ru.netology.mvc.repository.PostRepository;

import java.util.List;

/**
 * Сервисный слой CRUD-операций над постами.
 * <p>
 * Содержит бизнес-логику создания, чтения, обновления и мягкого удаления.
 * Правила видимости удалённых постов реализованы здесь, а не в контроллере:
 * клиентский API не должен возвращать или изменять помеченные записи.
 */
@Service
public class PostService {

    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    /**
     * Возвращает все активные (не удалённые) посты.
     */
    public List<Post> all() {
        return repository.findAll().stream()
                .filter(post -> !post.isRemoved())
                .toList();
    }

    /**
     * Возвращает активный пост по идентификатору.
     * Удалённые и отсутствующие посты приводят к {@link NotFoundException}.
     */
    public Post getById(long id) {
        return repository.findById(id)
                .filter(post -> !post.isRemoved())
                .orElseThrow(() -> new NotFoundException("Пост с id=" + id + " не найден"));
    }

    /**
     * Создаёт новый пост (id = 0) или обновляет существующий активный пост.
     */
    public Post save(Post post) {
        if (post.getId() != 0) {
            Post existing = repository.findById(post.getId())
                    .orElseThrow(() -> new NotFoundException("Пост с id=" + post.getId() + " не найден"));

            if (existing.isRemoved()) {
                throw new NotFoundException("Пост с id=" + post.getId() + " не найден");
            }
        }

        Post saved = repository.save(post);
        if (saved == null) {
            throw new NotFoundException("Пост с id=" + post.getId() + " не найден");
        }
        return saved;
    }

    /**
     * Помечает пост как удалённый (мягкое удаление).
     */
    public void removeById(long id) {
        Post existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пост с id=" + id + " не найден"));

        if (existing.isRemoved()) {
            throw new NotFoundException("Пост с id=" + id + " не найден");
        }

        if (!repository.markRemoved(id)) {
            throw new NotFoundException("Пост с id=" + id + " не найден");
        }
    }
}
