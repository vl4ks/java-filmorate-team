package ru.yandex.practicum.filmorate.storage.reviewLike;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.ReviewLike;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ReviewLikeMapper implements RowMapper<ReviewLike> {
    @Override
    public ReviewLike mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ReviewLike()
                .toBuilder()
                .id(rs.getLong("id"))
                .reaction(rs.getInt("reaction"))
                .reviewId(rs.getInt("review_id"))
                .userId(rs.getInt("user_id"))
                .build();
    }
}