package ru.yandex.practicum.filmorate.storage.reviewLike;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.ReviewLike;

import java.util.Optional;

public interface ReviewLikeStorage {

    Review addLike(int reviewId, int userId);

    Review addDislike(int reviewId, int userId);

    Review removeLike(int reviewId, int userId);

    Review removeDislike(int reviewId, int userId);

    Optional<ReviewLike> getReaction(int reviewId, int userId);
}
