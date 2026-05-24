# Netology — Spring Web MVC (ДЗ 2.3)

Учебный Maven-проект на **Spring Boot 3** с встроенным Tomcat. Реализован in-memory CRUD API для постов — миграция функциональности с сервлетов на Spring Web MVC.

## Задачи

| Задача | Ветка | Описание |
|--------|-------|----------|
| **Migration** | `main` | REST API `/api/posts` — создание, чтение, обновление, удаление постов |
| **Данные не удаляются*** | `feature/soft-delete` | Мягкое удаление через флаг `removed` (Pull Request) |

Исходная формулировка задания — в каталоге [`Docs/06_mvc/`](Docs/06_mvc/README.md).

## Структура проекта

```
├── Docs/06_mvc/          # Исходное задание Netology
├── pom.xml               # Maven-сборка, Spring Boot 3.5.3
└── src/main/java/ru/netology/mvc/
    ├── Main.java         # Точка входа, встроенный Tomcat
    ├── controller/       # PostController — REST-слой
    ├── service/          # PostService — бизнес-логика
    ├── repository/       # PostRepository — in-memory хранилище
    ├── model/            # Post — модель данных
    └── exception/        # NotFoundException → HTTP 404
```

## Требования

- Java 17+
- Maven 3.6+

## Установка и запуск

```bash
# Сборка
mvn clean package

# Запуск
java -jar target/spring-web-mvc-1.0.0-SNAPSHOT.jar
```

Приложение стартует на `http://localhost:8080`.

Альтернатива через Maven:

```bash
mvn spring-boot:run
```

## API — Migration (`main`)

Базовый URL: `http://localhost:8080/api/posts`

| Метод | URL | Описание |
|-------|-----|----------|
| `GET` | `/api/posts` | Список всех постов |
| `GET` | `/api/posts/{id}` | Пост по id (404, если не найден) |
| `POST` | `/api/posts` | Создание (`id: 0`) или обновление |
| `DELETE` | `/api/posts/{id}` | Удаление поста |

### Примеры запросов

```bash
# Создать пост
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{"id":0,"content":"Привет, Spring MVC!"}'

# Получить все посты
curl http://localhost:8080/api/posts

# Получить пост по id
curl http://localhost:8080/api/posts/1

# Обновить пост
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{"id":1,"content":"Обновлённый текст"}'

# Удалить пост
curl -X DELETE http://localhost:8080/api/posts/1
```

### Ожидаемое поведение

1. **Создание** — POST с `"id": 0` возвращает пост с автоматически присвоенным id.
2. **Обновление** — POST с существующим id обновляет `content`.
3. **Обновление несуществующего** — HTTP 404.
4. **Удаление** — пост исчезает из списка; повторный GET по id → 404.

## API — Soft Delete (`feature/soft-delete`)

После merge ветки `feature/soft-delete`:

- `DELETE` помечает пост флагом `removed`, не удаляя физически.
- `GET` и `POST` не видят помеченные посты (404 для get/update).

Подробности и обоснование выбора слоя — в описании Pull Request.

## Тестирование

1. Собрать проект: `mvn clean package`.
2. Запустить JAR и выполнить curl-команды из раздела выше.
3. Убедиться, что CRUD-операции возвращают ожидаемые JSON и коды ответов.

## История версий

| Версия | Описание |
|--------|----------|
| 1.0.0 | Migration — CRUD на Spring Web MVC |
| 1.1.0 | Soft delete — флаг `removed` (ветка `feature/soft-delete`) |
