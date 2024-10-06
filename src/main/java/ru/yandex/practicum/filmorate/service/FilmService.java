package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Slf4j
@Service("filmService")
public class FilmService {

    private InMemoryFilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addLikeToFilm(Film film, User user) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        if (film.getLikes().contains(user.getId())) {
            log.error("У пользователя {} уже есть лайк к этому фильму {}", user, film);
            throw new ConditionsNotMetException("У пользователя уже есть лайк к этому фильму");
        }
        log.info("Пользователь {} добавил лайк к фильму {}", user, film);
        film.getLikes().add(user.getId());
        return film;
    }

    public Film removeLikeFromFilm(Film film, User user) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        if (!film.getLikes().contains(user.getId())) {
            log.error("У пользователя {} нет лайка к этому фильму {}", user, film);
            throw new NotFoundException("У пользователя нет лайка к этому фильму");
        }
        log.info("Пользователь {} удалил лайк к фильму {}", user, film);
        film.getLikes().remove(user.getId());
        return film;
    }

    public Collection<Film> getTopFilmsLimited(int limit) {
        return filmStorage.getAllFilms().stream()
                .filter(film -> film.getLikes() != null)
                .filter(film -> !film.getLikes().isEmpty())
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public String removeFilm(Film film) {
        return filmStorage.removeFilm(film);
    }
}