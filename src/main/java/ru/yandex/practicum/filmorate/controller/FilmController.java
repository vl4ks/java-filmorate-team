package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private InMemoryFilmStorage filmStorage;
    private FilmService filmService;
    private InMemoryUserStorage userStorage;


    public FilmController(InMemoryFilmStorage filmStorage, FilmService filmService, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.userStorage = userStorage;
    }


    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable Long id) {
        return filmStorage.getFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getTopFilmsLimited(@RequestParam(required = false, defaultValue = "10") String count) {
        return filmService.getTopFilmsLimited(Integer.parseInt(count));
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLikeToFilm(filmStorage.getFilmById(id), userStorage.getUserById(userId));
        log.info("Пользователь {} лайкнул фильм {}", userId, id);
        return filmStorage.getFilmById(id);
    }

    @DeleteMapping
    public String delete(@Valid @RequestBody Film film) {
        return filmStorage.removeFilm(film);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film unlikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLikeFromFilm(filmStorage.getFilmById(id), userStorage.getUserById(userId));
        log.info("Пользователь {} убрал лайк с фильма {}", userId, id);
        return filmStorage.getFilmById(id);
    }
}
