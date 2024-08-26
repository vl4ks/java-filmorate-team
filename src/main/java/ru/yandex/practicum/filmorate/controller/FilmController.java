package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получение всех постов");
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Создание поста");
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ConditionsNotMetException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Описание не может быть больше 200 символов");
            throw new ConditionsNotMetException("Описание не может быть больше 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28)) || film.getReleaseDate().isAfter(LocalDate.now())) {
            log.error("Не корректная дата выхода фильма");
            throw new ConditionsNotMetException("Не корректная дата выхода фильма");
        }
        if (film.getDuration() < 0) {
            log.error("Длительность не может быть отрицательной");
            throw new ConditionsNotMetException("Длительность не может быть отрицательной");
        }
        film.setId(getNextFilmId());
        log.info("Добавили фильму id: {}", film.getId());
        film.setDuration(film.getDuration());
        films.put(film.getId(), film);
        log.info("Фильм добавлен, {}", film);
        log.info("{}", film.getDuration());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (film == null) {
            log.error("Фильм не может быть пустым");
            throw new NotFoundException("Фильм не может быть пустым");
        }
        log.info("Обновление поста");
        // проверяем необходимые условия
        if (film.getId() == 0 || film.getId() < 0) {
            log.error("Id не может быть меньше нуля");
            throw new ConditionsNotMetException("Id не может быть меньше нуля");
        }
        if (films.containsKey(film.getId())) {
            log.trace("Фильм с id = {} найден", film.getId());
            Film oldFilm = films.get(film.getId());
            log.trace("Обновляем фильм: {}", oldFilm);
            if (film.getName() == null || film.getName().isBlank()) {
                log.error("Имя не может быть пустым");
                throw new ConditionsNotMetException("Имя не может быть пустым");
            }
            if (film.getDuration() <= 0) {
                log.error("Длительность не может быть меньше или равной нулю");
                throw new ConditionsNotMetException("Длительность не может быть меньше или равной нулю");
            }
            oldFilm.setDescription(film.getDescription());
            oldFilm.setDuration(film.getDuration());
            oldFilm.setName(film.getName());
            oldFilm.setReleaseDate(film.getReleaseDate());
            log.info("Обновлено содержимое поста: {}", oldFilm);
            return oldFilm;
        }
        log.error("Фильм с id = {} не найден", film.getId());
        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
    }

    private long getNextFilmId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
