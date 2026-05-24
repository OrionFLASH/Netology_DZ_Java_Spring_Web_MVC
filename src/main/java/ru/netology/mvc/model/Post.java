package ru.netology.mvc.model;

/**
 * Модель поста для in-memory CRUD API.
 * <p>
 * Поле {@code id} равно 0 при создании нового поста — сервис присваивает
 * идентификатор автоматически. Поле {@code content} содержит текст поста.
 */
public class Post {

    /** Уникальный идентификатор поста; 0 означает «ещё не сохранён». */
    private long id;

    /** Текстовое содержимое поста. */
    private String content;

    public Post() {
    }

    public Post(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
