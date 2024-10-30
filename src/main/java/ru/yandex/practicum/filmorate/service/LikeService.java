package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeStorage likeStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final EventStorage eventStorage;

    private final EventService eventService;

    public void addLike(Long filmId, Long userId) {
        eventService.createEvent(userId, EventType.LIKE, EventOperation.ADD, filmId);
        likeStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        eventService.createEvent(userId, EventType.LIKE, EventOperation.REMOVE, filmId);
        likeStorage.removeLike(filmId, userId);
    }

    public Collection<Film> getLikedFilmsByUserId(Long userId) {
        Collection<Film> films = likeStorage.getLikedFilmsByUserId(userId);
        Map<Long, Collection<Genre>> filmGenresMap = filmGenreStorage.getAllFilmGenres(films);
        films.forEach(film -> {
            Long filmId = film.getId();
            film.setGenres(filmGenresMap.getOrDefault(filmId, new ArrayList<>()));
            film.setLikes(likeStorage.getLikesFilmId(filmId));
        });
        return films;
    }
}
