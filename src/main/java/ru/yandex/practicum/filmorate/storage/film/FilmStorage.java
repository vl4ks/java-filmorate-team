package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    String removeFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getAllFilms();

    Film getFilmByFilmId(Long id);

    Collection<Film> getPopularFilmsByGenreAndYear(int count, String genreId, String year);

    Collection<Film> getDirectorFilms(Integer directorId, String sortBy);

    Collection<Film> searchFilms(String query, List<String> searchDir);

    Collection<Film> getUserRecommendations(Long userId);
}
