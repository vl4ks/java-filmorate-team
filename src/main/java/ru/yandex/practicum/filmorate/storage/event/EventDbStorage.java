package ru.yandex.practicum.filmorate.storage.event;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;
    private final EventMaker eventMaker;
    //private final ReviewService reviewService;
    //я у себя в проекте использовал NamedParameterJdbcOperations, очень удобная штука)

    @Override
    public void eventAddFilm(Film film) {
        eventMaker.makeFilmEvent("Film","Add",film);
    }

    @Override
    public void eventUpdateFilm(Film film) {
        eventMaker.makeFilmEvent("Film","Update",film);
    }

    @Override
    public void eventDeleteFilm(Film film) {
        eventMaker.makeFilmEvent("Film","Delete",film);
    }

    @Override
    public void eventAddUser(User user) {
        eventMaker.makeUserEvent("User","Add",user);
    }

    @Override
    public void eventUpdateUser(User user) {
        eventMaker.makeUserEvent("User","Update",user);
    }

    @Override
    public void eventDeleteUser(User user) {
        eventMaker.makeUserEvent("User","Delete",user);
    }

    @Override
    public void eventAddLike(Long film, Long user) {
        eventMaker.makeLikeEvent("Like","Add", film, user);
    }

    @Override
    public void eventDeleteLike(Long film, Long user) {
        eventMaker.makeLikeEvent("Like","Delete",film, user);
    }

    @Override
    public void eventAddReview(Review review) {
        eventMaker.makeReviewEvent("Review","Add",review);
    }

    @Override
    public void eventUpdateReview(Review review) {
        eventMaker.makeReviewEvent("Review","Update",review);
    }

    @Override
    public void eventDeleteReview(Long id) {
        final String sql = "SELECT * FROM reviews WHERE review_id = ?";
        Review review = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                Review.builder()
                        .reviewId(rs.getLong("review_id"))
                        .content(rs.getString("content"))
                        .isPositive(rs.getBoolean("is_positive"))
                        .userId(rs.getInt("user_id"))
                        .filmId(rs.getInt("film_id"))
                        .useful(rs.getInt("useful"))
                        .build()
        );
        eventMaker.makeReviewEvent("Review", "Delete", review);
    }


    @Override
    public Event getEvents(Long id) {
        final String sql = "select * from events where event_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new EventMapper());
    }
}
