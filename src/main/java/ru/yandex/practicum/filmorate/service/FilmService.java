package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service("filmService")
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final LikeService likeService;
    private final EventService eventService;

    public void addLikeToFilm(Film film, User user) {
        likeStorage.addLike(film.getId(), user.getId());
        Film updatedFilm = filmStorage.getFilmByFilmId(film.getId());
        filmStorage.updateFilm(updatedFilm);
        eventService.createEvent(user.getId(), EventType.LIKE, EventOperation.ADD, film.getId());
    }

    public boolean removeLike(Long filmId, Long userId) {
        eventService.createEvent(userId, EventType.LIKE, EventOperation.REMOVE, filmId);
        return likeStorage.removeLike(filmId, userId);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmByFilmId(id);
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

    public Collection<Film> searchFilms(String query, Collection<String> searchDir) {
        return filmStorage.searchFilms(query, searchDir);
    }

    public Collection<Film> getPopularFilmsByGenreAndYear(int count, String genreId, String year) {
        var result = filmStorage.getPopularFilmsByGenreAndYear(count, genreId, year);
        return result;
    }


    public Collection<Film> getCommonFilms(Long userId, Long friendId) {
        Collection<Film> userFilms = likeService.getLikedFilmsByUserId(userId);
        Collection<Film> friendFilms = likeService.getLikedFilmsByUserId(friendId);

        return userFilms.stream()
                .filter(friendFilms::contains)
                .sorted((f1, f2) -> Integer.compare(f2.getRate(), f1.getRate()))
                .collect(Collectors.toList());
    }

    public Collection<Film> getDirectorFilms(Integer directorId, String sortBy) {
        Collection<Film> films = filmStorage.getDirectorFilms(directorId, sortBy);

        if (films.isEmpty()) {
            throw new NotFoundException(String.format("Фильма с id %s нет", directorId));
        }

        return films;
    }

    public Collection<Film> getRecomendations(Long userId) {
        return filmStorage.getUserRecommendations(userId);
    }
}