package ru.netology.mvc.service;

import org.springframework.stereotype.Service;
import ru.netology.mvc.exception.NotFoundException;
import ru.netology.mvc.model.Post;
import ru.netology.mvc.repository.PostRepository;

import java.util.List;

/**
 * Сервисный слой CRUD-операций над постами.
 * <p>
 * Содержит бизнес-логику создания, чтения, обновления и удаления.
 * Контроллер делегирует все операции этому классу.
 */
@Service
public class PostService {

    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    /**
     * Возвращает все посты из хранилища.
     */
    public List<Post> all() {
        return repository.findAll();
    }

    /**
     * Возвращает пост по идентификатору или выбрасывает {@link NotFoundException}.
     */
    public Post getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пост с id=" + id + " не найден"));
    }

    /**
     * Создаёт новый пост (id = 0) или обновляет существующий.
     */
    public Post save(Post post) {
        if (post.getId() != 0 && repository.findById(post.getId()).isEmpty()) {
            throw new NotFoundException("Пост с id=" + post.getId() + " не найден");
        }

        Post saved = repository.save(post);
        if (saved == null) {
            throw new NotFoundException("Пост с id=" + post.getId() + " не найден");
        }
        return saved;
    }

    /**
     * Удаляет пост по идентификатору.
     */
    public void removeById(long id) {
        if (!repository.removeById(id)) {
            throw new NotFoundException("Пост с id=" + id + " не найден");
        }
    }
}
