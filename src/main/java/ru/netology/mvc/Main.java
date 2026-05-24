package ru.netology.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа Spring Boot-приложения с встроенным Tomcat.
 * <p>
 * Аннотация {@link SpringBootApplication} включает автоконфигурацию Spring MVC
 * и component-scan для пакета {@code ru.netology.mvc} и его подпакетов.
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
