# Netology — Spring Web MVC (ДЗ 2.3)

Учебный Maven-проект на **Spring Boot 3** с встроенным Tomcat. Реализован in-memory CRUD API для постов — миграция функциональности с сервлетов на Spring Web MVC.

## Ссылки для сдачи

| Задача | Ссылка |
|--------|--------|
| **Migration** | https://github.com/OrionFLASH/Netology_DZ_Java_Spring_Web_MVC |
| **Данные не удаляются*** | https://github.com/OrionFLASH/Netology_DZ_Java_Spring_Web_MVC/pull/1 |

## Задачи

| Задача | Ветка | Описание |
|--------|-------|----------|
| **Migration** | `main` | REST API `/api/posts` — создание, чтение, обновление, удаление постов |
| **Данные не удаляются*** | `feature/soft-delete` | Мягкое удаление через флаг `removed` (Pull Request) |

Исходная формулировка задания — в каталоге [`Docs/06_mvc/`](Docs/06_mvc/README.md).  
Отчёт о проверке соответствия — [`Docs/06_mvc/VERIFICATION.md`](Docs/06_mvc/VERIFICATION.md).

## Структура проекта

```
├── Docs/06_mvc/          # Исходное задание, проверка, обоснование soft delete
├── pom.xml               # Maven-сборка, Spring Boot 3.5.3
├── src/main/java/ru/netology/mvc/
│   ├── Main.java         # Точка входа, встроенный Tomcat
│   ├── controller/       # PostController — REST-слой
│   ├── service/          # PostService — бизнес-логика
│   ├── repository/       # PostRepository — in-memory хранилище
│   ├── model/            # Post — модель данных
│   └── exception/        # NotFoundException → HTTP 404
└── src/test/java/        # Автоматические тесты
```

## Требования

- Java 17+
- Maven 3.6+

## Установка и запуск

```bash
# Сборка и тесты
mvn clean test

# Сборка JAR
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

Ветка `feature/soft-delete`, [Pull Request #1](https://github.com/OrionFLASH/Netology_DZ_Java_Spring_Web_MVC/pull/1):

- `DELETE` помечает пост флагом `removed`, не удаляя физически.
- `GET` и `POST` не видят помеченные посты (404 для get/update).
- Логика реализована в **PostService**, контроллер без изменений.

Обоснование выбора слоя — [`Docs/06_mvc/SOFT_DELETE_RATIONALE.md`](Docs/06_mvc/SOFT_DELETE_RATIONALE.md) (в ветке `feature/soft-delete`).

## Тестирование

### Автоматические тесты

```bash
mvn clean test
```

| Класс | Тестов | Покрытие |
|-------|--------|----------|
| `PostRepositoryTest` | 5 | Репозиторий: create, update, find, remove |
| `PostApiIntegrationTest` | 8 | HTTP CRUD: полный цикл и ошибки 404 |

**Результат последней проверки:** 13 тестов, 0 ошибок, `BUILD SUCCESS`.

### Ручная проверка

1. Запустить JAR: `java -jar target/spring-web-mvc-1.0.0-SNAPSHOT.jar`
2. Выполнить curl-команды из раздела «Примеры запросов».
3. Убедиться в корректных JSON-ответах и кодах HTTP.

Подробная матрица проверки — в [`Docs/06_mvc/VERIFICATION.md`](Docs/06_mvc/VERIFICATION.md).

## Соответствие заданию

| Критерий | Migration | Soft Delete |
|----------|-----------|-------------|
| Maven + Spring MVC + Tomcat | ✅ | ✅ |
| PostController по лекции | ✅ | ✅ (без изменений) |
| CRUD in-memory | ✅ | ✅ |
| Потокобезопасный репозиторий | ✅ | ✅ |
| NotFoundException → 404 | ✅ | ✅ |
| Pull Request | — | ✅ PR #1 |
| Логика не в Controller | ✅ | ✅ PostService |

## История версий

| Версия | Описание |
|--------|----------|
| 1.0.0 | Migration — CRUD на Spring Web MVC |
| 1.0.1 | Автотесты, отчёт о проверке соответствия |
| 1.1.0 | Soft delete — флаг `removed` (ветка `feature/soft-delete`) |
