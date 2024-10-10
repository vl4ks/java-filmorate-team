package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final String GENRE = "select * from genres";

    @Override
    public Genre getGenreById(Integer genreId) {
        try {
            return jdbcTemplate.queryForObject(GENRE.concat(" where id = ?"), new GenreMapper(), genreId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return jdbcTemplate.query(GENRE.concat(" order by id asc"), new GenreMapper());
    }
}
