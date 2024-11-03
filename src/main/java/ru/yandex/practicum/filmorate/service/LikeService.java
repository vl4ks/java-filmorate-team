package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(LikeService.class);
    private final LikeStorage likeStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final EventStorage eventStorage;

    private final EventService eventService;

    public void addLike(Long filmId, Long userId) {
        log.info("Добавляем лайк фильму {}, от пользователя {}", filmId, userId);
        likeStorage.addLike(filmId, userId);
        eventService.createEvent(userId, EventType.LIKE, EventOperation.ADD, filmId);
        log.info("Лайк фильму {}, от пользователя {} добавлен", filmId, userId);

    }

    public void removeLike(Long filmId, Long userId) {
        log.info("Удаляем лайк фильму {}, от пользователя {}", filmId, userId);
        likeStorage.removeLike(filmId, userId);
        eventService.createEvent(userId, EventType.LIKE, EventOperation.REMOVE, filmId);
        log.info("Лайк фильму {}, от пользователя {} удален", filmId, userId);
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
