# Проверка соответствия заданию «2.3. Spring Web MVC»

Дата проверки: 24.05.2026

## Задача Migration (`main`)

| Требование | Статус | Комментарий |
|------------|--------|-------------|
| Maven-проект | ✅ | `pom.xml`, Spring Boot 3.5.3 |
| Spring Web MVC + Embedded Tomcat | ✅ | `spring-boot-starter-web`, Tomcat 10.1 |
| Git-репозиторий с `.gitignore` | ✅ | Файл из репозитория Netology |
| `PostController` по спецификации лекции | ✅ | `/api/posts`, 4 метода CRUD |
| CRUD: создание (id=0) | ✅ | `PostRepository.save`, авто-increment id |
| CRUD: чтение списка и по id | ✅ | `GET /api/posts`, `GET /api/posts/{id}` |
| CRUD: обновление (id≠0) | ✅ | POST с существующим id |
| CRUD: удаление | ✅ | `DELETE /api/posts/{id}` |
| Потокобезопасный репозиторий | ✅ | `ConcurrentHashMap`, `AtomicLong` |
| NotFoundException → HTTP 404 | ✅ | `@ResponseStatus(NOT_FOUND)` |
| Документация в `Docs/` | ✅ | `Docs/06_mvc/README.md` |

## Задача «Данные не удаляются*» (`feature/soft-delete`)

| Требование | Статус | Комментарий |
|------------|--------|-------------|
| Pull Request к Migration | ✅ | [PR #1](https://github.com/OrionFLASH/Netology_DZ_Java_Spring_Web_MVC/pull/1) |
| Поле `removed` | ✅ | `Post.removed` |
| `removeById` — мягкое удаление | ✅ | `PostRepository.markRemoved` |
| `all` — без удалённых | ✅ | Фильтр в `PostService.all()` |
| `getById` — 404 для удалённых | ✅ | `NotFoundException` |
| `save` — 404 для удалённых | ✅ | Проверка `isRemoved()` в сервисе |
| Логика не в Controller | ✅ | Реализовано в `PostService` |
| Обоснование выбора слоя | ✅ | `SOFT_DELETE_RATIONALE.md` |

## Результаты автоматических тестов (`main`)

```
mvn clean test
Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

| Тест-класс | Тестов | Описание |
|------------|--------|----------|
| `PostRepositoryTest` | 5 | Создание, обновление, поиск, удаление |
| `PostApiIntegrationTest` | 8 | Полный CRUD-цикл через HTTP |

## Ручная проверка API

### Migration (`main`, порт 8080)

| Шаг | Команда | Ожидание | Результат |
|-----|---------|----------|-----------|
| 1 | `GET /api/posts` | `[]` | ✅ |
| 2 | `POST {"id":0,"content":"..."}` | id=1, content | ✅ |
| 3 | `GET /api/posts/1` | 200, JSON поста | ✅ |
| 4 | `POST {"id":1,"content":"updated"}` | content обновлён | ✅ |
| 5 | `DELETE /api/posts/1` | 200 | ✅ |
| 6 | `GET /api/posts/1` | 404 | ✅ |
| 7 | `POST {"id":999,...}` | 404 | ✅ |

### Soft Delete (`feature/soft-delete`, порт 8080)

| Шаг | Команда | Ожидание | Результат |
|-----|---------|----------|-----------|
| 1 | `POST {"id":0,"content":"test"}` | id=1, removed=false | ✅ |
| 2 | `DELETE /api/posts/1` | 200 | ✅ |
| 3 | `GET /api/posts` | `[]` (пост скрыт) | ✅ |
| 4 | `GET /api/posts/1` | 404 | ✅ |
| 5 | `POST {"id":1,"content":"..."}` | 404 | ✅ |

## Ссылки для сдачи

- **Репозиторий (Migration):** https://github.com/OrionFLASH/Netology_DZ_Java_Spring_Web_MVC
- **Pull Request (Soft Delete):** https://github.com/OrionFLASH/Netology_DZ_Java_Spring_Web_MVC/pull/1
