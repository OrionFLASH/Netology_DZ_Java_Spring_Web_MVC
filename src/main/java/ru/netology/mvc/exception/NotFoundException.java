package ru.netology.mvc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, сигнализирующее об отсутствии запрошенного ресурса.
 * <p>
 * Аннотация {@link ResponseStatus} переводит исключение в HTTP-ответ 404 Not Found.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
