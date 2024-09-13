package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();


    @Override
    public Film addFilm(Film film) {
        if (film.getId() == 0) {
            log.info("Создание фильма");
            film.setId(getNextFilmId());
            log.info("Добавили фильму id: {}", film.getId());
            films.put(film.getId(), film);
            log.info("Фильм добавлен, {}", film);
            return film;
        }
        if (films.containsKey(film.getId())) {
            log.error("Фильм с таким id уже существует");
            throw new DuplicatedDataException("Фильм с таким id уже существует");
        } else {
            log.error("Неизвестная ошибка создания фильма");
            throw new RuntimeException("Неизвестная ошибка создания фильма");
        }

    }

    @Override
    public String removeFilm(Film film) {
        if (films.containsKey(film.getId())) {
            log.trace("удаление фильма с id: {}", film.getId());
            films.remove(film.getId());
            return "Фильм с id " + film.getId() + " был удален";
        }
        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновление фильма");
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
            oldFilm.setLikes(film.getLikes());
            log.info("Обновлено содержимое фильма: {}", oldFilm);
            return oldFilm;
        }
        log.error("Фильм с id = {} не найден", film.getId());
        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Получен запрос на получение всех фильмов");
        log.trace("Фильмы: {}", films.values());
        return films.values();
    }

    private long getNextFilmId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Film getFilmById(long id) {
        log.info("Получен запрос на получение фильма с id: {}", id);
        if (films.containsKey(id)) {
            log.trace("Пользоваетль с id: {} найден", id);
            return films.get(id);
        }
        log.error("Фильм с id ={} не найден", id);
        throw new NotFoundException("Фильм с id = " + id + " не найден");
    }

}
