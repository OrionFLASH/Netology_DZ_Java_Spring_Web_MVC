package ru.netology.mvc;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.netology.mvc.model.Post;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Интеграционные тесты REST API /api/posts (задача Migration).
 * <p>
 * Проверяют полный CRUD-цикл и сценарии ошибок согласно заданию Netology.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/posts";
    }

    @Test
    @Order(1)
    void allReturnsEmptyListInitially() {
        ResponseEntity<List<Post>> response = restTemplate.exchange(
                baseUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @Order(2)
    void saveCreatesPostWithGeneratedId() {
        Post request = new Post(0, "Тестовый пост");

        ResponseEntity<Post> response = restTemplate.postForEntity(baseUrl(), request, Post.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        assertEquals("Тестовый пост", response.getBody().getContent());
    }

    @Test
    @Order(3)
    void getByIdReturnsExistingPost() {
        ResponseEntity<Post> response = restTemplate.getForEntity(baseUrl() + "/1", Post.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    @Order(4)
    void saveUpdatesExistingPost() {
        Post request = new Post(1, "Обновлённый текст");

        ResponseEntity<Post> response = restTemplate.postForEntity(baseUrl(), request, Post.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Обновлённый текст", response.getBody().getContent());
    }

    @Test
    @Order(5)
    void saveNonExistingPostReturns404() {
        Post request = new Post(999, "Не существует");

        ResponseEntity<Post> response = restTemplate.postForEntity(baseUrl(), request, Post.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(6)
    void getByIdForMissingPostReturns404() {
        ResponseEntity<Post> response = restTemplate.getForEntity(baseUrl() + "/999", Post.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(7)
    void removeByIdDeletesPost() {
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl() + "/1",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(8)
    void getByIdAfterDeleteReturns404() {
        ResponseEntity<Post> response = restTemplate.getForEntity(baseUrl() + "/1", Post.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
