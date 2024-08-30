package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

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
        log.trace("Получен запрос на получение всех фильмов");
        log.trace("Фильмы: {}", films.values());
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Создание фильма");
        film.setId(getNextFilmId());
        log.info("Добавили фильму id: {}", film.getId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен, {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
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
            oldFilm.setDescription(film.getDescription());
            oldFilm.setDuration(film.getDuration());
            oldFilm.setName(film.getName());
            oldFilm.setReleaseDate(film.getReleaseDate());
            log.info("Обновлено содержимое фильма: {}", oldFilm);
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
